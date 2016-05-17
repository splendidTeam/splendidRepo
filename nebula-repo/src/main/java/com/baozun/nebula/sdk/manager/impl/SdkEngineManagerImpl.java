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
package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AbstractScopeConditionResult;
import com.baozun.nebula.calculateEngine.condition.CrowdScopeConditionResult;
import com.baozun.nebula.calculateEngine.condition.ItemScopeConditionResult;
import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.dao.member.MemberGroupRelationDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.ItemTagRelationDao;
import com.baozun.nebula.dao.product.ShopDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemTagRelation;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitRuleFilterManager;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitationManager;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.feilong.core.Validator;
import com.feilong.core.bean.BeanUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class SdkEngineManagerImpl.
 */
@Transactional
@Service("sdkEngineService")
public class SdkEngineManagerImpl implements SdkEngineManager{

    /** The item dao. */
    @Autowired
    private ItemDao                           itemDao;

    /** The item category dao. */
    @Autowired
    private ItemCategoryDao                   itemCategoryDao;

    /** The item tag relation dao. */
    @Autowired
    private ItemTagRelationDao                itemTagRelationDao;

    /** The sku dao. */
    @Autowired
    private SkuDao                            skuDao;

    /** The item info dao. */
    @Autowired
    private ItemInfoDao                       itemInfoDao;

    /** The sdk item manager. */
    @Autowired
    private SdkItemManager                    sdkItemManager;

    /** The shop dao. */
    @Autowired
    private ShopDao                           shopDao;

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager                     sdkSkuManager;

    /** The sdk purchase limitation manager. */
    @Autowired
    private SdkPurchaseLimitationManager      sdkPurchaseLimitationManager;

    /** The sdk shopping cart manager. */
    @Autowired
    private SdkShoppingCartManager            sdkShoppingCartManager;

    /** The member group relation dao. */
    @Autowired
    private MemberGroupRelationDao            memberGroupRelationDao;

    /** The sdk purchase rule filter manager. */
    @Autowired
    private SdkPurchaseLimitRuleFilterManager sdkPurchaseRuleFilterManager;

    /**
     * 创建订单的引擎检查.
     *
     * @param memberId
     *            the member id
     * @param memCombos
     *            the mem combos
     * @param shoppingCartCommand
     *            the shopping cart command
     */
    @Override
    public void createOrderDoEngineChck(Long memberId,Set<String> memCombos,ShoppingCartCommand shoppingCartCommand){
        // 引擎检查(限购、有效性检查、库存)
        Set<String> memboIds = null == memberId ? getCrowdScopeListByMemberAndGroup(null, null) : memCombos;

        //获取购物车中的所有店铺id.
        Set<Long> ids = CollectionsUtil.getPropertyValueSet(shoppingCartCommand.getShoppingCartLineCommands(), "shopId");
        List<Long> shopIds = new ArrayList<Long>(ids);

        Set<String> itemComboIds = getItemComboIds(shoppingCartCommand.getShoppingCartLineCommands());
        List<LimitCommand> purchaseLimitationList = sdkPurchaseRuleFilterManager
                        .getIntersectPurchaseLimitRuleData(shopIds, memboIds, itemComboIds, new Date());
        if (null == purchaseLimitationList || purchaseLimitationList.size() == 0)
            purchaseLimitationList = new ArrayList<LimitCommand>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shoppingCartCommand.getShoppingCartByShopIdMap().entrySet()){
            for (ShoppingCartLineCommand shoppingCartLine : entry.getValue().getShoppingCartLineCommands()){
                //直推礼品不做校验
                if (!isNoNeedChoiceGift(shoppingCartLine)){
                    doEngineCheck(shoppingCartLine, false, shoppingCartCommand, purchaseLimitationList);
                }
            }
        }

