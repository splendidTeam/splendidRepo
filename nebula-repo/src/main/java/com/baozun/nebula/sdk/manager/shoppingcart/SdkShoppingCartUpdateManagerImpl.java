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

import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;

/**
 * The Class SdkShoppingCartUpdateManagerImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.9
 */
@Transactional
@Service("sdkShoppingCartUpdateManager")
public class SdkShoppingCartUpdateManagerImpl implements SdkShoppingCartUpdateManager{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkShoppingCartUpdateManagerImpl.class);

    /** The sdk shopping cart line dao. */
    @Autowired
    private SdkShoppingCartLineDao sdkShoppingCartLineDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartUpdateManager#updateCartLineQuantityByLineId(java.lang.Long, java.lang.Long, java.lang.Integer)
     */
    @Override
    public void updateCartLineQuantityByLineId(Long memberId,Long lineId,Integer quantity){
        Validate.notNull(memberId, "quantity can't be null!");
        Validate.notNull(lineId, "quantity can't be null!");
        Validate.notNull(quantity, "quantity can't be null!");

        int result = sdkShoppingCartLineDao.updateCartLineQuantityByLineId(memberId, lineId, quantity);
        if (1 != result){
            LOGGER.error("update:[{}],lineId:[{}] to quantity:[{}],result is:[{}], not expected 1", memberId, lineId, quantity, result);
            throw new NativeUpdateRowCountNotEqualException(1, result);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartUpdateManager#updateCartLineQuantity(java.lang.Long, java.util.Map)
     */
    @Override
    public void updateCartLineQuantity(Long memberId,Map<Long, Integer> shoppingcartLineIdAndCountMap){
        Validate.notEmpty(shoppingcartLineIdAndCountMap, "shoppingcartLineIdAndCountMap can't be null/empty!");

        for (Map.Entry<Long, Integer> entry : shoppingcartLineIdAndCountMap.entrySet()){
            Long lineId = entry.getKey();
            Integer quantity = entry.getValue();

            updateCartLineQuantityByLineId(memberId, lineId, quantity);
        }
    }
}
