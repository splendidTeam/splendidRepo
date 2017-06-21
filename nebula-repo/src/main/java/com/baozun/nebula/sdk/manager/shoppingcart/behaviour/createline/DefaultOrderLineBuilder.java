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

import static com.baozun.nebula.model.product.ItemInfo.TYPE_GIFT;
import static com.baozun.nebula.model.product.ItemInfo.TYPE_MAIN;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("orderLineBuilder")
public class DefaultOrderLineBuilder implements OrderLineBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderLineBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline.OrderLineBuilder#buildOrderLine(java.lang.Long, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public OrderLine buildOrderLine(Long orderId,ShoppingCartLineCommand shoppingCartLineCommand){
        boolean isGift = shoppingCartLineCommand.isGift();

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
        // 杂项
        orderLine.setMisc(shoppingCartLineCommand.getMisc());
        //******************************************************************************************

        //设置价格信息
        setPrice(orderLine, shoppingCartLineCommand, isGift);

        // 销售属性信息
        orderLine.setSaleProperty(shoppingCartLineCommand.getSaleProperty());

        // 行类型
        orderLine.setType(isGift ? TYPE_GIFT : TYPE_MAIN);
        // 分组号
        Long lineGroup = shoppingCartLineCommand.getLineGroup();
        if (isNotNullOrEmpty(lineGroup)){
            orderLine.setGroupId(lineGroup.intValue());
        }
        // 评价状态
        orderLine.setEvaluationStatus(null);
        // 商品快照版本
        orderLine.setSnapshot(null);

        orderLine.setVersion(new Date());
        return orderLine;
    }
    /**
     * 设置 price.
     *
     * @param orderLine
     *            the order line
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param isGift
     *            the is gift
     */
    private void setPrice(OrderLine orderLine,ShoppingCartLineCommand shoppingCartLineCommand,boolean isGift){
        // 原销售单价
        BigDecimal listPrice = shoppingCartLineCommand.getListPrice();
        orderLine.setMSRP(listPrice);
        // 现销售单价
        BigDecimal salePrice = shoppingCartLineCommand.getSalePrice();
        orderLine.setSalePrice(salePrice);
        // 行小计
        BigDecimal subTotalAmt = shoppingCartLineCommand.getSubTotalAmt();
        orderLine.setSubtotal(subTotalAmt);
        // 折扣、行类型
        BigDecimal discount = shoppingCartLineCommand.getDiscount();
        orderLine.setDiscount(isGift ? salePrice : discount);

        LOGGER.debug("orderLine msrp:{},salePrice:{},discount:{},subtotal:{}", listPrice, salePrice, discount, subTotalAmt);
    }
}
