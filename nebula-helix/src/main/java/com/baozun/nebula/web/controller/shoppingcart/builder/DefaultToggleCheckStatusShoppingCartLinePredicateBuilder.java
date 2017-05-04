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
package com.baozun.nebula.web.controller.shoppingcart.builder;

import java.util.List;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.shoppingcart.builder.predicate.HasInventoryShoppingCartLinePredicate;
import com.baozun.nebula.web.controller.shoppingcart.builder.predicate.NomalStatusShoppingCartLinePredicate;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartInventoryValidator;

import static com.feilong.core.util.predicate.BeanPredicateUtil.equalPredicate;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
@Component("toggleCheckStatusShoppingCartLinePredicateBuilder")
public class DefaultToggleCheckStatusShoppingCartLinePredicateBuilder implements ToggleCheckStatusShoppingCartLinePredicateBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultToggleCheckStatusShoppingCartLinePredicateBuilder.class);

    @Autowired
    private NomalStatusShoppingCartLinePredicate nomalStatusShoppingCartLinePredicate;

    /** The shopping cart inventory validator. */
    @Autowired
    private ShoppingCartInventoryValidator shoppingCartInventoryValidator;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.builder.ToggleCheckStatusShoppingCartLinePredicateBuilder#build(java.util.List, boolean)
     */
    @Override
    public Predicate<ShoppingCartLineCommand> build(List<ShoppingCartLineCommand> shoppingCartLineCommandList,boolean checkStatus){
        //找到需要处理的数据
        //如果全选,那么我们找到没有选中的购物车行; (可能这家伙已经在其他窗口中操作过了)

        Predicate<ShoppingCartLineCommand> settlementStatePredicate = equalPredicate("settlementState", checkStatus ? 0 : 1);
        if (!checkStatus){//如果全不选,那么我们找到选中的购物车行
            return settlementStatePredicate;
        }
        //如果是全选, 那么异常状态的就不需要选中了
        return PredicateUtils.allPredicate(//
                        settlementStatePredicate,
                        nomalStatusShoppingCartLinePredicate,
                        new HasInventoryShoppingCartLinePredicate(shoppingCartLineCommandList, shoppingCartInventoryValidator));
    }
}
