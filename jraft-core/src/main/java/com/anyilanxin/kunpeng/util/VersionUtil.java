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
package com.anyilanxin.kunpeng.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;

public final class VersionUtil {

    public static final Logger  LOG                        = Loggers.FILE_LOGGER;

    public static final String  VERSION_OVERRIDE_ENV_NAME  = "ZEEBE_VERSION_OVERRIDE";
    private static final String VERSION_PROPERTIES_PATH    = "/kunpeng.properties";
    private static final String VERSION_PROPERTY_NAME      = "kunpeng.version";
    private static final String LAST_VERSION_PROPERTY_NAME = "kunpeng.last.version";
    private static final String VERSION_DEV                = "development";

    private static String       version;
    private static String       versionLowerCase;
    private static String       lastVersion;

    private VersionUtil() {
    }

    /**
     * @return the current version or 'development' if none can be determined.
     */
    public static String getVersion() {
    if (version != null) {
      return version;
    }
    final var foundVersion =
        Optional.ofNullable(System.getenv(VERSION_OVERRIDE_ENV_NAME))
            .or(() -> Optional.ofNullable(readProperty(VERSION_PROPERTY_NAME)))
            .or(
                () ->
                    Optional.ofNullable(VersionUtil.class.getPackage().getImplementationVersion()));
    if (foundVersion.isPresent()) {
      version = foundVersion.get();
    } else {
      LOG.warn(
          "Version is not found in env, version file or package, using '%s' instead"
              .formatted(VERSION_DEV));
      version = VERSION_DEV;
    }
    return version;
  }

    /**
     * @return the current version if it can be parsed as a semantic version, otherwise empty.
     */
    public static Optional<SemanticVersion> getSemanticVersion() {
        return SemanticVersion.parse(getVersion());
    }

    public static String getVersionLowerCase() {
        if (versionLowerCase == null) {
            versionLowerCase = getVersion().toLowerCase();
        }
        return versionLowerCase;
    }

    /**
     * @return the previous stable version or null if none was found.
     */
    public static String getPreviousVersion() {
        if (lastVersion == null) {
            lastVersion = readProperty(LAST_VERSION_PROPERTY_NAME);
        }

        return lastVersion;
    }

    private static String readProperty(final String property) {
        try (final InputStream lastVersionFileStream = VersionUtil.class.getResourceAsStream(VERSION_PROPERTIES_PATH)) {
            final Properties props = new Properties();
            props.load(lastVersionFileStream);

            return props.getProperty(property);
        } catch (final IOException e) {
            LOG.error(String.format("Can't read version file: %s", VERSION_PROPERTIES_PATH), e);
        }

        return null;
    }
}
