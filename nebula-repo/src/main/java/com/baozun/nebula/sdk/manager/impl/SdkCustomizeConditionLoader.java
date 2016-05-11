package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.rule.CustomizeFilterClass;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.manager.SdkCustomizeConditionManager;
import com.baozun.nebula.sdk.manager.SdkCustomizeFilterClassManager;

public class SdkCustomizeConditionLoader {
	private static String serviceName = "sdkCustomizeFilterClassManager";
	
	public static int load(String currentPromotionConditionId,Long currentPromotionId,ShoppingCartCommand shopCart,List<PromotionBrief> briefListPrevious){
		//List<String> listIds = new ArrayList<String>();
		int conditionFactor = 0;
		//获得自定义实现类
		SdkCustomizeConditionManager citf = null;
		SdkCustomizeFilterClassManager registerSDK = null;

		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		registerSDK = (SdkCustomizeFilterClassManager)context.getBean(serviceName);
		List<Long> cstmIds = new ArrayList<Long>();
		cstmIds.add(Long.valueOf(currentPromotionConditionId));
		List<CustomizeFilterClass> cmdList = registerSDK.findCustomizeFilterClassListByIds(cstmIds);
		
		if (null == cmdList || cmdList.size()<1)
			return 0;
		String serviceCustomName = cmdList.get(0).getServiceName();
		//Integer cacheTime = cmdList.get(0).getCacheSecond();
		//Date currentVersion = cmdList.get(0).getVersion();
		//获得自定义实现类，执行公共接口返回Member Id List
		if (null == serviceCustomName)
			return 0;
		try
		{
			citf = (SdkCustomizeConditionManager)context.getBean(serviceCustomName);
		}catch (Exception e) {
			throw new BusinessException(String.format("自定义条件%s加载失败！可能不在当前jar包中！", serviceCustomName ));
		}
		
		/*
		//可以指定执行方法名称和参数。但必须返回List<Long>数据类型
		String methodName = "findMemberIdsByComsumeeAmtPromotionId";
		MethodInvoker methodInvoker = new MethodInvoker();
		methodInvoker.setTargetObject(context.getBean(serviceCustomName));
		
		methodInvoker.setTargetMethod(methodName);
		
		Integer prmCount = 3;
		Object[] arguments = new Object[prmCount];

		Long shopId = 1L;
		BigDecimal amt = BigDecimal.valueOf(3000);
		String prmId = "22";
		Array.set(arguments, 0, shopId);
		Array.set(arguments, 1, amt);
		Array.set(arguments, 2, prmId);

		// 设置参数
		if (arguments != null) {
			 methodInvoker.setArguments(arguments);
			}
			
		// 准备方法
		try {
			methodInvoker.prepare();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		
		// 执行方法
		try {
			listIdsLong = (List<Long>)methodInvoker.invoke();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		*/
		try
		{		
			conditionFactor = citf.getCustomConditionFactor(currentPromotionId,shopCart,briefListPrevious);
		}catch (Exception e) {
			throw new BusinessException(String.format("自定义条件%s加载失败！检查该自定义条件是否通过测试！", serviceCustomName ));
		}

		return conditionFactor;
	}
}
