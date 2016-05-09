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
package com.baozun.nebula.web.controller.shoppingcart.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShopSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * 购物车convert
 * 
 * @author hengheng.wang
 *
 */
public class ShoppingcartViewCommandConverter extends BaseConverter<ShoppingCartViewCommand> {

	@Autowired
	private ShopManager shopManager;

	/**
	 * 地址簿默认行数
	 */
	public static final int MEMBERADDRESSDEFAULTSIZE = 5;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7415881959809156733L;

	public ShoppingCartViewCommand convert(Object data) {
		if (data == null)
			return null;
		if (data instanceof ShoppingCartCommand) {
			ShoppingCartCommand shoppingCartCommand = (ShoppingCartCommand) data;
			// 转换的对象
			ShoppingCartViewCommand shoppingCartViewCommand = new ShoppingCartViewCommand();
			return convertToViewCommand(shoppingCartCommand, shoppingCartViewCommand);
		} else {
			throw new UnsupportDataTypeException(
					data.getClass() + " cannot convert to " + ContactCommand.class + "yet.");
		}

	}

	/**
	 * 封装页面viewcommand
	 * 
	 * @param cartCommand
	 * @param shoppingCartViewCommand
	 */
	protected ShoppingCartViewCommand convertToViewCommand(ShoppingCartCommand cartCommand,
			ShoppingCartViewCommand shoppingCartViewCommand) {

		Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap = new HashMap<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>>();

		// 获取店铺级别的购物车
		Map<Long, ShoppingCartCommand> shoppingCartByShopIdMap = cartCommand.getShoppingCartByShopIdMap();

		// 查询店铺信息
		List<Long> shopIds = new ArrayList<Long>();
		shopIds.addAll(shoppingCartByShopIdMap.keySet());
		List<ShopCommand> shopList = shopManager.findByShopIds(shopIds);
		// 店铺信息转成map形式 key是shopId
		Map<Long, ShopCommand> shopMap = CollectionsUtil.groupOne(shopList, "shopid");

		for (Long id : shopIds) {
			// 当前店铺购物车行信息
			List<ShoppingCartLineCommand> lineList = shoppingCartByShopIdMap.get(id).getShoppingCartLineCommands();
			// 当前店铺店铺信息
			ShopCommand shopCommand = shopMap.get(id);
			// ***********************************************创建ShopSubViewCommand******************创建ShoppingCartLineSubViewCommands
			shopAndShoppingCartLineSubViewCommandListMap.put(createShopSubViewCommand(shopCommand),
					createShoppingCartLineSubViewCommands(lineList));
		}
		shoppingCartViewCommand
				.setShopAndShoppingCartLineSubViewCommandListMap(shopAndShoppingCartLineSubViewCommandListMap);

		// TODO 页面显示时候店铺和购物车行排序

		return shoppingCartViewCommand;
	}

	/**
	 * 创建ShopSubViewCommand
	 * 
	 * @param shopCommand
	 * @return
	 */
	private ShopSubViewCommand createShopSubViewCommand(ShopCommand shopCommand) {
		ShopSubViewCommand viewCommand = new ShopSubViewCommand();
		viewCommand.setCode(shopCommand.getShopcode());
		viewCommand.setId(shopCommand.getShopid());
		viewCommand.setLifecycle(shopCommand.getLifecycle());
		viewCommand.setName(shopCommand.getShopname());
		return viewCommand;
	}

	/**
	 * 创建List<ShoppingCartLineSubViewCommand>
	 * 
	 * @param shopCommand
	 * @return
	 */
	private List<ShoppingCartLineSubViewCommand> createShoppingCartLineSubViewCommands(
			List<ShoppingCartLineCommand> lineList) {

		List<ShoppingCartLineSubViewCommand> result = new ArrayList<ShoppingCartLineSubViewCommand>();

		for (ShoppingCartLineCommand command : lineList) {

			ShoppingCartLineSubViewCommand viewCommand = new ShoppingCartLineSubViewCommand();
			viewCommand.setChecked(command.getSettlementState() == 0 ? false : true);
			viewCommand.setIsGift(command.isGift());
			viewCommand.setItemCode(command.getProductCode());
			viewCommand.setAddTime(command.getCreateTime());
			PropertyUtil.copyProperties(viewCommand, command, "extentionCode", "itemId", "itemName", "listPrice",
					"quantity", "salePrice", "skuId", "itemPic", "id","subTotalAmt");

			Map<String, SkuProperty> map = CollectionsUtil.groupOne(command.getSkuPropertys(), "pName");
			viewCommand.setPropertiesMap(map);
			result.add(viewCommand);
		}

		return result;
	}
}
