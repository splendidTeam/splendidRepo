/*
 * Copyright (C) 2008 feilong (venusdrogon@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baozun.nebula.web.controller.product.resolver;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utils.date.TimeInterval;
import com.baozun.nebula.web.controller.product.viewcommand.BrowsingHistoryViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.bean.ToStringConfig;
import com.feilong.core.lang.StringUtil;
import com.feilong.servlet.http.CookieUtil;
import com.feilong.servlet.http.entity.CookieEntity;

/**
 * The Class DefaultBrowsingHistory.
 * 
 * <h3>可配置参数:</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * <th align="left">必须</th>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>cookieName</td>
 * <td>cookie名称</td>
 * <td>false,默认 f_b_h</td>
 * </tr>
 * <tr valign="top">
 * <td>cookieMaxAge</td>
 * <td>cookie最大存活时间,单位秒</td>
 * <td>false,默认 3个月</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>cookieCharsetName</td>
 * <td>cookie编码</td>
 * <td>false,默认 UTF-8</td>
 * </tr>
 * <tr valign="top">
 * <td>maxCount</td>
 * <td>最大记录数量,超过的记录将被去掉</td>
 * <td>false,默认 5</td>
 * </tr>
 * </table>
 * </blockquote>
 *
 * @author feilong
 * @version 1.2.2 2015年7月20日 下午6:44:27
 * @since 1.2.2
 */
public class BrowsingHistoryCookieResolver implements BrowsingHistoryResolver{

    /** The Constant log. */
    private static final Logger LOGGER             = LoggerFactory.getLogger(BrowsingHistoryCookieResolver.class);

    /** The Constant DEFAULT_CONNECTOR. */
    private static final String DEFAULT_CONNECTOR  = "_";

    /** 默认的cookie名称. */
    private static final String DEFAULT_COOKIENAME = "f_b_h";

    /** The cookie name. */
    private String              cookieName         = DEFAULT_COOKIENAME;

    /** 单位秒 默认 3个月. */
    private int                 cookieMaxAge       = TimeInterval.SECONDS_PER_MONTH * 3;
    
    /** 最大记录数量,超过的记录将被去掉. */
    private Integer             maxCount           = 10;

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.browsingHistory.BrowsingHistoryResolver#resolveBrowsingHistory(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * com.feilong.spring.web.servlet.interceptor.browsingHistory.command.BrowsingHistoryCommand)
     */
    @Override
    public void resolveBrowsingHistory(HttpServletRequest request,HttpServletResponse response,BrowsingHistoryViewCommand browsingHistoryViewCommand){
        if (Validator.isNotNullOrEmpty(browsingHistoryViewCommand)){
            try{
                String cookieValue = constructItemBrowsingHistoryCookieValue(request, response, browsingHistoryViewCommand);

                if (Validator.isNotNullOrEmpty(cookieValue)){
                    CookieEntity cookieEntity = new CookieEntity(cookieName, cookieValue, cookieMaxAge);
                    cookieEntity.setHttpOnly(true);
                    CookieUtil.addCookie(cookieEntity, response);
                }
            }catch (Exception e){
                LOGGER.error("constructItemBrowsingHistory Exception,but we need code go-on!", e);
            }
        }else{
            LOGGER.warn("browsingHistoryCommand is null or empty");
        }

    }

