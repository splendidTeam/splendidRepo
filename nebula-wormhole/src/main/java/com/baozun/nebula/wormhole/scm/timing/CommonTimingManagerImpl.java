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
 */
package com.baozun.nebula.wormhole.scm.timing;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;

/**
 * 非同步相关的定时调用的实现类
 * 
 * @author chenguang.zhou
 * @date 2014年5月15日 下午3:55:31
 */
@Service("commonTimingManager")
@Transactional
public class CommonTimingManagerImpl implements CommonTimingManager {
	
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(CommonTimingManagerImpl.class);
	
	@Autowired
	private SdkMsgManager sdkMsgManager;
	
	@Autowired
	private SdkItemManager sdkItemManager;

	@Override
	public void addOnSalesProductRecord() {
		List<Sku> skuList = sdkItemManager.findAllOnSalesSkuList();
		if(null != skuList && skuList.size() > 0){
			Integer onSaleItemCount = skuList.size();
			Integer ext = 0;
			if(onSaleItemCount%100 == 0){
				ext = onSaleItemCount/100;
			}else{
				ext = (onSaleItemCount/100)+1;
			}
			for(int i=0; i<ext; i++){
				sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_ITEM_ONSALE_SYNC, null, String.valueOf(i+1));
			}
		}
	}
}
