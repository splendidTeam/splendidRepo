/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.shoppingcart.resolver;

import org.apache.commons.collections4.Closure;

import com.feilong.core.bean.PropertyUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 * @param <V>
 * @since 5.3.2.14
 * 
 * @see org.apache.commons.beanutils.BeanPropertyValueChangeClosure
 */
public class BeanPropertyValueChangeClosure<T> implements Closure<T>{

    private String propertyName;

    private Object value;

    /**
     * @param propertyName
     * @param value
     */
    public BeanPropertyValueChangeClosure(String propertyName, Object value){
        super();
        this.propertyName = propertyName;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Closure#execute(java.lang.Object)
     */
    @Override
    public void execute(T input){
        PropertyUtil.setProperty(input, propertyName, value);
    }
}
