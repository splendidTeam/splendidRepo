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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.wormhole.mq.entity.order.DeliveryInfoV5;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("deliveryInfoV5Builder")
public class DefaultDeliveryInfoV5Builder implements DeliveryInfoV5Builder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDeliveryInfoV5Builder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.wormhole.scm.makemsgcon.DeliveryInfoV5Builder#buildDeliveryInfoV5(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public DeliveryInfoV5 buildDeliveryInfoV5(SalesOrderCommand salesOrderCommand){
        DeliveryInfoV5 deliveryInfoV5 = new DeliveryInfoV5();

        //设置行政区划省市区等信息
        packAdministrativeDivision(deliveryInfoV5, salesOrderCommand);

        deliveryInfoV5.setAddress(salesOrderCommand.getAddress());
        deliveryInfoV5.setZipCode(salesOrderCommand.getPostcode());
        deliveryInfoV5.setReceiver(salesOrderCommand.getName());
        deliveryInfoV5.setReceiverMobile(salesOrderCommand.getMobile());
        deliveryInfoV5.setReceiverPhone(salesOrderCommand.getTel());
        deliveryInfoV5.setAppointShipDate(salesOrderCommand.getAppointShipDate());

        //派件时间类型（快递附加服务）【logisticsServiceType】：1：普通5：当日6：次日 7 次晨达
        deliveryInfoV5.setLogisticsServiceType(salesOrderCommand.getAppointType());
        deliveryInfoV5.setRemark(null);
        return deliveryInfoV5;
    }

    /**
     * 设置行政区划省市区等信息
     * 
     * @param deliveryInfoV5
     * @param salesOrderCommand
     */
    protected void packAdministrativeDivision(DeliveryInfoV5 deliveryInfoV5,SalesOrderCommand salesOrderCommand){
        deliveryInfoV5.setCountry(null);

        //*******************************************************
        //
        // 原先阿汤哥计划是 db 订单里面只存放地区 id,然后再使用多语言转换 , 
        // 经过分析,实现的时候, db 订单地址里面已经有地区的 name (基于多语言的)
        // so 其实可以直接传递,不需要再次转换
        //
        //*******************************************************

        deliveryInfoV5.setProvince(salesOrderCommand.getProvince());
        deliveryInfoV5.setCity(salesOrderCommand.getCity());
        deliveryInfoV5.setDistrict(salesOrderCommand.getArea());

        //*********************************************************

        deliveryInfoV5.setTown(null);
    }
}
