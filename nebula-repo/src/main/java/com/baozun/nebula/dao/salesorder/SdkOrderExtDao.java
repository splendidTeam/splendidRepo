/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.dao.salesorder;

import com.baozun.nebula.model.salesorder.SalesOrderExt;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

/**
 * SdkOrderExtDao 订单拓展Dao
 * 
 * @author: pengfei.fang
 * @date: 2016年02月02日
 * @deprecated 没有使用 since 5.3.2.22
 **/
@Deprecated
public interface SdkOrderExtDao extends GenericEntityDao<SalesOrderExt, Long>{

	/**
	 * 根据订单code获取订单扩展
	 * 
	 * @param code
	 *            订单code
	 * @return
	 */
	@NativeQuery(model = SalesOrderExt.class)
	SalesOrderExt findSalesOrderByCode(@QueryParam("code") String code);

}
