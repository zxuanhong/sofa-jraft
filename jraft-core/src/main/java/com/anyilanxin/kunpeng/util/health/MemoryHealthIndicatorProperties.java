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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** External configuration properties for {@link MemoryHealthIndicator}. */
@ConfigurationProperties(prefix = "management.health.memory")
@Component
@Primary
public class MemoryHealthIndicatorProperties {

  /** Minimum memory that should be available. */
  private double threshold = getDefaultThreshold();

  public double getThreshold() {
    return threshold;
  }

  public void setThreshold(final double threshold) {
    if (threshold <= 0 || threshold >= 1) {
      throw new IllegalArgumentException("Threshold must be a value in the interval ]0,1[");
    }
    this.threshold = threshold;
  }

  protected double getDefaultThreshold() {
    return 0.1;
  }
}
