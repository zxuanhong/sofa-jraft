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
