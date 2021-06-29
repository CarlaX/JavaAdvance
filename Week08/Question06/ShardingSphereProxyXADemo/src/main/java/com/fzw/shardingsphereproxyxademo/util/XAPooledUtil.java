package com.fzw.shardingsphereproxyxademo.util;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import lombok.extern.slf4j.Slf4j;

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

    private static AtomikosDataSourceBean atomikosDataSourceBean;
    private static AtomikosDataSourceBean atomikosDataSourceBeanSlave;
    private static UserTransactionManager userTransactionManager;
    public static final String MASTER = "master";
    public static final String SLAVE = "slave";

    static {
        XAPooledUtil.atomikosDataSourceBean = new AtomikosDataSourceBean();
        XAPooledUtil.atomikosDataSourceBean.setUniqueResourceName("mysql_xa");
        XAPooledUtil.atomikosDataSourceBean.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");
        properties.setProperty("url",
                "jdbc:mysql://localhost:3306/orderserver?serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true&allowMultiQueries=true");
        XAPooledUtil.atomikosDataSourceBean.setXaProperties(properties);

        XAPooledUtil.atomikosDataSourceBeanSlave = new AtomikosDataSourceBean();
        XAPooledUtil.atomikosDataSourceBeanSlave.setUniqueResourceName("mysql_xa_slave");
        XAPooledUtil.atomikosDataSourceBeanSlave.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        Properties properties_2 = new Properties();
        properties_2.setProperty("user", "root");
        properties_2.setProperty("password", "root");
        properties_2.setProperty("url",
                "jdbc:mysql://localhost:3307/orderserver?serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true&allowMultiQueries=true");
        XAPooledUtil.atomikosDataSourceBeanSlave.setXaProperties(properties_2);


        XAPooledUtil.userTransactionManager = new UserTransactionManager();
        try {
            XAPooledUtil.userTransactionManager.init();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String type) {
        Connection connection = null;
        try {
            if (XAPooledUtil.MASTER.equals(type)) {
                connection = XAPooledUtil.atomikosDataSourceBean.getConnection();
            } else {
                connection = XAPooledUtil.atomikosDataSourceBeanSlave.getConnection();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    public static void beginXA() {
        try {
            XAPooledUtil.userTransactionManager.begin();
        } catch (NotSupportedException | SystemException e) {
            e.printStackTrace();
        }
    }

    public static void commitXA() {
        try {
            XAPooledUtil.userTransactionManager.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            e.printStackTrace();
        }
    }

    public static void rollbackXA() {
        try {
            XAPooledUtil.userTransactionManager.rollback();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    public static void resumeXA(Transaction transaction) {
        try {
            XAPooledUtil.userTransactionManager.resume(transaction);
        } catch (InvalidTransactionException | SystemException e) {
            e.printStackTrace();
        }
    }

    public static Transaction suspendXA() {
        Transaction transaction = null;
        try {
            transaction = XAPooledUtil.userTransactionManager.suspend();
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return transaction;
    }
}
