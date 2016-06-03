package com.baozun.nebula.curator.invoke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.freight.manager.FreightMemoryManager;

/**
 * 运费相关的Watch,在invoke方法中用于调用引擎初始化方法
 * @see com.baozun.nebula.zk.FreightWatchInvoke
 * @author chengchao
 *
 */
public class FreightWatchInvoke implements IWatcherInvoke  {

	private Logger LOG = LoggerFactory.getLogger(FreightWatchInvoke.class);
	
	public static final String PATH_KEY = "/freightwatch";
	
	@Autowired
	private FreightMemoryManager  freightMemoryManager;

	@Override
	public void invoke(String path, byte[] data) {
		LOG.info(path+":invoke");
		freightMemoryManager.loadFreightInfosFromDB();
	}



}
