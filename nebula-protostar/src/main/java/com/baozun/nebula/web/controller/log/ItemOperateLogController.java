package com.baozun.nebula.web.controller.log;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.log.ItemOperateLogCommand;
import com.baozun.nebula.manager.log.ItemOperateLogManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;
 
/**
 * 商品操作日志
 * @author dong.cheng
 *
 */
@Controller
public class ItemOperateLogController extends BaseController{

	private static final Logger	            LOG	                  = LoggerFactory.getLogger(ItemOperateLogController.class);
	
	@Autowired
	private ItemOperateLogManager           itemOperateLogManager;
	
	
	
	/**
	 * 商品上下架操作日志列表+搜索
	 * @param model
	 * @param itemOperateLogForm
	 * @return
	 */
	@RequestMapping(value = "/backlog/itemOperateLogList.htm")
	public String ItemPushAndSoldLogQuery(){
		return "/log/itemOperateLog-list";
	}
	
	@RequestMapping(value = "/backlog/itemOperateLogList.json")
	@ResponseBody
	public Pagination<ItemOperateLogCommand> ItemPushAndSoldLogQuery(@QueryBeanParam QueryBean queryBean,Model model,HttpServletRequest request,HttpServletResponse response){
		//分页
		Sort[] sorts=queryBean.getSorts();
		//conditions
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("tpi.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		
		queryBean.getParaMap().put("activeTime", convertedToSeconds((Long) queryBean.getParaMap().get("activeTime")));
		Pagination<ItemOperateLogCommand> pagination = itemOperateLogManager.findItemByconditions(queryBean.getPage(), sorts, queryBean.getParaMap());
		return pagination;
	}
	
	
	/**
	 * 将天数换算成秒数
	 * @param day
	 * @return
	 */
	private Long convertedToSeconds(Long day){
		if(Validator.isNotNullOrEmpty(day)){
			return day*24*60*60;
		}else{
			return day;
		}
	}
}
