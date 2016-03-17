package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.baozun.nebula.calculateEngine.condition.AtomicSetting;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.rule.CustomizeFilterClass;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.manager.SdkCustomizeFilterClassManager;
import com.baozun.nebula.sdk.manager.SdkCustomizeSettingManager;

public class SdkCustomizeSettingLoader {
	private static String serviceName = "sdkCustomizeFilterClassManager";
	
	public static PromotionSettingDetail load(AtomicSetting set,PromotionCommand currentPromotion,ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious){
		//获得自定义实现类
		SdkCustomizeSettingManager citf = null;
		SdkCustomizeFilterClassManager registerSDK = null;

		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		registerSDK = (SdkCustomizeFilterClassManager)context.getBean(serviceName);
		List<Long> cstmIds = new ArrayList<Long>();
		cstmIds.add(Long.valueOf(set.getSettingValue().toString()));
		List<CustomizeFilterClass> cmdList = registerSDK.findCustomizeFilterClassListByIds(cstmIds);
		
		if (null == cmdList || cmdList.size()<1)
			return null;
		String serviceCustomName = cmdList.get(0).getServiceName();
		//Integer cacheTime = cmdList.get(0).getCacheSecond();
		//Date currentVersion = cmdList.get(0).getVersion();
		//获得自定义实现类，执行公共接口返回Member Id List
		//AtomicSetting setting
		if (null == serviceCustomName)
			return null;
		try
		{
			citf = (SdkCustomizeSettingManager)context.getBean(serviceCustomName);
		}catch (Exception e) {
			throw new BusinessException(String.format("自定义设置%s加载失败！可能不在当前jar包中！", serviceCustomName ));
		}
		PromotionSettingDetail detail = new PromotionSettingDetail();
		try
		{	
			detail = citf.getCustomSetting(set,currentPromotion,shopCart,briefListPrevious);
		}catch (Exception e) {
			throw new BusinessException(String.format("自定义设置%s加载失败！检查该自定义条件是否通过测试！", serviceCustomName ));
		}

		return detail;
	}
}
