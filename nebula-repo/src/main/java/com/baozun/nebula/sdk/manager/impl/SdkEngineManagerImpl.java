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
import java.util.Collections;
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
import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.dao.member.MemberGroupRelationDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitRuleFilterManager;
import com.baozun.nebula.sdk.manager.SdkPurchaseLimitationManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.core.Validator;
import com.feilong.core.bean.BeanUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class SdkEngineManagerImpl.
 */
@Transactional
@Service("sdkEngineService")
public class SdkEngineManagerImpl implements SdkEngineManager{

    /** The item category dao. */
    @Autowired
    private ItemCategoryDao                   itemCategoryDao;

    /** The sku dao. */
    @Autowired
    private SkuDao                            skuDao;

    /** The item info dao. */
    @Autowired
    private ItemInfoDao                       itemInfoDao;

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
        List<Long> shopIds = getShopIds(shoppingCartCommand);

        Set<String> itemComboIds = ShoppingCartUtil.getItemComboIds(shoppingCartCommand.getShoppingCartLineCommands());

        List<LimitCommand> purchaseLimitationList = sdkPurchaseRuleFilterManager
                        .getIntersectPurchaseLimitRuleData(shopIds, memboIds, itemComboIds, new Date());

        if (Validator.isNullOrEmpty(purchaseLimitationList)){
            purchaseLimitationList = new ArrayList<LimitCommand>();
        }

        for (Map.Entry<Long, ShoppingCartCommand> entry : shoppingCartCommand.getShoppingCartByShopIdMap().entrySet()){
            for (ShoppingCartLineCommand shoppingCartLine : entry.getValue().getShoppingCartLineCommands()){
                //直推礼品不做校验
                if (!isNoNeedChoiceGift(shoppingCartLine)){
                    doEngineCheck(shoppingCartLine, false);
                }
            }
        }

        //限购校验失败
        List<ShoppingCartLineCommand> errorLineList = doEngineCheckLimit(shoppingCartCommand, purchaseLimitationList);

        if (Validator.isNotNullOrEmpty(errorLineList)){
            toBusinessException(errorLineList);
        }
    }

    /**
     * @param errorLineList
     */
    private void toBusinessException(List<ShoppingCartLineCommand> errorLineList){
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

    /**
     * @param shoppingCartCommand
     * @return
     */
    private List<Long> getShopIds(ShoppingCartCommand shoppingCartCommand){
        Set<Long> ids = CollectionsUtil.getPropertyValueSet(shoppingCartCommand.getShoppingCartLineCommands(), "shopId");
        return new ArrayList<Long>(ids);
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
    public Integer doEngineCheck(ShoppingCartLineCommand shoppingCartLineCommand,boolean flag){
        // 只有购物车中增加数量时才做相关检查
        if (!flag){
            // 商品类型是否赠品 , 赠品是不可以购买
            Long skuId = shoppingCartLineCommand.getSkuId();
            List<Long> skuIds = new ArrayList<Long>();
            skuIds.add(skuId);

            List<ItemInfo> itemInfoList = itemInfoDao.findItemInfosBySkuids(skuIds);

            if (null != itemInfoList && itemInfoList.size() > 0){
                ItemInfo itemInfo = itemInfoList.get(0);
                if (ItemInfo.TYPE_GIFT.equals(itemInfo.getType())){
                    throw new BusinessException(Constants.THE_ITEM_IS_GIFT);
                }
            }

            if (!shoppingCartLineCommand.isValid()){
                throwBusinessException(
                                new Integer(1).equals(shoppingCartLineCommand.getValidType()) ? Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM
                                                : Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM,
                                shoppingCartLineCommand);
            }

            Sku sku = skuDao.getByPrimaryKey(skuId);
            //如果sku.lifecycle!=1,则表示不能通过检查,删除sku时会出现lifecycle=0
            if (!sku.getLifecycle().equals(Sku.LIFE_CYCLE_ENABLE)){
                throwBusinessException(Constants.CONTAINS_NOTVALID_ITEM, shoppingCartLineCommand);
            }

            // 商品还没有上架
            if (!checkActiveBeginTime(skuId)){
                throwBusinessException(Constants.THE_ORDER_CONTAINS_NOTVALID_ITEM, shoppingCartLineCommand);
            }
            // 如果更改购物车行时，若为增加购买时才检查库存
            Integer stock = shoppingCartLineCommand.getStock();
            if (null == stock || 0 == stock || stock < shoppingCartLineCommand.getQuantity()){
                throwBusinessException(Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM, shoppingCartLineCommand);
            }
        }
        return Constants.SUCCESS;
    }

    private void throwBusinessException(int errorCode,ShoppingCartLineCommand shoppingCartLineCommand){
        String itemName = shoppingCartLineCommand.getItemName();
        String productCode = shoppingCartLineCommand.getProductCode();
        String itemPic = shoppingCartLineCommand.getItemPic();
        throw new BusinessException(errorCode, new Object[] { itemName, productCode, itemPic });
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

            //***************************************************************************************
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
    public Set<String> getCrowdScopeListByMemberAndGroup(Long memberId,List<Long> memGroupIdList){
        Map<String, AbstractScopeConditionResult> crowdScopeConditionResultList = EngineManager.getInstance().getCrowdScopeEngine()
                        .getCrowdScopeMap();

        if (Validator.isNullOrEmpty(crowdScopeConditionResultList)){
            return Collections.emptySet();
        }

        if (null != memberId && Validator.isNullOrEmpty(memGroupIdList)){
            List<MemberGroupRelation> memberGroupRelationList = memberGroupRelationDao.findMemberGroupRelationListByMemberId(memberId);
            memGroupIdList = CollectionsUtil.getPropertyValueList(memberGroupRelationList, "groupId");
        }

        if (Validator.isNullOrEmpty(memGroupIdList)){
            memGroupIdList = new ArrayList<Long>();
            memGroupIdList.add(-1L);
        }

        Set<String> cids = new HashSet<String>(); // 会员组合
        for (String key : crowdScopeConditionResultList.keySet()){
            CrowdScopeConditionResult crowdScopeConditionResult = (CrowdScopeConditionResult) crowdScopeConditionResultList.get(key);
            Boolean checkFlag = crowdScopeConditionResult.getResult(memberId, memGroupIdList);
            if (checkFlag == null){
                checkFlag = false;
            }
            if (checkFlag){
                cids.add(key);
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
}
