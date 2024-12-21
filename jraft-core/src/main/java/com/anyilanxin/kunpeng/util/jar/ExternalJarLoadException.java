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
package com.anyilanxin.kunpeng.util.jar;

import java.io.IOException;
import java.nio.file.Path;

public final class ExternalJarLoadException extends IOException {
    private static final String MESSAGE_FORMAT = "Cannot load JAR at [%s]: %s";
    private static final long serialVersionUID = 1655276726721040696L;

    public ExternalJarLoadException(final Path jarPath, final String reason) {
        super(String.format(MESSAGE_FORMAT, jarPath, reason));
    }

    public ExternalJarLoadException(final Path jarPath, final String reason, final Throwable cause) {
        super(String.format(MESSAGE_FORMAT, jarPath, reason), cause);
    }
}
