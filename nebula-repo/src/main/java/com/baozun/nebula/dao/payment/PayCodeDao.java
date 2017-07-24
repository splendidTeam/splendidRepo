package com.baozun.nebula.dao.payment;

import java.util.Date;

import com.baozun.nebula.model.payment.PayCode;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface PayCodeDao extends GenericEntityDao<PayCode, Long>{

    @NativeQuery(model = PayCode.class)
    PayCode findPayCodeBySubOrdinate(@QueryParam("subOrdinate") String subOrdinate);

    @NativeUpdate
    Integer updatePayCodeBySubOrdinate(@QueryParam("subinateCode") String subinateCode,@QueryParam("payType") Integer payType);

    @NativeUpdate
    Integer updatePayCodePayStatus(@QueryParam("subinateCode") String subinateCode,@QueryParam("modifyTime") Date modifyTime,@QueryParam("paySuccessStatus") Boolean paySuccessStatus);

    @NativeQuery(model = PayCode.class)
    PayCode findPayCodeByCodeAndPayTypeAndPayStatus(@QueryParam("subinateCode") String subinateCode,@QueryParam("payType") Integer payType,@QueryParam("paySuccessStatus") Boolean paySuccessStatus);

    @NativeQuery(model = PayCode.class)
    PayCode findPayCodeByCodeAndPayType(@QueryParam("subinateCode") String subinateCode,@QueryParam("payType") Integer payType);
}
