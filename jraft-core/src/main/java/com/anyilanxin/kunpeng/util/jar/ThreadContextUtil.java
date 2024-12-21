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
  public static void runCheckedWithClassLoader(
      final CheckedRunnable runnable, final ClassLoader classLoader) throws Exception {
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
  public static <V> V callWithClassLoader(final Callable<V> callable, final ClassLoader classLoader)
      throws Exception {
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
  public static <V> V supplyWithClassLoader(
      final Supplier<V> supplier, final ClassLoader classLoader) {
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
