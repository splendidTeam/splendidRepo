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

import com.baozun.nebula.model.salesorder.ReturnOrderApp;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;

public interface SdkReturnOrderDao  extends GenericEntityDao<ReturnOrderApp, Long>{
	
	@NativeQuery(model = ReturnOrderCommand.class)
	public Pagination<ReturnOrderCommand> findReturnOrdersByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> searchParam);
	
	@NativeUpdate 
	public Integer updateReturnOrders(@QueryParam("handleId")Long handleId,@QueryParam("code")String code,@QueryParam("state")Integer state,@QueryParam("feedback")String feedback,@QueryParam("modifyTime")Date modifyTime);

	@NativeQuery(model = ReturnOrderCommand.class)
	public ReturnOrderCommand findReturnOrderAppByCode(@QueryParam("orderCode") String orderCode);
	
}
