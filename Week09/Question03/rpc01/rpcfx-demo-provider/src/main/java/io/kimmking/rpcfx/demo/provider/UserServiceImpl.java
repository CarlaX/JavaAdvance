package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.demo.api.pojo.User;
import io.kimmking.rpcfx.demo.api.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
