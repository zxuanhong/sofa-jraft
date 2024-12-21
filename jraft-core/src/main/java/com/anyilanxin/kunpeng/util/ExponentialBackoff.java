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
package com.anyilanxin.kunpeng.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleSupplier;
import java.util.function.LongUnaryOperator;

/**
 * An simple implementation of exponential backoff, which multiples the previous delay with an
 * increasing multiplier and adding some jitter to avoid multiple clients polling at the same time
 * even with back off.
 *
 * <p>The next delay is calculated as:
 *
 * <pre> max(min(maxDelay, currentDelay * backoffFactor), minDelay) + (rand(0.0, 1.0) *
 * (currentDelay * jitterFactor) + (currentDelay * -jitterFactor)) </pre>
 */
public final class ExponentialBackoff implements LongUnaryOperator {

    private static final long DEFAULT_MAX_RETRY_DELAY_MS = 5000;
    private static final long DEFAULT_MIN_RETRY_DELAY_MS = 100;

    // defaults are taken from gRPC's own exponential backoff policy
    private static final double DEFAULT_BACKOFF_FACTOR = 1.6;
    private static final double DEFAULT_JITTER_FACTOR = 0.1;

    private final long maxDelay;
    private final long minDelay;
    private final double backoffFactor;
    private final double jitterFactor;
    private final DoubleSupplier random;

    public ExponentialBackoff() {
        this(
                DEFAULT_MAX_RETRY_DELAY_MS,
                DEFAULT_MIN_RETRY_DELAY_MS,
                DEFAULT_BACKOFF_FACTOR,
                DEFAULT_JITTER_FACTOR);
    }

    public ExponentialBackoff(final long maxDelay, final long minDelay) {
        this(maxDelay, minDelay, DEFAULT_BACKOFF_FACTOR, DEFAULT_JITTER_FACTOR);
    }

    public ExponentialBackoff(
            final long maxDelay,
            final long minDelay,
            final double backoffFactor,
            final double jitterFactor) {
        this(
                maxDelay,
                minDelay,
                backoffFactor,
                jitterFactor,
                () -> ThreadLocalRandom.current().nextDouble());
    }

    @VisibleForTesting
    ExponentialBackoff(
            final long maxDelay,
            final long minDelay,
            final double backoffFactor,
            final double jitterFactor,
            final DoubleSupplier random) {
        this.maxDelay = maxDelay;
        this.minDelay = minDelay;
        this.backoffFactor = backoffFactor;
        this.jitterFactor = jitterFactor;
        this.random = random;
    }

    @Override
    public long applyAsLong(final long operand) {
        return supplyRetryDelay(operand);
    }

    public long supplyRetryDelay(final long currentRetryDelay) {
        final var delay = Math.max(Math.min(maxDelay, currentRetryDelay * backoffFactor), minDelay);
        final var jitter = computeJitter(delay);
        return Math.round(delay + jitter);
    }

    private double computeJitter(final double value) {
        final var minFactor = value * -jitterFactor;
        final var maxFactor = value * jitterFactor;

        return (random.getAsDouble() * (maxFactor - minFactor)) + minFactor;
    }
}
