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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public record FeatureFlags(
        boolean yieldingDueDateChecker,
        boolean enableActorMetrics,
        boolean enableMessageTTLCheckerAsync,
        boolean enableTimerDueDateCheckerAsync,
        boolean enableStraightThroughProcessingLoopDetector,
        boolean enablePartitionScaling
        /*, boolean foo*/) {

    /* To add a new feature toggle, please follow these steps:
     *
     * - add a record property to this class, and extend the test for this class
     *
     * - define the default value. When introducing a new feature flag the default
     *   value should be 'false'. This way the feature is disabled by default
     *   for all customers who do not change their configuration.
     *   As we gain more confidence in the efficacy of the feature flag, the
     *   default value can be set to 'true'
     *
     * - define a default value to be used in tests
     *
     * - make sure that all relevant tests use the default value for tests
     *
     * - add a field, getter and setter to FeatureFlagsCfg
     *
     * - add a description of the feature flag to
     *    - dist/src/main/config/broker.standalone.yaml.template
     *    - dist/src/main/config/broker.yaml.template
     *
     * - add test cases to FeaturesFlagCfgTest and feature-flags-cfg.yaml
     *
     * Be careful with parameter order in constructor calls!
     */

    //  protected static final boolean FOO_DEFAULT = false;

    private static final boolean YIELDING_DUE_DATE_CHECKER = true;
    private static final boolean ENABLE_ACTOR_METRICS = false;

    private static final boolean ENABLE_MSG_TTL_CHECKER_ASYNC = false;
    private static final boolean ENABLE_DUE_DATE_CHECKER_ASYNC = false;
    private static final boolean ENABLE_STRAIGHT_THOUGH_PROCESSING_LOOP_DETECTOR = true;
    private static final boolean ENABLE_PARTITION_SCALING = false;

    public static FeatureFlags createDefault () {
        return new FeatureFlags(
                YIELDING_DUE_DATE_CHECKER,
                ENABLE_ACTOR_METRICS,
                ENABLE_MSG_TTL_CHECKER_ASYNC,
                ENABLE_DUE_DATE_CHECKER_ASYNC,
                ENABLE_STRAIGHT_THOUGH_PROCESSING_LOOP_DETECTOR,
                ENABLE_PARTITION_SCALING
                /*, FOO_DEFAULT*/);
    }

    /**
     * Only to be used in tests
     *
     * @return
     */
    public static FeatureFlags createDefaultForTests () {
        return new FeatureFlags(
                true, /* YIELDING_DUE_DATE_CHECKER*/
                false, /* ENABLE_ACTOR_METRICS */
                true, /* ENABLE_MSG_TTL_CHECKER_ASYNC */
                true, /* ENABLE_DUE_DATE_CHECKER_ASYNC */
                true, /* ENABLE_STRAIGHT_THOUGH_PROCESSING_LOOP_DETECTOR */
                true /* ENABLE_PARTITION_SCALING */
                /*, FOO_DEFAULT*/);
    }

    @Override
    public String toString () {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
