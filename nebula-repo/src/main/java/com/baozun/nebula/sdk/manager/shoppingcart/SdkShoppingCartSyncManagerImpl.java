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
package com.baozun.nebula.sdk.manager.shoppingcart;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * The Class SdkShoppingCartSyncManagerImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月23日 下午6:24:22
 * @since 5.3.1
 */
@Transactional
@Service("sdkShoppingCartSyncManager")
public class SdkShoppingCartSyncManagerImpl implements SdkShoppingCartSyncManager{

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
            Validate.notBlank(extentionCode, "extentionCode can't be null/empty!");

            Integer quantity = shoppingCartLineCommand.getQuantity();
            Validate.isTrue(quantity >= 0, "quantity must >= 0,but:%s", quantity);

            ShoppingCartLineCommand cartLineInDb = sdkShoppingCartLineDao.findShopCartLine(memberId, extentionCode);

            if (null != cartLineInDb){ //如果数据库购物车表中会员有该商品，则将把该商品的数量相加
                updateCartLineQuantityByLineId(memberId, cartLineInDb.getId(), cartLineInDb.getQuantity() + quantity);
            }else{
                saveCartLine(memberId, shoppingCartLineCommand);
            }
        }
    }

    /**
     * Update cart line quantity by line id.
     *
     * @param memberId
     *            the member id
     * @param quantity
     *            the quantity
     * @param cartLineInDb
     *            the cart line in db
     */
    private void updateCartLineQuantityByLineId(Long memberId,Long lineId,Integer quantity){
        int result = sdkShoppingCartLineDao.updateCartLineQuantityByLineId(memberId, lineId, quantity);
        if (1 != result){
            throw new NativeUpdateRowCountNotEqualException(1, result);
        }
    }

    /**
     * 保存或更新购物行信息.
     *
     * @param shoppingCartLine
     *            the shopping cart line
     */
    private void saveCartLine(Long memberId,ShoppingCartLineCommand shoppingCartLine){
        // 保存
        int result = sdkShoppingCartLineDao.insertShoppingCartLine(
                        shoppingCartLine.getExtentionCode(),
                        shoppingCartLine.getSkuId(),
                        shoppingCartLine.getQuantity(),
                        memberId,
                        shoppingCartLine.getCreateTime(),
                        shoppingCartLine.getSettlementState(),
                        shoppingCartLine.getShopId(),
                        shoppingCartLine.isGift(),
                        shoppingCartLine.getPromotionId(),
                        shoppingCartLine.getLineGroup());

        if (1 != result){
            throw new NativeUpdateRowCountNotEqualException(1, result);
        }
    }
}
