package com.fzw.shardingspherespringdemo.demo;

import com.fzw.shardingspherespringdemo.mapper.UserMapper;
import com.fzw.shardingspherespringdemo.pojo.po.UserPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
//import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * @author fzw
 * @description
 * @date 2021-06-24
 **/
@Slf4j
@SpringBootTest
public class Demo01 {

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    private UserMapper userMapper;

//    @Test
//    public void test01() {
//
//        for (int i = 0; i < 5; i++) {
//            Connection connection = null;
//            try {
//                connection = this.dataSource.getConnection();
//
////                log.info("{}", this.dataSource.getClass().getName());
////                ShardingSphereDataSource shardingSphereDataSource = (ShardingSphereDataSource) this.dataSource;
////                log.info("{}", shardingSphereDataSource.getDataSourceMap());
////                ShardingSphereMetaData shardingSphereMetaData = shardingSphereDataSource.getMetaDataContexts().getDefaultMetaData();
////                log.info("DefaultMetaData: {},{},{}", shardingSphereMetaData.getName(), shardingSphereMetaData.getResource().getDatabaseType(),
////                        shardingSphereMetaData.getResource().getDataSourcesMetaData().getAllInstanceDataSourceNames());
////                DatabaseMetaData metaData = connection.getMetaData();
////                String url = metaData.getURL();
////                boolean readOnly = metaData.isReadOnly();
////                log.info("{},{}", readOnly, url);
//
//                connection.setAutoCommit(false);
//                PreparedStatement preparedStatement = connection.prepareStatement("select * from user");
//                ResultSet resultSet = preparedStatement.executeQuery();
//
//                resultSet.next();
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("password");
//
//                log.info("用户名：{},密码：{}", username, password);
//                connection.commit();
//            } catch (SQLException e) {
//                try {
//                    e.printStackTrace();
//                    connection.rollback();
//                } catch (SQLException e2) {
//                    e2.printStackTrace();
//                }
//            } finally {
//            }
//        }
//    }


    @Test
    public void test01() {
        for (int i = 0; i < 10; i++) {
            List<UserPO> userPOList = this.userMapper.queryAllUser();
            userPOList.forEach((userPO) -> {
                log.info("{}", userPO);
            });
        }
    }
}
