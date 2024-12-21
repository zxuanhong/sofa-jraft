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
package com.anyilanxin.kunpeng.util.exception;

/**
 * Should be use to indicate an unexpected exception during execution. This exception extends {@link
 * RuntimeException} to be unchecked.
 */
public final class UncheckedExecutionException extends RuntimeException {

  public UncheckedExecutionException(final String message) {
    super(message);
  }

  public UncheckedExecutionException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
