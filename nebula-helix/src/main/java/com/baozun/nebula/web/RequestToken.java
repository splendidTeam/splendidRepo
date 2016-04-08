package com.baozun.nebula.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface RequestToken{

	/**
	 * 表示生成token
	 */
	public static final String TYPE_MAKE="make";
	
	/**
	 * 表示检查token
	 */
	public static final String TYPE_CHECK="check";
	
	String value() default TYPE_MAKE;


}
