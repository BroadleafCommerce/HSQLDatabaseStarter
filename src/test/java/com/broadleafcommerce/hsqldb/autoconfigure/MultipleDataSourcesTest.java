/*-
 * #%L
 * BroadleafCommerce HSQLDB Database Starter
 * %%
 * Copyright (C) 2009 - 2019 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
/**
 * 
 */
package com.broadleafcommerce.hsqldb.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.broadleafcommerce.hsqldb.autoconfigure.HSQLDatabaseAutoConfiguration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class MultipleDataSourcesTest {

    @EnableAutoConfiguration
    @Configuration
    static class LegacyMergedDSConfig {

        @Bean
        public Map<String, String> blMergedDataSources() {
            return new HashMap<>();
        }
    }

    @Test
    void multipleDatasourcesWithLegacy() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(HSQLDatabaseAutoConfiguration.class))
                .withUserConfiguration(LegacyMergedDSConfig.class)
                .run(ctx -> {
                    String[] beans = ctx.getBeanNamesForType(DataSource.class);
                    assertThat(beans).containsExactlyInAnyOrder("blDS",
                            "webSecureDS",
                            "webStorageDS",
                            "webEventDS");
                });
    }

    @Test
    void singleDatasourceWithoutLegacy() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(HSQLDatabaseAutoConfiguration.class))
                .run(ctx -> {
                    String[] beans = ctx.getBeanNamesForType(DataSource.class);
                    assertThat(beans).containsExactlyInAnyOrder("blDS");
                });
    }
}
