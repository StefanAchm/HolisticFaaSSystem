package com.asi.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataInitializationConfig {

    private DataSource dataSource;

    public DataInitializationConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public void initDatabase() {

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        // Empty the tables

        populator.addScript(new ClassPathResource("data-users.sql"));
        populator.addScript(new ClassPathResource("data-functions.sql"));

        populator.execute(dataSource);

    }

}
