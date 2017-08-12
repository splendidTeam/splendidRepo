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

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryManager;
import com.baozun.nebula.utils.ShoppingCartUtil;

import static com.feilong.core.util.CollectionsUtil.select;

/**
 * 购物车 sku 库存校验.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.3
 */
@Component("shoppingCartInventoryValidator")
public class DefaultShoppingCartInventoryValidator implements ShoppingCartInventoryValidator{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShoppingCartInventoryValidator.class);

    @Autowired
    private SdkSkuInventoryManager sdkSkuInventoryManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartInventoryValidator#isMoreThanInventory(java.util.List, java.lang.Long, java.lang.String)
     */
    @Override
    public boolean isMoreThanInventory(List<ShoppingCartLineCommand> shoppingCartLineCommandList,Long skuId){
        //相同 skuId 所有的 lines
        List<ShoppingCartLineCommand> sameSkuIdLines = select(shoppingCartLineCommandList, "skuId", skuId);
        Integer sumBuyCount = ShoppingCartUtil.getSumQuantity(sameSkuIdLines);

        return isMoreThanInventory(skuId, sumBuyCount);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartInventoryValidator#isMoreThanInventory(long, int)
     */
    @Override
    public boolean isMoreThanInventory(long skuId,int sumBuyCount){
        Validate.isTrue(sumBuyCount > 0, "sumBuyCount:[%s] must > 0", sumBuyCount);

        //---------------------------------------------------------------------
        SkuInventory inventoryInDb = sdkSkuInventoryManager.findSkuInventoryBySkuId(skuId);
        Validate.notNull(inventoryInDb, "when skuId:[%s] ,inventoryInDb can't be null!", skuId);

        //---------------------------------------------------------------------

        Integer availableQty = inventoryInDb.getAvailableQty();
        boolean isMoreThanInventory = sumBuyCount > availableQty;

        //---------------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("skuId:[{}],sumBuyCount:[{}],availableQty:[{}],return :[{}]", skuId, sumBuyCount, availableQty, isMoreThanInventory);
        }

        return isMoreThanInventory;
    }
}
