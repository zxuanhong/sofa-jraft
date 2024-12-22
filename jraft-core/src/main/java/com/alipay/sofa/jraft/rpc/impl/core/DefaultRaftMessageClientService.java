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
package com.alipay.sofa.jraft.rpc.impl.core;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.ReplicatorGroup;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.option.RpcOptions;
import com.alipay.sofa.jraft.rpc.*;
import com.alipay.sofa.jraft.rpc.RpcRequests.*;
import com.alipay.sofa.jraft.rpc.impl.AbstractClientService;
import com.alipay.sofa.jraft.rpc.impl.FutureImpl;
import com.alipay.sofa.jraft.storage.FileService;
import com.alipay.sofa.jraft.util.Endpoint;
import com.alipay.sofa.jraft.util.Utils;
import com.alipay.sofa.jraft.util.concurrent.DefaultFixedThreadsExecutorGroupFactory;
import com.alipay.sofa.jraft.util.concurrent.FixedThreadsExecutorGroup;
import com.anyilanxin.kunpeng.atomix.cluster.Member;
import com.anyilanxin.kunpeng.atomix.cluster.MemberId;
import com.anyilanxin.kunpeng.atomix.cluster.PartitionManagementService;
import com.anyilanxin.kunpeng.atomix.cluster.messaging.ClusterCommunicationService;
import com.anyilanxin.kunpeng.atomix.cluster.messaging.MessagingService;
import com.anyilanxin.kunpeng.atomix.utils.net.Address;
import com.anyilanxin.kunpeng.util.concurrent.SingleThreadContext;
import com.anyilanxin.kunpeng.util.concurrent.ThreadContext;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Raft rpc service based bolt.
 *
 * @author boyan (boyan@alibaba-inc.com)
 * @author jiachun.fjc
 */
public class DefaultRaftMessageClientService implements RaftMessageClientService {
    private NodeOptions              nodeOptions;
    private final ReplicatorGroup    rgGroup;
    final PartitionManagementService partitionManagementService;
    private final RaftServerService  raftServerService;
    private final MessagingService   messagingService;
    ThreadContext                    threadContext = new SingleThreadContext("sdfsdfsdf-%d");

    @Override
    public boolean isConnected(Endpoint endpoint) {
        Member member = partitionManagementService.getMembershipService().getMember(
            Address.from(endpoint.getIp(), endpoint.getPort()));
        return member.isActive();
    }

    @Override
    public boolean checkConnection(Endpoint endpoint, boolean createIfAbsent) {
        return isConnected(endpoint);
    }

    public DefaultRaftMessageClientService(final ReplicatorGroup rgGroup, final NodeOptions nodeOptions,
                                           final PartitionManagementService partitionManagementService,
                                           final RaftServerService raftServerService) {
        this.rgGroup = rgGroup;
        this.nodeOptions = nodeOptions;
        this.partitionManagementService = partitionManagementService;
        this.raftServerService = raftServerService;
        this.messagingService = partitionManagementService.getMessagingService();
        register();
    }

    @Override
    public void isClosed() {
        unregister();
    }

    private void unregister() {
        messagingService.unregisterHandler("preVote");
        messagingService.unregisterHandler("requestVote");
        messagingService.unregisterHandler("appendEntries");
        messagingService.unregisterHandler("installSnapshot");
        messagingService.unregisterHandler("getFile");
        messagingService.unregisterHandler("timeoutNow");
        messagingService.unregisterHandler("readIndex");
    }

    private void register() {
        messagingService.registerHandler("preVote", this::handlePreVoteRequest, threadContext);
        messagingService.registerHandler("requestVote", this::handleRequestVote, threadContext);
        messagingService.registerHandler("appendEntries", this::handleAppendEntries, threadContext);
        messagingService.registerHandler("installSnapshot", this::handleInstallSnapshot, threadContext);
        messagingService.registerHandler("getFile", this::handleGetFile, threadContext);
        messagingService.registerHandler("timeoutNow", this::handleTimeoutNow, threadContext);
        messagingService.registerHandler("readIndex", this::handleReadIndex, threadContext);
    }

