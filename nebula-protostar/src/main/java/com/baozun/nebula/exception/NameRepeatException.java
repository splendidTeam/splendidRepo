package com.baozun.nebula.exception;

/**
 * name 重复
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 Jul 10, 2013 10:02:45 AM
 */
public class NameRepeatException extends BusinessException{

	private static final long	serialVersionUID	= 665112595508596601L;

	public NameRepeatException(int errorCode){
		super(errorCode);
	}

	public NameRepeatException(String message){
		super(message);
	}

	public NameRepeatException(int errorCode, Object[] args){
		super(errorCode, args);
	}

	public NameRepeatException(int errorCode, String message){
		super(errorCode, message);
	}

}
