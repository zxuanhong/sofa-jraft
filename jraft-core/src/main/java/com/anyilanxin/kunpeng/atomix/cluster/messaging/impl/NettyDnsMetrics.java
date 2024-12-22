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

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.resolver.dns.DnsQueryLifecycleObserver;
import io.prometheus.client.Counter;
import java.net.InetSocketAddress;
import java.util.List;

final class NettyDnsMetrics implements DnsQueryLifecycleObserver {
    private static final Counter ERROR   = Counter.build().help("Counts how often DNS queries fail with an error")
                                             .namespace("zeebe").subsystem("dns").name("error").register();
    private static final Counter FAILED  = Counter.build()
                                             .help("Counts how often DNS queries return an unsuccessful answer")
                                             .namespace("zeebe").subsystem("dns").name("failed").labelNames("code")
                                             .register();
    private static final Counter WRITTEN = Counter.build().help("Counts how often DNS queries are written")
                                             .namespace("zeebe").subsystem("dns").name("written").register();
    private static final Counter SUCCESS = Counter.build().help("Counts how often DNS queries are successful")
                                             .namespace("zeebe").subsystem("dns").name("success").register();

    @Override
    public void queryWritten(final InetSocketAddress dnsServerAddress, final ChannelFuture future) {
        WRITTEN.inc();
    }

    @Override
    public void queryCancelled(final int queriesRemaining) {
    }

    @Override
    public DnsQueryLifecycleObserver queryRedirected(final List<InetSocketAddress> nameServers) {
        return this;
    }

    @Override
    public DnsQueryLifecycleObserver queryCNAMEd(final DnsQuestion cnameQuestion) {
        return this;
    }

    @Override
    public DnsQueryLifecycleObserver queryNoAnswer(final DnsResponseCode code) {
        FAILED.labels(code.toString()).inc();
        return this;
    }

    @Override
    public void queryFailed(final Throwable cause) {
        ERROR.inc();
    }

    @Override
    public void querySucceed() {
        SUCCESS.inc();
    }
}
