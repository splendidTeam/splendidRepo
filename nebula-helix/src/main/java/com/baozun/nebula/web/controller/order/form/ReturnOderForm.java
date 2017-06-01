/**
 * 
 */
package com.baozun.nebula.web.controller.order.form;

import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.web.controller.BaseForm;

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
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>类型</td>
 * <td>1：退货2：换货</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>国家</td>
 * <td></td>
 * </tr>
 *  <tr valign="top" style="background-color:#eeeeff">
 * <td>省份</td>
 * <td></td>
 * </tr>
 *  <tr valign="top" style="background-color:#eeeeff">
 * <td>市</td>
 * <td></td>
 * </tr>
 *  <tr valign="top" style="background-color:#eeeeff">
 * <td>区</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>镇</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>详细地址</td>
 * <td></td>
 * </tr>
 *  <tr valign="top" style="background-color:#eeeeff">
 * <td>邮编</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>收货人</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>收件人号码</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>收件人手机</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>物流商编号</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>物流编号</td>
 * <td></td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>备注 明细信息字符串</td>
 * <td></td>
 * </tr>
 * </table>
 * </blockquote>
 *
 * @author jinhui.huang
 * @version
 *
 */
public class ReturnOderForm  extends BaseForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9020507259783914085L;
	
	//js '[1,2,3,4]'
	private String[] lineIdSelected;
	private String[] sumSelected;
	/**退货单行退货理由*/
	private String[] reasonSelected;
	private String bank;
	private String branch;
	private String account;
	private String userName;
	private String orderId;
	private String orderCode;
	private String memo;
	private String[] chg_extentionCode;
	/** 退货单退货原因*/
	//属性名
	private String retrunReason;
	
	/**退换货类型1：退货 2：换货*/
	private int returnType;
	
	/**以下部分为returnDelivery信息*/
	
	private SoReturnApplicationDeliveryInfo returnDeliveryInfo;

	public SoReturnApplicationDeliveryInfo getReturnDeliveryInfo() {
		return returnDeliveryInfo;
	}

	public void setReturnDeliveryInfo(
			SoReturnApplicationDeliveryInfo returnDeliveryInfo) {
		this.returnDeliveryInfo = returnDeliveryInfo;
	}
	public int getReturnType() {
		return returnType;
	}
	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}
	public String[] getLineIdSelected() {
		return lineIdSelected;
	}
	public void setLineIdSelected(String[] lineIdSelected) {
		this.lineIdSelected = lineIdSelected;
	}
	public String[] getSumSelected() {
		return sumSelected;
	}
	public void setSumSelected(String[] sumSelected) {
		this.sumSelected = sumSelected;
	}

	public String[] getReasonSelected() {
		return reasonSelected;
	}
	public void setReasonSelected(String[] reasonSelected) {
		this.reasonSelected = reasonSelected;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getRetrunReason() {
		return retrunReason;
	}

	public void setRetrunReason(String retrunReason) {
		this.retrunReason = retrunReason;
	}
	

	public String[] getChg_extentionCode() {
		return chg_extentionCode;
	}

	public void setChg_extentionCode(String[] chg_extentionCode) {
		this.chg_extentionCode = chg_extentionCode;
	}

	public ReturnOderForm() {
		super();
	}

	
}
