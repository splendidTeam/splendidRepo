/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.utilities.integration.payment.alipay;

import static com.baozun.nebula.utilities.common.condition.RequestParam.ALIPAYFAIL;
import static com.baozun.nebula.utilities.common.condition.RequestParam.ALIPAYSUCCESS;
import static com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus.FAILURE;
import static com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus.SUCCESS;
import static com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus.UNDEFINED;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.Md5Encrypt;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.RequestMapUtil;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnForMobileCommand;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.common.condition.ResponseParam;
import com.baozun.nebula.utilities.common.convertor.MapAndStringConvertor;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.convertor.RequestToCommand;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;
import com.baozun.nebula.utilities.io.http.HttpClientUtil;
import com.baozun.nebula.utilities.io.http.HttpMethodType;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

public abstract class AbstractAlipayPaymentAdaptor implements PaymentAdaptor{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAlipayPaymentAdaptor.class);

    public static final String _INPUT_CHARSET = "utf-8";

    //--------------------------------------------------------------------------------------------------------------------------

    protected Properties configs;

    private String payMethod;

    private String isDefault_login;

    private String returnUrl;

    private String notifyUrl;

    private String errorNotifyUrl;

    //--------------------------------------------------------------------------------------------------------------------------

    @Override
    public PaymentRequest newPaymentRequest(String requestType,Map<String, String> addition){
        AlipayPaymentRequest request = new AlipayPaymentRequest();
        try{
            request.setRequestType(requestType);
            if (Validator.isNotNullOrEmpty(isDefault_login)){
                addition.put("default_login", isDefault_login);
            }
            request.initPaymentRequestParams(configs, addition, this.getPayMethod(), returnUrl, notifyUrl, errorNotifyUrl);
        }catch (PaymentException ex){
            LOGGER.error("Alipay newPaymentRequest error: " + ex.toString());
        }
        return request;
    }

    @Override
    public PaymentRequest newPaymentRequestForMobileCreateDirect(Map<String, String> addition){
        AlipayPaymentRequest request = new AlipayPaymentRequest();
        try{
            request.initPaymentRequestParamsForMobileCreateDirect(configs, addition, returnUrl, notifyUrl, errorNotifyUrl);
        }catch (PaymentException ex){
            LOGGER.error("Alipay newPaymentRequestForMobileCreateDirect error: ", ex);
        }
        return request;
    }

    private PaymentRequest newPaymentRequestForMobileAuthAndExecute(Map<String, String> addition){
        AlipayPaymentRequest request = new AlipayPaymentRequest();
        try{
            request.initPaymentRequestParamsForMobileAuthAndExecute(configs, addition);
        }catch (PaymentException ex){
            LOGGER.error("Alipay newPaymentRequestForMobileAuthAndExecute error: ", ex);
        }
        return request;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    @Override
    public PaymentResult getPaymentResult(HttpServletRequest request){
        PaymentResult paymentResult = new PaymentResult();

        //--------------------------------------------------------------------------------------------------------

        RequestToCommand requestToCommand = new RequestToCommand();

        PaymentServiceReturnCommand paymentServiceReturnCommand = requestToCommand.alipaySynRequestToCommand(request);
        paymentResult.setPaymentStatusInformation(paymentServiceReturnCommand);

        //--------------------------------------------------------------------------------------------------------

        //卖家支 付宝账 号        String(10 0)        卖家支付宝账号，可以是 Email 或手机号码。        可空
        String sellerEmail = request.getParameter("seller_email");

        //不是我们配置的seller_email
        String configSellerEmail = configs.getProperty("param.seller_email");
        if (!configSellerEmail.equals(sellerEmail)){
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
            paymentResult.setMessage(Slf4jUtil.format("get seller_email:[{}], not our config:[{}]", sellerEmail, configSellerEmail));
            return paymentResult;
        }

        //--------------------------------------------------------------------------------------------------------

        // 返回函数进行加密比较
        if (isNotifyVerifySuccess(request.getParameter("notify_id"))){
            Map<String, String> responseMap = new HashMap<>();
            RequestMapUtil.requestConvert(request, responseMap);

            responseMap.remove("sign");
            responseMap.remove("sign_type");

            //----------------------------------
            String toBeSignedString = MapAndStringConvertor.getToBeSignedString(responseMap);

            String localSign = Md5Encrypt.md5(toBeSignedString + configs.getProperty("param.key"), configs.getProperty("param._input_charset"));
            String sign = request.getParameter("sign").toString();

            if (sign.equals(localSign)){

                String satus = request.getParameter("is_success");

                if (satus.equals("T")){
                    String resultStr = request.getParameter("trade_status");
                    getResult(resultStr, paymentResult);

                }else if (satus.equals("F")){
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    paymentResult.setMessage("failure");
                }else{
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
                    paymentResult.setMessage("undefined");
                }
            }else{
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
                paymentResult.setMessage("sign not match");
            }
        }else{
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
            paymentResult.setMessage("alipay return value error");
        }

        //----------------------------------------------------------------------------------------------------------------------

        return paymentResult;
    }

    @Override
    public PaymentResult getPaymentResultFromNotification(HttpServletRequest request){
        PaymentResult paymentResult = new PaymentResult();
        RequestToCommand requestToCommand = new RequestToCommand();
        PaymentServiceReturnCommand paymentServiceReturnCommand = requestToCommand.alipayAsyRequestToCommand(request);
        paymentResult.setPaymentStatusInformation(paymentServiceReturnCommand);
        //--------------------------------------------------------------------------------------------------------

        //卖家支 付宝账 号        String(10 0)        卖家支付宝账号，可以是 Email 或手机号码。        可空
        String sellerEmail = request.getParameter("seller_email");

        //不是我们配置的seller_email
        String configSellerEmail = configs.getProperty("param.seller_email");
        if (!configSellerEmail.equals(sellerEmail)){
            paymentResult.setPaymentServiceSatus(UNDEFINED);
            paymentResult.setMessage(Slf4jUtil.format("get seller_email:[{}], not our config:[{}]", sellerEmail, configSellerEmail));
            return paymentResult;
        }

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("seller_email is our config:[{}],bingo~~", sellerEmail);
        }

        //--------------------------------------------------------------------------------------------------------

        if (isNotifyVerifySuccess(request.getParameter("notify_id"))){
            Map<String, String> responseMap = new HashMap<>();
            RequestMapUtil.requestConvert(request, responseMap);

            responseMap.remove("sign");
            responseMap.remove("sign_type");

            String toBeSignedString = MapAndStringConvertor.getToBeSignedString(responseMap);
            String localSign = Md5Encrypt.md5(toBeSignedString + configs.getProperty("param.key"), configs.getProperty("param._input_charset"));
            String sign = request.getParameter("sign").toString();

            if (sign.equals(localSign)){
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("sign equals our sign:[{}],bingo~~", localSign);
                }

                String resultStr = request.getParameter("trade_status").toString();
                getResult(resultStr, paymentResult);
            }else{
                paymentResult.setPaymentServiceSatus(FAILURE);
                paymentResult.setMessage("failure");
            }
            paymentResult.setResponseValue(ALIPAYSUCCESS);

        }else{
            paymentResult.setPaymentServiceSatus(UNDEFINED);
            paymentResult.setMessage("alipay return value error");
            paymentResult.setResponseValue(ALIPAYFAIL);
        }

        return paymentResult;
    }

    @Override
    public PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(HttpServletRequest request){
        Map<String, String> responseMap = new HashMap<>();
        RequestMapUtil.requestConvert(request, responseMap);
        String sign = request.getParameter("sign").toString();
        String result = request.getParameter("result").toString();

        responseMap.remove("sign");
        responseMap.remove("sign_type");

        String toBeSignedString = MapAndStringConvertor.getToBeSignedString(responseMap);
        String localSign = Md5Encrypt.md5(toBeSignedString + AlipayMobileParams.KEY, configs.getProperty("param._input_charset"));

        PaymentResult paymentResult = new PaymentResult();
        // 返回函数进行加密比较
        if (sign.equals(localSign)){
            if (RequestParam.ALIPAYSUCCESS.equals(result)){
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
            }else{
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            }
        }else{
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
            paymentResult.setMessage("sign not match");
        }

        RequestToCommand requestToCommand = new RequestToCommand();
        PaymentServiceReturnForMobileCommand paymentServiceReturnForMobileCommand = requestToCommand.alipaySynRequestToCommandForMobile(request, null);
        paymentResult.setPaymentStatusInformation(paymentServiceReturnForMobileCommand);
        return paymentResult;
    }

    @Override
    public PaymentResult getPaymentResultForMobileAuthAndExecuteASY(HttpServletRequest request){
        PaymentResult paymentResult = new PaymentResult();

        try{
            Map<String, String> responseMap = new HashMap<>();
            RequestMapUtil.requestConvert(request, responseMap);

            Map<String, String> resultMap = MapAndStringConvertor.convertResultToMap(responseMap.get("notify_data"));
            String resultStr = resultMap.get("trade_status").toString();
            String notify_id = resultMap.get("notify_id").toString();

            responseMap.remove("sign");

            String toBeSignedString = "service=" + responseMap.get("service") + "&v=" + responseMap.get("v") + "&sec_id=" + responseMap.get("sec_id") + "&notify_data=" + responseMap.get("notify_data");
            String localSign = Md5Encrypt.md5(toBeSignedString + AlipayMobileParams.KEY, configs.getProperty("param._input_charset"));

            if (isNotifyVerifySuccess(notify_id)){
                String sign = request.getParameter("sign").toString();
                if (sign.equals(localSign)){
                    getResult(resultStr, paymentResult);
                }else{
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    paymentResult.setMessage("failure");
                }
                paymentResult.setResponseValue(RequestParam.ALIPAYSUCCESS);
            }else{
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
                paymentResult.setMessage("alipay return value error");
                paymentResult.setResponseValue(RequestParam.ALIPAYFAIL);
            }

            RequestToCommand requestToCommand = new RequestToCommand();
            PaymentServiceReturnForMobileCommand paymentServiceReturnForMobileCommand = requestToCommand.alipaySynRequestToCommandForMobile(request, resultMap);
            paymentResult.setPaymentStatusInformation(paymentServiceReturnForMobileCommand);
        }catch (DocumentException e){
            LOGGER.error("", e);
        }

        return paymentResult;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    @Override
    public PaymentResult closePaymentRequest(Map<String, String> parm){
        PaymentResult paymentResult = new PaymentResult();
        AlipayPaymentRequest request = new AlipayPaymentRequest();
        try{
            request.initPaymentRequestParamsCancel(configs, parm);
            _closeTrade(parm, paymentResult);
        }catch (PaymentException e){
            LOGGER.error("Alipay closePaymentRequest error: " + e.toString());
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setMessage(e.toString());
            LOGGER.error("", e);
        }
        return paymentResult;
    }

    @Override
    public boolean isSupportClosePaymentRequest(){
        return true;
    }

    @Override
    public PaymentResult getOrderInfo(Map<String, String> addition){
        PaymentResult paymentResult = new PaymentResult();
        AlipayPaymentRequest request = new AlipayPaymentRequest();
        try{
            request.initPaymentRequestParamsForQuery(configs, addition);
            queryOrderInfo(addition, paymentResult);
        }catch (PaymentException e){
            LOGGER.error("Alipay queryOrderInfo error: " + e.toString());
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setMessage(e.toString());
            LOGGER.error("", e);
        }
        return paymentResult;
    }

    /**
     * 使用 HttpURLConnection 去alipay 上面 验证 是否确实 校验成功.
     * 
     * @param notifyVerifyUrl
     *            通知验证的url
     * @return 如果获得的信息是true，则校验成功；如果获得的信息是其他，则校验失败。
     */
    public boolean isNotifyVerifySuccess(String notifyId){
        try{
            StringBuffer sb = new StringBuffer();
            sb.append(configs.getProperty("payment_gateway") + "?");
            sb.append("service=" + configs.getProperty("notify_verify")).append("&");
            sb.append("partner=" + configs.getProperty("param.partner")).append("&");
            sb.append("notify_id=" + notifyId);

            URL url = new URL(sb.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String notifyVerifyResult = bufferedReader.readLine();

            // 如果获得的信息是true，则校验成功；如果获得的信息是其他，则校验失败。
            boolean result = "true".equals(notifyVerifyResult);

            LOGGER.debug("validate notifyId,result:[{}],[{}]", result, notifyId);
            return result;
        }catch (Exception e){
            LOGGER.error("", e);
        }
        return false;
    }

    private void _closeTrade(Map<String, String> params,PaymentResult result){
        String alipayUrl = params.get("action").toString();
        params.remove("action");
        String closeTradeUrl = MapAndStringConvertor.getToBeSignedString(params);
        String url = alipayUrl + "?" + closeTradeUrl;
        String returnXML = HttpClientUtil.getHttpMethodResponseBodyAsString(url, HttpMethodType.GET);

        if (Validator.isNotNullOrEmpty(returnXML)){
            try{
                Map<String, String> resultMap = MapAndStringConvertor.convertResultToMap(returnXML);
                if ("T".equals(resultMap.get("is_success"))){
                    result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                }else{
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(resultMap.get("error"));
                }
            }catch (DocumentException e){
                result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                result.setMessage(e.toString());
                LOGGER.error("", e);
            }
        }
    }

    private void queryOrderInfo(Map<String, String> params,PaymentResult result){
        String alipayUrl = params.get("action").toString();
        params.remove("action");
        String queryUrl = MapAndStringConvertor.getToBeSignedString(params);
        String url = alipayUrl + "?" + queryUrl;
        String returnXML = HttpClientUtil.getHttpMethodResponseBodyAsString(url, HttpMethodType.GET);

        if (Validator.isNotNullOrEmpty(returnXML)){
            try{
                Map<String, String> resultMap = MapAndStringConvertor.convertResultToMap(returnXML);
                if ("T".equals(resultMap.get("is_success"))){
                    result.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                    result.setMessage(resultMap.get("trade_status"));

                    PaymentServiceReturnCommand resultCommand = new PaymentServiceReturnCommand();
                    resultCommand.setTradeNo(resultMap.get("trade_no"));
                    resultCommand.setBuyer(resultMap.get("buyer_email"));
                    resultCommand.setTradeStatus(resultMap.get("trade_status"));
                    result.setPaymentStatusInformation(resultCommand);
                }else{
                    result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    result.setMessage(resultMap.get("error"));
                }
            }catch (DocumentException e){
                result.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                result.setMessage(e.toString());
                LOGGER.error("", e);
            }
        }
    }

    public void getResult(String resultStr,PaymentResult paymentResult){
        if (ResponseParam.WAIT_BUYER_PAY.equals(resultStr)){
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.WAIT_BUYER_PAY);
            paymentResult.setMessage("WAIT_BUYER_PAY");
        }else if (ResponseParam.TRADE_CLOSED.equals(resultStr)){
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.TRADE_CLOSED);
            paymentResult.setMessage("TRADE_CLOSED");
        }else if (ResponseParam.TRADE_FINISHED.equals(resultStr)){
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
            paymentResult.setMessage("TRADE_FINISHED");
        }else if (ResponseParam.TRADE_PENDING.equals(resultStr)){
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.UNCONFIRUMED_PAYMENT_SUCCESS);
            paymentResult.setMessage("TRADE_PENDING");
        }else if (ResponseParam.TRADE_SUCCESS.equals(resultStr)){
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
            paymentResult.setMessage("TRADE_SUCCESS");
        }
    }

    /*
     * 初始化地址配置参数
     */
    public void getAddress(String paymentType){
        Properties configs = ProfileConfigUtil.findCommonPro(PaymentFactory.ALIPAYADDRESS);
        this.returnUrl = configs.getProperty(paymentType + ".return_url");
        this.notifyUrl = configs.getProperty(paymentType + ".notify_url");
        this.errorNotifyUrl = configs.getProperty(paymentType + ".error_notify_url");
    }

    @Override
    public PaymentResult unifiedOrder(Map<String, String> addition){
        PaymentResult paymentResult = new PaymentResult();
        return paymentResult;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.utilities.integration.payment.PaymentAdaptor#getCreateResponseToken()
     */
    @Override
    public PaymentRequest getCreateResponseToken(PaymentRequest paymentRequest){
        String result = HttpClientUtil.getHttpMethodResponseBodyAsStringIgnoreCharSet(paymentRequest.getRequestURL(), HttpMethodType.GET, _INPUT_CHARSET);
        Validate.notBlank(result, "result can't be blank!");

        LOGGER.info("result:{}", result);

        try{
            result = java.net.URLDecoder.decode(result, _INPUT_CHARSET);
            String[] params = result.split("&");

            //---------------------------------------------------------------------

            Map<String, String> map = new HashMap<>();
            for (String param : params){
                String key = param.split("=")[0].toString();
                String val = param.replace(param.split("=")[0].toString() + "=", "");
                map.put(key, val);
            }

            if (LOGGER.isInfoEnabled()){
                LOGGER.info("map:{}", JsonUtil.format(map));
            }

            //---------------------------------------------------------------------

            //手机WAP端授权结果
            PaymentResult paymentResult = getPaymentResultForMobileCreateDirect(map);
            PaymentServiceStatus paymentServiceSatus = paymentResult.getPaymentServiceSatus();
            if (paymentServiceSatus.equals(SUCCESS)){
                Map<String, String> resultMap = MapAndStringConvertor.convertResultToMap(map.get("res_data").toString());
                //创建一个新的MOBILE端交易请求
                paymentRequest = newPaymentRequestForMobileAuthAndExecute(resultMap);
                LOGGER.info("newPaymentRequestForMobileAuthAndExecute URL:[{}]", paymentRequest.getRequestURL());
                return paymentRequest;
            }

            String message = Slf4jUtil.format("newPaymentRequestForMobileAuthAndExecute error:[{}]", paymentResult.getMessage() + paymentServiceSatus);

            LOGGER.error(message);
            throw new RuntimeException(message);

        }catch (Exception e){
            LOGGER.error("newPaymentRequestForMobileAuthAndExecute error", e);
            throw new RuntimeException(e);
        }
    }

    private PaymentResult getPaymentResultForMobileCreateDirect(Map<String, String> resultStr){
        PaymentResult paymentResult = new PaymentResult();
        String sign = resultStr.get("sign");

        try{
            resultStr.remove("sign");
            //---------------------------------------------
            Map<String, String> resultMap = MapAndStringConvertor.convertResultToMap(resultStr.get("res_data").toString());
            String toBeSignedString = MapAndStringConvertor.getToBeSignedString(resultStr);
            String localSign = Md5Encrypt.md5(toBeSignedString + AlipayMobileParams.KEY, configs.getProperty("param._input_charset"));

            // 返回函数进行加密比较
            if (localSign.equals(sign)){
                if (null == resultMap.get("request_token")){
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    paymentResult.setMessage(resultMap.get("code") + "/" + resultMap.get("detail"));
                }else{
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                    paymentResult.setMessage(resultMap.get("request_token"));
                }
            }else{
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
                paymentResult.setMessage("sign not match");
            }
        }catch (Exception e){
            LOGGER.error("", e);
        }

        return paymentResult;
    }

    //--------------------------------------------------------------------------------------------------------------------------

    @Override
    public abstract String getServiceProvider();

    @Override
    public String getServiceType(){
        return "jishidaozhang";
    }

    @Override
    public String getServiceVersion(){
        return "4.0";
    }

    public String getPayMethod(){
        return payMethod;
    }

    public void setPayMethod(String payMethod){
        this.payMethod = payMethod;
    }

    public String getIsDefault_login(){
        return isDefault_login;
    }

    public void setIsDefault_login(String isDefault_login){
        this.isDefault_login = isDefault_login;
    }

}
