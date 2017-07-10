package club.calabashbrothers.zkadmin.manager.zookeeper.model;


import java.util.List;

/**
 * Created by liaojiacan on 3/11/16.
 */


public abstract class ZkNode<T> {

    protected String path;

    private T content;

    private boolean acl = false;

    private List<ZkNode> children;

    public ZkNode(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

     public  T getContent(){
        return content;
     }

     public  void setContent(T t){
         this.content = t;
     }

    abstract public byte[] getBytes();

    abstract public T parse(byte[] data);

    public boolean isAcl() {
        return acl;
    }

    public void setAcl(boolean acl) {
        this.acl = acl;
    }

    public List<ZkNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZkNode> children) {
        this.children = children;
    }
}