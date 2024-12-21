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
 * <p>https://cloud.google.com/error-reporting/docs/formatting-error-messages
 */
@JsonInclude(Include.NON_EMPTY)
public final class ReportLocation {
  @JsonProperty("functionName")
  private String functionName;

  @JsonProperty("filePath")
  private String filePath;

  @JsonProperty("lineNumber")
  private int lineNumber;

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(final String filePath) {
    this.filePath = filePath;
  }

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(final String functionName) {
    this.functionName = functionName;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(final int lineNumber) {
    this.lineNumber = lineNumber;
  }
}
