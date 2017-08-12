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
package com.baozun.nebula.sdk.manager.shoppingcart.handler;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class DefaultCombinedShoppingCartLineCommandFinder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Component("combinedShoppingCartLineCommandFinder")
public class DefaultCombinedShoppingCartLineCommandFinder implements CombinedShoppingCartLineCommandFinder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCombinedShoppingCartLineCommandFinder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.handler.CombinedShoppingCartLineCommandFinder#findCombinedShoppingCartLineCommand(java.util.List, java.util.List)
     */
    @Override
    public ShoppingCartLineCommand find(List<ShoppingCartLineCommand> shoppingCartLineCommandListDB,List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        //循环内存list,如果发现数量和db不相同,那么返回这条数据
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            Long lineId = shoppingCartLineCommand.getId();
            ShoppingCartLineCommand db = CollectionsUtil.find(shoppingCartLineCommandListDB, "id", lineId);

            Integer dbQuantity = db.getQuantity();
            Integer commandQuantity = shoppingCartLineCommand.getQuantity();

            boolean isEquals = Objects.equals(dbQuantity, commandQuantity);

            //---------------------------------------------------------------------

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("when lineId:[{}],dbQuantity:[{}],commandQuantity:[{}],isEquals:[{}]", lineId, dbQuantity, commandQuantity, isEquals);
            }

            if (!isEquals){
                LOGGER.debug("will return shoppingCartLineCommand id:[{}]", lineId);

                return shoppingCartLineCommand;
            }
        }
        throw new BusinessException("not find Combined ShoppingCartLineCommand");
    }
}
