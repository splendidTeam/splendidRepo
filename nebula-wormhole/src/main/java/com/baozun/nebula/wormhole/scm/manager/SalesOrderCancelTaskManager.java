/*
 * Copyright (C) 2008 feilong
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
package com.baozun.nebula.wormhole.scm.manager;

/**
 * 执行定时任务：取消订单.
 *
 * @author  <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-10
 * @since 5.0.0
 */
public interface SalesOrderCancelTaskManager{

    /**
     * 取消订单.
     * 
     * <p>
     * 由于这里的方法名称和pts 里面的定时任务配置相匹配,因此请不要随意的更改
     * </p>
     */
    public void cancelOrders();
    
}
