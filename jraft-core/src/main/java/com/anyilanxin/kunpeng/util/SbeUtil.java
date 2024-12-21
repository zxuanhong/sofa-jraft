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

import com.anyilanxin.kunpeng.util.buffer.BufferWriter;
import java.nio.ByteOrder;
import org.agrona.sbe.MessageEncoderFlyweight;

public final class SbeUtil {
  private SbeUtil() {}

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
  public static void writeNested(
      final BufferWriter writer,
      final int headerLength,
      final MessageEncoderFlyweight message,
      final ByteOrder order) {
    final int dataLength = writer.getLength();
    final var limit = message.limit();

    message.limit(limit + headerLength + dataLength);
    message.buffer().putInt(limit, dataLength, order);
    writer.write(message.buffer(), limit + headerLength);
  }
}
