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
package com.anyilanxin.kunpeng.atomix.cluster.discovery;

import com.anyilanxin.kunpeng.atomix.cluster.Node;
import com.anyilanxin.kunpeng.atomix.utils.net.Address;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Bootstrap discovery builder.
 */
public class BootstrapDiscoveryBuilder extends NodeDiscoveryBuilder {
    private final BootstrapDiscoveryConfig config = new BootstrapDiscoveryConfig();

    /**
     * Sets the bootstrap nodes.
     *
     * @param nodes the bootstrap nodes
     * @return the location provider builder
     */
    public BootstrapDiscoveryBuilder withNodes(final Address... nodes) {
        return withNodes(
                Stream.of(nodes)
                        .map(address -> Node.builder().withAddress(address).build())
                        .collect(Collectors.toSet()));
    }

    /**
     * Sets the bootstrap nodes.
     *
     * @param locations the bootstrap member locations
     * @return the location provider builder
     */
    public BootstrapDiscoveryBuilder withNodes(final Collection<Node> locations) {
        config.setNodes(locations.stream().map(Node::config).collect(Collectors.toList()));
        return this;
    }

    @Override
    public NodeDiscoveryProvider build() {
        return new BootstrapDiscoveryProvider(config);
    }
}
