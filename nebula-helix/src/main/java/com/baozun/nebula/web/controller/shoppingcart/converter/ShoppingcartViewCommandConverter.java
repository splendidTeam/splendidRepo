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

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingCartLineSubViewCommandListSorter;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShopSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartLineSubViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.CollectionsUtil.groupOne;

/**
 * 购物车convert.
 *
 * @author hengheng.wang
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class ShoppingcartViewCommandConverter extends BaseConverter<ShoppingCartViewCommand>{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7415881959809156733L;

    /** The shop manager. */
    @Autowired
    private ShopManager shopManager;

    @Autowired
    private ShoppingCartLineSubViewCommandListSorter shoppingCartLineSubViewCommandListSorter;

    /**
     * 店铺 command 转成view command 的转换器 (可以spring 注入,如果没有注入,默认会 new 一个 ShopCommandToViewCommandTransformer).
     * 
     * @since 5.3.2.13
     */
    private ShopCommandToViewCommandTransformer shopCommandToViewCommandTransformer;

    /**
     * 购物车行 command 转成view command 的转换器 (可以spring 注入,如果没有注入,默认会 new 一个 ShoppingCartLineCommandToViewCommandTransformer).
     * 
     * @since 5.3.2.13
     */
    private ShoppingCartLineCommandToViewCommandTransformer shoppingCartLineCommandToViewCommandTransformer;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.BaseConverter#convert(java.lang.Object)
     */
    @Override
    public ShoppingCartViewCommand convert(Object data){
        if (data == null){
            return null;
        }

        if (data instanceof ShoppingCartCommand){
            ShoppingCartCommand shoppingCartCommand = (ShoppingCartCommand) data;
            // 转换的对象
            ShoppingCartViewCommand shoppingCartViewCommand = new ShoppingCartViewCommand();
            return convertToViewCommand(shoppingCartCommand, shoppingCartViewCommand);
        }

        throw new UnsupportDataTypeException(data.getClass() + " cannot convert to " + ContactCommand.class + "yet.");
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
    protected ShoppingCartViewCommand convertToViewCommand(ShoppingCartCommand shoppingCartCommand,ShoppingCartViewCommand shoppingCartViewCommand){
        Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap = buildShopAndShoppingCartLineSubViewCommandListMap(shoppingCartCommand);
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
    private Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> buildShopAndShoppingCartLineSubViewCommandListMap(ShoppingCartCommand shoppingCartCommand){
        // 获取店铺级别的购物车
        Map<Long, ShoppingCartCommand> shoppingCartByShopIdMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        // 查询店铺信息
        List<Long> shopIds = toList(shoppingCartByShopIdMap.keySet());

        List<ShopCommand> shopCommandList = shopManager.findByShopIds(shopIds);
        // 店铺信息转成map形式 key是shopId
        Map<Long, ShopCommand> shopMap = groupOne(shopCommandList, "shopid");

        //********************************************************************************************************
        Map<ShopSubViewCommand, List<ShoppingCartLineSubViewCommand>> shopAndShoppingCartLineSubViewCommandListMap = new HashMap<>();

        ShopCommandToViewCommandTransformer useShopCommandToViewCommandTransformer = defaultIfNull(shopCommandToViewCommandTransformer, new ShopCommandToViewCommandTransformer());

        for (Long id : shopIds){
            // 当前店铺购物车行信息
            List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingCartByShopIdMap.get(id).getShoppingCartLineCommands();

            // 当前店铺店铺信息
            ShopCommand shopCommand = shopMap.get(id);

            shopAndShoppingCartLineSubViewCommandListMap.put(//
                            useShopCommandToViewCommandTransformer.transform(shopCommand),
                            toShoppingCartLineSubViewCommandList(shoppingCartLineCommandList));
        }
        return shopAndShoppingCartLineSubViewCommandListMap;
    }

    /**
     * 创建List<ShoppingCartLineSubViewCommand>.
     *
     * @param lineList
     *            the line list
     * @return the list
     */
    private List<ShoppingCartLineSubViewCommand> toShoppingCartLineSubViewCommandList(List<ShoppingCartLineCommand> lineList){
        List<ShoppingCartLineSubViewCommand> result = new ArrayList<>();

        ShoppingCartLineCommandToViewCommandTransformer useShoppingCartLineCommandToViewCommandTransformer = defaultIfNull(shoppingCartLineCommandToViewCommandTransformer, new ShoppingCartLineCommandToViewCommandTransformer());

        for (ShoppingCartLineCommand shoppingCartLineCommand : lineList){
            // 过滤赠品标题行
            if (isNotNullOrEmpty(shoppingCartLineCommand.getLineCaption())){
                continue;
            }

            result.add(useShoppingCartLineCommandToViewCommandTransformer.transform(shoppingCartLineCommand));
        }

        result = shoppingCartLineSubViewCommandListSorter.sort(result);
        return result;
    }

    //------------

    /**
     * 设置 店铺 command 转成view command 的转换器 (可以spring 注入,如果没有注入,默认会 new 一个 ShopCommandToViewCommandTransformer).
     *
     * @param shopCommandToViewCommandTransformer
     *            the shopCommandToViewCommandTransformer to set
     * @since 5.3.2.13
     */
    public void setShopCommandToViewCommandTransformer(ShopCommandToViewCommandTransformer shopCommandToViewCommandTransformer){
        this.shopCommandToViewCommandTransformer = shopCommandToViewCommandTransformer;
    }

    /**
     * 设置 购物车行 command 转成view command 的转换器 (可以spring 注入,如果没有注入,默认会 new 一个 ShoppingCartLineCommandToViewCommandTransformer).
     *
     * @param shoppingCartLineCommandToViewCommandTransformer
     *            the shoppingCartLineCommandToViewCommandTransformer to set
     * @since 5.3.2.13
     */
    public void setShoppingCartLineCommandToViewCommandTransformer(ShoppingCartLineCommandToViewCommandTransformer shoppingCartLineCommandToViewCommandTransformer){
        this.shoppingCartLineCommandToViewCommandTransformer = shoppingCartLineCommandToViewCommandTransformer;
    }
}
