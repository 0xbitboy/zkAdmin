package club.calabashbrothers.zkadmin.controller;

import club.calabashbrothers.zkadmin.manager.zookeeper.ZookeeperManager;
import club.calabashbrothers.zkadmin.manager.zookeeper.ZookeeprClientFactory;
import club.calabashbrothers.zkadmin.manager.zookeeper.model.TextNode;
import club.calabashbrothers.zkadmin.manager.zookeeper.model.ZkNode;
import club.calabashbrothers.zkadmin.web.Constants;
import club.calabashbrothers.zkadmin.web.Result;
import club.calabashbrothers.zkadmin.web.form.ZookeeperConnectForm;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by liaojiacan on 2017/7/6.
 */
@Controller("zookeeper")
public class CoreController {

    /**
     * 连接 zookeeper
     * @param form
     * @param bindingResult
     * @param session
     * @return
     */
    @RequestMapping("connect.json")
    @ResponseBody
    public Map<String,Object> connect(@Valid ZookeeperConnectForm form, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            return Result.formErrorWrapper(bindingResult.getFieldError().getField(),bindingResult.getFieldError().getDefaultMessage());
        }
        ZookeeperManager zookeeperManager = getZookeeperManager(session);
        if(zookeeperManager!=null){
            zookeeperManager.close();
        }
        ZookeeprClientFactory factry = new ZookeeprClientFactory(form.getConnectString(),form.getSessionTimeout());
        zookeeperManager = new ZookeeperManager(factry);
        try {
            Stat rootInfo = zookeeperManager.getNodeInfo("/");
            session.setAttribute(Constants.ZOOKEEPER_MANAGER_SESSION_KEY,zookeeperManager);
            return  Result.successWrapper("object",rootInfo);
        } catch (Exception e) {
            zookeeperManager.close();
            return Result.errorWrapper(e.getMessage());
        }
    }

    /**
     * 获取 完整的目录树
     * @param session
     * @return
     */
    @RequestMapping("loadPathTree.json")
    @ResponseBody
    public Map<String,Object> loadPathTree( HttpSession session){
        ZookeeperManager zkmg = getZookeeperManager(session);
        if(zkmg == null){
            return Result.NONE_ZK_CONNECTION;
        }
        ZkNode rootNode = zkmg.getZkTree();
        return Result.successWrapper("object",rootNode);
    }

    /**
     * 创建节点
     * @param path
     * @param session
     * @return
     */
    @RequestMapping("createPath.json")
    @ResponseBody
    public Map<String,Object> createPath( String path,HttpSession session){
        ZookeeperManager zkmg = getZookeeperManager(session);
        if(zkmg == null){
            return Result.NONE_ZK_CONNECTION;
        }
        if(path==null) return  Result.formErrorWrapper("path","path 不能为空！");
        try {
            zkmg.createNode(path);
        } catch (Exception e) {
            return Result.errorWrapper(e.getMessage());
        }
        return Result.SIMPLE_SUCCESS;
    }

    /**
     * 保存节点内容
     * @param path
     * @param content
     * @param session
     * @return
     */
    @RequestMapping("save.json")
    @ResponseBody
    public Map<String,Object> save( String path,String content,HttpSession session){
        ZookeeperManager zkmg = getZookeeperManager(session);
        if(zkmg == null){
            return Result.NONE_ZK_CONNECTION;
        }
        if(path==null) return  Result.formErrorWrapper("path","path 不能为空！");
        try {
            ZkNode zkNode = new TextNode(path);
            zkNode.setContent(content);
            zkmg.save(zkNode);
        } catch (Exception e) {
            return Result.errorWrapper(e.getMessage());
        }
        return Result.SIMPLE_SUCCESS;
    }

    /**
     * 读取节点数据
     * @param path
     * @param session
     * @return
     */
    @RequestMapping("readPath.json")
    @ResponseBody
    public Map<String,Object> readPath( String path,HttpSession session) {
        ZookeeperManager zkmg = getZookeeperManager(session);
        if(zkmg == null){
            return Result.NONE_ZK_CONNECTION;
        }
        if(path==null) return  Result.formErrorWrapper("path","path 不能为空！");
        try {
            //先处理 是文本的节点，后面再加其他处理器。。。
            ZkNode zkNode = new TextNode(path);
            zkmg.loadNode(zkNode);
            return Result.successWrapper(zkNode);
        } catch (Exception e) {
            return Result.errorWrapper(e.getMessage());
        }
    }

    /**
     * 删除节点
     * @param path
     * @param session
     * @return
     */
    @RequestMapping("deletePath.json")
    @ResponseBody
    public Map<String,Object> deletePath( String path,HttpSession session) {
        ZookeeperManager zkmg = getZookeeperManager(session);
        if(zkmg == null){
            return Result.NONE_ZK_CONNECTION;
        }
        if(path==null) return  Result.formErrorWrapper("path","path 不能为空！");

        try {
            zkmg.remove(path);
        } catch (Exception e) {
            return Result.errorWrapper(e.getMessage());
        }
        return Result.SIMPLE_SUCCESS;
    }

    private ZookeeperManager getZookeeperManager(HttpSession session){
        Object zkObj = session.getAttribute(Constants.ZOOKEEPER_MANAGER_SESSION_KEY);

        if(zkObj!=null){
            return (ZookeeperManager) zkObj;
        }
        return  null;
    }

}
