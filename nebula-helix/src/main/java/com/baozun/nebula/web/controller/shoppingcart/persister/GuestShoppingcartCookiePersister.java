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
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baozun.nebula.sdk.command.shoppingcart.CookieShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;

/**
 * 基于 {@link Cookie}的游客购物车持久化处理方式.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月3日 下午4:20:24
 * @since 5.3.1
 */
public class GuestShoppingcartCookiePersister implements GuestShoppingcartPersister{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GuestShoppingcartCookiePersister.class);

    /** cookie寄存器. */
    private CookieAccessor      cookieAccessor;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.persister.GuestShoppingcartPersister#clear(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void clear(HttpServletRequest request,HttpServletResponse response){
        cookieAccessor.remove(response);
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

            return CollectionsUtil.collect(cookieShoppingCartLineList, new ToShoppingCartLineCommandTransformer());
        }catch (EncryptionException e){
            LOGGER.error("EncryptionException e :", e);
            throw new IllegalArgumentException(e);// XXX feilong 换成更好的 runtimeexception
        }
    }

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
        List<CookieShoppingCartLine> cartLineList = CollectionsUtil
                        .collect(needChangeCheckedCommandList, new ToCookieShoppingCartLineTransformer());

        String json = JSON.toJSONString(cartLineList);
        try{
            String encrypt = EncryptUtil.getInstance().encrypt(json);
            cookieAccessor.save(encrypt, response);
        }catch (EncryptionException e){
            LOGGER.error("EncryptionException e:", e);
        }
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
        String value = cookieAccessor.get(request);
        if (Validator.isNullOrEmpty(value)){
            return null;
        }
        String decrypt = EncryptUtil.getInstance().decrypt(value);
        return JSON.parseObject(decrypt, new TypeReference<ArrayList<CookieShoppingCartLine>>(){});
    }

    /**
     * 设置 cookie寄存器.
     *
     * @param cookieAccessor
     *            the cookieAccessor to set
     */
    public void setCookieAccessor(CookieAccessor cookieAccessor){
        this.cookieAccessor = cookieAccessor;
    }
}
