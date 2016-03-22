/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.springframework.ui.Model;

/**
 * The Class BaseControllerTest.
 *
 * @author feilong
 * @version 5.0.0 2016年3月21日 下午5:39:52
 * @since 5.0.0
 */
public abstract class BaseControllerTest{

    /** The control. */
    protected IMocksControl       control;

    /** The request. */
    protected HttpServletRequest  request;

    /** The response. */
    protected HttpServletResponse response;

    /** The model. */
    protected Model               model;

    /**
     * Base init.
     */
    @Before
    public void baseInit(){

        control = EasyMock.createNiceControl();

        request = control.createMock("HttpServletRequest", HttpServletRequest.class);
        response = control.createMock("HttpServletResponse", HttpServletResponse.class);

        model = control.createMock("model", Model.class);
    }
}
