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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baozun.nebula.sdk.command.shoppingcart.CookieShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.web.constants.CookieKeyConstants;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;

/**
 * 基于cookie的游客购物车持久化处理方式.
 *
 * @author feilong
 * @version 5.3.1 2016年5月3日 下午4:20:24
 * @since 5.3.1
 */
@Component("guestShoppingcartPersister")
public class GuestShoppingcartCookiePersister implements GuestShoppingcartPersister{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GuestShoppingcartCookiePersister.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.GuestShoppingcartPersister#load(javax.servlet.http.HttpServletRequest)
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
            throw new IllegalArgumentException(e);// TODO 换成更好的 runtimeexception
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
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieName(CookieKeyConstants.GUEST_COOKIE_GC);
        cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE);
        try{
            String encrypt = EncryptUtil.getInstance().encrypt(json);
            cookieGenerator.addCookie(response, encrypt);
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
    private List<CookieShoppingCartLine> toCookieShoppingCartLineList(List<ShoppingCartLineCommand> shoppingCartLines){
        if (Validator.isNullOrEmpty(shoppingCartLines)){
            return Collections.emptyList();
        }
        // 将ShoppingCartLineCommand对象转换为CookieShoppingCartLine对象
        List<CookieShoppingCartLine> cookieLines = new ArrayList<CookieShoppingCartLine>();
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLines){
            CookieShoppingCartLine cookieShoppingCartLine = new CookieShoppingCartLine();

            PropertyUtil.copyProperties(
                            cookieShoppingCartLine,
                            shoppingCartLineCommand,
                            "extentionCode",
                            "skuId",
                            "quantity",
                            "createTime",
                            "settlementState",
                            "shopId",
                            "promotionId",
                            "lineGroup");
            cookieShoppingCartLine.setIsGift(shoppingCartLineCommand.isGift());
            // TODO bundle 以后再考虑 id
            cookieShoppingCartLine
                            .setId(null == shoppingCartLineCommand.getId() ? shoppingCartLines.size() : shoppingCartLineCommand.getId());

            cookieLines.add(cookieShoppingCartLine);
        }
        return cookieLines;
    }

    /**
     * 获取cookie中的购物车行信息.将cookie中的购物车 转换为 shoppingCartLineCommand
     *
     * @param cookieShoppingCartLineList
     *            the cookie shopping cart line list
     * @return the list< shopping cart line command>
     */
    private List<ShoppingCartLineCommand> toShoppingCartLineCommandList(List<CookieShoppingCartLine> cookieShoppingCartLineList){
        List<ShoppingCartLineCommand> shoppingCartLines = new ArrayList<ShoppingCartLineCommand>();
        for (CookieShoppingCartLine cookieShoppingCartLine : cookieShoppingCartLineList){

            // 将cookie中的购物车 转换为 shoppingCartLineCommand
            ShoppingCartLineCommand shoppingLineCommand = new ShoppingCartLineCommand();
            PropertyUtil.copyProperties(
                            shoppingLineCommand,
                            cookieShoppingCartLine,
                            "createTime",
                            "skuId",
                            "quantity",
                            "extentionCode",
                            "settlementState",
                            "shopId",
                            "promotionId",
                            "lineGroup",
                            "id");
            shoppingLineCommand.setGift(null == cookieShoppingCartLine.getIsGift() ? false : cookieShoppingCartLine.getIsGift());
            shoppingCartLines.add(shoppingLineCommand);
        }
        return shoppingCartLines;
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
        Cookie cookie = WebUtils.getCookie(request, CookieKeyConstants.GUEST_COOKIE_GC);

        if (null == cookie){
            return null;
        }

        if (StringUtils.isBlank(cookie.getValue())){
            return null;
        }

        String decrypt = EncryptUtil.getInstance().decrypt(cookie.getValue());
        return JSON.parseObject(decrypt, new TypeReference<ArrayList<CookieShoppingCartLine>>(){});
    }
}
