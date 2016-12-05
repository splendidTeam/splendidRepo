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
 * 按重量计算运费
 * 
 * @author Tianlong.Zhang
 * @deprecated 之前有考虑过几种运费模式：简单模式，就是保底价加上续重单价，计重模式，就是单位重量单价等等。但是这些都不合用。<br>
 *             主要原因是如果这个用于前端计算运费逻辑不合适，如果用于后端计算运费逻辑又太简单
 *             <p>
 *             本来要直接删除的,但是看到pts 里面也有相关功能,暂做 deprecated处理,在未来一版整体优化时,干掉
 */
@Deprecated
public class WeightFreightStrategy implements FreightStrategy{

    @Override
    public BigDecimal cal(ShippingFeeConfigCommand feeCmd,List<ItemFreightInfoCommand> itemList){
        BigDecimal result = BigDecimal.ZERO;

        Double totalWeight = 0D;

        //算出商品的总重量
        for (ItemFreightInfoCommand freightInfoCmd : itemList){
            Integer curCount = freightInfoCmd.getCount();
            Double curWeight = freightInfoCmd.getWeight();
            totalWeight += curWeight * curCount;
        }

        Integer firstPartUnit = feeCmd.getFirstPartUnit();
        BigDecimal firstPartPrice = feeCmd.getFirstPartPrice();

        Integer subsequentPartUnit = feeCmd.getSubsequentPartUnit();
        BigDecimal subsequentPartPrice = feeCmd.getSubsequentPartPrice();

        if (totalWeight <= firstPartUnit){// 未超出 首重 运费单位的，按照首重运费的来算
            result = firstPartPrice;
        }else{
            result = result.add(firstPartPrice);
            double subWeight = totalWeight - firstPartUnit;// 剩下的件数

            Integer subPartCount = (int) (subWeight / subsequentPartUnit); // 剩下的 件数 / 续件单位 = 要续件的数量

            if (subWeight % subsequentPartUnit != 0){// 如果有余数， 要续件的数量+1
                subPartCount++;
            }

            BigDecimal subPrice = subsequentPartPrice.multiply(new BigDecimal(subPartCount));

            result = result.add(subPrice);
        }

        result.setScale(2);

        return result;
    }

}
