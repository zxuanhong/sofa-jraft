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
