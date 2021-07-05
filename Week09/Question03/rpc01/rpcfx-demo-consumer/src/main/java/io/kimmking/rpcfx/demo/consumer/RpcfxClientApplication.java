package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.api.Filter;
import io.kimmking.rpcfx.api.LoadBalancer;
import io.kimmking.rpcfx.api.Router;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.client.NettyHttpClient;
import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.demo.api.pojo.User;
import io.kimmking.rpcfx.demo.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication(scanBasePackages = {"io.kimmking.rpcfx.demo.*"})
public class RpcfxClientApplication {

    // 二方库
    // 三方库 lib
    // nexus, userserivce -> userdao -> user
    //

    public static void main(String[] args) {

        // UserService service = new xxx();
        // service.findById

        UserService userService = Rpcfx.create(UserService.class, "http://localhost:8080/");
        User user = userService.findById(1);
        System.out.println("find user id=1 from server: " + user.getName());
//		OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8080/");
//		Order order = orderService.findOrderById(1992129);
//		System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));
//
//
//		UserService userService2 = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());

//        SpringApplication.run(RpcfxClientApplication.class, args);
    }

    private static class TagRouter implements Router {
        @Override
        public List<String> route(List<String> urls) {
            return urls;
        }
    }

    private static class RandomLoadBalancer implements LoadBalancer {
        @Override
        public String select(List<String> urls) {
            return urls.get(0);
        }
    }

    private static class CuicuiFilter implements Filter {
        private static final Logger log = LoggerFactory.getLogger(CuicuiFilter.class);

        @Override
        public boolean filter(RpcfxRequest request) {
            log.info("filter {} -> {}", this.getClass().getName(), request.toString());
            return true;
        }
    }
}



