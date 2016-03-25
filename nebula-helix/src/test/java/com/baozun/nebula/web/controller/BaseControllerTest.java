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
package com.baozun.nebula.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

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

    /**
     * Mock binding result.
     *
     * @param baseForm
     *            the base form
     * @return the binding result
     * @since 5.0.0
     */
    protected BindingResult mockBindingResult(BaseForm baseForm){
        String objectName = baseForm.getClass().getSimpleName();
        return new BindException(baseForm, objectName);
    }

}
