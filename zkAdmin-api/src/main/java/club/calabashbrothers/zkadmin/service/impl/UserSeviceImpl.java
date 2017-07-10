package club.calabashbrothers.zkadmin.service.impl;

import club.calabashbrothers.zkadmin.domain.entity.User;
import club.calabashbrothers.zkadmin.domain.mapper.UserMapper;
import club.calabashbrothers.zkadmin.service.UserService;
import club.calabashbrothers.zkadmin.shiro.exception.DataExistException;
import club.calabashbrothers.zkadmin.shiro.exception.PasswordUnMatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by liaojiacan on 2017/6/28.
 */
@Service
public class UserSeviceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    public List<User> listAll() {
        return userMapper.listAll();
    }

    @Override
    public User findUserByLoginName(String loginName) {
        return userMapper.findUserByLoginName(loginName);
    }

    /**
     * 创建用户
     * @param user
     * @return
     */
    @Transactional
    public User createUser(User user) throws DataExistException {
        User dbrd = userMapper.findUserByLoginName(user.getLoginName());
        if(dbrd!=null){
            throw  new DataExistException("用户名已经存在！");
        }
        passwordHelper.encryptPassword(user);
        user.setCreatedDate(new Date());
        user.setUpdatedDate(new Date());
        userMapper.insert(user);
        return  user;
    }
    /**
     * 删除用户
     * @param userId
     * @return
     */
    @Transactional
    public int deleteUser(long userId){
        return  userMapper.deleteByPrimaryKey(userId);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @Transactional
    public User updateUser(User user){
        userMapper.updateByPrimaryKeySelective(user);
        return  user;
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPassowrd
     * @param newPassword
     * @throws PasswordUnMatchException
     */
    @Transactional
    public void changePassword(Long userId,String oldPassowrd,String newPassword) throws PasswordUnMatchException {
        User user = userMapper.selectByPrimaryKey(userId);
        if(passwordHelper.matchPassword(user,oldPassowrd)){
            user.setPassword(newPassword);
            user.setUpdatedDate(new Date());
            user.setOpUser(user.getLoginName());
            passwordHelper.encryptPassword(user);
            updateUser(user);
        }else{
            throw new PasswordUnMatchException();
        }
    }


}
