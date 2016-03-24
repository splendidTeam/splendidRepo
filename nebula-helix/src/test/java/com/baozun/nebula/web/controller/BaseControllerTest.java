package com.baozun.nebula.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.springframework.ui.Model;
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

    /** The binding result. */
    protected BindingResult       bindingResult;

    /**
     * Base init.
     */
    @Before
    public void baseInit(){

        control = EasyMock.createNiceControl();

        request = control.createMock("HttpServletRequest", HttpServletRequest.class);
        response = control.createMock("HttpServletResponse", HttpServletResponse.class);

        bindingResult = control.createMock("bindingResult", BindingResult.class);

        model = control.createMock("model", Model.class);
    }
}
