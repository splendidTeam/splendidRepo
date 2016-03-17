package com.baozun.nebula.web.controller.salesorder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.manager.salesorder.CancelOrderAppManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.CancelApplicationCommand;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 
 * @author qiang.yang
 * @createtime 2013-11-27
 *
 */

@Controller
public class CancelOrderAppController extends BaseController {
	@Autowired
	private CancelOrderAppManager cancelOrderAppManager;
	
	
	
	/**
	 * 跳转到订单列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/CancelApplicationList.htm")
	public String orderList(Model model){
		
		return "/salesorder/cancel-order-list";
	}
	
	@RequestMapping(value = "/order/cancelApplicationOrderList.json")
	@ResponseBody
	public Pagination<CancelApplicationCommand> findOrderListJson(Model model,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){
		
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("sco.CREATE_TIME","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		
		Pagination<CancelApplicationCommand> result =cancelOrderAppManager.findCancelApplicationListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());

		return result;
	}

}
