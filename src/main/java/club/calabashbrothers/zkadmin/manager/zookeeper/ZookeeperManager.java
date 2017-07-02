package club.calabashbrothers.zkadmin.manager.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
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
}
