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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.pack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.feilong.core.Validator;

/**
 * 普通的购物车行的行为.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("shoppingCartLineCommandPackCommonBehaviour")
public class ShoppingCartLineCommandPackCommonBehaviour extends AbstractShoppingCartLineCommandPackBehaviour{

    /** The item dao. */
    @Autowired
    private ItemDao itemDao;

    /** The sku dao. */
    @Autowired
    private SkuDao skuDao;

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /** The item info dao. */
    @Autowired
    private ItemInfoDao itemInfoDao;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.shoppingcart.behaviour.AbstractSdkShoppingCartLineCommandBehaviour#doPackShoppingCartLine(com.baozun.
     * nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    protected void doPackShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand){
        Long skuId = shoppingCartLineCommand.getSkuId();
        Sku sku = skuDao.findSkuById(skuId);
        if (sku == null){
            shoppingCartLineCommand.setValid(false);
            return;
        }

        String outid = sku.getOutid();
        if (Validator.isNullOrEmpty(shoppingCartLineCommand.getExtentionCode())){
            shoppingCartLineCommand.setExtentionCode(outid);
        }

        // 购物车行有活动时, 同一个sku可以出现在多行中(可以是多个赠品), 库存数应为总库存数减去每个行中的qty
        List<PromotionCommand> promotionCommandList = shoppingCartLineCommand.getPromotionList();

        if (Validator.isNullOrEmpty(promotionCommandList)){
            SkuCommand skuCommand = sdkSkuManager.findSkuQSVirtualInventoryById(skuId, outid);
            shoppingCartLineCommand.setStock(null != skuCommand ? skuCommand.getAvailableQty() : 0);
        }

        shoppingCartLineCommand.setSalePrice(sku.getSalePrice());
        shoppingCartLineCommand.setListPrice(sku.getListPrice());

        //*************************************************************************************
        Long itemId = sku.getItemId();
        Item item = itemDao.findItemById(itemId);
        //设置 valid 状态
        packCheckValid(shoppingCartLineCommand, skuId, item);

        //封装 店铺和 行业信息
        packShopAndIndustry(shoppingCartLineCommand, item);

        //封装 商品信息
        packItemInfo(shoppingCartLineCommand, item);

        packCategoryAndLables(shoppingCartLineCommand, itemId);

        //封装销售属性
        packSalePropertys(shoppingCartLineCommand, sku);
    }

    /**
     * Pack check valid.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param skuId
     *            the sku id
     * @param item
     *            the item
     */
    private void packCheckValid(ShoppingCartLineCommand shoppingCartLineCommand,Long skuId,Item item){
        Integer quantity = shoppingCartLineCommand.getQuantity();
        if (Constants.ITEM_ADDED_VALID_STATUS.equals(String.valueOf(item.getLifecycle()))){
            shoppingCartLineCommand.setValid(true); // 上架状态
            if (!checkActiveBeginTime(skuId)){
                setInValid(shoppingCartLineCommand, 1);
            }else{
                Integer stock = shoppingCartLineCommand.getStock();
                if (stock <= 0 || stock < quantity){
                    setInValid(shoppingCartLineCommand, 2);
                }
            }
        }else{
            setInValid(shoppingCartLineCommand, 1);
        }
    }

    /**
     * Check active begin time.
     *
     * @author 何波
     * @param skuId
     *            the sku id
     * @return Boolean
     * @Description: 检查商品是否上架
     */
    //XXX feilong 重复了 see com.baozun.nebula.sdk.manager.impl.SdkEngineManagerImpl.checkActiveBeginTime(Long)
    private Boolean checkActiveBeginTime(Long skuId){
        List<Long> skuids = new ArrayList<Long>();
        skuids.add(skuId);

        List<ItemInfo> itemInfoList = itemInfoDao.findItemInfosBySkuids(skuids);
        if (itemInfoList == null || itemInfoList.size() == 0){
            return false;
        }
        ItemInfo itemInfo = itemInfoList.get(0);
        Date activeBeginTime = itemInfo.getActiveBeginTime();
        if (activeBeginTime != null){
            // 当前时间
            Long ctime = System.currentTimeMillis();
            if (activeBeginTime.getTime() > ctime){
                return false;
            }
        }else{
            return true;
        }
        return true;
    }

    /**
     * Pack sale propertys.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param sku
     *            the sku
     */
    private void packSalePropertys(ShoppingCartLineCommand shoppingCartLineCommand,Sku sku){
        // 销售属性
        String skuProperties = sku.getProperties();
        shoppingCartLineCommand.setSaleProperty(skuProperties);

        List<SkuProperty> skuPropertyList = sdkSkuManager.getSkuPros(skuProperties);
        if (Validator.isNotNullOrEmpty(skuPropertyList)){
            shoppingCartLineCommand.setSkuPropertys(skuPropertyList);
        }
    }

    /**
     * 设置 无效.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param validType
     *            有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
     */
    private void setInValid(ShoppingCartLineCommand shoppingCartLineCommand,Integer validType){
        shoppingCartLineCommand.setValid(false);// 下架状态
        shoppingCartLineCommand.setValidType(validType);
    }

}
