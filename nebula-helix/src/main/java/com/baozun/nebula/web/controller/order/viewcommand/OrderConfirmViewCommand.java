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

	private static final long		serialVersionUID	= -8532701843297930089L;

	/**
	 * 购物车
	 */
	private ShoppingCartCommand		shoppingCartCommand;

	/**
	 * 会员收货地址
	 */
	private List<ContactCommand>	addressList;
	
	/***
	 * 立即购买的key
	 */
	private String					key;

	public ShoppingCartCommand getShoppingCartCommand(){
		return shoppingCartCommand;
	}

	public void setShoppingCartCommand(ShoppingCartCommand shoppingCartCommand){
		this.shoppingCartCommand = shoppingCartCommand;
	}

	public List<ContactCommand> getAddressList(){
		return addressList;
	}

	public void setAddressList(List<ContactCommand> addressList){
		this.addressList = addressList;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

}
