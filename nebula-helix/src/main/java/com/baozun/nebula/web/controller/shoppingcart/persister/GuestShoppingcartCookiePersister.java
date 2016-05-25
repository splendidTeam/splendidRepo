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
package com.baozun.nebula.web.controller.shoppingcart.persister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baozun.nebula.sdk.command.shoppingcart.CookieShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.web.constants.CookieKeyConstants;
import com.feilong.core.TimeInterval;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.servlet.http.CookieUtil;

/**
 * 基于 {@link Cookie}的游客购物车持久化处理方式.
 *
 * @author feilong
 * @version 5.3.1 2016年5月3日 下午4:20:24
 * @since 5.3.1
 */
@Component("guestShoppingcartPersister")
public class GuestShoppingcartCookiePersister implements GuestShoppingcartPersister{

    /** The Constant LOGGER. */
    private static final Logger   LOGGER                      = LoggerFactory.getLogger(GuestShoppingcartCookiePersister.class);

    /** cookie的名称. */
    private String                cookieNameGuestShoppingcart = CookieKeyConstants.GUEST_COOKIE_GC;

    private static final String[] COPY_PROPERTY_NAMES         = {
                                                                  "skuId",
                                                                  "extentionCode",
                                                                  "quantity",
                                                                  "createTime",
                                                                  "settlementState",
                                                                  "lineGroup" };

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.persister.GuestShoppingcartPersister#clear(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void clear(HttpServletRequest request,HttpServletResponse response){
        CookieUtil.deleteCookie(cookieNameGuestShoppingcart, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.persister.GuestShoppingcartPersister#load(javax.servlet.http.
     * HttpServletRequest)
     */
    @Override
    public List<ShoppingCartLineCommand> load(HttpServletRequest request){
        try{
            // 获取cookie中的购物车行集合
            List<CookieShoppingCartLine> cookieShoppingCartLineList = getCookieShoppingCartLines(request);

            if (Validator.isNullOrEmpty(cookieShoppingCartLineList)){
                return null;
            }

            return toShoppingCartLineCommandList(cookieShoppingCartLineList);

        }catch (EncryptionException e){
            LOGGER.error("EncryptionException e :", e);
            throw new IllegalArgumentException(e);// TODO feilong 换成更好的 runtimeexception
        }
    }

    //***************************************************************************************************

    /**
     * 把cookie购物车行对象加入cookie当中.
     *
     * @param needChangeCheckedCommandList
     *            the need change checked command list
     * @param request
     *            the request
     * @param response
     *            the response
     */
    @Override
    public void save(List<ShoppingCartLineCommand> needChangeCheckedCommandList,HttpServletRequest request,HttpServletResponse response){
        List<CookieShoppingCartLine> cartLineList = toCookieShoppingCartLineList(needChangeCheckedCommandList);

        String json = JSON.toJSONString(cartLineList);
        try{
            String encrypt = EncryptUtil.getInstance().encrypt(json);
            CookieUtil.addCookie(cookieNameGuestShoppingcart, encrypt, TimeInterval.SECONDS_PER_YEAR, response);
        }catch (EncryptionException e){
            LOGGER.error("EncryptionException e:", e);
        }

    }

    //*******************************************************************************************
    /**
     * 将ShoppingCartLineCommand对象转换为CookieShoppingCartLine对象.
     *
     * @param shoppingCartLines
     *            the shopping cart lines
     * @return the list< cookie shopping cart line>
     */
    private List<CookieShoppingCartLine> toCookieShoppingCartLineList(final List<ShoppingCartLineCommand> shoppingCartLines){
        // 将ShoppingCartLineCommand对象转换为CookieShoppingCartLine对象
        Transformer<ShoppingCartLineCommand, CookieShoppingCartLine> transformer = new Transformer<ShoppingCartLineCommand, CookieShoppingCartLine>(){

            @Override
            public CookieShoppingCartLine transform(ShoppingCartLineCommand shoppingCartLineCommand){
                CookieShoppingCartLine cookieShoppingCartLine = new CookieShoppingCartLine();
                PropertyUtil.copyProperties(cookieShoppingCartLine, shoppingCartLineCommand, COPY_PROPERTY_NAMES);
                cookieShoppingCartLine.setIsGift(shoppingCartLineCommand.isGift());
                // TODO feilong bundle 以后再考虑 id
                cookieShoppingCartLine.setId(
                                null == shoppingCartLineCommand.getId() ? shoppingCartLines.size() : shoppingCartLineCommand.getId());
                return cookieShoppingCartLine;
            }
        };
        return CollectionsUtil.collect(shoppingCartLines, transformer);
    }

    /**
     * 获取cookie中的购物车行信息.将cookie中的购物车 转换为 shoppingCartLineCommand
     *
     * @param cookieShoppingCartLineList
     *            the cookie shopping cart line list
     * @return the list< shopping cart line command>
     */
    private List<ShoppingCartLineCommand> toShoppingCartLineCommandList(List<CookieShoppingCartLine> cookieShoppingCartLineList){
        Transformer<CookieShoppingCartLine, ShoppingCartLineCommand> transformer = new Transformer<CookieShoppingCartLine, ShoppingCartLineCommand>(){

            @Override
            public ShoppingCartLineCommand transform(CookieShoppingCartLine cookieShoppingCartLine){
                // 将cookie中的购物车 转换为 shoppingCartLineCommand
                ShoppingCartLineCommand shoppingLineCommand = new ShoppingCartLineCommand();
                PropertyUtil.copyProperties(shoppingLineCommand, cookieShoppingCartLine, COPY_PROPERTY_NAMES);

                shoppingLineCommand.setId(cookieShoppingCartLine.getId());
                shoppingLineCommand.setGift(null == cookieShoppingCartLine.getIsGift() ? false : cookieShoppingCartLine.getIsGift());

                return shoppingLineCommand;
            }
        };
        return CollectionsUtil.collect(cookieShoppingCartLineList, transformer);
    }

    /**
     * 获取cookie中的购物车行集合.
     *
     * @param request
     *            the request
     * @return the cookie shopping cart lines
     * @throws EncryptionException
     *             the encryption exception
     */
    private List<CookieShoppingCartLine> getCookieShoppingCartLines(HttpServletRequest request) throws EncryptionException{
        Cookie cookie = CookieUtil.getCookie(request, cookieNameGuestShoppingcart);

        if (null == cookie){
            return null;
        }

        if (Validator.isNullOrEmpty(cookie.getValue())){
            return null;
        }

        String decrypt = EncryptUtil.getInstance().decrypt(cookie.getValue());
        return JSON.parseObject(decrypt, new TypeReference<ArrayList<CookieShoppingCartLine>>(){});
    }

    /**
     * 设置 cookie的名称.
     *
     * @param cookieNameGuestShoppingcart
     *            the cookieNameGuestShoppingcart to set
     */
    public void setCookieNameGuestShoppingcart(String cookieNameGuestShoppingcart){
        this.cookieNameGuestShoppingcart = cookieNameGuestShoppingcart;
    }
}
