package club.calabashbrothers.zkadmin.manager.zookeeper;

import club.calabashbrothers.zkadmin.manager.zookeeper.model.TextNode;
import club.calabashbrothers.zkadmin.manager.zookeeper.model.ZkNode;
import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by liaojiacan on 2017/7/15.
 */
public class ZookeeperManagerTest {


    private ZookeeperManager zookeeperManager ;

    @Before
    public void before(){
        ZookeeprClientFactory factory = new ZookeeprClientFactory("192.168.31.182:2181",30000);
        zookeeperManager = new ZookeeperManager(factory);
    }

    @After
    public void after(){
        zookeeperManager.close();
    }


    @Test
    public void getNodeInfo() throws Exception {
        System.out.println(JSON.toJSONString(zookeeperManager.getNodeInfo("/test")));
    }

    @Test
    public void createNode() throws Exception {
        zookeeperManager.createNode("/test");
    }

    @Test
    public void listChilds() throws Exception {

    }

    @Test
    public void getZkTree() throws Exception {
        System.out.println(JSON.toJSONString(zookeeperManager.getZkTree()));
    }

    @Test
    public void save() throws Exception {
    }

    @Test
    public void loadNode() throws Exception {
        ZkNode zkNode = new TextNode("/brokers/ids/1");
        zookeeperManager.loadNode(zkNode);
        System.out.println(JSON.toJSONString(zkNode));
    }

    @Test
    public void remove() throws Exception {
        zookeeperManager.remove("/test");
    }

}