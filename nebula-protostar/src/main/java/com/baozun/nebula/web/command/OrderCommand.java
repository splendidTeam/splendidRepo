package com.baozun.nebula.web.command;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.salesorder.OrderLog;
import com.baozun.nebula.model.salesorder.OrderStatusLog;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * 用于封装订单明细页面的详细信息
 * @author Qiang.yang *
 * @createtime 2013-11-26
 *
 */
public class OrderCommand   implements Command {

	private static final long serialVersionUID = -4906127289814092553L;
	/**订单信息*/
	private SalesOrderCommand salesOrderCommand;
			
	
	/**订单历史记录*/
	private List<OrderLog> orderLogs;
	
	/**订单状态变更记录*/
	private List<OrderStatusLog> orderStatusLogs;
	
	/**订单物流状态信息*/
	private String logisticsInfo;
	
	/**财务状态信息*/
	private String financialStatusInfo;
	
	/**订单来源*/
	private String orderSource;
	/**发票类型*/
	private String receiptType;
	
	/**支付方式*/
	private Map<Integer,String> payTypeMap;
	
	/**行类型*/
	private Map<Integer,String> orderLineTypeMap;
	
	/**评价信息*/
	private Map<Integer,String> orderLineEvaluationMap;
	/**会员名称*/
	private String memberName;
	
	/**状态Map*/
	private Map<Integer,String> statusMap;
	
	/**指定收货*/
	private String appointReceive;
	/**促销类型*/
	private Map<Integer,String> promotionTypeMap;
	

	


	public SalesOrderCommand getSalesOrderCommand() {
		return salesOrderCommand;
	}

	public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand) {
		this.salesOrderCommand = salesOrderCommand;
	}

	public List<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(List<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	public List<OrderStatusLog> getOrderStatusLogs() {
		return orderStatusLogs;
	}

	public void setOrderStatusLogs(List<OrderStatusLog> orderStatusLogs) {
		this.orderStatusLogs = orderStatusLogs;
	}


	public String getLogisticsInfo() {
		return logisticsInfo;
	}

	public void setLogisticsInfo(String logisticsInfo) {
		this.logisticsInfo = logisticsInfo;
	}

	public String getFinancialStatusInfo() {
		return financialStatusInfo;
	}

	public void setFinancialStatusInfo(String financialStatusInfo) {
		this.financialStatusInfo = financialStatusInfo;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	public Map<Integer, String> getPayTypeMap() {
		return payTypeMap;
	}

	public void setPayTypeMap(Map<Integer, String> payTypeMap) {
		this.payTypeMap = payTypeMap;
	}

	public Map<Integer, String> getOrderLineTypeMap() {
		return orderLineTypeMap;
	}

	public void setOrderLineTypeMap(Map<Integer, String> orderLineTypeMap) {
		this.orderLineTypeMap = orderLineTypeMap;
	}

	public Map<Integer, String> getOrderLineEvaluationMap() {
		return orderLineEvaluationMap;
	}

	public void setOrderLineEvaluationMap(
			Map<Integer, String> orderLineEvaluationMap) {
		this.orderLineEvaluationMap = orderLineEvaluationMap;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Map<Integer, String> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<Integer, String> statusMap) {
		this.statusMap = statusMap;
	}

	public String getAppointReceive() {
		return appointReceive;
	}

	public void setAppointReceive(String appointReceive) {
		this.appointReceive = appointReceive;
	}

	public Map<Integer, String> getPromotionTypeMap() {
		return promotionTypeMap;
	}

	public void setPromotionTypeMap(Map<Integer, String> promotionTypeMap) {
		this.promotionTypeMap = promotionTypeMap;
	}
	
	
	
	
}
