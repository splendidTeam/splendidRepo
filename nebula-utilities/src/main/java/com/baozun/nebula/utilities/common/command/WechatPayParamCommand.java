/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.utilities.common.command;

import java.io.Serializable;

/**
 * @author lxy
 */
public class WechatPayParamCommand implements Serializable {

 
	private static final long serialVersionUID = 7408420859396853610L;
	
	/**
	 * 公众账号ID
	 * sampleValue="wx8888888888888888"
	 */
	private String  appid;
	
	/**
	 * 商户号
	 * sampleValue="1900000109"
	 */
	private String  mch_id;
	
	/**
	 * 设备号	
	 * sampleValue="013467007045764"
	 */
	private String  device_info;
	
	/**
	 * 随机字符串
	 * sampleValue="5K8264ILTKCH16CQ2502SI8ZNMTM67VS"
	 */
	private String  nonce_str;
	
	/**
	 * 签名
	 * sampleValue="C380BEC2BFD727A4B6845133519F3AD6"
	 */
	private String  sign;
	
	/**
	 * 商品描述
	 * sampleValue="Ipad mini  16G  白色"
	 */
	private String  body;
	
	/**
	 * 商品详情
	 * sampleValue="Ipad mini  16G  白色"
	 */
	private String  detail;
	
	/**
	 * 附加数据
	 * sampleValue="说明"
	 */
	private String  attach;
	
	/**
	 * 商户订单号
	 * sampleValue="1217752501201407033233368018"
	 */
	private String  out_trade_no;
	
	/**
	 * 币种类型
	 * sampleValue="CNY"
	 */
	private String  fee_type;
	
	/**
	 * 总金额
	 * sampleValue="888"
	 */
	private Integer total_fee;
	
	/**
	 *  终端IP
	 *  sampleValue="8.8.8.8"
	 */
	private String  spbill_create_ip;
	
	/**
	 *  交易开始时间
	 *  sampleValue="20091225091010"
	 */
	private String  time_start;
	
	/**
	 *  交易结束时间
	 *  sampleValue="20091225091010"
	 */
	private String  time_expire;
	
	/**
	 *  商品标记
	 *  sampleValue="WXG"
	 */
	private String  goods_tag;
	
	/**
	 *  通知地址
	 *  sampleValue="http://www.baidu.com/"
	 */
	private String  notify_url;
	
	/**
	 *  交易类型
	 *  sampleValue="JSAPI"
	 */
	private String  trade_type;
	
	/**
	 *  商品ID
	 *  sampleValue="12235413214070356458058"
	 */
	private String  product_id;
	
	/**
	 *  用户标识
	 *  sampleValue="oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"
	 */
	private String  openid;
	
	/**
	 * 微信订单号
	 * sampleValue="013467007045764"
	 */
	private String  transaction_id;
	
	
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public Integer getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_expire() {
		return time_expire;
	}
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}
	public String getGoods_tag() {
		return goods_tag;
	}
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}


}
