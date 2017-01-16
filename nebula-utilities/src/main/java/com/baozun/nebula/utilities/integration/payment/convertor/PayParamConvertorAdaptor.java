package com.baozun.nebula.utilities.integration.payment.convertor;

import java.util.Map;

import com.baozun.nebula.utilities.integration.payment.adaptor.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;

public interface PayParamConvertorAdaptor {

	/**
	 * 用于将command转换成支付平台可用map，用于产生支付链接
	 * @param payParamCommand
	 * @return
	 * @throws PaymentParamErrorException
	 */
	public Map<String,String> commandConvertorToMapForCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException;
	
	/**
	 * 
	 * @Description 用于将command转换成支付平台可用map，用于产生mobile端支付链接
	 * @param payParamCommand
	 * @return
	 * @throws PaymentParamErrorException
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-17
	 */
	public Map<String,String> commandConvertorToMapForMobileCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException;
	
	/**
	 * 用于将command转换成支付平台可用map，用于产生取消订单
	 * @param payParamCommand
	 * @return
	 * @throws PaymentParamErrorException
	 */
	public Map<String,String> commandConvertorToMapForCaneclOrder(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException;
	
	/**
	 * 
	 * @Description 用于将command转换成支付平台可用map，用于产生查询订单支付状态
	 * @param payParamCommand
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-17
	 */
	public Map<String,String> commandConvertorToMapForOrderInfo(BasePayParamCommandAdaptor payParamCommand);
	
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
	public Map<String,String> extendCommandConvertorMap(Map<String,String> params,Map<String, Object> addition);
}
