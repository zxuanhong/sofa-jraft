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

import java.util.concurrent.Callable;

import org.agrona.LangUtil;

/**
 * A simple extension of runnable which allows for exceptions to be thrown.
 */
@FunctionalInterface
// ignore generic exception warning here as we want to allow for any exception to be thrown
@SuppressWarnings("java:S112")
public interface CheckedRunnable {
    void run() throws Exception;

    static java.lang.Runnable toUnchecked(final CheckedRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (final Exception e) {
                LangUtil.rethrowUnchecked(e);
            }
        };
    }

    static Callable<Void> toCallable(final CheckedRunnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }
}
