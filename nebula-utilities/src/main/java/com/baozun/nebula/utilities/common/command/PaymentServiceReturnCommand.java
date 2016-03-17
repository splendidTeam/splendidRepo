package com.baozun.nebula.utilities.common.command;

import java.io.Serializable;


/**
 * 该类只作为同步回传或异步回传信息的COMMAND
 * @author jumbo
 *
 */
@SuppressWarnings("serial")
public class PaymentServiceReturnCommand implements Serializable {

	/**
	 * 回传状态
	 */
	private String returnStatus;
	
	/**
	 * 加密类型
	 */
	private String singType;
	
	/**
	 * 加密字段
	 */
	private String sign;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 商品名称
	 */
	private String subject;
	
	/**
	 * 支付类型
	 */
	private String paymentType;
	
	/**
	 * 接口名称
	 */
	private String exterface;
	
	/**
	 * 流水号
	 */
	private String tradeNo;
	
	/**
	 * 交易状态
	 */
	private String tradeStatus;
	
	/**
	 * 校验状态（如果有就填）
	 */
	private String notifyId;
	
	/**
	 * 通知时间
	 */
	private String notiftTime;
	
	/**
	 * 通知类型
	 */
	private String notifyType;
	
	/**
	 * 卖家支付账号
	 */
	private String seller;
	
	/**
	 * 买家账号
	 */
	private String buyer;
	
	/**
	 * 卖家账号（ID）
	 */
	private String sellerId;
	
	/**
	 * 卖家账号(ID)
	 */
	private String buyerId;
	
	/**
	 * 交易金额
	 */
	private String totalFee;
	
	/**
	 * 商品描述
	 */
	private String body;
	
	/**
	 * 公用回传参数
	 */
	private String extraCommonParam;
	
	/**
	 * 信用支付代理ID
	 */
	private String agentUserId;
	
	/**
	 * 交易创建时间
	 */
	private String gmtCreate;
	
	/**
	 * 交易付款时间
	 */
	private String gmtPayment;
	
	/**
	 * 交易关闭时间
	 */
	private String gmtClose;
	
	/**
	 * 退款状态
	 */
	private String refundStatus;
	
	/**
	 * 退款时间
	 */
	private String gmtRefund;
	
	/**
	 * 商品价格
	 */
	private String price;
	
	/**
	 * 商品数量
	 */
	private String quantity;
	
	/**
	 * 折扣
	 */
	private String discount;
	
	/**
	 * 是否调整总价
	 */
	private String isTotalFeeAdjust;
	
	/**
	 * 是否使用红包
	 */
	private String useCoupon;
	
	/**
	 * 支付渠道
	 */
	private String outChannelType;
	
	/**
	 * 渠道支付金额
	 */
	private String outChannelAmount;
	
	/**
	 * 实际渠道支付金额
	 */
	private String outChannelInst;
	
	/**
	 * 是否扫描码支付
	 */
	private String businessScene;
	
	/**
	 * 支付回传响应信息
	 */
	private String responseMessage;
	
	/**
	 * 交易币种
	 */
	private String orderCurrency;
	
	/**
	 * 清算金额
	 */
	private String settleAmount;
	
	/**
	 * 清算币种
	 */
	private String settleCurrency;
	
	/**
	 * 清算日期
	 */
	private String settleDate;
	
	/**
	 * 清算汇率
	 */
	private String exchangeRate;
	
	/**
	 * 兑换日期
	 */
	private String exchangeDate;
	
	/**
	 * 系统保留域（银联）
	 */
	private String cupReserved;
	
	/**
	 * 版本号
	 * @return
	 */
	private String version;
	
	/**
	 * 字符编码
	 * @return
	 */
	private String charset;
	
	/**
	 * 响应信息
	 * @return
	 */
	private String respMsg;
	
	/**
	 * 跟踪单号
	 * @return
	 */
	private String traceNumber;
	
	/**
	 * 商户名称
	 * @return
	 */
	private String merAbbr;
	
	/**
	 * 商户代码
	 * @return
	 */
	private String merId;
	
	/**
	 * 跟踪时间
	 * @return
	 */
	private String traceTime;
	
