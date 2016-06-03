package com.baozun.nebula.zk;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utilities.library.address.AddressUtil;

public class I18nLangWatchInvoke extends WatchInvoke {

	public static final String LISTEN_PATH=ZooKeeperOperator.LIFECYCLE_NODE+"/i18nLang";
	Log  log = LogFactory.getLog(getClass());
	
	@Autowired
	private SdkI18nLangManager sdkI18nLangManager;
	
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
		System.out.println(path+":invoke");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			log.error(e);
		}
		sdkI18nLangManager.loadI18nLangs();
		
		// 设置要加载的地址信息
		AddressUtil.setI18nOffOn(LangProperty.getI18nOnOff());
		AddressUtil.setLanguageList(sdkI18nLangManager.getAllEnabledI18nKeyList());
		AddressUtil.init();
	}

	@Override
	public String getListenPath() {
		return LISTEN_PATH;
	}



}
