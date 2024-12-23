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
package com.anyilanxin.kunpeng.atomix.cluster;

import com.anyilanxin.kunpeng.atomix.utils.AbstractIdentifier;
import java.util.Objects;
import java.util.UUID;

/** Node identifier. */
public class NodeId extends AbstractIdentifier<String> implements Comparable<NodeId> {

    /** Constructor for serialization. */
    private NodeId() {
        this("");
    }

    /**
     * Creates a new cluster node identifier from the specified string.
     *
     * @param id string identifier
     */
    public NodeId(final String id) {
        super(id);
    }

    /**
     * Creates a new cluster node identifier from the specified string.
     *
     * @return node id
     */
    public static NodeId anonymous() {
        return new NodeId(UUID.randomUUID().toString());
    }

    /**
     * Creates a new cluster node identifier from the specified string.
     *
     * @param id string identifier
     * @return node id
     */
    public static NodeId from(final String id) {
        return new NodeId(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof NodeId) {
            return ((NodeId) object).id().equals(id());
        }

        if (object instanceof AbstractIdentifier) {
            return object.equals(this);
        }

        return false;
    }

    @Override
    public int compareTo(final NodeId that) {
        return identifier.compareTo(that.identifier);
    }
}
