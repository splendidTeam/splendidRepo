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
package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.wormhole.constants.PromotionTypeConstants;
import com.baozun.nebula.wormhole.mq.entity.order.OrderLineV5;
import com.baozun.nebula.wormhole.mq.entity.order.OrderPromotionV5;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("orderLineV5ListBuilder")
public class DefaultOrderLineV5ListBuilder implements OrderLineV5ListBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderLineV5ListBuilder.class);

    @Autowired
    private OrderLineProductPackageV5Builder orderLineProductPackageV5Builder;

    /**
     * 上传图片的域名
     */
    @Value("#{meta['upload.img.domain.base']}")
    private String uploadImgDomain;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.wormhole.scm.makemsgcon.OrderLineV5ListBuilder#buildOrderLineV5List(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public List<OrderLineV5> buildOrderLineV5List(SalesOrderCommand salesOrderCommand){
        Validate.notNull(salesOrderCommand, "salesOrderCommand can't be null!");
        List<OrderLineCommand> orderLines = salesOrderCommand.getOrderLines();
        Validate.notEmpty(orderLines, "orderLines can't be null/empty!");

        List<OrderLineV5> orderLineV5List = new ArrayList<>();
        for (OrderLineCommand orderLineCommand : orderLines){
            orderLineV5List.add(toOrderLineV5(orderLineCommand, salesOrderCommand));
        }
        return orderLineV5List;
    }

    /**
     * @param orderLineCommand
     * @param salesOrderCommand
     * @return
     */
    protected OrderLineV5 toOrderLineV5(OrderLineCommand orderLineCommand,SalesOrderCommand salesOrderCommand){
        OrderLineV5 orderLineV5 = new OrderLineV5();
        orderLineV5.setBsOrderLineId(orderLineCommand.getId());
        orderLineV5.setExtentionCode(orderLineCommand.getExtentionCode());
        orderLineV5.setBsSkuName(orderLineCommand.getItemName());
        orderLineV5.setQty(orderLineCommand.getCount());
        //是否赠品 
        orderLineV5.setIsPrezzie(!Constants.ITEM_TYPE_SALE.equals(orderLineCommand.getType()));
        //保修时长(按月计)
        orderLineV5.setWarrantyMonths(null);

        //订单行包装信息
        orderLineV5.setProductPackages(orderLineProductPackageV5Builder.buildProductPackageV5List(orderLineCommand));
        //设置商品图片
        orderLineV5.setItemPic(uploadImgDomain + orderLineCommand.getItemPic());

        //吊牌价,参考数据，可以为空
        orderLineV5.setListPrice(orderLineCommand.getMSRP());
        //销售价(折前单价)
        orderLineV5.setUnitPrice(orderLineCommand.getSalePrice());
        //行总计(扣减所有活动优惠且未扣减积分抵扣)sum(sl.totalActual)=so.totalActualtotalActual+discountFee=unitPrice×qty最终货款=销售价X数量-折扣
        orderLineV5.setTotalActual(orderLineCommand.getSubtotal());

        //行优惠总金额(不包含积分抵扣)
        orderLineV5.setDiscountFee(getOrderLineDiscount(salesOrderCommand, orderLineCommand));

        //行优惠   
        orderLineV5.setPromotions(getOrderLinePromotion(salesOrderCommand, orderLineCommand));
        return orderLineV5;
    }

    private static BigDecimal getOrderLineDiscount(SalesOrderCommand salesOrderCommand,OrderLineCommand orderLine){
        BigDecimal orderLineDiscount = BigDecimal.ZERO;
        if (salesOrderCommand.getOrderPromotions() != null){
            for (OrderPromotionCommand orderPromotionCommand : salesOrderCommand.getOrderPromotions()){
                if (orderLine.getId().equals(orderPromotionCommand.getOrderLineId()) && !orderPromotionCommand.getBaseOrder()){
                    orderLineDiscount = orderLineDiscount.add(orderPromotionCommand.getDiscountAmount());
                }
            }
        }
        return orderLineDiscount;
    }

    private static List<OrderPromotionV5> getOrderLinePromotion(SalesOrderCommand salesOrderCommand,OrderLineCommand orderLine){
        List<OrderPromotionV5> promotions = new ArrayList<OrderPromotionV5>();
        if (salesOrderCommand.getOrderPromotions() != null){
            //主商品才有优惠
            if (orderLine.getType().equals(1)){
                for (OrderPromotionCommand orderPromotionCommand : salesOrderCommand.getOrderPromotions()){
                    //不是运费优惠
                    if (!orderPromotionCommand.getIsShipDiscount() && orderLine.getId().equals(orderPromotionCommand.getOrderLineId())){
                        OrderPromotionV5 orderPromotionV5 = new OrderPromotionV5();
                        //促销类型值域由商城前端预定义(商城共用一套),维护进omsChooseOption
                        orderPromotionV5.setType(orderPromotionCommand.getBaseOrder() ? PromotionTypeConstants.PROMOTION_ALLONLINE : PromotionTypeConstants.PROMOTION_LINE);
                        //促销编码该字段目前在oms中无需备案,oms中仅记录该信息商城定义的活动编号
                        orderPromotionV5.setCode(orderPromotionCommand.getActivityId().toString());
                        orderPromotionV5.setDescription(orderPromotionCommand.getDescribe());
                        orderPromotionV5.setDiscountFee(orderPromotionCommand.getDiscountAmount());
                        orderPromotionV5.setCouponCode(orderPromotionCommand.getCoupon());
                        orderPromotionV5.setRemark(null);
                        promotions.add(orderPromotionV5);
                    }
                }
            }
        }
        return promotions;
    }
}