    /**
     * Construct item browsing history cookie value.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param browsingHistoryCommand
     *            the browsing history command
     * @return if nothing to do ,then return null, such as in the cookie, first item is current item
     */
    private String constructItemBrowsingHistoryCookieValue(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    BrowsingHistoryViewCommand browsingHistoryViewCommand){

        if (Validator.isNullOrEmpty(browsingHistoryViewCommand)){
            return null;
        }

        Serializable id = browsingHistoryViewCommand.getId();

        LinkedList<String> linkedList = null;
        try{
            linkedList = getBrowsingHistory(request, String.class);
        }catch (Exception e){
            //如果出错了,那么就将cookie删掉
            CookieUtil.deleteCookie(cookieName, response);
        }

        //如果cookie没有,表示第一次访问PDP页面 ,这时逻辑是构建一个往cookie 里加入
        String idStr = id.toString();
        if (Validator.isNullOrEmpty(linkedList)){
            linkedList = new LinkedList<String>();
            //如果没有 添加一个
            linkedList.add(idStr);
        }else{
            @SuppressWarnings("null")
            String first = linkedList.getFirst();
            //如果 list 里面的数据 第一个是当前item  那么一般表示刷新页面 或者重新打开新窗口
            //这种case 没有必要操作 cookie
            if (first.equals(idStr)){
                LOGGER.info("in cookie,first pk is:[{}],current pk:[{}], nothing to do", first, id);
                return null;
            }

            //如果有当前商品,那么删除掉 并将 当前的item id 塞第一个
            if (linkedList.contains(idStr)){
                LOGGER.info("in cookie,linkedList:[{}],contains:[{}],remove it~", linkedList, idStr);
                linkedList.remove(idStr);
            }
            linkedList.addFirst(idStr);

            //如果超长了 ,截取
            int size = linkedList.size();
            if (size > maxCount){
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("linkedList size:[{}] > maxCount[{}],linkedList:[{}],will sub subList", size, maxCount, linkedList);
                }

                // so non-structural changes in the returned list
                List<String> subList = linkedList.subList(0, maxCount);
                //linkedList = (LinkedList<Serializable>) subList;  //java.util.SubList cannot be cast to java.util.LinkedList
                linkedList = new LinkedList<String>(subList);
            }
        }

        //****************************************************************************
        //cookie value 是  itemid join--->aes hex 加密格式字符串
        ToStringConfig toStringConfig = new ToStringConfig(DEFAULT_CONNECTOR);
        String original = ConvertUtil.toString(toStringConfig, linkedList);

        //如果cookie没有,表示第一次访问PDP页面 ,这时逻辑是构建一个往cookie 里加入
        String encryptHex = null;
		try {
			encryptHex = EncryptUtil.getInstance().encrypt(original);
		} catch (Exception e) {
			LOGGER.error("", e);
			throw new IllegalArgumentException(e);
		}

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("will add to cookie,original:[{}],encryptHex:[{}]", original, encryptHex);
        }
        return encryptHex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.browsingHistory.BrowsingHistoryResolver#getBrowsingHistory(javax.servlet.http.
     * HttpServletRequest)
     */
    @Override
    public <T extends Serializable> LinkedList<T> getBrowsingHistory(HttpServletRequest request,Class<T> klass){
        LinkedList<T> linkedList = new LinkedList<T>();

        Cookie cookie = CookieUtil.getCookie(request, cookieName);

        if (Validator.isNotNullOrEmpty(cookie)){
            String value = cookie.getValue();

            if (Validator.isNotNullOrEmpty(value)){
                try{
                    String decryptHex = EncryptUtil.getInstance().encrypt(value);
                    String[] tokenizeToStringArray = StringUtil.tokenizeToStringArray(decryptHex, DEFAULT_CONNECTOR);
                    for (String string : tokenizeToStringArray){
                        linkedList.add(ConvertUtil.convert(string, klass));
                    }
                }catch (NumberFormatException e){
                    LOGGER.error("", e);
                    throw new IllegalArgumentException(e);
                }catch (Exception e) {
                    LOGGER.error("", e);
					throw new IllegalArgumentException(e);
				}
            }
        }
        return linkedList;
    }

    /**
     * 设置 the cookie name.
     *
     * @param cookieName
     *            the cookieName to set
     */
    public void setCookieName(String cookieName){
        this.cookieName = cookieName;
    }

    /**
     * 设置 单位秒 默认 3个月.
     *
     * @param cookieMaxAge
     *            the cookieMaxAge to set
     */
    public void setCookieMaxAge(int cookieMaxAge){
        this.cookieMaxAge = cookieMaxAge;
    }
    
    /**
     * 设置 最大记录数量,超过的记录将被去掉.
     *
     * @param maxCount
     *            the maxCount to set
     */
    public void setMaxCount(Integer maxCount){
        this.maxCount = maxCount;
    }

}
