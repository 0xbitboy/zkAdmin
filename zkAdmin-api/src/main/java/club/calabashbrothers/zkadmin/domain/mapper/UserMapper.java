package club.calabashbrothers.zkadmin.domain.mapper;

import club.calabashbrothers.zkadmin.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User>  listAll();

    User findUserByLoginName(String loginName);

}