package com.baozun.nebula.manager.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;

/**
 * 购物车操作处理器
 * 
 * @author jumbo
 *
 */
public interface ShoppingcartResolver {

	/**
	 * 获得购物车list
	 * 
	 * @param request
	 * @param memberDetails
	 * @return 如果获取不到返回null
	 */
	List<ShoppingCartLineCommand> getShoppingCartLineCommandList(HttpServletRequest request,
			MemberDetails memberDetails);

	/**
	 * 加入购物车
	 * 
	 * @param memberDetails
	 * @param skuId
	 * @param count
	 * @param request
	 */
	ShoppingcartResult addShoppingCart(MemberDetails memberDetails, Long skuId, Integer count,
			HttpServletRequest request, HttpServletResponse response);

	/**
	 * 更新购物车
	 * 
	 * @param memberDetails
	 * @param shoppingcartLineId
	 * @param count
	 * @param request
	 * @param response
	 * @return
	 */
	ShoppingcartResult updateShoppingCart(MemberDetails memberDetails, Long shoppingcartLineId, Integer count,
			HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 删除购物车行
	 * 
	 * @param memberDetails
	 * @param shoppingcartLineId
	 * @param count
	 * @param request
	 * @param response
	 * @return
	 */
	ShoppingcartResult deleteShoppingCartLine(MemberDetails memberDetails, Long shoppingcartLineId, 
			HttpServletRequest request, HttpServletResponse response);
	
	
	/**
	 * 勾选购物车行
	 * 
	 * @param memberDetails
	 * @param shoppingcartLineId
	 * @param count
	 * @param request
	 * @param response
	 * @return
	 */
	ShoppingcartResult selectShoppingCartLine(MemberDetails memberDetails, Long shoppingcartLineId, Integer settlementState,
			HttpServletRequest request, HttpServletResponse response);


}
