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

import com.anyilanxin.kunpeng.protocol.Protocol;
import org.agrona.collections.IntHashSet;

public final class PartitionUtil {
  private PartitionUtil() {}

  /**
   * Returns a set of all partitions from {@link Protocol#START_PARTITION_ID} to {@code
   * numPartitions}.
   */
  public static IntHashSet allPartitions(final int numPartitions) {
    requireValidPartitionId(numPartitions);
    final var partitions = new IntHashSet(numPartitions);
    for (int i = Protocol.START_PARTITION_ID;
        i < Protocol.START_PARTITION_ID + numPartitions;
        i++) {
      partitions.add(i);
    }
    return partitions;
  }

  /**
   * Throws if the partition is outside the range of {@link Protocol#START_PARTITION_ID} to {@link
   * Protocol#MAXIMUM_PARTITIONS}
   */
  public static void requireValidPartitionId(final int partitionId) {
    if (partitionId < Protocol.START_PARTITION_ID) {
      throw new IllegalArgumentException(
          "Partition id " + partitionId + " must be >= " + Protocol.START_PARTITION_ID);
    }
    if (partitionId > Protocol.MAXIMUM_PARTITIONS) {
      throw new IllegalArgumentException(
          "Partition id " + partitionId + " must be <= " + Protocol.MAXIMUM_PARTITIONS);
    }
  }
}
