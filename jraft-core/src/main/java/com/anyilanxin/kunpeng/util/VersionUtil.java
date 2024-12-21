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

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;

public final class VersionUtil {

  public static final Logger LOG = Loggers.FILE_LOGGER;

  public static final String VERSION_OVERRIDE_ENV_NAME = "ZEEBE_VERSION_OVERRIDE";
  private static final String VERSION_PROPERTIES_PATH = "/kunpeng.properties";
  private static final String VERSION_PROPERTY_NAME = "kunpeng.version";
  private static final String LAST_VERSION_PROPERTY_NAME = "kunpeng.last.version";
  private static final String VERSION_DEV = "development";

  private static String version;
  private static String versionLowerCase;
  private static String lastVersion;

  private VersionUtil() {}

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
    try (final InputStream lastVersionFileStream =
        VersionUtil.class.getResourceAsStream(VERSION_PROPERTIES_PATH)) {
      final Properties props = new Properties();
      props.load(lastVersionFileStream);

      return props.getProperty(property);
    } catch (final IOException e) {
      LOG.error(String.format("Can't read version file: %s", VERSION_PROPERTIES_PATH), e);
    }

    return null;
  }
}
