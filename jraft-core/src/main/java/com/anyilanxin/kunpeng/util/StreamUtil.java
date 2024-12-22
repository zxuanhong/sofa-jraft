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
    private StreamUtil() {
    }

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
