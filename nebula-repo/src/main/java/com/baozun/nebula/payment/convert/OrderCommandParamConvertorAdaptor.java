package com.baozun.nebula.payment.convert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.command.OnLinePaymentCommand;
import com.baozun.nebula.payment.manager.ReservedPaymentType;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.feilong.core.Validator;

import static com.feilong.core.Validator.isNotNullOrEmpty;

public class OrderCommandParamConvertorAdaptor implements PayParamCommandAdaptor{

    private SalesOrderCommand salesOrderCommand;

    private Map<String, Object> requestParams;

    public static final String BUY = "buy";

    public static final String SELLER = "seller";

    public SalesOrderCommand getSalesOrderCommand(){
        if (null == salesOrderCommand){
            setSalesOrderCommand(new SalesOrderCommand());
        }
        return salesOrderCommand;
    }

    public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand){
        this.salesOrderCommand = salesOrderCommand;
    }

    /**
     * @return the requestParams
     */
    @Override
    public Map<String, Object> getRequestParams(){
        if (null == requestParams){
            setRequestParams(new HashMap<String, Object>());
        }
        return requestParams;
    }

    /**
     * @param requestParams
     *            the requestParams to set
     */
    @Override
    public void setRequestParams(Map<String, Object> requestParams){
        this.requestParams = requestParams;
    }

    @Override
    public String getDefault_bank(){
        String bankCode = null;
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCommand()) && StringUtils.isNotEmpty(getSalesOrderCommand().getOnLinePaymentCommand().getBankCode())){
            bankCode = getSalesOrderCommand().getOnLinePaymentCommand().getBankCode();
            return bankCode;
        }else if (Validator.isNotNullOrEmpty(getRequestParams().get("bankCode")))
            return getRequestParams().get("bankCode").toString();
        else
            return null;
    }

    @Override
    public String getOrderNo(){
        if (StringUtils.isNotEmpty(getSalesOrderCommand().getCode()))
            return salesOrderCommand.getCode();
        else if (Validator.isNotNullOrEmpty(getRequestParams().get("code")))
            return getRequestParams().get("code").toString();
        else
            return null;
    }

    @Override
    public BigDecimal getTotalFee(){
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getTotal()))
            return salesOrderCommand.getTotal();
        else if (Validator.isNotNullOrEmpty(getRequestParams().get("total")))
            return new BigDecimal(getRequestParams().get("total").toString());
        else
            return null;
    }

    @Override
    public boolean isInternationalCard(){
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCommand()) && BooleanUtils.isTrue(getSalesOrderCommand().getOnLinePaymentCommand().getIsInternationalCard()))
            return true;
        else if (Validator.isNotNullOrEmpty(getRequestParams().get("isInternationalCard")) && Boolean.valueOf(getRequestParams().get("isInternationalCard").toString()))
            return true;
        else
            return false;
    }

    @Override
    public String getBody(){
        String body = getSalesOrderCommand().getDescribe();
        if (StringUtils.isNotEmpty(body))
            return body;
        else if (Validator.isNotNullOrEmpty(getRequestParams().get("describe")))
            return getRequestParams().get("describe").toString();
        else
            return null;
    }

    @Override
    public String getTrade_role(){
        String role = null;
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCancelCommand()) && StringUtils.isEmpty(getSalesOrderCommand().getOnLinePaymentCancelCommand().getTrade_role())){
            role = getSalesOrderCommand().getOnLinePaymentCancelCommand().getTrade_role();
        }else if (Validator.isNotNullOrEmpty(getRequestParams().get("trade_role"))){
            role = getRequestParams().get("trade_role").toString();
        }
        if (BUY.equals(role)){
            return "B";
        }
        if (SELLER.equals(role)){
            return "S";
        }
        return null;
    }

    @Override
    public String getTrade_no(){
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCancelCommand()) && StringUtils.isNotEmpty(getSalesOrderCommand().getOnLinePaymentCancelCommand().getTrade_no()))
            return salesOrderCommand.getOnLinePaymentCancelCommand().getTrade_no();
        else if (Validator.isNotNullOrEmpty(getRequestParams().get("trade_no")))
            return getRequestParams().get("trade_no").toString();
        else
            return null;
    }

    @Override
    public String getPaymentType(){
        Integer payType = null;
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCommand()) && Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCommand().getPayType())){
            payType = getSalesOrderCommand().getOnLinePaymentCommand().getPayType();
        }else if (Validator.isNotNullOrEmpty(getRequestParams().get("payType"))){
            payType = Integer.valueOf(getRequestParams().get("payType").toString());
        }
        String type = null;
        switch (payType) {
            case ReservedPaymentType.ALIPAY:
                type = PaymentFactory.PAY_TYPE_ALIPAY;
                break;
            case ReservedPaymentType.ALIPAY_BANK:
                type = PaymentFactory.PAY_TYPE_ALIPAY_BANK;
                break;
            case ReservedPaymentType.ALIPAY_CREDIT:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT;
                break;
            case ReservedPaymentType.ALIPAY_CREDIT_INT_V:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
                break;
            case ReservedPaymentType.ALIPAY_CREDIT_INT_M:
                type = PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
                break;
            case ReservedPaymentType.UNIONPAY:
                type = PaymentFactory.PAY_TYPE_UNIONPAY;
                break;
            case ReservedPaymentType.WECHAT:
                type = PaymentFactory.PAY_TYPE_WECHAT;
        }
        return type;
    }

    @Override
    public String getPaymentTime(){
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCommand()) && StringUtils.isNotEmpty(getSalesOrderCommand().getOnLinePaymentCommand().getPayTime()))
            return salesOrderCommand.getOnLinePaymentCommand().getPayTime();
        else if (Validator.isNotNullOrEmpty(getRequestParams().get("payTime")))
            return getRequestParams().get("payTime").toString();
        else
            return null;
    }

    @Override
    public String getCustomerIp(){
        SalesOrderCommand salesOrderCommand2 = getSalesOrderCommand();
        OnLinePaymentCommand onLinePaymentCommand = salesOrderCommand2.getOnLinePaymentCommand();
        if (isNotNullOrEmpty(onLinePaymentCommand) && StringUtils.isNotEmpty(onLinePaymentCommand.getCustomerIp())){
            return onLinePaymentCommand.getCustomerIp();
        }

        Map<String, Object> requestParams2 = getRequestParams();
        Object object = requestParams2.get("customerIp");
        if (isNotNullOrEmpty(object)){
            return object.toString();
        }
        return null;
    }

    @Override
    public String getIt_b_pay(){
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCommand()) && StringUtils.isNotEmpty(getSalesOrderCommand().getOnLinePaymentCommand().getItBPay()))
            return salesOrderCommand.getOnLinePaymentCommand().getItBPay();
        else if (Validator.isNotNullOrEmpty(getRequestParams().get("itBPay")))
            return getRequestParams().get("itBPay").toString();
        else
            return null;
    }

    @Override
    public String getQrPayMode(){
        if (Validator.isNotNullOrEmpty(getSalesOrderCommand().getOnLinePaymentCommand()) && StringUtils.isNotEmpty(getSalesOrderCommand().getOnLinePaymentCommand().getQrPayMode()))
            return salesOrderCommand.getOnLinePaymentCommand().getQrPayMode();
        else if (Validator.isNotNullOrEmpty(getRequestParams().get("qrPayMode")))
            return getRequestParams().get("qrPayMode").toString();
        else
            return null;
    }

}
