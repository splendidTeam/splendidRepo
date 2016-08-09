package com.baozun.nebula.sdk.manager.impl;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.manager.SdkSecretManager;
import com.baozun.nebula.utilities.common.EncryptUtil;

@Service("sdkSecretManager")
@Transactional
public class SdkSecretManagerImpl implements SdkSecretManager {
	
	/**
	 * 默认开启加密解密
	 * @return
	 */
	private boolean needEncrypt(){
		return true;
	}
	
	@Override
	public Object encrypt(Object source, String[] fields) {
		if(!needEncrypt()) return source;
		
		try{
			for(String field:fields){
				String pro=BeanUtils.getProperty(source, field);
				
				if(StringUtils.isBlank(pro)) continue;
				
				pro =EncryptUtil.getInstance().encrypt(pro);
				BeanUtils.setProperty(source, field, pro);
			}
			
			return source;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Object decrypt(Object source, String[] fields) {
		if(!needEncrypt()) return source;
		
		try{
			for(String field:fields){
				String pro=BeanUtils.getProperty(source, field);
				
				if(StringUtils.isBlank(pro)) continue;
				
				pro =EncryptUtil.getInstance().decrypt(pro);
				BeanUtils.setProperty(source, field, pro);
			}
			
			return source;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
