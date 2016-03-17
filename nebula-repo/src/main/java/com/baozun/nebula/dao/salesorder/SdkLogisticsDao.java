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
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.salesorder.Logistics;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;


public interface SdkLogisticsDao extends GenericEntityDao<Logistics, Long> {

	@NativeQuery(model = LogisticsCommand.class)
	public LogisticsCommand findLogisticsByOrderId(@QueryParam("orderId") Long orderId);
	
	@NativeQuery(model = LogisticsCommand.class)
    public List<LogisticsCommand> findLogisticsListByQueryMap(@QueryParam("queryMap") Map<String,Object> queryMap);
}
