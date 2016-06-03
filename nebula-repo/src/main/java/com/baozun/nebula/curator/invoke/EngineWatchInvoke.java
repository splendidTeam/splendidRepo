package com.baozun.nebula.curator.invoke;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.sdk.manager.SdkLimitManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;


/**
 * 引擎相关的Watch,在invoke方法中用于调用引擎初始化方法
 * 
 * @see com.baozun.nebula.zk.EngineWatchInvoke
 * 
 * @author chengchao
 *
 */
public class EngineWatchInvoke implements IWatcherInvoke {
	
	private Logger LOG = LoggerFactory.getLogger(EngineWatchInvoke.class);
	
	public static final String PATH_KEY = "enginewatch";

	@Autowired
	private SdkPromotionManager sdkPromotionManager;
	
	@Autowired
	private SdkLimitManager sdkLimitManager;
	
	@Override
	public void invoke(String path, byte[] data) {
		LOG.info(path+":invoke");
		//促销规则
		sdkPromotionManager.publishPromotion(new Date());
		//限购规则
		sdkLimitManager.publishLimit(new Date());
	}

}
