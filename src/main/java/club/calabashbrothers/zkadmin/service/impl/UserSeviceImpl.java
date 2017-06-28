package club.calabashbrothers.zkadmin.service.impl;

import club.calabashbrothers.zkadmin.domain.entity.User;
import club.calabashbrothers.zkadmin.domain.mapper.UserMapper;
import club.calabashbrothers.zkadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liaojiacan on 2017/6/28.
 */
@Service
public class UserSeviceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> listAll() {
        return userMapper.listAll();
    }
}
