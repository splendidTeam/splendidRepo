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
package com.baozun.nebula.sdk.manager.order;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.promotion.SdkOrderPromotionManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartLineImageManager;
import com.feilong.core.Validator;
import com.feilong.core.bean.BeanUtil;
import com.feilong.core.lang.NumberUtil;

/**
 * The Class SdkOrderLineCreateManagerImpl.
 */
@Transactional
@Service("sdkOrderLineCreateManager")
public class SdkOrderLineCreateManagerImpl implements SdkOrderLineCreateManager{

    /** The Constant LOGGER. */
    private static final Logger             LOGGER = LoggerFactory.getLogger(SdkOrderLineCreateManagerImpl.class);

    /** The sdk order line dao. */
    @Autowired
    private SdkOrderLineDao                 sdkOrderLineDao;

    /** The sdk order line dao. */
    @Autowired
    private SdkSkuManager                   sdkSkuManager;

    @Autowired
    private SdkItemManager                  sdkItemManager;

    @Autowired
    private ItemDao                         itemDao;

    /** The sdk order promotion manager. */
    @Autowired
    private SdkOrderPromotionManager        sdkOrderPromotionManager;

    @Autowired
    private SdkShoppingCartLineImageManager sdkShoppingCartLineImageManager;

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

        if (null == relatedItemId){
            saveCommonLine(orderId, couponCodes, promotionSKUDiscAMTBySettingList, shoppingCartLineCommand);
        }else{
            saveRelatedLines(orderId, couponCodes, promotionSKUDiscAMTBySettingList, shoppingCartLineCommand);
        }
    }

    private void saveRelatedLines(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    ShoppingCartLineCommand shoppingCartLineCommand){

        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        Long[] skuIds = shoppingCartLineCommand.getSkuIds();
        Integer quantity = shoppingCartLineCommand.getQuantity();

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
     * @param relatedItemId
     * @param skuId
     * @param quantity
     * @param shoppingCartLineCommand
     * @return
     */
    private ShoppingCartLineCommand buildNewShoppingCartLineCommand(
                    Long relatedItemId,
                    Long skuId,
                    Integer quantity,
                    ShoppingCartLineCommand shoppingCartLineCommand){
        Integer type = shoppingCartLineCommand.getType();
        Long lineGroup = shoppingCartLineCommand.getLineGroup();

        //TODO feilong bundle 下单要进行拆分
        ShoppingCartLineCommand newShoppingCartLineCommand = BeanUtil.cloneBean(shoppingCartLineCommand);

        Sku sku = sdkSkuManager.findSkuById(skuId);
        Long itemId = sku.getItemId();
        Item item = itemDao.findItemById(itemId);
        ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoByCode(item.getCode());

        //FIXME feilong 
        BigDecimal listPrice = new BigDecimal(999);
        BigDecimal salePrice = new BigDecimal(999);
        BigDecimal discount = new BigDecimal(0);
        BigDecimal subTotalAmt = NumberUtil.getMultiplyValue(salePrice, quantity, 2);

        newShoppingCartLineCommand.setQuantity(quantity);
        newShoppingCartLineCommand.setSkuId(skuId);
        newShoppingCartLineCommand.setExtentionCode(sku.getOutid());
        newShoppingCartLineCommand.setItemId(itemId);
        newShoppingCartLineCommand.setRelatedItemId(relatedItemId);

        newShoppingCartLineCommand.setItemName(itemBaseCommand.getTitle());

        // 商品主图
        newShoppingCartLineCommand.setItemPic(sdkShoppingCartLineImageManager.getItemPicUrl(itemId));

        //******************************************************************************************
        // 原销售单价
        newShoppingCartLineCommand.setListPrice(listPrice);

        // 现销售单价
        newShoppingCartLineCommand.setSalePrice(salePrice);

        // 折扣、行类型
        newShoppingCartLineCommand.setDiscount(discount);

        // 行小计
        newShoppingCartLineCommand.setSubTotalAmt(subTotalAmt);

        // 销售属性信息
        newShoppingCartLineCommand.setSaleProperty(sku.getProperties());
        // 行类型
        newShoppingCartLineCommand.setType(type);
        // 分组号
        newShoppingCartLineCommand.setLineGroup(lineGroup);
        return newShoppingCartLineCommand;
    };

    /**
     * Save common line.
     *
     * @param orderId
     *            the order id
     * @param couponCodes
     *            the coupon codes
     * @param promotionSKUDiscAMTBySettingList
     *            the promotion sku disc amt by setting list
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     */
    private void saveCommonLine(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    ShoppingCartLineCommand shoppingCartLineCommand){
        //直推赠品(送完即止) 1：如果数量为零 该行不存入数据库 :2：如果库存量小于购买量时 存入库存量
        //是否是不需要用户选择的礼品
        boolean noNeedChoiceGift = shoppingCartLineCommand.isGift()
                        && GiftChoiceType.NoNeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType());
        if (noNeedChoiceGift){
            // 下架
            boolean isOffsale = !shoppingCartLineCommand.isValid() && shoppingCartLineCommand.getValidType() == 1;
            if (isOffsale){
                return;
            }
            // 无库存
            Integer stock = shoppingCartLineCommand.getStock();
            boolean isNostock = null == stock || stock <= 0;
            if (isNostock){
                return;
            }
            if (stock < shoppingCartLineCommand.getQuantity()){
                shoppingCartLineCommand.setQuantity(stock);// 库存不足
            }
        }

        OrderLine orderLine = saveOrderLine(orderId, shoppingCartLineCommand);
        if (Validator.isNotNullOrEmpty(promotionSKUDiscAMTBySettingList)){
            savaOrderLinePromotions(orderId, couponCodes, promotionSKUDiscAMTBySettingList, orderLine);
        }
    }

    private OrderLine saveOrderLine(Long orderId,ShoppingCartLineCommand shoppingCartLineCommand){
        OrderLine orderLine = buildOrderLine(orderId, shoppingCartLineCommand);
        return sdkOrderLineDao.save(orderLine);
    }

    private OrderLine buildOrderLine(Long orderId,ShoppingCartLineCommand shoppingCartLineCommand){
        OrderLine orderLine = new OrderLine();
        // 订单id
        orderLine.setOrderId(orderId);
        // 商品数量
        orderLine.setCount(shoppingCartLineCommand.getQuantity());

        // skuId
        orderLine.setSkuId(shoppingCartLineCommand.getSkuId());
        // UPC
        orderLine.setExtentionCode(shoppingCartLineCommand.getExtentionCode());
        // 商品id
        orderLine.setItemId(shoppingCartLineCommand.getItemId());
        //对应关联关系的商品id.
        orderLine.setRelatedItemId(shoppingCartLineCommand.getRelatedItemId());
        // 商品名称
        orderLine.setItemName(shoppingCartLineCommand.getItemName());
        // 商品主图
        orderLine.setItemPic(shoppingCartLineCommand.getItemPic());
        //******************************************************************************************
        // 原销售单价
        orderLine.setMSRP(shoppingCartLineCommand.getListPrice());
        // 现销售单价
        BigDecimal salePrice = shoppingCartLineCommand.getSalePrice();
        orderLine.setSalePrice(salePrice);
        // 行小计
        orderLine.setSubtotal(shoppingCartLineCommand.getSubTotalAmt());
        // 折扣、行类型
        if (shoppingCartLineCommand.isGift()){
            orderLine.setDiscount(salePrice);
            orderLine.setType(ItemInfo.TYPE_GIFT);
        }else{
            orderLine.setDiscount(shoppingCartLineCommand.getDiscount());
            orderLine.setType(ItemInfo.TYPE_MAIN);
        }

        // 销售属性信息
        orderLine.setSaleProperty(shoppingCartLineCommand.getSaleProperty());
        // 行类型
        orderLine.setType(shoppingCartLineCommand.getType());
        // 分组号
        Long lineGroup = shoppingCartLineCommand.getLineGroup();
        if (Validator.isNotNullOrEmpty(lineGroup)){
            orderLine.setGroupId(lineGroup.intValue());
        }
        // 评价状态
        orderLine.setEvaluationStatus(null);
        // 商品快照版本
        orderLine.setSnapshot(null);
        return orderLine;
    }

    /**
     * Sava order line promotions.
     *
     * @param orderId
     *            the order id
     * @param couponCodes
     *            the coupon codes
     * @param promotionSKUDiscAMTBySettingList
     *            the promotion sku disc amt by setting list
     * @param orderLine
     *            the order line
     */
    private void savaOrderLinePromotions(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    OrderLine orderLine){

        // 非免运费
        Long orderLineSkuId = orderLine.getSkuId();
        Integer orderLineType = orderLine.getType();
        Long orderLineId = orderLine.getId();

        for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList){
            boolean giftMark = promotionSKUDiscAMTBySetting.getGiftMark();
            //0代表赠品 1代表主卖品
            Integer type = !giftMark ? 1 : 0;

            boolean freeShippingMark = promotionSKUDiscAMTBySetting.getFreeShippingMark();
            boolean equalsType = orderLineType.equals(type);
            boolean equalsSkuId = promotionSKUDiscAMTBySetting.getSkuId().equals(orderLineSkuId);

            if (!freeShippingMark && equalsSkuId && equalsType){
                sdkOrderPromotionManager.savaOrderPromotion(orderId, orderLineId, promotionSKUDiscAMTBySetting, couponCodes);
            }
        }
    }

}
