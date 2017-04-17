/**
 * 
 */
package com.baozun.nebula.web.controller.order.viewcommand;

import java.util.List;

import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 用于记录对应订单行已完成的退货数
 * @author 黄金辉
 *
 */
public class ReturnLinesViewCommand extends BaseViewCommand{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3916664551899563958L;

	private List<ReturnLineCommand> lineList;
	

	public List<ReturnLineCommand> getLineList() {
		return lineList;
	}
	public void setLineList(List<ReturnLineCommand> lineList) {
		this.lineList = lineList;
	}
}
