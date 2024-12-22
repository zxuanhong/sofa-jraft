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
package com.anyilanxin.kunpeng.atomix.cluster.messaging.impl;

import com.anyilanxin.kunpeng.util.CloseableSilently;

public interface MessagingMetrics {

    CloseableSilently startRequestTimer(String name);

    void observeRequestSize(String to, String name, int requestSizeInBytes);

    void countMessage(String to, String name);

    void countRequestResponse(String to, String name);

    void countSuccessResponse(String address, String name);

    void countFailureResponse(String address, String name, String error);

    void incInFlightRequests(String address, String topic);

    void decInFlightRequests(String address, String topic);
}
