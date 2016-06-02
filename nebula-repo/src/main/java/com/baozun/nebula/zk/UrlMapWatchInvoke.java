package com.baozun.nebula.zk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageInstanceManager;

/**
 * 引擎相关的Watch,在invoke方法中用于调用引擎初始化方法
 * @author Justin Hu
 *
 */
public class UrlMapWatchInvoke extends WatchInvoke {

	public static final String LISTEN_PATH=ZooKeeperOperator.LIFECYCLE_NODE+"/urlmap";
	
	@Autowired
	private SdkCmsPageInstanceManager sdkCmsPageInstanceManager;
	@Autowired
	private CacheManager				cacheManager;
	private  Log log = LogFactory.getLog(getClass());
	
	@Override
	public boolean isMatch(String path, EventType type) {
		// TODO Auto-generated method stub
		System.out.println(path+"-"+type.toString());
		
		if(path!=null&&path.startsWith(LISTEN_PATH)&&type==EventType.NodeDataChanged){
			return true;
		}
		else
			return false;
	}
	
	@Override
	public boolean needContinueWatch(String path, EventType type, KeeperState state) {
		// TODO Auto-generated method stub
		if(path!=null&&path.startsWith(LISTEN_PATH)&&type==EventType.NodeDataChanged
//				||type==EventType.NodeChildrenChanged
			){
			return true;
		}
		
		return false;	
	}

	@Override
	public void invoke(String path) {
		
		System.out.println(path+":invoke");
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
			datas = zooKeeperOperator.getZk().getData(path, false, null);
			String data = new String(datas);
			if(data!=null && data.length()>0){
				if(data.startsWith("#")){
					data=data.substring(1);
					cacheManager.removeMapValue(CacheKeyConstant.CMS_PAGE_KEY, data);
				}
			}
		} catch (KeeperException e) {
			log.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
		
	}

	@Override
	public String getListenPath() {
		// TODO Auto-generated method stub
		return LISTEN_PATH;
	}



}
