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
package com.baozun.nebula.dao;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;

import com.baozun.nebula.BaseJUnit4SpringContextTests;

/**
 * The Class DaoTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0.9 2015年3月30日 上午11:24:09
 * @since 1.0.9
 */

public class DaoTest extends BaseJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger     LOGGER = LoggerFactory.getLogger(DaoTest.class);

    /** The session factory. */
    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    /**
     * Creates the tables.
     */
    @Test
    public void updateDatabaseSchema(){
        sessionFactory.updateDatabaseSchema();
    }

    /**
     * TestDaoTest.
     */
    @Test
    public void testDaoTest(){
        LOGGER.debug("hello, world");
    }
}
