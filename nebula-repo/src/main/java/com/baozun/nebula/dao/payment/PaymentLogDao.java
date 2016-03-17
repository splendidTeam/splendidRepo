package com.baozun.nebula.dao.payment;

import java.util.Date;

import com.baozun.nebula.model.payment.PaymentLog;

import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface PaymentLogDao extends GenericEntityDao<PaymentLog,Long>{

	@NativeUpdate
	void savePaymentLog(@QueryParam("createTime")Date createTime,@QueryParam("message")String message,@QueryParam("operator")String operator,@QueryParam("returnVal")String returnVal);

}
