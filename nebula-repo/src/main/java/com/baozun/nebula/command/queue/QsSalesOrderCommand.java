package com.baozun.nebula.command.queue;

import java.io.Serializable;
import java.util.Set;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;


/***
 * 
* @Title: QsSalesOrderCommand.java 
* @Package com.baozun.store.command.salesorder 
* @Description: qs order 对象
*                  商城有自定义属性  可以继承该类
*                  
*                  由于需要在用户退出登录仍然需要继续排队  暂时不支持游客订单   后续版本可以由商城自主
*                  实现  对于游客可将sessionId进行缓存
* @author zlh 
* @date 2016-1-18 下午7:55:29 
* @version V1.0
 */
public class QsSalesOrderCommand implements Serializable{

	private ShoppingCartCommand shoppingCartCommand;
	
	private SalesOrderCommand salesOrderCommand;
	
	private Long              memberId;
	
	public ShoppingCartCommand getShoppingCartCommand() {
		return shoppingCartCommand;
	}

	public void setShoppingCartCommand(ShoppingCartCommand shoppingCartCommand) {
		this.shoppingCartCommand = shoppingCartCommand;
	}

	public SalesOrderCommand getSalesOrderCommand() {
		return salesOrderCommand;
	}

	public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand) {
		this.salesOrderCommand = salesOrderCommand;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
}
