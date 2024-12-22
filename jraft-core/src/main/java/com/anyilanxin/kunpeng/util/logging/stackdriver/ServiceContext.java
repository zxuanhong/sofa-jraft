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
