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
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.SHOPPING_CART_LINE_SIZE_NOT_EXPECT;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.SUCCESS;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import com.baozun.nebula.sdk.manager.shoppingcart.extractor.ShoppingCartAddSameLineExtractor;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingcartAddDetermineSameLineElementsBuilder;
import com.baozun.nebula.web.controller.shoppingcart.builder.ToggleCheckStatusShoppingCartLinePredicateBuilder;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;
import com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartInventoryValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.add.ShoppingcartLineAddValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.update.ShoppingcartLineUpdateValidator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.MapUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.find;
import static com.feilong.core.util.CollectionsUtil.removeAll;
import static com.feilong.core.util.CollectionsUtil.select;

/**
 * 所有类型的购物车(会员/游客)操作的 抽象类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @author weihui.tang
 * @since 5.3.1
 */
public abstract class AbstractShoppingcartResolver implements ShoppingcartResolver{

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /** 相同行提取器. */
    @Autowired
    private ShoppingCartAddSameLineExtractor shoppingCartAddSameLineExtractor;

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

    /**  */
    @Autowired
    private ShoppingcartAddDetermineSameLineElementsBuilder shoppingcartAddDetermineSameLineElementsBuilder;

    /**  */
    @Autowired
    private ToggleCheckStatusShoppingCartLinePredicateBuilder toggleCheckStatusShoppingCartLinePredicateBuilder;

    //---------------------------------------------------------------------

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
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#addShoppingCart(com.baozun.nebula.web.MemberDetails, java.lang.Long[], java.lang.Integer, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartBatchAddResult addShoppingCart(MemberDetails memberDetails,Long[] skuIds,Integer count,HttpServletRequest request,HttpServletResponse response){
        Validate.notEmpty(skuIds, "skuIds can't be null/empty!");
        Validate.notNull(count, "count can't be null!");

