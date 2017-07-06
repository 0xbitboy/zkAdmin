package club.calabashbrothers.zkadmin.manager.zookeeper;

import club.calabashbrothers.zkadmin.manager.zookeeper.model.TextNode;
import club.calabashbrothers.zkadmin.manager.zookeeper.model.ZkNode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
        loadZkNodes(zookeeprClientFactory.createZookeeper(),root);
        return  root;
    }

    /**
     * 递归构建 目录树
     * @param zooKeeper
     * @param node 顶级节点
     */
    private void loadZkNodes(ZooKeeper zooKeeper,ZkNode node){
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
            loadZkNodes(zooKeeper,childNode);
            node.getChildren().add(childNode);
        }
    }

    /**
     * 保存一个节点
     * @param zkNode
     * @return
     */
    public boolean save(ZkNode zkNode){

        Object content = zkNode.getContent();
        if (content == null) content = "";
        ZooKeeper zk = zookeeprClientFactory.createZookeeper();
        try {
            Stat stat = zk.exists(zkNode.getPath(), null);
            byte[] bytes = zkNode.getBytes();
            if (stat == null){
                recursiveCreate(zk, zkNode.getPath(), bytes);
            }else {
                zk.setData(zkNode.getPath(),bytes,stat.getVersion());
            }
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

        return false;

    }

    public void remove(String path) throws KeeperException, InterruptedException {
        ZooKeeper zk = zookeeprClientFactory.createZookeeper();
        recursiveRemove(zk,path);
    }


    private   void recursiveRemove(ZooKeeper zk,String pPath) throws KeeperException, InterruptedException {
        List<String> chPaths = zk.getChildren(pPath,false);
        if (chPaths.size() == 0){
            zk.delete(pPath,-1);
            System.out.println(pPath);
            return;
        }
        //delete childs
        for (String path : chPaths){
            String recursivePPath = pPath + "/" +path;
            recursiveRemove(zk,recursivePPath);
        }
        //delete self
        zk.delete(pPath,-1);
    }

    private void recursiveCreate(ZooKeeper zk,String path,byte[] data) throws KeeperException, InterruptedException {
        if ("/".equals(path.trim())) return;
        path = StringUtils.trimTrailingCharacter(path, '/');
        Stack<String> paths = buildPathStack(path);
        byte[] tempdata = "".getBytes();
        while (!paths.empty()) {
            String elPath = paths.pop();
            Stat stat = zk.exists(elPath, false);
            if (paths.isEmpty()) tempdata = data;
            if (stat == null)zk.create(elPath, tempdata, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    private Stack<String> buildPathStack(String path){
        Stack<String> stack = new Stack<String>();
        stack.push(path);
        int end = path.lastIndexOf("/");
        while (end > 0){
            String elPath = path.substring(0,end);
            stack.push(elPath);
            end = elPath.lastIndexOf("/");
        }
        return stack;
    }


    public  void close(){
        zookeeprClientFactory.close();
    }



}
