package com.baozun.nebula.web.listener;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.curator.watcher.IWatcher;
import com.baozun.nebula.curator.watcher.ZkWatcherControl;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;

/**
 * 系统初始化时的listener
 * 
 * @author D.C
 */
public class NebulaSystemInitListener implements ServletContextListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaSystemInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce){
        initZkWatcher(sce);
        extendProcess(sce);
    }

    /**
     * 扩展点
     * 
     * @param sce
     */
    protected void extendProcess(ServletContextEvent sce){

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce){

    }

    /**
     * 初使化zookeeper watcher
     * 
     * @param sce
     */
    private void initZkWatcher(ServletContextEvent sce){
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
        ZkWatcherControl zkWatcherControl = applicationContext.getBean(ZkWatcherControl.class);

        List<IWatcher> watchers = zkWatcherControl.getWatchers();

        if (isNullOrEmpty(watchers)){
            LOGGER.info("watchers is null or empty");
            return;
        }

        //---------------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("watchers.size:[{}]", watchers.size());
        }

        //---------------------------------------------------------------------

        for (IWatcher watcher : watchers){
            try{

                Date beginDate = new Date();
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("begin watcher :[{}]", watcher.getListenerPath());
                }

                watcher.initListen();

                if (LOGGER.isInfoEnabled()){
                    LOGGER.info("watcher:[{}],use time: [{}]", watcher.getListenerPath(), formatDuration(beginDate));
                }
            }catch (Exception e){
                LOGGER.error("[zookeeper] Zookeeper watcher init error!", e);
            }
        }

    }
}
