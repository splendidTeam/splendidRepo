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
package com.baozun.nebula.web.controller.shoppingcart.handler;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.factory.ShoppingcartFactory;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShopSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.Status;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.predicate.BeanPredicateUtil;
import com.feilong.tools.jsonlib.JsonUtil;
import com.google.common.collect.Iterables;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.6
 */
@Component("uncheckedInvalidStateShoppingCartLineHandler")
public class DefaultUncheckedInvalidStateShoppingCartLineHandler implements UncheckedInvalidStateShoppingCartLineHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUncheckedInvalidStateShoppingCartLineHandler.class);

    @Autowired
    private ShoppingcartFactory shoppingcartFactory;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.handler.UncheckedInvalidStateShoppingCartLineHandler#uncheckedInvalidStateShoppingCartLine(com.baozun.nebula.web.MemberDetails,
     * com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand)
     */
    @Override
    public void uncheckedInvalidStateShoppingCartLine(MemberDetails memberDetails,ShoppingCartViewCommand shoppingCartViewCommand,HttpServletRequest request,HttpServletResponse response){
        if (null == shoppingCartViewCommand){
            return;
        }

        Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap = shoppingCartViewCommand.getShopAndShoppingCartLineSubViewCommandListMap();
        List<ShoppingCartLineSubViewCommand> list = IterableUtils.toList(Iterables.concat(shopAndShoppingCartLineSubViewCommandListMap.values()));

        if (isNullOrEmpty(list)){
            return;
        }

        //***********************************将状态不对的 选中状态的订单行 变成不选中************************
        Predicate<ShoppingCartLineSubViewCommand> invalidStateAndCheckedPredicate = PredicateUtils.allPredicate(//
                        PredicateUtils.notPredicate(BeanPredicateUtil.equalPredicate("status", Status.NORMAL)),
                        BeanPredicateUtil.equalPredicate("isGift", false),
                        BeanPredicateUtil.equalPredicate("checked", true));
        List<ShoppingCartLineSubViewCommand> invalidStateShoppingCartLineSubViewCommandList = CollectionsUtil.select(list, invalidStateAndCheckedPredicate);
        if (isNullOrEmpty(invalidStateShoppingCartLineSubViewCommandList)){
            LOGGER.debug("no invalidState and checked shopping cart lines");
            return;
        }
        List<Long> invalidStateIdList = getPropertyValueList(invalidStateShoppingCartLineSubViewCommandList, "id");
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("{} status not valid and checked", JsonUtil.format(invalidStateIdList));
        }

        ShoppingcartResolver shoppingcartResolver = shoppingcartFactory.getShoppingcartResolver(memberDetails);
        ShoppingcartResult toggleShoppingCartLinesCheckStatus = shoppingcartResolver.uncheckShoppingCartLines(memberDetails, invalidStateIdList, request, response);
        LOGGER.info("{}", toggleShoppingCartLinesCheckStatus);
    }
}
