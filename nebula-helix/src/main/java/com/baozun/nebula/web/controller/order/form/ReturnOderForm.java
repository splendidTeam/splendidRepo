/**
 * 
 */
package com.baozun.nebula.web.controller.order.form;


import java.util.Date;

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
	
	/**退换货类型1：退货 2：换货*/
	private int returnType;
	
	/**以下部分为returnDelivery信息*/
	
	 /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 镇.
     */
    private String town;

    /**
     * 换货地址详细地址
     */
    private String address;

    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收件人号码
     */
    private String receiverPhone;

    /**
     * 收货人手机
     */
    private String receiverMobile;

    /**
     * 物流商编号
     */
    private String transName;

    /**
     * 物流单号
     */
    private String transCode;

    /**
     * 备注 明细信息字符串
     */
    private String description;

    

	public String getProvince() {
		return province;
	}



	public void setProvince(String province) {
		this.province = province;
	}



	public String getDistrict() {
		return district;
	}



	public void setDistrict(String district) {
		this.district = district;
	}



	public String getTown() {
		return town;
	}



	public void setTown(String town) {
		this.town = town;
	}



	public String getZipcode() {
		return zipcode;
	}



	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}



	public String getReceiver() {
		return receiver;
	}



	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}



	public String getReceiverPhone() {
		return receiverPhone;
	}



	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}



	public String getReceiverMobile() {
		return receiverMobile;
	}



	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}



	public String getTransName() {
		return transName;
	}



	public void setTransName(String transName) {
		this.transName = transName;
	}



	public String getTransCode() {
		return transCode;
	}



	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public int getReturnType() {
		return returnType;
	}



	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}



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
