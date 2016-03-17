package com.baozun.nebula.utilities.integration.payment.wechat;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.Md5Encrypt;
import com.baozun.nebula.utilities.common.convertor.MapAndStringConvertor;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;
import com.baozun.nebula.utilities.integration.payment.exception.RequestTypeNotSupporttedException;

public class WechatPaymentRequest implements PaymentRequest, Serializable {


	private static final long serialVersionUID = -2680226482587807706L;

	private static final Logger logger = LoggerFactory
			.getLogger(WechatPaymentRequest.class);

	private Map<String, String> paymentParameters;

	/**
	 * 公众账号ID
	 */
	private String  appid;
	/**
	 * 商户号
	 */
	private String  mch_id;
	/**
	 * 设备号	
	 */
	private String  device_info;
	/**
	 * 随机字符串
	 */
	private String  nonce_str;
	/**
	 * 签名
	 */
	private String  sign;
	/**
	 * 商品描述
	 */
	private String  body;
	/**
	 * 商品详情
	 */
	private String  detail;
	/**
	 * 附加数据
	 */
	private String  attach;
	/**
	 * 商户订单号
	 */
	private String  out_trade_no;
	/**
	 * 币种类型
	 */
	private String  fee_type;
	/**
	 * 总金额
	 */
	private Integer total_fee;
	/**
	 *  终端IP
	 */
	private String  spbill_create_ip;
	/**
	 *  交易开始时间
	 */
	private String  time_start;
	/**
	 *  交易结束时间
	 */
	private String  time_expire;
	/**
	 *  商品标记
	 */
	private String  goods_tag;
	/**
	 *  通知地址
	 */
	private String  notify_url;
	/**
	 *  交易类型
	 */
	private String  trade_type;
	/**
	 *  商品ID
	 */
	private String  product_id;
	/**
	 *  用户标识
	 */
	private String  openid;
	
	
	
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

	public void setPaymentParameters(Map<String, String> paymentParameters) {
		this.paymentParameters = paymentParameters;
	}

	@Override
	public void prepare() throws PaymentException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supportRequestType(String requestType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRequestType(String requestType)
			throws RequestTypeNotSupporttedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPaymentParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestHtml() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 设置PC端支付初始化参数并检查
	 * 
	 * @param prop
	 * @param orderNo
	 * @param amt
	 * @param addition
	 * @throws PaymentException
	 */
	public void initPaymentRequestParams(Properties prop, Map<String, String> addition)
			throws PaymentException {
		//数据校验
		//setParamMap(addition);
		//数据封装
		String toBeSignedString = MapAndStringConvertor.getToBeSignedString(addition);
		String sign = Md5Encrypt.md5(toBeSignedString + "&key="+prop.getProperty("wechat.paySignKey")).toUpperCase();
		addition.put("sign", sign);
		this.paymentParameters = addition;
	}

}