        //限购校验失败
        List<ShoppingCartLineCommand> errorLineList = doEngineCheckLimit(shoppingCartCommand, purchaseLimitationList);
        if (null != errorLineList && errorLineList.size() > 0){
            StringBuffer errorItemName = new StringBuffer();
            List<Long> itemList = new ArrayList<Long>();
            for (ShoppingCartLineCommand cartLine : errorLineList){
                if ("".equals(errorItemName.toString())){
                    errorItemName.append(cartLine.getItemName());
                    itemList.add(cartLine.getItemId());
                }else{
                    if (!itemList.contains(cartLine.getItemId())){
                        errorItemName.append(",").append(cartLine.getItemName());
                        itemList.add(cartLine.getSkuId());
                    }
                }
            }
            throw new BusinessException(Constants.THE_ORDER_CONTAINS_LIMIT_ITEM, new Object[] { errorItemName.toString() });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkEngineManager#packShoppingCartLine(com.baozun.nebula.sdk.command.shoppingcart.
     * ShoppingCartLineCommand)
     */
    @Override
    @Transactional(readOnly = true)
    public void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand){
        Long skuId = shoppingCartLineCommand.getSkuId();
        Sku sku = skuDao.findSkuById(skuId);
        if (sku == null){
            shoppingCartLineCommand.setValid(false);
            return;
        }
        Long itemId = sku.getItemId();
        Item item = itemDao.findItemById(itemId);

        // 购物车行有活动时, 同一个sku可以出现在多行中(可以是多个赠品), 库存数应为总库存数减去每个行中的qty
        List<PromotionCommand> promotionCommandList = shoppingCartLineCommand.getPromotionList();

        if (Validator.isNullOrEmpty(promotionCommandList)){
            SkuCommand skuCommand = sdkSkuManager.findSkuQSVirtualInventoryById(skuId, shoppingCartLineCommand.getExtentionCode());
            if (null != skuCommand){
                shoppingCartLineCommand.setStock(skuCommand.getAvailableQty());
            }else{
                shoppingCartLineCommand.setStock(0);
            }
        }

        //*************************************************************************************

        if (Constants.ITEM_ADDED_VALID_STATUS.equals(String.valueOf(item.getLifecycle()))){
            shoppingCartLineCommand.setValid(true); // 上架状态
            if (!checkActiveBeginTime(skuId)){
                setValid(shoppingCartLineCommand, 1);
            }else{
                Integer stock = shoppingCartLineCommand.getStock();
                if (stock <= 0 || stock < shoppingCartLineCommand.getQuantity()){
                    setValid(shoppingCartLineCommand, 2);
                }
            }
        }else{
            setValid(shoppingCartLineCommand, 1);
        }

        if (Validator.isNullOrEmpty(shoppingCartLineCommand.getExtentionCode())){
            shoppingCartLineCommand.setExtentionCode(sku.getOutid());
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

        shoppingCartLineCommand.setComboIds(getItemScopeListByItemAndCategory(itemId.toString(), itemCategoryList));

        shoppingCartLineCommand.setIndstryId(item.getIndustryId());

        shoppingCartLineCommand.setItemId(item.getId());
        shoppingCartLineCommand.setProductCode(itemCode);
        shoppingCartLineCommand.setItemName(itemBaseCommand.getTitle());
        shoppingCartLineCommand.setItemPic(getItemPicUrl(itemId));

        List<Long> categoryList = CollectionsUtil.getPropertyValueList(itemCategoryList, "categoryId");
        shoppingCartLineCommand.setCategoryList(categoryList);

        List<Long> lableIds = CollectionsUtil.getPropertyValueList(itemTagRelationList, "tagId");
        shoppingCartLineCommand.setLableIds(lableIds);

        shoppingCartLineCommand.setSalePrice(sku.getSalePrice());
        shoppingCartLineCommand.setListPrice(sku.getListPrice());

        if (null == itemBaseCommand.getType()){
            shoppingCartLineCommand.setType(ItemInfo.TYPE_MAIN);
        }else{
            shoppingCartLineCommand.setType(itemBaseCommand.getType());
        }

        // 销售属性
        String skuProperties = sku.getProperties();
        shoppingCartLineCommand.setSaleProperty(skuProperties);
        List<SkuProperty> skuPros = sdkSkuManager.getSkuPros(skuProperties);
        if (Validator.isNotNullOrEmpty(skuPros)){
            shoppingCartLineCommand.setSkuPropertys(skuPros);
        }
    }

    /**
     * 设置 valid.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param validType
     *            有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
     * @since 5.3.1
     */
    private void setValid(ShoppingCartLineCommand shoppingCartLineCommand,Integer validType){
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
    @Transactional(readOnly = true)
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.SdkEngineManager#doEngineGiftCheck(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * boolean, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public Integer doEngineGiftCheck(
                    ShoppingCartLineCommand line,
                    boolean flag,
                    ShoppingCartCommand cart,
                    List<LimitCommand> purchaseLimitationList){
        if (!flag){
            Long skuId = line.getSkuId();
            if (!line.isValid()){
                if (new Integer(1).equals(line.getValidType())){
                    return Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM;
                }else{
                    return Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM;
                }
            }

            Sku sku = skuDao.getByPrimaryKey(skuId);
            //如果sku.lifecycle!=1,则表示不能通过检查,删除sku时会出现lifecycle=0
            if (!sku.getLifecycle().equals(Sku.LIFE_CYCLE_ENABLE)){
                return Constants.CHECK_ITEM_SKU_FAILURE;
            }

            // 商品还没有上架
            if (!checkActiveBeginTime(skuId)){
                return Constants.CHECK_ITEM_ACTIVE_FAILURE;
            }
            // 如果更改购物车行时，若为增加购买时才检查库存

            Integer stock = line.getStock();
            if (null == stock || 0 == stock || stock < line.getQuantity()){
                return Constants.CHECK_INVENTORY_FAILURE;
            }
        }
        return Constants.SUCCESS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkEngineManager#doEngineCheck(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * boolean, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public Integer doEngineCheck(
                    ShoppingCartLineCommand line,
                    boolean flag,
                    ShoppingCartCommand cart,
                    List<LimitCommand> purchaseLimitationList){
        // 只有购物车中增加数量时才做相关检查
        if (!flag){
            // 商品类型是否赠品 , 赠品是不可以购买
            Long skuId = line.getSkuId();
            List<Long> skuIds = new ArrayList<Long>();
            skuIds.add(skuId);
            List<ItemInfo> itemInfoList = itemInfoDao.findItemInfosBySkuids(skuIds);
            if (null != itemInfoList && itemInfoList.size() > 0){
                ItemInfo itemInfo = itemInfoList.get(0);
                if (ItemInfo.TYPE_GIFT.equals(itemInfo.getType())){
                    throw new BusinessException(Constants.THE_ITEM_IS_GIFT);
                }
            }

            if (!line.isValid()){
                if (new Integer(1).equals(line.getValidType())){
                    throw new BusinessException(
                                    Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM,
                                    new Object[] { line.getItemName(), line.getProductCode(), line.getItemPic() });
                }else{
                    throw new BusinessException(
                                    Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM,
                                    new Object[] { line.getItemName(), line.getProductCode(), line.getItemPic() });
                }
            }

            Sku sku = skuDao.getByPrimaryKey(skuId);
            //如果sku.lifecycle!=1,则表示不能通过检查,删除sku时会出现lifecycle=0
            if (!sku.getLifecycle().equals(Sku.LIFE_CYCLE_ENABLE)){
                throw new BusinessException(
                                Constants.CONTAINS_NOTVALID_ITEM,
                                new Object[] { line.getItemName(), line.getProductCode(), line.getItemPic() });
            }

            // 商品还没有上架
            if (!checkActiveBeginTime(skuId)){
                throw new BusinessException(
                                Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM,
                                new Object[] { line.getItemName(), line.getProductCode(), line.getItemPic() });
            }
            // 如果更改购物车行时，若为增加购买时才检查库存

            Integer stock = line.getStock();
            if (null == stock || 0 == stock || stock < line.getQuantity()){
                throw new BusinessException(
                                Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM,
                                new Object[] { line.getItemName(), line.getProductCode(), line.getItemPic() });
            }
        }
        return Constants.SUCCESS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.SdkEngineManager#doEngineCheckLimit(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * java.util.List)
     */
    @Override
    public List<ShoppingCartLineCommand> doEngineCheckLimit(
                    ShoppingCartCommand shoppingCartCommand,
                    List<LimitCommand> purchaseLimitationList){
        if (Validator.isNullOrEmpty(purchaseLimitationList)){
            return null;
        }

        Map<Long, ShoppingCartCommand> cartByShopMap = sdkShoppingCartManager
                        .getShoppingCartMapByShop(shoppingCartCommand.getShoppingCartLineCommands());
        // 区分店铺的促销
        Map<Long, List<LimitCommand>> limitMaps = CollectionsUtil.group(purchaseLimitationList, "shopId");

        List<ShoppingCartLineCommand> errorLineList = null;
        for (Map.Entry<Long, ShoppingCartCommand> entry : cartByShopMap.entrySet()){
            Long shopId = entry.getKey();
            ShoppingCartCommand shoppingCartCommandByShop = entry.getValue();
            shoppingCartCommandByShop.setUserDetails(shoppingCartCommand.getUserDetails());

            List<LimitCommand> limitCommandList = limitMaps.get(shopId);
            if (null == limitCommandList){
                // 不存在
                limitCommandList = new ArrayList<LimitCommand>();
            }
            ShoppingCartCommand shoppingCart = new ShoppingCartCommand();
            List<ShoppingCartLineCommand> tmpLineList = new ArrayList<ShoppingCartLineCommand>();

            for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartCommandByShop.getShoppingCartLineCommands()){
                ShoppingCartLineCommand tmpLine = new ShoppingCartLineCommand();
                // 过滤caption行, 用户选择的赠品行(null是因为用户未选)
                if (shoppingCartLineCommand.isCaptionLine() || ItemInfo.TYPE_GIFT.equals(shoppingCartLineCommand.getType())
                                || (GiftChoiceType.NeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType())
                                                && null == shoppingCartLineCommand.getSettlementState())){
                    continue;
                }
                // 过滤用户可以选择的赠品行但未选中赠品
                if (shoppingCartLineCommand.isGift()
                                && Constants.NOCHECKED_CHOOSE_STATE.equals(shoppingCartLineCommand.getSettlementState())){
                    continue;
                }

                if (null == shoppingCartLineCommand.getCreateTime()){
                    shoppingCartLineCommand.setCreateTime(new Date());
                }

                BeanUtil.copyProperties(tmpLine, shoppingCartLineCommand);
                tmpLineList.add(tmpLine);
            }
            shoppingCart.setShoppingCartLineCommands(tmpLineList);
            shoppingCart.setShoppingCartByShopIdMap(shoppingCartCommandByShop.getShoppingCartByShopIdMap());
            shoppingCart.setUserDetails(shoppingCartCommandByShop.getUserDetails());
            errorLineList = sdkPurchaseLimitationManager.checkSKUPurchaseByLimitaionList(limitCommandList, shoppingCart);
            // 检查限购
        }
        return errorLineList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkEngineManager#getCrowdScopeListByMemberAndGroup(java.lang.Long, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public Set<String> getCrowdScopeListByMemberAndGroup(Long memberId,List<Long> memGroupId){
        Set<String> cids = new HashSet<String>(); // 会员组合
        Map<String, AbstractScopeConditionResult> crowdScopeConditionResultList = EngineManager.getInstance().getCrowdScopeEngine()
                        .getCrowdScopeMap();

        if (null != crowdScopeConditionResultList && crowdScopeConditionResultList.size() > 0){

            if (null != memberId && (null == memGroupId || memGroupId.size() == 0)){
                memGroupId = new ArrayList<Long>();
                List<MemberGroupRelation> memGroRelList = memberGroupRelationDao.findMemberGroupRelationListByMemberId(memberId);
                if (null != memGroRelList && memGroRelList.size() != 0){
                    for (MemberGroupRelation memberGroupRelation : memGroRelList){
                        memGroupId.add(memberGroupRelation.getGroupId());
                    }
                }
            }

            if (null == memGroupId || memGroupId.size() <= 0){
                memGroupId = new ArrayList<Long>();
                memGroupId.add(-1L);
            }

            for (String key : crowdScopeConditionResultList.keySet()){
                CrowdScopeConditionResult crowdScopeConditionResult = (CrowdScopeConditionResult) crowdScopeConditionResultList.get(key);
                Boolean checkFlag = crowdScopeConditionResult.getResult(memberId, memGroupId);
                if (checkFlag == null){
                    checkFlag = false;
                }
                if (checkFlag){
                    cids.add(key);
                }
            }
        }
        return cids;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkEngineManager#getItemScopeListByItemAndCategory(java.lang.String, java.util.List)
     */
    @Override
    @Transactional(readOnly = true)
    public Set<String> getItemScopeListByItemAndCategory(String itemId,List<ItemCategory> categoryLists){
        Set<String> cids = new HashSet<String>(); // 商品组合
        Map<String, AbstractScopeConditionResult> itemScopeConditionResultList = EngineManager.getInstance().getItemScopeEngine()
                        .getItemScopeMap();

        if (null != itemScopeConditionResultList && itemScopeConditionResultList.size() > 0){

            Long itemIdL = new Long(itemId);
            List<Long> categoryIds = new ArrayList<Long>();

            if (null == categoryLists || categoryLists.size() == 0){
                if (StringUtils.isNotBlank(itemId)){
                    categoryLists = itemCategoryDao.findItemCategoryListByItemId(itemIdL);
                    for (ItemCategory itemCategory : categoryLists){
                        categoryIds.add(itemCategory.getCategoryId());
                    }
                }
            }else{
                for (ItemCategory itemCategory : categoryLists){
                    categoryIds.add(itemCategory.getCategoryId());
                }
            }

            if (null == categoryIds || categoryIds.size() <= 0){
                categoryIds = new ArrayList<Long>();
                categoryIds.add(-1L);
            }

            for (String key : itemScopeConditionResultList.keySet()){
                ItemScopeConditionResult itemScopeConditionResult = (ItemScopeConditionResult) itemScopeConditionResultList.get(key);
                Boolean checkFlag = itemScopeConditionResult.getResult(itemIdL, categoryIds);
                if (checkFlag){
                    cids.add(key);
                }
            }
        }
        return cids;
    }

    /**
     * 获得 item pic url.
     *
     * @param itemId
     *            the item id
     * @return the item pic url
     */
    private String getItemPicUrl(Long itemId){
        List<Long> itemIds = new ArrayList<Long>(1);
        itemIds.add(itemId);

        List<ItemImageCommand> itemImageCommandList = sdkItemManager.findItemImagesByItemIds(itemIds, ItemImage.IMG_TYPE_LIST);
        if (Validator.isNotNullOrEmpty(itemImageCommandList)){
            ItemImageCommand itemImageCommand = itemImageCommandList.get(0);
            if (Validator.isNotNullOrEmpty(itemImageCommand)){
                List<ItemImage> imgList = itemImageCommand.getItemIamgeList();

                if (Validator.isNotNullOrEmpty(imgList)){
                    return imgList.get(0).getPicUrl();
                }
            }
        }
        return null;
    }

    /**
     * 是否是不需要用户选择的礼品.
     *
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return true, if checks if is no need choice gift
     * @since 5.3.1
     */
    private boolean isNoNeedChoiceGift(ShoppingCartLineCommand shoppingCartLineCommand){
        return shoppingCartLineCommand.isGift() && GiftChoiceType.NoNeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType());
    }

    /**
     * 根据购物车行获取ItemForCheckCommand集合.
     *
     * @param lines
     *            the lines
     * @return the item combo ids
     */
    private Set<String> getItemComboIds(List<ShoppingCartLineCommand> lines){
        Set<String> set = new HashSet<String>();
        if (null != lines && lines.size() > 0){
            for (ShoppingCartLineCommand line : lines){
                if (line.getComboIds() != null)
                    set.addAll(line.getComboIds());
            }
        }
        return set;
    }
}
