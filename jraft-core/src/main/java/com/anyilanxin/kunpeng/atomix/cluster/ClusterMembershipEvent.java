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

import com.anyilanxin.kunpeng.atomix.utils.event.AbstractEvent;
import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Describes cluster-related event.
 */
public class ClusterMembershipEvent extends AbstractEvent<ClusterMembershipEvent.Type, Member> {

    /**
     * Creates an event of a given type and for the specified instance and the current time.
     *
     * @param type     cluster event type
     * @param instance cluster device subject
     */
    public ClusterMembershipEvent(final Type type, final Member instance) {
        super(type, instance);
    }

    /**
     * Creates an event of a given type and for the specified device and time.
     *
     * @param type     device event type
     * @param instance event device subject
     * @param time     occurrence time
     */
    public ClusterMembershipEvent(final Type type, final Member instance, final long time) {
        super(type, instance, time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type(), subject(), time());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ClusterMembershipEvent) {
            final ClusterMembershipEvent other = (ClusterMembershipEvent) obj;
            return Objects.equals(type(), other.type()) && Objects.equals(subject(), other.subject())
                   && Objects.equals(time(), other.time());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass()).add("type", type()).add("subject", subject()).add("time", time())
            .toString();
    }

    /**
     * Type of cluster-related events.
     */
    public enum Type {
        /**
         * Indicates that a new member has been added.
         */
        MEMBER_ADDED,

        /**
         * Indicates that a member's metadata has changed.
         */
        METADATA_CHANGED,

        /**
         * Indicates that a member's reachability has changed.
         */
        REACHABILITY_CHANGED,

        /**
         * Indicates that a member has been removed.
         */
        MEMBER_REMOVED,
    }
}
