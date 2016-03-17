/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.api.salesorder;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.utils.spring.SpringUtil;


/**
 * 默认的订单号生成器
 * 当前毫秒数+4位随机数
 * @author Tianlong.Zhang
 *
 */
// @Service 不配置成spring @Service 是为了便于商城端扩展。
// 见OrderManager的@Autowired(required = false) private final OrderCodeCreatorManager orderCodeCreator;
// @Transactional
public class DefaultOrderCodeCreatorManager implements OrderCodeCreatorManager {
	
	/**
	 * 前台下单
	 */
	private static String SOURCE_FRONTED = "BN";
	
	/**
	 * 后台下单 
	 */
	private static String SOURCE_CUSTOMER_SERVICE = "BC";
	
	/**
	 * 手工单
	 */
	private static String SOURCE_HANDLE = "BH";
	
	public DefaultOrderCodeCreatorManager(){
		
	}

	@Override
	public String createOrderCode() {
		return createOrderSerialNO();
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.api.sales.OrderCodeCreator#createOrderCodeBySource(java.lang.Integer)
	 */
	@Override
	public String createOrderCodeBySource(Integer source) {
		SdkOrderDao sdkOrderDao = (SdkOrderDao) SpringUtil.getBean(SdkOrderDao.class);
		
		if(source==null){
			throw new IllegalArgumentException();
		}
		
		Long sequence = sdkOrderDao.findCodeSeq();
		
		StringBuilder sb = new StringBuilder();
		
		if(source==1){
			sb.append(SOURCE_FRONTED).append(sequence);
		}else if(source==2){
			sb.append(SOURCE_CUSTOMER_SERVICE).append(sequence);
		}else if(source==3){
			sb.append(SOURCE_HANDLE).append(sequence);
		}else{
			throw new IllegalArgumentException();
		}
		
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.api.salesorder.OrderCodeCreatorManager#createOrderSerialNO()
	 */
	@Override
	public String createOrderSerialNO() {
		SdkOrderDao sdkOrderDao = (SdkOrderDao) SpringUtil.getBean(SdkOrderDao.class);
		Long sequence = sdkOrderDao.findOrderSerialNO();
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat datetime1 = new SimpleDateFormat("yyyyMMdd");
		sb.append(datetime1.format(new Date()));
		sb.append(sequence);
		return sb.toString();
	}

}
