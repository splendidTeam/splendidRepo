package com.baozun.nebula.wormhole.scm.timing;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.wormhole.utils.MsgUtils;

/**
 * 推送公共相关的实现类
 * 
 * @author lihao_mamababa
 * 
 */
@Service("syncCommonManager")
public class SyncCommonManagerImpl implements SyncCommonManager {

	private static Logger logger = LoggerFactory.getLogger(SyncCommonManagerImpl.class);

	@Override
	public <T> List<T> queryMsgBody(String msgBody, Class<T> cls) {

		try {
			String aesString = EncryptUtil.getInstance().decrypt(msgBody);// 解密后的内容值
			return MsgUtils.jsonToList(aesString, cls);
		} catch (EncryptionException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

}
