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

/**
 * Failure Listener invoked by a {@link HealthMonitorable} component.
 */
public interface FailureListener {

    /**
     * Invoked when the health status becomes unhealthy.
     */
    void onFailure(HealthReport report);

    /**
     * Invoked when health status becomes healthy after being unhealthy for some time. A component can
     * be marked unhealthy initially and set to healthy only after start up is complete. It is
     * expected to call {#onRecovered} when it is marked as healthy.
     */
    void onRecovered(HealthReport report);

    /**
     * Invoked when the health status becomes dead and the system can't become healthy again without
     * external intervention.
     */
    void onUnrecoverableFailure(HealthReport report);

    default void onHealthReport(final HealthReport healthReport) {
        switch (healthReport.getStatus()) {
            case HEALTHY -> onRecovered(healthReport);
            case UNHEALTHY -> onFailure(healthReport);
            case DEAD -> onUnrecoverableFailure(healthReport);
            default -> {
            }
        }
    }
}
