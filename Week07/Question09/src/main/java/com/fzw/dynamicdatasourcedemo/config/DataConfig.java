package com.fzw.dynamicdatasourcedemo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author fzw
 * @description
 * @date 2021-06-24
 **/
@Configuration
public class DataConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public HikariConfig masterHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave-one")
    public HikariConfig slaveOneHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave-two")
    public HikariConfig slaveTwoHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource masterDataSource() {
        return new HikariDataSource(this.masterHikariConfig());
    }

    @Bean
    public DataSource slaveOneDataSource() {
        return new HikariDataSource(this.slaveOneHikariConfig());
    }

    @Bean
    public DataSource slaveTwoDataSource() {
        return new HikariDataSource(this.slaveTwoHikariConfig());
    }

    @Bean
    public DynamicDatasource dynamicDatasource() {
        return new DynamicDatasource(List.of("slave-one", "slave-tow"), "master",
                Map.of("master", this.masterDataSource(),
                        "slave-one", this.slaveOneDataSource(),
                        "slave-tow", this.slaveTwoDataSource()),
                this.masterDataSource());
    }
}
