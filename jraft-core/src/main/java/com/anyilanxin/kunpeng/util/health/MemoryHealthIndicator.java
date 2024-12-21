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
package com.anyilanxin.kunpeng.util.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * Health indicator that compares the free memory against a given threshold. The threshold is given
 * in percent of max memory.
 */
public final class MemoryHealthIndicator implements HealthIndicator {

  private final double threshold;

  /**
   * Constructs a memory health indicator
   *
   * @param threshold threshold of free memory in percent; must be a value between {@code ]0,1[}
   */
  public MemoryHealthIndicator(final double threshold) {
    if (threshold <= 0 || threshold >= 1) {
      throw new IllegalArgumentException("Threshold must be a value in the interval ]0,1[");
    }
    this.threshold = threshold;
  }

  public double getThreshold() {
    return threshold;
  }

  @Override
  public Health health() {
    if (getAvailableMemoryPercentageCurrently() > threshold) {
      return Health.up().withDetail("threshold", threshold).build();
    } else {
      return Health.down().withDetail("threshold", threshold).build();
    }
  }

  private double getAvailableMemoryPercentageCurrently() {
    final Runtime runtime = Runtime.getRuntime();

    final long freeMemory = runtime.freeMemory(); // currently free memory
    final long totalMemory = runtime.totalMemory(); // currently allocated by JVM
    final long maxMemory =
        runtime.maxMemory(); // the maximum memory the JVM could allocate (specified by -Xmx)

    final long notYetAllocatedMemory = (maxMemory - totalMemory);
    final long availableMemory = freeMemory + notYetAllocatedMemory;

    return (double) (availableMemory) / maxMemory;
  }
}
