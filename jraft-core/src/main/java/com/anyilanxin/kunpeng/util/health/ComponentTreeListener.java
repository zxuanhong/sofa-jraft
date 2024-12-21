/*
 * Copyright © 2024 anyilanxin xuanhongzhou(anyilanxin@aliyun.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.anyilanxin.kunpeng.util.health;

import java.util.Optional;

/**
 * Listener to changes in "graph" of monitored components. Primary use case is to export the graph
 * to a human-readable format (such as grafana).
 */
public interface ComponentTreeListener extends AutoCloseable {

    /**
     * Register a node in the graph. If the node has a parent, its parent-child relationship must be
     * registered before registering the node. Using the method {@link
     * ComponentTreeListener#registerNode(HealthMonitorable, Optional)} is preferred.
     *
     * @param component to be registered
     */
    void registerNode(HealthMonitorable component);

    /**
     * Register a new node, together with its relationship with its parent.
     *
     * @param component to register
     * @param parent    parent of the component, will be registered before registering the node
     */
    default void registerNode(final HealthMonitorable component, final Optional<String> parent) {
        parent.ifPresent(p -> registerRelationship(component.componentName(), p));
        registerNode(component);
    }

    void unregisterNode(HealthMonitorable component);

    /**
     * Register a relationship between a child and its parent. It must be called before registering
     * the child
     */
    void registerRelationship(String child, String parent);

    /**
     * Unregister a relationship between a child and its parent
     */
    void unregisterRelationship(String child, String parent);

    static ComponentTreeListener noop() {
        return new ComponentTreeListener() {
            @Override
            public void registerNode(final HealthMonitorable component) {
            }

            @Override
            public void unregisterNode(final HealthMonitorable component) {
            }

            @Override
            public void registerRelationship(final String child, final String parent) {
            }

            @Override
            public void unregisterRelationship(final String child, final String parent) {
            }

            @Override
            public void close() throws Exception {
            }
        };
    }
}
