package io.kimmking.rpcfx.demo.api.service;

import io.kimmking.rpcfx.demo.api.pojo.User;

public interface UserService {

    User findById(int id);

}
