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
