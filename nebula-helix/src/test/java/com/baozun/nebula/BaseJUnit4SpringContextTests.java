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
package com.baozun.nebula;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * 公共的junit spring 测试类.
 *
 * @author feilong
 * @version 1.1.1 2015年4月17日 下午2:24:26
 * @since 1.1.1
 */
@ContextConfiguration(locations = {
                                    "classpath*:loxia-hibernate-context.xml",
                                    "classpath*:loxia-service-context.xml",
                                    "classpath*:spring.xml" })
public class BaseJUnit4SpringContextTests extends AbstractJUnit4SpringContextTests{

    static{
        String value = "dev";
        System.setProperty("spring.profiles.active", value);
        ProfileConfigUtil.setMode(value);
    }
}
