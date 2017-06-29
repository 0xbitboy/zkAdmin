package club.calabashbrothers.zkadmin.service;

import club.calabashbrothers.zkadmin.domain.entity.User;
import club.calabashbrothers.zkadmin.shiro.exception.DataExistException;
import club.calabashbrothers.zkadmin.shiro.exception.PasswordUnMatchException;

import java.util.List;

/**
 * Created by liaojiacan on 2017/6/28.
 */
public interface UserService {

    List<User> listAll();
    User findUserByLoginName(String loginName);
    User createUser(User user) throws DataExistException;
    User updateUser(User user);
    public void changePassword(Long userId,String oldPassowrd,String newPassword) throws PasswordUnMatchException;
    User findUserByPhone(String phone);
}
