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
package com.anyilanxin.kunpeng.util.buffer;

import org.agrona.DirectBuffer;

/**
 * Implementations may expose methods for access to properties from the buffer that is read. The
 * reader is a <em>view</em> on the buffer Any concurrent changes to the underlying buffer become
 * immediately visible to the reader.
 */
public interface BufferReader {
    /**
     * Wraps a buffer for read access.
     *
     * @param buffer the buffer to read from
     * @param offset the offset at which to start reading
     * @param length the length of the values to read
     */
    void wrap(DirectBuffer buffer, int offset, int length);

    /**
     * Copies the contents of {@code source} into a newly allocated buffer before reading it back.
     *
     * @param source the source writer
     * @throws NullPointerException if source is null
     */
    default void copyFrom(final BufferWriter source) {
        BufferUtil.copy(source, this);
    }
}
