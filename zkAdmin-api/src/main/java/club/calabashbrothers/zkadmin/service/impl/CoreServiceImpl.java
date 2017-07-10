package club.calabashbrothers.zkadmin.service.impl;

import club.calabashbrothers.zkadmin.domain.entity.ConnectionInfo;
import club.calabashbrothers.zkadmin.domain.mapper.ConnectionInfoMapper;
import club.calabashbrothers.zkadmin.manager.zookeeper.ZookeeperManager;
import club.calabashbrothers.zkadmin.manager.zookeeper.ZookeeprClientFactory;
import club.calabashbrothers.zkadmin.service.CoreService;
import club.calabashbrothers.zkadmin.shiro.exception.DataExistException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by liaojiacan on 2017/7/10.
 */
@Service
public class CoreServiceImpl implements CoreService {


    @Autowired
    private ConnectionInfoMapper connectionInfoMapper;


    @Override
    public void saveConnection(ConnectionInfo connectionInfo) throws DataExistException {
        try{
            if(connectionInfo.getId()!=null){
                connectionInfo.setUpdatedDate(new Date());
                connectionInfoMapper.updateByPrimaryKeySelective(connectionInfo);
            }else{
                connectionInfo.setCreatedDate(new Date());
                connectionInfo.setUpdatedDate(new Date());
                connectionInfoMapper.insert(connectionInfo);
            }
        }catch (DuplicateKeyException dke){
            throw new DataExistException("已经存在：["+connectionInfo.getConnectUrl()+"]的连接信息");
        }

    }

    @Override
    public void testConnection(ConnectionInfo connectionInfo) throws InterruptedException, IOException, KeeperException {
        ZookeeprClientFactory factry = new ZookeeprClientFactory(connectionInfo.getConnectUrl(),connectionInfo.getSessionTimeout());
        ZookeeperManager zookeeperManager = new ZookeeperManager(factry);
        Stat rootInfo = zookeeperManager.getNodeInfo("/");
    }

    @Override
    public List<ConnectionInfo> listConnections(String owner) {
        return connectionInfoMapper.listConnections(owner);
    }
}
