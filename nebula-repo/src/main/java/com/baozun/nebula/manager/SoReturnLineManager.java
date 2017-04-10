package com.baozun.nebula.manager;

import java.util.List;

import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.model.salesorder.SoReturnLine;


/**
 * 退换货申请单行管理接口
 *
 */
public interface SoReturnLineManager  {
	

	
	//保存退货订单行
	public void saveReturnLine(List<SoReturnLine> soReturnLine);
	
	/**
	 * 通过退货单编号查询退货订单行
	 * @param returnOrderCode
	 * @return
	 */
	public List<ReturnLineCommand> findSoReturnLinesByReturnOrderId(long id);
	
}