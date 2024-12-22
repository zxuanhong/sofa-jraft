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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maintains a map of all loaded external JARs and their corresponding class loaders for quick
 * reuse.
 */
public final class ExternalJarRepository implements AutoCloseable {

    public static final String                      JAR_EXTENSION = ".jar";
    private static final Logger                     LOGGER        = LoggerFactory
                                                                      .getLogger(ExternalJarRepository.class);
    private final Map<Path, ExternalJarClassLoader> loadedJars;

    public ExternalJarRepository() {
        this(new HashMap<>());
    }

    public ExternalJarRepository(final Map<Path, ExternalJarClassLoader> loadedJars) {
        this.loadedJars = loadedJars;
    }

    public Map<Path, ExternalJarClassLoader> getJars() {
        return Collections.unmodifiableMap(loadedJars);
    }

    public ExternalJarClassLoader remove(final String jarPath) {
        return remove(Paths.get(jarPath));
    }

    public ExternalJarClassLoader remove(final Path jarPath) {
        return loadedJars.remove(jarPath);
    }

    public ExternalJarClassLoader load(final String jarPath) throws ExternalJarLoadException {
        return load(Paths.get(jarPath));
    }

    public ExternalJarClassLoader load(final Path jarPath) throws ExternalJarLoadException {
        ExternalJarClassLoader classLoader = loadedJars.get(jarPath);

        if (classLoader == null) {
            verifyJarPath(jarPath);

            classLoader = ExternalJarClassLoader.ofPath(jarPath);
            loadedJars.put(jarPath, classLoader);
        }

        return classLoader;
    }

    @Override
    public void close() throws Exception {
        loadedJars.forEach(this::closeClassLoader);
    }

    /**
     * Verifies that the given path points to an existing, readable JAR file. Does not perform more
     * complex validation such as checking it is a valid JAR, verifying its signature, etc.
     *
     * @param path path to verify
     * @throws ExternalJarLoadException if it is not a JAR, not readable, or does not exist
     */
    private void verifyJarPath(final Path path) throws ExternalJarLoadException {
        final File jarFile = path.toFile();

        if (!jarFile.getName().endsWith(JAR_EXTENSION)) {
            throw new ExternalJarLoadException(path, "is not a JAR");
        }

        if (!jarFile.canRead()) {
            throw new ExternalJarLoadException(path, "is not readable");
        }
    }

    private void closeClassLoader(final Path path, final ExternalJarClassLoader classLoader) {
        try {
            classLoader.close(false);
        } catch (final Exception e) {
            LOGGER.warn("Failed to close external JAR class loader for path {}", path, e);
        }
    }
}
