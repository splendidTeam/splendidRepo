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
package com.baozun.nebula.web.controller.shoppingcart.validator.update;

import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.MAX_THAN_INVENTORY;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.ONE_LINE_MAX_THAN_COUNT;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.ONE_LINE_MAX_THAN_COUNT_AFTER_MERGED;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.TOTAL_MAX_THAN_QUANTITY;
import static com.feilong.core.util.CollectionsUtil.collect;
import static com.feilong.core.util.CollectionsUtil.find;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.shoppingcart.extractor.ShoppingCartUpdateNeedCombinedLineExtractor;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingcartUpdateDetermineSameLineElementsBuilder;
import com.baozun.nebula.web.controller.shoppingcart.form.PackageInfoForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResultUtil;
import com.baozun.nebula.web.controller.shoppingcart.validator.AbstractShoppingcartLineOperateValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineOperateCommonValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLinePackageInfoFormListValidator;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartMaxTotalQuantityValidator;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * 默认实现.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.3
 * @since 5.3.2.14 move new package
 */
@Component("shoppingcartLineUpdateValidator")
public class DefaultShoppingcartLineUpdateValidator extends AbstractShoppingcartLineOperateValidator implements ShoppingcartLineUpdateValidator{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShoppingcartLineUpdateValidator.class);

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /** The shoppingcart line operate common validator. */
    @Autowired
    private ShoppingcartLineOperateCommonValidator shoppingcartLineOperateCommonValidator;

    /**  */
    @Autowired
    private ShoppingcartLinePackageInfoFormListValidator shoppingcartLinePackageInfoFormListValidator;

    /**  */
    @Autowired
    private ShoppingcartUpdateDetermineSameLineElementsBuilder shoppingcartUpdateDetermineSameLineElementsBuilder;

    /**  */
    @Autowired(required = false)
    private ShoppingcartLineUpdateValidatorConfigBuilder shoppingcartLineUpdateValidatorConfigBuilder;

    /** 购物车修改的时候相同行提取器. */
    @Autowired
    private ShoppingCartUpdateNeedCombinedLineExtractor shoppingCartUpdateNeedCombinedLineExtractor;

    /**
     * 购物车购买最大数量校验
     * 
     * @since 5.3.2.22
     */
    @Autowired
    private ShoppingcartMaxTotalQuantityValidator shoppingcartMaxTotalQuantityValidator;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineUpdateValidator#validator(com.baozun.nebula.web.MemberDetails, java.util.List, java.lang.Long,
     * com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm)
     */
    @Override
    public ShoppingcartResult validator(
                    MemberDetails memberDetails,//
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    Long shoppingcartLineId,
                    ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm){
        Validate.notNull(shoppingcartLineId, "shoppingcartLineId can't be null!");
        Validate.notNull(shoppingCartLineUpdateSkuForm, "shoppingCartLineUpdateSkuForm can't be null!");

        ShoppingcartLineUpdateValidatorConfigBuilder useShoppingcartLineUpdateValidatorConfigBuilder = defaultIfNull(shoppingcartLineUpdateValidatorConfigBuilder, new DefaultShoppingcartLineUpdateValidatorConfigBuilder());
        ShoppingcartLineUpdateValidatorConfig shoppingcartLineUpdateValidatorConfig = useShoppingcartLineUpdateValidatorConfigBuilder.build(memberDetails, shoppingCartLineCommandList, shoppingcartLineId, shoppingCartLineUpdateSkuForm);

        //--------------包装信息校验-------------------------------------------------------------------------
        shoppingcartLinePackageInfoFormListValidator.validator(shoppingCartLineUpdateSkuForm.getPackageInfoFormList());

        ShoppingCartLineCommand currentShoppingCartLineCommand = find(shoppingCartLineCommandList, "id", shoppingcartLineId);
        if (null == currentShoppingCartLineCommand){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        //---------------------------------------------------------------------------------------

        //--操作行最新的sku id 如果没有变更那么值是原来的sku id,如果变更了那么是新的sku id
        Long targetSkuId = defaultIfNull(shoppingCartLineUpdateSkuForm.getNewSkuId(), currentShoppingCartLineCommand.getSkuId());

        //2.2 CommonValidator
        Sku targetSku = sdkSkuManager.findSkuById(targetSkuId);

        //订单行最终修改的全量数量(必填).
        Integer count = shoppingCartLineUpdateSkuForm.getCount();
        ShoppingcartResult commonValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(targetSku, count);
        if (ShoppingcartResultUtil.isNotSuccess(commonValidateShoppingcartResult)){
            return commonValidateShoppingcartResult;
        }

        //---------------------------------------------------------------------------------------

        //2.3 单行最大数量 校验
        if (shoppingcartOneLineMaxQuantityValidator.isGreaterThanMaxQuantity(memberDetails, targetSkuId, count)){
            return ONE_LINE_MAX_THAN_COUNT;
        }

        // -----------商品总数量验证-----------------------------------------------------------------------------
        //since 5.3.2.22
        //计算修改后购物车商品总数量
        Integer sumTotalQuantity = sumTotalQuantity(shoppingCartLineCommandList, currentShoppingCartLineCommand, count);
        //校验添加后购物车商品总数是否超过规定最大数量
        if (shoppingcartMaxTotalQuantityValidator.isGreaterThanMaxQuantity(memberDetails, sumTotalQuantity)){
            return TOTAL_MAX_THAN_QUANTITY;

        }

        //-----------------------------------------------------------------------------------------

        //since 5.2.2.3
        //是否需要合并购物车行
        //相同的类型和sku 需要合并购物车行的 购物车行
        ShoppingCartLineCommand needCombinedShoppingCartLineCommand = shoppingCartUpdateNeedCombinedLineExtractor.extractor(//
                        shoppingCartLineCommandList,
                        shoppingcartUpdateDetermineSameLineElementsBuilder.build(currentShoppingCartLineCommand, shoppingCartLineUpdateSkuForm));

        if (null == needCombinedShoppingCartLineCommand){
            LOGGER.debug("can not find need Combined ShoppingCartLineCommand,just update self line data");

            //没有找到,那么更新当前行
            updateCurrentShoppingCartLineCommand(currentShoppingCartLineCommand, shoppingCartLineUpdateSkuForm, targetSku);
        }else{

            //---------------------------------------------------------------
            int totalQuantity = needCombinedShoppingCartLineCommand.getQuantity() + count;
            //校验单行库存
            if (shoppingcartLineUpdateValidatorConfig.getIsCheckSingleLineSkuInventory() && shoppingcartOneLineMaxQuantityValidator.isGreaterThanMaxQuantity(memberDetails, targetSkuId, totalQuantity)){
                return ONE_LINE_MAX_THAN_COUNT_AFTER_MERGED;
            }

            //---------------------------------------------------------------

            needCombinedShoppingCartLineCommand.setQuantity(totalQuantity);
            LOGGER.debug("need Combined Line:[{}],update Quantity to:[{}]", needCombinedShoppingCartLineCommand.getId(), totalQuantity);

            //---------------------------------------------------------------

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("shoppingCartLine List:[{}] will remove current Line :[{}]", JsonUtil.formatWithIncludes(shoppingCartLineCommandList, "id"), currentShoppingCartLineCommand.getId());
            }

            //如果需要合并,那么当前行删掉合并到需要合并的行 
            shoppingCartLineCommandList.remove(currentShoppingCartLineCommand);
        }

        //----------------------------------------------------

        //2.4 相同的sku库存校验
        if (shoppingcartLineUpdateValidatorConfig.getIsCheckTotalLineSameSkuInventory() && shoppingCartInventoryValidator.isMoreThanInventory(shoppingCartLineCommandList, targetSkuId)){
            return MAX_THAN_INVENTORY;
        }

        return null;
    }

    /**
     * 计算当前购物车商品总数量(修改后)
     * 
     * @return
     *         商品总数量
     * 
     * @since 5.3.2.22
     */
    private Integer sumTotalQuantity(List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineCommand currentShoppingCartLineCommand,Integer count){
        //计算修改前购物车商品总数量
        Integer currentTotalCount = ShoppingCartUtil.getSumQuantity(shoppingCartLineCommandList);
        //修改后购物车商品总数量=当前购物车总数量-修改的购物车行原来购买数量+修改后的数量
        return currentTotalCount - currentShoppingCartLineCommand.getQuantity() + count;
    }

    /**
     * 如果不需要合并,那么仅修改当前行数据 .
     *
     * @param currentShoppingCartLineCommand
     * @param shoppingCartLineUpdateSkuForm
     * @param sku
     * @since 5.3.2.13
     */
    protected void updateCurrentShoppingCartLineCommand(ShoppingCartLineCommand currentShoppingCartLineCommand,ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm,Sku sku){
        Long id = sku.getId();
        Integer count = shoppingCartLineUpdateSkuForm.getCount();
        String outid = sku.getOutid();

        currentShoppingCartLineCommand.setSkuId(id);//注意这里只修改了当前行的skuid  其他属性没有修改 比如价格等

        currentShoppingCartLineCommand.setQuantity(count);

        //如果有修改newSkuId 那么把 ExtentionCode 重新设置,以便后面使用
        currentShoppingCartLineCommand.setExtentionCode(outid);

        //since 5.3.2.13
        currentShoppingCartLineCommand.setShoppingCartLinePackageInfoCommandList(buildShoppingCartLinePackageInfoCommandList(shoppingCartLineUpdateSkuForm.getPackageInfoFormList()));

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("update self line:[{}],new info:{}", currentShoppingCartLineCommand.getId(), JsonUtil.formatWithIncludes(currentShoppingCartLineCommand, "skuId", "quantity", "extentionCode", "shoppingCartLinePackageInfoCommandList"));
        }
    }

    /**
     * 可能需要考虑 包装信息的变更.
     * 
     * <h3>关于修改购物车行时,包装信息修改:</h3>
     * <blockquote>
     * 
     * <p>
     * 不过对于reebok 而言, 只有一条包装信息,<br>
     * </p>
     * 
     * <ol>
     * 
     * <li>要么原来就没有,现在也没有;</li>
     * <li>要么原来就没有,现在有;</li>
     * 
     * 
     * <li>要么原来有,现在有且没有变;</li>
     * <li>要么原来有,现在有且是新的;</li>
     * <li>要么原来有,现在没有;</li>
     * 
     * </ol>
     * 
     * <p>
     * 如果要做成标准化,可能还会有多重组合场景
     * </p>
     * </blockquote>
     * 
     * @param packageInfoFormList
     * @return
     * @since 5.3.2.13
     */
    private static List<ShoppingCartLinePackageInfoCommand> buildShoppingCartLinePackageInfoCommandList(List<PackageInfoForm> packageInfoFormList){
        return collect(packageInfoFormList, ShoppingCartLinePackageInfoCommand.class, "type", "featureInfo", "total", "extendInfo");
    }

}
