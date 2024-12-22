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

import com.anyilanxin.kunpeng.util.buffer.BufferWriter;
import java.nio.ByteOrder;
import org.agrona.sbe.MessageEncoderFlyweight;

public final class SbeUtil {
    private SbeUtil() {
    }

    /**
     * Writes a {@link BufferWriter} instance as a nested field in an SBE message. This can be useful
     * to avoid intermediate copies to a {@link org.agrona.DirectBuffer} instance and using the
     * encoder to write the copy. This can also be used to write nested SBE messages as well.
     *
     * <p>NOTE: variable length data in SBE is written/read in order. This method should be called as
     * well in the right order so the data is written at the right offset.
     *
     * @param writer the data to write
     * @param headerLength the length of the header corresponding to the data we want to write
     * @param message the SBE message into which we should write
     * @param order the byte order
     */
    public static void writeNested(final BufferWriter writer, final int headerLength,
                                   final MessageEncoderFlyweight message, final ByteOrder order) {
        final int dataLength = writer.getLength();
        final var limit = message.limit();

        message.limit(limit + headerLength + dataLength);
        message.buffer().putInt(limit, dataLength, order);
        writer.write(message.buffer(), limit + headerLength);
    }
}
