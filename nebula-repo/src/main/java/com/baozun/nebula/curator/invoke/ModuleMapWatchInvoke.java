package com.baozun.nebula.curator.invoke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.sdk.manager.cms.SdkCmsModuleInstanceManager;

/**
 * @see com.baozun.nebula.zk.ModuleMapWatchInvoke
 * @author chengchao
 *
 */
public class ModuleMapWatchInvoke implements IWatcherInvoke {

	private Logger LOG = LoggerFactory.getLogger(ModuleMapWatchInvoke.class);
	
	public static final String PATH_KEY = "/modulemapwatch";
	
	@Autowired
	private SdkCmsModuleInstanceManager sdkCmsModuleInstanceManager;
	
	@Override
	public void invoke(String path, byte[] data) {
		LOG.info(path+":invoke");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		sdkCmsModuleInstanceManager.loadModuleMap();
		
	}



}
