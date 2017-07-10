package club.calabashbrothers.zkadmin.service;

import club.calabashbrothers.zkadmin.domain.entity.ConnectionInfo;
import club.calabashbrothers.zkadmin.shiro.exception.DataExistException;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

/**
 * Created by liaojiacan on 2017/7/10.
 */
public interface CoreService {


    /**
     * 保存连接信息
     * @param connectionInfo
     */
    void saveConnection(ConnectionInfo connectionInfo) throws DataExistException;

    /**
     * 测试连接
     * @param connectionInfo
     */
    void testConnection(ConnectionInfo connectionInfo) throws InterruptedException, IOException, KeeperException;


    /**
     * 获取系统记录的连接记录
     * @param owner
     * @return
     */
    List<ConnectionInfo> listConnections(String owner);

    /**
     *
     * @param id
     */
    void removeConnectionInfo(Long id);
}
