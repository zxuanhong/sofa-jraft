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

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.MDC;

public final class LogUtil {
  private LogUtil() {}

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
