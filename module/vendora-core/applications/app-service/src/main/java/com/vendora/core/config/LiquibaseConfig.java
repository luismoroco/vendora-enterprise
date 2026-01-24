package com.vendora.core.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Value("${DB_HOST:localhost}")
    private String dbHost;

    @Value("${DB_PORT:5432}")
    private String dbPort;

    @Value("${DB_NAME:vendora}")
    private String dbName;

    @Value("${DB_USER:admin}")
    private String dbUser;

    @Value("${DB_PASSWORD:admin123}")
    private String dbPassword;

    @Value("${spring.liquibase.change-log:classpath:/db/changelog/db.changelog-master.yaml}")
    private String changeLog;

    @Value("${spring.liquibase.enabled:true}")
    private boolean enabled;

    @Bean
    public DataSource liquibaseDataSource() {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(dbUser)
                .password(dbPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    public SpringLiquibase liquibase(DataSource liquibaseDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(liquibaseDataSource);
        liquibase.setChangeLog(changeLog);
        liquibase.setShouldRun(enabled);
        liquibase.setDefaultSchema("public");
        return liquibase;
    }
}

