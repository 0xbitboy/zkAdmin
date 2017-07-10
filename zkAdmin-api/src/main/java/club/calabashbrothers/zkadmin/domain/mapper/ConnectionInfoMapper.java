package club.calabashbrothers.zkadmin.domain.mapper;

import club.calabashbrothers.zkadmin.domain.entity.ConnectionInfo;

public interface ConnectionInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConnectionInfo record);

    int insertSelective(ConnectionInfo record);

    ConnectionInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConnectionInfo record);

    int updateByPrimaryKey(ConnectionInfo record);
}