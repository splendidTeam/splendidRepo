package com.baozun.nebula.curator.invoke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;

/**
 * 系统配置的Watch,在invoke方法中用于调用如下：
 * matainfo的数据
 * 
 * @see com.baozun.nebula.zk.SystemConfigWatchInvoke
 * @author chengchao
 *
 */
public class SystemConfigWatchInvoke implements IWatcherInvoke {

	private Logger LOG = LoggerFactory.getLogger(SystemConfigWatchInvoke.class);
	
	public static final String PATH_KEY = "systemconfigwatch";
	
	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;

	@Override
	public void invoke(String path, byte[] data) {
		LOG.info(path+":invoke");
		//获取metainfo数据载入到内存中
		//有时候pts修改的事务还未提交，这里就接到通知了，所以取urlmap时，这里先暂停两秒
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		sdkMataInfoManager.initMetaMap();
	}



}
