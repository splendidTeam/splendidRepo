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
package com.baozun.nebula.web.controller.shoppingcart.resolver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class AbstractShoppingcartResolver.
 *
 * @author weihui.tang
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
public abstract class AbstractShoppingcartResolver implements ShoppingcartResolver{

    /** The Constant log. */
    private static final Logger                    LOGGER = LoggerFactory.getLogger(AbstractShoppingcartResolver.class);

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager                          sdkSkuManager;

    /** The sdk item manager. */
    @Autowired
    private SdkItemManager                         sdkItemManager;

    /** The shoppingcart count persister. */
    @Autowired
    private ShoppingcartCountPersister             shoppingcartCountPersister;

    @Autowired
    private ShoppingcartLineOperateCommonValidator shoppingcartLineOperateCommonValidator;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#addShoppingCart(com.baozun.nebula.web.MemberDetails,
     * java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult addShoppingCart(
                    MemberDetails memberDetails,
                    Long skuId,
                    Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response){

        Validate.notNull(skuId, "skuId can't be null!");
        Validate.notNull(count, "count can't be null!");

        Sku sku = sdkSkuManager.findSkuById(skuId);
        ShoppingcartResult commandValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, count);

        if (null != commandValidateShoppingcartResult){
            return commandValidateShoppingcartResult;
        }

        // ****************************************************************************************
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        if (null == shoppingCartLineCommandList){
            shoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();
        }

        List<ShoppingCartLineCommand> mainLines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        ShoppingCartLineCommand currentShoppingCartLineCommand = findSameSkuShoppingCartLineCommand(mainLines, sku);

        if (null != currentShoppingCartLineCommand){// 存在
            currentShoppingCartLineCommand.setQuantity(currentShoppingCartLineCommand.getQuantity() + count);
        }else{
            // 最大行数验证
            // 校验是否超过购物车规定的商品行数
            if ((mainLines.size() + 1) > Constants.SHOPPING_CART_SKU_MAX_COUNT){
                return ShoppingcartResult.MAIN_LINE_MAX_THAN_COUNT;
            }

            // 构造一条 塞进去
            ShoppingCartLineCommand shoppingCartLineCommand = buildShoppingCartLineCommand(sku.getId(), count, sku.getOutid());
            shoppingCartLineCommandList.add(shoppingCartLineCommand);

            currentShoppingCartLineCommand = shoppingCartLineCommand;
        }

        if (isMoreThanInventory(shoppingCartLineCommandList, skuId, sku.getOutid())){
            return ShoppingcartResult.MAX_THAN_INVENTORY;
        }

        ShoppingcartResult addShoppingCartShoppingcartResult = doAddShoppingCart(
                        memberDetails,
                        shoppingCartLineCommandList,
                        currentShoppingCartLineCommand,
                        request,
                        response);

