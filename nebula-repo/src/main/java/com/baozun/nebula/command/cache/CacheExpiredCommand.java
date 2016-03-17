package com.baozun.nebula.command.cache;

import java.io.Serializable;

/**
 * 用于Map中过期的Object
 * @author Justin Hu
 *
 * @param <T>
 */
public class CacheExpiredCommand <T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3223292566286383651L;

	
	private T object;

	/**
	 * 过期时间
	 */
	private long expiredTime;

	public T getObject() {
		return object;
	}


	public void setObject(T object) {
		this.object = object;
	}


	public long getExpiredTime() {
		return expiredTime;
	}


	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	
}
