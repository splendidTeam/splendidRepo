/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-29
 */
package com.baozun.nebula.payment.convert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.baozun.nebula.command.OnLinePaymentCancelCommand;
import com.baozun.nebula.command.OnLinePaymentCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.common.ReflectionUtil;
import com.feilong.core.Validator;

/**
 * @Description
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-29
 */
public class SalesOrderCommandToPaymentParamsConverter {

	public static Map<String, Object> convert(Object data) {
		Map<String, Object> paramsMap = null;
		if(null == data){
			return paramsMap;
		}
		if(data instanceof SalesOrderCommand){
			SalesOrderCommand salesOrderCommand = (SalesOrderCommand)data;
			if (Validator.isNotNullOrEmpty(salesOrderCommand)) {
				paramsMap = new HashMap<String, Object>();
				Field[] fields = SalesOrderCommand.class.getFields();
				for (Field field : fields) {
					try {
						Object param = ReflectionUtil.getFieldValue(salesOrderCommand, field.getName());
						if (param instanceof OnLinePaymentCommand) {
							Field[] paymentFields = OnLinePaymentCommand.class.getFields();
							for (Field paymentField : paymentFields) {
								Object paymentParam = ReflectionUtil.getFieldValue(param, paymentField.getName());
								paramsMap.put(paymentField.getName(), paymentParam);
							}
						} else if (param instanceof OnLinePaymentCancelCommand) {
							Field[] paymentFields = OnLinePaymentCancelCommand.class.getFields();
							for (Field paymentField : paymentFields) {
								Object paymentParam = ReflectionUtil.getFieldValue(param, paymentField.getName());
								paramsMap.put(paymentField.getName(), paymentParam);
							}
						}
						paramsMap.put(field.getName(), param);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return paramsMap;
	}
}
