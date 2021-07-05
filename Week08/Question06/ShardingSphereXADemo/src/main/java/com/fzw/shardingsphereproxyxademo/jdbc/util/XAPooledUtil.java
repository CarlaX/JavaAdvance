package com.fzw.shardingsphereproxyxademo.jdbc.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.connection.ShardingSphereConnection;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author fzw
 * @description
 * @date 2021-06-29
 **/
@Slf4j
public class XAPooledUtil {
    private static ShardingSphereDataSource shardingSphereDataSource;
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String username = "root";
    private static String password = "root";

    static {
        HikariConfig masterConfig = new HikariConfig();
        masterConfig.setPoolName("master");
        masterConfig.setDriverClassName(driverName);
        masterConfig.setUsername(username);
        masterConfig.setPassword(password);
        masterConfig.setJdbcUrl(
                "jdbc:mysql://localhost:3306/ordersharding?serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true&allowMultiQueries=true&rewriteBatchedStatements=true");
        HikariDataSource master = new HikariDataSource(masterConfig);

        HikariConfig slaveConfig = new HikariConfig();
        slaveConfig.setPoolName("slave");
        slaveConfig.setDriverClassName(driverName);
        slaveConfig.setUsername(username);
        slaveConfig.setPassword(password);
        slaveConfig.setJdbcUrl(
                "jdbc:mysql://localhost:3307/ordersharding?serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true&allowMultiQueries=true&rewriteBatchedStatements=true");
        HikariDataSource slave = new HikariDataSource(slaveConfig);


        Map<String, DataSource> dataSourceMap = Map.of("ds_0", master, "ds_1", slave);
        Properties properties = new Properties();
        properties.setProperty("sql-show", "true");

        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        ShardingTableRuleConfiguration userTableRule = new ShardingTableRuleConfiguration("user", "ds_${0..1}.user_${0..3}");
        userTableRule.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("id", "snowflake"));
        userTableRule.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "user_inline"));

        ShardingTableRuleConfiguration orderTableRule = new ShardingTableRuleConfiguration("order", "ds_${0..1}.order_${0..3}");
        orderTableRule.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("id", "snowflake"));
        orderTableRule.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "order_inline"));

        shardingRuleConfiguration.setTables(List.of(userTableRule, orderTableRule));
        shardingRuleConfiguration.setBindingTableGroups(List.of("user", "order"));
        shardingRuleConfiguration.setDefaultDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("id", "database_inline"));

        Properties database_inline_properties = new Properties();
        database_inline_properties.setProperty("algorithm-expression", "ds_${id % 2}");
//        直接单独添加
//        shardingRuleConfiguration.getShardingAlgorithms().put("database_inline", new ShardingSphereAlgorithmConfiguration("INLINE", database_inline_properties));

        Properties user_inline_properties = new Properties();
        user_inline_properties.setProperty("algorithm-expression", "user_${id % 4}");
//        shardingRuleConfiguration.getShardingAlgorithms().put("user_inline", new ShardingSphereAlgorithmConfiguration("INLINE", user_inline_properties));

        Properties order_inline_properties = new Properties();
        order_inline_properties.setProperty("algorithm-expression", "order_${id % 4}");
//        shardingRuleConfiguration.getShardingAlgorithms().put("order_inline", new ShardingSphereAlgorithmConfiguration("INLINE", order_inline_properties));

//        一并添加
        shardingRuleConfiguration.setShardingAlgorithms(Map.of(
                "database_inline", new ShardingSphereAlgorithmConfiguration("INLINE", database_inline_properties),
                "user_inline", new ShardingSphereAlgorithmConfiguration("INLINE", user_inline_properties),
                "order_inline", new ShardingSphereAlgorithmConfiguration("INLINE", order_inline_properties)));

        Properties snowflake_properties = new Properties();
        snowflake_properties.setProperty("work-id", "123");
        shardingRuleConfiguration.setKeyGenerators(Map.of("snowflake", new ShardingSphereAlgorithmConfiguration("SNOWFLAKE", snowflake_properties)));

        try {
            shardingSphereDataSource = new ShardingSphereDataSource(dataSourceMap, List.of(shardingRuleConfiguration), properties);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        TransactionTypeHolder.set(TransactionType.XA);
    }

    public static ShardingSphereConnection getConnection() {
        return XAPooledUtil.shardingSphereDataSource.getConnection();
    }

}
