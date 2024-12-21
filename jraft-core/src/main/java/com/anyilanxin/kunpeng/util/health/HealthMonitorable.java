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
 * Any component that can be monitored for health should implement this interface.
 */
public interface HealthMonitorable {

    /**
     * Used by a HealthMonitor to get the name of this component. Most `HealthMonitorable`s override
     * this method to return their actor name.
     *
     * @return the name of this component
     */
    String componentName();

    /**
     * Used by a HealthMonitor to get the health status of this component, typically invoked
     * periodically. Implementation should be thread safe
     *
     * @return health status
     */
    HealthReport getHealthReport();

    /**
     * Register a failure observer.
     *
     * @param failureListener failure observer to be invoked when a failure that affects the health
     *                        status of this component occurs
     */
    void addFailureListener(FailureListener failureListener);

    /**
     * Removes a previously registered listener. Should do nothing if it was not previously
     * registered.
     *
     * @param failureListener the failure listener to remove
     */
    void removeFailureListener(FailureListener failureListener);
}
