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

package com.baozun.nebula.utils.cookie;

import javax.servlet.http.Cookie;

public class TimeIntervalCookie extends Cookie {
	public static final int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;
	public static final int SECONDS_PER_MONTH = 60 * 60 * 24 * 30;
	public static final int SECONDS_PER_WEEK = 60 * 60 * 24 * 7;
	public static final int SECONDS_PER_DAY = 60 * 60 * 24;
	public static final int SECONDS_PER_HOUR = 60 * 60;

    public TimeIntervalCookie(String name, String value) {
    	super(name, value);
        setMaxAge(SECONDS_PER_YEAR);
        setPath("/");
    }
    
    public TimeIntervalCookie(String name, String value, int interval) {
        super(name, value);
        setMaxAge(interval);
        setPath("/");
    }
    
    public TimeIntervalCookie(String name, String value, int interval, String path) {
        super(name, value);
        setMaxAge(interval);
        setPath(path);
    }
}