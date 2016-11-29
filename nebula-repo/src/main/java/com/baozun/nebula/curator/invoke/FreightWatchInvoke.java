package com.baozun.nebula.curator.invoke;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.freight.manager.FreightMemoryManager;

/**
 * 运费相关的Watch,在invoke方法中用于调用引擎初始化方法
 * 
 * @see com.baozun.nebula.zk.FreightWatchInvoke
 * @author chengchao
 *
 */
public class FreightWatchInvoke implements IWatcherInvoke{

    private Logger LOG = LoggerFactory.getLogger(FreightWatchInvoke.class);

    @Autowired
    private FreightMemoryManager freightMemoryManager;

    @Override
    public void invoke(String path,byte[] data){
        LOG.info(path + ":invoke");
        try{
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            LOG.error(e.getMessage());
        }
        freightMemoryManager.loadFreightInfosFromDB();
    }
}
