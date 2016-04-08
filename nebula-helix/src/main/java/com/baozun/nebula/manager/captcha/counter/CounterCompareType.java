/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.manager.captcha.counter;

/**
 * 比较结果.
 *
 * @author feilong
 * @version 1.5.3 2016年4月5日 下午10:00:16
 * @since 1.5.3
 */
public enum CounterCompareType{

    /** 都不超过阀值. */
    LESS,

    /** session出错数大于阀值. */
    SESSION_MORE,

    /** 服务器端出错数大于阀值. */
    SERVER_MORE,

    /** session和服务器端出错数均大于阀值. */
    SESSION_AND_SERVER_MORE
}
