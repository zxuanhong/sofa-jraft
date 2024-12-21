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
package com.anyilanxin.kunpeng.util.logging.stackdriver;

/**
 * The severity of the event described in a log entry, expressed as one of the standard severity
 * levels listed below. For your reference, the levels are assigned the listed numeric values. The
 * effect of using numeric values other than those listed is undefined.
 *
 * <p>https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry#logseverity
 */
public enum Severity {
  DEFAULT(0),
  DEBUG(100),
  INFO(200),
  NOTICE(300),
  WARNING(400),
  ERROR(500),
  CRITICAL(600),
  ALERT(700),
  EMERGENCY(800);

  private final int level;

  Severity(final int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
  }
}
