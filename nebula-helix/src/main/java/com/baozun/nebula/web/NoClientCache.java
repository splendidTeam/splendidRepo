package com.baozun.nebula.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 表示在会输出到client时，会在response header中加上nocache的标识
 * 用于穿透cdn,强制要求浏览器不缓存
 * @author Justin Hu
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoClientCache{

	boolean value() default true;

}
