package com.baozun.nebula.zk;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.freight.manager.FreightMemoryManager;

/**
 * 运费相关的Watch,在invoke方法中用于调用引擎初始化方法
 * @author Justin Hu
 *
 */
public class FreightWatchInvoke extends WatchInvoke {

	public static final String LISTEN_PATH=ZooKeeperOperator.LIFECYCLE_NODE+"/fw";
	
	@Autowired
	private FreightMemoryManager  freightMemoryManager;
	
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
		
		// TODO Auto-generated method stub
		System.out.println(path+":invoke");
		freightMemoryManager.loadFreightInfosFromDB();
	}

	@Override
	public String getListenPath() {
		// TODO Auto-generated method stub
		return LISTEN_PATH;
	}



}
