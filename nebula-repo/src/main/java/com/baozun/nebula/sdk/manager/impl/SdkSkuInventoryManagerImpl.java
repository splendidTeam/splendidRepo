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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryManager;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.SdkShoppingCartLineCommandBehaviour;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.SdkShoppingCartLineCommandBehaviourFactory;
import com.baozun.nebula.utils.ShoppingCartUtil;

/**
 * The Class SdkSkuInventoryManagerImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月12日 下午12:58:43
 * @since 5.3.1
 */
@Transactional
@Service("sdkSkuInventoryManager")
public class SdkSkuInventoryManagerImpl implements SdkSkuInventoryManager{

    /** The sdk sku inventory dao. */
    @Autowired
    private SdkSkuInventoryDao                         sdkSkuInventoryDao;

    @Autowired
    private SdkShoppingCartLineCommandBehaviourFactory sdkShoppingCartLineCommandBehaviourFactory;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkSkuInventoryManager#deductSkuInventory(java.util.List)
     */
    @Override
    public void deductSkuInventory(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        Validate.notEmpty(shoppingCartLineCommandList, "shoppingCartLineCommandList can't be null/empty!");

        Map<String, Integer> extentionCodeAndCountMap = buildExtentionCodeAndCountMap(shoppingCartLineCommandList);
        deductSkuInventory(extentionCodeAndCountMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkSkuInventoryManager#deductSkuInventory(java.util.Map)
     */
    @Override
    public void deductSkuInventory(Map<String, Integer> extentionCodeandCountMap){
        Validate.notEmpty(extentionCodeandCountMap, "extentionCodeandCountMap can't be null/empty!");

        for (Map.Entry<String, Integer> entry : extentionCodeandCountMap.entrySet()){
            String extentionCode = entry.getKey();
            Integer count = entry.getValue();

            Validate.notBlank(extentionCode, "extentionCode can't be blank!");
            Validate.isTrue(count > 0, "count must > 0,extentionCode is %s", extentionCode);

            int result = sdkSkuInventoryDao.liquidateSkuInventory(extentionCode, count);
            // 返回的行数是否为 1 如果不是,说明库存不足 就抛出异常
            if (result != 1){
                throw new BusinessException(Constants.CHECK_INVENTORY_FAILURE, new Object[] { extentionCode });
            }
        }
    }

    /**
     * Builds the extention code and count map.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @return the map< string, integer>
     */
    private Map<String, Integer> buildExtentionCodeAndCountMap(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        Map<String, Integer> extentionCodeAndCountMap = new HashMap<String, Integer>();
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            //如果直推礼品库存数小于购买量时，扣减现有库存
            Integer quantity = shoppingCartLineCommand.getQuantity();
            if (ShoppingCartUtil.isNoNeedChoiceGift(shoppingCartLineCommand)){//是否是不需要用户选择的礼品.
                //下架
                if (!shoppingCartLineCommand.isValid() && shoppingCartLineCommand.getValidType() == 1){
                    continue;
                }
                Integer stock = shoppingCartLineCommand.getStock();
                if (null == stock || stock <= 0){
                    continue;
                }else if (stock < quantity){
                    shoppingCartLineCommand.setQuantity(stock);
                }
            }
            SdkShoppingCartLineCommandBehaviour sdkShoppingCartLineCommandBehaviour = sdkShoppingCartLineCommandBehaviourFactory
                            .getShoppingCartLineCommandBehaviour(shoppingCartLineCommand);
            sdkShoppingCartLineCommandBehaviour
                            .organizeExtentionCodeAndCountMapForDeductSkuInventory(shoppingCartLineCommand, extentionCodeAndCountMap);
        }
        return extentionCodeAndCountMap;
    }
}
