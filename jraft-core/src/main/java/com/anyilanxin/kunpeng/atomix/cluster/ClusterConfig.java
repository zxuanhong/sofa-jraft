/*
 * Copyright 2018-present Open Networking Foundation
 * Copyright © 2024 anyilanxin xuanhongzhou(anyilanxin@aliyun.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anyilanxin.kunpeng.atomix.cluster;

import static com.google.common.base.Preconditions.checkNotNull;

import com.anyilanxin.kunpeng.atomix.cluster.discovery.NodeDiscoveryConfig;
import com.anyilanxin.kunpeng.atomix.cluster.messaging.MessagingConfig;
import com.anyilanxin.kunpeng.atomix.cluster.protocol.GroupMembershipProtocolConfig;
import com.anyilanxin.kunpeng.atomix.cluster.protocol.SwimMembershipProtocolConfig;
import com.anyilanxin.kunpeng.atomix.utils.config.Config;

/**
 * Cluster configuration.
 */
public class ClusterConfig implements Config {

    private static final String DEFAULT_CLUSTER_NAME = "atomix";

    private String clusterId = DEFAULT_CLUSTER_NAME;
    private MemberConfig nodeConfig = new MemberConfig();
    private NodeDiscoveryConfig discoveryConfig;
    private GroupMembershipProtocolConfig protocolConfig = new SwimMembershipProtocolConfig();
    private MessagingConfig messagingConfig = new MessagingConfig();

    /**
     * Returns the cluster identifier.
     *
     * @return the cluster identifier
     */
    public String getClusterId() {
        return clusterId;
    }

    /**
     * Sets the cluster identifier.
     *
     * @param clusterId the cluster identifier
     * @return the cluster configuration
     */
    public ClusterConfig setClusterId(final String clusterId) {
        this.clusterId = clusterId;
        return this;
    }

    /**
     * Returns the local member configuration.
     *
     * @return the local member configuration
     */
    public MemberConfig getNodeConfig() {
        return nodeConfig;
    }

    /**
     * Sets the local member configuration.
     *
     * @param nodeConfig the local member configuration
     * @return the cluster configuration
     */
    public ClusterConfig setNodeConfig(final MemberConfig nodeConfig) {
        this.nodeConfig = checkNotNull(nodeConfig);
        return this;
    }

    /**
     * Returns the node discovery provider configuration.
     *
     * @return the node discovery provider configuration
     */
    public NodeDiscoveryConfig getDiscoveryConfig() {
        return discoveryConfig;
    }

    /**
     * Sets the node discovery provider configuration.
     *
     * @param discoveryConfig the node discovery provider configuration
     * @return the node configuration
     */
    public ClusterConfig setDiscoveryConfig(final NodeDiscoveryConfig discoveryConfig) {
        this.discoveryConfig = checkNotNull(discoveryConfig);
        return this;
    }

    /**
     * Returns the group membership protocol configuration.
     *
     * @return the group membership protocol configuration
     */
    public GroupMembershipProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    /**
     * Sets the group membership protocol configuration.
     *
     * @param protocolConfig the group membership protocol configuration
     * @return the cluster configuration
     */
    public ClusterConfig setProtocolConfig(final GroupMembershipProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
        return this;
    }

    /**
     * Returns the cluster messaging configuration.
     *
     * @return the messaging configuration
     */
    public MessagingConfig getMessagingConfig() {
        return messagingConfig;
    }

    /**
     * Sets the cluster messaging configuration.
     *
     * @param messagingConfig the messaging configuration
     * @return the cluster configuration
     */
    public ClusterConfig setMessagingConfig(final MessagingConfig messagingConfig) {
        this.messagingConfig = messagingConfig;
        return this;
    }
}
