/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.utils;

import org.junit.Test;

import com.baozun.nebula.utils.cache.GuavaAbstractLoadingCache;
import com.google.common.base.Optional;

/**
 * @ClassName: GuavaCacheTest
 * @author dianchao.song
 * @date 2017年6月23日 下午12:50:04
 * 
 */
public class GuavaCacheTest {
    @Test
    public void testGuavaCache() throws Exception {
        System.out.println(cache.getValue("key"));
        System.out.println(cache.getValue("key2"));
        Thread.sleep(55*1000l);
        System.out.println(cache.getValue("key"));
        System.out.println(cache.getValue("key2"));
        Thread.sleep(10*1000l);
        System.out.println(cache.getValue("key"));
        System.out.println(cache.getValue("key2"));
        
    }
    /**
     * null值处理
     */
    private GuavaAbstractLoadingCache<String, Optional<String>> cache = new GuavaAbstractLoadingCache<String, Optional<String>>() {
        @Override
        protected Optional<String> fetchData(String key) {
            System.out.println("from db");
            if ("key".equals(key))
                return Optional.fromNullable(key);
            else
                return Optional.fromNullable(null);
        }
    };
}
