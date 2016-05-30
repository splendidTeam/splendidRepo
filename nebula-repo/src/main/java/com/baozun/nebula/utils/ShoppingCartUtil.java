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

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.predicate.BeanPropertyValueEqualsPredicate;

/**
 * 购物车工具类.
 * 
 * <p>
 * 主要是计算 {@link ShoppingCartLineCommand} {@link ShoppingCartCommand} 等常用方法
 * </p>
 *
 * @author feilong
 * @see ShoppingCartLineCommand
 * @see ShoppingCartCommand
 * @since 5.3.1
 */
public final class ShoppingCartUtil{

    /** Don't let anyone instantiate this class. */
    private ShoppingCartUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获得主卖品(剔除 促銷行 贈品),通常我们只操作主卖品.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @return the main shopping cart line command list
     */
    public static List<ShoppingCartLineCommand> getMainShoppingCartLineCommandList(
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        // 主賣品(剔除 促銷行 贈品)
        Predicate<ShoppingCartLineCommand> andPredicate = PredicateUtils.andPredicate(
                        new BeanPropertyValueEqualsPredicate<ShoppingCartLineCommand>("captionLine", false),
                        new BeanPropertyValueEqualsPredicate<ShoppingCartLineCommand>("gift", false));
        return CollectionsUtil.select(shoppingCartLineCommandList, andPredicate);
    }

    /**
     * 获得 main shopping cart line command list with check status.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param checkStatus
     *            true表示 从中获取选中的, false 表示不选中
     * @return the main shopping cart line command list with check status
     */
    public static List<ShoppingCartLineCommand> getMainShoppingCartLineCommandListWithCheckStatus(
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    boolean checkStatus){
        // 主賣品(剔除 促銷行 贈品)
        Predicate<ShoppingCartLineCommand> allPredicate = PredicateUtils.allPredicate(
                        new BeanPropertyValueEqualsPredicate<ShoppingCartLineCommand>("captionLine", false),
                        new BeanPropertyValueEqualsPredicate<ShoppingCartLineCommand>("gift", false),
                        new BeanPropertyValueEqualsPredicate<ShoppingCartLineCommand>("settlementState", checkStatus ? 1 : 0));
        return CollectionsUtil.select(shoppingCartLineCommandList, allPredicate);
    }

    /**
     * 计算整单商品数量.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart lines
     * @return 如果 <code>shoppingCartLineCommandList</code> 是null或者empty,返回0<br>
     *         否则累加 每个元素的 quantity属性之和
     * @see com.feilong.core.util.CollectionsUtil#sum(java.util.Collection, String)
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
     * 是否是不需要用户选择的礼品.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return true, if checks if is no need choice gift
     */
    public static boolean isNoNeedChoiceGift(ShoppingCartLineCommand shoppingCartLineCommand){
        return shoppingCartLineCommand.isGift() && GiftChoiceType.NoNeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType());
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
