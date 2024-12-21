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

    public static final String JAR_EXTENSION = ".jar";
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalJarRepository.class);
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
