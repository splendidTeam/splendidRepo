package com.baozun.nebula.curator.invoke;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utilities.library.address.AddressUtil;

/**
 * @see com.baozun.nebula.zk.I18nLangWatchInvoke
 * 
 * @author chengchao
 *
 */
public class I18nLangWatchInvoke implements IWatcherInvoke{

	private Logger LOG = LoggerFactory.getLogger(I18nLangWatchInvoke.class);
	
	public static final String PATH_KEY = "i18nlangwatch";
	
	@Autowired
	private SdkI18nLangManager sdkI18nLangManager;

	@Override
	public void invoke(String path, byte[] data) {
		LOG.info(path+":invoke");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		sdkI18nLangManager.loadI18nLangs();
		
		// 设置要加载的地址信息
		AddressUtil.setI18nOffOn(LangProperty.getI18nOnOff());
		AddressUtil.setLanguageList(sdkI18nLangManager.getAllEnabledI18nKeyList());
		AddressUtil.init();
	}



}
