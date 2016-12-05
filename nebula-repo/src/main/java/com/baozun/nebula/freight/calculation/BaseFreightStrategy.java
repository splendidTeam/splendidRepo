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
 * 基础类型
 * 
 * 基础(重量/重量单位*单价，低于基础价时使用基础价)
 * 
 * @author Tianlong.Zhang
 *
 * @deprecated 之前有考虑过几种运费模式：简单模式，就是保底价加上续重单价，计重模式，就是单位重量单价等等。但是这些都不合用。<br>
 *             主要原因是如果这个用于前端计算运费逻辑不合适，如果用于后端计算运费逻辑又太简单
 *             <p>
 *             本来要直接删除的,但是看到pts 里面也有相关功能,暂做 deprecated处理,在未来一版整体优化时,干掉
 */
@Deprecated
public class BaseFreightStrategy implements FreightStrategy{

    @Override
    public BigDecimal cal(ShippingFeeConfigCommand feeCmd,List<ItemFreightInfoCommand> itemList){

        BigDecimal result = BigDecimal.ZERO;

        BigDecimal totalWeight = BigDecimal.ZERO;

        //算出商品的总重量
        for (ItemFreightInfoCommand freightInfoCmd : itemList){
            BigDecimal curCount = new BigDecimal(freightInfoCmd.getCount());
            BigDecimal curWeight = new BigDecimal(freightInfoCmd.getWeight());
            totalWeight = totalWeight.add(curWeight.multiply(curCount));
        }

        //重量单位  ：存放在  首重单位 
        //单价         ：存放在  首重价格 

        BigDecimal weightUnit = new BigDecimal(feeCmd.getFirstPartUnit());
        BigDecimal weightPrice = feeCmd.getFirstPartPrice();
        BigDecimal basePrice = feeCmd.getBasePrice();

        result = weightPrice.multiply(totalWeight.divide(weightUnit));

        if (result.compareTo(basePrice) < 0){// 如果价格小于basePrice 则返回basePrice
            result = basePrice;
        }

        result = result.setScale(2);
        return result;
    }
}
