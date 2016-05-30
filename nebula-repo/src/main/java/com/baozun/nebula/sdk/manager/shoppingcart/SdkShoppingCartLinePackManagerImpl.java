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
package com.baozun.nebula.sdk.manager.shoppingcart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.ItemTagRelationDao;
import com.baozun.nebula.dao.product.ShopDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemTagRelation;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.product.SdkBundleManager;
import com.feilong.core.Validator;
import com.feilong.core.lang.NumberUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class SdkShoppingCartLinePackManagerImpl.
 *
 * @author feilong
 * @version 5.3.1 2016年5月23日 下午5:04:54
 * @since 5.3.1
 */
@Transactional
@Service("sdkShoppingCartLinePackManager")
public class SdkShoppingCartLinePackManagerImpl implements SdkShoppingCartLinePackManager{

    /** The item dao. */
    @Autowired
    private ItemDao                         itemDao;

    /** The item tag relation dao. */
    @Autowired
    private ItemTagRelationDao              itemTagRelationDao;

    /** The shop dao. */
    @Autowired
    private ShopDao                         shopDao;

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager                   sdkSkuManager;

    /** The sdk item manager. */
    @Autowired
    private SdkItemManager                  sdkItemManager;

    /** The item category dao. */
    @Autowired
    private ItemCategoryDao                 itemCategoryDao;

    /** The sku dao. */
    @Autowired
    private SkuDao                          skuDao;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager                sdkEngineManager;

    /** The item info dao. */
    @Autowired
    private ItemInfoDao                     itemInfoDao;

    /** The sdk shopping cart line image manager. */
    @Autowired
    private SdkShoppingCartLineImageManager sdkShoppingCartLineImageManager;

    /** The sdk bundle manager. */
    @Autowired
    private SdkBundleManager                sdkBundleManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkEngineManager#packShoppingCartLine(com.baozun.nebula.sdk.command.shoppingcart.
     * ShoppingCartLineCommand)
     */
    @Override
    @Transactional(readOnly = true)
    public void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand){
        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        //TODO feilong bundle 只有 relatedItemId
        if (null == relatedItemId){
            doPackCommonShoppingCartLineCommand(shoppingCartLineCommand);
        }else{
            doPackRelatedShoppingCartLineCommand(shoppingCartLineCommand);
        }

        // 购物车行 金额小计
        BigDecimal salePrice = shoppingCartLineCommand.getSalePrice();
        BigDecimal subTotalAmt = NumberUtil.getMultiplyValue(shoppingCartLineCommand.getQuantity(), salePrice);
        shoppingCartLineCommand.setSubTotalAmt(subTotalAmt);
    }

    /**
     * Do pack related shopping cart line command.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * 
     * @since 5.3.1
     */
    private void doPackRelatedShoppingCartLineCommand(ShoppingCartLineCommand shoppingCartLineCommand){
        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        Item item = itemDao.findItemById(relatedItemId);
        Integer type = item.getType();
        if (Item.ITEM_TYPE_BUNDLE.equals(type)){
            doPackBundle(shoppingCartLineCommand, item);
        }
    }

    /**
     * Do pack bundle.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param item
     *            the item
     * @since 5.3.1
     */
    private void doPackBundle(ShoppingCartLineCommand shoppingCartLineCommand,Item item){
        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        Long[] skuIds = shoppingCartLineCommand.getSkuIds();

        //FIXME feilong 金额
        //sdkBundleManager.getBundleSkusPrice(relatedItemId, skuIds);
        //封装 店铺和 行业信息
        packShopAndIndustry(shoppingCartLineCommand, item);

        //封装 商品信息
        packItemInfo(shoppingCartLineCommand, item);

        packCategoryAndLables(shoppingCartLineCommand, item.getId());

        //shoppingCartLineCommand.setSalePrice(sku.getSalePrice());
        //shoppingCartLineCommand.setListPrice(sku.getListPrice()); 
    }

    /**
     * Do pack common shopping cart line command.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     */
    private void doPackCommonShoppingCartLineCommand(ShoppingCartLineCommand shoppingCartLineCommand){
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
     * Pack category and lables.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param itemId
     *            the item id
     * @since 5.3.1
     */
    private void packCategoryAndLables(ShoppingCartLineCommand shoppingCartLineCommand,Long itemId){
        List<ItemCategory> itemCategoryList = itemCategoryDao.findItemCategoryListByItemId(itemId);
        shoppingCartLineCommand.setComboIds(buildComboIds(itemId, itemCategoryList));
        shoppingCartLineCommand.setCategoryList(buildCategoryIdList(itemCategoryList));
        shoppingCartLineCommand.setLableIds(buildLableIds(itemId));
    }

    /**
     * Pack item info.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param item
     *            the item
     * @since 5.3.1
     */
    private void packItemInfo(ShoppingCartLineCommand shoppingCartLineCommand,Item item){
        Long itemId = item.getId();
        String itemCode = item.getCode();
        ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoByCode(itemCode);
        shoppingCartLineCommand.setItemId(itemId);
        shoppingCartLineCommand.setProductCode(itemCode);
        shoppingCartLineCommand.setItemName(itemBaseCommand.getTitle());
        shoppingCartLineCommand.setItemPic(sdkShoppingCartLineImageManager.getItemPicUrl(itemId));
        shoppingCartLineCommand.setType(null == itemBaseCommand.getType() ? ItemInfo.TYPE_MAIN : itemBaseCommand.getType());

        //        if (null != itemBaseCommand.getActiveBeginTime() && DateUtil.isAfter(itemBaseCommand.getActiveBeginTime(), new Date())){
        //            setInValid(shoppingCartLineCommand, 1);
        //        }
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
     * Builds the combo ids.
     *
     * @param itemId
     *            the item id
     * @param itemCategoryList
     *            the item category list
     * @return the set< string>
     */
    private Set<String> buildComboIds(Long itemId,List<ItemCategory> itemCategoryList){
        return sdkEngineManager.getItemScopeListByItemAndCategory(itemId, itemCategoryList);
    }

    /**
     * Builds the category id list.
     *
     * @param itemCategoryList
     *            the item category list
     * @return the list< long>
     */
    private List<Long> buildCategoryIdList(List<ItemCategory> itemCategoryList){
        return CollectionsUtil.getPropertyValueList(itemCategoryList, "categoryId");
    }

    /**
     * Builds the lable ids.
     *
     * @param itemId
     *            the item id
     * @return the list< long>
     */
    private List<Long> buildLableIds(Long itemId){
        List<ItemTagRelation> itemTagRelationList = itemTagRelationDao.findItemTagRelationListByItemId(itemId);
        return CollectionsUtil.getPropertyValueList(itemTagRelationList, "tagId");
    }

    /**
     * Pack shop and industry.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param item
     *            the item
     */
    private void packShopAndIndustry(ShoppingCartLineCommand shoppingCartLineCommand,Item item){
        Long shopId = item.getShopId();
        ShopCommand shopCommand = shopDao.findShopById(shopId);
        // 店铺信息
        shoppingCartLineCommand.setShopId(shopId);
        shoppingCartLineCommand.setShopName(shopCommand.getShopname());
        shoppingCartLineCommand.setStoreId(shopId);
        shoppingCartLineCommand.setIndstryId(item.getIndustryId());
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
        List<SkuProperty> skuPros = sdkSkuManager.getSkuPros(skuProperties);
        if (Validator.isNotNullOrEmpty(skuPros)){
            shoppingCartLineCommand.setSkuPropertys(skuPros);
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

}
