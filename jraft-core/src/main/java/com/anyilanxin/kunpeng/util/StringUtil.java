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

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

public final class StringUtil {

  /** Helper functions that removes nulls and empty strings and trims all remaining strings */
  public static final UnaryOperator<List<String>> LIST_SANITIZER =
      input ->
          Optional.ofNullable(input).orElse(Collections.emptyList()).stream()
              .filter(Objects::nonNull)
              .map(String::trim)
              .filter(not(String::isEmpty))
              .collect(toList());

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  public static byte[] getBytes(final String value) {
    return getBytes(value, DEFAULT_CHARSET);
  }

  public static byte[] getBytes(final String value, final Charset charset) {
    return value.getBytes(charset);
  }

  public static String fromBytes(final byte[] bytes) {
    return fromBytes(bytes, DEFAULT_CHARSET);
  }

  public static String fromBytes(final byte[] bytes, final Charset charset) {
    return new String(bytes, charset);
  }

  public static String limitString(final String message, final int maxLength) {
    if (message.length() > maxLength) {
      return message.substring(0, maxLength).concat("...");
    } else {
      return message;
    }
  }
}
