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
package com.baozun.nebula.web.controller.order.builder;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.bundle.SdkShoppingCartBundleNewLineBuilder;
import com.feilong.core.bean.BeanUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Component("shoppingCartCommandShowBuilder")
public class DefaultShoppingCartCommandShowBuilder implements ShoppingCartCommandShowBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShoppingCartCommandShowBuilder.class);

    /** The sdk shopping cart bundle new line builder. */
    @Autowired
    private SdkShoppingCartBundleNewLineBuilder sdkShoppingCartBundleNewLineBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.ShoppingCartCommandShowBuilder#build(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand)
     */
    @Override
    public ShoppingCartCommand build(ShoppingCartCommand shoppingCartCommand){
        List<ShoppingCartLineCommand> newShoppingCartLineCommands = buildNewShoppingCartLineCommands(shoppingCartCommand);

        ShoppingCartCommand cloneShoppingCartCommand = BeanUtil.cloneBean(shoppingCartCommand);
        cloneShoppingCartCommand.setShoppingCartLineCommands(newShoppingCartLineCommands);
        return cloneShoppingCartCommand;
    }

    /**
     * Builds the new shopping cart line commands.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the list
     * @since 5.3.1
     */
    private List<ShoppingCartLineCommand> buildNewShoppingCartLineCommands(ShoppingCartCommand shoppingCartCommand){
        List<ShoppingCartLineCommand> newShoppingCartLineCommands = new ArrayList<>();

        List<ShoppingCartLineCommand> shoppingCartLineCommands = shoppingCartCommand.getShoppingCartLineCommands();
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommands){
            Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
            if (null == relatedItemId){
                newShoppingCartLineCommands.add(shoppingCartLineCommand);
            }else{
                doWithBundle(newShoppingCartLineCommands, shoppingCartLineCommand, relatedItemId);
            }
        }
        return newShoppingCartLineCommands;
    }

    /**
     * 打散 bundle 显示,bundle 单行是由 多个 skuIds 组成, 循环成新的newShoppingCartLineCommand.
     * 
     * <p>
     * 目的是为了实现, 内存对象中,是一条购物车行, 但是显示的时候要拆散显示
     * </p>
     *
     * @param newShoppingCartLineCommands
     *            the new shopping cart line commands
     * @param oldShoppingCartLineCommand
     *            the old shopping cart line command
     * @param relatedItemId
     *            the related item id
     * @since 5.3.1.6
     */
    private void doWithBundle(List<ShoppingCartLineCommand> newShoppingCartLineCommands,ShoppingCartLineCommand oldShoppingCartLineCommand,Long relatedItemId){
        Long[] skuIds = oldShoppingCartLineCommand.getSkuIds();
        Integer quantity = oldShoppingCartLineCommand.getQuantity();

        for (Long skuId : skuIds){
            ShoppingCartLineCommand newShoppingCartLineCommand = sdkShoppingCartBundleNewLineBuilder.buildNewShoppingCartLineCommand(relatedItemId, skuId, quantity, oldShoppingCartLineCommand);
            newShoppingCartLineCommands.add(newShoppingCartLineCommand);
        }
    }
}
