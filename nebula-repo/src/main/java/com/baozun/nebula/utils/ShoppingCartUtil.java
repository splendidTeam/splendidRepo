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
package com.baozun.nebula.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;

/**
 * 购物车工具类.
 *
 * @author feilong
 * @version 5.3.1 2016年5月24日 下午6:17:20
 * @since 5.3.1
 */
public final class ShoppingCartUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartUtil.class);

    /** Don't let anyone instantiate this class. */
    private ShoppingCartUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 计算整单商品数量.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart lines
     * @return the order quantity
     */
    public static int getSumQuantity(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        return Validator.isNullOrEmpty(shoppingCartLineCommandList) ? 0
                        : CollectionsUtil.sum(shoppingCartLineCommandList, "quantity").intValue();
    }

    /**
     * 根据购物车行获取ItemForCheckCommand集合.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @return the item combo ids
     */
    public static Set<String> getItemComboIds(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        Set<Set<String>> comboIds = CollectionsUtil.getPropertyValueSet(shoppingCartLineCommandList, "comboIds");
        return toPropertyValueSet(comboIds);
    }

    /**
     * To property value set.
     *
     * @param comboIds
     *            the combo ids
     * @return the set< string>
     */
    //TODO feilong 提取
    private static Set<String> toPropertyValueSet(Set<Set<String>> comboIds){
        if (Validator.isNullOrEmpty(comboIds)){
            return Collections.emptySet();
        }

        Set<String> propertyValueSet = new HashSet<>();
        for (Set<String> set : comboIds){
            propertyValueSet.addAll(set);
        }
        return propertyValueSet;
    }
}
