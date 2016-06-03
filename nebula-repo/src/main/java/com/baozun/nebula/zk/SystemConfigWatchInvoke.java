package com.baozun.nebula.zk;

import java.util.Date;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.sdk.manager.SdkMataInfoManager;

/**
 * 系统配置的Watch,在invoke方法中用于调用如下：
 * matainfo的数据
 * 
 * @author Justin Hu
 *
 */
public class SystemConfigWatchInvoke extends WatchInvoke {

	public static final String LISTEN_PATH=ZooKeeperOperator.LIFECYCLE_NODE+"/systemconfig";
	
	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;
	

	
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
		//获取metainfo数据载入到内存中
		//有时候pts修改的事务还未提交，这里就接到通知了，所以取urlmap时，这里先暂停两秒
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sdkMataInfoManager.initMetaMap();
	}

	@Override
	public String getListenPath() {
		// TODO Auto-generated method stub
		return LISTEN_PATH;
	}



}
