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

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.MDC;

public final class LogUtil {
    private LogUtil() {
    }

    /** see https://logback.qos.ch/manual/mdc.html */
    public static void doWithMDC(final Map<String, String> context, final Runnable r) {
        final Map<String, String> currentContext = MDC.getCopyOfContextMap();
        MDC.setContextMap(context);

        try {
            r.run();
        } finally {
            if (currentContext != null) {
                MDC.setContextMap(currentContext);
            } else {
                MDC.clear();
            }
        }
    }

    public static void catchAndLog(final Logger log, final CheckedRunnable r) {
        try {
            r.run();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
