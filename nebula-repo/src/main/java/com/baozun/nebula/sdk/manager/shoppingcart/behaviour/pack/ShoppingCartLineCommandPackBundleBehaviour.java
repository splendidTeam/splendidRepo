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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.pack;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.product.BundleSkuPriceCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.product.SdkBundleManager;
import com.feilong.core.lang.NumberUtil;
import com.feilong.core.util.AggregateUtil;

/**
 * bundle类商品的行为.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("shoppingCartLineCommandPackBundleBehaviour")
public class ShoppingCartLineCommandPackBundleBehaviour extends AbstractShoppingCartLineCommandPackBehaviour{

    /** The item dao. */
    @Autowired
    private ItemDao          itemDao;

    /** The sdk bundle manager. */
    @Autowired
    private SdkBundleManager sdkBundleManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.shoppingcart.behaviour.AbstractSdkShoppingCartLineCommandBehaviour#doPackShoppingCartLine(com.baozun.
     * nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    protected void doPackShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand){
        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        Item item = itemDao.findItemById(relatedItemId);

        packPriceInfo(relatedItemId, shoppingCartLineCommand);

        //封装 店铺和 行业信息
        packShopAndIndustry(shoppingCartLineCommand, item);

        //封装 商品信息
        packItemInfo(shoppingCartLineCommand, item);

        packCategoryAndLables(shoppingCartLineCommand, item.getId());
    }

    /**
     * 封装价格信息.
     *
     * @param relatedItemId
     *            the related item id
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @since 5.3.1.6
     */
    private void packPriceInfo(Long relatedItemId,ShoppingCartLineCommand shoppingCartLineCommand){
        Long[] skuIds = shoppingCartLineCommand.getSkuIds();
        Integer quantity = shoppingCartLineCommand.getQuantity();
        List<BundleSkuPriceCommand> bundleSkuPriceCommandList = sdkBundleManager.getBundleSkusPrice(relatedItemId, skuIds);

        Map<String, BigDecimal> sumMap = AggregateUtil.sum(bundleSkuPriceCommandList, "listPrice", "salesPrice", "originalSalesPrice");

        //*******************************************************************************************
        BigDecimal originalSalesPriceSum = sumMap.get("originalSalesPrice");
        BigDecimal salesPriceSum = sumMap.get("salesPrice");

        shoppingCartLineCommand.setListPrice(sumMap.get("listPrice"));
        shoppingCartLineCommand.setSalePrice(originalSalesPriceSum);//salesPriceSum

        BigDecimal disCountSum = originalSalesPriceSum.subtract(salesPriceSum);
        // BigDecimal disCountSum = BigDecimal.ZERO;

        shoppingCartLineCommand.setDiscount(NumberUtil.getMultiplyValue(disCountSum, quantity, 2));
    }

}
