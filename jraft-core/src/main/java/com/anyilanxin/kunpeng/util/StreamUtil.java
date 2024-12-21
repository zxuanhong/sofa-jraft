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

import com.anyilanxin.kunpeng.util.StreamUtil.MinMaxCollector.MinMax;
import java.util.Comparator;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class StreamUtil {
  private StreamUtil() {}

  /**
   * Returns a collector that computes the minimum and maximum of a stream of elements according to
   * the provided comparator.
   */
  public static <T> Collector<T, MinMax<T>, MinMax<T>> minMax(final Comparator<T> comparator) {
    return new MinMaxCollector<>(comparator);
  }

  static final class MinMaxCollector<T> implements Collector<T, MinMax<T>, MinMax<T>> {
    private final Comparator<T> comparator;

    private MinMaxCollector(final Comparator<T> comparator) {
      this.comparator = comparator;
    }

    @Override
    public Supplier<MinMax<T>> supplier() {
      return MinMax::new;
    }

    @Override
    public BiConsumer<MinMax<T>, T> accumulator() {
      return (minMax, value) -> {
        if (minMax.min == null || comparator.compare(value, minMax.min) < 0) {
          minMax.min = value;
        }
        if (minMax.max == null || comparator.compare(value, minMax.max) > 0) {
          minMax.max = value;
        }
      };
    }

    @Override
    public BinaryOperator<MinMax<T>> combiner() {
      return (minMax1, minMax2) -> {
        if (comparator.compare(minMax1.min, minMax2.min) < 0) {
          minMax1.min = minMax2.min;
        }
        if (comparator.compare(minMax1.max, minMax2.max) > 0) {
          minMax1.max = minMax2.max;
        }
        return minMax1;
      };
    }

    @Override
    public Function<MinMax<T>, MinMax<T>> finisher() {
      return (minMax) -> minMax;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return Set.of(Characteristics.IDENTITY_FINISH);
    }

    public static final class MinMax<T> {
      private T min;
      private T max;

      /**
       * @return the minimum value of the stream, or {@code null} if the stream was empty.
       */
      public T min() {
        return min;
      }

      /**
       * @return the maximum value of the stream, or {@code null} if the stream was empty.
       */
      public T max() {
        return max;
      }
    }
  }
}