        Validate.noNullElements(skuIds, "skuIds can't has null element");
        //---------------------------------------------------------------------
        Map<Long, Integer> skuIdAndCountMap = new LinkedHashMap<>();
        for (Long skuId : skuIds){
            Validate.isTrue(skuId > 0, "skuId:[%s] must > 0", skuId);
            skuIdAndCountMap.put(skuId, count);
        }
        return addShoppingCart(memberDetails, skuIdAndCountMap, null, request, response);
    }

    @Override
    public ShoppingcartBatchAddResult addShoppingCart(MemberDetails memberDetails,Map<Long, Integer> skuIdAndCountMap,ShoppingcartBatchAddOptions shoppingcartBatchAddOptions,HttpServletRequest request,HttpServletResponse response){
        Validate.notEmpty(skuIdAndCountMap, "skuIdAndCountMap can't be null/empty!");

        ShoppingcartBatchAddOptions useShoppingcartBatchAddOptions = defaultIfNull(shoppingcartBatchAddOptions, ShoppingcartBatchAddOptions.DEFAULT);

        //---------------------------------------------------------------------
        //批量添加的时候,如果某条失败了,是否继续.
        boolean isSkuAddFailContinue = useShoppingcartBatchAddOptions.isFailContinue();

        //TODO 待重构

        //********1. 校验************************************
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = defaultIfNullOrEmpty(getShoppingCartLineCommandList(memberDetails, request), new ArrayList<ShoppingCartLineCommand>());

        //FIXME 保证出错的数据 不能添加到集合
        Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap = buildSkuIdAndShoppingcartResultFailMap(memberDetails, shoppingCartLineCommandList, skuIdAndCountMap, isSkuAddFailContinue);
        //有错误
        if (isNotNullOrEmpty(skuIdAndShoppingcartResultFailMap)){
            return doWithHasFailMap(memberDetails, shoppingCartLineCommandList, skuIdAndCountMap, skuIdAndShoppingcartResultFailMap, isSkuAddFailContinue, request, response);
        }
        //----没有错误-----------------------------------------------------------------
        return addShoppingCart(memberDetails, shoppingCartLineCommandList, skuIdAndCountMap, null, request, response);

    }

    /**
     * @param memberDetails
     * @param shoppingCartLineCommandList
     * @param skuIdAndCountMap
     * @param skuIdAndShoppingcartResultFailMap
     * @param isSkuAddFailContinue
     * @param request
     * @param response
     * @return
     * @since 5.3.2.18
     */
    private ShoppingcartBatchAddResult doWithHasFailMap(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    Map<Long, Integer> skuIdAndCountMap,
                    Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap,
                    boolean isSkuAddFailContinue,
                    HttpServletRequest request,
                    HttpServletResponse response){
        //如果不继续,那么直接返回
        if (!isSkuAddFailContinue){
            return new ShoppingcartBatchAddResult(false, skuIdAndShoppingcartResultFailMap);
        }

        //全部出错了
        if (skuIdAndShoppingcartResultFailMap.size() == skuIdAndCountMap.size()){
            return new ShoppingcartBatchAddResult(false, skuIdAndShoppingcartResultFailMap);
        }

        //去除 有毛病的 sku ,其他的sku 继续添加
        Map<Long, Integer> canAddSkuIdAndCountMap = MapUtil.removeKeys(skuIdAndCountMap, toArray(skuIdAndShoppingcartResultFailMap.keySet(), Long.class));
        return addShoppingCart(memberDetails, shoppingCartLineCommandList, canAddSkuIdAndCountMap, skuIdAndShoppingcartResultFailMap, request, response);
    }

    /**
     * @param memberDetails
     * @param shoppingCartLineCommandList
     * @param canAddSkuIdAndCountMap
     * @param request
     * @param response
     * @return
     * @since 5.3.2.18
     */
    private ShoppingcartBatchAddResult addShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    Map<Long, Integer> canAddSkuIdAndCountMap,
                    Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap,
                    HttpServletRequest request,
                    HttpServletResponse response){
        //----------------构造需要被操作的购物车行-----------------------------------------------------
        List<ShoppingCartLineCommand> toBeOperatedShoppingCartLineCommandList = buildToBeOperatedShoppingCartLineCommandList(canAddSkuIdAndCountMap, shoppingCartLineCommandList);

        //---------------------------------------------------------------------
        ShoppingcartResult addShoppingCartShoppingcartResult = doAddShoppingCart(//
                        memberDetails,
                        shoppingCartLineCommandList,
                        toBeOperatedShoppingCartLineCommandList,
                        request,
                        response);

        postShoppingcartResult(memberDetails, addShoppingCartShoppingcartResult, shoppingCartLineCommandList, request, response);

        //---------------------------------------------------------------------
        //要没有错误的sku 才返回true
        boolean isSuccess = isNotNullOrEmpty(skuIdAndShoppingcartResultFailMap);
        return new ShoppingcartBatchAddResult(isSuccess, skuIdAndShoppingcartResultFailMap);
    }

    /**
     * @param memberDetails
     * @param shoppingCartLineCommandList
     * @param skuIdAndCountMap
     * @param useShoppingcartBatchAddOptions
     * @since 5.3.2.18
     */
    private Map<Long, ShoppingcartResult> buildSkuIdAndShoppingcartResultFailMap(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,Map<Long, Integer> skuIdAndCountMap,boolean isSkuAddFailContinue){
        Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : skuIdAndCountMap.entrySet()){
            Long skuId = entry.getKey();
            Integer count = entry.getValue();

            ShoppingCartLineAddForm shoppingCartLineAddForm = new ShoppingCartLineAddForm(skuId, count);
            //FIXME
            ShoppingcartResult validatorShoppingcartResult = shoppingcartLineAddValidator.validator(memberDetails, shoppingCartLineCommandList, shoppingCartLineAddForm);

            //---------------------------------------------------------------------
            if (null == validatorShoppingcartResult){
                continue;
            }
            //---------------------------------------------------------------------
            skuIdAndShoppingcartResultFailMap.put(skuId, validatorShoppingcartResult);
            if (isSkuAddFailContinue){
                continue;
            }
            break;
        }
        return skuIdAndShoppingcartResultFailMap;
    }

    /**
     * @param skuIdAndCountMap
     * @param shoppingCartLineCommandList
     * @return
     * @since 5.3.2.18
     */
    private List<ShoppingCartLineCommand> buildToBeOperatedShoppingCartLineCommandList(Map<Long, Integer> skuIdAndCountMap,List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        Validate.notEmpty(skuIdAndCountMap, "skuIdAndCountMap can't be null/empty!");

        //待操作的购物车行
        List<ShoppingCartLineCommand> mainLines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);

        List<ShoppingCartLineCommand> toBeOperatedShoppingCartLineCommandList = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : skuIdAndCountMap.entrySet()){
            Long skuId = entry.getKey();
            Integer count = entry.getValue();

            ShoppingCartLineAddForm shoppingCartLineAddForm = new ShoppingCartLineAddForm(skuId, count);
            ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand = shoppingCartAddSameLineExtractor.extractor(mainLines, shoppingcartAddDetermineSameLineElementsBuilder.build(shoppingCartLineAddForm));
            toBeOperatedShoppingCartLineCommandList.add(toBeOperatedShoppingCartLineCommand);
        }
        return toBeOperatedShoppingCartLineCommandList;
    }

    /**
     * @param memberDetails
     * @param shoppingCartLineCommandList
     *            总最终数据
     * @param toBeOperatedShoppingCartLineCommandList
     *            待操作的数据
     * @param request
     * @param response
     * @return
     * @since 5.3.2.18
     */
    protected abstract ShoppingcartResult doAddShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    List<ShoppingCartLineCommand> toBeOperatedShoppingCartLineCommandList,
                    HttpServletRequest request,
                    HttpServletResponse response);

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
        ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand = shoppingCartAddSameLineExtractor.extractor(mainLines, shoppingcartAddDetermineSameLineElementsBuilder.build(shoppingCartLineAddForm));
        ShoppingcartResult addShoppingCartShoppingcartResult = doAddShoppingCart(memberDetails, shoppingCartLineCommandList, toBeOperatedShoppingCartLineCommand, request, response);

        return postShoppingcartResult(memberDetails, addShoppingCartShoppingcartResult, shoppingCartLineCommandList, request, response);
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
        Validate.notNull(shoppingcartLineId, "shoppingcartLineId can't be null!");
        return deleteShoppingCartLine(memberDetails, toArray(shoppingcartLineId), request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#deleteShoppingCartLine(com.baozun.nebula.web.MemberDetails, java.lang.Long[], javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult deleteShoppingCartLine(MemberDetails memberDetails,Long[] shoppingcartLineIds,HttpServletRequest request,HttpServletResponse response){
        Validate.notEmpty(shoppingcartLineIds, "shoppingcartLineIds can't be null/empty!");

        //----------------------------------------------------------------------------------------

        // 获取购物车行信息
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);

        //----------------------------------------------------------------------------------------

        List<ShoppingCartLineCommand> selectWillDeleteLines = select(mainlines, "id", shoppingcartLineIds);
        if (isNullOrEmpty(selectWillDeleteLines)){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        //可能重复提交, 或者其他窗口,或者其他浏览器中已经删除, 此时最好让用户刷新下界面 重新选择删除
        if (selectWillDeleteLines.size() != shoppingcartLineIds.length){
            return SHOPPING_CART_LINE_SIZE_NOT_EXPECT;
        }

        //----------------------------------------------------------------------------------------
        shoppingCartLineCommandList = removeAll(shoppingCartLineCommandList, selectWillDeleteLines);

        ShoppingcartResult deleteShoppingCartResult = doDeleteShoppingCartLine(memberDetails, shoppingCartLineCommandList, shoppingcartLineIds, request, response);

        return postShoppingcartResult(memberDetails, deleteShoppingCartResult, shoppingCartLineCommandList, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#clearShoppingCartLine(com.baozun.nebula.web.MemberDetails, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult clearShoppingCartLine(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response){
        ShoppingcartResult clearShoppingCartResult = doClearShoppingCartLine(memberDetails, request, response);
        return postShoppingcartResult(memberDetails, clearShoppingCartResult, null, request, response);
    }

    //**************************************************************************************

    /**
     * 处理清空.
     * 
     * @param memberDetails
     * @param request
     * @param response
     * @return
     * @since 5.3.2.14
     */
    protected abstract ShoppingcartResult doClearShoppingCartLine(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response);

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

        return postShoppingcartResult(memberDetails, updateShoppingcartResult, shoppingCartLineCommandList, request, response);
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

        return postShoppingcartResult(memberDetails, updateShoppingcartResult, shoppingCartLineCommandList, request, response);
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

    //--------------------------------------------------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#toggleShoppingCartLineCheckStatus(com.baozun.nebula.web.
     * MemberDetails, java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult toggleShoppingCartLineCheckStatus(MemberDetails memberDetails,Long shoppingcartLineId,boolean checkStatus,HttpServletRequest request,HttpServletResponse response){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        if (isNullOrEmpty(mainlines)){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        //--------------------------------------------------------------------------

        // 找到实际需要操作的行
        ShoppingCartLineCommand needChangeCheckedCommand = find(mainlines, "id", shoppingcartLineId);

        // 如果已经是期望的状态,那么直接返回操作成功 (比如他在新窗口中已经操作过了)
        if (isSameCheckStatus(needChangeCheckedCommand.getSettlementState(), checkStatus)){
            return SUCCESS;
        }

        //--------------商品验证----------------------------------------------------------------------------

        if (checkStatus){ //如果是选中

            Long skuId = needChangeCheckedCommand.getSkuId();
            Sku sku = sdkSkuManager.findSkuById(skuId);

            //公共校验
            ShoppingcartResult commonValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, needChangeCheckedCommand.getQuantity());

            if (null != commonValidateShoppingcartResult){
                return commonValidateShoppingcartResult;
            }

            //校验库存
            if (shoppingCartInventoryValidator.isMoreThanInventory(shoppingCartLineCommandList, skuId)){
                return MAX_THAN_INVENTORY;
            }
        }

        needChangeCheckedCommand.setSettlementState(checkStatus ? 1 : 0);

        //---------------改变选中状态------------------------------------------------------------------------------

        ShoppingcartResult toggleShoppingcartResult = doToggleShoppingCartLineCheckStatus(memberDetails, shoppingCartLineCommandList, toList(needChangeCheckedCommand), checkStatus, request, response);

        return defaultIfNull(toggleShoppingcartResult, SUCCESS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#uncheckShoppingCartLines(com.baozun.nebula.web.MemberDetails, java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ShoppingcartResult uncheckShoppingCartLines(MemberDetails memberDetails,List<Long> shoppingcartLineIdList,HttpServletRequest request,HttpServletResponse response){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = getShoppingCartLineCommandList(memberDetails, request);
        List<ShoppingCartLineCommand> mainlines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);
        if (isNullOrEmpty(mainlines)){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        // 找到实际需要操作的行
        List<ShoppingCartLineCommand> needChangeCheckedCommandList = select(mainlines, "id", shoppingcartLineIdList);
        return toggleShoppingCartLineCheckStatus(memberDetails, shoppingCartLineCommandList, needChangeCheckedCommandList, false, request, response);
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
     * 每个实现类具体的删除实现.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            用户所有的购物车list(已经剔除了删除之后的,比如便于游客cookie 直接操作)
     * @param shoppingcartLineIds
     *            需要被删除的行
     * @param request
     *            the request
     * @param response
     *            the response
     * @return 如果操作过程中没有错,那么返回null
     * @since 5.3.2.14
     */
    protected abstract ShoppingcartResult doDeleteShoppingCartLine(
                    MemberDetails memberDetails,//
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    Long[] shoppingcartLineIds,
                    HttpServletRequest request,
                    HttpServletResponse response);

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
     * Toggle shopping cart line check status.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            该用户的所有的购物车行数据
     * @param needChangeCheckedCommandList
     *            需要被修改状态的购物车行数据
     * @param checkStatus
     *            需要被改成什么状态呢?如果是true,表示选中状态;false,表示是不选中状态
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

        //--------------------------------------------------------------------------------------------
        List<ShoppingCartLineCommand> toDoNeedChangeCheckedCommandList = select(needChangeCheckedCommandList, toggleCheckStatusShoppingCartLinePredicateBuilder.build(shoppingCartLineCommandList, checkStatus));
        if (isNullOrEmpty(toDoNeedChangeCheckedCommandList)){
            return SUCCESS;
        }

        CollectionsUtil.forEach(toDoNeedChangeCheckedCommandList, "settlementState", checkStatus ? 1 : 0);

        //---------------改变选中状态------------------------------------------------------------------------------
        ShoppingcartResult toggleShoppingcartResult = doToggleShoppingCartLineCheckStatus(memberDetails, shoppingCartLineCommandList, toDoNeedChangeCheckedCommandList, checkStatus, request, response);

        return defaultIfNull(toggleShoppingcartResult, SUCCESS);
    }

    /**
     * 是不是相同的状态(可能这家伙已经在其他窗口中操作过了).
     *
     * @param settlementState
     *            the settlement state
     * @param checkStatus
     *            the check status
     * @return true, if checks if is same check status
     */
    private static boolean isSameCheckStatus(Integer settlementState,boolean checkStatus){
        return checkStatus ? settlementState.equals(1) : settlementState.equals(0);
    }

    //---------------------------------------------------------------------

    /**
     * 操作执行之后的数据处理.
     * 
     * @param memberDetails
     * @param shoppingcartResult
     *            操作的结果
     * @param shoppingCartLineCommandList
     *            最终的购物车数据
     * @param request
     * @param response
     * @return 如果 null!=shoppingcartResult 直接返回;<br>
     *         否则会调用 {@link #afterOperateShoppingCart(MemberDetails, List, HttpServletRequest, HttpServletResponse)} 方法,没有异常的话,会返回 {@link ShoppingcartResult#SUCCESS}<br>
     *         如果 <code>shoppingCartLineCommandList</code> 是null或者是empty 视具体的实现,会重置cookie 购物车数量为0
     * @since 5.3.2.18
     */
    private ShoppingcartResult postShoppingcartResult(MemberDetails memberDetails,ShoppingcartResult shoppingcartResult,List<ShoppingCartLineCommand> shoppingCartLineCommandList,HttpServletRequest request,HttpServletResponse response){
        if (null != shoppingcartResult){
            return shoppingcartResult;
        }
        afterOperateShoppingCart(memberDetails, shoppingCartLineCommandList, request, response);
        return SUCCESS;
    }

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

}
