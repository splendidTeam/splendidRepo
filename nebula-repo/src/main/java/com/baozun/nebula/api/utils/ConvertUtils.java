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
package com.baozun.nebula.api.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;

import com.feilong.core.bean.PropertyUtil;

/**
 * 转换工具类 复制源对象的属性到目标对象 (如果源对象与目标对象的属性类型不同,则可能会抛出异常,返回空对象)
 * 
 * @author chuanyang.zheng
 * 
 */
public class ConvertUtils{

    /**
     * 
     * 
     * @deprecated since 5.3.2.22 直接使用 PropertyDescriptor 没有缓存,性能不高, 该方法 可以使用 {@link PropertyUtil#copyProperties(Object, Object, String...)}
     */
    @Deprecated
    public static Object convertTwoObject(Object target,Object sourse){
        if (sourse == null){
            return null;
        }
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(sourse.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds){
                Method getMethod = pd.getReadMethod();
                if (getMethod.invoke(sourse) != null){
                    BeanUtils.setProperty(target, pd.getName(), getMethod.invoke(sourse));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return target;
    }

    public static Object convertModelToApi(Object target,Object sourse){
        if (sourse == null){
            return null;
        }
        try{
            BeanUtils.copyProperties(target, sourse);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return target;
    }

    /**
     * 
     * 
     * @deprecated since 5.3.2.22 直接使用 PropertyDescriptor 没有缓存, 性能不高, 该方法 可以使用 {@link PropertyUtil#copyProperties(Object, Object, String...)}
     */
    @Deprecated
    public static Object convertFromTarget(Object target,Object sourse){
        if (target == null){
            return null;
        }
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(sourse.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds){
                Method getMethod = pd.getReadMethod();
                try{
                    if (getMethod.invoke(sourse) != null){
                        BeanUtils.setProperty(target, pd.getName(), getMethod.invoke(sourse));
                    }else{
                        BeanUtils.setProperty(target, pd.getName(), null);
                    }
                }catch (Exception e){
                    continue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return target;
    }

}
