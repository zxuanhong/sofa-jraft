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
package com.anyilanxin.kunpeng.util.buffer;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;

public final class DirectBufferWriter implements BufferWriter {
    private DirectBuffer buffer;
    private int          offset;
    private int          length;

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
