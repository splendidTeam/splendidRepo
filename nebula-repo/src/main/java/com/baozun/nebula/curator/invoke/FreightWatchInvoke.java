package com.baozun.nebula.curator.invoke;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.freight.manager.FreightMemoryManager;

import static com.feilong.core.date.DateExtensionUtil.formatDuration;

/**
 * 运费相关的Watch,在invoke方法中用于调用引擎初始化方法
 * 
 * @see com.baozun.nebula.zk.FreightWatchInvoke
 * @author chengchao
 *
 */
public class FreightWatchInvoke implements IWatcherInvoke{

    private Logger LOGGER = LoggerFactory.getLogger(FreightWatchInvoke.class);

    @Autowired
    private FreightMemoryManager freightMemoryManager;

    @Override
    public void invoke(String path,byte[] data){
        LOGGER.info("[{}]:invoke,sleep 3's", path);
        try{
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            LOGGER.error("", e);
        }

        //---------------------------------------------------------------------

        Date beginDate = new Date();

        freightMemoryManager.loadFreightInfosFromDB();

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("use time: [{}]", formatDuration(beginDate));
        }

    }
}
