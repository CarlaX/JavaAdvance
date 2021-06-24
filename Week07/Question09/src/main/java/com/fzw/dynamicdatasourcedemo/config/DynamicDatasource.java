package com.fzw.dynamicdatasourcedemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author fzw
 * @description
 * @date 2021-06-24
 **/
@Slf4j
public class DynamicDatasource extends AbstractRoutingDataSource {
    public static final String MASTER_KEY = "MASTER";
    public static final String SLAVE_KEY = "SLAVE";
    private static final ThreadLocal<String> KEY_CONTEXT = new ThreadLocal<>();
    private int slaveIndex;
    private final List<String> slaveKeyList;
    private final String masterKey;
    private final int slaveSize;

    public DynamicDatasource(List<String> slaveKeyList, String masterKey, Map<Object, Object> targetDataSources, DataSource defaultTargetDataSource) {
        this.slaveKeyList = slaveKeyList;
        this.masterKey = masterKey;
        this.slaveSize = slaveKeyList.size();
        this.slaveIndex = 0;
        super.setTargetDataSources(targetDataSources);
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String key = DynamicDatasource.KEY_CONTEXT.get();
        log.info("选择的key：{}", key);
        if (key.equals(DynamicDatasource.MASTER_KEY)) {
            return this.masterKey;
        } else if (key.equals(DynamicDatasource.SLAVE_KEY)) {
            return this.selectSlaveKey();
        } else {
            throw new RuntimeException("数据源key非法");
        }
    }

    private synchronized String selectSlaveKey() {
        int index = this.slaveIndex % this.slaveSize;
        this.slaveIndex = index + 1;
        return this.slaveKeyList.get(index);
    }

    public static void setKeyContextContent(String key) {
        DynamicDatasource.KEY_CONTEXT.set(key);
    }

    public static void clearKeyContextContent() {
        DynamicDatasource.KEY_CONTEXT.remove();
    }
}
