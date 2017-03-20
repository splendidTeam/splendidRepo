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
package com.baozun.nebula.web.controller.shoppingcart.converter;

import java.util.Map;

import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLinePackageInfoViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.Status;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.collect;

/**
 * 将 {@link ShoppingCartLineCommand} 转成 {@link ShoppingCartLineSubViewCommand}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public class ShoppingCartLineCommandToViewCommandTransformer implements Transformer<ShoppingCartLineCommand, ShoppingCartLineSubViewCommand>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartLineCommandToViewCommandTransformer.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Transformer#transform(java.lang.Object)
     */
    @Override
    public ShoppingCartLineSubViewCommand transform(ShoppingCartLineCommand shoppingCartLineCommand){
        ShoppingCartLineSubViewCommand shoppingCartLineSubViewCommand = new ShoppingCartLineSubViewCommand();

        Integer settlementState = shoppingCartLineCommand.getSettlementState();
        boolean checked = isNotNullOrEmpty(settlementState) ? settlementState != 0 : false;
        shoppingCartLineSubViewCommand.setChecked(checked);

        shoppingCartLineSubViewCommand.setIsGift(shoppingCartLineCommand.isGift());
        shoppingCartLineSubViewCommand.setItemCode(shoppingCartLineCommand.getProductCode());
        shoppingCartLineSubViewCommand.setAddTime(shoppingCartLineCommand.getCreateTime());
        PropertyUtil.copyProperties(
                        shoppingCartLineSubViewCommand,
                        shoppingCartLineCommand,
                        "extentionCode",
                        "stock", // since 5.3.1.8
                        "itemId",
                        "itemName",
                        "listPrice",
                        "quantity",
                        "salePrice",
                        "skuId",
                        "itemPic",
                        "id",
                        "subTotalAmt");

        Map<String, SkuProperty> map = CollectionsUtil.groupOne(shoppingCartLineCommand.getSkuPropertys(), "pName");
        shoppingCartLineSubViewCommand.setPropertiesMap(map);
        shoppingCartLineSubViewCommand.setStatus(toStatus(shoppingCartLineCommand));
        shoppingCartLineSubViewCommand.setShoppingCartLinePackageInfoViewCommandList(collect(shoppingCartLineCommand.getShoppingCartLinePackageInfoCommandList(), ShoppingCartLinePackageInfoViewCommand.class));

        return shoppingCartLineSubViewCommand;
    }

    private static Status toStatus(ShoppingCartLineCommand shoppingCartLineCommand){
        if (shoppingCartLineCommand.isValid()){
            return Status.NORMAL;
        }

        //目前 看到底层只实现了这两种， 以后要扩展
        //******************************************************************
        Integer validType = shoppingCartLineCommand.getValidType();
        if (validType == 1){
            return Status.ITEM_LIFECYCLE_OFF_SHELF;
        }else if (validType == 2){
            return Status.OUT_OF_STOCK;
        }
        //TODO 待扩展

        String messagePattern = "validType:[{}] not support!";
        throw new UnsupportedOperationException(Slf4jUtil.format(messagePattern, validType));
    }
}
