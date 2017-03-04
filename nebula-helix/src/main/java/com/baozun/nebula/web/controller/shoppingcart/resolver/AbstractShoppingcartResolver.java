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

import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.MAX_THAN_INVENTORY;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.SUCCESS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;
import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartInventoryValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartSameLineExtractor;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineAddValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineUpdateValidator;
import com.feilong.core.util.CollectionsUtil;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.find;
import static com.feilong.core.util.CollectionsUtil.select;

/**
 * The Class AbstractShoppingcartResolver.
 *
 * @author weihui.tang
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
public abstract class AbstractShoppingcartResolver implements ShoppingcartResolver{

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /** 相同行提取器. */
    @Autowired
    private ShoppingCartSameLineExtractor shoppingCartSameLineExtractor;

    /** The shoppingcart count persister. */
    @Autowired
    private ShoppingcartCountPersister shoppingcartCountPersister;

    /** The shoppingcart line update validator. */
    @Autowired
    private ShoppingcartLineUpdateValidator shoppingcartLineUpdateValidator;

    /** The shoppingcart line add validator. */
    @Autowired
    private ShoppingcartLineAddValidator shoppingcartLineAddValidator;

    /** The shoppingcart line operate common validator. */
    @Autowired
    private ShoppingcartLineOperateCommonValidator shoppingcartLineOperateCommonValidator;

    /** The shopping cart inventory validator. */
    @Autowired
    protected ShoppingCartInventoryValidator shoppingCartInventoryValidator;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#addShoppingCart(com.baozun.nebula.web.MemberDetails,
     * java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult addShoppingCart(MemberDetails memberDetails,Long skuId,Integer count,HttpServletRequest request,HttpServletResponse response){
        Validate.notNull(skuId, "skuId can't be null!");
        Validate.notNull(count, "count can't be null!");

        ShoppingCartLineAddForm shoppingCartLineAddForm = new ShoppingCartLineAddForm(skuId, count);
        return addShoppingCart(memberDetails, shoppingCartLineAddForm, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#addShoppingCart(com.baozun.nebula.web.MemberDetails, com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult addShoppingCart(MemberDetails memberDetails,ShoppingCartLineAddForm shoppingCartLineAddForm,HttpServletRequest request,HttpServletResponse response){
        Validate.notNull(shoppingCartLineAddForm, "shoppingCartLineAddForm can't be null!");

        final Long skuId = shoppingCartLineAddForm.getSkuId();
        final Integer count = shoppingCartLineAddForm.getCount();

        Validate.notNull(skuId, "skuId can't be null!");
        Validate.notNull(count, "count can't be null!");

        //********1. 校验************************************
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = defaultIfNullOrEmpty(getShoppingCartLineCommandList(memberDetails, request), new ArrayList<ShoppingCartLineCommand>());
        ShoppingcartResult validatorShoppingcartResult = shoppingcartLineAddValidator.validator(memberDetails, shoppingCartLineCommandList, shoppingCartLineAddForm);

        if (null != validatorShoppingcartResult){
            return validatorShoppingcartResult;
        }

        //待操作的购物车行
        List<ShoppingCartLineCommand> mainLines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand = shoppingCartSameLineExtractor.getSameLine(mainLines, shoppingCartLineAddForm);
        ShoppingcartResult addShoppingCartShoppingcartResult = doAddShoppingCart(memberDetails, shoppingCartLineCommandList, toBeOperatedShoppingCartLineCommand, request, response);

        if (null != addShoppingCartShoppingcartResult){
            return addShoppingCartShoppingcartResult;
        }
        afterOperateShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);
        return SUCCESS;
    }

    //**************************************************************************************
    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#deleteShoppingCartLine(com.baozun.nebula.web.
     * MemberDetails, java.lang.Long, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult deleteShoppingCartLine(MemberDetails memberDetails,Long shoppingcartLineId,HttpServletRequest request,HttpServletResponse response){
        // 获取购物车行信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);

        ShoppingCartLineCommand currentShoppingCartLineCommand = find(mainlines, "id", shoppingcartLineId);
        if (isNullOrEmpty(currentShoppingCartLineCommand)){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        shoppingCartLineCommandList = CollectionsUtil.remove(shoppingCartLineCommandList, currentShoppingCartLineCommand);

        ShoppingcartResult deleteShoppingCartResult = doDeleteShoppingCartLine(memberDetails, shoppingCartLineCommandList, currentShoppingCartLineCommand, request, response);

        if (null != deleteShoppingCartResult){
            return deleteShoppingCartResult;
        }

        afterOperateShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);
        return SUCCESS;
    }

    //**************************************************************************************

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#updateShoppingCartCount(com.baozun.nebula.web.
     * MemberDetails, java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult updateShoppingCartCount(MemberDetails memberDetails,Long shoppingcartLineId,Integer count,HttpServletRequest request,HttpServletResponse response){
        //不需要修改 sku 信息,仅修改 count
        ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm = new ShoppingCartLineUpdateSkuForm();
        shoppingCartLineUpdateSkuForm.setCount(count);

        return updateShoppingCartLine(memberDetails, shoppingcartLineId, shoppingCartLineUpdateSkuForm, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#updateShoppingCartCount(com.baozun.nebula.web.MemberDetails, java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult updateShoppingCartCount(MemberDetails memberDetails,Map<Long, Integer> shoppingcartLineIdAndCountMap,HttpServletRequest request,HttpServletResponse response){
        Validate.notEmpty(shoppingcartLineIdAndCountMap, "shoppingcartLineIdAndCountMap can't be null/empty!");

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);

        //2.校验****************************************************************

        for (Map.Entry<Long, Integer> entry : shoppingcartLineIdAndCountMap.entrySet()){
            Long shoppingcartLineId = entry.getKey();
            Integer count = entry.getValue();

            ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm = new ShoppingCartLineUpdateSkuForm();
            shoppingCartLineUpdateSkuForm.setCount(count);
            ShoppingcartResult validatorShoppingcartResult = shoppingcartLineUpdateValidator.validator(memberDetails, shoppingCartLineCommandList, shoppingcartLineId, shoppingCartLineUpdateSkuForm);
            if (null != validatorShoppingcartResult){
                return validatorShoppingcartResult;
            }
        }

        //3.不同方法的实现****************************************************************
        ShoppingcartResult updateShoppingcartResult = doBatchUpdateShoppingCart(memberDetails, shoppingCartLineCommandList, shoppingcartLineIdAndCountMap, request, response);

        if (null != updateShoppingcartResult){
            return updateShoppingcartResult;
        }

        //4.afterOperateShoppingCart****************************************
        afterOperateShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);
        return SUCCESS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#updateShoppingCartLine(com.baozun.nebula.web.MemberDetails, java.lang.Long, com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult updateShoppingCartLine(MemberDetails memberDetails,Long shoppingcartLineId,ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm,HttpServletRequest request,HttpServletResponse response){
        //1.取到原来的数据
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);

        //2.校验****************************************************************
        //2.1 校验 shoppingcartLineId
        ShoppingcartResult validatorShoppingcartResult = shoppingcartLineUpdateValidator.validator(memberDetails, shoppingCartLineCommandList, shoppingcartLineId, shoppingCartLineUpdateSkuForm);
        if (null != validatorShoppingcartResult){
            return validatorShoppingcartResult;
        }

        //3.不同方法的实现****************************************************************
        ShoppingcartResult updateShoppingcartResult = doUpdateShoppingCart(memberDetails, shoppingCartLineCommandList, shoppingcartLineId, request, response);

        if (null != updateShoppingcartResult){
            return updateShoppingcartResult;
        }

        //4.afterOperateShoppingCart****************************************
        afterOperateShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);
        return SUCCESS;
    }
    //******************************************************************************************************

    /**
     * 批量修改购物车行的不同实现.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param shoppingcartLineIdAndCountMap
     *            the shoppingcart line id and count map
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the shoppingcart result
     * @since 5.3.1.9
     * @since 5.3.2.3 rename
     */
    protected abstract ShoppingcartResult doBatchUpdateShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    Map<Long, Integer> shoppingcartLineIdAndCountMap,
                    HttpServletRequest request,
                    HttpServletResponse response);

    /**
     * 修改时候的具体实现.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            用户所有的购物车list(修改之后的)
     * @param shoppingcartLineId
     *            直接操作的行的id
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果操作过程中没有错,那么返回null
     * @since 5.3.2.3 change method param
     */
    protected abstract ShoppingcartResult doUpdateShoppingCart(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,Long shoppingcartLineId,HttpServletRequest request,HttpServletResponse response);

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#toggleShoppingCartLineCheckStatus(com.baozun.nebula.web.
     * MemberDetails, java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult toggleShoppingCartLineCheckStatus(MemberDetails memberDetails,Long shoppingcartLineId,boolean checkStatus,HttpServletRequest request,HttpServletResponse response){
        return toggleShoppingCartLinesCheckStatus(memberDetails, toList(shoppingcartLineId), checkStatus, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#toggleShoppingCartLinesCheckStatus(com.baozun.nebula.web.MemberDetails, java.util.List, boolean, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult toggleShoppingCartLinesCheckStatus(MemberDetails memberDetails,List<Long> shoppingcartLineIdList,boolean checkStatus,HttpServletRequest request,HttpServletResponse response){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        if (isNullOrEmpty(mainlines)){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        // 找到实际需要操作的行
        List<ShoppingCartLineCommand> needChangeCheckedCommandList = select(mainlines, "id", shoppingcartLineIdList);
        return toggleShoppingCartLineCheckStatus(memberDetails, shoppingCartLineCommandList, needChangeCheckedCommandList, checkStatus, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#toggleAllShoppingCartLineCheckStatus(com.baozun.nebula.
     * web.MemberDetails, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult toggleAllShoppingCartLineCheckStatus(MemberDetails memberDetails,boolean checkStatus,HttpServletRequest request,HttpServletResponse response){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        // 找不到 就抛
        if (isNullOrEmpty(mainlines)){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        return toggleShoppingCartLineCheckStatus(
                        memberDetails,
                        shoppingCartLineCommandList,
                        mainlines, // 找到实际需要操作的行
                        checkStatus,
                        request,
                        response);
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
     * @return 如果操作过程中没有错,那么返回null
     */
    protected abstract ShoppingcartResult doAddShoppingCart(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineCommand currentLine,HttpServletRequest request,HttpServletResponse response);

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
     * @return 如果操作过程中没有错,那么返回null
     */
    protected abstract ShoppingcartResult doDeleteShoppingCartLine(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineCommand currentLine,HttpServletRequest request,HttpServletResponse response);

    /**
     * Do select shopping cart line.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            所有的购物车行
     * @param needChangeCheckedStatusShoppingCartLineCommandList
     *            需要更改状态的购物车行list
     * @param checkStatus
     *            the settlement state
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果操作过程中没有错,那么返回null
     */
    protected abstract ShoppingcartResult doToggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    List<ShoppingCartLineCommand> needChangeCheckedStatusShoppingCartLineCommandList,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response);

    //**************************************************************************************

    /**
     * 操作购物车之后做的事情.
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
    private void afterOperateShoppingCart(@SuppressWarnings("unused") MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,HttpServletRequest request,HttpServletResponse response){
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
        if (isNullOrEmpty(needChangeCheckedCommandList)){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        //XXX feilong 这个目前会员购物车更新状态 需要这个参数
        List<String> extentionCodeList = new ArrayList<>();

        for (ShoppingCartLineCommand needChangeCheckedCommand : needChangeCheckedCommandList){
            // 跳过已经是该状态的购物车行
            if (isSameCheckStatus(needChangeCheckedCommand.getSettlementState(), checkStatus)){
                continue;
            }

            // ********* 商品验证***********
            Long skuId = needChangeCheckedCommand.getSkuId();
            Sku sku = sdkSkuManager.findSkuById(skuId);

            if (checkStatus){
                //公共校验
                ShoppingcartResult commonValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, needChangeCheckedCommand.getQuantity());

                if (null != commonValidateShoppingcartResult){
                    return commonValidateShoppingcartResult;
                }

                //校验库存
                if (shoppingCartInventoryValidator.isMoreThanInventory(shoppingCartLineCommandList, skuId, sku.getOutid())){
                    return MAX_THAN_INVENTORY;
                }
            }

            extentionCodeList.add(needChangeCheckedCommand.getExtentionCode());
            needChangeCheckedCommand.setSettlementState(checkStatus ? 1 : 0);

        }

        if (isNullOrEmpty(extentionCodeList)){
            return SUCCESS;
        }

        // ********* 改变选中状态***********
        ShoppingcartResult toggleShoppingcartResult = doToggleShoppingCartLineCheckStatus(memberDetails, shoppingCartLineCommandList, needChangeCheckedCommandList, checkStatus, request, response);

        if (null != toggleShoppingcartResult){
            return toggleShoppingcartResult;
        }
        return SUCCESS;
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

}
