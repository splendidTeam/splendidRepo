package com.baozun.nebula.curator.invoke;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.sdk.manager.SdkDeliveryAreaManager;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.feilong.core.Validator;

/**
 * @see com.baozun.nebula.zk.I18nLangWatchInvoke
 * 
 * @author chengchao
 *
 */
public class I18nLangWatchInvoke implements IWatcherInvoke{

	private Logger LOG = LoggerFactory.getLogger(I18nLangWatchInvoke.class);
	
	@Autowired
	private SdkI18nLangManager sdkI18nLangManager;
	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;
	@Autowired
	private SdkDeliveryAreaManager deliveryAreaManager;
	
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
		List<String> languageList = sdkI18nLangManager.getAllEnabledI18nKeyList();
		AddressUtil.setLanguageList(languageList);
		
		String delivery_mode_on_off = sdkMataInfoManager.findValue("delivery_mode_on_off");
		if(null == delivery_mode_on_off || delivery_mode_on_off.equals("false") || !delivery_mode_on_off.equals("true")){
			AddressUtil.init();
		}else{
			AddressUtil.setDeliveryModeOn(true);
			Properties pro = ProfileConfigUtil.findPro("config/metainfo.properties");
			String jsPath = "";
			if(Validator.isNotNullOrEmpty(pro)){
				jsPath = StringUtils.trim(pro.getProperty("delivery.area.js"));
			}
			for(String language : languageList){
				// 顺序 ,一般先有父 再有 子
				Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
				Map<String, Map<String, String>> map = deliveryAreaManager.findAllDeliveryAreaByLang(language,sorts);
				AddressUtil.initDeliveryArea(map, language, jsPath);
			}
		}
		
	}



}
