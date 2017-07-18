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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;

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
    
    /**
     * @author yaohua.wang@baozun.cn 
     * @since Nebula 5.3.2.20
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderLineCommand> findOrderLinesByLineIds(List<Long> lineIds){
        List<OrderLineCommand> orderLineCommands = sdkOrderLineDao.findOrderDetailListByIds(lineIds);
        return orderLineCommands;
    }

}
