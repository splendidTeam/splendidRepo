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
package com.baozun.nebula.utilities.integration.payment.wechat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.WechatUtil;
import com.baozun.nebula.utilities.common.XMLUtils;
import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentRequest;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.PaymentUtil;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatResponseKeyConstants.TradeStateValue;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.util.SortUtil.sortMapByKeyAsc;

public abstract class AbstractWechatPaymentAdaptor implements PaymentAdaptor{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWechatPaymentAdaptor.class);

    @Override
    public PaymentResult getPaymentResultFromNotification(HttpServletRequest request){
        String requestBody = null;
        try{
            requestBody = WechatUtil.getRequestBody(request);
        }catch (IOException e){
            LOGGER.error("", e);
            throw new RuntimeException(e);
        }

        //---------------------------------------------------------------------

        Map<String, String> responseMap = XMLUtils.parserXml(requestBody);
        Validate.notEmpty(responseMap, "responseMap can't be null/empty!");

        if (LOGGER.isInfoEnabled()){
            LOGGER.info(JsonUtil.format(sortMapByKeyAsc(responseMap)));
        }

        PaymentResult paymentResult = new PaymentResult();
        //---------------------------------------------------------------------

        if ("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RETURN_CODE))){
            if ("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RESULT_CODE))){
                Set<String> excludes = new HashSet<String>();
                excludes.add(WechatResponseKeyConstants.SIGN);
                String resSign = WechatUtil.makeSign(responseMap, excludes, WechatConfig.PAY_SIGN_KEY);

                //验证签名
                if (!responseMap.get(WechatResponseKeyConstants.SIGN).equals(resSign)){
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    paymentResult.setMessage("sign not match");
                    LOGGER.error("getPaymentResultFromNotification sign not match, parameter sign : {}, local sign : {}", responseMap.get(WechatResponseKeyConstants.SIGN), resSign);
                    return paymentResult;
                }

                String partner = responseMap.get(WechatResponseKeyConstants.MCH_ID);// 商户号

                // 验证商户号是否一致
                if (!WechatConfig.PARTNER_ID.equals(partner)){
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    paymentResult.setMessage("partner not match");
                    LOGGER.error("getPaymentResultFromNotification partner not match, parameter partner : {}, local partner : {}", partner, WechatConfig.PARTNER_ID);
                    return paymentResult;
                }

                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.PAYMENT_SUCCESS);
                paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));

            }else{
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.ERR_CODE));
                LOGGER.error(
                                "getPaymentResultFromNotification failure , result_code : {}, err_code : {},err_code_des : {}",
                                responseMap.get(WechatResponseKeyConstants.RESULT_CODE),
                                responseMap.get(WechatResponseKeyConstants.ERR_CODE),
                                responseMap.get(WechatResponseKeyConstants.ERR_CODE_DES));
            }

        }else{
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
            LOGGER.error("getPaymentResultFromNotification failure , return_code : {}, return_msg : {}", responseMap.get(WechatResponseKeyConstants.RETURN_CODE), responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
        }

        //------------------------------------------------------------------

        String subOrdinate = responseMap.get(WechatResponseKeyConstants.OUT_TRADE_NO);
        String billNo = responseMap.get(WechatResponseKeyConstants.TRANSACTION_ID);//微信支付订单号

        //------------------------------------------------------------------

        PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
        paymentServiceReturnCommand.setReturnMsg(responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
        paymentServiceReturnCommand.setOrderNo(subOrdinate);
        paymentServiceReturnCommand.setTradeNo(billNo);
        paymentServiceReturnCommand.setBuyer(responseMap.get(WechatResponseKeyConstants.OPENID));

        //since 5.3.2.18
        paymentServiceReturnCommand.setTotalFee(PaymentUtil.toYuanString(responseMap.get(WechatResponseKeyConstants.TOTAL_FEE)));
        paymentResult.setPaymentStatusInformation(paymentServiceReturnCommand);

        return paymentResult;
    }

    //------------------------------------------------------------------

    @Override
    public PaymentResult closePaymentRequest(Map<String, String> parm){
        Validate.notEmpty(parm, "parm can't be null/empty!");

        if (LOGGER.isInfoEnabled()){
            LOGGER.info(JsonUtil.format(sortMapByKeyAsc(parm)));
        }

        //---------------------------------------------------------------------
        String sign = WechatUtil.makeSign(parm, null, WechatConfig.PAY_SIGN_KEY);
        parm.put(WechatResponseKeyConstants.SIGN, sign);

        String xmlInfo = WechatUtil.getXmlInfo(parm);
        String response = WechatUtil.post(WechatConfig.CLOSE_ORDER_URL, xmlInfo);
        Set<String> excludes = new HashSet<String>();
        excludes.add(WechatResponseKeyConstants.SIGN);
        Map<String, String> responseMap = XMLUtils.parserXml(response);

        if (LOGGER.isInfoEnabled()){
            LOGGER.info(JsonUtil.format(sortMapByKeyAsc(responseMap)));
        }
        //---------------------------------------------------------------------

        PaymentResult paymentResult = new PaymentResult();
        if ("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RETURN_CODE))){
            if ("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RESULT_CODE))){
                String resSign = WechatUtil.makeSign(responseMap, excludes, WechatConfig.PAY_SIGN_KEY);

                if (responseMap.get(WechatResponseKeyConstants.SIGN).equals(resSign)){
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                    paymentResult.setMessage(PaymentServiceStatus.SUCCESS.toString());
                }else{
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    paymentResult.setMessage("sign not match");
                    LOGGER.error("closePaymentRequest sign not match, parameter sign : {}, local sign : {}", responseMap.get(WechatResponseKeyConstants.SIGN), resSign);

                }

            }else{
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.ERR_CODE));
                LOGGER.error(
                                "closePaymentRequest failure , result_code : {}, err_code : {},err_code_des : {}",
                                responseMap.get(WechatResponseKeyConstants.RESULT_CODE),
                                responseMap.get(WechatResponseKeyConstants.ERR_CODE),
                                responseMap.get(WechatResponseKeyConstants.ERR_CODE_DES));

            }

        }else{
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
            LOGGER.error("closePaymentRequest failure , return_code : {}, return_msg : {}", responseMap.get(WechatResponseKeyConstants.RETURN_CODE), responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
        }
        return paymentResult;
    }

    //------------------------------------------------------------------

    @Override
    public PaymentResult getOrderInfo(Map<String, String> addition){
        Validate.notEmpty(addition, "addition can't be null/empty!");

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(sortMapByKeyAsc(addition)));
        }

        //---------------------------------------------------------------------
        String sign = WechatUtil.makeSign(addition, null, WechatConfig.PAY_SIGN_KEY);

        addition.put(WechatResponseKeyConstants.SIGN, sign);

        String xmlInfo = WechatUtil.getXmlInfo(addition);
        String response = WechatUtil.post(WechatConfig.ORDER_QUERY_URL, xmlInfo);

        Set<String> excludes = new HashSet<String>();
        excludes.add(WechatResponseKeyConstants.SIGN);

        Map<String, String> responseMap = XMLUtils.parserXml(response);

        if (LOGGER.isInfoEnabled()){
            LOGGER.info(JsonUtil.format(sortMapByKeyAsc(responseMap)));
        }

        //---------------------------------------------------------------------

        PaymentResult paymentResult = new PaymentResult();
        if ("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RETURN_CODE))){
            if ("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RESULT_CODE))){
                String resSign = WechatUtil.makeSign(responseMap, excludes, WechatConfig.PAY_SIGN_KEY);

                if (responseMap.get(WechatResponseKeyConstants.SIGN).equals(resSign)){
                    /**
                     * trade_state为交易状态
                     * 追加判断trade_state为SUCCESS
                     * 
                     * @author yaohua.wang@baozun.com
                     * @since 5.3.2.20
                     */
                    String trade_state = responseMap.get(WechatResponseKeyConstants.TRADE_STATE);
                    if (Validator.isNullOrEmpty(trade_state) && TradeStateValue.SUCCESS.equals(trade_state)){
                        paymentResult.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                        paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.TRADE_STATE));
                    }else{
                        paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                        paymentResult.setMessage("TRADE_STATE is not SUCCESS ; TRADE_STATE value is " + trade_state + ".");
                    }
                }else{
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    paymentResult.setMessage("sign not match");
                    LOGGER.error("orderquery sign not match, parameter sign : {}, local sign : {}", responseMap.get(WechatResponseKeyConstants.SIGN), resSign);

                }

            }else{
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.ERR_CODE));
                LOGGER.error(
                                "orderquery failure , result_code : {}, err_code : {},err_code_des : {}",
                                responseMap.get(WechatResponseKeyConstants.RESULT_CODE),
                                responseMap.get(WechatResponseKeyConstants.ERR_CODE),
                                responseMap.get(WechatResponseKeyConstants.ERR_CODE_DES));

            }
        }else{
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
            LOGGER.error("orderquery failure , return_code : {}, return_msg : {}", responseMap.get(WechatResponseKeyConstants.RETURN_CODE), responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
        }

        PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
        paymentServiceReturnCommand.setOrderNo(responseMap.get(WechatResponseKeyConstants.OUT_TRADE_NO));
        paymentServiceReturnCommand.setTradeNo(responseMap.get(WechatResponseKeyConstants.TRANSACTION_ID));

        paymentResult.setPaymentStatusInformation(paymentServiceReturnCommand);
        return paymentResult;
    }

    @Override
    public PaymentResult unifiedOrder(Map<String, String> addition){
        Validate.notEmpty(addition, "addition can't be null/empty!");

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("addition info:{}", JsonUtil.format(sortMapByKeyAsc(addition)));
        }

        //---------------------------------------------------------------------
        String sign = WechatUtil.makeSign(addition, null, WechatConfig.PAY_SIGN_KEY);

        addition.put(WechatResponseKeyConstants.SIGN, sign);
        String xmlInfo = WechatUtil.getXmlInfo(addition);
        // 调用微信的统一下单接口
        // @see http://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
        String response = WechatUtil.post(WechatConfig.PAY_UNIFIED_ORDER_URL, xmlInfo);
        LOGGER.debug("response xml info: [{}]", response);

        //---------------------------------------------------------------------
        Map<String, String> responseMap = XMLUtils.parserXml(response);
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("responseMap:[{}]", JsonUtil.format(sortMapByKeyAsc(responseMap)));
        }

        Set<String> excludes = new HashSet<String>();
        excludes.add(WechatResponseKeyConstants.SIGN);

        //---------------------------------------------------------------------
        PaymentResult paymentResult = new PaymentResult();
        if ("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RETURN_CODE))){
            if ("SUCCESS".equals(responseMap.get(WechatResponseKeyConstants.RESULT_CODE))){
                String resSign = WechatUtil.makeSign(responseMap, excludes, WechatConfig.PAY_SIGN_KEY);

                if (responseMap.get(WechatResponseKeyConstants.SIGN).equals(resSign)){
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
                    paymentResult.setMessage(String.valueOf(PaymentServiceStatus.SUCCESS));
                    //封装返回参数
                    getReturnCommand(paymentResult, responseMap);
                }else{
                    paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                    paymentResult.setMessage("sign not match");
                    LOGGER.error("unifiedOrder sign not match, parameter sign : {}, local sign : {}", responseMap.get(WechatResponseKeyConstants.SIGN), resSign);
                }

            }else{
                paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
                paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.ERR_CODE));
                LOGGER.error(
                                "unifiedOrder failure , result_code : {}, err_code : {},err_code_des : {}",
                                responseMap.get(WechatResponseKeyConstants.RESULT_CODE),
                                responseMap.get(WechatResponseKeyConstants.ERR_CODE),
                                responseMap.get(WechatResponseKeyConstants.ERR_CODE_DES));
                //封装返回参数
                getReturnCommand(paymentResult, responseMap);
            }

        }else{
            paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
            paymentResult.setMessage(responseMap.get(WechatResponseKeyConstants.RETURN_CODE));
            LOGGER.error("unifiedOrder failure , return_code : {}, return_msg : {}", responseMap.get(WechatResponseKeyConstants.RETURN_CODE), responseMap.get(WechatResponseKeyConstants.RETURN_MSG));
        }

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(paymentResult));
        }

        return paymentResult;
    }

    private void getReturnCommand(PaymentResult paymentResult,Map<String, String> resMap){

        PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.APPID))){
            paymentServiceReturnCommand.setAppId(resMap.get(WechatResponseKeyConstants.APPID));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.MCH_ID))){
            paymentServiceReturnCommand.setMchId(resMap.get(WechatResponseKeyConstants.MCH_ID));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.DEVICE_INFO))){
            paymentServiceReturnCommand.setDeviceInfo(resMap.get(WechatResponseKeyConstants.DEVICE_INFO));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.NONCE_STR))){
            paymentServiceReturnCommand.setNonceStr(resMap.get(WechatResponseKeyConstants.NONCE_STR));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.SIGN))){
            paymentServiceReturnCommand.setSign(resMap.get(WechatResponseKeyConstants.SIGN));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.RESULT_CODE))){
            paymentServiceReturnCommand.setResultCode(resMap.get(WechatResponseKeyConstants.RESULT_CODE));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.ERR_CODE))){
            paymentServiceReturnCommand.setErrCode(resMap.get(WechatResponseKeyConstants.ERR_CODE));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.ERR_CODE_DES))){
            paymentServiceReturnCommand.setErrCodeDes(resMap.get(WechatResponseKeyConstants.ERR_CODE_DES));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.TRADE_TYPE))){
            paymentServiceReturnCommand.setTradeType(resMap.get(WechatResponseKeyConstants.TRADE_TYPE));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.PREPAY_ID))){
            paymentServiceReturnCommand.setPrepayId(resMap.get(WechatResponseKeyConstants.PREPAY_ID));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.CODE_URL))){
            paymentServiceReturnCommand.setCodeUrl(resMap.get(WechatResponseKeyConstants.CODE_URL));
        }
        if (StringUtils.isNotBlank(resMap.get(WechatResponseKeyConstants.MWEB_URL))){
            paymentServiceReturnCommand.setMwebUrl(resMap.get(WechatResponseKeyConstants.MWEB_URL));
        }
        paymentResult.setPaymentStatusInformation(paymentServiceReturnCommand);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.utilities.integration.payment.PaymentAdaptor#getCreateResponseToken()
     */
    @Override
    public PaymentRequest getCreateResponseToken(PaymentRequest paymentRequest){
        return paymentRequest;
    }

    //-------------------------------------------------------------------------------------

    @Override
    public String getServiceProvider(){
        return null;
    }

    @Override
    public String getServiceType(){
        return null;
    }

    @Override
    public String getServiceVersion(){
        return null;
    }

    @Override
    public PaymentRequest newPaymentRequest(String httpType,Map<String, String> addition){
        return null;
    }

    @Override
    public PaymentRequest newPaymentRequestForMobileCreateDirect(Map<String, String> addition){
        return null;
    }

    @Override
    public PaymentResult getPaymentResult(HttpServletRequest request){
        return null;
    }

    @Override
    public PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(HttpServletRequest request){
        return null;
    }

    @Override
    public PaymentResult getPaymentResultForMobileAuthAndExecuteASY(HttpServletRequest request){
        return null;
    }

    //------------------------------------------------------------------

    @Override
    public boolean isSupportClosePaymentRequest(){
        return true;
    }

}
