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

import com.anyilanxin.kunpeng.atomix.cluster.messaging.impl.ProtocolReply.Status;
import io.netty.channel.Channel;
import java.util.Optional;

/** Remote server connection manages messaging on the server side of a Netty connection. */
final class RemoteServerConnection extends AbstractServerConnection {
    private static final byte[] EMPTY_PAYLOAD = new byte[0];

    private final Channel       channel;

    RemoteServerConnection(final HandlerRegistry handlers, final Channel channel) {
        super(handlers);
        this.channel = channel;
    }

    @Override
    public void reply(final long messageId, final Status status, final Optional<byte[]> payload) {
        final ProtocolReply response = new ProtocolReply(messageId, payload.orElse(EMPTY_PAYLOAD), status);
        channel.writeAndFlush(response, channel.voidPromise());
    }
}
