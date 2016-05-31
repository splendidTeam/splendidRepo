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
package com.baozun.nebula.web.controller.shoppingcart.factory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;
import com.feilong.accessor.AutoKeyAccessor;
import com.feilong.core.Validator;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月22日 下午2:52:34
 * @since 5.3.1
 */
@Component("shoppingcartFactory")
public class ShoppingcartFactoryImpl implements ShoppingcartFactory{

    /** The guest shoppingcart resolver. */
    @Autowired
    @Qualifier("guestShoppingcartResolver")
    private ShoppingcartResolver guestShoppingcartResolver;

    /** The member shoppingcart resolver. */
    @Autowired
    @Qualifier("memberShoppingcartResolver")
    private ShoppingcartResolver memberShoppingcartResolver;

    @Autowired
    @Qualifier("immediatelyBuyAutoKeyAccessor")
    protected AutoKeyAccessor    autoKeyAccessor;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.ShoppingcartFactory#getShoppingcartResolver(com.baozun.nebula.web.MemberDetails)
     */
    @Override
    public ShoppingcartResolver getShoppingcartResolver(MemberDetails memberDetails){
        return null == memberDetails ? guestShoppingcartResolver : memberShoppingcartResolver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.ShoppingcartFactory#getShoppingCartLineCommandList(com.baozun.nebula.web.MemberDetails,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request){
        return getShoppingcartResolver(memberDetails).getShoppingCartLineCommandList(memberDetails, request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.ShoppingcartFactory#getShoppingCartLineCommandList(com.baozun.nebula.web.MemberDetails,
     * java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,String key,HttpServletRequest request){
        return Validator.isNullOrEmpty(key) ? getShoppingCartLineCommandList(memberDetails, request)
                        : (List<ShoppingCartLineCommand>) autoKeyAccessor.get(key, request);
    }
}
