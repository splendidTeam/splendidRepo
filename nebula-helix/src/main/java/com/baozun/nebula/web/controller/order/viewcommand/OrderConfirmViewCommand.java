package com.baozun.nebula.web.controller.order.viewcommand;

import java.util.List;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 订单
 * 
 * @author dahui.huang
 */
public class OrderConfirmViewCommand extends BaseViewCommand{

    private static final long    serialVersionUID = -8532701843297930089L;

    /**
     * 购物车
     * 
     * @deprecated will change to viewCommand
     */
    private ShoppingCartCommand  shoppingCartCommand;

    /**
     * 会员收货地址
     */
    private List<ContactCommand> addressList;

    /**
     * @deprecated will change to viewCommand
     * @return
     */
    public ShoppingCartCommand getShoppingCartCommand(){
        return shoppingCartCommand;
    }

    /**
     * @deprecated will change to viewCommand
     * @param shoppingCartCommand
     */
    public void setShoppingCartCommand(ShoppingCartCommand shoppingCartCommand){
        this.shoppingCartCommand = shoppingCartCommand;
    }

    public List<ContactCommand> getAddressList(){
        return addressList;
    }

    public void setAddressList(List<ContactCommand> addressList){
        this.addressList = addressList;
    }

}
