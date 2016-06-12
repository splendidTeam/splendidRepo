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
 *
 */
package com.baozun.nebula.web.controller.log;


import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.product.SkuInventoryChangeLogCommand;
import com.baozun.nebula.manager.product.SkuInventoryChangeLogManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;
/**
 * SkuInventoryChangeLogController
 * @author  dongliang.ma
 *
 */
 @Controller
 @RequestMapping(value="/backlog")
public class SkuInventoryChangeLogController extends BaseController{


	@Autowired
	private SkuInventoryChangeLogManager skuInventoryChangeLogManager;
	
	@RequestMapping("/skuInventoryChangeLog/list.htm")
	public String list(){
		return "log/skuInventoryChangeLog-list";
	}
	
	/**
	* @Description: 分页获取SkuInventoryChangeLog列表
	* @param queryBean
	* @return   
	* Pagination<SkuInventoryChangeLog>
	* @throws
	 */
	@RequestMapping("/skuInventoryChangeLog/page.json")
	@ResponseBody
	public Pagination<SkuInventoryChangeLogCommand> findSkuInventoryChangeLogListByQueryMapWithPage(@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0){
			Sort sort = new Sort("log.create_time", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		return  skuInventoryChangeLogManager.findSkuInventoryChangeLogListByQueryMapWithPage(queryBean.getPage(),
				sorts, queryBean.getParaMap());
	}
}
