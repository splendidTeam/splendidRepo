package com.baozun.nebula.web.controller.salesorder;

import static org.easymock.EasyMock.createNiceControl;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import com.baozun.nebula.manager.salesorder.ReturnOrderAppManager;
import com.baozun.nebula.manager.salesorder.SalesOrderManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.command.PtsReturnOrderCommand;
//import com.baozun.nebula.web.controller.salesorder.ReturnOrderAppController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ReturnOrderAppControllerTest{

    private IMocksControl control;

    private Model model;

    private HttpServletRequest request;

    private HttpServletResponse response;

    //private ReturnOrderAppController returnOrderAppController;

    private ReturnOrderAppManager returnOrderAppManager;

    @Before
    public void init(){
        //returnOrderAppController = new ReturnOrderAppController();
        control = createNiceControl();
        request = control.createMock("HttpServletRequest", HttpServletRequest.class);
        response = control.createMock("HttpServletResponse", HttpServletResponse.class);

        returnOrderAppManager = control.createMock("ReturnOrderAppManager", ReturnOrderAppManager.class);

        //ReflectionTestUtils.setField(returnOrderAppController, "returnOrderAppManager", returnOrderAppManager);

        model = control.createMock("Model", Model.class);

    }

    @Test
    public void testReturnApplicationList(){
        Page page = new Page(1, 10);
        Sort[] sorts = Sort.parse("sro.CREATE_TIME asc");
        Map<String, Object> searchParam = new HashMap<String, Object>();
        searchParam.put("sdkQueryType", "1");

        Pagination<PtsReturnOrderCommand> returnOrderList = new Pagination<PtsReturnOrderCommand>();

        EasyMock.expect(returnOrderAppManager.findReturnApplicationListByQueryMapWithPage(page, sorts, searchParam)).andReturn(returnOrderList);

        control.replay();

        QueryBean queryBean = new QueryBean();

        queryBean.setPage(page);
        queryBean.setSorts(sorts);
        queryBean.setParaMap(searchParam);

        //assertEquals(returnOrderList, returnOrderAppController.returnApplicationListJson(model, queryBean, request, response));
    }

    @Test
    public void testReturnApplicationListJson(){

    }

}
