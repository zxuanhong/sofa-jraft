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
package com.anyilanxin.kunpeng.atomix.utils.serializer;

/** Interface for serialization of store artifacts. */
public interface Serializer {

    /**
     * Creates a new serializer builder.
     *
     * @return a new serializer builder
     */
    static SerializerBuilder builder() {
    return new SerializerBuilder();
  }

    /**
     * Serialize the specified object.
     *
     * @param object object to serialize.
     * @param <T> encoded type
     * @return serialized bytes.
     */
    <T> byte[] encode(T object);

    /**
     * Deserialize the specified bytes.
     *
     * @param bytes byte array to deserialize.
     * @param <T> decoded type
     * @return deserialized object.
     */
    <T> T decode(byte[] bytes);

    /**
     * Creates a new Serializer instance from a Namespace.
     *
     * @param namespace serializer namespace
     * @return Serializer instance
     */
    static Serializer using(final Namespace namespace) {
    return new Serializer() {

      @Override
      public <T> byte[] encode(final T object) {
        return namespace.serialize(object);
      }

      @Override
      public <T> T decode(final byte[] bytes) {
        return namespace.deserialize(bytes);
      }
    };
  }
}
