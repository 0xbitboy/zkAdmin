package club.calabashbrothers.zkadmin.manager.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liaojiacan on 2017/7/1.
 */
public class ZookeeprClientFactory {

    String connectString = null;
    int sessionTimeout = 50000;
    EventCallBack eventCallBackProxy;
    private ZooKeeper zk;
    private String scheme;
    private byte[] auth;

    public ZookeeprClientFactory(String connectString, int sessionTimeout) {
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
    }
    public ZookeeprClientFactory setEventCallBack(EventCallBack cb){
        this.eventCallBackProxy = cb;
        return this;
    }
    public ZookeeprClientFactory setAuthInfo(String schema, byte[] auth){
        this.scheme = schema;
        this.auth = auth;
        return this;
    }
    //double check
    public ZooKeeper createZookeeper() {
        try {
            if (this.zk == null){
                synchronized (ZookeeprClientFactory.class){
                    if (this.zk == null){
                        buildZk();
                    }
                }
            } else {
                if (!this.zk.getState().isAlive()) {
                    close();
                    buildZk();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.scheme != null && this.auth != null) zk.addAuthInfo(this.scheme,this.auth);
        return this.zk;
    }

    private void buildZk() throws InterruptedException, IOException {
        ConnectWatcher watcher = new ConnectWatcher() {
            @Override
            void onSyncConnected() {
                if (eventCallBackProxy != null) eventCallBackProxy.onSyncConnected();
            }

            @Override
            void onExpired() {
                if (eventCallBackProxy != null) eventCallBackProxy.onExpired();
            }

            @Override
            void onDisconnected() {
                if (eventCallBackProxy != null) eventCallBackProxy.onDisconnected();
            }
        };
        this.zk = new ZooKeeper(this.connectString,this.sessionTimeout,watcher);
        watcher.connectLatch.await();
    }

    public void close() {
        if (this.zk != null){
            try {
                this.zk.close();
                this.zk = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static interface EventCallBack{
        void onSyncConnected();
        void onExpired();
        void onDisconnected();
    }
    private static abstract class ConnectWatcher implements Watcher {
        public CountDownLatch connectLatch = new CountDownLatch(1);
        //connected status watcher
        @Override
        public void process(WatchedEvent event) {
            switch (event.getState()) {
                case SyncConnected:
                    connectLatch.countDown();
                    System.out.println("syncconnected");
                    onSyncConnected();
                    break;
                //客户端再次链接服务端时,如果session实效,触发Expired
                case Expired:
                    System.out.println("expired");
                    onExpired();
                    break;
                //客户端与接服务端长链接断开
                case Disconnected:
                    System.out.println("disconnected");
                    onDisconnected();
                    break;
                default:
                    System.out.println("others event:"+event.getState().name());
            }
        }
        abstract void onSyncConnected();
        abstract void onExpired();
        abstract void onDisconnected();
    }
}
