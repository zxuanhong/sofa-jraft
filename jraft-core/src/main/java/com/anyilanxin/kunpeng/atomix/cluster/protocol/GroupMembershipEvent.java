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
package com.anyilanxin.kunpeng.atomix.cluster.protocol;

import com.anyilanxin.kunpeng.atomix.cluster.Member;
import com.anyilanxin.kunpeng.atomix.utils.event.AbstractEvent;

import java.util.Objects;

/**
 * Group membership protocol event.
 */
public class GroupMembershipEvent extends AbstractEvent<GroupMembershipEvent.Type, Member> {

    public GroupMembershipEvent(final Type type, final Member subject) {
        super(type, subject);
    }

    /**
     * Returns the member.
     *
     * @return the member
     */
    public Member member() {
        return subject();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type(), member());
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof GroupMembershipEvent) {
            final GroupMembershipEvent that = (GroupMembershipEvent) object;
            return type() == that.type() && member().equals(that.member());
        }
        return false;
    }

    /**
     * Group membership protocol event type.
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
