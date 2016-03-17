package com.baozun.nebula.sdk.manager.impl;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkSecretManager;
import com.baozun.nebula.utilities.common.EncryptUtil;

@Service("sdkSecretManager")
@Transactional
public class SdkSecretManagerImpl implements SdkSecretManager {

	
	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;
	
	/**
	 * 是否需要加密解密
	 * @return
	 */
	private boolean needEncrypt(){
		
		String value=sdkMataInfoManager.findValue(MataInfo.KEY_NEED_ENCRYPT_PERSON);
		
		if(value!=null&&value.equalsIgnoreCase("true")){
			return true;
			
		}
		
		return false;
	}
	
	@Override
	public Object encrypt(Object source, String[] fields) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
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
