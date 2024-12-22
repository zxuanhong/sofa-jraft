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

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import com.anyilanxin.kunpeng.atomix.utils.Version;
import com.anyilanxin.kunpeng.atomix.utils.net.Address;
import java.util.Objects;
import java.util.Properties;

/** Represents a node as a member in a cluster. */
public class Member extends Node {
    private static final int UNKNOWN_TIMESTAMP = 0;

    private final MemberId   id;
    private final String     zone;
    private final String     rack;
    private final String     host;
    private final Properties properties;

    public Member(final MemberConfig config) {
        super(config);
        id = config.getId();
        zone = config.getZoneId();
        rack = config.getRackId();
        host = config.getHostId();
        properties = new Properties();
        properties.putAll(config.getProperties());
    }

    protected Member(final MemberId id, final Address address) {
        this(id, address, null, null, null, new Properties());
    }

    protected Member(final MemberId id, final Address address, final String zone, final String rack, final String host,
                     final Properties properties) {
        super(id, address);
        this.id = checkNotNull(id, "id cannot be null");
        this.zone = zone;
        this.rack = rack;
        this.host = host;
        this.properties = properties;
    }

    /**
     * Returns a new member builder with no ID.
     *
     * @return the member builder
     */
    public static MemberBuilder builder() {
        return new MemberBuilder(new MemberConfig());
    }

    /**
     * Returns a new member builder.
     *
     * @param memberId the member identifier
     * @return the member builder
     * @throws NullPointerException if the member ID is null
     */
    public static MemberBuilder builder(final String memberId) {
        return builder(MemberId.from(memberId));
    }

    /**
     * Returns a new member builder.
     *
     * @param memberId the member identifier
     * @return the member builder
     * @throws NullPointerException if the member ID is null
     */
    public static MemberBuilder builder(final MemberId memberId) {
        return builder().withId(memberId);
    }

    /**
     * Returns a new anonymous cluster member.
     *
     * @param address the member address
     * @return the member
     */
    public static Member member(final String address) {
        return member(Address.from(address));
    }

    /**
     * Returns a new named cluster member.
     *
     * @param name the member identifier
     * @param address the member address
     * @return the member
     */
    public static Member member(final String name, final String address) {
        return member(MemberId.from(name), Address.from(address));
    }

    /**
     * Returns a new anonymous cluster member.
     *
     * @param address the member address
     * @return the member
     */
    public static Member member(final Address address) {
        return builder().withAddress(address).build();
    }

    /**
     * Returns a new named cluster member.
     *
     * @param memberId the member identifier
     * @param address the member address
     * @return the member
     */
    public static Member member(final MemberId memberId, final Address address) {
        return builder(memberId).withAddress(address).build();
    }

    @Override
    public MemberId id() {
        return id;
    }

    @Override
    public MemberConfig config() {
        return new MemberConfig().setId(id()).setAddress(address()).setZoneId(zone()).setRackId(rack())
            .setHostId(host()).setProperties(properties());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, zone, rack, host, properties);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Member)) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        final Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(zone, member.zone) && Objects.equals(rack, member.rack)
               && Objects.equals(host, member.host) && Objects.equals(properties, member.properties);
    }

    @Override
    public String toString() {
        return toStringHelper(Member.class).add("id", id()).add("address", address()).add("zone", zone())
            .add("rack", rack()).add("host", host()).add("properties", properties()).omitNullValues().toString();
    }

    /**
     * Returns a boolean indicating whether this member is an active member of the cluster.
     *
     * @return indicates whether this member is an active member of the cluster
     */
    public boolean isActive() {
        return false;
    }

    /**
     * Returns the node reachability.
     *
     * @return the node reachability
     */
    public boolean isReachable() {
        return false;
    }

    /**
     * Returns the zone to which the member belongs.
     *
     * @return the zone to which the member belongs
     */
    public String zone() {
        return zone;
    }

    /**
     * Returns the rack to which the member belongs.
     *
     * @return the rack to which the member belongs
     */
    public String rack() {
        return rack;
    }

    /**
     * Returns the host to which the member belongs.
     *
     * @return the host to which the member belongs
     */
    public String host() {
        return host;
    }

    /**
     * Returns the member properties.
     *
     * @return the member properties
     */
    public Properties properties() {
        return properties;
    }

    /**
     * Returns the node version.
     *
     * @return the node version
     */
    public Version version() {
        return null;
    }

    /**
     * Returns the member timestamp.
     *
     * @return the member timestamp
     */
    public long timestamp() {
        return UNKNOWN_TIMESTAMP;
    }
}
