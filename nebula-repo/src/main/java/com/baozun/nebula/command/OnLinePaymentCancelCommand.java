package com.baozun.nebula.command;

import java.io.Serializable;

public class OnLinePaymentCancelCommand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2164738371020384282L;

	/**支付类型**/
	private Integer payType;
	
	/**第三方支付流水号**/
	private String trade_no;
	
	/** 取消订单角色  */
	private String trade_role;


	public OnLinePaymentCancelCommand(Integer payType,String trade_no,String trade_role){
		this.payType = payType;
		this.trade_no = trade_no;
		this.trade_role = trade_role;
	}
	
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTrade_role() {
		return trade_role;
	}

	public void setTrade_role(String trade_role) {
		this.trade_role = trade_role;
	}

}
