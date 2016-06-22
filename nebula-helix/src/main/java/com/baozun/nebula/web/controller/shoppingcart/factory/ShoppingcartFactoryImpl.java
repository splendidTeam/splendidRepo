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

import java.util.ArrayList;
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
import com.feilong.core.bean.BeanUtil;

/**
 * The Class ShoppingcartFactoryImpl.
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

    /** The auto key accessor. */
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

    @Override
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,String key,HttpServletRequest request){
        return Validator.isNullOrEmpty(key) ? getShoppingCartLineCommandList(memberDetails, request) : getFromAccessor(key, request);
    }

    /**
     * Gets the from accessor.
     * <p>
     * 由于保存到session中的对象 是引用类型,操作会直接影响到session里面保存的对象, 比如在订单确认页面 使用优惠券和取消使用优惠券会影响到session里面的对象
     * </p>
     * 
     * @param key
     *            the key
     * @param request
     *            the request
     * @return the from accessor
     * @see com.baozun.nebula.web.controller.shoppingcart.NebulaAbstractImmediatelyBuyShoppingCartController#saveToAccessor(List,
     *      HttpServletRequest)
     * @since 5.3.1.2
     */
    private List<ShoppingCartLineCommand> getFromAccessor(String key,HttpServletRequest request){
        //        Serializable serializable = autoKeyAccessor.get(key, request);
        //        return JsonUtil.toList(serializable.toString(), ShoppingCartLineCommand.class);

        List<ShoppingCartLineCommand> list = autoKeyAccessor.get(key, request);
        List<ShoppingCartLineCommand> newlist = new ArrayList<>(list.size());

        for (ShoppingCartLineCommand shoppingCartLineCommand : list){
            newlist.add(BeanUtil.cloneBean(shoppingCartLineCommand));
        }
        return newlist;

    }
}
