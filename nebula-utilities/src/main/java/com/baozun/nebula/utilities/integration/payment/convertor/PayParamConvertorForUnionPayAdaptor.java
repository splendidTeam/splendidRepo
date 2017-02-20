package com.baozun.nebula.utilities.integration.payment.convertor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.integration.payment.adaptor.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;
import com.feilong.core.Validator;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.UnionPayBase;

public class PayParamConvertorForUnionPayAdaptor implements PayParamConvertorAdaptor{
	
	public PayParamConvertorForUnionPayAdaptor (){
		SDKConfig.getConfig().loadProperties(ProfileConfigUtil.findCommonPro("config/unionpay.properties"));
	}
	
	/**
	 * 重要：联调测试时请仔细阅读注释！
	 * 
	 * 产品：无跳转产品<br>
	 * 交易：消费：前台交易，有后台通知<br>
	 * 日期： 2015-09<br>
	 * 版本： 1.0.0
	 * 版权： 中国银联<br>
	 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障<br>
	 * 交易说明:1）确定交易成功机制：商户需开发后台通知接口或交易状态查询接口（Form03_6_5_Query）确定交易是否成功，建议发起查询交易的机制：可查询N次（不超过6次），每次时间间隔2N秒发起,即间隔1，2，4，8，16，32S查询（查询到03，04，05继续查询，否则终止查询）
	 *       2）报文中必送卡号，消费后卡号就开通了。
	 */
	public Map<String,String> commandConvertorToMapForCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException{
		Map<String,String> requestParam = new HashMap<String,String>();
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		//版本号
		requestParam.put(SDKConstants.param_version, SDKConfig.getConfig().getVersion());// M

		//字符集编码 可以使用UTF-8,GBK两种方式
		requestParam.put(SDKConstants.param_encoding, SDKConfig.getConfig().getEncoding());// M
		
		//签名方法 目前只支持01-RSA方式证书加密
		requestParam.put(SDKConstants.param_signmethod, SDKConfig.getConfig().getSignMethod());// M

		//交易类型 01-消费
		requestParam.put(SDKConstants.param_txnType, SDKConfig.getConfig().getTxnType());// M

		//交易子类型 01-消费  01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）03：分期付款
		requestParam.put(SDKConstants.param_txnSubType, SDKConfig.getConfig().getTxnSubType());// M

		//业务类型 认证支付2.0
		requestParam.put(SDKConstants.param_bizType, SDKConfig.getConfig().getBizType());// M
		
		/***商户接入参数***/
		//商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
		requestParam.put(SDKConstants.param_merId, SDKConfig.getConfig().getMerId());
		
		////接入类型，商户接入固定填0，不需修改; 0：普通商户直连接入2：平台类商户接入
		requestParam.put(SDKConstants.param_accessType, SDKConfig.getConfig().getAccessType());// M
		
		//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
		requestParam.put(SDKConstants.param_orderId, payParamCommand.getOrderNo());// M
		
		//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestParam.put(SDKConstants.param_txnTime, UnionPayBase.getCurrentTime());// M
		
		//交易币种（境内商户一般是156 人民币）
		requestParam.put(SDKConstants.param_currencyCode, SDKConfig.getConfig().getCurrencyCode());// M
		
		//交易金额，单位分，不要带小数点
		requestParam.put(SDKConstants.param_txnAmt, String.valueOf(payParamCommand.getTotalFee().multiply(new BigDecimal(100)).intValue()));// M
		
		//账号类型: 后台类交易且卡号上送；跨行收单且收单机构收集银行卡信息时上送01：银行卡02：存折03：C卡默认取值：01取值“03”表示以IC终端发起的IC卡交易，IC作为普通银行卡进行支付时，此域填写为“01”
		requestParam.put(SDKConstants.param_accType, SDKConfig.getConfig().getAccType());// C
		
		////////////【开通并付款卡号必送】如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo加密：
		//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
		//String accNo1 = AcpService.encryptData(payParamCommand.getRequestParams().get("accNo")+"", "UTF-8");  			   
		//requestParam.put("accNo", accNo1);
		//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
		//requestParam.put("encryptCertId",AcpService.getEncryptCertId());       
		/////////【开通并付款卡号必送】
		//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
		//contentData.put("accNo", accNo);            
		//请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
		//contentData.put("reqReserved", "透传字段");      
		//如果开通并支付页面需要使用嵌入页面的话，请上送此用法
		//contentData.put("reserved", "{customPage=true}");     
		
		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
		//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注：如果开通失败的“返回商户”按钮也是触发frontUrl地址，点击时是按照get方法返回的，没有通知数据返回商户
		requestParam.put(SDKConstants.param_frontUrl, SDKConfig.getConfig().getFrontUrl());// C

		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestParam.put(SDKConstants.param_backUrl, SDKConfig.getConfig().getBackUrl());// M

		//订单接收超时时间 当距离交易发送时间超过该时间时，银联全渠道系统不再为该笔交易提供支付服务 （此参数传任何值报错400 可能需要开通该服务）
		//requestParam.put("orderTimeout","");
		
		return requestParam;
	}

