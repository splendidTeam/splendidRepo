package com.baozun.nebula.curator.invoke;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageInstanceManager;

/**
 * 引擎相关的Watch,在invoke方法中用于调用引擎初始化方法
 * 
 * @see com.baozun.nebula.zk.UrlMapWatchInvoke
 * @author chengchao
 *
 */
public class UrlMapWatchInvoke implements IWatcherInvoke {

	private Logger LOG = LoggerFactory.getLogger(SystemConfigWatchInvoke.class);
	
	public static final String PATH_KEY = "urlmapwatch";
	
	@Autowired
	private SdkCmsPageInstanceManager sdkCmsPageInstanceManager;
	@Autowired
	private CacheManager				cacheManager;

	@Autowired
	private ZkOperator zkOperator;
	
	@Override
	public void invoke(String path, byte[] data) {
		LOG.info(path+":invoke");
		//获取urlmap载入到内存中
		//有时候pts修改的事务还未提交，这里就接到通知了，所以取urlmap时，这里先暂停两秒
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sdkCmsPageInstanceManager.loadUrlMap();
		
		byte[] datas;
		try {
			datas = zkOperator.getData(path);
			String s_data = new String(datas);
			if(s_data!=null && s_data.length()>0){
				if(s_data.startsWith("#")){
					s_data=s_data.substring(1);
					cacheManager.removeMapValue(CacheKeyConstant.CMS_PAGE_KEY, s_data);
				}
			}
		} catch (KeeperException e) {
			LOG.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
