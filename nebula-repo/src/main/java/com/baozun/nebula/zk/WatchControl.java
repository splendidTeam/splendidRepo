package com.baozun.nebula.zk;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 根据watch的path的匹配，确定调用哪些WatchInvoke的实现类
 * @author Justin Hu
 *
 */
public class WatchControl implements Watcher {
	
	/**
	 * 所有注册的watch的列表
	 * 在spring中进行配置
	 */
	private List<WatchInvoke> watchInvokeList=null;
	
	@Autowired
	private ZooKeeperOperator zooKeeperOperator;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;


	protected Log				log					= LogFactory.getLog(getClass());
	
	public WatchControl(List<WatchInvoke> list) throws Exception{

		watchInvokeList=list;
		
		
	}
	
	public void initWatch(){
		for(WatchInvoke wi:watchInvokeList){
			wi.setWatcher(this);
			wi.initListen();
		}
	}
	
	/**
	 * 先处理通知，再重新注册watch,如果重复刷，将会丢失间隔的通知
	 */
	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		
		log.debug("myzk-----------type:"+event.getType()+"---state:"+event.getState());
		
		//如果session超时
		if(KeeperState.Expired.equals(event.getState())&&EventType.None.equals(event.getType())){
			
			zooKeeperOperator.reConnection();
			
/*			for(WatchInvoke wi:watchInvokeList){
				
				wi.watchAgainByReconn();
			}*/
			//如果当前数据库连接不可用，休息1秒钟再继续来
			boolean isRun=true;
			while(isRun){
				isRun=false;
				try {
					Thread.sleep(1000);
					jdbcTemplate.getDataSource().getConnection();
				} catch (Exception e) {
					isRun=true;
				}
				
			}
			
			
			//重连以后重新监听：
            //会注册监听器，并且invoke
			for(WatchInvoke wi:watchInvokeList){
				
				wi.initListen();
			}
		
		}
		
		
			for(WatchInvoke wi:watchInvokeList){
				if(wi.isMatch(event.getPath(), event.getType())){
					wi.notice(event);
				}
				
				
				wi.watchAgain(event.getPath(), event.getType(), event.getState());
				
			}

		
		

	}

}
