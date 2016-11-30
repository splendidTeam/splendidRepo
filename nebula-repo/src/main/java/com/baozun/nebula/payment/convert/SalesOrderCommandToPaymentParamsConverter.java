/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-29
 */
package com.baozun.nebula.payment.convert;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

	public static Map<String, Object> convert(
			SalesOrderCommand salesOrderCommand) {
		Map<String, Object> paramsMap = null;
		if (Validator.isNotNullOrEmpty(salesOrderCommand)) {
			paramsMap = new HashMap<String, Object>();
			Field[] fields = SalesOrderCommand.class.getFields();
			for (Field field : fields) {
				try {
					Object param = ReflectionUtil.getFieldValue(
							salesOrderCommand, field.getName());
					if (param instanceof OnLinePaymentCommand) {

					} else if (param instanceof OnLinePaymentCancelCommand) {

					}
					paramsMap.put(field.getName(), param);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return paramsMap;
	}
}
