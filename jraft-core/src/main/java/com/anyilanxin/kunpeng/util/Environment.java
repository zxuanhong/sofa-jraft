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
package com.anyilanxin.kunpeng.util;

import java.util.*;

import org.slf4j.Logger;

public final class Environment {

    private static final Logger       LOG = Loggers.CONFIG_LOGGER;

    private final Map<String, String> environment;

    public Environment() {
        this(System.getenv());
    }

    public Environment(final Map<String, String> environment) {
        this.environment = environment;
    }

    public Set<String> getPropertyKeys() {
        return Collections.unmodifiableSet(environment.keySet());
    }

    public Optional<String> get(final String name) {
        return Optional.ofNullable(environment.get(name));
    }

    public Optional<Integer> getInt(final String name) {
        try {
            return get(name).map(Integer::valueOf);
        } catch (final Exception e) {
            LOG.warn("Failed to parse environment variable {}", name, e);
            return Optional.empty();
        }
    }

    public Optional<Double> getDouble(final String name) {
        try {
            return get(name).map(Double::valueOf);
        } catch (final Exception e) {
            LOG.warn("Failed to parse environment variable {}", name, e);
            return Optional.empty();
        }
    }

    public Optional<Long> getLong(final String name) {
        try {
            return get(name).map(Long::valueOf);
        } catch (final Exception e) {
            LOG.warn("Failed to parse environment variable {}", name, e);
            return Optional.empty();
        }
    }

    public Optional<Boolean> getBool(final String name) {
        try {
            return get(name).map(Boolean::valueOf);
        } catch (final Exception e) {
            LOG.warn("Failed to parse environment variable {}", name, e);
            return Optional.empty();
        }
    }

    public Optional<List<String>> getList(final String name) {
        return get(name).map(v -> v.split(",")).map(Arrays::asList).map(StringUtil.LIST_SANITIZER);
    }
}
