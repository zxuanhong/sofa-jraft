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
import java.net.*;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a class loader which isolates external exporters from other exporters, while exposing
 * our own code to ensure versions match at runtime.
 *
 * <p>NOTE: if you forget to close this class loader, the underlying file is cleaned up by the
 * garbage collector (via {@link java.lang.ref.Cleaner} once this class loader has been garbage
 * collected.
 *
 * <p>If possible, it's still a good idea to close explicitly to free resources sooner and to avoid
 * depending on undocumented behavior of a standard library class.
 */
public final class ExternalJarClassLoader extends URLClassLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalJarClassLoader.class);

    private static final String JAVA_PACKAGE_PREFIX = "java.";
    private static final String JAR_URL_FORMAT = "jar:%s!/";

    private ExternalJarClassLoader(final URL[] urls) {
        super(urls);
    }

    /**
     * Close class loader with verbose log statement.
     *
     * <p>Be aware that premature closing may lead to ClassNotFoundException.
     */
    @Override
    public void close() throws IOException {
        close(true);
    }

    /**
     * Allows to close the class loader without warning statement
     *
     * <p>Be aware that premature closing may lead to ClassNotFoundException.
     */
    public void close(final boolean verbose) throws IOException {
        if (verbose) {
            LOGGER.warn(
                    "Closing the class loader may cause future class loading calls to fail with ClassNotFoundException");
        }
        super.close();
    }

    public static ExternalJarClassLoader ofPath(final Path jarPath) throws ExternalJarLoadException {
        final URL jarUrl;

        try {
            final String expandedPath = jarPath.toUri().toURL().toString();
            jarUrl = new URI(String.format(JAR_URL_FORMAT, expandedPath)).toURL();
        } catch (final MalformedURLException | URISyntaxException e) {
            throw new ExternalJarLoadException(jarPath, "bad JAR url", e);
        }

        return new ExternalJarClassLoader(new URL[]{jarUrl});
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            if (name.startsWith(JAVA_PACKAGE_PREFIX)) {
                return findSystemClass(name);
            }

            Class<?> clazz = findLoadedClass(name);
            if (clazz == null) {
                try {
                    clazz = findClass(name);
                } catch (final ClassNotFoundException ex) {
                    LOGGER.trace("Failed to load class {}, falling back to parent class loader", name, ex);
                    clazz = super.loadClass(name);
                }
            }

            return clazz;
        }
    }
}
