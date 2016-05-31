package com.baozun.nebula.exception;

/**
 * code 重复
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 Jul 10, 2013 10:02:45 AM
 */
public class CodeRepeatException extends BusinessException{

	private static final long	serialVersionUID	= 665112595508596601L;

	public CodeRepeatException(int errorCode){
		super(errorCode);
	}

	public CodeRepeatException(String message){
		super(message);
	}

	public CodeRepeatException(int errorCode, Object[] args){
		super(errorCode, args);
	}

	public CodeRepeatException(int errorCode, String message){
		super(errorCode, message);
	}

}
