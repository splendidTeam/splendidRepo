package com.baozun.nebula.sdk.manager.returnapplication;

import java.util.List;

import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.model.returnapplication.ReturnApplicationLine;


/**
 * 退换货申请单行管理接口
 *
 */
public interface SdkReturnApplicationLineManager  {
	
    /**
     * 保存退货订单行
     * 
     * @param soReturnLine
     * @return
     */
	public List<ReturnApplicationLine> saveReturnLine(List<ReturnApplicationLine> soReturnLine);
	
	/**
	 * 通过退货单编号查询退货订单行
	 * @param returnOrderCode
	 * @return
	 */
	public List<ReturnLineCommand> findSoReturnLinesByReturnOrderIds(List<Long> returnOrderIds);

	/**
	 * 按退换货行ID集合查询
	 * 
	 * @param returnLinesIds
	 * @return
	 */
	public List<ReturnLineCommand> findReturnLinesByIds(List<Long> returnLinesIds);
	
}