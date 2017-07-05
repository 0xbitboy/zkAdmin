package club.calabashbrothers.zkadmin.web.listener;


import club.calabashbrothers.zkadmin.manager.zookeeper.ZookeeperManager;
import club.calabashbrothers.zkadmin.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by liaojiacan on 2017/7/5.
 */
@WebListener
public class WebSessionListener implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        logger.trace("session created!");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        Object obj = httpSessionEvent.getSession().getAttribute(Constants.ZOOKEEPER_MANAGER_SESSION_KEY);
        if(obj!=null){
            ((ZookeeperManager)obj).close();
        }
        logger.trace("session destroy!");
    }
}
