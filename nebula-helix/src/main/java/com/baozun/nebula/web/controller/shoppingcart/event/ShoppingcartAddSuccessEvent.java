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
package com.baozun.nebula.web.controller.shoppingcart.event;

import java.util.List;

import com.baozun.nebula.event.AbstractShoppingcartEvent;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;

/**
 * nebula 购物车添加成功相关的事件.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
public class ShoppingcartAddSuccessEvent extends AbstractShoppingcartEvent{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 341115578760692711L;

    /** 谁. */
    private MemberDetails memberDetails;

    /** 他的总购物车行. */
    private List<ShoppingCartLineCommand> shoppingCartLineCommandList;

    /** 添加的购物车行. */
    private ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand;

    /**
     * Instantiates a new abstract shoppingcart event.
     *
     * @param source
     *            the source
     * @param memberDetails
     *            the member details
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param toBeOperatedShoppingCartLineCommand
     *            the to be operated shopping cart line command
     */
    public ShoppingcartAddSuccessEvent(Object source, MemberDetails memberDetails, List<ShoppingCartLineCommand> shoppingCartLineCommandList, ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand){
        super(source);
        this.memberDetails = memberDetails;
        this.shoppingCartLineCommandList = shoppingCartLineCommandList;
        this.toBeOperatedShoppingCartLineCommand = toBeOperatedShoppingCartLineCommand;
    }

    //---------------------------------------------------------------------

    /**
     * 获得 谁.
     *
     * @return the memberDetails
     */
    public MemberDetails getMemberDetails(){
        return memberDetails;
    }

    /**
     * 设置 谁.
     *
     * @param memberDetails
     *            the memberDetails to set
     */
    public void setMemberDetails(MemberDetails memberDetails){
        this.memberDetails = memberDetails;
    }

    /**
     * 获得 他的总购物车行.
     *
     * @return the shoppingCartLineCommandList
     */
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(){
        return shoppingCartLineCommandList;
    }

    /**
     * 设置 他的总购物车行.
     *
     * @param shoppingCartLineCommandList
     *            the shoppingCartLineCommandList to set
     */
    public void setShoppingCartLineCommandList(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        this.shoppingCartLineCommandList = shoppingCartLineCommandList;
    }

    /**
     * 获得 添加的购物车行.
     *
     * @return the toBeOperatedShoppingCartLineCommand
     */
    public ShoppingCartLineCommand getToBeOperatedShoppingCartLineCommand(){
        return toBeOperatedShoppingCartLineCommand;
    }

    /**
     * 设置 添加的购物车行.
     *
     * @param toBeOperatedShoppingCartLineCommand
     *            the toBeOperatedShoppingCartLineCommand to set
     */
    public void setToBeOperatedShoppingCartLineCommand(ShoppingCartLineCommand toBeOperatedShoppingCartLineCommand){
        this.toBeOperatedShoppingCartLineCommand = toBeOperatedShoppingCartLineCommand;
    }

}
