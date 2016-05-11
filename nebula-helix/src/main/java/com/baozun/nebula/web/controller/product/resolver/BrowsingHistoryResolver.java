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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.web.controller.product.viewcommand.BrowsingHistoryViewCommand;

/**
 * The Interface BrowsingHistory.
 * 去掉拦截器  直接通过controller调用
 * @author xingyu.liu 
 * @version 1.2.3 
 * @since  1.2.2  by feilong
 */


public interface BrowsingHistoryResolver{

    /**
     * 处理历史记录.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param browsingHistoryCommand
     *            the browsing history command
     */
    void resolveBrowsingHistory(HttpServletRequest request,HttpServletResponse response,BrowsingHistoryViewCommand browsingHistoryCommand);

    /**
     * 获得 browsing history.
     *
     * @param <T>
     *            the generic type
     * @param request
     *            the request
     * @param klass
     *            the klass
     * @return the browsing history
     */
    //TODO feilong 设置成  BrowsingHistoryCommand
    <T extends Serializable> LinkedList<T> getBrowsingHistory(HttpServletRequest request,Class<T> klass);

}
