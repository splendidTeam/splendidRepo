package com.baozun.nebula.sdk.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * 
 * @author 阳羽
 * @createtime 2014-3-7 上午10:16:48
 */
public interface SdkPaymentManager extends BaseManager{

    /**
     * 保存支付日志
     * 
     * @param createTime
     * @param message
     * @param operator
     * @param returnVal
     */
    void savePaymentLog(Date createTime,String message,String operator,String returnVal);

    /**
     * 根据订单号修改支付信息中的调用关闭交易的状态
     * 
     * @param orderId
     * @param callCloseStatus
     * @param modifyTime
     * @return
     */
    void upPayInfoCallCloseStaBySubOrdinate(Long orderId,Boolean callCloseStatus,Date modifyTime);

    /**
     * 保存支付订单号信息(t_so_paycode)以及订单支付信息(t_so_payinfo)
     * 
     * @param sos
     * @return
     */
    String savePayCodeInfo(List<SalesOrderCommand> sos);

    /**
     * 根据支付订单号(t_so_paycode subordinate)更改支付类型
     * 
     * @param subinateCode
     * @param payType
     */
    void updatePayCodeBySubOrdinate(String subinateCode,Integer payType);

    /**
     * 根据支付订单号(t_so_paycode subordinate)更改支付成功状态
     * 
     * @param subinateCode
     * @param modifyTime
     * @param paySuccessStatus
     */
    void updatePayCodePayStatus(String subinateCode,Date modifyTime,Boolean paySuccessStatus);

    /**
     * 根据支付订单号查询支付订单号信息
     * 
     * @param subOrdinate
     * @return
     */
    PayCode findPayCodeBySubOrdinate(String subOrdinate);

    /**
     * 根据支付订单号、支付类型、是否支付成功查询支付订单信息
     * 
     * @param code
     * @param payType
     * @param flag
     * @return
     */
    PayCode findPayCodeByCodeAndPayTypeAndPayStatus(String code,Integer payType,boolean flag);

    /**
     * 根据支付订单号、支付类型查询支付订单信息
     * 
     * @param code
     * @param payType
     * @return
     */
    PayCode findPayCodeByCodeAndPayType(String code,Integer payType);

    /**
     * 根据订单id查询支付信息
     * 
     * @param orderId
     * @return
     */
    List<PayInfoCommand> findPayInfoCommandByOrderId(Long orderId);

    /**
     * 查询付款日志
     * 
     * @param paraMap
     * @return
     */
    List<PayInfoLog> findPayInfoLogListByQueryMap(Map<String, Object> paraMap);
}
