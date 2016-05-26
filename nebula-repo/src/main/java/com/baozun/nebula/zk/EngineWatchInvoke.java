package com.baozun.nebula.zk;

import java.util.Date;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.sdk.manager.SdkLimitManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;

/**
 * 引擎相关的Watch,在invoke方法中用于调用引擎初始化方法
 * @author Justin Hu
 *
 */
public class EngineWatchInvoke extends WatchInvoke {

	public static final String LISTEN_PATH=ZooKeeperOperator.LIFECYCLE_NODE+"/engine";
	
	@Autowired
	private SdkPromotionManager sdkPromotionManager;
	
	@Autowired
	private SdkLimitManager sdkLimitManager;
	
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
		
		return false;	//这里设置的是无论如何都要重新监听
	}

	@Override
	public void invoke(String path) {
		
		// TODO Auto-generated method stub
		System.out.println(path+":invoke");
		//促销规则
		sdkPromotionManager.publishPromotion(new Date());
		
		//限购规则
		sdkLimitManager.publishLimit(new Date());
	}

	@Override
	public String getListenPath() {
		// TODO Auto-generated method stub
		return LISTEN_PATH;
	}



}
