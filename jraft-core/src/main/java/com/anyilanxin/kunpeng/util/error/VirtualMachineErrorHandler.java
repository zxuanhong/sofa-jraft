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
package com.anyilanxin.kunpeng.util.error;

import java.lang.Thread.UncaughtExceptionHandler;
import org.slf4j.Logger;

/**
 * Handles all Throwables and exits for {@link VirtualMachineError}. It can also be used as a {@link
 * UncaughtExceptionHandler uncaught exception handler}, for example as the {@link
 * Thread#setDefaultUncaughtExceptionHandler default uncaught exception handler}
 */
public final class VirtualMachineErrorHandler
    implements FatalErrorHandler, UncaughtExceptionHandler {
  private static final int EXIT_CODE = 156; // ascii code Z + B
  private final Logger log;

  VirtualMachineErrorHandler(final Logger log) {
    this.log = log;
  }

  /**
   * Handles arbitrary {@link Throwable}s and completely terminates the JVM if it's an unrecoverable
   * error, i.e. a {@link VirtualMachineError}. Use this method whenever catching a {@link
   * Throwable}, before carrying on with your regular error handling.
   *
   * <p>Some example of {@link VirtualMachineError}s include {@link OutOfMemoryError}, {@link
   * StackOverflowError} and {@link InternalError}. We consider these errors unrecoverable because
   * there is no action we can take to resolve them, and it is safer to terminate and let a
   * hypervisor restart Zeebe.
   *
   * @param e the throwable
   */
  @Override
  public void handleError(final Throwable e) {
    if (e instanceof VirtualMachineError) {
      tryLogging(e);
      System.exit(EXIT_CODE);
    }
  }

  private void tryLogging(final Throwable e) {
    try {
      if (e instanceof OutOfMemoryError) {
        log.error(
            "Out of memory, exiting now because we can't recover from OOM."
                + " Consider adjusting memory limits.",
            e);
      } else {
        log.error(
            "Shutting down because we can't recover from JVM errors."
                + " Consider restarting this broker if it is a temporary issue.",
            e);
      }
    } catch (final Throwable loggingError) {
      // Ignored! We've tried logging a useful error message, but failed. There's nothing we can do.
    }
  }

  @Override
  public void uncaughtException(final Thread t, final Throwable e) {
    handleError(e);
  }
}
