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

import io.prometheus.client.Gauge;

final class SwimMembershipProtocolMetrics {

    private static final Gauge MEMBERS_INCARNATION_NUMBER = Gauge
                                                              .build()
                                                              .namespace("zeebe")
                                                              .name("smp_members_incarnation_number")
                                                              .help(
                                                                  "Member's Incarnation number. This metric shows the incarnation number of each "
                                                                          + "member in the several nodes. This is useful to observe state propagation of each"
                                                                          + "member information.")
                                                              .labelNames("memberId").register();

    static void updateMemberIncarnationNumber(final String member, final long incarnationNumber) {
        MEMBERS_INCARNATION_NUMBER.labels(member).set(incarnationNumber);
    }
}
