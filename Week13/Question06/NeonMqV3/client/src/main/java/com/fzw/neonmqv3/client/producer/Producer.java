package com.fzw.neonmqv3.client.producer;

import com.fzw.neonmqv3.client.util.HttpUtil;
import com.fzw.neonmqv3.common.NeonMessage;
import com.fzw.neonmqv3.common.NeonResult;
import com.fzw.neonmqv3.common.UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
@Slf4j
public class Producer {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("topic", "test_topic");
        for (int i = 0; i < 6; i++) {
            UserInfo userInfo = new UserInfo("carla_" + i, i);
            NeonMessage<UserInfo> neonMessage = new NeonMessage<>(null, userInfo);
            NeonResult<Integer> result = HttpUtil.post("http://localhost:8080/neonMq/produce", map, neonMessage, NeonResult.class);
            log.info("{}", result.getData());
        }
    }
}
