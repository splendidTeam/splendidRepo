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
import java.util.Date;
import java.util.List;

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
        salesOrder.setCode(orderCode);
        salesOrder.setCreateTime(new Date());
        salesOrder.setShopId(shopId);

        //*******************************************************************************
        //应付运费
        BigDecimal originShoppingFee = shopCartCommandByShop.getOriginShoppingFee();
        BigDecimal actualFreight = originShoppingFee.subtract(shopCartCommandByShop.getOffersShipping());
        // 总价 不含运费最终货款
        BigDecimal total = shopCartCommandByShop.getRealPayAmount().subtract(actualFreight);

        salesOrder.setTotal(total);
        salesOrder.setDiscount(shopCartCommandByShop.getOffersTotal());//设置 整单折扣 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额.
        salesOrder.setPayableFreight(originShoppingFee);
        salesOrder.setActualFreight(actualFreight);

        //*******************************************************************************

        salesOrder.setQuantity(shopCartCommandByShop.getQty());

        // 财务状态
        salesOrder.setFinancialStatus(SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT);
        // 物流状态
        salesOrder.setLogisticsStatus(SalesOrder.SALES_ORDER_STATUS_NEW);

        setRemark(shopId, salesOrderCommand, salesOrder);
        setLogisticsProviderInfo(shopId, salesOrderCommand, salesOrder);
        setLang(salesOrder);

        return sdkOrderDao.save(salesOrder);
    }

    /**
     * @param salesOrder
     */
    private void setLang(SalesOrder salesOrder){
        String lang = LangUtil.getCurrentLang();
        salesOrder.setLang(Validator.isNullOrEmpty(lang) ? LangUtil.ZH_CN : lang);
    }

    private void setLogisticsProviderInfo(Long shopId,SalesOrderCommand salesOrderCommand,SalesOrder salesOrder){
        // 快递
        List<String> logisticsProviders = salesOrderCommand.getLogisticsProvider();
        if (Validator.isNotNullOrEmpty(logisticsProviders)){
            for (String logisticsProvider : logisticsProviders){
                // String a = "shopid_code||value"
                String[] strs = logisticsProvider.split(SEPARATOR_FLAG);
                if (strs[0].equals(shopId.toString()) && strs.length == 3){
                    salesOrder.setLogisticsProviderCode(strs[1]);
                    salesOrder.setLogisticsProviderName(strs[2]);
                }
            }
        }
    }

    private void setRemark(Long shopId,SalesOrderCommand salesOrderCommand,SalesOrder salesOrder){
        // 设置买家留言 
        List<String> remarks = salesOrderCommand.getRemarks();
        if (Validator.isNotNullOrEmpty(remarks)){
            for (String remark : remarks){
                // String a = "shopid||value"
                String[] strs = remark.split(SEPARATOR_FLAG);
                if (strs[0].equals(shopId.toString()) && strs.length == 2){
                    salesOrder.setRemark(strs[1]);
                }
            }
        }
    }
}
