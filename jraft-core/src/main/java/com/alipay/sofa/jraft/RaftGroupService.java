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
package com.alipay.sofa.jraft;

import com.anyilanxin.kunpeng.atomix.cluster.messaging.MessagingService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.option.RpcOptions;
import com.alipay.sofa.jraft.rpc.ProtobufMsgFactory;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.alipay.sofa.jraft.util.Endpoint;
import com.alipay.sofa.jraft.util.Utils;

/**
 * A framework to implement a raft group service.
 *
 * @author boyan (boyan@alibaba-inc.com)
 * <p>
 * 2018-Apr-08 7:53:03 PM
 */
public class RaftGroupService {

    private static final Logger LOG     = LoggerFactory.getLogger(RaftGroupService.class);

    static {
        ProtobufMsgFactory.load();
    }

    private volatile boolean    started = false;

    /**
     * This node serverId
     */
    private PeerId              serverId;

    /**
     * Node options
     */
    private NodeOptions         nodeOptions;

    private MessagingService    rpcServer;

    /**
     * If we want to share the rpcServer instance, then we can't stop it when shutdown.
     */
    private final boolean       sharedRpcServer;

    /**
     * The raft group id
     */
    private String              groupId;
    /**
     * The raft node.
     */
    private Node                node;

    public RaftGroupService(final String groupId, final PeerId serverId, final NodeOptions nodeOptions,
                            final MessagingService messagingService) {
        this(groupId, serverId, nodeOptions, messagingService, false);
    }

    public RaftGroupService(final String groupId, final PeerId serverId, final NodeOptions nodeOptions,
                            final MessagingService messagingService, final boolean sharedRpcServer) {
        super();
        this.groupId = groupId;
        this.serverId = serverId;
        this.nodeOptions = nodeOptions;
        this.rpcServer = messagingService;
        this.sharedRpcServer = sharedRpcServer;
    }

    public synchronized Node getRaftNode() {
        return this.node;
    }

    /**
     * Starts the raft group service, returns the raft node.
     *
     */
    public synchronized Node start() {
        if (this.started) {
            return this.node;
        }
        if (this.serverId == null || this.serverId.getEndpoint() == null
            || this.serverId.getEndpoint().equals(new Endpoint(Utils.IP_ANY, 0))) {
            throw new IllegalArgumentException("Blank serverId:" + this.serverId);
        }
        if (StringUtils.isBlank(this.groupId)) {
            throw new IllegalArgumentException("Blank group id:" + this.groupId);
        }
        //Adds RPC server to Server.
        NodeManager.getInstance().addAddress(this.serverId.getEndpoint());

        this.node = RaftServiceFactory.createAndInitRaftNode(this.groupId, this.serverId, this.nodeOptions);
        this.started = true;
        LOG.info("Start the RaftGroupService successfully.");
        return this.node;
    }

    /**
     * Block thread to wait the server shutdown.
     *
     * @throws InterruptedException if the current thread is interrupted
     *                              while waiting
     */
    public synchronized void join() throws InterruptedException {
        if (this.node != null) {
            this.node.join();
            this.node = null;
        }
    }

    public synchronized void shutdown() {
        if (!this.started) {
            return;
        }
        this.node.shutdown();
        NodeManager.getInstance().removeAddress(this.serverId.getEndpoint());
        this.started = false;
        LOG.info("Stop the RaftGroupService successfully.");
    }

    /**
     * Returns true when service is started.
     */
    public boolean isStarted() {
        return this.started;
    }

    /**
     * Returns the raft group id.
     */
    public String getGroupId() {
        return this.groupId;
    }

    /**
     * Set the raft group id
     */
    public void setGroupId(final String groupId) {
        if (this.started) {
            throw new IllegalStateException("Raft group service already started");
        }
        this.groupId = groupId;
    }

    /**
     * Returns the node serverId
     */
    public PeerId getServerId() {
        return this.serverId;
    }

    /**
     * Set the node serverId
     */
    public void setServerId(final PeerId serverId) {
        if (this.started) {
            throw new IllegalStateException("Raft group service already started");
        }
        this.serverId = serverId;
    }

    /**
     * Returns the node options.
     */
    public RpcOptions getNodeOptions() {
        return this.nodeOptions;
    }

    /**
     * Set node options.
     */
    public void setNodeOptions(final NodeOptions nodeOptions) {
        if (this.started) {
            throw new IllegalStateException("Raft group service already started");
        }
        if (nodeOptions == null) {
            throw new IllegalArgumentException("Invalid node options.");
        }
        nodeOptions.validate();
        this.nodeOptions = nodeOptions;
    }

    /**
     * Returns the rpc server instance.
     */
    public MessagingService getRpcServer() {
        return this.rpcServer;
    }

    /**
     * Set rpc server.
     */
    public void setRpcServer(final MessagingService rpcServer) {
        if (this.started) {
            throw new IllegalStateException("Raft group service already started");
        }
        if (this.serverId == null) {
            throw new IllegalStateException("Please set serverId at first");
        }
        this.rpcServer = rpcServer;
    }
}
