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

import java.time.Duration;

/**
 * Retry delay strategy that uses {@link ExponentialBackoff} to calculate the next retry delay.
 */
public class ExponentialBackoffRetryDelay implements RetryDelayStrategy {

    private final ExponentialBackoff exponentialBackoff;
    private long currentDelay = 0; // starts with minDelay

    public ExponentialBackoffRetryDelay(final Duration maxDelay, final Duration minDelay) {
        exponentialBackoff = new ExponentialBackoff(maxDelay.toMillis(), minDelay.toMillis());
    }

    @Override
    public Duration nextDelay() {
        currentDelay = exponentialBackoff.supplyRetryDelay(currentDelay);
        return Duration.ofMillis(currentDelay);
    }

    @Override
    public void reset() {
        currentDelay = 0;
    }
}
