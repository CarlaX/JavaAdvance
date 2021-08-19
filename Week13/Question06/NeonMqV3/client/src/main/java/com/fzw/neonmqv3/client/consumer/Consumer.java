package com.fzw.neonmqv3.client.consumer;

import com.fzw.neonmqv3.client.util.HttpUtil;
import com.fzw.neonmqv3.common.NeonMessage;
import com.fzw.neonmqv3.common.NeonResult;
import com.fzw.neonmqv3.common.UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
@Slf4j
public class Consumer {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("topic", "test_topic");
        map.put("name", "test_consumer");
        for (int i = 0; i < 2; i++) {
            NeonResult<NeonMessage<UserInfo>> result = HttpUtil.get("http://localhost:8080/neonMq/consume", map, NeonResult.class);
            log.info("{}", result);
//            NeonResult<Void> result1 = HttpUtil.post("http://localhost:8080/neonMq/ack", map, null, NeonResult.class);
//            log.info("{}", result1);

        }
    }
}
