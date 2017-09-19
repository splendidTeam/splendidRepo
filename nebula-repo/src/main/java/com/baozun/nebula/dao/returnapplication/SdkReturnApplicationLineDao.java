package com.baozun.nebula.dao.returnapplication;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.model.returnapplication.ReturnApplicationLine;


/**
 * 订单退换货处理接口
 * 退换货订单行
 */
public interface SdkReturnApplicationLineDao extends GenericEntityDao<ReturnApplicationLine, Long> {
	
	/**
	 * 通过退货单编号查询退货订单行
	 * @param returnOrderCode
	 * @return
	 */
	@NativeQuery(model = ReturnLineCommand.class,value = "SdkReturnApplicationLineDao.findSoReturnLinesByReturnOrderIds")
	public List<ReturnLineCommand> findSoReturnLinesByReturnOrderIds(@QueryParam("returnOrderIds")List<Long> returnOrderIds);
	
	/**
	 * 按退换货行ID集合查询
	 * 
	 * @param returnLinesIds
	 * @return
	 */
	@NativeQuery(model = ReturnLineCommand.class, value = "SdkReturnApplicationLineDao.findReturnLinesByIds")
	public List<ReturnLineCommand> findReturnLinesByIds(@QueryParam("returnLinesIds")List<Long> returnLinesIds);


}
