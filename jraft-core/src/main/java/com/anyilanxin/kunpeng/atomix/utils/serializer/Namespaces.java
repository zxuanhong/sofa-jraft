/*
 * Copyright 2014-present Open Networking Foundation
 * Copyright © 2024 anyilanxin xuanhongzhou(anyilanxin@aliyun.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anyilanxin.kunpeng.atomix.utils.serializer;

import com.anyilanxin.kunpeng.atomix.utils.Version;
import com.anyilanxin.kunpeng.atomix.utils.serializer.serializers.*;
import com.google.common.collect.*;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class Namespaces {

  public static final Namespace BASIC =
      new Namespace.Builder()
          .nextId(Namespace.FLOATING_ID)
          .register(byte[].class)
          .register(new AtomicBooleanSerializer(), AtomicBoolean.class)
          .register(new AtomicIntegerSerializer(), AtomicInteger.class)
          .register(new AtomicLongSerializer(), AtomicLong.class)
          .register(
              new ImmutableListSerializer(),
              ImmutableList.class,
              ImmutableList.of(1).getClass(),
              ImmutableList.of(1, 2).getClass(),
              ImmutableList.of(1, 2, 3).subList(1, 3).getClass())
          .register(
              new ImmutableSetSerializer(),
              ImmutableSet.class,
              ImmutableSet.of().getClass(),
              ImmutableSet.of(1).getClass(),
              ImmutableSet.of(1, 2).getClass())
          .register(
              new ImmutableMapSerializer(),
              ImmutableMap.class,
              ImmutableMap.of().getClass(),
              ImmutableMap.of("a", 1).getClass(),
              ImmutableMap.of("R", 2, "D", 2).getClass())
          .register(Collections.unmodifiableSet(Collections.emptySet()).getClass())
          .register(HashMap.class)
          .register(ConcurrentHashMap.class)
          .register(CopyOnWriteArraySet.class)
          .register(
              ArrayList.class,
              LinkedList.class,
              HashSet.class,
              LinkedHashSet.class,
              ArrayDeque.class)
          .register(HashMultiset.class)
          .register(Multisets.immutableEntry("", 0).getClass())
          .register(Sets.class)
          .register(Maps.immutableEntry("a", "b").getClass())
          .register(new ArraysAsListSerializer(), Arrays.asList().getClass())
          .register(Collections.singletonList(1).getClass())
          .register(Duration.class)
          .register(Collections.emptySet().getClass())
          .register(Optional.class)
          .register(Collections.emptyList().getClass())
          .register(Collections.singleton(Object.class).getClass())
          .register(Properties.class)
          .register(int[].class)
          .register(long[].class)
          .register(short[].class)
          .register(double[].class)
          .register(float[].class)
          .register(char[].class)
          .register(String[].class)
          .register(boolean[].class)
          .register(Object[].class)
          .register(Void.class) // placeholder for the deleted LogicalTimestamp class
          .register(Void.class) // placeholder for the deleted WallClockTimestamp class
          .register(Version.class)
          .register(
              new ByteBufferSerializer(),
              ByteBuffer.class,
              ByteBuffer.allocate(1).getClass(),
              ByteBuffer.allocateDirect(1).getClass())
          .name("BASIC")
          .build();

  /** Kryo registration Id for user custom registration. */
  public static final int BEGIN_USER_CUSTOM_ID = 500;

  // not to be instantiated
  private Namespaces() {}
}
