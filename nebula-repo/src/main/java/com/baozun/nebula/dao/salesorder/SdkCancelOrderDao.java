package com.baozun.nebula.dao.salesorder;

import java.util.Date;
import java.util.Map;
import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.salesorder.CancelOrderApp;
import com.baozun.nebula.sdk.command.CancelOrderCommand;

public interface SdkCancelOrderDao  extends GenericEntityDao<CancelOrderApp, Long>{


	/**
	 * 分页查询申请取消订单
	 * @param page
	 * @param sorts
	 * @param memberId
	 * @return
	 */
	@NativeQuery(model = CancelOrderCommand.class)
	Pagination<CancelOrderCommand> findCancelOrdersByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> searchParam);
	
	/**
	 * 更改取消订单申请状态
	 * @param orderId
	 * @param status
	 * @return
	 */
	@NativeUpdate 
	public Integer updateCancelOrders(@QueryParam("handleId")Long handleId,@QueryParam("code")String code,@QueryParam("state")Integer state,@QueryParam("feedback")String feedback,@QueryParam("modifyTime")Date modifyTime);
	
	
	/**
	 * 根据code查询该订单是否已经申请了取消订单
	 * @param code
	 * @return
	 */
	@NativeQuery(model = CancelOrderCommand.class)
	CancelOrderCommand findCancelOrderAppByCode(@QueryParam("code") String code);
}
