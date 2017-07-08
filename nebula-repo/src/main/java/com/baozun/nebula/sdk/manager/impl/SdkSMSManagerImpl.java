/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
package com.baozun.nebula.sdk.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.hub.sdk.api.SmsService;
import com.baozun.hub.sdk.command.invoice2notice.SmsCommand;
import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.dao.system.SmsSendLogDao;
import com.baozun.nebula.model.system.SmsSendLog;
import com.baozun.nebula.sdk.manager.SdkSMSManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * @author D.C
 * @time 2016年3月29日 下午5:59:59
 */
@Transactional
@Service("sdkSMSManager")
public class SdkSMSManagerImpl implements SdkSMSManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(SdkSMSManagerImpl.class);

    @Autowired
    private SmsSendLogDao smsSendLogDao;

    @Override
    public SendResult send(SMSCommand smsCommand){
        //构建hub接口需要的SmsCommand
        SmsCommand sms = buildSmsCommand(smsCommand);

        if (LOGGER.isDebugEnabled()){
            //TODO mobile没有mask
            LOGGER.debug("[SEND_SMS_BEGIN]  param : {}", JsonUtil.format(sms));
        }

        String result = SmsService.send(sms);

        LOGGER.info("[SEND_SMS_END]  result : {}", result);

        Map<String, Object> map = JsonUtil.toMap(result);
        Map<String, String> resultMap = new HashMap<String, String>();
        Set<Entry<String, Object>> entrySet = map.entrySet();
        for (Entry<String, Object> entry : entrySet){
            resultMap.put(entry.getKey(), entry.getValue().toString());
        }

        //记录日志
        saveSmsSendLog(resultMap, sms);

        return getSendResult(resultMap);
    }

    /**
     * 构建hub接口的入参SmsCommand
     * 
     * @param smsCommand
     * @return
     */
    private SmsCommand buildSmsCommand(SMSCommand smsCommand){
        SmsCommand sms = new SmsCommand();
        PropertyUtil.copyProperties(sms, smsCommand, "mobile", "templateCode");
        sms.setDataMap(smsCommand.getVars());
        // 客户编码，找hub要
        sms.setCustomerCode(ProfileConfigUtil.findPro("config/metainfo.properties").getProperty("sms.customer.code"));
        return sms;
    }

    /**
     * 记录日志
     * 
     * @param resultMap
     * @param smsCommand
     */
    private void saveSmsSendLog(Map<String, String> resultMap,SmsCommand smsCommand){
        //记录日志
        SmsSendLog sendLog = new SmsSendLog();
        PropertyUtil.copyProperties(sendLog, smsCommand, "mobile", "templateCode");
        sendLog.setResultCode(resultMap.get("code"));
        sendLog.setSendTime(new Date());
        if (Validator.isNotNullOrEmpty(resultMap.get("logId"))){
            sendLog.setLogId(Long.valueOf(resultMap.get("logId")));
        }
        smsSendLogDao.save(sendLog);
    }

    /**
     * 构建返回值
     * 
     * @param resultMap
     *            {"code":,"msg":"","logId":""}
     *            code可选值
     *            0（成功）
     *            1（失败或其它）
     *            30001（输入参数不正确）
     *            30002 （没找到对应服务）
     *            30003 （没找到对应模板）
     *            30004 （无权访问）
     *            30005 （发送超额）
     *            30006 （重复发送短信）
     * @return SendResult
     */
    private SendResult getSendResult(Map<String, String> resultMap){

        SendResult sendResult;
        if (Validator.isNullOrEmpty(resultMap)){
            sendResult = SendResult.ERROR;
        }else{
            switch (resultMap.get("code")) {
                case "0":
                    sendResult = SendResult.SUCESS;
                    break;
                case "1":
                    sendResult = SendResult.FAILURE;
                    break;
                case "30001":
                    sendResult = SendResult.INVALIDATE_PARAM;
                    break;
                default:
                    sendResult = SendResult.FAILURE;
                    break;
            }
        }

        return sendResult;
    }
}
