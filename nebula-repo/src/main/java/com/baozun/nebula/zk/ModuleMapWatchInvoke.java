package com.baozun.nebula.zk;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.sdk.manager.SdkCmsModuleInstanceManager;

public class ModuleMapWatchInvoke extends WatchInvoke {

	public static final String LISTEN_PATH=ZooKeeperOperator.LIFECYCLE_NODE+"/modulemap";
	
	@Autowired
	private SdkCmsModuleInstanceManager sdkCmsModuleInstanceManager;
	
	@Override
	public boolean isMatch(String path, EventType type) {
		System.out.println(path+"-"+type.toString());
		if(path!=null&&path.startsWith(LISTEN_PATH)&&type==EventType.NodeDataChanged){
			return true;
		}
		else
			return false;
	}
	
	@Override
	public boolean needContinueWatch(String path, EventType type, KeeperState state) {
		if(path!=null&&path.startsWith(LISTEN_PATH)&&type==EventType.NodeDataChanged
			){
			return true;
		}
		return false;	
	}

	@Override
	public void invoke(String path) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sdkCmsModuleInstanceManager.loadModuleMap();
	}

	@Override
	public String getListenPath() {
		return LISTEN_PATH;
	}



}
