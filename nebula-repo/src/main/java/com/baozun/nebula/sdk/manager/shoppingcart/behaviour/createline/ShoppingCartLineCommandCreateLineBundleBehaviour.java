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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.product.BundleSkuPriceCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.product.SdkBundleManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartLineImageManager;
import com.feilong.core.bean.BeanUtil;
import com.feilong.core.lang.NumberUtil;

/**
 * bundle类型的 ShoppingCartLineCommand 创建订单行.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("shoppingCartLineCommandCreateLineBundleBehaviour")
public class ShoppingCartLineCommandCreateLineBundleBehaviour extends AbstractShoppingCartLineCommandCreateLineBehaviour{

    /** The Constant LOGGER. */
    private static final Logger             LOGGER = LoggerFactory.getLogger(ShoppingCartLineCommandCreateLineBundleBehaviour.class);

    /** The sdk order line dao. */
    @Autowired
    private SdkSkuManager                   sdkSkuManager;

    /** The sdk item manager. */
    @Autowired
    private SdkItemManager                  sdkItemManager;

    /** The item dao. */
    @Autowired
    private ItemDao                         itemDao;

    /** The sdk shopping cart line image manager. */
    @Autowired
    private SdkShoppingCartLineImageManager sdkShoppingCartLineImageManager;

    /** The sdk bundle manager. */
    @Autowired
    private SdkBundleManager                sdkBundleManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.SdkOrderLineCreateManager#saveOrderLine(java.lang.Long, java.util.List, java.util.List,
     * com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void saveOrderLine(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    ShoppingCartLineCommand shoppingCartLineCommand){

        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        Long[] skuIds = shoppingCartLineCommand.getSkuIds();
        Integer quantity = shoppingCartLineCommand.getQuantity();

        //TODO feilong bundle 下单要进行拆分
        for (Long skuId : skuIds){
            ShoppingCartLineCommand newShoppingCartLineCommand = buildNewShoppingCartLineCommand(
                            relatedItemId,
                            skuId,
                            quantity,
                            shoppingCartLineCommand);
            saveCommonLine(orderId, couponCodes, promotionSKUDiscAMTBySettingList, newShoppingCartLineCommand);
        }
    }

    /**
     * Builds the new shopping cart line command.
     *
     * @param relatedItemId
     *            the related item id
     * @param skuId
     *            the sku id
     * @param quantity
     *            the quantity
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return the shopping cart line command
     */
    private ShoppingCartLineCommand buildNewShoppingCartLineCommand(
                    Long relatedItemId,
                    Long skuId,
                    Integer quantity,
                    ShoppingCartLineCommand shoppingCartLineCommand){
        Integer type = shoppingCartLineCommand.getType();
        Long lineGroup = shoppingCartLineCommand.getLineGroup();

        ShoppingCartLineCommand newShoppingCartLineCommand = BeanUtil.cloneBean(shoppingCartLineCommand);

        Sku sku = sdkSkuManager.findSkuById(skuId);
        Long itemId = sku.getItemId();
        Item item = itemDao.findItemById(itemId);
        ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoByCode(item.getCode());

        newShoppingCartLineCommand.setQuantity(quantity);
        newShoppingCartLineCommand.setSkuId(skuId);
        newShoppingCartLineCommand.setExtentionCode(sku.getOutid());
        newShoppingCartLineCommand.setItemId(itemId);
        newShoppingCartLineCommand.setRelatedItemId(relatedItemId);

        newShoppingCartLineCommand.setItemName(itemBaseCommand.getTitle());

        // 商品主图
        newShoppingCartLineCommand.setItemPic(sdkShoppingCartLineImageManager.getItemPicUrl(itemId));

        //******************************************************************************************
        //设置金额
        serLinePrices(newShoppingCartLineCommand, relatedItemId, skuId, quantity);

        // 销售属性信息
        newShoppingCartLineCommand.setSaleProperty(sku.getProperties());
        // 行类型
        newShoppingCartLineCommand.setType(type);
        // 分组号
        newShoppingCartLineCommand.setLineGroup(lineGroup);
        return newShoppingCartLineCommand;
    }

    /**
     * @param newShoppingCartLineCommand
     * @param relatedItemId
     * @param skuId
     * @param quantity
     */
    private void serLinePrices(ShoppingCartLineCommand newShoppingCartLineCommand,Long relatedItemId,Long skuId,Integer quantity){
        //FIXME feilong bundle商品金额
        BundleSkuPriceCommand bundleSkuPriceCommand = sdkBundleManager.getBundleSkuPrice(relatedItemId, skuId);
        BigDecimal listPrice = bundleSkuPriceCommand.getListPrice();
        BigDecimal salePrice = bundleSkuPriceCommand.getSalesPrice();
        BigDecimal discount = new BigDecimal(0);
        // 原销售单价
        newShoppingCartLineCommand.setListPrice(listPrice);

        // 现销售单价
        newShoppingCartLineCommand.setSalePrice(salePrice);

        // 折扣、行类型
        newShoppingCartLineCommand.setDiscount(discount);

        // 行小计
        newShoppingCartLineCommand.setSubTotalAmt(NumberUtil.getMultiplyValue(salePrice, quantity, 2));
    };

}
