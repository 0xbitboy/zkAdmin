package club.calabashbrothers.zkadmin.manager.zookeeper;

import club.calabashbrothers.zkadmin.manager.zookeeper.model.TextNode;
import club.calabashbrothers.zkadmin.manager.zookeeper.model.ZkNode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liaojiacan on 2017/7/2.
 */
public class ZookeeperManager {

    private ZookeeprClientFactory zookeeprClientFactory;

    public ZookeeperManager(ZookeeprClientFactory zookeeprClientFactory) {
        this.zookeeprClientFactory = zookeeprClientFactory;
    }

    public Stat getNodeInfo(String path) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk = zookeeprClientFactory.createZookeeper();
        Stat stat = zk.exists(path, false);
        return stat;
    }

    public String createNode(String path) throws KeeperException, InterruptedException, IOException {
        ZooKeeper zk = zookeeprClientFactory.createZookeeper();
        Stat stat = zk.exists(path, true);
        String resultPath = null;
        if (stat == null){
            resultPath = zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        return resultPath;
    }

    public List<String> listChilds(String path) {
        ZooKeeper zk = null;
        List<String> childs = null;
        try {
            zk = zookeeprClientFactory.createZookeeper();
            Stat stat = zk.exists(path, true);
            if (stat == null) return null;
            childs = zk.getChildren(path,null,null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } finally {
            if (zk != null) try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return childs;
        }
    }

    /**
     * 获取完整的目录数
     * @return
     */
    public ZkNode getZkTree(){
        ZkNode  root = new TextNode("/");
        loadZkNode(zookeeprClientFactory.createZookeeper(),root);
        return  root;
    }

    /**
     * 递归构建 目录树
     * @param zooKeeper
     * @param node 顶级节点
     */
    private void loadZkNode(ZooKeeper zooKeeper,ZkNode node){
        List<String> children=null;
        boolean acl = false;
        try {
            children = zooKeeper.getChildren(node.getPath(), null, null);
        }catch (KeeperException.NoAuthException noAuthe){
            acl = true;
        }catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(acl){
            node.setAcl(true);
        }
        if(children==null){
            return;
        }
        node.setChildren(new LinkedList<ZkNode>());
        for (String child:children){
            ZkNode  childNode = new TextNode(child);
            loadZkNode(zooKeeper,childNode);
            node.getChildren().add(childNode);
        }
    }


}
