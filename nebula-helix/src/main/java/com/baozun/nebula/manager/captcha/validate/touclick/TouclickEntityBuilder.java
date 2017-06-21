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
package com.baozun.nebula.manager.captcha.validate.touclick;

/**
 * 数据转换.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 * @deprecated 请使用 feilong-captch ,进行了框架的升级
 */
@Deprecated
public interface TouclickEntityBuilder{

    /**
     * Builds the.
     *
     * @param touclickForm
     *            the touclick form
     * @param customerData
     *            the customer data
     * @return the touclick entity
     */
    TouclickEntity build(TouclickForm touclickForm,Object customerData);
}
