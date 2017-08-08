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
package com.baozun.nebula.web.eventlistener;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.event.AbstractNebulaEventListener;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.member.event.RegisterSuccessEvent;

/**
 * 注册成功监听器
 * 
 * @author Viktor Huang
 * @author D.C
 */
public class RegisterSuccessEventListener extends AbstractNebulaEventListener<RegisterSuccessEvent>{

    private static final Logger LOG = LoggerFactory.getLogger(RegisterSuccessEventListener.class);

    @Autowired
    private EmailTemplateManager emailTemplateManager;

    @Override
    public void onApplicationEvent(RegisterSuccessEvent event){

        MemberDetails memberDetails = (MemberDetails) event.getSource();

        LOG.info("[MEM_REGISTER_SUCCESS] {} [{}] \"\"", memberDetails.getLoginName(), new Date());
        this.handler(memberDetails, event.getClientContext());
    }

    /**
     * 可扩展的处理器
     * 
     * @param source
     * @param context
     */
    protected void handler(MemberDetails source,Map<String, String> context){
        // TODO 会员分组处理
        // TODO 激活邮件
        // emailTemplateManager.sendEmail(event.getReceiverEmail(), event.getCode(), event.getDataMap());
    }
}
