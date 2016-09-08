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
package com.baozun.nebula.web.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.log.SchedulerLogCommand;
import com.baozun.nebula.sdk.manager.SchedulerLogManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;

import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 定时任务运行日志
 * @author jin.wang
 *
 */
@Controller
@RequestMapping(value="/backlog")
public class SchedulerLogController {
	
	@Autowired
	private SchedulerLogManager schedulerLogManager;
	
	@RequestMapping("/schedulerLog/list.htm")
	public String list(){
		return "log/schedulerLog-list";
	}
	
	/**
	 * 分页获取SchedulerLog数据
	 * @param queryBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/schedulerLog/list.json")
	public Pagination<SchedulerLogCommand> findSchedulerLogListByQueryBeanWithPage(@QueryBeanParam QueryBean queryBean){
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0)
			sorts = new Sort[] {new Sort("log.end_time", "desc")};
		return schedulerLogManager.findSchedulerLogListByQueryBeanWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
	}
}
