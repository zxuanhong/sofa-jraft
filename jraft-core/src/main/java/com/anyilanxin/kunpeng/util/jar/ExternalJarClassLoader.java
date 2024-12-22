/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    private static final Logger LOGGER              = LoggerFactory.getLogger(ExternalJarClassLoader.class);

    private static final String JAVA_PACKAGE_PREFIX = "java.";
    private static final String JAR_URL_FORMAT      = "jar:%s!/";

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
            LOGGER
                .warn("Closing the class loader may cause future class loading calls to fail with ClassNotFoundException");
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

        return new ExternalJarClassLoader(new URL[] { jarUrl });
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
