package com.baozun.nebula.utilities.common.command;

public class PaymentServiceReturnForMobileCommand extends PaymentServiceReturnCommand {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3425329191621529450L;

	/**
	 * 结果
	 */
	private String result;
	
	/**
	 * request_token
	 */
	private String request_token;
	
	/**
	 * 异步数据
	 * @return
	 */
	private String notify_data;
	
	/**
	 * 加密方式
	 * @return
	 */
	private String sec_id;
	
	/**
	 * 版本
	 * @return
	 */
	private String v;
	
	/**
	 * 服务
	 * @return
	 */
	private String service;
	
	/**
	 * 是否使用优惠券
	 * @return
	 */
	private String use_coupon;
	
	/**
	 * 
	 * @return
	 */
	private String is_total_fee_adjust;
	
	/**
	 * 
	 * @return
	 */
	private String gmt_payment;
	
	/**
	 * 
	 * @return
	 */
	private String gmt_create;

	public String getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

	public String getUse_coupon() {
		return use_coupon;
	}

	public void setUse_coupon(String use_coupon) {
		this.use_coupon = use_coupon;
	}

	public String getIs_total_fee_adjust() {
		return is_total_fee_adjust;
	}

	public void setIs_total_fee_adjust(String is_total_fee_adjust) {
		this.is_total_fee_adjust = is_total_fee_adjust;
	}

	public String getGmt_payment() {
		return gmt_payment;
	}

	public void setGmt_payment(String gmt_payment) {
		this.gmt_payment = gmt_payment;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRequest_token() {
		return request_token;
	}

	public void setRequest_token(String request_token) {
		this.request_token = request_token;
	}

	public String getNotify_data() {
		return notify_data;
	}

	public void setNotify_data(String notify_data) {
		this.notify_data = notify_data;
	}

	public String getSec_id() {
		return sec_id;
	}

	public void setSec_id(String sec_id) {
		this.sec_id = sec_id;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

}
