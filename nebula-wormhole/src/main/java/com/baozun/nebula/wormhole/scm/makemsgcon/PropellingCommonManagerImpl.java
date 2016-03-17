package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.wormhole.utils.JacksonUtils;

/**
 * 推送公共相关的方法实现类
 * 
 * @author lihao_mamababa
 * 
 */
@Service("propellingCommonManager")
public class PropellingCommonManagerImpl implements PropellingCommonManager {

	private static Logger logger = LoggerFactory.getLogger(PropellingCommonManagerImpl.class);

	@Autowired
	private SdkMsgManager sdkMsgManager;

	@Override
	public MsgSendContent saveMsgBody(Object obj, Long id) {

		try {
			String josnString = JacksonUtils.getObjectMapper().writeValueAsString(obj);
			String aesString = EncryptUtil.getInstance().encrypt(josnString);// 加密后的内容值
			MsgSendContent msc = new MsgSendContent();
			msc.setId(id);
			msc.setMsgBody(aesString);
			MsgSendContent msgSendContend = sdkMsgManager.findMsgSendContentById(id);
			if(null == msgSendContend){
				sdkMsgManager.saveMsgSendContent(msc);
			}
			return msc;

		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (EncryptionException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
