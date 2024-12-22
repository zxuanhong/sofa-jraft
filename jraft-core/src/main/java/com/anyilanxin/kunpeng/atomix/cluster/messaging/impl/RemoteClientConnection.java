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

import io.netty.channel.Channel;
import java.util.concurrent.CompletableFuture;

/** Client-side Netty remote connection. */
final class RemoteClientConnection extends AbstractClientConnection {
    private final Channel          channel;
    private final MessagingMetrics messagingMetrics;

    RemoteClientConnection(final MessagingMetrics messagingMetrics, final Channel channel) {
        this.messagingMetrics = messagingMetrics;
        this.channel = channel;
    }

    @Override
  public CompletableFuture<Void> sendAsync(final ProtocolRequest message) {
    final CompletableFuture<Void> future = new CompletableFuture<>();
    countMessageMetrics(message);
    channel
        .writeAndFlush(message)
        .addListener(
            channelFuture -> {
              if (!channelFuture.isSuccess()) {
                future.completeExceptionally(channelFuture.cause());
              } else {
                future.complete(null);
              }
            });
    return future;
  }

    @Override
  public CompletableFuture<byte[]> sendAndReceive(final ProtocolRequest message) {
    final CompletableFuture<byte[]> responseFuture = awaitResponseForRequestWithId(message.id());
    countReqResponseMetrics(message, responseFuture);
    channel
        .writeAndFlush(message)
        .addListener(
            channelFuture -> {
              if (!channelFuture.isSuccess()) {
                responseFuture.completeExceptionally(channelFuture.cause());
              }
            });
    return responseFuture;
  }

    private void countMessageMetrics(final ProtocolRequest message) {
        final String toAddress = channel.remoteAddress().toString();
        final String subject = message.subject();
        messagingMetrics.countMessage(channel.remoteAddress().toString(), message.subject());
        final byte[] payload = message.payload();
        messagingMetrics.observeRequestSize(toAddress, subject, payload == null ? 0 : payload.length);
    }

    private void countReqResponseMetrics(
      final ProtocolRequest message, final CompletableFuture<byte[]> responseFuture) {
    final String toAddress = channel.remoteAddress().toString();
    final String subject = message.subject();
    messagingMetrics.countRequestResponse(toAddress, subject);
    messagingMetrics.incInFlightRequests(toAddress, subject);
    final var timer = messagingMetrics.startRequestTimer(subject);
    final byte[] payload = message.payload();
    messagingMetrics.observeRequestSize(toAddress, subject, payload == null ? 0 : payload.length);

    responseFuture.whenComplete(
        (success, failure) -> {
          timer.close();
          messagingMetrics.decInFlightRequests(toAddress, subject);
          if (failure != null) {
            messagingMetrics.countFailureResponse(toAddress, subject, failure.getClass().getName());
          } else {
            messagingMetrics.countSuccessResponse(toAddress, subject);
          }
        });
  }

    @Override
    public String toString() {
        return "RemoteClientConnection{channel=" + channel + "}";
    }
}
