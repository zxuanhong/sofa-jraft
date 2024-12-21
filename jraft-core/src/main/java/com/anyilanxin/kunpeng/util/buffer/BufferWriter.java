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

import org.agrona.MutableDirectBuffer;

/**
 * Implementations may add custom setters to specify values that should be written. Values are
 * written/copied when the {@link #write(MutableDirectBuffer, int)} method is called. Calling a
 * call-by-reference setter method (e.g. an Object setter) tells the writer <em>which object</em> to
 * write but not <em>what value</em>. The value is only determined at the time of writing, so that
 * value changes happening between setter and <em>#write</em> invocations affect the writer.
 */
public interface BufferWriter {
    /**
     * @return the number of bytes that this writer is going to write
     */
    int getLength();

    /**
     * Writes to a buffer.
     *
     * @param buffer the buffer that this writer writes to
     * @param offset the offset in the buffer that the writer begins writing at
     */
    void write(MutableDirectBuffer buffer, int offset);
}
