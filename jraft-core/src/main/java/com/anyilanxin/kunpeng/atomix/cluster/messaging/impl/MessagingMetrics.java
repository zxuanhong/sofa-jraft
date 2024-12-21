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
package com.anyilanxin.kunpeng.atomix.cluster.messaging.impl;

import com.anyilanxin.kunpeng.util.CloseableSilently;

public interface MessagingMetrics {

  CloseableSilently startRequestTimer(String name);

  void observeRequestSize(String to, String name, int requestSizeInBytes);

  void countMessage(String to, String name);

  void countRequestResponse(String to, String name);

  void countSuccessResponse(String address, String name);

  void countFailureResponse(String address, String name, String error);

  void incInFlightRequests(String address, String topic);

  void decInFlightRequests(String address, String topic);
}
