/*
 * Copyright 2018-present Open Networking Foundation
 * Copyright © 2024 anyilanxin xuanhongzhou(anyilanxin@aliyun.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anyilanxin.kunpeng.atomix.utils.serializer;

import com.anyilanxin.kunpeng.atomix.utils.Builder;

/** Serializer builder. */
public class SerializerBuilder implements Builder<Serializer> {
  private final Namespace.Builder namespaceBuilder =
      new Namespace.Builder().register(Namespaces.BASIC).nextId(Namespaces.BEGIN_USER_CUSTOM_ID);

  @Override
  public Serializer build() {
    return Serializer.using(namespaceBuilder.build());
  }
}
