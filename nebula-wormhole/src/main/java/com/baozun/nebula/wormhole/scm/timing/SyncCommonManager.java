package com.baozun.nebula.wormhole.scm.timing;

import java.util.List;

/**
 * 推送公共相关的方法
 * 
 * @author Justin Hu
 * 
 */
public interface SyncCommonManager {

	/**
	 * 将接收到的消息正文，通过aes解密，并将json串转成所需要的对象列表
	 * 
	 * @param <T>
	 * @param msgBody
	 * @param cls
	 *            具体对象类型
	 * @return
	 */
	public <T> List<T> queryMsgBody(String msgBody, Class<T> cls);
}
