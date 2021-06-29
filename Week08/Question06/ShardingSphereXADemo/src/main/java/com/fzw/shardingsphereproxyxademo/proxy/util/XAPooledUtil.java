package com.fzw.shardingsphereproxyxademo.proxy.util;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import javax.transaction.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author fzw
 * @description
 * @date 2021-06-29
 **/
@Slf4j
public class XAPooledUtil {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(
                "jdbc:mysql://localhost:3316/sharding_db?" +
                        "serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&" +
                        "useAffectedRows=true&allowPublicKeyRetrieval=true&allowMultiQueries=true" +
                        "&rewriteBatchedStatements=true");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("root");
        dataSource = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            dataSource.evictConnection(connection);
        }
    }

    public static void closePool(){
        dataSource.close();
    }
}
