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
package com.anyilanxin.kunpeng.util.error;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;

/**
 * FatalErrorHandler can be used to handle all {@link Throwable}s safely and consistently.
 * Implementations interpret a throwable and take <i>some</i> action when the throwable is
 * considered fatal.
 *
 * @see VirtualMachineErrorHandler
 */
public interface FatalErrorHandler {
    /**
     * Handles arbitrary {@link Throwable}s. Use this method whenever catching a {@link Throwable},
     * before carrying on with your regular error handling.
     *
     * <p>{@link VirtualMachineErrorHandler} will exit on all {@link VirtualMachineError}s
     *
     * @param e the throwable
     */
    void handleError(Throwable e);

    /**
     * Builds a {@link FatalErrorHandler} that can be used as the default uncaught exception handler
     */
    static UncaughtExceptionHandler uncaughtExceptionHandler(final Logger logger) {
        return new VirtualMachineErrorHandler(logger);
    }

    /**
     * Builds the default {@link FatalErrorHandler}
     */
    static FatalErrorHandler withLogger(final Logger logger) {
        return new VirtualMachineErrorHandler(logger);
    }
}
