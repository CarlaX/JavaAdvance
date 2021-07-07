package com.fzw.dubboprovider.config;

import com.fzw.dubbocommon.config.DynamicRoutingDatasource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    //    @Bean
    public DataSource masterDataSource() {
        return new HikariDataSource(this.masterHikariConfig());
    }

    //    @Bean
    public DataSource slaveOneDataSource() {
        return new HikariDataSource(this.slaveOneHikariConfig());
    }

    @Bean
//    @Primary
    public DynamicRoutingDatasource dynamicDatasource() {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("slave-one");
        Map<Object, Object> map = new HashMap<>();
        DataSource masterDataSource = this.masterDataSource();
        DataSource slaveOneDataSource = this.slaveOneDataSource();
        map.put("master", masterDataSource);
        map.put("slave-one", slaveOneDataSource);
        return new DynamicRoutingDatasource(objects, "master", map, masterDataSource);
    }
}
