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
package com.baozun.nebula.wormhole.constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.baozun.nebula.model.salesorder.SalesOrder;

/**
 * @author yfxie
 * 商城和oms之间订单推送的状态
 *
 */
public class OrderStatusV5Constants {
	
	//----订单状态
	/**订单创建成功*/
	public static final Integer ORDER_NEW = 1;
	/**订单取消*/
	public static final Integer ORDER_CANCEL = 2;
	/**过单到仓库*/
	public static final Integer ORDER_CONFIRMED = 3;
	/**销售出库*/
	public static final Integer ORDER_DELIVERIED = 5;
	/**交易完成*/
	public static final Integer ORDER_FINISHED = 10;
	//----退货状态
	/**退货已入库*/
	public static final Integer FEEDBACK_CONFIRM = 7;
	/**退换货申请取消*/
	public static final Integer FEEDBACK_CANCEL_NEW = 21;
	/**退换货申请取消成功*/
	public static final Integer FEEDBACK_CANCEL_AGREE = 22;
	/**退换货申请取消失败*/
	public static final Integer FEEDBACK_CANCEL_REFUSED = 23;
	
	public static final Map<Integer,Set<Integer>> ORDER_STATUS_BEFORE_MAP = new HashMap<Integer,Set<Integer>>();
	static {
		/**oms订单创建成功 对应商城之前的订单状态*/
		Set<Integer> ORDER_NEW_SET = new HashSet<Integer>();
		ORDER_NEW_SET.add(SalesOrder.SALES_ORDER_STATUS_NEW);
		ORDER_STATUS_BEFORE_MAP.put(ORDER_NEW, ORDER_NEW_SET);
		
		/**oms订单取消 对应商城之前的订单状态*/
		Set<Integer> ORDER_CANCEL_SET = new HashSet<Integer>();
		ORDER_CANCEL_SET.add(SalesOrder.SALES_ORDER_STATUS_CONFIRMED);
		ORDER_CANCEL_SET.add(SalesOrder.SALES_ORDER_STATUS_TOOMS);
		ORDER_CANCEL_SET.add(SalesOrder.SALES_ORDER_STATUS_WH_HANDLING);
		ORDER_CANCEL_SET.add(SalesOrder.SALES_ORDER_STATUS_DELIVERIED);
		//ORDER_CANCEL_SET.add(SalesOrder.SALES_ORDER_STATUS_FINISHED);
		ORDER_STATUS_BEFORE_MAP.put(ORDER_CANCEL, ORDER_CANCEL_SET);
		
		/**oms过单到仓库  对应商城之前的订单状态*/
		Set<Integer> ORDER_CONFIRMED_SET = new HashSet<Integer>();
		ORDER_CONFIRMED_SET.add(SalesOrder.SALES_ORDER_STATUS_NEW);
		ORDER_CONFIRMED_SET.add(SalesOrder.SALES_ORDER_STATUS_TOOMS);
		ORDER_CONFIRMED_SET.add(SalesOrder.SALES_ORDER_STATUS_CONFIRMED);
		ORDER_STATUS_BEFORE_MAP.put(ORDER_CONFIRMED, ORDER_CONFIRMED_SET);
		
		/**oms销售出库 对应商城之前的订单状态*/
		Set<Integer> ORDER_DELIVERIED_SET = new HashSet<Integer>();
		ORDER_DELIVERIED_SET.add(SalesOrder.SALES_ORDER_STATUS_TOOMS);
		ORDER_DELIVERIED_SET.add(SalesOrder.SALES_ORDER_STATUS_CONFIRMED);
		ORDER_DELIVERIED_SET.add(SalesOrder.SALES_ORDER_STATUS_WH_HANDLING);
		ORDER_STATUS_BEFORE_MAP.put(ORDER_DELIVERIED, ORDER_DELIVERIED_SET);
		
		/**oms交易完成  对应商城之前的订单状态*/
		Set<Integer> ORDER_FINISHED_SET = new HashSet<Integer>();
		ORDER_FINISHED_SET.add(SalesOrder.SALES_ORDER_STATUS_DELIVERIED);
		ORDER_STATUS_BEFORE_MAP.put(ORDER_FINISHED, ORDER_FINISHED_SET);
	}

}
