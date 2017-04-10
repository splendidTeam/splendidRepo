package com.baozun.nebula.dao.salesorder;




import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.model.salesorder.SoReturnLine;


/**
 * 订单退换货处理接口
 * 退换货订单行
 */
public interface SoReturnLineDao extends GenericEntityDao<SoReturnLine, Long> {
	
	/**
	 * 通过退货单编号查询退货订单行
	 * @param returnOrderCode
	 * @return
	 */
	@NativeQuery(model = ReturnLineCommand.class,value = "SoReturnLine.findSoReturnLinesByReturnOrderId")
	public List<ReturnLineCommand> findSoReturnLinesByReturnOrderId(@QueryParam("returnOrderId")long returnOrderId);


}
