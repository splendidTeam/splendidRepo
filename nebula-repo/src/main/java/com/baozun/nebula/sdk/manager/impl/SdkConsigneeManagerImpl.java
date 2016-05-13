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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.salesorder.SdkConsigneeDao;
import com.baozun.nebula.model.salesorder.Consignee;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkConsigneeManager;
import com.feilong.core.Validator;

/**
 *
 * @author feilong
 * @version 5.3.1 2016年5月13日 下午3:04:49
 * @since 5.3.1
 */
@Transactional
@Service("sdkConsigneeManager")
public class SdkConsigneeManagerImpl implements SdkConsigneeManager{

    /** The Constant SEPARATOR_FLAG. */
    private static final String SEPARATOR_FLAG = "\\|\\|";

    /** The sdk consignee dao. */
    @Autowired
    private SdkConsigneeDao     sdkConsigneeDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkConsigneeManager#saveConsignee(java.lang.Long, java.lang.Long,
     * com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public void saveConsignee(Long orderId,Long shopId,SalesOrderCommand salesOrderCommand){
        Consignee consignee = new Consignee();
        ConvertUtils.convertFromTarget(consignee, salesOrderCommand);
        List<String> appointTimeQuantums = salesOrderCommand.getAppointTimeQuantums();
        // 设置指定时间段
        if (Validator.isNotNullOrEmpty(appointTimeQuantums)){
            for (String appointTimeQuantum : appointTimeQuantums){
                // String a = "shopid||value"
                String[] strs = appointTimeQuantum.split(SEPARATOR_FLAG);
                if (shopId.toString().equals(strs[0]) && strs.length == 2){
                    consignee.setAppointTimeQuantum(strs[1]);
                }
            }
        }
        // 设置指定日期
        List<String> appointTimes = salesOrderCommand.getAppointTimes();
        if (Validator.isNotNullOrEmpty(appointTimes)){
            for (String appointTime : appointTimes){
                // String a = "shopid||value"
                String[] strs = appointTime.split(SEPARATOR_FLAG);
                if (shopId.toString().equals(strs[0]) && strs.length == 2){
                    consignee.setAppointTime(strs[1]);
                }
            }
        }
        // 设置指定类型
        List<String> appointTypes = salesOrderCommand.getAppointTypes();
        if (Validator.isNotNullOrEmpty(appointTypes)){
            for (String appointType : appointTypes){
                // String a = "shopid||value"
                String[] strs = appointType.split(SEPARATOR_FLAG);
                if (shopId.toString().equals(strs[0]) && strs.length == 2){
                    consignee.setAppointType(strs[1]);
                }
            }
        }

        consignee.setOrderId(orderId);
        sdkConsigneeDao.save(consignee);
    }
}
