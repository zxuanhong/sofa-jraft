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

import com.anyilanxin.kunpeng.atomix.utils.misc.ArraySizeHashPrinter;
import com.anyilanxin.kunpeng.atomix.utils.net.Address;
import com.google.common.base.MoreObjects;

/** Internal request message. */
public final class ProtocolRequest extends ProtocolMessage {
    private final Address sender;
    private final String  subject;

    public ProtocolRequest(final long id, final Address sender, final String subject, final byte[] payload) {
        super(id, payload);
        this.sender = sender;
        this.subject = subject;
    }

    @Override
    public Type type() {
        return Type.REQUEST;
    }

    public String subject() {
        return subject;
    }

    public Address sender() {
        return sender;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id()).add("subject", subject).add("sender", sender)
            .add("payload", ArraySizeHashPrinter.of(payload())).toString();
    }
}
