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

import java.lang.annotation.*;

/**
 * Indicates that the visibility was strengthened purely for testing purposes.
 *
 * <p>NOTE: this should not be used on public members, but is meant only to highlight
 * package-private or protected fields, types, etc., which aren't private in order to allow
 * comprehensive tests. In general, you should try to avoid this, but at times there is no other
 * way.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface VisibleForTesting {

  /** An optional justification as to why something was made visible for testing */
  String value() default "";
}
