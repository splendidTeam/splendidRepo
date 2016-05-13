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

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.salesorder.OrderCodeCreatorManager;
import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkOrderManager;
import com.baozun.nebula.utilities.common.LangUtil;
import com.feilong.core.Validator;

/**
 *
 * @author feilong
 * @version 5.3.1 2016年5月13日 下午2:39:01
 * @since 5.3.1
 */
@Transactional
@Service("sdkOrderManager")
public class SdkOrderManagerImpl implements SdkOrderManager{

    /** The sdk order dao. */
    @Autowired
    private SdkOrderDao             sdkOrderDao;

    /** The order code creator. */
    @Autowired(required = false)
    private OrderCodeCreatorManager orderCodeCreatorManager;

    /** The Constant SEPARATOR_FLAG. */
    private static final String     SEPARATOR_FLAG = "\\|\\|";

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderManager#savaOrder(java.lang.Long, com.baozun.nebula.sdk.command.SalesOrderCommand,
     * com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop)
     */
    @Override
    public SalesOrder savaOrder(Long shopId,SalesOrderCommand salesOrderCommand,ShopCartCommandByShop shopCartCommandByShop){
        SalesOrder salesOrder = new SalesOrder();
        ConvertUtils.convertTwoObject(salesOrder, salesOrderCommand);
        // 生成订单号
        String orderCode = orderCodeCreatorManager.createOrderCodeBySource(salesOrderCommand.getSource());
        if (orderCode == null){
            throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
        }

        String lang = LangUtil.getCurrentLang();
        salesOrder.setLang(Validator.isNullOrEmpty(lang) ? LangUtil.ZH_CN : lang);

        BigDecimal actualFreight = shopCartCommandByShop.getOriginShoppingFee().subtract(shopCartCommandByShop.getOffersShipping());
        // 总价 不含运费最终货款
        BigDecimal total = shopCartCommandByShop.getRealPayAmount().subtract(actualFreight);
        // 财务状态
        salesOrder.setFinancialStatus(SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT);
        // 物流状态
        salesOrder.setLogisticsStatus(SalesOrder.SALES_ORDER_STATUS_NEW);
        salesOrder.setCode(orderCode);
        salesOrder.setCreateTime(new Date());
        salesOrder.setShopId(shopId);
        salesOrder.setQuantity(shopCartCommandByShop.getQty());
        salesOrder.setTotal(total);
        salesOrder.setDiscount(shopCartCommandByShop.getOffersTotal());
        salesOrder.setPayableFreight(shopCartCommandByShop.getOriginShoppingFee());
        salesOrder.setActualFreight(actualFreight);
        // 设置买家留言 格式一定要shopId_value 不留言就是shopid_
        if (Validator.isNotNullOrEmpty(salesOrderCommand.getRemarks())){
            for (String remark : salesOrderCommand.getRemarks()){
                // String a = "shopid||value"
                String[] strs = remark.split(SEPARATOR_FLAG);
                if (strs[0].equals(shopId.toString()) && strs.length == 2){
                    salesOrder.setRemark(strs[1]);
                }
            }
        }
        // 快递
        if (Validator.isNotNullOrEmpty(salesOrderCommand.getLogisticsProvider())){
            for (String logisticsProvider : salesOrderCommand.getLogisticsProvider()){
                // String a = "shopid_code||value"
                String[] strs = logisticsProvider.split(SEPARATOR_FLAG);
                if (strs[0].equals(shopId.toString()) && strs.length == 3){
                    salesOrder.setLogisticsProviderCode(strs[1]);
                    salesOrder.setLogisticsProviderName(strs[2]);
                }
            }
        }

        return sdkOrderDao.save(salesOrder);
    }
}
