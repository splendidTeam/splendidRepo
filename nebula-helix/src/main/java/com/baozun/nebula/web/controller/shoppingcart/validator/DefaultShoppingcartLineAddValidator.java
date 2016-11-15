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
import static com.feilong.core.util.CollectionsUtil.find;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;

/**
 * The Class DefaultShoppingcartLineAddValidator.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.3
 */
@Component("shoppingcartLineAddValidator")
public class DefaultShoppingcartLineAddValidator extends AbstractShoppingcartLineOperateValidator implements ShoppingcartLineAddValidator{

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /** The shoppingcart total line max size validator. */
    @Autowired(required = false)
    private ShoppingcartTotalLineMaxSizeValidator shoppingcartTotalLineMaxSizeValidator;

    /** The shoppingcart line operate common validator. */
    @Autowired
    private ShoppingcartLineOperateCommonValidator shoppingcartLineOperateCommonValidator;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineAddValidator#validator(com.baozun.nebula.web.MemberDetails, java.util.List, java.lang.Long, java.lang.Integer)
     */
    @Override
    public ShoppingcartResult validator(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,Long skuId,Integer count){
        Sku sku = sdkSkuManager.findSkuById(skuId);
        ShoppingcartResult commonValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, count);

        if (null != commonValidateShoppingcartResult){
            return commonValidateShoppingcartResult;
        }

        // ****************************************************************************************
        List<ShoppingCartLineCommand> mainLines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);

        // ****************************************************************************************
        //待操作的购物车行
        ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand = find(mainLines, "skuId", sku.getId());

        //是否已经在购物车里面有
        Integer oneLineTotalCount = null != toBeOperatedShoppingCartLineCommand ? toBeOperatedShoppingCartLineCommand.getQuantity() + count : count;

        ShoppingcartOneLineMaxQuantityValidator useShoppingcartOneLineMaxCountValidator = getUseShoppingcartOneLineMaxQuantityValidator();
        if (useShoppingcartOneLineMaxCountValidator.isGreaterThanMaxQuantity(memberDetails, skuId, oneLineTotalCount)){
            return ONE_LINE_MAX_THAN_COUNT;
        }

        // 最大行数验证
        // 校验是否超过购物车规定的商品行数
        if (null == toBeOperatedShoppingCartLineCommand){
            ShoppingcartTotalLineMaxSizeValidator useShoppingcartTotalLineMaxSizeValidator = defaultIfNull(shoppingcartTotalLineMaxSizeValidator, new DefaultShoppingcartTotalLineMaxSizeValidator());
            if (useShoppingcartTotalLineMaxSizeValidator.isGreaterThanMaxSize(memberDetails, mainLines.size() + 1)){
                return MAIN_LINE_MAX_THAN_COUNT;
            }
        }

        //***************************************************************************************
        if (null != toBeOperatedShoppingCartLineCommand){// 存在
            toBeOperatedShoppingCartLineCommand.setQuantity(oneLineTotalCount);
        }else{
            // 构造一条 塞进去
            ShoppingCartLineCommand shoppingCartLineCommand = buildShoppingCartLineCommand(sku.getId(), count, sku.getOutid());
            shoppingCartLineCommandList.add(shoppingCartLineCommand);

            toBeOperatedShoppingCartLineCommand = shoppingCartLineCommand;
        }

        //***********************************************************************************************

        if (shoppingCartInventoryValidator.isMoreThanInventory(shoppingCartLineCommandList, skuId, sku.getOutid())){
            return MAX_THAN_INVENTORY;
        }
        return null;
    }

    /**
     * 转换为ShoppingCartLineCommand对象.
     *
     * @param skuId
     *            the sku id
     * @param quantity
     *            the quantity
     * @param extensionCode
     *            the extension code
     * @return the shopping cart line command
     */
    private ShoppingCartLineCommand buildShoppingCartLineCommand(Long skuId,Integer quantity,String extensionCode){
        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
        shoppingCartLineCommand.setSkuId(skuId);
        shoppingCartLineCommand.setExtentionCode(extensionCode);
        shoppingCartLineCommand.setQuantity(quantity);
        shoppingCartLineCommand.setCreateTime(new Date());
        shoppingCartLineCommand.setSettlementState(Constants.CHECKED_CHOOSE_STATE);
        return shoppingCartLineCommand;
    }

}
