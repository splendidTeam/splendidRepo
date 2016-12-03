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
package com.baozun.nebula.web.controller.shoppingcart.validator;

import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.MAX_THAN_INVENTORY;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.ONE_LINE_MAX_THAN_COUNT;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.ONE_LINE_MAX_THAN_COUNT_AFTER_MERGED;
import static com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult.SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.find;
import static com.feilong.core.util.predicate.BeanPredicateUtil.equalPredicate;
import static org.apache.commons.collections4.PredicateUtils.notPredicate;

import java.util.List;

import org.apache.commons.collections4.PredicateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.feilong.core.util.CollectionsUtil;

/**
 * 默认实现.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.3
 */
@Component("shoppingcartLineUpdateValidator")
public class DefaultShoppingcartLineUpdateValidator extends AbstractShoppingcartLineOperateValidator implements ShoppingcartLineUpdateValidator{

    /** The sdk sku manager. */
    @Autowired
    private SdkSkuManager sdkSkuManager;

    /** The shoppingcart line operate common validator. */
    @Autowired
    private ShoppingcartLineOperateCommonValidator shoppingcartLineOperateCommonValidator;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLineUpdateValidator#doUpdateShoppingCartValidator(com.baozun.nebula.web.MemberDetails, java.util.List, java.lang.Long,
     * com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm)
     */
    @Override
    public ShoppingcartResult validator(
                    MemberDetails memberDetails,//
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    Long shoppingcartLineId,
                    ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm){
        ShoppingCartLineCommand currentShoppingCartLineCommand = find(shoppingCartLineCommandList, "id", shoppingcartLineId);
        if (null == currentShoppingCartLineCommand){
            return SHOPPING_CART_LINE_COMMAND_NOT_FOUND;
        }

        //订单行最终修改的全量数量(必填).
        Integer count = shoppingCartLineUpdateSkuForm.getCount();
        //订单行要修改成什么新的sku(如果是null,等同于仅修改数量).
        Long newSkuId = shoppingCartLineUpdateSkuForm.getNewSkuId();

        //是否需要修改sku 信息
        boolean isNeedChangeSku = isNeedChangeSku(currentShoppingCartLineCommand, newSkuId);
        if (isNeedChangeSku){
            currentShoppingCartLineCommand.setSkuId(newSkuId);//注意这里只修改了当前行的skuid  其他属性没有修改 比如价格等
        }

        //***********************************************************************************

        Long skuId = currentShoppingCartLineCommand.getSkuId();
        //2.2 CommonValidator
        Sku sku = sdkSkuManager.findSkuById(skuId);

        ShoppingcartResult commonValidateShoppingcartResult = shoppingcartLineOperateCommonValidator.validate(sku, count);
        if (null != commonValidateShoppingcartResult){
            return commonValidateShoppingcartResult;
        }

        //2.3 单行最大数量 校验
        ShoppingcartOneLineMaxQuantityValidator useShoppingcartOneLineMaxCountValidator = getUseShoppingcartOneLineMaxQuantityValidator();
        if (useShoppingcartOneLineMaxCountValidator.isGreaterThanMaxQuantity(memberDetails, skuId, count)){
            return ONE_LINE_MAX_THAN_COUNT;
        }

        boolean isNeedCombinedShoppingCartLine = false;

        //since 5.2.2.3
        //是否需要合并购物车行
        if (isNeedChangeSku){
            //相同的类型和sku 需要合并购物车行的 购物车行
            ShoppingCartLineCommand sameTypeAndSkuNeedCombinedShoppingCartLineCommand = getSameTypeAndSkuNeedCombinedShoppingCartLineCommand(shoppingCartLineCommandList, currentShoppingCartLineCommand);
            if (null != sameTypeAndSkuNeedCombinedShoppingCartLineCommand){
                isNeedCombinedShoppingCartLine = true;

                //如果需要合并,那么当前行删掉合并到需要合并的行 
                int totalQuantity = sameTypeAndSkuNeedCombinedShoppingCartLineCommand.getQuantity() + count;
                sameTypeAndSkuNeedCombinedShoppingCartLineCommand.setQuantity(totalQuantity);

                shoppingCartLineCommandList.remove(currentShoppingCartLineCommand);
                if (useShoppingcartOneLineMaxCountValidator.isGreaterThanMaxQuantity(memberDetails, skuId, totalQuantity)){
                    return ONE_LINE_MAX_THAN_COUNT_AFTER_MERGED;
                }
            }
        }
        //如果不需要合并,那么仅修改当前行数据 
        if (!isNeedCombinedShoppingCartLine){
            currentShoppingCartLineCommand.setQuantity(count);
            //如果有修改newSkuId 那么把 ExtentionCode 重新设置,以便后面使用
            currentShoppingCartLineCommand.setExtentionCode(sku.getOutid());
        }

        //2.4 库存校验
        if (shoppingCartInventoryValidator.isMoreThanInventory(shoppingCartLineCommandList, skuId, sku.getOutid())){
            return MAX_THAN_INVENTORY;
        }

        return null;
    }

    /**
     * 是否需要修改sku信息.
     *
     * @param currentShoppingCartLineCommand
     *            the current shopping cart line command
     * @param newSkuId
     *            新的skuid
     * @return 如果newSkuId 不是null,并且 newSkuId不等于现在的skuid 表示需要修改sku信息
     */
    private boolean isNeedChangeSku(ShoppingCartLineCommand currentShoppingCartLineCommand,Long newSkuId){
        return isNotNullOrEmpty(newSkuId) && newSkuId != currentShoppingCartLineCommand.getSkuId();
    }

    /**
     * Gets the same type and sku need combined shopping cart line command.
     *
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param currentShoppingCartLineCommand
     *            the current shopping cart line command
     * @return the same type and sku need combined shopping cart line command
     */
    private ShoppingCartLineCommand getSameTypeAndSkuNeedCombinedShoppingCartLineCommand(List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineCommand currentShoppingCartLineCommand){
        //skuid相同 lineGroup相同  relatedItemId相同
        return find(shoppingCartLineCommandList, PredicateUtils.<ShoppingCartLineCommand> allPredicate(//
                        equalPredicate("skuId", currentShoppingCartLineCommand.getSkuId()),
                        equalPredicate("lineGroup", currentShoppingCartLineCommand.getLineGroup()),
                        equalPredicate("relatedItemId", currentShoppingCartLineCommand.getRelatedItemId()),
                        notPredicate(equalPredicate("id", currentShoppingCartLineCommand.getId())))
        //
        );
    }
}
