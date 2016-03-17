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
 * 计件类型的运费计算
 * @author Tianlong.Zhang
 *
 */
public class UnitFreightStrategy implements FreightStrategy{

	@Override
	public BigDecimal cal(ShippingFeeConfigCommand feeCmd,
			List<ItemFreightInfoCommand> itemList) {
		BigDecimal result = BigDecimal.ZERO;
		
		Integer totalCount = 0;
		
		//算出商品的总件数
		for(ItemFreightInfoCommand freightInfoCmd : itemList){
			Integer curCount = freightInfoCmd.getCount();
			totalCount += curCount;
		}
		
		Integer firstPartUnit = feeCmd.getFirstPartUnit();
		BigDecimal firstPartPrice = feeCmd.getFirstPartPrice();
		
		Integer subsequentPartUnit = feeCmd.getSubsequentPartUnit();
		BigDecimal subsequentPartPrice = feeCmd.getSubsequentPartPrice();
		
		if(totalCount <= firstPartUnit){// 未超出 首 件 运费单位的，按照首件运费的来算
			result = firstPartPrice;
		}else{
			result = result.add(firstPartPrice);
			Integer subCount = totalCount - firstPartUnit;// 剩下的件数
			
			Integer subPartCount = subCount/subsequentPartUnit; // 剩下的 件数 / 续件单位 = 要续件的数量
			
			if(subCount%subsequentPartUnit!=0){// 如果有余数， 要续件的数量+1
				subPartCount ++;
			}
			
			BigDecimal subPrice = subsequentPartPrice.multiply(new BigDecimal(subPartCount));
			
			result = result.add(subPrice);
		}
		
		result = result.setScale(2);
		return result;
	}

}
