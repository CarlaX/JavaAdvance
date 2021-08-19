package com.fzw.neonmqv3.broker.mq;

import com.fzw.neonmqv3.common.NeonMessage;
import com.fzw.neonmqv3.common.NeonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
@Slf4j
@RestController
@RequestMapping(path = "/neonMq")
public class NeonMqController {

    @PostMapping(path = "/produce")
    public NeonResult<Integer> produce(@RequestParam("topic") String topic, @RequestBody NeonMessage<?> message) {
        NeonQueue neonQueue = NeonBroker.DEFAULT.createTopic(topic);
        log.info("{},{}", topic, message);
        int write = neonQueue.write(message);
        return new NeonResult<>(100, "", write);
    }

    @GetMapping(path = "/consume")
    public NeonResult<NeonMessage<?>> consume(@RequestParam("topic") String topic, @RequestParam("name") String name) {
        NeonQueue neonQueue = NeonBroker.DEFAULT.getTopic(topic);
        if (neonQueue == null) {
            return new NeonResult<>(100, "", null);
        }
        NeonMessage<?> read = neonQueue.read(name);
        return new NeonResult<>(100, "", read);
    }

    @PostMapping(path = "/ack")
    public NeonResult<Void> ack(@RequestParam("topic") String topic, @RequestParam("name") String name) {
        NeonQueue neonQueue = NeonBroker.DEFAULT.getTopic(topic);
        if (neonQueue == null) {
            return new NeonResult<>(100, "", null);
        }
        neonQueue.ack(name);
        return new NeonResult<>(100, "", null);
    }

}
