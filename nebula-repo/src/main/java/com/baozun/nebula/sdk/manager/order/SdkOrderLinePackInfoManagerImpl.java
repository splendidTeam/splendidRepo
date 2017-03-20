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

package com.baozun.nebula.sdk.manager.order;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.salesorder.SdkOrderLinePackageInfoDao;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderLinePackageInfoCommand;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;
import static com.feilong.core.util.CollectionsUtil.group;

/**
 * 封装包装信息.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("sdkOrderLinePackInfoManager")
public class SdkOrderLinePackInfoManagerImpl implements SdkOrderLinePackInfoManager{

    @Autowired
    private SdkOrderLinePackageInfoDao sdkOrderLinePackageInfoDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.SdkOrderLineManager#packOrderLinesPackageInfo(java.util.List)
     */
    @Override
    public <T extends OrderLineCommand> List<T> packOrderLinesPackageInfo(List<T> orderLineCommandList){
        if (isNullOrEmpty(orderLineCommandList)){
            return Collections.emptyList();
        }

        //----------------

        List<Long> orderLineIdList = getPropertyValueList(orderLineCommandList, "id");
        List<OrderLinePackageInfoCommand> orderLinePackageInfoCommandList = sdkOrderLinePackageInfoDao.findOrderLinePackageInfoCommandList(orderLineIdList);
        if (isNullOrEmpty(orderLinePackageInfoCommandList)){
            return orderLineCommandList;
        }

        //----------------

        //按照行分个组
        Map<Long, List<OrderLinePackageInfoCommand>> orderLineIdAndOrderLinePackageInfoCommandListMap = group(orderLinePackageInfoCommandList, "orderLineId");
        for (T orderLineCommand : orderLineCommandList){
            List<OrderLinePackageInfoCommand> list = orderLineIdAndOrderLinePackageInfoCommandListMap.get(orderLineCommand.getId());

            if (isNotNullOrEmpty(list)){
                orderLineCommand.setOrderLinePackageInfoCommandList(list);
            }
        }
        return orderLineCommandList;
    }

}
