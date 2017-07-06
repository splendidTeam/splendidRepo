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
package com.baozun.nebula.web.controller.shoppingcart.validator.add;

import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.MAIN_LINE_MAX_THAN_COUNT;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.MAX_THAN_INVENTORY;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.ONE_LINE_MAX_THAN_COUNT;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.shoppingcart.extractor.ShoppingCartAddSameLineExtractor;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingCartLineCommandBuilder;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingcartAddDetermineSameLineElementsBuilder;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResultUtil;
import com.baozun.nebula.web.controller.shoppingcart.validator.AbstractShoppingcartLineOperateValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.DefaultShoppingcartTotalLineMaxSizeValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLinePackageInfoFormListValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartOneLineMaxQuantityValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartTotalLineMaxSizeValidator;

import static com.feilong.core.util.CollectionsUtil.select;

/**
 * The Class DefaultShoppingcartLineAddValidator.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.3
 * @since 5.3.2.14 move new package
 */
@Component("shoppingcartLineAddValidator")
public class DefaultShoppingcartLineAddValidator extends AbstractShoppingcartLineOperateValidator implements ShoppingcartLineAddValidator{

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /**  */
    @Autowired
    private ShoppingCartLineCommandBuilder shoppingCartLineCommandBuilder;

    /** 购物车添加的时候相同行提取器. */
    @Autowired
    private ShoppingCartAddSameLineExtractor shoppingCartAddSameLineExtractor;

    /** The shoppingcart total line max size validator. */
    @Autowired(required = false)
    private ShoppingcartTotalLineMaxSizeValidator shoppingcartTotalLineMaxSizeValidator;

    /** The shoppingcart line operate common validator. */
    @Autowired
    private ShoppingcartLineOperateCommonValidator shoppingcartLineOperateCommonValidator;

    /**  */
    @Autowired
    private ShoppingcartLinePackageInfoFormListValidator shoppingcartLinePackageInfoFormListValidator;

    /**  */
    @Autowired
    private ShoppingcartAddDetermineSameLineElementsBuilder shoppingcartAddDetermineSameLineElementsBuilder;

    //---------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineAddValidator#validator(com.baozun.nebula.web.MemberDetails, java.util.List, com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm)
     */
    @Override
    public ShoppingcartResult validator(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineAddForm shoppingCartLineAddForm){
        Validate.notNull(shoppingCartLineAddForm, "shoppingCartLineAddForm can't be null!");

        final Long skuId = shoppingCartLineAddForm.getSkuId();
        final Integer count = shoppingCartLineAddForm.getCount();

        Validate.notNull(skuId, "skuId can't be null!");
        Validate.notNull(count, "count can't be null!");

        //--------------------------------校验包装信息-------------------------------------

        shoppingcartLinePackageInfoFormListValidator.validator(shoppingCartLineAddForm.getPackageInfoFormList());

        //--------------------1.common 校验--------------------------------------------
        Sku sku = sdkSkuManager.findSkuById(skuId);
        ShoppingcartResult commonValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, count);

        if (ShoppingcartResultUtil.isNotSuccess(commonValidateShoppingcartResult)){
            return commonValidateShoppingcartResult;
        }

