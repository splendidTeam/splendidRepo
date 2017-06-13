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

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;

/**
 * @author - 项硕
 */
public interface OrderLineManager extends BaseManager {

	public OrderLine findByPk(Long id);

    public List<SimpleOrderLineSubViewCommand> findByOrderID(Long orderId); 
}
