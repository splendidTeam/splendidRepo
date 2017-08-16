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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.handler.CombinedShoppingCartLineCommandFinder;
import com.baozun.nebula.sdk.utils.ManagerValidate;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.find;

/**
 * 专门处理购物车更新操作的业务类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.9
 */
@Transactional
@Service("sdkShoppingCartUpdateManager")
public class SdkShoppingCartUpdateManagerImpl implements SdkShoppingCartUpdateManager{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkShoppingCartUpdateManagerImpl.class);

    //---------------------------------------------------------------------

    /** The sdk shopping cart line dao. */
    @Autowired
    private SdkShoppingCartLineDao sdkShoppingCartLineDao;

    @Autowired
    private SdkShoppingCartQueryManager sdkShoppingCartQueryManager;

    @Autowired
    private ShoppingCartLinePackageInfoManager shoppingCartLinePackageInfoManager;

    @Autowired
    private CombinedShoppingCartLineCommandFinder combinedShoppingCartLineCommandFinder;

    //---------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartUpdateManager#updateCartLineQuantityByLineId(java.lang.Long, java.lang.Long, java.lang.Integer)
     */
    @Override
    public void updateCartLineQuantityByLineId(Long memberId,Long lineId,Integer quantity){
        Validate.notNull(memberId, "memberId can't be null!");
        Validate.notNull(lineId, "lineId can't be null!");
        Validate.notNull(quantity, "quantity can't be null!");

        int result = sdkShoppingCartLineDao.updateCartLineQuantityByLineId(memberId, lineId, quantity);
        ManagerValidate.isExpectedResult(1, result, "memberId:[{}}],update line:[{}] count:[{}]", memberId, lineId, quantity);

        //---------------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("updateCartLineQuantityByLineId success,memberId:[{}],lineId:[{}], quantity:[{}]", memberId, lineId, quantity);
        }
    }

    @Override
    public void updateCartLineQuantityAndSettlementByLineId(Long memberId,Long lineId,Integer quantity,boolean settlementState){
        Validate.notNull(memberId, "memberId can't be null!");
        Validate.notNull(lineId, "lineId can't be null!");
        Validate.notNull(quantity, "quantity can't be null!");
        Validate.notNull(settlementState, "settlementState can't be null!");
        int result = sdkShoppingCartLineDao.updateCartLineQuantityAndSettlementStateByLineId(memberId, lineId, quantity, settlementState == true ? 1 : 0);
        ManagerValidate.isExpectedResult(1, result, "memberId:[{}}],update line:[{}] count:[{}] settlementState:[{}]", memberId, lineId, quantity, settlementState);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartUpdateManager#updateCartLineQuantity(java.lang.Long, java.util.Map)
     */
    @Override
    public void updateCartLineQuantity(Long memberId,Map<Long, Integer> shoppingcartLineIdAndCountMap){
        Validate.notEmpty(shoppingcartLineIdAndCountMap, "shoppingcartLineIdAndCountMap can't be null/empty!");

        for (Map.Entry<Long, Integer> entry : shoppingcartLineIdAndCountMap.entrySet()){
            Long lineId = entry.getKey();
            Integer quantity = entry.getValue();

            updateCartLineQuantityByLineId(memberId, lineId, quantity);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartUpdateManager#updateCartLineSettlementState(java.lang.Long, java.util.List, boolean)
     */
    @Override
    public void updateCartLineSettlementState(Long memberId,List<Long> cartLineIdList,boolean settleState){
        Validate.notNull(memberId, "memberId can't be null!");
        Validate.notEmpty(cartLineIdList, "cartLineIdList can't be null/empty!");

        Integer updateCount = sdkShoppingCartLineDao.updateCartLineSettlementState(memberId, cartLineIdList, settleState ? 1 : 0);
        ManagerValidate.isExpectedResult(cartLineIdList.size(), updateCount, "memberId:[{}],update lines:[{}]'s status:[{}]", memberId, cartLineIdList, settleState);
    }

    //---------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartUpdateManager#updateCartLine(java.lang.Long, java.util.List, java.lang.Long)
     */
    @Override
    public void updateCartLine(Long memberId,List<ShoppingCartLineCommand> shoppingCartLineCommandList,Long shoppingcartLineId){
        Validate.notNull(memberId, "memberId can't be null!");
        Validate.notNull(shoppingcartLineId, "shoppingcartLineId can't be null!");
        Validate.notEmpty(shoppingCartLineCommandList, "shoppingCartLineCommandList can't be null/empty!");

        //----------------------------------------------------------------------------------------------------------
        //查找数据库中此时的数据
        List<ShoppingCartLineCommand> shoppingCartLineCommandListDB = sdkShoppingCartQueryManager.findShoppingCartLineCommandList(memberId);
        Validate.notEmpty(shoppingCartLineCommandListDB, "shoppingCartLineCommandListDB can't be null/empty!");

        //---------------------------------------------------------------------

        //db 里面的操作的shoppingcartLineId 对应的ShoppingCartLineCommand
        ShoppingCartLineCommand shoppingCartLineCommandInDB = find(shoppingCartLineCommandListDB, "id", shoppingcartLineId);
        Validate.notNull(shoppingCartLineCommandInDB, "shoppingCartLineCommandInDB can't be null!");

        //----------------------------------------------------------------------------------------------------------

        //当前 shoppingcartLineId 在内存list中的对象
        ShoppingCartLineCommand currentShoppingCartLineCommand = find(shoppingCartLineCommandList, "id", shoppingcartLineId);

        //----------------------------------------------------------------------------------------------------------

        //如果不存在,那么表示已经被合并了
        //被合并,那么需要找到合并的老数据,并删掉当前行
        if (null == currentShoppingCartLineCommand){

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("shoppingcartLineId:[{}],currentShoppingCartLineCommand is null,will update combinedShoppingCartLineCommand", shoppingcartLineId);
            }

            //找到合并的是哪条数据
            ShoppingCartLineCommand combinedShoppingCartLineCommand = combinedShoppingCartLineCommandFinder.find(shoppingCartLineCommandListDB, shoppingCartLineCommandList);
            Validate.notNull(combinedShoppingCartLineCommand, "combinedShoppingCartLineCommand can't be null!");

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug(
                                "shoppingcartLineId:[{}],shoppingCartLineCommandList:[{}],combinedShoppingCartLine id is:[{}]", //
                                shoppingcartLineId,
                                JsonUtil.formatWithIncludes(shoppingCartLineCommandList, "id"),
                                combinedShoppingCartLineCommand.getId());
            }

            //修改某行数据数量并且删除另外的一行(通常用于修改购物车行销售属性的时合并购物车行的场景).
            updateCartLineQuantityAndDeleteOtherLineId(memberId, shoppingcartLineId, combinedShoppingCartLineCommand);

        }

        //---------------------------------------------------------------------
        //如果存在,那么表示没有被合并,
        //没有被合并,那么需要更新当前行,且包装信息(目前是全删全插入)
        else{
            //修改行在db 里面的sku id
            Long skuIdInDB = shoppingCartLineCommandInDB.getSkuId();

            //相等表示不需要修改sku信息,不相等表示需要修改sku信息 
            Long newSkuId = skuIdInDB == currentShoppingCartLineCommand.getSkuId() ? null : currentShoppingCartLineCommand.getSkuId();
            updateCartLineSkuInfo(memberId, shoppingcartLineId, newSkuId, currentShoppingCartLineCommand.getQuantity());

            //delete 包装信息
            shoppingCartLinePackageInfoManager.deleteByShoppingCartLineId(shoppingcartLineId);

            //add 包装信息
            List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList = currentShoppingCartLineCommand.getShoppingCartLinePackageInfoCommandList();
            if (isNotNullOrEmpty(shoppingCartLinePackageInfoCommandList)){
                shoppingCartLinePackageInfoManager.savePackageInfo(shoppingcartLineId, shoppingCartLinePackageInfoCommandList);
            }
        }
    }

    /**
     * 修改某行数据数量并且删除另外的一行(通常用于修改购物车行销售属性的时合并购物车行的场景).
     * 
     * @param memberId
     * @param deleteLineId
     *            删掉那一行
     * @param combinedShoppingCartLineCommand
     *            更新这条数据
     * @since 5.3.2.13
     */
    private void updateCartLineQuantityAndDeleteOtherLineId(Long memberId,Long deleteLineId,ShoppingCartLineCommand combinedShoppingCartLineCommand){
        updateCartLineQuantityByLineId(memberId, combinedShoppingCartLineCommand.getId(), combinedShoppingCartLineCommand.getQuantity());

        int result = sdkShoppingCartLineDao.deleteByCartLineIdAndMemberId(memberId, deleteLineId);
        ManagerValidate.isExpectedResult(1, result, "memberId:[{}}],delete line:[{}]", memberId, deleteLineId);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("deleteByCartLineIdAndMemberId success,memberId:[{}],deleteLineId:[{}] ", memberId, deleteLineId);
        }
    }

    /**
     * 修改订单行 sku 信息.
     * 
     * @param memberId
     *            哪个会员
     * @param cartLineId
     *            哪个订单行
     * @param newSkuId
     *            新sku id,如果是null,那么不修改sku 信息
     * @param quantity
     *            数量是多少
     */
    private void updateCartLineSkuInfo(Long memberId,Long cartLineId,Long newSkuId,Integer quantity){
        Validate.notNull(memberId, "memberId can't be null!");
        Validate.notNull(cartLineId, "cartLineId can't be null!");
        Validate.notNull(quantity, "quantity can't be null!");

        int result = sdkShoppingCartLineDao.updateCartLineSkuInfo(memberId, cartLineId, newSkuId, quantity);
        ManagerValidate.isExpectedResult(1, result, "memberId:[{}],update lineId:[{}],change to newSkuId:[{}],quantity:[{}]", memberId, cartLineId, newSkuId, quantity);
    }

}
