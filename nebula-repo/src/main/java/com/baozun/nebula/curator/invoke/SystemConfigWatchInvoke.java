package com.baozun.nebula.curator.invoke;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.sdk.manager.SdkMataInfoManager;

/**
 * 系统配置的Watch,在invoke方法中用于调用如下： matainfo的数据
 * 
 * @see com.baozun.nebula.zk.SystemConfigWatchInvoke
 * @author chengchao
 * @author D.C 2017/8/10 凌晨 初始化取消睡眠
 */
public class SystemConfigWatchInvoke extends AbstractWatchInvoke{

    private Logger LOG = LoggerFactory.getLogger(SystemConfigWatchInvoke.class);

    @Autowired
    private SdkMataInfoManager sdkMataInfoManager;

    @Override
    public void invoke(String path,byte[] data){
        LOG.info(path + ":invoke");
        // 获取metainfo数据载入到内存中
        // 有时候pts修改的事务还未提交，这里就接到通知了，所以取urlmap时，这里先暂停3秒
        if (!this.initialized.compareAndSet(false, true)){
            try{
                TimeUnit.SECONDS.sleep(3);
            }catch (InterruptedException e){}
        }
        sdkMataInfoManager.initMetaMap();
    }

}
