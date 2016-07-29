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

import static com.feilong.core.Validator.isNotNullOrEmpty;

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
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.Status;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 购物车convert.
 *
 * @author hengheng.wang
 */
public class ShoppingcartViewCommandConverter extends BaseConverter<ShoppingCartViewCommand>{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID         = -7415881959809156733L;

    /** The shop manager. */
    @Autowired
    private ShopManager       shopManager;

    /** 地址簿默认行数. */
    public static final int   MEMBERADDRESSDEFAULTSIZE = 5;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.BaseConverter#convert(java.lang.Object)
     */
    @Override
    public ShoppingCartViewCommand convert(Object data){
        if (data == null)
            return null;
        if (data instanceof ShoppingCartCommand){
            ShoppingCartCommand shoppingCartCommand = (ShoppingCartCommand) data;
            // 转换的对象
            ShoppingCartViewCommand shoppingCartViewCommand = new ShoppingCartViewCommand();
            return convertToViewCommand(shoppingCartCommand, shoppingCartViewCommand);
        }else{
            throw new UnsupportDataTypeException(data.getClass() + " cannot convert to " + ContactCommand.class + "yet.");
        }

    }

    /**
     * 封装页面viewcommand.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param shoppingCartViewCommand
     *            the shopping cart view command
     * @return the shopping cart view command
     */
    protected ShoppingCartViewCommand convertToViewCommand(
                    ShoppingCartCommand shoppingCartCommand,
                    ShoppingCartViewCommand shoppingCartViewCommand){
        Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap = buildShopAndShoppingCartLineSubViewCommandListMap(
                        shoppingCartCommand);
        shoppingCartViewCommand.setShopAndShoppingCartLineSubViewCommandListMap(shopAndShoppingCartLineSubViewCommandListMap);

        // TODO feilong 页面显示时候店铺和购物车行排序
        return shoppingCartViewCommand;
    }

    /**
     * Builds the.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the map
     * @since 5.3.1.6
     */
    private Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> buildShopAndShoppingCartLineSubViewCommandListMap(
                    ShoppingCartCommand shoppingCartCommand){
        // 获取店铺级别的购物车
        Map<Long, ShoppingCartCommand> shoppingCartByShopIdMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        // 查询店铺信息
        List<Long> shopIds = new ArrayList<Long>();
        shopIds.addAll(shoppingCartByShopIdMap.keySet());

        List<ShopCommand> shopList = shopManager.findByShopIds(shopIds);
        // 店铺信息转成map形式 key是shopId
        Map<Long, ShopCommand> shopMap = CollectionsUtil.groupOne(shopList, "shopid");

        //********************************************************************************************************

        Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap = new HashMap<>();
        for (Long id : shopIds){
            // 当前店铺购物车行信息
            List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingCartByShopIdMap.get(id).getShoppingCartLineCommands();

            // 当前店铺店铺信息
            ShopCommand shopCommand = shopMap.get(id);

            ShopSubViewCommand shopSubViewCommand = toShopSubViewCommand(shopCommand);
            List<ShoppingCartLineSubViewCommand> shoppingCartLineSubViewCommandList = toShoppingCartLineSubViewCommandList(
                            shoppingCartLineCommandList);
            shopAndShoppingCartLineSubViewCommandListMap.put(shopSubViewCommand, shoppingCartLineSubViewCommandList);
        }
        return shopAndShoppingCartLineSubViewCommandListMap;
    }

    /**
     * To shop sub view command.
     *
     * @param shopCommand
     *            the shop command
     * @return the shop sub view command
     */
    private ShopSubViewCommand toShopSubViewCommand(ShopCommand shopCommand){
        ShopSubViewCommand viewCommand = new ShopSubViewCommand();
        viewCommand.setCode(shopCommand.getShopcode());
        viewCommand.setId(shopCommand.getShopid());
        viewCommand.setLifecycle(shopCommand.getLifecycle());
        viewCommand.setName(shopCommand.getShopname());
        return viewCommand;
    }

    /**
     * 创建List<ShoppingCartLineSubViewCommand>.
     *
     * @param lineList
     *            the line list
     * @return the list
     */
    private List<ShoppingCartLineSubViewCommand> toShoppingCartLineSubViewCommandList(List<ShoppingCartLineCommand> lineList){
        List<ShoppingCartLineSubViewCommand> result = new ArrayList<ShoppingCartLineSubViewCommand>();
        for (ShoppingCartLineCommand command : lineList){
            // 过滤赠品标题行
            if (Validator.isNotNullOrEmpty(command.getLineCaption())){
                continue;
            }
            result.add(toShoppingCartLineSubViewCommand(command));
        }
        return result;
    }

    /**
     * To shopping cart line sub view command.
     *
     * @param shoppingCartLineCommand
     *            the command
     * @return the shopping cart line sub view command
     * @since 5.3.1.6
     */
    private ShoppingCartLineSubViewCommand toShoppingCartLineSubViewCommand(ShoppingCartLineCommand shoppingCartLineCommand){
        ShoppingCartLineSubViewCommand shoppingCartLineSubViewCommand = new ShoppingCartLineSubViewCommand();

        Integer settlementState = shoppingCartLineCommand.getSettlementState();
        boolean checked = isNotNullOrEmpty(settlementState) ? settlementState != 0 : false;
        shoppingCartLineSubViewCommand.setChecked(checked);

        shoppingCartLineSubViewCommand.setIsGift(shoppingCartLineCommand.isGift());
        shoppingCartLineSubViewCommand.setItemCode(shoppingCartLineCommand.getProductCode());
        shoppingCartLineSubViewCommand.setAddTime(shoppingCartLineCommand.getCreateTime());
        PropertyUtil.copyProperties(
                        shoppingCartLineSubViewCommand,
                        shoppingCartLineCommand,
                        "extentionCode",
                        "stock",// since 5.3.1.8
                        "itemId",
                        "itemName",
                        "listPrice",
                        "quantity",
                        "salePrice",
                        "skuId",
                        "itemPic",
                        "id",
                        "subTotalAmt");

        Map<String, SkuProperty> map = CollectionsUtil.groupOne(shoppingCartLineCommand.getSkuPropertys(), "pName");
        shoppingCartLineSubViewCommand.setPropertiesMap(map);

        shoppingCartLineSubViewCommand.setStatus(toStatus(shoppingCartLineCommand));
        return shoppingCartLineSubViewCommand;
    }

    private Status toStatus(ShoppingCartLineCommand command){
        if (command.isValid()){
            return Status.NORMAL;
        }

        //目前 看到底层只实现了这两种， 以后要扩展
        //******************************************************************
        Integer validType = command.getValidType();
        if (validType == 1){
            return Status.ITEM_LIFECYCLE_OFF_SHELF;
        }else if (validType == 2){
            return Status.OUT_OF_STOCK;
        }
        //TODO 待扩展

        String messagePattern = "validType:[{}] not support!";
        throw new UnsupportedOperationException(Slf4jUtil.format(messagePattern, validType));
    }
}
