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
package com.anyilanxin.kunpeng.util.health;

import java.time.Instant;

/**
 * A health issue contains information about the cause for unhealthy/dead components. It can either
 * be a string message, a {@link Throwable} or another {@link HealthReport}.
 */
public record HealthIssue(String message, Throwable throwable, HealthReport cause, Instant since) {

    public static HealthIssue of ( final String message, final Instant since){
        return new HealthIssue(message, null, null, since);
    }

    public static HealthIssue of ( final Throwable throwable, final Instant since){
        return new HealthIssue(null, throwable, null, since);
    }

    public static HealthIssue of ( final HealthReport cause, final Instant since){
        return new HealthIssue(null, null, cause, since);
    }
}
