package com.example.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;

/**
 * Spring configuration java class.
 */
@Configuration
@ComponentScan({"com.example"})
public class DAOConfiguration {

    /**
     * The Database name.
     */
    @Value("${database.name}")
    private String databaseName;

    /**
     * The Database url.
     */
    @Value("${database.url}")
    private String databaseUrl;
    /**
     * The Database driver class.
     */
    @Value("${database.driverClassName}")
    private String databaseDriverClass;
    /**
     * The Db user name.
     */
    @Value("${database.username}")
    private String dbUserName;
    /**
     * The Db password.
     */
    @Value("${database.password}")
    private String dbPassword;


    /**
     * Gets data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(databaseDriverClass);
        dataSource.setUrl(databaseUrl + databaseName);
        dataSource.setUsername(dbUserName);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    /**
     * Property placeholder configurer.
     *
     * @return the property sources placeholder configurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
