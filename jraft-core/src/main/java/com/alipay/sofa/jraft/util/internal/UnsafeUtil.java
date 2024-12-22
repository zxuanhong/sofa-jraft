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
package com.alipay.sofa.jraft.util.internal;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For the {@link sun.misc.Unsafe} access.
 *
 * @author jiachun.fjc
 */
@SuppressWarnings("ConstantConditions")
public final class UnsafeUtil {

    private static final Logger         LOG                       = LoggerFactory.getLogger(UnsafeUtil.class);

    private static final Object         UNSAFE                    = getUnsafe0();

    private static final UnsafeAccessor UNSAFE_ACCESSOR           = getUnsafeAccessor0();

    private static final long           BYTE_ARRAY_BASE_OFFSET    = arrayBaseOffset(byte[].class);
    // Micro-optimization: we can assume a scale of 1 and skip the multiply
    // private static final long BYTE_ARRAY_INDEX_SCALE = 1;

    private static final long           INT_ARRAY_BASE_OFFSET     = arrayBaseOffset(int[].class);
    private static final long           INT_ARRAY_INDEX_SCALE     = arrayIndexScale(int[].class);

    private static final long           BUFFER_ADDRESS_OFFSET     = objectFieldOffset(bufferAddressField());

    private static final long           STRING_VALUE_OFFSET       = objectFieldOffset(stringValueField());

    /**
     * Whether or not can use the unsafe api.
     */
    public static boolean hasUnsafe() {
        return UNSAFE != null;
    }

    public static int getInt(final Object target, final long offset) {
        return UNSAFE_ACCESSOR.getInt(target, offset);
    }


    public static byte getByte(final byte[] target, final long index) {
        return UNSAFE_ACCESSOR.getByte(target, BYTE_ARRAY_BASE_OFFSET + index);
    }

    public static void putByte(final byte[] target, final long index, final byte value) {
        UNSAFE_ACCESSOR.putByte(target, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    public static int getInt(final int[] target, final long index) {
        return UNSAFE_ACCESSOR.getInt(target, INT_ARRAY_BASE_OFFSET + (index * INT_ARRAY_INDEX_SCALE));
    }

    public static byte getByte(final long address) {
        return UNSAFE_ACCESSOR.getByte(address);
    }

    public static void putByte(final long address, final byte value) {
        UNSAFE_ACCESSOR.putByte(address, value);
    }

    public static int getInt(final long address) {
        return UNSAFE_ACCESSOR.getInt(address);
    }


    /**
     * Reports the offset of the first element in the storage allocation of a
     * given array class.
     */
    public static int arrayBaseOffset(final Class<?> clazz) {
        return hasUnsafe() ? UNSAFE_ACCESSOR.arrayBaseOffset(clazz) : -1;
    }

    /**
     * Reports the scale factor for addressing elements in the storage
     * allocation of a given array class.
     */
    public static int arrayIndexScale(final Class<?> clazz) {
        return hasUnsafe() ? UNSAFE_ACCESSOR.arrayIndexScale(clazz) : -1;
    }

    /**
     * Returns the offset of the provided field, or {@code -1} if {@code sun.misc.Unsafe} is not
     * available.
     */
    public static long objectFieldOffset(final Field field) {
        return field == null || hasUnsafe() ? UNSAFE_ACCESSOR.objectFieldOffset(field) : -1;
    }


    /**
     * Gets the offset of the {@code address} field of the given
     * direct {@link ByteBuffer}.
     */
    public static long addressOffset(final ByteBuffer buffer) {
        return UNSAFE_ACCESSOR.getLong(buffer, BUFFER_ADDRESS_OFFSET);
    }

    public static void throwException(final Throwable t) {
        UNSAFE_ACCESSOR.throwException(t);
    }

    /**
     * Returns a new {@link String} backed by the given {@code chars}.
     * The char array should not be mutated any more after calling
     * this function.
     */
    public static String moveToString(final char[] chars) {
        if (STRING_VALUE_OFFSET == -1) {
            // In the off-chance that this JDK does not implement String as we'd expect, just do a copy.
            return new String(chars);
        }
        final String str;
        try {
            str = (String) UNSAFE_ACCESSOR.allocateInstance(String.class);
        } catch (final InstantiationException e) {
            // This should never happen, but return a copy as a fallback just in case.
            return new String(chars);
        }
        UNSAFE_ACCESSOR.putObject(str, STRING_VALUE_OFFSET, chars);
        return str;
    }

    /**
     * Finds the address field within a direct {@link Buffer}.
     */
    private static Field bufferAddressField() {
        return field(Buffer.class, "address", long.class);
    }

    /**
     * Finds the value field within a {@link String}.
     */
    private static Field stringValueField() {
        return field(String.class, "value", char[].class);
    }

    /**
     * Gets the field with the given name within the class, or
     * {@code null} if not found. If found, the field is made accessible.
     */
    private static Field field(final Class<?> clazz, final String fieldName, final Class<?> expectedType) {
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            if (!field.getType().equals(expectedType)) {
                return null;
            }
        } catch (final Throwable t) {
            // Failed to access the fields.
            field = null;
        }
        return field;
    }

    private static UnsafeAccessor getUnsafeAccessor0() {
        return null;
    }

    private static Object getUnsafe0() {
        Object unsafe;
        try {
            final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = unsafeField.get(null);
        } catch (final Throwable t) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("sun.misc.Unsafe.theUnsafe: unavailable.", t);
            }
            unsafe = null;
        }
        return unsafe;
    }

    public static class UnsafeAccessor {

        private final sun.misc.Unsafe unsafe;

        UnsafeAccessor(Object unsafe) {
            this.unsafe = (sun.misc.Unsafe) unsafe;
        }


        public byte getByte(final Object target, final long offset) {
            return this.unsafe.getByte(target, offset);
        }

        public void putByte(final Object target, final long offset, final byte value) {
            this.unsafe.putByte(target, offset, value);
        }

        public int getInt(final Object target, final long offset) {
            return this.unsafe.getInt(target, offset);
        }

        public long getLong(final Object target, final long offset) {
            return this.unsafe.getLong(target, offset);
        }


        public void putObject(final Object target, final long offset, final Object value) {
            this.unsafe.putObject(target, offset, value);
        }

        public byte getByte(final long address) {
            return this.unsafe.getByte(address);
        }

        public void putByte(final long address, final byte value) {
            this.unsafe.putByte(address, value);
        }

        public int getInt(final long address) {
            return this.unsafe.getInt(address);
        }

        /**
         * Reports the offset of the first element in the storage allocation of a
         * given array class.
         */
        public int arrayBaseOffset(final Class<?> clazz) {
            return this.unsafe != null ? this.unsafe.arrayBaseOffset(clazz) : -1;
        }

        /**
         * Reports the scale factor for addressing elements in the storage
         * allocation of a given array class.
         */
        public int arrayIndexScale(final Class<?> clazz) {
            return this.unsafe != null ? this.unsafe.arrayIndexScale(clazz) : -1;
        }

        /**
         * Returns the offset of the provided field, or {@code -1} if {@code sun.misc.Unsafe} is not
         * available.
         */
        public long objectFieldOffset(final Field field) {
            return field == null || this.unsafe == null ? -1 : this.unsafe.objectFieldOffset(field);
        }

        public Object allocateInstance(final Class<?> clazz) throws InstantiationException {
            return this.unsafe.allocateInstance(clazz);
        }

        public void throwException(final Throwable t) {
            this.unsafe.throwException(t);
        }
    }

    private UnsafeUtil() {
    }
}
