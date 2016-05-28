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

import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.feilong.core.DatePattern;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;

/**
 * The Class ShoppingcartLineOperateCommonValidatorImpl.
 *
 * @author feilong
 * @version 5.3.1 2016年5月25日 下午4:48:40
 * @since 5.3.1
 */
@Component("shoppingcartLineOperateCommonValidator")
public class ShoppingcartLineOperateCommonValidatorImpl implements ShoppingcartLineOperateCommonValidator{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingcartLineOperateCommonValidatorImpl.class);

    /** The sdk item manager. */
    @Autowired
    private SdkItemManager      sdkItemManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartLineOperateCommonValidator#validate(com.baozun.nebula.model.
     * product.Sku, java.lang.Integer)
     */
    @Override
    public ShoppingcartResult validate(Sku sku,Integer count){
        //===============① 数量不能小于1===============
        Validate.isTrue(count >= 1, "count:%s can not <1", count);

        //===============② 判断sku是否存在===============
        if (Validator.isNullOrEmpty(sku)){
            //TODO feilong change to exception
            LOGGER.error("sku not exist!!!!!!");
            return ShoppingcartResult.SKU_NOT_EXIST;
        }

        //===============③ 判断sku生命周期===============
        Integer skuLifecycle = sku.getLifecycle();
        if (!skuLifecycle.equals(Sku.LIFE_CYCLE_ENABLE)){
            LOGGER.error("sku's lifecycle is:{},return SKU_NOT_ENABLE", skuLifecycle);
            return ShoppingcartResult.SKU_NOT_ENABLE;
        }

        ItemCommand itemCommand = sdkItemManager.findItemCommandById(sku.getItemId());
        // item生命周期验证
        Integer lifecycle = itemCommand.getLifecycle();

        //===============④  判断item的生命周期===============
        if (!Constants.ITEM_ADDED_VALID_STATUS.equals(String.valueOf(lifecycle))){
            Long id = itemCommand.getId();
            LOGGER.error("item id:{}, status is :{} can not operate in shoppingcart", id, lifecycle);
            return ShoppingcartResult.ITEM_STATUS_NOT_ENABLE;
        }

        //===============⑤  还没上架===============
        Date activeBeginTime = itemCommand.getActiveBeginTime();
        Date now = new Date();
        String itemCode = itemCommand.getCode();
        if (null != activeBeginTime && DateUtil.isAfter(activeBeginTime, now)){
            LOGGER.warn(
                            "now is :[{}],but item:[{}]'s activeBeginTime is:{},return ITEM_NOT_ACTIVE_TIME",
                            DateUtil.date2String(now, DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                            itemCode,
                            DateUtil.date2String(activeBeginTime, DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND));
            return ShoppingcartResult.ITEM_NOT_ACTIVE_TIME;
        }

        //===============⑥ 赠品验证===============
        if (ItemInfo.TYPE_GIFT.equals(itemCommand.getType())){
            LOGGER.warn("item:[{}] is gift don't need operate,return ITEM_IS_GIFT", itemCode);
            return ShoppingcartResult.ITEM_IS_GIFT;
        }
        return null;
    }
}