        //---------------------------------------------------------------------
        return validatorAdd(memberDetails, shoppingCartLineCommandList, shoppingCartLineAddForm, sku);
    }

    /**
     * add 校验.
     *
     * @param memberDetails
     * @param shoppingCartLineCommandList
     *            用户对应的购物车行
     * @param shoppingCartLineAddForm
     *            购物车添加的 form
     * @param sku
     *            对应的sku
     * @return
     * @since 5.3.2.18
     */
    private ShoppingcartResult validatorAdd(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineAddForm shoppingCartLineAddForm,Sku sku){
        final Long skuId = shoppingCartLineAddForm.getSkuId();
        final Integer count = shoppingCartLineAddForm.getCount();

        List<ShoppingCartLineCommand> mainLines = ShoppingCartUtil.getMainShoppingCartLineCommandList(shoppingCartLineCommandList);

        //待操作的购物车行
        ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand = shoppingCartAddSameLineExtractor.extractor(mainLines, shoppingcartAddDetermineSameLineElementsBuilder.build(shoppingCartLineAddForm));

        //------------2.单行 最大购买数校验-----------------------------------------------------------------------------------
        //是否已经在购物车里面有,
        //如果有,那么这一行累计数量进行校验; 如果没有那么仅仅校验传入的数量
        Integer oneLineTotalCount = null != toBeOperatedShoppingCartLineCommand ? toBeOperatedShoppingCartLineCommand.getQuantity() + count : count;

        ShoppingcartOneLineMaxQuantityValidator useShoppingcartOneLineMaxCountValidator = getUseShoppingcartOneLineMaxQuantityValidator();
        if (useShoppingcartOneLineMaxCountValidator.isGreaterThanMaxQuantity(memberDetails, skuId, oneLineTotalCount)){
            return ONE_LINE_MAX_THAN_COUNT;
        }

        //是否存在相同行
        boolean isExistSameLine = null != toBeOperatedShoppingCartLineCommand;

        // -----------3.最大行数验证-----------------------------------------------------------------------------
        // 如果没有找到相同的行, 那么需要创建一个line,所以需要校验是否超过购物车规定的商品行数
        if (!isExistSameLine){
            ShoppingcartTotalLineMaxSizeValidator useShoppingcartTotalLineMaxSizeValidator = defaultIfNull(shoppingcartTotalLineMaxSizeValidator, new DefaultShoppingcartTotalLineMaxSizeValidator());
            if (useShoppingcartTotalLineMaxSizeValidator.isGreaterThanMaxSize(memberDetails, mainLines.size() + 1)){
                return MAIN_LINE_MAX_THAN_COUNT;
            }
        }

        //---------------------------------------------------------------------
        return validateInventory(shoppingCartLineCommandList, shoppingCartLineAddForm, sku, toBeOperatedShoppingCartLineCommand, oneLineTotalCount);
    }

    /**
     * 
     *
     * @param shoppingCartLineCommandList
     * @param shoppingCartLineAddForm
     * @param sku
     * @param toBeOperatedShoppingCartLineCommand
     * @param oneLineTotalCount
     * @return
     * @since 5.3.2.18
     */
    private ShoppingcartResult validateInventory(List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineAddForm shoppingCartLineAddForm,Sku sku,ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand,Integer oneLineTotalCount){
        final Long skuId = shoppingCartLineAddForm.getSkuId();

        Integer count = shoppingCartLineAddForm.getCount();
        //---------------------------------------------------------------------
        Integer useSumBuyCount = buildSameSkuIdTotalBuyCount(shoppingCartLineCommandList, skuId, count);

        //************4.统计购物车所有相同 skuid 库存校验*************************
        if (shoppingCartInventoryValidator.isMoreThanInventory(skuId, useSumBuyCount)){
            return MAX_THAN_INVENTORY;
        }

        packShoppingCart(shoppingCartLineCommandList, shoppingCartLineAddForm, sku, toBeOperatedShoppingCartLineCommand, oneLineTotalCount);

        //---------------------------------------------------------------------
        return null;
    }

    /**
     * 操作购物车.
     * 
     * <p>
     * 如果存在相同line ,那么行数量变更;<br>
     * 如果不存在,那么构造个新的数据
     * </p>
     * 
     * @param shoppingCartLineCommandList
     * @param shoppingCartLineAddForm
     * @param sku
     * @param toBeOperatedShoppingCartLineCommand
     * @param oneLineTotalCount
     * @since 5.3.2.18
     */
    private void packShoppingCart(List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineAddForm shoppingCartLineAddForm,Sku sku,ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand,Integer oneLineTotalCount){
        //---------------------------------------------------------------------
        //是否存在相同行
        boolean isExistSameLine = null != toBeOperatedShoppingCartLineCommand;
        if (isExistSameLine){// 存在

            //引用对象 重新赋予新值
            toBeOperatedShoppingCartLineCommand.setQuantity(oneLineTotalCount);
        }else{
            // 构造一条 塞进去
            ShoppingCartLineCommand shoppingCartLineCommand = shoppingCartLineCommandBuilder.build(shoppingCartLineAddForm, sku.getOutid());
            shoppingCartLineCommandList.add(shoppingCartLineCommand);
        }
    }

    /**
     * 构造指定skuid 总购买量.
     *
     * @param shoppingCartLineCommandList
     *            原来的购物车
     * @param skuId
     * @param newBuyCount
     *            新买的数量
     * @return
     * @since 5.3.2.18
     */
    private Integer buildSameSkuIdTotalBuyCount(List<ShoppingCartLineCommand> shoppingCartLineCommandList,Long skuId,Integer newBuyCount){
        //相同 skuId 所有的 lines
        List<ShoppingCartLineCommand> sameSkuIdLines = select(shoppingCartLineCommandList, "skuId", skuId);

        //没加入之前购物车 里面sku id 的count
        Integer sumBuyCount = ShoppingCartUtil.getSumQuantity(sameSkuIdLines);

        //不管是 合并 还是新增行, 都是拿已经购买的 skuid count + 新购买的行 count
        return sumBuyCount + newBuyCount;
    }
}
