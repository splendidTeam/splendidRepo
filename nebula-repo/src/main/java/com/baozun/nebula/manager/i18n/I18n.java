package com.baozun.nebula.manager.i18n;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
* @Description: 需要国际化的类
* @author 何波
* @date 2014年11月19日 上午10:53:14 
*
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface I18n {
}