    @Override
    public CompletableFuture<RequestVoteResponse> preVote(Endpoint endpoint, RequestVoteRequest request) {
        CompletableFuture<RequestVoteResponse> future = new CompletableFuture<>();
        messagingService.sendAndReceive(
                Address.from(endpoint.getIp(), endpoint.getPort()),
                "preVote",
                request.toByteArray()).whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
            } else {
                try {
                    future.complete(RequestVoteResponse.parseFrom(bytes));
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return future;
    }

    byte[] handlePreVoteRequest(Address address, byte[] request) {
        try {
            RequestVoteResponse requestVoteResponse = raftServerService.handlePreVoteRequest(RequestVoteRequest
                .parseFrom(request));
            return requestVoteResponse.toByteArray();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<RequestVoteResponse> requestVote(Endpoint endpoint, RequestVoteRequest request) {
        CompletableFuture<RequestVoteResponse> future = new CompletableFuture<>();
        messagingService.sendAndReceive(
                Address.from(endpoint.getIp(), endpoint.getPort()),
                "requestVote",
                request.toByteArray()).whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
            } else {
                try {
                    future.complete(RequestVoteResponse.parseFrom(bytes));
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return future;
    }

    byte[] handleRequestVote(Address address, byte[] request) {
        try {
            RequestVoteResponse requestVoteResponse = raftServerService.handleRequestVoteRequest(RequestVoteRequest
                .parseFrom(request));
            return requestVoteResponse.toByteArray();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<AppendEntriesResponse> appendEntries(Endpoint endpoint, AppendEntriesRequest request, int timeoutMs) {
        CompletableFuture<AppendEntriesResponse> future = new CompletableFuture<>();
        messagingService.sendAndReceive(
                Address.from(endpoint.getIp(), endpoint.getPort()),
                "appendEntries",
                request.toByteArray()).whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
            } else {
                try {
                    future.complete(AppendEntriesResponse.parseFrom(bytes));
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return future;
    }

    byte[] handleAppendEntries(Address address, byte[] request) {
        try {
            AppendEntriesResponse requestVoteResponse = raftServerService
                .handleAppendEntriesRequest(AppendEntriesRequest.parseFrom(request));
            return requestVoteResponse.toByteArray();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<InstallSnapshotResponse> installSnapshot(Endpoint endpoint, InstallSnapshotRequest request) {
        CompletableFuture<InstallSnapshotResponse> future = new CompletableFuture<>();
        messagingService.sendAndReceive(
                Address.from(endpoint.getIp(), endpoint.getPort()),
                "installSnapshot",
                request.toByteArray()).whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
            } else {
                try {
                    future.complete(InstallSnapshotResponse.parseFrom(bytes));
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return future;
    }

    byte[] handleInstallSnapshot(Address address, byte[] request) {
        try {
            InstallSnapshotResponse response = raftServerService.handleInstallSnapshot(InstallSnapshotRequest
                .parseFrom(request));
            return response.toByteArray();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<GetFileResponse> getFile(Endpoint endpoint, GetFileRequest request, int timeoutMs) {
        CompletableFuture<GetFileResponse> future = new CompletableFuture<>();
        messagingService.sendAndReceive(
                Address.from(endpoint.getIp(), endpoint.getPort()),
                "getFile",
                request.toByteArray()).whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
            } else {
                try {
                    future.complete(GetFileResponse.parseFrom(bytes));
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return future;
    }

    byte[] handleGetFile(Address address, byte[] request) {
        try {
            GetFileResponse response = FileService.getInstance().handleGetFile(GetFileRequest.parseFrom(request));
            return response.toByteArray();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<TimeoutNowResponse> timeoutNow(Endpoint endpoint, TimeoutNowRequest request, int timeoutMs) {
        CompletableFuture<TimeoutNowResponse> future = new CompletableFuture<>();
        messagingService.sendAndReceive(
                Address.from(endpoint.getIp(), endpoint.getPort()),
                "timeoutNow",
                request.toByteArray()).whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
            } else {
                try {
                    future.complete(TimeoutNowResponse.parseFrom(bytes));
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return future;
    }

    byte[] handleTimeoutNow(Address address, byte[] request) {
        try {
            TimeoutNowResponse response = raftServerService.handleTimeoutNowRequest(TimeoutNowRequest
                .parseFrom(request));
            return response.toByteArray();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<ReadIndexResponse> readIndex(Endpoint endpoint, ReadIndexRequest request, int timeoutMs) {
        CompletableFuture<ReadIndexResponse> future = new CompletableFuture<>();
        messagingService.sendAndReceive(
                Address.from(endpoint.getIp(), endpoint.getPort()),
                "readIndex",
                request.toByteArray()).whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
            } else {
                try {
                    future.complete(ReadIndexResponse.parseFrom(bytes));
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return future;
    }

    byte[] handleReadIndex(Address address, byte[] request) {
        try {
            ReadIndexResponse response = raftServerService.handleReadIndexRequest(ReadIndexRequest.parseFrom(request));
            return response.toByteArray();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
