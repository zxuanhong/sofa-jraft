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

import com.anyilanxin.kunpeng.atomix.cluster.messaging.ClusterCommunicationService;
import com.anyilanxin.kunpeng.atomix.cluster.messaging.MessagingService;

/**
 * Default partition management service.
 */
public class DefaultPartitionManagementService implements PartitionManagementService {
    private final ClusterMembershipService membershipService;
    private final MessagingService         messagingService;

    public DefaultPartitionManagementService(final ClusterMembershipService membershipService,
                                             final MessagingService messagingService) {
        this.membershipService = membershipService;
        this.messagingService = messagingService;
    }

    @Override
    public ClusterMembershipService getMembershipService() {
        return membershipService;
    }

    @Override
    public MessagingService getMessagingService() {
        return messagingService;
    }
}
