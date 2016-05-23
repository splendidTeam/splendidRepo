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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkShoppingCartManager;
import com.baozun.nebula.web.MemberDetails;

/**
 * 会员购物车操作.
 *
 * @author weihui.tang
 * @author feilong
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
@Component("memberShoppingcartResolver")
public class MemberShoppingcartResolver extends AbstractShoppingcartResolver{

    /** The sdk shopping cart manager. */
    @Autowired
    private SdkShoppingCartManager sdkShoppingCartManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#getShoppingCartLineCommandList(com.baozun.nebula.web.
     * MemberDetails, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request){
        return sdkShoppingCartManager.findShoppingCartLinesByMemberId(memberDetails.getGroupId(), null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doAddShoppingCart(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doAddShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        currentLine.setMemberId(memberDetails.getGroupId());

        boolean result = sdkShoppingCartManager.merageShoppingCartLineById(memberDetails.getGroupId(), currentLine);
        if (!result){
            return ShoppingcartResult.OPERATE_ERROR;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doUpdateShoppingCart(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doUpdateShoppingCart(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        currentLine.setMemberId(memberDetails.getGroupId());

        boolean result = sdkShoppingCartManager.merageShoppingCartLineById(memberDetails.getGroupId(), currentLine);
        if (!result){
            return ShoppingcartResult.OPERATE_ERROR;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doDeleteShoppingCartLine(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doDeleteShoppingCartLine(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    ShoppingCartLineCommand currentLine,
                    HttpServletRequest request,
                    HttpServletResponse response){
        Integer result = sdkShoppingCartManager.removeShoppingCartLineById(memberDetails.getGroupId(), currentLine.getId());
        if (com.baozun.nebula.sdk.constants.Constants.FAILURE.equals(result)){
            return ShoppingcartResult.OPERATE_ERROR;
        }
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doToggleShoppingCartLineCheckStatus(com.baozun.
     * nebula.web.MemberDetails, java.util.List, java.util.List, boolean, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doToggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    List<String> extentionCodeList,
                    List<ShoppingCartLineCommand> needChangeCheckedCommandList,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response){
        sdkShoppingCartManager.updateCartLineSettlementState(memberDetails.getGroupId(), extentionCodeList, checkStatus ? 1 : 0);
        return null;
    }
}
