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

package com.baozun.nebula.manager.salesorder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkOrderLineManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;
import com.feilong.core.bean.BeanUtil;

/**
 * @author - 项硕
 */
@Transactional
@Service("OrderLineManager")
public class OrderLineManagerImpl implements OrderLineManager {
	
	@Autowired
	private SdkOrderLineManager lineManager;
	
	@Autowired
        private SdkSkuManager sdkSkuManager;

	@Override
	public OrderLine findByPk(Long id) {
		return lineManager.findByPk(id);
	}

    @Override
    public List<SimpleOrderLineSubViewCommand> findByOrderID(Long orderId) {
        List<OrderLineCommand> findOrderLinesByOrderId = lineManager.findOrderLinesByOrderId(orderId);
        List<SimpleOrderLineSubViewCommand> list=new ArrayList<SimpleOrderLineSubViewCommand>();
        for (OrderLineCommand orderLineCommand : findOrderLinesByOrderId) {
            String properties = orderLineCommand.getSaleProperty();
            List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
            orderLineCommand.setSkuPropertys(propList);
             SimpleOrderLineSubViewCommand slsv=new SimpleOrderLineSubViewCommand();
             BeanUtil.copyProperties(slsv, orderLineCommand);
             list.add(slsv);
        }
        return list;
    }

}
