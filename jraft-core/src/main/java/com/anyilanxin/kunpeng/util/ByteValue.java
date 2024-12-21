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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

public final class ByteValue {
    private static final int CONVERSION_FACTOR_KB = 1024;
    private static final int CONVERSION_FACTOR_MB = CONVERSION_FACTOR_KB * 1024;
    private static final int CONVERSION_FACTOR_GB = CONVERSION_FACTOR_MB * 1024;

    /**
     * Converts the {@code value} kilobytes into bytes
     *
     * @param value value in kilobytes
     * @return {@code value} converted into bytes
     */
    public static long ofKilobytes(final long value) {
        return value * CONVERSION_FACTOR_KB;
    }

    /**
     * Converts the {@code value} megabytes into bytes
     *
     * @param value value in megabytes
     * @return {@code value} converted into bytes
     */
    public static long ofMegabytes(final long value) {
        return value * CONVERSION_FACTOR_MB;
    }

    /**
     * Converts the {@code value} gigabytes into bytes
     *
     * @param value value in gigabytes
     * @return {@code value} converted into bytes
     */
    public static long ofGigabytes(final long value) {
        return value * CONVERSION_FACTOR_GB;
    }

    public static String prettyPrint(final long bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException("Value must be >= 0");
        }
        if (bytes < 1024) {
            return bytes + " B";
        }
        long value = bytes;
        final CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && bytes > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format(Locale.US, "%.1f %cB", value / 1024.0, ci.current());
    }
}
