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
import org.agrona.MutableDirectBuffer;

public final class DirectBufferWriter implements BufferWriter {
    private DirectBuffer buffer;
    private int offset;
    private int length;

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void write(final MutableDirectBuffer writeBuffer, final int writeOffset) {
        writeBuffer.putBytes(writeOffset, buffer, offset, length);
    }

    public DirectBufferWriter wrap(final DirectBuffer buffer, final int offset, final int length) {
        this.buffer = buffer;
        this.offset = offset;
        this.length = length;

        return this;
    }

    public DirectBufferWriter wrap(final DirectBuffer buffer) {
        return wrap(buffer, 0, buffer.capacity());
    }

    public void reset() {
        buffer = null;
        offset = -1;
        length = 0;
    }
}
