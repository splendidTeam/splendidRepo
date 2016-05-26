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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemImageCommand;
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
import com.baozun.nebula.model.product.ItemImage;
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
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;

/**
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

    @Autowired
    private SdkEngineManager                sdkEngineManager;

    /** The item info dao. */
    @Autowired
    private ItemInfoDao                     itemInfoDao;

    @Autowired
    private SdkShoppingCartLineImageManager sdkShoppingCartLineImageManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkEngineManager#packShoppingCartLine(com.baozun.nebula.sdk.command.shoppingcart.
     * ShoppingCartLineCommand)
     */
    @Override
    @Transactional(readOnly = true)
    public void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand){
        //TODO feilong bundle 只有 relatedItemId
        Long skuId = shoppingCartLineCommand.getSkuId();
        Sku sku = skuDao.findSkuById(skuId);
        if (sku == null){
            shoppingCartLineCommand.setValid(false);
            return;
        }

        Long itemId = sku.getItemId();
        Item item = itemDao.findItemById(itemId);

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

        //*************************************************************************************

        if (Constants.ITEM_ADDED_VALID_STATUS.equals(String.valueOf(item.getLifecycle()))){
            shoppingCartLineCommand.setValid(true); // 上架状态
            if (!checkActiveBeginTime(skuId)){
                setInValid(shoppingCartLineCommand, 1);
            }else{
                Integer stock = shoppingCartLineCommand.getStock();
                if (stock <= 0 || stock < shoppingCartLineCommand.getQuantity()){
                    setInValid(shoppingCartLineCommand, 2);
                }
            }
        }else{
            setInValid(shoppingCartLineCommand, 1);
        }

        String itemCode = item.getCode();
        Long shopId = item.getShopId();

        ItemBaseCommand itemBaseCommand = sdkItemManager.findItemBaseInfoByCode(itemCode);
        ShopCommand shopCommand = shopDao.findShopById(shopId);
        // 店铺信息
        shoppingCartLineCommand.setShopId(shopId);
        shoppingCartLineCommand.setShopName(shopCommand.getShopname());
        shoppingCartLineCommand.setStoreId(shopId);

        List<ItemCategory> itemCategoryList = itemCategoryDao.findItemCategoryListByItemId(itemId);
        List<ItemTagRelation> itemTagRelationList = itemTagRelationDao.findItemTagRelationListByItemId(itemId);

        shoppingCartLineCommand.setComboIds(sdkEngineManager.getItemScopeListByItemAndCategory(itemId.toString(), itemCategoryList));

        shoppingCartLineCommand.setIndstryId(item.getIndustryId());

        shoppingCartLineCommand.setItemId(item.getId());
        shoppingCartLineCommand.setProductCode(itemCode);
        shoppingCartLineCommand.setItemName(itemBaseCommand.getTitle());
        shoppingCartLineCommand.setItemPic(sdkShoppingCartLineImageManager.getItemPicUrl(itemId));

        List<Long> categoryList = CollectionsUtil.getPropertyValueList(itemCategoryList, "categoryId");
        shoppingCartLineCommand.setCategoryList(categoryList);

        List<Long> lableIds = CollectionsUtil.getPropertyValueList(itemTagRelationList, "tagId");
        shoppingCartLineCommand.setLableIds(lableIds);

        shoppingCartLineCommand.setSalePrice(sku.getSalePrice());
        shoppingCartLineCommand.setListPrice(sku.getListPrice());

        shoppingCartLineCommand.setType(null == itemBaseCommand.getType() ? ItemInfo.TYPE_MAIN : itemBaseCommand.getType());

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
     * @since 5.3.1
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
    //TODO feilong 重复了
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