	/**
	 * 
	 */
	@Override
	public Map<String, String> commandConvertorToMapForCaneclOrder(
			BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException {
		Map<String, String> addition = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		//版本号
		addition.put(SDKConstants.param_version, SDKConfig.getConfig().getVersion());// M

		//字符集编码 可以使用UTF-8,GBK两种方式
		addition.put(SDKConstants.param_encoding, SDKConfig.getConfig().getEncoding());// M
		//签名方法 目前只支持01-RSA方式证书加密
		addition.put(SDKConstants.param_signmethod, SDKConfig.getConfig().getSignMethod());// M
		
		//交易类型 31-消费撤销
		addition.put(SDKConstants.param_txnType, SDKConstants.TXNTYPE_CANECL);
		
		////交易子类型  默认00; 01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）03：分期付款
		addition.put(SDKConstants.param_txnSubType, SDKConstants.TXNSUBTYPE_DEFAULT);// M

		//业务类型
		addition.put(SDKConstants.param_bizType, SDKConfig.getConfig().getBizType());
		
		//渠道类型，07-PC，08-手机
		if(Validator.isNotNullOrEmpty(payParamCommand.getRequestParams().get(SDKConstants.param_channelType))){
			addition.put(SDKConstants.param_channelType, String.valueOf(payParamCommand.getRequestParams().get(SDKConstants.param_channelType)));
		}
		
		/***商户接入参数***/
		//商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		addition.put(SDKConstants.param_merId, SDKConfig.getConfig().getMerId());
		
		//接入类型，商户接入固定填0，不需修改	
		addition.put(SDKConstants.param_accessType, SDKConfig.getConfig().getAccessType());// M
		
		//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		addition.put(SDKConstants.param_orderId, String.valueOf(payParamCommand.getOrderNo()));// M

		//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		addition.put(SDKConstants.param_txnTime, UnionPayBase.getCurrentTime());// M
		
		//交易币种(境内商户一般是156 人民币)
		addition.put(SDKConstants.param_currencyCode, SDKConfig.getConfig().getCurrencyCode());  
		
		//请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
		//addition.put("reqReserved", "透传信息");        
		
		//后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费撤销交易 商户通知,其他说明同消费交易的商户通知
		addition.put(SDKConstants.param_backUrl, SDKConfig.getConfig().getBackUrl());  
		 
		/***要调通交易以下字段必须修改***/
		//【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		addition.put(SDKConstants.param_origQryId, payParamCommand.getOrderNo());   
		
		//交易金额，单位分，不要带小数点
		addition.put(SDKConstants.param_txnAmt, String.valueOf(payParamCommand.getTotalFee().multiply(new BigDecimal(100)).intValue()));// M
		
		return addition;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor#commandConvertorToMapForMobileCreatUrl(com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor)
	 */
	@Override
	public Map<String, String> commandConvertorToMapForMobileCreatUrl(
			BasePayParamCommandAdaptor payParamCommand)
			throws PaymentParamErrorException {
		Map<String,String> requestParam = commandConvertorToMapForCreatUrl(payParamCommand);
		requestParam.put(SDKConstants.param_channelType, SDKConstants.CHANNELTYPE_MOBILE);
		return requestParam;
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor#commandConvertorToMapForOrderInfo(com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor)
	 */
	@Override
	public Map<String, String> commandConvertorToMapForOrderInfo(
			BasePayParamCommandAdaptor payParamCommand) {
		Map<String, String> addition = new HashMap<String, String>();
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		//版本号
		addition.put(SDKConstants.param_version, SDKConfig.getConfig().getVersion());// M

		//字符集编码 可以使用UTF-8,GBK两种方式
		addition.put(SDKConstants.param_encoding, SDKConfig.getConfig().getEncoding());// M
		//签名方法 目前只支持01-RSA方式证书加密
		addition.put(SDKConstants.param_signmethod, SDKConfig.getConfig().getSignMethod());// M
		
		//交易类型 00-默认
		addition.put(SDKConstants.param_txnType, SDKConfig.getConfig().getTxnType());// M

		// //交易子类型  默认00; 01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）03：分期付款
		addition.put(SDKConstants.param_txnSubType, SDKConfig.getConfig().getTxnSubType());// M

		//业务类型
		addition.put(SDKConstants.param_bizType, SDKConfig.getConfig().getBizType());// M

		/***商户接入参数***/
		//商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		addition.put(SDKConstants.param_merId, SDKConfig.getConfig().getMerId());
		
		//接入类型，商户接入固定填0，不需修改 0：普通商户直连接入2：平台类商户接入
		addition.put(SDKConstants.param_accessType, SDKConfig.getConfig().getAccessType());// M
		
		/***要调通交易以下字段必须修改***/
		//****商户订单号，每次发交易测试需修改为被查询的交易的订单号
		addition.put(SDKConstants.param_orderId, String.valueOf(payParamCommand.getOrderNo()));// M

		//****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间
		addition.put(SDKConstants.param_txnTime, UnionPayBase.getCurrentTime());// M
		
		return addition;
	}

	/**
	 * 
	 * @Description 用于构建基本参数的ConvertorAdaptor方法之后，扩展基本参数ConvertorAdaptor对象的注入，可以覆盖配置文件的参数，亦可以扩展。</br>
	 * 				主要应用于同一支付接口，根据参数区别，调用不同形态支付方式场景。</br>
	 * @param params	基本参数容器	
	 * @param addition	扩展参数容器
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @since 2017.1.12
	 */
	@Override
	public Map<String, String> extendCommandConvertorMap(Map<String, String> params, Map<String, Object> additionParams) {
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_version))){
			//版本号
			params.put(SDKConstants.param_version, String.valueOf(additionParams.get(SDKConstants.param_version)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_encoding))){
			//字符集编码 可以使用UTF-8,GBK两种方式
			params.put(SDKConstants.param_encoding, String.valueOf(additionParams.get(SDKConstants.param_encoding)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_signmethod))){
			//签名方法 目前只支持01-RSA方式证书加密
			params.put(SDKConstants.param_signmethod, String.valueOf(additionParams.get(SDKConstants.param_signmethod)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_txnType))){
			//交易类型 01-消费
			params.put(SDKConstants.param_txnType, String.valueOf(additionParams.get(SDKConstants.param_txnType)));
		}

		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_txnSubType))){
			//交易子类型 01-消费  01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）03：分期付款
			params.put(SDKConstants.param_txnSubType, String.valueOf(additionParams.get(SDKConstants.param_txnSubType)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_bizType))){
			//业务类型 认证支付2.0
			params.put(SDKConstants.param_bizType, String.valueOf(additionParams.get(SDKConstants.param_bizType)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_channelType))){
			//渠道类型07-PC
			params.put(SDKConstants.param_channelType, String.valueOf(additionParams.get(SDKConstants.param_channelType)));
		}
		if(Validator.isNullOrEmpty(params.get(SDKConstants.param_channelType))){
			params.put(SDKConstants.param_channelType, SDKConstants.CHANNELTYPE_PC);
		}
		
		/***商户接入参数***/
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_merId))){
			//商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
			params.put(SDKConstants.param_merId, String.valueOf(additionParams.get(SDKConstants.param_merId)));
		}

		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_accessType))){
			//接入类型，商户接入固定填0，不需修改; 0：普通商户直连接入2：平台类商户接入
			params.put(SDKConstants.param_accessType, String.valueOf(additionParams.get(SDKConstants.param_accessType)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_orderId))){
			//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
			params.put(SDKConstants.param_orderId, String.valueOf(additionParams.get(SDKConstants.param_orderId)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_origQryId))){
			/***要调通交易以下字段必须修改***/
			//【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
			params.put(SDKConstants.param_origQryId, String.valueOf(additionParams.get(SDKConstants.param_orderId)));
		}
		
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_txnTime))){
			//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
			params.put(SDKConstants.param_txnTime, String.valueOf(additionParams.get(SDKConstants.param_txnTime)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_currencyCode))){
			//交易币种（境内商户一般是156 人民币）
			params.put(SDKConstants.param_currencyCode, String.valueOf(additionParams.get(SDKConstants.param_currencyCode)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_txnAmt))){
			//交易金额，单位分，不要带小数点
			params.put(SDKConstants.param_txnAmt, String.valueOf(new BigDecimal(String.valueOf(additionParams.get(SDKConstants.param_txnAmt))).multiply(new BigDecimal(100)).intValue()));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_accType))){
			//账号类型: 后台类交易且卡号上送；跨行收单且收单机构收集银行卡信息时上送01：银行卡02：存折03：C卡默认取值：01取值“03”表示以IC终端发起的IC卡交易，IC作为普通银行卡进行支付时，此域填写为“01”
			params.put(SDKConstants.param_accType, String.valueOf(additionParams.get(SDKConstants.param_accType)));
		}
		
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_accNo))){
			////////////【开通并付款卡号必送】
			//如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo加密：
			//AcpService.encryptData(String.valueOf(additionParams.get(SDKConstants.param_accNo)), "UTF-8")
			//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
			params.put(SDKConstants.param_accNo, String.valueOf(additionParams.get(SDKConstants.param_accNo)));
		}

		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_encryptCertId))){
			//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
			params.put(SDKConstants.param_encryptCertId, String.valueOf(additionParams.get(SDKConstants.param_encryptCertId)));
		}
      
		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_reqReserved))){
			//请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
			params.put(SDKConstants.param_reqReserved, String.valueOf(additionParams.get(SDKConstants.param_reqReserved)));
		}

		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_reserved))){
			//如果开通并支付页面需要使用嵌入页面的话，请上送此用法
			//contentData.put("reserved", "{customPage=true}");    
			params.put(SDKConstants.param_reserved, String.valueOf(additionParams.get(SDKConstants.param_reserved)));     
		}

		if(Validator.isNotNullOrEmpty(additionParams.get(SDKConstants.param_orderTimeout))){
			//订单接收超时时间 当距离交易发送时间超过该时间时，银联全渠道系统不再为该笔交易提供支付服务 （此参数传任何值报错400 可能需要开通该服务）
			params.put(SDKConstants.param_orderTimeout,String.valueOf(additionParams.get(SDKConstants.param_orderTimeout)));
		}
		
		return params;
	}


	
}
