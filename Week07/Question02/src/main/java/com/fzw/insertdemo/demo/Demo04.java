package com.fzw.insertdemo.demo;

import com.fzw.insertdemo.util.PooledUtil;
import com.fzw.insertdemo.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.util.ExecutorServices;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author fzw
 * @description
 **/
@Slf4j
public class Demo04 {

    private LocalDateTime before;
    private LocalDateTime after;
    private Random random = new Random(System.currentTimeMillis());
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(16);
    private CountDownLatch countDownLatch = new CountDownLatch(10);

    @BeforeEach
    public void beforeTest() {

        this.before = TimeUtil.currentLocalDateTime();
        log.info("插入开始：{}", TimeUtil.localDateTimeFormat(before));
    }

    @AfterEach
    public void afterTest() {
        this.after = TimeUtil.currentLocalDateTime();
        log.info("插入结束：{}", TimeUtil.localDateTimeFormat(after));
        log.info("花费时间：{}", TimeUtil.durationFormat(Duration.between(this.before, this.after)));
    }

    /**
     * 100w条insert语句，使用jdbc batch，使用10个子线程，使用rewriteBatchedStatements参数
     * 花费时间：0天 0:0:7.392 总计秒数：7秒
     */
    @Test
    public void test() {
        String sql = "insert into `order` (user_id,total_price,total_amount,create_time,pay_time,finish_time) values (?,?,?,?,?,?)";

        for (int i = 0; i < 10; i++) {
            fixedThreadPool.submit(() -> {
                Connection connection = null;
                try {
                    connection = PooledUtil.getConnection();
                    connection.setAutoCommit(false);
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (int j = 0; j < 100000; j++) {
                        statement.setInt(1, this.random.nextInt(3));
                        statement.setBigDecimal(2, BigDecimal.valueOf(this.random.nextDouble()).setScale(2, RoundingMode.CEILING));
                        statement.setInt(3, this.random.nextInt(10));
                        LocalDateTime now = TimeUtil.currentLocalDateTime();
                        statement.setTimestamp(4, TimeUtil.localDateTime2SqlTimeStamp(now));
                        statement.setTimestamp(5, TimeUtil.localDateTime2SqlTimeStamp(now));
                        statement.setTimestamp(6, TimeUtil.localDateTime2SqlTimeStamp(now));
                        statement.addBatch();
                    }
                    statement.executeBatch();
                    connection.commit();
                } catch (SQLException e) {
                    try {
                        connection.rollback();
                    } catch (SQLException e2) {
                        e2.printStackTrace();
                    }
                    e.printStackTrace();
                } finally {
                    PooledUtil.releaseConnection(connection);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
