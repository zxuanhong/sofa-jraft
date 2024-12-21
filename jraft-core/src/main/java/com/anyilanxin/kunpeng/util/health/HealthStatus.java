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

import java.util.Comparator;

/**
 * Enum cases are ordered based on the "severity" of the status. The ordinal field of the
 * enumeration implicitly determines its severity.
 */
public enum HealthStatus {
    HEALTHY,
    UNHEALTHY,
    DEAD;

    /**
     * The comparator just uses the ordinal field to order the statuses
     */
    public static final Comparator<HealthStatus> COMPARATOR =
            Comparator.comparingInt(HealthStatus::ordinal);

    /**
     * Symmetric binary operations to reduce HealthStatus
     *
     * @return an HealthStatus using the following rules:
     * <ul>
     *   <li>if any of the two is DEAD -> DEAD
     *   <li>if any of the two is UNHEALTHY -> UNHEALTHY
     *   <li>if both are HEALTHY -> HEALTHY
     * </ul>
     */
    public HealthStatus combine(final HealthStatus other) {
        return compareTo(other) > 0 ? this : other;
    }
}
