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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    private DateUtil() {
    }

    public static OffsetDateTime toOffsetDateTime(final Instant timestamp) {
        return OffsetDateTime.ofInstant(timestamp, ZoneOffset.UTC);
    }

    public static OffsetDateTime toOffsetDateTime(final Long timestamp) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }

    public static OffsetDateTime toOffsetDateTime(final String timestamp) {
        return toOffsetDateTime(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static OffsetDateTime toOffsetDateTime(final String timestamp, final DateTimeFormatter dateTimeFormatter) {
        try {
            final ZonedDateTime zonedDateTime = ZonedDateTime.parse(timestamp, dateTimeFormatter);
            return OffsetDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault());
        } catch (final DateTimeParseException e) {
            LOGGER.error(String.format("Cannot parse date from %s - %s", timestamp, e.getMessage()), e);
        }

        return null;
    }

    public static OffsetDateTime fuzzyToOffsetDateTime(final Object object) {
        return switch (object) {
            case null -> null;
            case final OffsetDateTime offsetDateTime -> offsetDateTime;
            case final Instant instant -> toOffsetDateTime(instant);
            case final Long l -> toOffsetDateTime(Instant.ofEpochMilli(l));
            case final String s -> toOffsetDateTime(s);
            default -> throw new IllegalArgumentException(
                    "Could not convert " + object.getClass() + " to OffsetDateTime");
        };
    }
}
