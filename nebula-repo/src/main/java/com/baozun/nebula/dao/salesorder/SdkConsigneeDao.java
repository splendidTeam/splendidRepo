/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.dao.salesorder;

import java.util.List;
import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.salesorder.Consignee;
import com.baozun.nebula.sdk.command.ConsigneeCommand;

/**
 * 收货地址Dao
 * @author Tianlong.Zhang
 * 
 */
public interface SdkConsigneeDao extends GenericEntityDao<Consignee, Long> {
	@NativeQuery(model = ConsigneeCommand.class)
	ConsigneeCommand findConsigneeOrderId(@QueryParam("orderId") Long orderId);
	
	@NativeQuery(model = ConsigneeCommand.class)
	List<ConsigneeCommand> findConsigneesByOrderIds(@QueryParam("orderIds") List<Long> orderIds);
}
