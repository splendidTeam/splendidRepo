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
package com.baozun.nebula.web.controller.shoppingcart;

import java.io.Serializable;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.NeedLogin;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResult;
import com.feilong.core.Validator;

/**
 * 
 * <p>
 * 主要由以下方法组成:
 * </p>
 *
 * 
 * @author feilong
 * @since 5.3.1
 */
public class NebulaImmediatelyBuyShoppingCartController extends NebulaAbstractImmediatelyBuyShoppingCartController{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaImmediatelyBuyShoppingCartController.class);

    @Autowired
    private SdkSkuManager       sdkSkuManager;

    @Autowired
    private SdkItemManager      sdkItemManager;

    /**
     * (立即购买)不走普通购物车直接走购物通道.
     *
     * @param memberDetails
     *            某个用户
     * @param skuId
     *            the sku id
     * @param count
     *            买几件
     * @param request
     *            the request
     * @param response
     *            the response
     * @param model
     *            the model
     * @return the nebula return result
     * @NeedLogin(guest = true)
     * @RequestMapping(value = "/transaction/immediatelybuy", method = RequestMethod.POST)
     */
    public NebulaReturnResult immediatelyBuy(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "skuId",required = true) Long skuId,
                    @RequestParam(value = "count",required = true) Integer count,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){
        // feilong validator
        Sku sku = sdkSkuManager.findSkuById(skuId);
        ShoppingcartResult shoppingcartResult = null;

        //===============① 数量不能小于1===============
        Validate.isTrue(count >= 1, "count:%s can not <1", count);

        //===============② 判断sku是否存在===============
        if (Validator.isNullOrEmpty(sku)){
            shoppingcartResult = ShoppingcartResult.SKU_NOT_EXIST;
        }

        // feilong 
        //===============③ 判断sku生命周期===============
        if (!sku.getLifecycle().equals(Sku.LIFE_CYCLE_ENABLE)){
            shoppingcartResult = ShoppingcartResult.SKU_NOT_ENABLE;
        }

        ItemCommand itemCommand = sdkItemManager.findItemCommandById(sku.getItemId());
        // item生命周期验证
        Integer lifecycle = itemCommand.getLifecycle();

        //===============④  判断item的生命周期===============
        if (!Constants.ITEM_ADDED_VALID_STATUS.equals(String.valueOf(lifecycle))){
            LOGGER.error("item id:{}, status is :{} can not operate in shoppingcart", itemCommand.getId(), lifecycle);
            shoppingcartResult = ShoppingcartResult.ITEM_STATUS_NOT_ENABLE;
        }

        // ********************************************************************************************
        //===============⑤  还没上架===============
        if (!checkActiveBeginTime(itemCommand)){
            // TODO feilong log
            shoppingcartResult = ShoppingcartResult.ITEM_NOT_ACTIVE_TIME;
        }

        //===============⑥ 赠品验证===============
        if (ItemInfo.TYPE_GIFT.equals(itemCommand.getType())){
            shoppingcartResult = ShoppingcartResult.ITEM_IS_GIFT;
        }

        DefaultReturnResult result = new DefaultReturnResult();
        if (shoppingcartResult == null){
            //	feilong 构造购物车信息
            List<ShoppingCartLineCommand> shoppingCartLineCommandList = buildShoppingCartLineCommandList(skuId, count);
            String key = autoKeyAccessor.save((Serializable) shoppingCartLineCommandList, request);

            // 跳转到订单确认页面的地址
            String checkoutUrl = buildCheckoutUrl(key, request);

            result.setResult(true);
            DefaultResultMessage message = new DefaultResultMessage();
            message.setMessage(checkoutUrl);
            result.setResultMessage(message);
        }else{
            //  失败就直接返回失败的信息
            result.setResult(false);
            String messageStr = getMessage(shoppingcartResult.toString());
            DefaultResultMessage message = new DefaultResultMessage();
            message.setMessage(messageStr);
            result.setResultMessage(message);
            LOGGER.error(messageStr);
        }
        return result;
    }

    private Boolean checkActiveBeginTime(ItemCommand item){
        Date activeBeginTime = item.getActiveBeginTime();
        return null == activeBeginTime ? true : activeBeginTime.before(new Date());
    }

    private List<ShoppingCartLineCommand> buildShoppingCartLineCommandList(Long skuId,Integer count){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();

        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();

        shoppingCartLineCommand.setSkuId(skuId);
        shoppingCartLineCommand.setQuantity(count);

        //选中
        shoppingCartLineCommand.setSettlementState(1);

        shoppingCartLineCommandList.add(shoppingCartLineCommand);
        return shoppingCartLineCommandList;
    }
}
