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
package com.anyilanxin.kunpeng.atomix.cluster.impl;

import com.anyilanxin.kunpeng.atomix.cluster.BootstrapService;
import com.anyilanxin.kunpeng.atomix.cluster.Node;
import com.anyilanxin.kunpeng.atomix.cluster.discovery.*;
import com.anyilanxin.kunpeng.atomix.utils.event.AbstractListenerManager;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default node discovery service.
 */
public class DefaultNodeDiscoveryService
        extends AbstractListenerManager<NodeDiscoveryEvent, NodeDiscoveryEventListener>
        implements ManagedNodeDiscoveryService {

    private final BootstrapService bootstrapService;
    private final Node localNode;
    private final NodeDiscoveryProvider provider;
    private final AtomicBoolean started = new AtomicBoolean();
    private final NodeDiscoveryEventListener discoveryEventListener = this::post;

    public DefaultNodeDiscoveryService(
            final BootstrapService bootstrapService,
            final Node localNode,
            final NodeDiscoveryProvider provider) {
        this.bootstrapService = bootstrapService;
        this.localNode = localNode;
        this.provider = provider;
    }

    @Override
    public Set<Node> getNodes() {
        return provider.getNodes();
    }

    @Override
    public CompletableFuture<NodeDiscoveryService> start() {
        if (started.compareAndSet(false, true)) {
            provider.addListener(discoveryEventListener);
            final Node node =
                    Node.builder().withId(localNode.id().id()).withAddress(localNode.address()).build();
            return provider.join(bootstrapService, node).thenApply(v -> this);
        }
        return CompletableFuture.completedFuture(this);
    }

    @Override
    public boolean isRunning() {
        return started.get();
    }

    @Override
    public CompletableFuture<Void> stop() {
        if (started.compareAndSet(true, false)) {
            return provider
                    .leave(localNode)
                    .thenRun(
                            () -> {
                                provider.removeListener(discoveryEventListener);
                            });
        }
        return CompletableFuture.completedFuture(null);
    }
}
