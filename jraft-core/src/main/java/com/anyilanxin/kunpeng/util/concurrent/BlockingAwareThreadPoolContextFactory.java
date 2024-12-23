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
package com.anyilanxin.kunpeng.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;

/**
 * Thread pool context factory.
 */
public class BlockingAwareThreadPoolContextFactory implements ThreadContextFactory {
    private final ScheduledExecutorService executor;

    public BlockingAwareThreadPoolContextFactory(String name, int threadPoolSize, Logger logger) {
        this(threadPoolSize, Threads.namedThreads(name, logger));
    }

    public BlockingAwareThreadPoolContextFactory(int threadPoolSize, ThreadFactory threadFactory) {
        this(Executors.newScheduledThreadPool(threadPoolSize, threadFactory));
    }

    public BlockingAwareThreadPoolContextFactory(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public ThreadContext createContext() {
        return new BlockingAwareThreadPoolContext(executor);
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
