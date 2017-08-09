package com.baozun.nebula.curator.invoke;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.sdk.manager.cms.SdkCmsModuleInstanceManager;

/**
 * @see com.baozun.nebula.zk.ModuleMapWatchInvoke
 * @author chengchao
 * @author D.C 2017/8/10 凌晨 初始化取消睡眠
 */
public class ModuleMapWatchInvoke extends AbstractWatchInvoke{

    private Logger LOG = LoggerFactory.getLogger(ModuleMapWatchInvoke.class);

    @Autowired
    private SdkCmsModuleInstanceManager sdkCmsModuleInstanceManager;

    @Override
    public void invoke(String path,byte[] data){
        LOG.info(path + ":invoke");
        if (!this.initialized.compareAndSet(false, true)){
            try{
                TimeUnit.SECONDS.sleep(3);
            }catch (InterruptedException e){}
        }
        sdkCmsModuleInstanceManager.loadModuleMap();

    }

}
