/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.shoppingcart.validator;

import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.MAIN_LINE_MAX_THAN_COUNT;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.MAX_THAN_INVENTORY;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.ONE_LINE_MAX_THAN_COUNT;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartBatchOptions;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartBatchResult;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResultUtil;

import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 */
@Component("checkStatusShoppingCartLineListValidator")
public class DefaultCheckStatusShoppingCartLineListValidator extends AbstractShoppingcartLineOperateValidator implements CheckStatusShoppingCartLineListValidator{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCheckStatusShoppingCartLineListValidator.class);

    @Autowired
    private SdkSkuManager sdkSkuManager;

    @Autowired
    private ShoppingcartLineOperateCommonValidator shoppingcartLineOperateCommonValidator;

    /** The shoppingcart total line max size validator. */
    @Autowired(required = false)
    private ShoppingcartTotalLineMaxSizeValidator shoppingcartTotalLineMaxSizeValidator;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.CheckStatusShoppingCartLineListValidator#validate(java.util.List)
     */
    @Override
    public ShoppingcartBatchResult validate(MemberDetails memberDetails,List<ShoppingCartLineCommand> checkedStatusShoppingCartLineCommandList,ShoppingcartBatchOptions shoppingcartBatchOptions){
        Validate.notEmpty(checkedStatusShoppingCartLineCommandList, "checkedStatusShoppingCartLineCommandList can't be null/empty!");

        ShoppingcartBatchOptions useShoppingcartBatchOptions = defaultIfNull(shoppingcartBatchOptions, ShoppingcartBatchOptions.DEFAULT);

        //---------------------------------------------------------------------
        //批量添加的时候,如果某条失败了,是否继续.
        boolean isFailContinue = useShoppingcartBatchOptions.isFailContinue();

        Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap = new LinkedHashMap<>();

        // 选中行单品校验
        for (ShoppingCartLineCommand shoppingCartLineCommand : checkedStatusShoppingCartLineCommandList){
            ShoppingcartResult shoppingcartResult = validate(memberDetails, shoppingCartLineCommand, checkedStatusShoppingCartLineCommandList);
            if (ShoppingcartResultUtil.isSuccess(shoppingcartResult)){
                LOGGER.debug("line [{}],item:[{}] validate success,continue", shoppingCartLineCommand.getId(), shoppingCartLineCommand.getItemId());
                continue;
            }
            //---------------------------------------------------------------------
            skuIdAndShoppingcartResultFailMap.put(shoppingCartLineCommand.getSkuId(), shoppingcartResult);
            //---------------------------------------------------------------------
            if (isFailContinue){
                continue;
            }
            break;
            //TODO
            //   return toNebulaReturnResult(commandValidateShoppingcartResult, shoppingCartLineCommand.getId());
        }

        //---------------------------------------------------------------------
        ShoppingcartBatchResult shoppingcartBatchResult = new ShoppingcartBatchResult();
        shoppingcartBatchResult.setSkuIdAndShoppingcartResultFailMap(skuIdAndShoppingcartResultFailMap);
        shoppingcartBatchResult.setIsSuccess(isNullOrEmpty(skuIdAndShoppingcartResultFailMap));
        return shoppingcartBatchResult;
    }

    private ShoppingcartResult validate(MemberDetails memberDetails,ShoppingCartLineCommand shoppingCartLineCommand,List<ShoppingCartLineCommand> checkedStatusShoppingCartLineCommandList){
        Long skuId = shoppingCartLineCommand.getSkuId();

        //FIXME 做成批量的
        Sku sku = sdkSkuManager.findSkuById(skuId);

        ShoppingcartResult commandValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, shoppingCartLineCommand.getQuantity());

        if (ShoppingcartResultUtil.isNotSuccess(commandValidateShoppingcartResult)){
            return commandValidateShoppingcartResult;
        }

        //------------2.单行 最大购买数校验-----------------------------------------------------------------------------------
        //是否已经在购物车里面有,
        //如果有,那么这一行累计数量进行校验; 如果没有那么仅仅校验传入的数量
        Integer oneLineTotalCount = shoppingCartLineCommand.getQuantity();

        ShoppingcartOneLineMaxQuantityValidator useShoppingcartOneLineMaxCountValidator = getUseShoppingcartOneLineMaxQuantityValidator();
        if (useShoppingcartOneLineMaxCountValidator.isGreaterThanMaxQuantity(memberDetails, skuId, oneLineTotalCount)){
            return ONE_LINE_MAX_THAN_COUNT;
        }

        // -----------3.最大行数验证-----------------------------------------------------------------------------
        // 如果没有找到相同的行, 那么需要创建一个line,所以需要校验是否超过购物车规定的商品行数
        ShoppingcartTotalLineMaxSizeValidator useShoppingcartTotalLineMaxSizeValidator = defaultIfNull(shoppingcartTotalLineMaxSizeValidator, new DefaultShoppingcartTotalLineMaxSizeValidator());
        if (useShoppingcartTotalLineMaxSizeValidator.isGreaterThanMaxSize(memberDetails, checkedStatusShoppingCartLineCommandList.size())){
            return MAIN_LINE_MAX_THAN_COUNT;
        }

        //------------4.统计购物车所有相同 skuid 库存校验----------------------------------------------
        if (shoppingCartInventoryValidator.isMoreThanInventory(checkedStatusShoppingCartLineCommandList, skuId)){
            return MAX_THAN_INVENTORY;
        }
        return null;
    }

}
