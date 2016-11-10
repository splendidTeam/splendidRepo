/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baozun.nebula.wormhole.scm.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.BeforePaymentCancelOrderCommand;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.wormhole.scm.handler.SalesOrderCancelHandler;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;
import com.feilong.tools.jsonlib.JsonUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

import loxia.dao.Sort;

/**
 * 执行定时任务实现：取消订单.
 * 
 * <h3>业务逻辑:</h3> <blockquote>
 * <ol>
 * <li>批量获取需要自动取消的订单 {@link #loadToBeCancelSalesOrderCommandList()}</li>
 * <li>for循环,执行取消每笔订单
 * {@link LevisPayManager#cancelOrder(BeforePaymentCancelOrderCommand)}</li>
 * </ol>
 * </blockquote>
 * 
 * <h3>需要关心的细节:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>在取消每笔订单的时候,如果出现异常,不能影响其他订单的处理</li>
 * </ol>
 * </blockquote>
 * 
 * @author  <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-10
 * @since 5.0.0
 */
@Service("salesOrderCancelTaskManager")
// @Transactional 注意该类不要有事务, 事务由没有调用的执行类来控制, 这样可以做到 当一个取消失败了, 其他还可以正常执行
public class SalesOrderCancelTaskManagerImpl implements SalesOrderCancelTaskManager {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderCancelTaskManagerImpl.class);

	/** 配置的订单超时时间订单过期时间,单位分钟. */
	private final static String PAY_EXPIRY_TIME = "payExpiryTime";

	/** The sdk mata info manager. */
	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;

	/** The esprit sales order dao. */
	@Autowired
	private OrderManager orderManager;

	/** The order cancel manager. */
	@Autowired
	private PayManager payManager;
	
	@Autowired(required = false)
    private SalesOrderCancelHandler salesOrderCancelHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baozun.store.manager.order.StoreCancelOrderTaskManager#cancelOrders()
	 */
	@Override
	public void cancelOrders() {
		// 第一步,加载待取消的订单列表.
		List<SalesOrderCommand> salesOrderCommandList = new ArrayList<SalesOrderCommand>();
		salesOrderCommandList.addAll(loadToBeCancelSalesOrderCommandList());

		if (Validator.isNullOrEmpty(salesOrderCommandList)) {
			LOGGER.info("salesOrderCommandList is null or empty~~,maybe no order need to be cancel");
			return;
		}

		LOGGER.info("toBeCancelOrder list size:{}",salesOrderCommandList.size());

		// 第二步,循环取消待取消的订单
		for (SalesOrderCommand salesOrderCommand : salesOrderCommandList) {
			BeforePaymentCancelOrderCommand beforePaymentCancelOrderCommand = BeforePaymentCancelOrderCommand
					.buildForCancelOrderTask(salesOrderCommand);
			try {
				payManager.cancelOrder(beforePaymentCancelOrderCommand);
			} catch (Exception e) {
				// 处理异常,如果有异常捕获,下笔订单可以正常执行
				String message = Slf4jUtil.format("cancelOrder exception,beforePaymentCancelOrderCommand:[{}]",JsonUtil.format(beforePaymentCancelOrderCommand));
				LOGGER.error(message, e);
			}
		}
		
        if (null != salesOrderCancelHandler){
        	salesOrderCancelHandler.cancelSalesOrder(salesOrderCommandList);
        }
	}

	/**
	 * 加载待取消的订单列表.
	 * 
	 * @return the list< sales order command>
	 * @since 5.0.0
	 */
	private List<SalesOrderCommand> loadToBeCancelSalesOrderCommandList() {
		// 取超时时间
		String payExpiryTime = sdkMataInfoManager.findValue(PAY_EXPIRY_TIME);

		if (Validator.isNullOrEmpty(payExpiryTime)) {
			throw new IllegalArgumentException(Slf4jUtil.format(
					"**系统取消订单,系统参数配置:[{}],未设置！！！！！", PAY_EXPIRY_TIME));
		}

		// ********************************************************************************

		Sort[] sorts = Sort.parse("o.id asc");

		Map<String, Object> searchParam = new HashMap<String, Object>();
		searchParam.put("endDate", DateUtil.addMinute(new Date(),
				-Integer.parseInt(payExpiryTime)));// 分钟之前

		// ********************************************************************************
		// 查询待取消的未付款订单
		return orderManager.findToBeCancelOrders(sorts, searchParam);
	}
}
