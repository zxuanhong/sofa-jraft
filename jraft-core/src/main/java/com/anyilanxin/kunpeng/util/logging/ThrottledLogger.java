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
package com.anyilanxin.kunpeng.util.logging;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * Logger that logs only once every configured interval.
 *
 * <p>Example:
 *
 * <pre>
 * while(condition) {
 *     log.info("repeated log");
 * }
 * </pre>
 *
 * <p>When ThrottledLogger is used, this is not logged in every loop. Instead, it will be logged
 * once in every configured interval.
 */
public class ThrottledLogger implements Logger {

  private long lastLogTime = 0;
  private final Logger log;
  private final long intervalMillis;

  public ThrottledLogger(final Logger log, final Duration interval) {
    this.log = log;
    intervalMillis = interval.toMillis();
  }

  @Override
  public String getName() {
    return log.getName();
  }

  @Override
  public boolean isTraceEnabled() {
    return log.isTraceEnabled();
  }

  @Override
  public void trace(final String s) {
    checkAndLog(() -> log.trace(s));
  }

  @Override
  public void trace(final String s, final Object o) {
    checkAndLog(() -> log.trace(s, o));
  }

  @Override
  public void trace(final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.trace(s, o, o1));
  }

  @Override
  public void trace(final String s, final Object... objects) {
    checkAndLog(() -> log.trace(s, objects));
  }

  @Override
  public void trace(final String s, final Throwable throwable) {
    checkAndLog(() -> log.trace(s, throwable));
  }

  @Override
  public boolean isTraceEnabled(final Marker marker) {
    return log.isTraceEnabled(marker);
  }

  @Override
  public void trace(final Marker marker, final String s) {
    checkAndLog(() -> log.trace(marker, s));
  }

  @Override
  public void trace(final Marker marker, final String s, final Object o) {
    checkAndLog(() -> log.trace(marker, s, o));
  }

  @Override
  public void trace(final Marker marker, final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.trace(marker, s, o, o1));
  }

  @Override
  public void trace(final Marker marker, final String s, final Object... objects) {
    checkAndLog(() -> log.trace(marker, s, objects));
  }

  @Override
  public void trace(final Marker marker, final String s, final Throwable throwable) {
    checkAndLog(() -> log.trace(marker, s, throwable));
  }

  @Override
  public boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  @Override
  public void debug(final String s) {
    checkAndLog(() -> log.debug(s));
  }

  @Override
  public void debug(final String s, final Object o) {
    checkAndLog(() -> log.debug(s, o));
  }

  @Override
  public void debug(final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.debug(s, o, o1));
  }

  @Override
  public void debug(final String s, final Object... objects) {
    checkAndLog(() -> log.debug(s, objects));
  }

  @Override
  public void debug(final String s, final Throwable throwable) {
    checkAndLog(() -> log.debug(s, throwable));
  }

  @Override
  public boolean isDebugEnabled(final Marker marker) {
    return log.isDebugEnabled(marker);
  }

  @Override
  public void debug(final Marker marker, final String s) {
    checkAndLog(() -> log.debug(marker, s));
  }

  @Override
  public void debug(final Marker marker, final String s, final Object o) {
    checkAndLog(() -> log.debug(marker, s, o));
  }

  @Override
  public void debug(final Marker marker, final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.debug(marker, s, o, o1));
  }

  @Override
  public void debug(final Marker marker, final String s, final Object... objects) {
    checkAndLog(() -> log.debug(marker, s, objects));
  }

  @Override
  public void debug(final Marker marker, final String s, final Throwable throwable) {
    checkAndLog(() -> log.debug(marker, s, throwable));
  }

  @Override
  public boolean isInfoEnabled() {
    return log.isInfoEnabled();
  }

  @Override
  public void info(final String s) {
    checkAndLog(() -> log.info(s));
  }

  @Override
  public void info(final String s, final Object o) {
    checkAndLog(() -> log.info(s, o));
  }

  @Override
  public void info(final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.info(s, o, o1));
  }

  @Override
  public void info(final String s, final Object... objects) {
    checkAndLog(() -> log.info(s, objects));
  }

  @Override
  public void info(final String s, final Throwable throwable) {
    checkAndLog(() -> log.info(s, throwable));
  }

  @Override
  public boolean isInfoEnabled(final Marker marker) {
    return log.isInfoEnabled(marker);
  }

  @Override
  public void info(final Marker marker, final String s) {
    checkAndLog(() -> log.info(marker, s));
  }

  @Override
  public void info(final Marker marker, final String s, final Object o) {
    checkAndLog(() -> log.info(marker, s, o));
  }

  @Override
  public void info(final Marker marker, final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.info(marker, s, o, o1));
  }

  @Override
  public void info(final Marker marker, final String s, final Object... objects) {
    checkAndLog(() -> log.info(marker, s, objects));
  }

  @Override
  public void info(final Marker marker, final String s, final Throwable throwable) {
    checkAndLog(() -> log.info(marker, s, throwable));
  }

  @Override
  public boolean isWarnEnabled() {
    return log.isWarnEnabled();
  }

  @Override
  public void warn(final String s) {
    checkAndLog(() -> log.warn(s));
  }

  @Override
  public void warn(final String s, final Object o) {
    checkAndLog(() -> log.warn(s, o));
  }

  @Override
  public void warn(final String s, final Object... objects) {
    checkAndLog(() -> log.warn(s, objects));
  }

  @Override
  public void warn(final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.warn(s, o, o1));
  }

  @Override
  public void warn(final String s, final Throwable throwable) {
    checkAndLog(() -> log.warn(s, throwable));
  }

  @Override
  public boolean isWarnEnabled(final Marker marker) {
    return log.isWarnEnabled(marker);
  }

  @Override
  public void warn(final Marker marker, final String s) {
    checkAndLog(() -> log.warn(marker, s));
  }

  @Override
  public void warn(final Marker marker, final String s, final Object o) {
    checkAndLog(() -> log.warn(marker, s, o));
  }

  @Override
  public void warn(final Marker marker, final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.warn(marker, s, o, o1));
  }

  @Override
  public void warn(final Marker marker, final String s, final Object... objects) {
    checkAndLog(() -> log.warn(marker, s, objects));
  }

  @Override
  public void warn(final Marker marker, final String s, final Throwable throwable) {
    checkAndLog(() -> log.warn(marker, s, throwable));
  }

  @Override
  public boolean isErrorEnabled() {
    return log.isErrorEnabled();
  }

  @Override
  public void error(final String s) {
    checkAndLog(() -> log.error(s));
  }

  @Override
  public void error(final String s, final Object o) {
    checkAndLog(() -> log.error(s, o));
  }

  @Override
  public void error(final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.error(s, o, o1));
  }

  @Override
  public void error(final String s, final Object... objects) {
    checkAndLog(() -> log.error(s, objects));
  }

  @Override
  public void error(final String s, final Throwable throwable) {
    checkAndLog(() -> log.error(s, throwable));
  }

  @Override
  public boolean isErrorEnabled(final Marker marker) {
    return log.isErrorEnabled(marker);
  }

  @Override
  public void error(final Marker marker, final String s) {
    checkAndLog(() -> log.error(marker, s));
  }

  @Override
  public void error(final Marker marker, final String s, final Object o) {
    checkAndLog(() -> log.error(marker, s, o));
  }

  @Override
  public void error(final Marker marker, final String s, final Object o, final Object o1) {
    checkAndLog(() -> log.error(marker, s, o, o1));
  }

  @Override
  public void error(final Marker marker, final String s, final Object... objects) {
    checkAndLog(() -> log.error(marker, s, objects));
  }

  @Override
  public void error(final Marker marker, final String s, final Throwable throwable) {
    checkAndLog(() -> log.error(marker, s, throwable));
  }

  private void checkAndLog(final Runnable logger) {
    final var currentTime = System.currentTimeMillis();
    if (currentTime - lastLogTime >= intervalMillis) {
      logger.run();
      lastLogTime = currentTime;
    }
  }
}
