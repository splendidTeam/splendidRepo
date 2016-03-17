package com.baozun.nebula.command;

import java.util.Date;

public class PayLogCommand  implements Command{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8629650059435724117L;

	/** PK. */
	private Long				id;
	
	/**支付日志创建时间**/
	private Date createTime;
	
	/** 支付日志信息 **/
	private String message;
	
	/**支付的用户**/
	private String operator;
	
	/**调用支付操作时的返回值**/
	private String returnVal;

	public PayLogCommand(Date createTime, String message, String operator,
			String returnVal) {
		super();
		this.createTime = createTime;
		this.message = message;
		this.operator = operator;
		this.returnVal = returnVal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getReturnVal() {
		return returnVal;
	}

	public void setReturnVal(String returnVal) {
		this.returnVal = returnVal;
	}
}
