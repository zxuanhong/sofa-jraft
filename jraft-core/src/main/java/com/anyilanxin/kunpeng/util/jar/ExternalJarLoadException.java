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
package com.anyilanxin.kunpeng.util.jar;

import java.io.IOException;
import java.nio.file.Path;

public final class ExternalJarLoadException extends IOException {
    private static final String MESSAGE_FORMAT   = "Cannot load JAR at [%s]: %s";
    private static final long   serialVersionUID = 1655276726721040696L;

    public ExternalJarLoadException(final Path jarPath, final String reason) {
        super(String.format(MESSAGE_FORMAT, jarPath, reason));
    }

    public ExternalJarLoadException(final Path jarPath, final String reason, final Throwable cause) {
        super(String.format(MESSAGE_FORMAT, jarPath, reason), cause);
    }
}
