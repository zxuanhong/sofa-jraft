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
 * The service context is used to properly aggregate errors in the Stackdriver Error Reporting tool.
 *
 * <p>Errors are grouped by service name and service version, allowing us to track which versions
 * and clusters are affected by a set of errors.
 *
 * <p>https://cloud.google.com/error-reporting/docs/formatting-error-messages
 */
@JsonInclude(Include.NON_EMPTY)
final class ServiceContext {
  @JsonProperty("service")
  private String service;

  @JsonProperty("version")
  private String version;

  public String getService() {
    return service;
  }

  public void setService(final String service) {
    this.service = service;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }
}
