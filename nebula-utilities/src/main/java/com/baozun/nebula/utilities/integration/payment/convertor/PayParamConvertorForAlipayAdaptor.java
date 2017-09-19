package com.baozun.nebula.utilities.integration.payment.convertor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.baozun.nebula.utilities.integration.payment.adaptor.BasePayParamCommandAdaptor;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;
import com.feilong.core.Validator;

import static com.feilong.core.Validator.isNotNullOrEmpty;

public class PayParamConvertorForAlipayAdaptor implements PayParamConvertorAdaptor{

    /**
     * 此方法将COMMAND中的参数转换成支付宝能使用的map，并在此期间进行参数校验
     * 支付宝在此处有个讨厌的设置默认选中的银行参数是有区别的
     * <p>
     * 国际卡中使用此参数使用:default_bank
     * <p>
     * 国内卡中使用此参数:defaultbank
     * <p>
     */
    public Map<String, String> commandConvertorToMapForCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException{
        Map<String, String> requestParam = new HashMap<String, String>();
        if (isNotNullOrEmpty(payParamCommand.getOrderNo())){//铁定不能为空的
            requestParam.put("out_trade_no", payParamCommand.getOrderNo());
        }else{
            throw new PaymentParamErrorException("out_trade_no can't be null/empty!");
        }

        if (isNotNullOrEmpty(payParamCommand.getDefault_bank())){
            if (payParamCommand.isInternationalCard()){
                requestParam.put("default_bank", payParamCommand.getDefault_bank());
            }else{
                requestParam.put("defaultbank", payParamCommand.getDefault_bank());
            }
        }

        //---------------------------------------------------------------------

        BigDecimal minPay = new BigDecimal(0.01f);
        BigDecimal maxPay = new BigDecimal(100000000);

        if (isNotNullOrEmpty(payParamCommand.getTotalFee())){
            requestParam.put("total_fee", String.valueOf(payParamCommand.getTotalFee()));// 交易金额(可为空)
        }else if (payParamCommand.getTotalFee().compareTo(minPay) == -1 || payParamCommand.getTotalFee().compareTo(maxPay) == 1){
            throw new PaymentParamErrorException("total_fee:" + payParamCommand.getTotalFee() + " can't < " + minPay + " or > " + maxPay);
        }else{
            throw new PaymentParamErrorException("total_fee can't be null/empty!");
        }

        //可为空，但项目不允许传空的超时时间
        if (isNotNullOrEmpty(payParamCommand.getIt_b_pay())){
            requestParam.put("it_b_pay", payParamCommand.getIt_b_pay());// 超时时间(可为空)
        }else{
            throw new PaymentParamErrorException("it_b_pay can't be null/empty!");
        }
        //支付宝扫码支付方式
        if (isNotNullOrEmpty(payParamCommand.getQrPayMode())){
            requestParam.put("qr_pay_mode", payParamCommand.getQrPayMode());
        }

        if (isNotNullOrEmpty(payParamCommand.getBody())){
            requestParam.put("body", payParamCommand.getBody());// 商品描述(可为空)
        }

        //---------------------------------------------------------------------
        //since 5.3.2.22
        String customerIp = payParamCommand.getCustomerIp();
        if (isNotNullOrEmpty(customerIp)){
            //用户在创建交易时，该用户当前所 使用机器的 IP。
            //如果商户申请后台开通防钓鱼 IP 地址检查选项，此字段必填，校验用。
            requestParam.put("exter_invoke_ip", customerIp);
        }

        return requestParam;
    }

    /**
     * 支付宝取消订单可以不填订单号，但支付宝订单流水必须存在
     */
    @Override
    public Map<String, String> commandConvertorToMapForCaneclOrder(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException{
        Map<String, String> requestParam = new HashMap<String, String>();
        if (Validator.isNullOrEmpty(payParamCommand.getOrderNo())){
            throw new PaymentParamErrorException("orderNo can't be null/empty!");
        }

        if (Validator.isNotNullOrEmpty(payParamCommand.getTrade_no())){
            requestParam.put("trade_no", payParamCommand.getTrade_no());
        }

        if (Validator.isNotNullOrEmpty(payParamCommand.getOrderNo())){
            requestParam.put("out_order_no", payParamCommand.getOrderNo());
        }
        if (Validator.isNotNullOrEmpty(payParamCommand.getTrade_role())){
            requestParam.put("trade_role", payParamCommand.getTrade_role());
        }else{
            throw new PaymentParamErrorException("trade_role can't be null/empty!");
        }

        return requestParam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor#commandConvertorToMapForMobileCreatUrl(com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor)
     */
    @Override
    public Map<String, String> commandConvertorToMapForMobileCreatUrl(BasePayParamCommandAdaptor payParamCommand) throws PaymentParamErrorException{
        Map<String, String> requestParam = new HashMap<String, String>();
        if (Validator.isNotNullOrEmpty(payParamCommand.getOrderNo())){//铁定不能为空的
            requestParam.put("out_trade_no", payParamCommand.getOrderNo());
        }else{
            throw new PaymentParamErrorException("out_trade_no can't be null/empty!");
        }

        if (Validator.isNotNullOrEmpty(payParamCommand.getRequestParams().get("omsCode"))){
            requestParam.put("req_id", String.valueOf(payParamCommand.getRequestParams().get("omsCode")));
        }else{
            throw new PaymentParamErrorException("req_id can't be null/empty!");
        }

        BigDecimal minPay = new BigDecimal(0.01f);
        BigDecimal maxPay = new BigDecimal(100000000);

        BigDecimal total = new BigDecimal(String.valueOf(payParamCommand.getRequestParams().get("total")));
        if (Validator.isNotNullOrEmpty(total)){
            requestParam.put("total_fee", String.valueOf(total));
        }else if (total.compareTo(minPay) == -1 || total.compareTo(maxPay) == 1){
            throw new PaymentParamErrorException("total_fee:" + total + " can't < " + minPay + " or > " + maxPay);
        }else{
            throw new PaymentParamErrorException("total_fee can't be null/empty!");
        }

        //可为空，但项目不允许传空的超时时间
        if (Validator.isNotNullOrEmpty(payParamCommand.getIt_b_pay())){
            requestParam.put("pay_expire", payParamCommand.getIt_b_pay());
        }else{
            throw new PaymentParamErrorException("it_b_pay can't be null/empty!");
        }

        if (Validator.isNotNullOrEmpty(payParamCommand.getRequestParams().get("name"))){
            requestParam.put("out_user", String.valueOf(payParamCommand.getRequestParams().get("name")));
        }
        return requestParam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor#commandConvertorToMapForOrderInfo(com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor)
     */
    @Override
    public Map<String, String> commandConvertorToMapForOrderInfo(BasePayParamCommandAdaptor payParamCommand){
        Map<String, String> addition = new HashMap<String, String>();
        addition.put("out_trade_no", String.valueOf(payParamCommand.getRequestParams().get("code")));
        return addition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.utilities.integration.payment.convertor.PayParamConvertorAdaptor#extendCommandConvertorMap(java.util.Map, java.util.Map)
     */
    @Override
    public Map<String, String> extendCommandConvertorMap(Map<String, String> params,Map<String, Object> addition){
        return params;
    }

}
