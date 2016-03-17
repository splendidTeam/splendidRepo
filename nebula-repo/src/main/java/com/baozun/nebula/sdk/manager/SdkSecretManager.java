package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.manager.BaseManager;

/**
 * 用于个人隐私信息进行加密解密处理
 * @author Justin Hu
 *
 */
public interface SdkSecretManager extends BaseManager{

	Object encrypt(Object source,String[] fields);
	
	Object decrypt(Object source,String[] fields);
}
