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
package com.baozun.nebula.sdk.manager.impl;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkShoppingCartSyncManager;

/**
 * The Class SdkShoppingCartSyncManagerImpl.
 *
 * @author feilong
 * @version 5.3.1 2016年5月23日 下午6:24:22
 * @since 5.3.1
 */
@Transactional
@Service("sdkShoppingCartSyncManager")
public class SdkShoppingCartSyncManagerImpl implements SdkShoppingCartSyncManager{

    /** The Constant LOGGER. */
    private static final Logger    LOGGER = LoggerFactory.getLogger(SdkShoppingCartSyncManagerImpl.class);

    /** The sdk shopping cart line dao. */
    @Autowired
    private SdkShoppingCartLineDao sdkShoppingCartLineDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkShoppingCartSyncManager#syncShoppingCart(java.lang.Long, java.util.List)
     */
    @Override
    public void syncShoppingCart(Long memberId,List<ShoppingCartLineCommand> shoppingLines){
        Validate.notNull(memberId, "memberId can't be null!");
        Validate.notEmpty(shoppingLines, "shoppingLines can't be null!");

        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingLines){
            if (shoppingCartLineCommand.isGift()){ // 不同步赠品数据
                continue;
            }

            String extentionCode = shoppingCartLineCommand.getExtentionCode();
            Integer quantity = shoppingCartLineCommand.getQuantity();
            ShoppingCartLineCommand cartLineInDb = sdkShoppingCartLineDao.findShopCartLine(memberId, extentionCode);

            int result = 0;
            if (null != cartLineInDb){ //如果数据库购物车表中会员有该商品，则将把该商品的数量相加
                result = sdkShoppingCartLineDao
                                .updateCartLineQuantityByLineId(memberId, cartLineInDb.getId(), cartLineInDb.getQuantity() + quantity);
            }else{
                result = sdkShoppingCartLineDao.addCartLineQuantity(memberId, extentionCode, quantity);
            }

            if (1 != result){
                throw new NativeUpdateRowCountNotEqualException(1, result);
            }
        }
    }
}
