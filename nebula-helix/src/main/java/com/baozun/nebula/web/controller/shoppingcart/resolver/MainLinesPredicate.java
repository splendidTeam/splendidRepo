package com.baozun.nebula.web.controller.shoppingcart.resolver;

import org.apache.commons.collections4.Predicate;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

//TODO
public class MainLinesPredicate implements Predicate<ShoppingCartLineCommand>{

    @Override
    public boolean evaluate(ShoppingCartLineCommand shoppingCartLineCommand){
        // 促銷行 & 贈品 不參與遍曆
        return !shoppingCartLineCommand.isCaptionLine() && !shoppingCartLineCommand.isGift();
    }
}
