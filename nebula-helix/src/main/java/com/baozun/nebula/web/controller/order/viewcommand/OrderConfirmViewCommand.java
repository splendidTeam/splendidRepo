package com.baozun.nebula.web.controller.order.viewcommand;

import java.util.List;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.web.controller.BaseViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;
/**
 * 订单
 * @author dahui.huang
 *
 */
public class OrderConfirmViewCommand extends BaseViewCommand{

	private static final long		serialVersionUID	= -8532701843297930089L;

	/**
	 * 购物车
	 */
	private ShoppingCartViewCommand	shoppingCartViewCommand;

	/**
	 * 会员收货地址
	 */
	private List<ContactCommand>	addressList;

	public ShoppingCartViewCommand getShoppingCartViewCommand(){
		return shoppingCartViewCommand;
	}

	public void setShoppingCartViewCommand(ShoppingCartViewCommand shoppingCartViewCommand){
		this.shoppingCartViewCommand = shoppingCartViewCommand;
	}

	public List<ContactCommand> getAddressList(){
		return addressList;
	}

	public void setAddressList(List<ContactCommand> addressList){
		this.addressList = addressList;
	}

}
