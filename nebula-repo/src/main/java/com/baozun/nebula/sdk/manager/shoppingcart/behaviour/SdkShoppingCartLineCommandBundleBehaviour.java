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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.product.SdkBundleManager;
import com.feilong.core.util.MapUtil;

/**
 * The Class SdkShoppingCartLineCommandBundleBehaviour.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("sdkShoppingCartLineCommandBundleBehaviour")
public class SdkShoppingCartLineCommandBundleBehaviour implements SdkShoppingCartLineCommandBehaviour{

    /** The sku dao. */
    @Autowired
    private SkuDao           skuDao;

    /** The sdk bundle manager. */
    @Autowired
    private SdkBundleManager sdkBundleManager;

    /**
     * {@inheritDoc}
     * 
     * <h3>流程.</h3>
     * 
     * <blockquote>
     * <p>
     * <img src="http://venusdrogon.github.io/feilong-platform/mysource/store/bundle库存扣减流程.png"/>
     * </p>
     * </blockquote>
     */
    @Override
    public void organizeExtentionCodeAndCountMapForDeductSkuInventory(
                    ShoppingCartLineCommand shoppingCartLineCommand,
                    Map<String, Integer> extentionCodeAndCountMap){

        Integer quantity = shoppingCartLineCommand.getQuantity();
        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();

        BundleCommand bundleCommand = sdkBundleManager.findBundleCommandByBundleItemId(relatedItemId, true);
        //捆绑装库存
        Integer availableQty = bundleCommand.getAvailableQty();
        Long[] skuIds = shoppingCartLineCommand.getSkuIds();

        boolean isDeductingBundleInventory = null != availableQty;
        if (isDeductingBundleInventory){
            sdkBundleManager.deductingBundleInventory(relatedItemId, quantity);
        }

        boolean isSyncWithInv = null == availableQty || BooleanUtils.isTrue(bundleCommand.getSyncWithInv());
        if (isSyncWithInv){
            List<Sku> enableSkuListBySkuIds = skuDao.findEnableSkuListBySkuIds(skuIds);
            Validate.isTrue(
                            enableSkuListBySkuIds.size() == skuIds.length,
                            "enableSkuListBySkuIds size:%s,!= skuIds.length :%s",
                            enableSkuListBySkuIds.size(),
                            skuIds.length);

            for (Sku sku : enableSkuListBySkuIds){
                MapUtil.putSumValue(extentionCodeAndCountMap, sku.getOutid(), quantity);
            }
        }
    }
}
