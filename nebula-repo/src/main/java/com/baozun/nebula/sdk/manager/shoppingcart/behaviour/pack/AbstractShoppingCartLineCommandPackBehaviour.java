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

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemTagRelationDao;
import com.baozun.nebula.dao.product.ShopDao;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemTagRelation;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartLineImageManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class AbstractShoppingCartLineCommandPackBehaviour.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public abstract class AbstractShoppingCartLineCommandPackBehaviour implements ShoppingCartLineCommandPackBehaviour{

    /** The shop dao. */
    @Autowired
    private ShopDao shopDao;

    /** The sdk item manager. */
    @Autowired
    private SdkItemManager sdkItemManager;

    /** The item category dao. */
    @Autowired
    private ItemCategoryDao itemCategoryDao;

    /** The item tag relation dao. */
    @Autowired
    private ItemTagRelationDao itemTagRelationDao;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager sdkEngineManager;

    /** The sdk shopping cart line image manager. */
    @Autowired
    private SdkShoppingCartLineImageManager sdkShoppingCartLineImageManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.shoppingcart.behaviour.SdkShoppingCartLineCommandBehaviour#packShoppingCartLine(com.baozun.nebula.sdk.
     * command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand){
        doPackShoppingCartLine(shoppingCartLineCommand);

        shoppingCartLineCommand.setSubTotalAmt(ShoppingCartUtil.getSubTotalAmt(shoppingCartLineCommand));
    }

    /**
     * Do pack shopping cart line.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     */
    protected abstract void doPackShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand);

    /**
     * Pack shop and industry.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param item
     *            the item
     */
    protected void packShopAndIndustry(ShoppingCartLineCommand shoppingCartLineCommand,Item item){
        Long shopId = item.getShopId();
        ShopCommand shopCommand = shopDao.findShopById(shopId);
        // 店铺信息
        shoppingCartLineCommand.setShopId(shopId);
        shoppingCartLineCommand.setShopName(shopCommand.getShopname());
        shoppingCartLineCommand.setIndstryId(item.getIndustryId());
    }

    /**
     * Pack item info.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param item
     *            the item
     */
    protected void packItemInfo(ShoppingCartLineCommand shoppingCartLineCommand,Item item){
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
     * Pack category and lables.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param itemId
     *            the item id
     */
    protected void packCategoryAndLables(ShoppingCartLineCommand shoppingCartLineCommand,Long itemId){
        List<ItemCategory> itemCategoryList = itemCategoryDao.findItemCategoryListByItemId(itemId);
        shoppingCartLineCommand.setComboIds(buildComboIds(itemId, itemCategoryList));
        shoppingCartLineCommand.setCategoryList(buildCategoryIdList(itemCategoryList));
        shoppingCartLineCommand.setLableIds(buildLableIds(itemId));
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

}
