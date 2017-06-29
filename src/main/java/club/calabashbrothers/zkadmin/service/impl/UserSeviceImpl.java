package club.calabashbrothers.zkadmin.service.impl;

import club.calabashbrothers.zkadmin.domain.entity.User;
import club.calabashbrothers.zkadmin.domain.mapper.UserMapper;
import club.calabashbrothers.zkadmin.service.UserService;
import club.calabashbrothers.zkadmin.shiro.exception.DataExistException;
import club.calabashbrothers.zkadmin.shiro.exception.PasswordUnMatchException;
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

    @Override
    public User findUserByLoginName(String loginName) {
        return null;
    }

    @Override
    public User createUser(User user) throws DataExistException {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void changePassword(Long userId, String oldPassowrd, String newPassword) throws PasswordUnMatchException {

    }

    @Override
    public User findUserByPhone(String phone) {
        return null;
    }
}
