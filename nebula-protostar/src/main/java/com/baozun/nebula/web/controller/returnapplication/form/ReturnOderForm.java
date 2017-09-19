/**
 * 
 */
package com.baozun.nebula.web.controller.returnapplication.form;



/**
 * 创建订单提交的form.
 * 
 * 
 * <h3>表单提交需要以下信息:</h3>
 * <blockquote>
 * 
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>退货单行id</td>
 * <td>一次退货可能会涉及到多个订单行，这个值可能为一个数组</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>退货单行数量</td>
 * <td>每个退货单行的退货数量不同，这个值可能为一个数组</td>
 * </tr>
 * <tr valign="top" >
 * <td>退货原因</td>
 * <td>每个退货单行一个退货原因，可能为一个数组</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>退款银行名称</td>
 * <td>例如：中国银行、上海银行</td>
 * </tr>
 * <tr valign="top" >
 * <td>银行分行</td>
 * <td>例如：中国银行大场分行</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>账号</td>
 * <td>银行卡号</td>
 *  <tr valign="top" style="background-color:#eeeeff">
 * <td>银行卡用户名</td>
 * <td>银行卡持有者姓名</td>
 *  <tr valign="top" style="background-color:#eeeeff">
 * <td>订单id</td>
 * <td></td>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>订单code</td>
 * <td></td>
 *  <tr valign="top" style="background-color:#eeeeff">
 * <td>备注</td>
 * <td></td>
 * </tr>
 * </table>
 * </blockquote>
 *
 * @author jinhui.huang
 * @version
 *
 */
public class ReturnOderForm{
	
	/**
	 * 
	 */
	private String lineIdSelected;
	private String sumSelected;
	/**退货单行退货理由*/
	private String reasonSelected;
	private String bank;
	private String branch;
	private String account;
	private String userName;
	private String orderId;
	private String orderCode;
	private String memo;
	/** 退货单退货原因*/
	private String RetrunReason;
	


	/**
	 * @return the lineIdSelected
	 */
	public String getLineIdSelected() {
		return lineIdSelected;
	}



	/**
	 * @param lineIdSelected the lineIdSelected to set
	 */
	public void setLineIdSelected(String lineIdSelected) {
		this.lineIdSelected = lineIdSelected;
	}



	/**
	 * @return the sumSelected
	 */
	public String getSumSelected() {
		return sumSelected;
	}



	/**
	 * @param sumSelected the sumSelected to set
	 */
	public void setSumSelected(String sumSelected) {
		this.sumSelected = sumSelected;
	}



	/**
	 * @return the reasonSelected
	 */
	public String getReasonSelected() {
		return reasonSelected;
	}



	/**
	 * @param reasonSelected the reasonSelected to set
	 */
	public void setReasonSelected(String reasonSelected) {
		this.reasonSelected = reasonSelected;
	}



	/**
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}



	/**
	 * @param bank the bank to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}



	/**
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}



	/**
	 * @param branch the branch to set
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}



	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}



	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}



	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}



	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}



	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}



	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}



	/**
	 * @param orderCode the orderCode to set
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}



	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}



	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}


	/**
	 * 
	 */
	public ReturnOderForm() {
		super();
	}



	public String getRetrunReason() {
		return RetrunReason;
	}



	public void setRetrunReason(String retrunReason) {
		RetrunReason = retrunReason;
	}
	
}
