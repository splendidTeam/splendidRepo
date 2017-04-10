/**
 * 
 */
package com.baozun.nebula.web.controller.order.viewcommand;

import java.util.List;

import com.baozun.nebula.command.ReturnLineCommand;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 用于记录对应订单行已完成的退货数
 * @author 黄金辉
 *
 */
public class ReturnApplicationViewCommand extends BaseViewCommand{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3916664551899563958L;


	private SoReturnApplication app;
	private List<ReturnLineCommand> lineList;
	private Integer returnCount;
	private Boolean isCod;
	private List<OrderLineCommand>      orderLines;
	public SoReturnApplication getApp() {
		return app;
	}
	public void setApp(SoReturnApplication app) {
		this.app = app;
	}
	public List<ReturnLineCommand> getLineList() {
		return lineList;
	}
	public void setLineList(List<ReturnLineCommand> lineList) {
		this.lineList = lineList;
	}
	public Integer getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}
	public Boolean getIsCod() {
		return isCod;
	}
	public void setIsCod(Boolean isCod) {
		this.isCod = isCod;
	}
	public List<OrderLineCommand> getOrderLines() {
		return orderLines;
	}
	public void setOrderLines(List<OrderLineCommand> orderLines) {
		this.orderLines = orderLines;
	}
}
