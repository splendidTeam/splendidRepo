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

import com.baozun.nebula.manager.salesorder.ReturnOrderAppManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.PtsReturnOrderCommand;
import com.baozun.nebula.web.controller.BaseController;


@Controller
public class ReturnOrderAppController extends BaseController{

	
	@Autowired
	private ReturnOrderAppManager returnOrderAppManager;

	/**
	 * 跳转到申请退换货列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/returnApplicationList.htm")
	public String returnApplicationList(Model model){
		
		return "/salesorder/return-order-list";
	}
	
	@RequestMapping(value = "/order/returnApplicationList.json")
	@ResponseBody
	public Pagination<PtsReturnOrderCommand> returnApplicationListJson(Model model,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){
		
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("sro.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		
		Pagination<PtsReturnOrderCommand> result =returnOrderAppManager
				.findReturnApplicationListByQueryMapWithPage(queryBean.getPage()
				, sorts, queryBean.getParaMap());

		return result;
	}

}
