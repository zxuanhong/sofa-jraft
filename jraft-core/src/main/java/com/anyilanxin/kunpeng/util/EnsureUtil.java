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

import java.util.Objects;

import org.agrona.DirectBuffer;

public final class EnsureUtil {

    private static final String ERROR_MSG_NON_EMPTY = "%s must not be empty";

    private EnsureUtil() {
    }

    public static void ensureNotNull(final String property, final Object o) {
        Objects.requireNonNull(o, property);
    }

    public static long ensureGreaterThan(
            final String property, final long testValue, final long comparisonValue) {
        if (testValue <= comparisonValue) {
            throw new IllegalArgumentException(property + " must be greater than " + comparisonValue);
        }

        return testValue;
    }

    public static void ensureGreaterThanOrEqual(
            final String property, final long testValue, final long comparisonValue) {
        if (testValue < comparisonValue) {
            throw new IllegalArgumentException(
                    property + " must be greater than or equal to " + comparisonValue);
        }
    }

    public static void ensureGreaterThan(
            final String property, final double testValue, final double comparisonValue) {
        if (testValue <= comparisonValue) {
            throw new IllegalArgumentException(property + " must be greater than " + comparisonValue);
        }
    }

    public static void ensureGreaterThanOrEqual(
            final String property, final double testValue, final double comparisonValue) {
        if (testValue < comparisonValue) {
            throw new IllegalArgumentException(
                    property + " must be greater than or equal to " + comparisonValue);
        }
    }

    public static void ensureNotNullOrEmpty(final String property, final String testValue) {
        ensureNotNull(property, testValue);

        if (testValue.isEmpty()) {
            throw new IllegalArgumentException(String.format(ERROR_MSG_NON_EMPTY, property));
        }
    }

    public static void ensureNotNullOrEmpty(final String property, final byte[] testValue) {
        ensureNotNull(property, testValue);

        if (testValue.length == 0) {
            throw new IllegalArgumentException(String.format(ERROR_MSG_NON_EMPTY, property));
        }
    }

    public static void ensureNotNullOrEmpty(final String property, final DirectBuffer testValue) {
        ensureNotNull(property, testValue);

        if (testValue.capacity() == 0) {
            throw new IllegalArgumentException(String.format(ERROR_MSG_NON_EMPTY, property));
        }
    }
}
