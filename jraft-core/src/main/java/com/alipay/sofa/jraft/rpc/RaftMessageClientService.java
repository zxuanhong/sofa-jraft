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
package com.alipay.sofa.jraft.rpc;

import com.alipay.sofa.jraft.util.Endpoint;
import com.google.protobuf.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Raft client RPC service.
 *
 * @author boyan (boyan@alibaba-inc.com)
 *
 * 2018-Apr-08 5:59:52 PM
 */
public interface RaftMessageClientService {

    boolean isConnected(final Endpoint endpoint);

    void isClosed();

     boolean checkConnection(final Endpoint endpoint, final boolean createIfAbsent) ;


    /**
     * Sends a pre-vote request and handle the response with done.
     *
     * @param endpoint destination address (ip, port)
     * @param request  request data
     * @return a future with result
     */
    CompletableFuture<RpcRequests.RequestVoteResponse> preVote(final Endpoint endpoint, final RpcRequests.RequestVoteRequest request);

    /**
     * Sends a request-vote request and handle the response with done.
     *
     * @param endpoint destination address (ip, port)
     * @param request  request data
     * @return a future with result
     */
    CompletableFuture<RpcRequests.RequestVoteResponse> requestVote(final Endpoint endpoint, final RpcRequests.RequestVoteRequest request);

    /**
     * Sends a append-entries request and handle the response with done.
     *
     * @param endpoint destination address (ip, port)
     * @param request  request data
     * @return a future with result
     */
    CompletableFuture<RpcRequests.AppendEntriesResponse> appendEntries(final Endpoint endpoint, final RpcRequests.AppendEntriesRequest request,
                                  final int timeoutMs);

    /**
     * Sends a install-snapshot request and handle the response with done.
     *
     * @param endpoint destination address (ip, port)
     * @param request  request data
     * @param done     callback
     * @return a future result
     */
    CompletableFuture<RpcRequests.InstallSnapshotResponse> installSnapshot(final Endpoint endpoint, final RpcRequests.InstallSnapshotRequest request,
                                    final RpcRequests.InstallSnapshotResponse done);

    /**
     * Get a piece of file data by GetFileRequest, and handle the response with done.
     *
     * @param endpoint  destination address (ip, port)
     * @param request   request data
     * @param timeoutMs timeout millis
     * @return a future result
     */
    CompletableFuture<RpcRequests.GetFileResponse> getFile(final Endpoint endpoint, final RpcRequests.GetFileRequest request, final int timeoutMs);

    /**
     * Send a timeout-now request and handle the response with done.
     *
     * @param endpoint  destination address (ip, port)
     * @param request   request data
     * @param timeoutMs timeout millis
     * @return a future result
     */
    CompletableFuture<RpcRequests.TimeoutNowResponse> timeoutNow(final Endpoint endpoint, final RpcRequests.TimeoutNowRequest request,
                               final int timeoutMs);

    /**
     * Send a read-index request and handle the response with done.
     *
     * @param endpoint  destination address (ip, port)
     * @param request   request data
     * @param timeoutMs timeout millis
     * @return a future result
     */
    CompletableFuture<RpcRequests.ReadIndexResponse> readIndex(final Endpoint endpoint, final RpcRequests.ReadIndexRequest request, final int timeoutMs);
}
