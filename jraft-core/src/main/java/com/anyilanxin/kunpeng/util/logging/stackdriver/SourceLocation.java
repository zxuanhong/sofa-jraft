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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Additional information about the source code location that produced the log entry.
 *
 * <p>https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry#logentrysourcelocation
 */
@JsonInclude(Include.NON_EMPTY)
final class SourceLocation {
  @JsonProperty("function")
  private String function;

  @JsonProperty("file")
  private String file;

  @JsonProperty("line")
  private int line;

  public String getFile() {
    return file;
  }

  public void setFile(final String file) {
    this.file = file;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(final String function) {
    this.function = function;
  }

  public int getLine() {
    return line;
  }

  public void setLine(final int line) {
    this.line = line;
  }
}
