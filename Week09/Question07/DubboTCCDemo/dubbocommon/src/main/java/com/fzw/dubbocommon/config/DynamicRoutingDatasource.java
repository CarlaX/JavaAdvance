package com.fzw.dubbocommon.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author fzw
 * @description
 * @date 2021-06-24
 **/
@Slf4j
public class DynamicRoutingDatasource extends AbstractRoutingDataSource {
    public static final String MASTER_KEY = "MASTER";
    public static final String SLAVE_KEY = "SLAVE";
    private static final ThreadLocal<String> KEY_CONTEXT = new ThreadLocal<>();
    private int slaveIndex;
    private final List<String> slaveKeyList;
    private final String masterKey;
    private final int slaveSize;

    public DynamicRoutingDatasource(List<String> slaveKeyList, String masterKey, Map<Object, Object> targetDataSources, DataSource defaultTargetDataSource) {
        this.slaveKeyList = slaveKeyList;
        this.masterKey = masterKey;
        this.slaveSize = slaveKeyList.size();
        this.slaveIndex = 0;
        super.setTargetDataSources(targetDataSources);
//        super.setDefaultTargetDataSource(null);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        log.info(DynamicRoutingDatasource.KEY_CONTEXT.get());
        String key = DynamicRoutingDatasource.KEY_CONTEXT.get();
        log.info("选择的key：{}", key);
        if (DynamicRoutingDatasource.MASTER_KEY.equals(key)) {
            return this.masterKey;
        } else if (DynamicRoutingDatasource.SLAVE_KEY.equals(key)) {
            return this.selectSlaveKey();
        } else {
            return null;
        }
    }

    private synchronized String selectSlaveKey() {
        int index = this.slaveIndex % this.slaveSize;
        this.slaveIndex = index + 1;
        return this.slaveKeyList.get(0);
    }

    public static void setKeyContextContent(String key) {
        log.info("setKeyContextContent: {}", key);
        DynamicRoutingDatasource.KEY_CONTEXT.set(key);
    }

    public static void clearKeyContextContent() {
        log.info("clearKeyContextContent");
        DynamicRoutingDatasource.KEY_CONTEXT.remove();
    }
}
