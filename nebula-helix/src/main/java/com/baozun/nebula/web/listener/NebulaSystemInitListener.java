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
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;

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

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("watchers.size:[{}],list:[{}]", watchers.size(), JsonUtil.format(getPropertyValueList(watchers, "listenerPath")));
        }

        //---------------------------------------------------------------------

        for (IWatcher watcher : watchers){
            try{
                String listenerPath = watcher.getListenerPath();

                Date beginDate = new Date();
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("begin watcher :[{}]", listenerPath);
                }

                watcher.initListen();

                if (LOGGER.isInfoEnabled()){
                    LOGGER.info("watcher:[{}],use time: [{}]", listenerPath, formatDuration(beginDate));
                }
            }catch (Exception e){
                LOGGER.error("[zookeeper] Zookeeper watcher init error!", e);
            }
        }

    }
}
