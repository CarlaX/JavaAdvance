package com.fzw.insertdemo.demo;

import com.fzw.insertdemo.util.PooledUtil;
import com.fzw.insertdemo.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
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

/**
 * @author fzw
 * @description
 **/
@Slf4j
public class Demo01 {

    private LocalDateTime before;
    private LocalDateTime after;
    private Random random = new Random(System.currentTimeMillis());

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
     * 100w条insert语句
     * 花费时间：0天 0:2:33.600 总计秒数：153秒
     */
    @Test
    public void test() {
        Connection connection = null;
        try {
            connection = PooledUtil.getConnection();
            connection.setAutoCommit(false);
            String sql = "insert into `order` (user_id,total_price,total_amount,create_time,pay_time,finish_time) values (?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < 1000000; i++) {
                statement.setInt(1, this.random.nextInt(3));
                statement.setBigDecimal(2, BigDecimal.valueOf(this.random.nextDouble()).setScale(2, RoundingMode.CEILING));
                statement.setInt(3, this.random.nextInt(10));
                LocalDateTime now = TimeUtil.currentLocalDateTime();
                statement.setTimestamp(4, TimeUtil.localDateTime2SqlTimeStamp(now));
                statement.setTimestamp(5, TimeUtil.localDateTime2SqlTimeStamp(now));
                statement.setTimestamp(6, TimeUtil.localDateTime2SqlTimeStamp(now));
                statement.execute();
            }
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
        }
    }

}
