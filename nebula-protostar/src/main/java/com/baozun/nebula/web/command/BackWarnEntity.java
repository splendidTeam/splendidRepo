package com.baozun.nebula.web.command;

import java.io.Serializable;

/**
 * 返回提示.
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 2010-6-24 上午03:14:56
 * @since 1.0
 */
public class BackWarnEntity implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 1L;

	/** 是否成功. */
	private boolean				isSuccess;

	/**
	 * 错误码,某些时候需要 设置 ,而前台判断(可选)
	 */
	private Integer				errorCode			= null;

	/** 描述. */
	private Object				description;

	/**
	 * Instantiates a new back warn entity.
	 */
	public BackWarnEntity(){}

	/**
	 * Instantiates a new back warn entity.
	 * 
	 * @param isSuccess
	 *            the is success
	 * @param description
	 *            the description
	 */
	public BackWarnEntity(boolean isSuccess, Object description){
		this.isSuccess = isSuccess;
		this.description = description;
	}

	/**
	 * Gets the 是否成功.
	 * 
	 * @return the isSuccess
	 */
	public boolean getIsSuccess(){
		return isSuccess;
	}

	/**
	 * Sets the 是否成功.
	 * 
	 * @param isSuccess
	 *            the isSuccess to set
	 */
	public void setIsSuccess(boolean isSuccess){
		this.isSuccess = isSuccess;
	}

	/**
	 * Gets the 描述.
	 * 
	 * @return the description
	 */
	public Object getDescription(){
		return description;
	}

	/**
	 * Sets the 描述.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(Object description){
		this.description = description;
	}

	/**
	 * @return the errorCode
	 */
	public Integer getErrorCode(){
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(Integer errorCode){
		this.errorCode = errorCode;
	}

}
