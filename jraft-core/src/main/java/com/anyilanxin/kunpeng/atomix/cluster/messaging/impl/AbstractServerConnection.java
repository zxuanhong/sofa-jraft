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

import com.anyilanxin.kunpeng.util.StringUtil;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for server-side connections. Manages dispatching requests to message handlers.
 */
abstract class AbstractServerConnection implements ServerConnection {
    private final Logger          log = LoggerFactory.getLogger(getClass());
    private final HandlerRegistry handlers;

    AbstractServerConnection(final HandlerRegistry handlers) {
        this.handlers = handlers;
    }

    @Override
    public void dispatch(final ProtocolRequest message) {
        final String subject = message.subject();
        final BiConsumer<ProtocolRequest, ServerConnection> handler = handlers.get(subject);
        if (handler != null) {
            log.trace("Received message type {} from {}", subject, message.sender());
            handler.accept(message, this);
        } else {
            log.debug("No handler for message type {} from {}", subject, message.sender());

            byte[] subjectBytes = null;
            if (subject != null) {
                subjectBytes = StringUtil.getBytes(subject);
            }

            reply(message.id(), ProtocolReply.Status.ERROR_NO_HANDLER, Optional.ofNullable(subjectBytes));
        }
    }
}
