/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anyilanxin.kunpeng.util.concurrent;

import com.anyilanxin.kunpeng.util.CheckedRunnable;
import com.anyilanxin.kunpeng.util.CloseableSilently;
import com.anyilanxin.kunpeng.util.RetryDelayStrategy;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/** Scheduler. */
public interface Scheduler extends CloseableSilently {

  /**
   * Schedules a runnable after a delay.
   *
   * @param delay the delay after which to run the callback
   * @param timeUnit the time unit
   * @param callback the callback to run
   * @return the scheduled callback
   */
  default Scheduled schedule(long delay, TimeUnit timeUnit, Runnable callback) {
    return schedule(Duration.ofMillis(timeUnit.toMillis(delay)), callback);
  }

  /**
   * Schedules a runnable after a delay.
   *
   * @param delay the delay after which to run the callback
   * @param callback the callback to run
   * @return the scheduled callback
   */
  Scheduled schedule(Duration delay, Runnable callback);

  /**
   * Schedules a runnable at a fixed rate.
   *
   * @param initialDelay the initial delay
   * @param interval the interval at which to run the callback
   * @param timeUnit the time unit
   * @param callback the callback to run
   * @return the scheduled callback
   */
  default Scheduled schedule(
      long initialDelay, long interval, TimeUnit timeUnit, Runnable callback) {
    return schedule(
        Duration.ofMillis(timeUnit.toMillis(initialDelay)),
        Duration.ofMillis(timeUnit.toMillis(interval)),
        callback);
  }

  /**
   * Schedules a runnable at a fixed rate.
   *
   * @param initialDelay the initial delay
   * @param interval the interval at which to run the callback
   * @param callback the callback to run
   * @return the scheduled callback
   */
  Scheduled schedule(Duration initialDelay, Duration interval, Runnable callback);

  /**
   * Schedules a runnable at a fixed rate.
   *
   * @param interval the interval at which to run the callback
   * @param callback the callback to run
   * @return the scheduled callback
   */
  default Scheduled scheduleAtFixedRate(final Duration interval, final Runnable callback) {
    return schedule(Duration.ZERO, interval, callback);
  }

  default <A> CompletableFuture<A> retryUntilSuccessful(
      final Callable<A> callable,
      final RetryDelayStrategy retryDelayStrategy,
      final Predicate<Exception> shouldRetry) {
    final var result = new CompletableFuture<A>();
    // cannot be a lambda as it needs a reference to itself for scheduling
    final Runnable runnable =
        new Runnable() {
          Scheduled scheduled = null;

          @Override
          public void run() {
            try {
              final var a = callable.call();
              result.complete(a);
            } catch (final Exception e) {
              if (shouldRetry.test(e)) {
                if (!result.isDone()) {
                  final var next = retryDelayStrategy.nextDelay();
                  scheduled = schedule(next, this);
                }
              } else {
                result.completeExceptionally(e);
              }
            }
          }
        };

    runnable.run();
    return result;
  }

  default CompletableFuture<Void> retryUntilSuccessful(
      final CheckedRunnable runnable,
      final RetryDelayStrategy retryDelayStrategy,
      final Predicate<Exception> shouldRetry) {
    return retryUntilSuccessful(
        CheckedRunnable.toCallable(runnable), retryDelayStrategy, shouldRetry);
  }

  @Override
  default void close() {}
}
