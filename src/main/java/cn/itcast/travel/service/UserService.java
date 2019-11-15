package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {

    boolean register(User user);

    boolean activate(String code);

    User login(User user);
}
