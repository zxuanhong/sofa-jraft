/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.jraft.storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import com.alipay.sofa.jraft.rpc.RpcRequests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.error.RetryAgainException;
import com.alipay.sofa.jraft.rpc.RpcRequestClosure;
import com.alipay.sofa.jraft.rpc.RpcRequests.GetFileRequest;
import com.alipay.sofa.jraft.rpc.RpcRequests.GetFileResponse;
import com.alipay.sofa.jraft.storage.io.FileReader;
import com.alipay.sofa.jraft.util.BufferUtils;
import com.alipay.sofa.jraft.util.ByteBufferCollector;
import com.alipay.sofa.jraft.util.OnlyForTest;
import com.alipay.sofa.jraft.util.RpcFactoryHelper;
import com.alipay.sofa.jraft.util.Utils;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.google.protobuf.ZeroByteStringHelper;

/**
 * File reader service.
 *
 * @author boyan (boyan@alibaba-inc.com)
 *
 * 2018-Mar-30 10:23:13 AM
 */
public final class FileService {

    private static final Logger                   LOG           = LoggerFactory.getLogger(FileService.class);

    private static final FileService              INSTANCE      = new FileService();

    private final ConcurrentMap<Long, FileReader> fileReaderMap = new ConcurrentHashMap<>();
    private final AtomicLong                      nextId        = new AtomicLong();

    /**
     * Retrieve the singleton instance of FileService.
     *
     * @return a fileService instance
     */
    public static FileService getInstance() {
        return INSTANCE;
    }

    @OnlyForTest
    void clear() {
        this.fileReaderMap.clear();
    }

    private FileService() {
        final long processId = Utils.getProcessId(ThreadLocalRandom.current().nextLong(10000, Integer.MAX_VALUE));
        final long initialValue = Math.abs(processId << 45 | System.nanoTime() << 17 >> 17);
        this.nextId.set(initialValue);
        LOG.info("Initial file reader id in FileService is {}", initialValue);
    }

    /**
     * Handle GetFileRequest, run the response or set the response with done.
     */
    public RpcRequests.GetFileResponse handleGetFile(final GetFileRequest request) {
        GetFileResponse.Builder responseBuilder = GetFileResponse.newBuilder();
        RpcRequests.ErrorResponse.Builder errorBuilder = RpcRequests.ErrorResponse.newBuilder();
        if (request.getCount() <= 0 || request.getOffset() < 0) {
            errorBuilder.setErrorCode(RaftError.EREQUEST.getNumber())
                    .setErrorMsg(String.format("Invalid request: %s", request));
            return responseBuilder.setErrorResponse(errorBuilder.build()).build();
        }
        final FileReader reader = this.fileReaderMap.get(request.getReaderId());
        if (reader == null) {
            errorBuilder.setErrorCode(RaftError.ENOENT.getNumber())
                    .setErrorMsg(String.format("Fail to find reader=%d",
                            request.getReaderId()));
            return responseBuilder.setErrorResponse(errorBuilder.build()).build();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("GetFile path={} filename={} offset={} count={}",
                reader.getPath(), request.getFilename(), request.getOffset(), request.getCount());
        }

        final ByteBufferCollector dataBuffer = ByteBufferCollector.allocate();
        try {
            final int read = reader
                .readFile(dataBuffer, request.getFilename(), request.getOffset(), request.getCount());
            responseBuilder.setReadSize(read);
            responseBuilder.setEof(read == FileReader.EOF);
            final ByteBuffer buf = dataBuffer.getBuffer();
            BufferUtils.flip(buf);
            if (!buf.hasRemaining()) {
                // skip empty data
                responseBuilder.setData(ByteString.EMPTY);
            } else {
                // TODO check hole
                responseBuilder.setData(ZeroByteStringHelper.wrap(buf));
            }
            return responseBuilder.build();
        } catch (final RetryAgainException e) {
            errorBuilder.setErrorCode(RaftError.EAGAIN.getNumber())
                    .setErrorMsg(String.format("Fail to read from path=%s filename=%s with error: %s", reader.getPath(), request.getFilename(),
                            e.getMessage()));
            return responseBuilder.setErrorResponse(errorBuilder.build()).build();
        } catch (final IOException e) {
            LOG.error("Fail to read file path={} filename={}", reader.getPath(), request.getFilename(), e);
            errorBuilder.setErrorCode(RaftError.EIO.getNumber())
                    .setErrorMsg(String.format("Fail to read from path=%s filename=%s", reader.getPath(), request.getFilename()));
            return responseBuilder.setErrorResponse(errorBuilder.build()).build();
        }
    }

    /**
     * Adds a file reader and return it's generated readerId.
     */
    public long addReader(final FileReader reader) {
        final long readerId = this.nextId.getAndIncrement();
        if (this.fileReaderMap.putIfAbsent(readerId, reader) == null) {
            return readerId;
        } else {
            return -1L;
        }
    }

    /**
     * Remove the reader by readerId.
     */
    public boolean removeReader(final long readerId) {
        return this.fileReaderMap.remove(readerId) != null;
    }
}
