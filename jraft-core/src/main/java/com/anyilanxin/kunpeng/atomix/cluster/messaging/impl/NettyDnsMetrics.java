/*
 * Copyright © 2024 anyilanxin xuanhongzhou(anyilanxin@aliyun.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
  private static final Counter ERROR =
      Counter.build()
          .help("Counts how often DNS queries fail with an error")
          .namespace("zeebe")
          .subsystem("dns")
          .name("error")
          .register();
  private static final Counter FAILED =
      Counter.build()
          .help("Counts how often DNS queries return an unsuccessful answer")
          .namespace("zeebe")
          .subsystem("dns")
          .name("failed")
          .labelNames("code")
          .register();
  private static final Counter WRITTEN =
      Counter.build()
          .help("Counts how often DNS queries are written")
          .namespace("zeebe")
          .subsystem("dns")
          .name("written")
          .register();
  private static final Counter SUCCESS =
      Counter.build()
          .help("Counts how often DNS queries are successful")
          .namespace("zeebe")
          .subsystem("dns")
          .name("success")
          .register();

  @Override
  public void queryWritten(final InetSocketAddress dnsServerAddress, final ChannelFuture future) {
    WRITTEN.inc();
  }

  @Override
  public void queryCancelled(final int queriesRemaining) {}

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
