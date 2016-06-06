package com.baozun.nebula.web.controller.payment.resolver;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.web.MemberDetails;
import com.feilong.core.Validator;

public class WechatPaymentResolver implements PaymentResolver {

	@Autowired
	private SdkPaymentManager sdkPaymentManager;
	
	@Autowired
	private PaymentManager paymentManager;

	@Autowired
	private PayManager payManager;
	
	@Override
	public String buildPayUrl(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, Map<String,Object> extra, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IllegalPaymentStateException {
		
		String payInfo = payInfoLog.getPayInfo();

		Properties pro = ProfileConfigUtil.findCommonPro("config/payMentInfo.properties");
		String payTypeStr = pro.getProperty(payInfo + ".payType");
		
		Integer payType = null;

		if (Validator.isNotNullOrEmpty(payTypeStr)) {
			payType = Integer.parseInt(payTypeStr.trim());
		}
		
		String subOrdinate = payInfoLog.getSubOrdinate();
		
		//更新t_so_paycode
    	sdkPaymentManager.updatePayCodeBySubOrdinate(subOrdinate,payType);
		// 加上showwxpaytitle=1 微信会显示“微信安全支付”
		String url = "/pay/wechatPay.htm?showwxpaytitle=1&soCode="+subOrdinate;
	
		
		
		
		
		
		
//		String userAgent = RequestUtil.getUserAgent(request);
//		String orderNo = "";
//		Date orderCreateDate = null;
//		String payTypeStr = null;
//		PayCode pc = sdkPaymentManager.findPayCodeBySubOrdinate(subOrdinate);
//		if (Validator.isNullOrEmpty(pc)) {
//			// 支付订单号不存在
//			log.error("pay order NO. is not exists.  subOrdinate is {}", subOrdinate);
//			throw new BusinessException(ErrorCodes.transaction_ordercode_error);
//		} else {
//			// 验证订单状态是否正确。
//			Map<String, Object> paraMap = new HashMap<String, Object>();
//			paraMap.put("subOrdinate", subOrdinate);
//			// 查询订单的需要支付的payInfolog
//			List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);
//			if (Validator.isNotNullOrEmpty(payInfoLogs)) {
//				PayInfoLog payInfoLog = payInfoLogs.get(0);
//				SalesOrderCommand salesOrderCommand = sdkOrderManager.findOrderById(payInfoLog.getOrderId(), 1);
//				if (!salesOrderCommand.getLogisticsStatus().equals(SalesOrder.SALES_ORDER_STATUS_NEW)
//						&& !salesOrderCommand.getLogisticsStatus().equals(SalesOrder.SALES_ORDER_STATUS_TOOMS)) {
//					throw new BusinessException(ErrorCodes.transaction_so_paystatus_trade_close);
//				}
//				orderCreateDate = salesOrderCommand.getCreateTime();
//				orderNo = salesOrderCommand.getCode();
//				String payInfo = payInfoLog.getPayInfo();
//
//				Properties pro = ProfileConfigUtil.findCommonPro("config/payMentInfo.properties");
//				payTypeStr = pro.getProperty(payInfo + ".payType");
//			}
//			Integer payType = null;
//
//			if (Validator.isNotNullOrEmpty(payTypeStr)) {
//				payType = Integer.parseInt(payTypeStr.trim());
//			}
//
//			BigDecimal _total = pc.getPayMoney();
//			model.addAttribute("orderNo", orderNo);
//			model.addAttribute("total", _total);
//			model.addAttribute("code", subOrdinate);
//			model.addAttribute("appId", WechatConfig.APP_ID);
//			String clientIp = RequestUtil.getClientIp(request); // 下单机器的IP
//
//			/***************************************BEGIN 统一下单接口 BEGIN*************************************************/
//			String timeExpire = DateFormatUtils.format(new Date(orderCreateDate.getTime() + 45*60*1000), "yyyyMMddHHmmss");
//			String totalFee = (_total.multiply(new BigDecimal(100))).setScale(0).toString();
//			String productId = "123";
//			String body = "测试商品";
//			
//			String openId = "";
//			String tradeType ="";
//			//如果是微信客户端
//			if(userAgent.contains("MicroMessenger")){
//				openId = "o_hORjq6sBDYzaUiWLVBaeAnVuHs";
//				tradeType = WechatResponseKeyConstants.TRADE_TYPE_JSAPI;
//			}else{
//				tradeType = WechatResponseKeyConstants.TRADE_TYPE_NATIVE;
//			}
//			model.addAttribute("openId", openId);
//			
//			WechatPayParamCommand wechatPayParamCommand = getWechatPayParamCommand(
//					subOrdinate, orderCreateDate, clientIp, timeExpire,
//					totalFee,tradeType, openId, productId,body);
//			
//			
//			//前面参数自己定义 wechatPayParamCommand
//			PaymentResult paymentResult = paymentManager.unifiedOrder(wechatPayParamCommand, payType.toString());
//			
//			
//			if(paymentResult.getPaymentServiceSatus().equals(PaymentServiceStatus.SUCCESS)){
//				if(tradeType.equals(WechatResponseKeyConstants.TRADE_TYPE_NATIVE)){
//					//二维码参照下面代码
//					String url = paymentResult.getPaymentStatusInformation().getCodeUrl();
//					
//				    BitMatrix bitMatrix = null;
//			        int width = 300; 
//			        int height = 300; 
//			        //二维码的图片格式 
//			        String format = "gif"; 
//			        Hashtable hints = new Hashtable(); 
//			        //内容所使用编码 
//			        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
//			    
//					try {
//						bitMatrix = new MultiFormatWriter().encode(url, 
//						        BarcodeFormat.QR_CODE, width, height, hints);
//					} catch (WriterException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} 
//					OutputStream os =null;
//					try {
//						//生成二维码 
//				        os =  response.getOutputStream(); 
//						MatrixToImageUtil.writeToStream(bitMatrix, format, os);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}finally {
//						try {
//							os.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//					return null;
//					
//				}else{
//					// jsapi 参考下面代码
//					model.addAttribute("wechatJsApiPayCommand",payManager.getWechatJsApiPayCommand(paymentResult.getPaymentStatusInformation().getPrepayId()));
//					
//					log.info("*************prepayId:{}*******************",paymentResult.getPaymentStatusInformation().getPrepayId());
//					// 获取wx.config中的参数
//					String url = RequestUtil.getRequestAllURL(request);
//					url = url.split("#")[0];
//					
//					//自己传 jsapi_ticket的有效期为7200秒 开发者必须在自己的服务全局缓存jsapi_ticket
//					// 相关文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E5.8F.91.E8.B5.B7.E4.B8.80.E4.B8.AA.E5.BE.AE.E4.BF.A1.E6.94.AF.E4.BB.98.E8.AF.B7.E6.B1.82
//					
//					String jsapi_ticket = "bxLdikRXVbTPdHSM05e5u-Ra2IWXllo34re0FgAe0K8esIUajuUJZkndxrVWzSQET1tbi4pTm5x_IsYqJ77KNQ";
//					
//					model.addAttribute("wechatJsApiConfigCommand", payManager.getWechatJsApiConfigCommand(url,jsapi_ticket));
//					return "/wechat/payment";
//				}
//	
//			}else{
//				throw new BusinessException(ErrorCodes.WECHAT_ERROR_MSG,new Object[]{paymentResult.getPaymentStatusInformation().getErrCodeDes()});
//			}
		
		
		
		
		
		
		
		return null;
	}

	@Override
	public String doPayReturn(HttpServletRequest request,
			HttpServletResponse response, String payType, Device device)
			throws IllegalPaymentStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doPayNotify(HttpServletRequest request,
			HttpServletResponse response, String payType, Device device)
			throws IllegalPaymentStateException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	


	
}
