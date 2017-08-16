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

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.feilong.core.Validator;
import com.feilong.core.lang.NumberUtil;
import com.feilong.core.lang.ObjectUtil;
import com.feilong.core.util.AggregateUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.predicate.BeanPredicateUtil;

import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 购物车工具类.
 * 
 * <p>
 * 主要是计算 {@link ShoppingCartLineCommand} {@link ShoppingCartCommand} 等常用方法
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see ShoppingCartLineCommand
 * @see ShoppingCartCommand
 * @since 5.3.1
 */
public final class ShoppingCartUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartUtil.class);

    /** Don't let anyone instantiate this class. */
    private ShoppingCartUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------------

    /**
     * 计算应付金额.
     * 
     * <p>
     * 自动去除 gift 以及 isCaptionLine
     * </p>
     *
     * @param shoppingCartLines
     *            the shopping cart lines
     * @return the origin pay amount
     */
    public static BigDecimal getOriginPayAmount(List<ShoppingCartLineCommand> shoppingCartLines){
        BigDecimal originPayAmount = new BigDecimal(0);
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLines){
            if (shoppingCartLineCommand.isGift() || shoppingCartLineCommand.isCaptionLine()){
                continue;
            }
            //getSubTotalAmt(shoppingCartLineCommand)
            originPayAmount = originPayAmount.add(NumberUtil.getMultiplyValue(shoppingCartLineCommand.getQuantity(), shoppingCartLineCommand.getSalePrice(), 2));
        }
        return originPayAmount = originPayAmount.setScale(2, ROUND_HALF_UP);
    }

    /**
     * 订单行小计.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return the sub total amt
     */
    public static BigDecimal getSubTotalAmt(ShoppingCartLineCommand shoppingCartLineCommand){
        Validate.notNull(shoppingCartLineCommand, "shoppingCartLineCommand can't be null!");

        BigDecimal discount = shoppingCartLineCommand.getDiscount();
        BigDecimal salePrice = shoppingCartLineCommand.getSalePrice();
        Integer quantity = shoppingCartLineCommand.getQuantity();

        Validate.notNull(salePrice, "salePrice can't be null!");
        Validate.notNull(quantity, "quantity can't be null!");
        Validate.notNull(discount, "discount can't be null!");
        Validate.isTrue(quantity > 0, "quantity must >0");

        BigDecimal lineSubTotalAmt = NumberUtil.getMultiplyValue(quantity, salePrice, 2).subtract(discount);
        BigDecimal subTotalAmt = lineSubTotalAmt.compareTo(ZERO) < 0 ? ZERO : lineSubTotalAmt;

        String message = "line:[{}],salesprice:[{}],qty:[{}],discount:[{}],subTotalAmt:[{}*{}-{}={}]";
        LOGGER.debug(message, shoppingCartLineCommand.getId(), salePrice, quantity, discount, salePrice, quantity, discount, subTotalAmt);
        return subTotalAmt;
    }

    /**
     * 获得主卖品(剔除 促銷行 贈品),通常我们只操作主卖品.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @return the main shopping cart line command list
     */
    public static List<ShoppingCartLineCommand> getMainShoppingCartLineCommandList(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        // 主賣品(剔除 促銷行 贈品)
        Predicate<ShoppingCartLineCommand> andPredicate = PredicateUtils.andPredicate(//
                        BeanPredicateUtil.equalPredicate("captionLine", false),
                        BeanPredicateUtil.equalPredicate("gift", false));
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
    public static List<ShoppingCartLineCommand> getMainShoppingCartLineCommandListWithCheckStatus(List<ShoppingCartLineCommand> shoppingCartLineCommandList,boolean checkStatus){
        // 主賣品(剔除 促銷行 贈品)
        Predicate<ShoppingCartLineCommand> allPredicate = PredicateUtils
                        .<ShoppingCartLineCommand> allPredicate(BeanPredicateUtil.equalPredicate("captionLine", false), BeanPredicateUtil.equalPredicate("gift", false), BeanPredicateUtil.equalPredicate("settlementState", checkStatus ? 1 : 0));
        return CollectionsUtil.select(shoppingCartLineCommandList, allPredicate);
    }

    /**
     * 统计传入的 <code>shoppingCartLineCommandList</code> ,里面购买的商品数量.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart lines
     * @return 如果 <code>shoppingCartLineCommandList</code> 是null或者empty,返回 <b>0</b><br>
     *         否则累加每个元素的 quantity 属性 之和
     * @see com.feilong.core.util.AggregateUtil#sum(Iterable, String)
     */
    public static int getSumQuantity(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        return isNullOrEmpty(shoppingCartLineCommandList) ? 0 : AggregateUtil.sum(shoppingCartLineCommandList, "quantity").intValue();
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
     * 累加 行内 discount小计(仅限 bundle商品).
     *
     * @param shoppingCartLineCommands
     *            the shopping cart line commands
     * @return 如果没有bundle商品, 那么返回 0
     * @since 5.3.1.8
     */
    public static BigDecimal getBundleDiscount(List<ShoppingCartLineCommand> shoppingCartLineCommands){
        BigDecimal sumBundleDiscount = AggregateUtil.sum(shoppingCartLineCommands, "discount", new Predicate<ShoppingCartLineCommand>(){

            @Override
            public boolean evaluate(ShoppingCartLineCommand shoppingCartLineCommand){

                //TODO feilong 这里使用简单粗暴的手段,对bundle强行处理,需要提炼
                Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
                return null != relatedItemId;
            }
        });

        sumBundleDiscount = ObjectUtil.defaultIfNullOrEmpty(sumBundleDiscount, ZERO);
        LOGGER.debug("sumBundleDiscount:[{}]", sumBundleDiscount);
        return sumBundleDiscount;
    }

    /**
     * To property value set.
     *
     * @param comboIds
     *            the combo ids
     * @return 如果 <code>dateList</code> 是null或者empty,返回 {@link Collections#emptyList()}<br>
     */
    //XXX feilong 提取
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
