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

    public static OffsetDateTime toOffsetDateTime(
            final String timestamp, final DateTimeFormatter dateTimeFormatter) {
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
