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
package com.anyilanxin.kunpeng.atomix.cluster.discovery;

import com.anyilanxin.kunpeng.atomix.cluster.NodeConfig;

import java.util.Collection;
import java.util.Collections;

/**
 * Bootstrap discovery configuration.
 */
public class BootstrapDiscoveryConfig extends NodeDiscoveryConfig {
    private Collection<NodeConfig> nodes = Collections.emptySet();

    @Override
    public NodeDiscoveryProvider.Type getType() {
        return BootstrapDiscoveryProvider.TYPE;
    }

    /**
     * Returns the configured bootstrap nodes.
     *
     * @return the configured bootstrap nodes
     */
    public Collection<NodeConfig> getNodes() {
        return nodes;
    }

    /**
     * Sets the bootstrap nodes.
     *
     * @param nodes the bootstrap nodes
     * @return the bootstrap provider configuration
     */
    public BootstrapDiscoveryConfig setNodes(final Collection<NodeConfig> nodes) {
        this.nodes = nodes;
        return this;
    }
}
