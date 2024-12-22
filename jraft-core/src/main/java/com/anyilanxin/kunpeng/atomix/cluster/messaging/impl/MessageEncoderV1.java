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

import com.anyilanxin.kunpeng.atomix.utils.net.Address;
import io.netty.buffer.ByteBuf;
import java.net.InetAddress;

/** V1 message encoder. */
class MessageEncoderV1 extends AbstractMessageEncoder {
    MessageEncoderV1(final Address address) {
        super(address);
    }

    @Override
    protected void encodeAddress(final ProtocolMessage message, final ByteBuf buffer) {
        final InetAddress senderIp = address.tryResolveAddress();
        final byte[] senderIpBytes = senderIp.getAddress();
        buffer.writeByte(senderIpBytes.length);
        buffer.writeBytes(senderIpBytes);
        buffer.writeInt(address.port());
    }

    @Override
    protected void encodeMessage(final ProtocolMessage message, final ByteBuf buffer) {
        buffer.writeByte(message.type().id());
        writeLong(buffer, message.id());

        final byte[] payload = message.payload();
        writeInt(buffer, payload.length);
        buffer.writeBytes(payload);
    }

    @Override
    protected void encodeRequest(final ProtocolRequest request, final ByteBuf out) {
        writeString(out, request.subject());
    }

    @Override
    protected void encodeReply(final ProtocolReply reply, final ByteBuf out) {
        out.writeByte(reply.status().id());
    }
}
