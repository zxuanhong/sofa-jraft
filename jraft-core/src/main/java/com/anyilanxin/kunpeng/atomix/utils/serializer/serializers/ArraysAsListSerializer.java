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
package com.anyilanxin.kunpeng.atomix.utils.serializer.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.List;

/**
 * Kryo Serializer for {@link java.util.Arrays#asList(Object...)}.
 */
public final class ArraysAsListSerializer extends Serializer<List<?>> {

    @Override
    public void write(final Kryo kryo, final Output output, final List<?> object) {
        output.writeInt(object.size(), true);
        for (final Object elm : object) {
            kryo.writeClassAndObject(output, elm);
        }
    }

    @Override
    public List<?> read(final Kryo kryo, final Input input, final Class<? extends List<?>> type) {
        final int size = input.readInt(true);
        final List<Object> list = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            list.add(kryo.readClassAndObject(input));
        }
        return list;
    }
}