        if (null != addShoppingCartShoppingcartResult){
            return addShoppingCartShoppingcartResult;
        }
        afterMergeShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);
        return ShoppingcartResult.SUCCESS;
    }

    //**************************************************************************************
    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#deleteShoppingCartLine(com.baozun.nebula.web.
     * MemberDetails, java.lang.Long, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult deleteShoppingCartLine(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    HttpServletRequest request,
                    HttpServletResponse response){

        // 获取购物车行信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);

        ShoppingCartLineCommand currentShoppingCartLineCommand = CollectionsUtil.find(mainlines, "id", shoppingcartLineId);
        if (Validator.isNullOrEmpty(currentShoppingCartLineCommand)){
            return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        shoppingCartLineCommandList = CollectionsUtil.remove(shoppingCartLineCommandList, currentShoppingCartLineCommand);

        ShoppingcartResult deleteShoppingCartResult = doDeleteShoppingCartLine(
                        memberDetails,
                        shoppingCartLineCommandList,
                        currentShoppingCartLineCommand,
                        request,
                        response);

        if (null != deleteShoppingCartResult){
            return deleteShoppingCartResult;
        }

        afterMergeShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);

        return ShoppingcartResult.SUCCESS;
    }

    //**************************************************************************************

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#updateShoppingCartCount(com.baozun.nebula.web.
     * MemberDetails, java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult updateShoppingCartCount(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response){

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);

        // 找不到 就抛
        ShoppingCartLineCommand currentShoppingCartLineCommand = CollectionsUtil
                        .find(shoppingCartLineCommandList, "id", shoppingcartLineId);

        if (null == currentShoppingCartLineCommand){
            return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        Long skuId = currentShoppingCartLineCommand.getSkuId();
        Sku sku = sdkSkuManager.findSkuById(skuId);

        ShoppingcartResult commandValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, count);

        if (null != commandValidateShoppingcartResult){
            return commandValidateShoppingcartResult;
        }

        // ****************************************************************************************
        currentShoppingCartLineCommand.setQuantity(count);

        if (isMoreThanInventory(shoppingCartLineCommandList, skuId, sku.getOutid())){
            return ShoppingcartResult.MAX_THAN_INVENTORY;
        }

        ShoppingcartResult updateShoppingcartResult = doUpdateShoppingCart(
                        memberDetails,
                        shoppingCartLineCommandList,
                        currentShoppingCartLineCommand,
                        request,
                        response);

        if (null != updateShoppingcartResult){
            return updateShoppingcartResult;
        }

        afterMergeShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);

        return ShoppingcartResult.SUCCESS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#toggleShoppingCartLineCheckStatus(com.baozun.nebula.web.
     * MemberDetails, java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult toggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    Long shoppingcartLineId,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response){

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        if (Validator.isNullOrEmpty(mainlines)){
            return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        // 找到实际需要操作的行
        List<ShoppingCartLineCommand> needChangeCheckedCommandList = CollectionsUtil.select(mainlines, "id", shoppingcartLineId);
        return toggleShoppingCartLineCheckStatus(
                        memberDetails,
                        shoppingCartLineCommandList,
                        needChangeCheckedCommandList,
                        checkStatus,
                        request,
                        response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#toggleAllShoppingCartLineCheckStatus(com.baozun.nebula.
     * web.MemberDetails, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult toggleAllShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response){

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        // 找不到 就抛
        if (Validator.isNullOrEmpty(mainlines)){
            return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        return toggleShoppingCartLineCheckStatus(
                        memberDetails,
                        shoppingCartLineCommandList,
                        mainlines, // 找到实际需要操作的行
                        checkStatus,
                        request,
                        response);
    }

    /**
     * 从main 里面找相同 sku 的 ShoppingCartLineCommand.
     *
     * @param mainLines
     *            the main lines
     * @param sku
     *            the sku
     * @return the shopping cart line command
     */
    private ShoppingCartLineCommand findSameSkuShoppingCartLineCommand(List<ShoppingCartLineCommand> mainLines,Sku sku){
        //        return CollectionsUtil.find(mainLines, "extentionCode", sku.getOutid());
        return CollectionsUtil.find(mainLines, "skuId", sku.getId());
    }

    //**************************************************************************************
    /**
     * Do add shopping cart.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            用户所有的购物车list
     * @param currentLine
     *            需要被操作的行
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果没有操作过程中没有错,那么返回null
     */
    protected abstract ShoppingcartResult doAddShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * Do update shopping cart.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            用户所有的购物车list
     * @param currentLine
     *            需要被操作的行
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果没有操作过程中没有错,那么返回null
     */
    protected abstract ShoppingcartResult doUpdateShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 每个实现类具体的实现.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            用户所有的购物车list
     * @param currentLine
     *            需要被操作的行
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果没有操作过程中没有错,那么返回null
     */
    protected abstract ShoppingcartResult doDeleteShoppingCartLine(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * Do select shopping cart line.
     *
     * @param memberDetails
     *            the member details
     * @param extentionCodeList
     *            the extention code list
     * @param needChangeCheckedStatusShoppingCartLineCommandList
     *            需要更改状态的购物车行list
     * @param checkStatus
     *            the settlement state
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果没有操作过程中没有错,那么返回null
     */
    protected abstract ShoppingcartResult doToggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    List<String> extentionCodeList,
                    List<ShoppingCartLineCommand> needChangeCheckedStatusShoppingCartLineCommandList,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response);

    //**************************************************************************************

    /**
     * 转换为ShoppingCartLineCommand对象.
     *
     * @param skuId
     *            the sku id
     * @param quantity
     *            the quantity
     * @param extensionCode
     *            the extension code
     * @return the shopping cart line command
     */
    private ShoppingCartLineCommand buildShoppingCartLineCommand(Long skuId,Integer quantity,String extensionCode){
        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();
        shoppingCartLineCommand.setSkuId(skuId);
        shoppingCartLineCommand.setExtentionCode(extensionCode);
        shoppingCartLineCommand.setQuantity(quantity);
        shoppingCartLineCommand.setCreateTime(new Date());
        shoppingCartLineCommand.setSettlementState(Constants.CHECKED_CHOOSE_STATE);
        return shoppingCartLineCommand;
    }

    /**
     * merge购物车之后做的事情.
     * 
     * <p>
     * 通常
     * {@link #addShoppingCart(MemberDetails, Long, Long, HttpServletRequest, HttpServletResponse, Model) addShoppingCart},
     * {@link #updateShoppingCartCount(MemberDetails, Long, Long, HttpServletRequest, HttpServletResponse, Model) updateShoppingCartCount},
     * {@link #deleteShoppingCartLine(MemberDetails, Long, HttpServletRequest, HttpServletResponse, Model) deleteShoppingCartLine} 都需要调用他
     * </p>
     * 
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param request
     *            the request
     * @param response
     *            the response
     */
    private void afterMergeShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    HttpServletRequest request,
                    HttpServletResponse response){
        shoppingcartCountPersister.save(shoppingCartLineCommandList, request, response);
    }

    /**
     * Toggle shopping cart line check status.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param needChangeCheckedCommandList
     *            the need change checked command list
     * @param checkStatus
     *            the check status
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the shoppingcart result
     */
    private ShoppingcartResult toggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    List<ShoppingCartLineCommand> needChangeCheckedCommandList,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response){
        if (Validator.isNullOrEmpty(needChangeCheckedCommandList)){
            return ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        //TODO feilong 这个目前会员购物车更新状态 需要这个参数
        List<String> extentionCodeList = new ArrayList<String>();

        for (ShoppingCartLineCommand needChangeCheckedCommand : needChangeCheckedCommandList){
            // 跳过已经是该状态的购物车行
            if (isSameCheckStatus(needChangeCheckedCommand.getSettlementState(), checkStatus)){
                continue;
            }

            // ********* 商品验证***********
            Long skuId = needChangeCheckedCommand.getSkuId();
            Sku sku = sdkSkuManager.findSkuById(skuId);
            ShoppingcartResult commandValidateShoppingcartResult = shoppingcartLineOperateCommonValidator
                            .validate(sku, needChangeCheckedCommand.getQuantity());

            if (null != commandValidateShoppingcartResult){
                return commandValidateShoppingcartResult;
            }

            if (isMoreThanInventory(shoppingCartLineCommandList, skuId, sku.getOutid())){
                return ShoppingcartResult.MAX_THAN_INVENTORY;
            }

            extentionCodeList.add(needChangeCheckedCommand.getExtentionCode());
            needChangeCheckedCommand.setSettlementState(checkStatus ? 1 : 0);

        }

        if (Validator.isNullOrEmpty(extentionCodeList)){
            return ShoppingcartResult.SUCCESS;
        }

        // ********* 改变选中状态***********
        ShoppingcartResult toggleShoppingcartResult = doToggleShoppingCartLineCheckStatus(
                        memberDetails,
                        extentionCodeList,
                        needChangeCheckedCommandList,
                        checkStatus,
                        request,
                        response);

        if (null != toggleShoppingcartResult){
            return toggleShoppingcartResult;
        }

        return ShoppingcartResult.SUCCESS;
    }

    /**
     * 是不是相同的状态.
     *
     * @param settlementState
     *            the settlement state
     * @param checkStatus
     *            the check status
     * @return true, if checks if is same check status
     */
    private boolean isSameCheckStatus(Integer settlementState,boolean checkStatus){
        return checkStatus ? settlementState.equals(1) : settlementState.equals(0);
    }

    /**
     * 校验购物车里面的指定的skuId 是否超过库存量.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param skuId
     *            the sku id
     * @param extentionCode
     *            the out id
     * @return 如果超过库存量,返回true;否则返回false
     */
    private boolean isMoreThanInventory(List<ShoppingCartLineCommand> shoppingCartLineCommandList,Long skuId,String extentionCode){
        SkuInventory inventoryInDb = sdkItemManager.getSkuInventoryByExtentionCode(extentionCode);

        //相同 skuId 所有的 lines
        List<ShoppingCartLineCommand> sameSkuIdLines = CollectionsUtil.select(shoppingCartLineCommandList, "skuId", skuId);
        Integer sumStock = ShoppingCartUtil.getSumQuantity(sameSkuIdLines);
        return sumStock > inventoryInDb.getAvailableQty();
    }
}
