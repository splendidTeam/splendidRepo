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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.dao.system.SMSTemplateDao;
import com.baozun.nebula.manager.VelocityManager;
import com.baozun.nebula.model.system.SMSTemplate;
import com.baozun.nebula.sdk.manager.SdkSMSManager;

/**
 * @author D.C
 * @time 2016年3月29日 下午5:59:59
 */
@Transactional
@Service("sdkSMSManager")
public class SdkSMSManagerImpl implements SdkSMSManager {
	@Autowired
	SMSTemplateDao smsTemplateDao;
	
	@Autowired
	VelocityManager velocityManager;

	@Override
	public SendResult send(SMSCommand smsCommand){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("code", smsCommand.getTemplateCode());
		//TODO cache & dao method
		List<SMSTemplate> smsTemplateList = smsTemplateDao.findEffectSMSTemplateListByQueryMap(paramMap);
		if (smsTemplateList != null && smsTemplateList.size() > 0) {
			SMSTemplate et = smsTemplateList.get(0);
			String content = velocityManager.parseVMContent(et.getBody(), smsCommand.getVars());
			System.out.println("the message is being sended");
			System.out.println("the mobileNumber is" + smsCommand.getMobile());
			System.out.println("the message content is" + content);
			System.out.println("the message is sended");
			// 记录日志
			//SMSSendLog sendLog = generateMailLog(et.getId(), receiverEmail);
			//smsSendLogDao.save(sendLog);
		}
		return SendResult.SUCESS;
	}

}
