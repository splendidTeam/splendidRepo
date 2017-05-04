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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.Status;
import com.feilong.core.util.SortUtil;
import com.feilong.core.util.comparator.BeanComparatorUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.15
 */
@Component("shoppingCartLineSubViewCommandListSoter")
public class DefaultShoppingCartLineSubViewCommandListSorter implements ShoppingCartLineSubViewCommandListSorter{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShoppingCartLineSubViewCommandListSorter.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.converter.ShoppingCartLineSubViewCommandListSoter#sort(java.util.List)
     */
    @Override
    public List<ShoppingCartLineSubViewCommand> sort(List<ShoppingCartLineSubViewCommand> shoppingCartLineSubViewCommandList){
        SortUtil.sortList(
                        shoppingCartLineSubViewCommandList, //
                        BeanComparatorUtil.<ShoppingCartLineSubViewCommand, Status> propertyComparator("status", Status.NORMAL, Status.OUT_OF_STOCK, Status.ITEM_LIFECYCLE_OFF_SHELF),
                        BeanComparatorUtil.<ShoppingCartLineSubViewCommand> chainedComparator("group", "addTime desc"));
        return shoppingCartLineSubViewCommandList;
    }
}
