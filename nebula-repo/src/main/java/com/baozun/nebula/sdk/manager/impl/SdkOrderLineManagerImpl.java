/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package com.baozun.nebula.sdk.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkOrderLineManager;
import com.feilong.core.Validator;

/**
 * @author - 项硕
 */
@Transactional
@Service("SdkOrderLineManager")
public class SdkOrderLineManagerImpl implements SdkOrderLineManager{

    @Autowired
    private SdkOrderLineDao sdkOrderLineDao;

    @Override
    @Transactional(readOnly = true)
    public OrderLine findByPk(Long id){
        return sdkOrderLineDao.getByPrimaryKey(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderLineCommand> findOrderLinesByOrderId(Long orderId){
        return sdkOrderLineDao.findOrderLinesByOrderId(orderId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderLineManager#saveOrderLine(java.lang.Long,
     * com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    //TODO feilong bundle 下单要进行拆分
    public OrderLine saveOrderLine(Long orderId,ShoppingCartLineCommand shoppingCartLineCommand){
        OrderLine orderLine = new OrderLine();
        // 商品数量
        orderLine.setCount(shoppingCartLineCommand.getQuantity());

        //直推赠品(送完即止) 1：如果数量为零 该行不存入数据库 :2：如果库存量小于购买量时 存入库存量
        if (isNoNeedChoiceGift(shoppingCartLineCommand)){
            // 下架
            if (!shoppingCartLineCommand.isValid() && shoppingCartLineCommand.getValidType() == 1){
                return null;
            }
            // 无库存
            if (null == shoppingCartLineCommand.getStock() || shoppingCartLineCommand.getStock() <= 0){
                return null;
            }else if (shoppingCartLineCommand.getStock() < shoppingCartLineCommand.getQuantity()){
                // 库存不足
                orderLine.setCount(shoppingCartLineCommand.getStock());
            }
        }

        // 订单id
        orderLine.setOrderId(orderId);
        // UPC
        orderLine.setExtentionCode(shoppingCartLineCommand.getExtentionCode());
        // skuId
        orderLine.setSkuId(shoppingCartLineCommand.getSkuId());
        // 商品id
        orderLine.setItemId(shoppingCartLineCommand.getItemId());
        // 原销售单价
        orderLine.setMSRP(shoppingCartLineCommand.getListPrice());
        // 现销售单价
        orderLine.setSalePrice(shoppingCartLineCommand.getSalePrice());
        // 行小计
        orderLine.setSubtotal(shoppingCartLineCommand.getSubTotalAmt());
        // 折扣、行类型
        if (shoppingCartLineCommand.isGift()){
            orderLine.setDiscount(shoppingCartLineCommand.getSalePrice());
            orderLine.setType(ItemInfo.TYPE_GIFT);
        }else{
            orderLine.setDiscount(shoppingCartLineCommand.getDiscount());
            orderLine.setType(ItemInfo.TYPE_MAIN);
        }

        // 商品名称
        orderLine.setItemName(shoppingCartLineCommand.getItemName());
        // 商品主图
        orderLine.setItemPic(shoppingCartLineCommand.getItemPic());
        // 销售属性信息
        orderLine.setSaleProperty(shoppingCartLineCommand.getSaleProperty());
        // 行类型
        orderLine.setType(shoppingCartLineCommand.getType());
        // 分组号
        if (Validator.isNotNullOrEmpty(shoppingCartLineCommand.getLineGroup())){
            orderLine.setGroupId(Integer.valueOf(shoppingCartLineCommand.getLineGroup().toString()));
        }
        // 评价状态
        orderLine.setEvaluationStatus(null);
        // 商品快照版本
        orderLine.setSnapshot(null);

        return sdkOrderLineDao.save(orderLine);
    }

    /**
     * 是否是不需要用户选择的礼品.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return true, if checks if is no need choice gift
     * @since 5.3.1
     */
    private boolean isNoNeedChoiceGift(ShoppingCartLineCommand shoppingCartLineCommand){
        return shoppingCartLineCommand.isGift() && GiftChoiceType.NoNeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType());
    }

}
