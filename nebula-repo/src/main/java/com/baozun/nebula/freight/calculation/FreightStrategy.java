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
package com.baozun.nebula.freight.calculation;

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.freight.command.ShippingFeeConfigCommand;
import com.baozun.nebula.sdk.command.logistics.ItemFreightInfoCommand;

/**
 * 运费计算接口
 * @author Tianlong.Zhang
 *
 */
public interface FreightStrategy {
	
	/**
	 * 计算运费
	 * @param feeCmd   运费配置
	 * @param itemList 要计算运费的商品列表
	 * @return
	 */
	BigDecimal cal(ShippingFeeConfigCommand feeCmd,List<ItemFreightInfoCommand> itemList);
}