	/**
	 * 网银流水
	 * 2014-10-10 新增
	 */
	private String bank_seq_no;
	
	/****************************下面微信相关***************************************/
	
	/***
	 * 返回状态码
	 */
	private String returnCode;
	
	/***
	 * 返回信息
	 */
	private String returnMsg;

	/**
	 * 公众账号ID
	 */
	private String appId;
	/**
	 * 商户号
	 */
	private String mchId;
	/**
	 * 设备号
	 */
	private String deviceInfo;

	/**
	 * 随机字符串
	 */
	private String nonceStr;
	
	/**
	 * 业务结果
	 */
	private String resultCode;
	
	/**
	 * 错误代码
	 */
	private String errCode;
	
	/**
	 * 错误代码描述
	 */
	private String errCodeDes;
	
	/**
	 * 交易类型
	 */
	private String tradeType;
	
	/**
	 * 预支付交易会话标识
	 */
	private String prepayId;
	
	/**
	 * 二维码链接
	 */
	private String codeUrl;
	
	
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public String getBank_seq_no() {
		return bank_seq_no;
	}

	public void setBank_seq_no(String bank_seq_no) {
		this.bank_seq_no = bank_seq_no;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getSingType() {
		return singType;
	}

	public void setSingType(String singType) {
		this.singType = singType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getExterface() {
		return exterface;
	}

	public void setExterface(String exterface) {
		this.exterface = exterface;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}

	public String getNotiftTime() {
		return notiftTime;
	}

	public void setNotiftTime(String notiftTime) {
		this.notiftTime = notiftTime;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getExtraCommonParam() {
		return extraCommonParam;
	}

	public void setExtraCommonParam(String extraCommonParam) {
		this.extraCommonParam = extraCommonParam;
	}

	public String getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getGmtPayment() {
		return gmtPayment;
	}

	public void setGmtPayment(String gmtPayment) {
		this.gmtPayment = gmtPayment;
	}

	public String getGmtClose() {
		return gmtClose;
	}

	public void setGmtClose(String gmtClose) {
		this.gmtClose = gmtClose;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getGmtRefund() {
		return gmtRefund;
	}

	public void setGmtRefund(String gmtRefund) {
		this.gmtRefund = gmtRefund;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getIsTotalFeeAdjust() {
		return isTotalFeeAdjust;
	}

	public void setIsTotalFeeAdjust(String isTotalFeeAdjust) {
		this.isTotalFeeAdjust = isTotalFeeAdjust;
	}

	public String getUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(String useCoupon) {
		this.useCoupon = useCoupon;
	}

	public String getOutChannelType() {
		return outChannelType;
	}

	public void setOutChannelType(String outChannelType) {
		this.outChannelType = outChannelType;
	}

	public String getOutChannelAmount() {
		return outChannelAmount;
	}

	public void setOutChannelAmount(String outChannelAmount) {
		this.outChannelAmount = outChannelAmount;
	}

	public String getOutChannelInst() {
		return outChannelInst;
	}

	public void setOutChannelInst(String outChannelInst) {
		this.outChannelInst = outChannelInst;
	}

	public String getBusinessScene() {
		return businessScene;
	}

	public void setBusinessScene(String businessScene) {
		this.businessScene = businessScene;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getOrderCurrency() {
		return orderCurrency;
	}

	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}

	public String getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(String settleAmount) {
		this.settleAmount = settleAmount;
	}

	public String getSettleCurrency() {
		return settleCurrency;
	}

	public void setSettleCurrency(String settleCurrency) {
		this.settleCurrency = settleCurrency;
	}

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangerRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getExchangeDate() {
		return exchangeDate;
	}

	public void setExchangeDate(String exchangeDate) {
		this.exchangeDate = exchangeDate;
	}

	public String getCupReserved() {
		return cupReserved;
	}

	public void setCupReserved(String cupReserved) {
		this.cupReserved = cupReserved;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getTraceNumber() {
		return traceNumber;
	}

	public void setTraceNumber(String traceNumber) {
		this.traceNumber = traceNumber;
	}

	public String getMerAbbr() {
		return merAbbr;
	}

	public void setMerAbbr(String merAbbr) {
		this.merAbbr = merAbbr;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	
	
}
