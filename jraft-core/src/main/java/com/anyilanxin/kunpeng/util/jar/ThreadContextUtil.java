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
package com.anyilanxin.kunpeng.util.jar;

import com.anyilanxin.kunpeng.util.CheckedRunnable;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import org.agrona.LangUtil;

/**
 * A collection of utilities to run an arbitrary {@link Runnable} with a specific thread context
 * class loader. This is required when side loading external code via the {@link
 * ExternalJarClassLoader}, as that code may be using the {@link Thread#getContextClassLoader()}.
 *
 * <p>As the same thread may be reused, it's also important to reset the thread afterwards to avoid
 * operations being run on the wrong class loader.
 */
public final class ThreadContextUtil {

    /**
     * Executes the given {@code runnable}, swapping the thread context class loader for the given
     * class loader, and swapping it back with the previous class loader afterwards.
     *
     * @param runnable the operation to execute
     * @param classLoader the class loader to temporarily assign to the current thread's context class
     *     loader
     */
    public static void runWithClassLoader(final Runnable runnable, final ClassLoader classLoader) {
    try {
      runCheckedWithClassLoader(runnable::run, classLoader);
    } catch (final Exception e) {
      LangUtil.rethrowUnchecked(e);
    }
  }

    /**
     * Executes the given {@code runnable}, swapping the thread context class loader for the given
     * class loader, and swapping it back with the previous class loader afterwards.
     *
     * <p>Use this method if you want your operation to throw exceptions; the class loader is
     * guaranteed to be reset even if an exception is thrown.
     *
     * @param runnable the operation to execute
     * @param classLoader the class loader to temporarily assign to the current thread's context class
     *     loader
     */
    public static void runCheckedWithClassLoader(final CheckedRunnable runnable, final ClassLoader classLoader)
                                                                                                               throws Exception {
        final var currentThread = Thread.currentThread();
        final var contextClassLoader = currentThread.getContextClassLoader();

        try {
            currentThread.setContextClassLoader(classLoader);
            runnable.run();
        } finally {
            currentThread.setContextClassLoader(contextClassLoader);
        }
    }

    /**
     * Executes the given {@code callable}, swapping the thread context class loader for the given
     * class loader, and swapping it back with the previous class loader afterwards.
     *
     * @param callable the operation to execute
     * @param classLoader the class loader to temporarily assign to the current thread's context class
     *     loader
     */
    public static <V> V callWithClassLoader(final Callable<V> callable, final ClassLoader classLoader) throws Exception {
        final var currentThread = Thread.currentThread();
        final var contextClassLoader = currentThread.getContextClassLoader();

        try {
            currentThread.setContextClassLoader(classLoader);
            return callable.call();
        } finally {
            currentThread.setContextClassLoader(contextClassLoader);
        }
    }

    /**
     * Executes the given {@code Supplier}, swapping the thread context class loader for the given
     * class loader, and swapping it back with the previous class loader afterwards.
     *
     * @param supplier the operation to execute
     * @param classLoader the class loader to temporarily assign to the current thread's context class
     *     loader
     */
    public static <V> V supplyWithClassLoader(final Supplier<V> supplier, final ClassLoader classLoader) {
        final var currentThread = Thread.currentThread();
        final var contextClassLoader = currentThread.getContextClassLoader();
        try {
            currentThread.setContextClassLoader(classLoader);
            return supplier.get();
        } finally {
            currentThread.setContextClassLoader(contextClassLoader);
        }
    }
}
