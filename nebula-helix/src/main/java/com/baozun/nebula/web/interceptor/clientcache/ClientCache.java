/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.interceptor.clientcache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.feilong.core.TimeInterval;

/**
 * (为了提高速度一些浏览器会缓存浏览者浏览过的页面,通过下面的相关参数的定义,浏览器一般不会缓存页面,而且浏览器无法脱机浏览.<br>
 * 借鉴了 胡总的 {@link "NoClientCache"}
 * <p>
 * {@link #value()} 参数优先级最高
 * </p>
 * 
 * <h3>关于springmvc cache:</h3>
 * 
 * <blockquote>
 * <p>
 * springmvc cache 参见 {@link org.springframework.web.servlet.support.WebContentGenerator},此外可以参考 {@link ShallowEtagHeaderFilter}
 * </p>
 * </blockquote>
 *
 * @author feilong
 * @version 1.0.9 2015年3月30日 下午4:25:10
 * @since 1.0.9
 */
//表示产生文档,比如通过javadoc产生文档, 将此注解包含在 javadoc 中, 这个Annotation可以被写入javadoc
//在默认情况下，注释 不包括在 Javadoc 中
@Documented
//在jvm加载class时候有效, VM将在运行期也保留注释,因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)

//仅用于 Method 
@Target({ ElementType.METHOD })
public @interface ClientCache{

    /**
     * 过期时间 = max-age 属性,单位<span style="color:red">秒</span>. <br>
     * 举例:
     * Cache-Control: max-age=3600
     * 
     * 只需要设置 <code>@ClientCache(3600)</code>
     * <p>
     * if value <=0 表示不缓存<br>
     * 默认:0 不缓存
     * </p>
     * 
     * 设置为int类型,int 最大值是{@link Integer#MAX_VALUE} 为 68.096259734906,参见 {@link TimeInterval} ,绝对够用了
     *
     * @return the int
     * @since 1.0.9
     */
    int value() default 0;

}
