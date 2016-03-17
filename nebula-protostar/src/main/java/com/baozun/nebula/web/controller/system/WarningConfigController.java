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
package com.baozun.nebula.web.controller.system;

import java.util.Arrays;
import java.util.List;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.system.WarningConfig;
import com.baozun.nebula.sdk.manager.SdkWarningConfigManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * WarningConfigController
 * 
 * @author 何波
 * 
 */
@Controller
public class WarningConfigController extends BaseController {

	@Autowired
	private SdkWarningConfigManager warningConfigManager;

	@RequestMapping("/warningConfig/list.htm")
	public String list() {
		return "/system/warning/warningConfig";
	}

	/**
	 * 保存WarningConfig
	 * 
	 */
	@RequestMapping("/warningConfig/save.json")
	@ResponseBody
	public BackWarnEntity saveWarningConfig(WarningConfig model) {
		try {
			warningConfigManager.saveWarningConfig(model);
		} catch (BusinessException e) {
			BackWarnEntity back = new BackWarnEntity();
			back.setIsSuccess(false);
			back.setDescription(e.getMessage());
			return back;
		}
		return SUCCESS;
	}

	/**
	 * 通过id获取WarningConfig
	 * 
	 */
	@RequestMapping("/warningConfig/findByid.json")
	@ResponseBody
	public WarningConfig findWarningConfigById(Long id) {

		return warningConfigManager.findWarningConfigById(id);
	}

	/**
	 * 获取所有WarningConfig列表
	 * 
	 * @return
	 */
	@RequestMapping("/warningConfig/findAll.json")
	@ResponseBody
	public List<WarningConfig> findAllWarningConfigList() {

		return warningConfigManager.findAllWarningConfigList();
	};

	/**
	 * 通过ids获取WarningConfig列表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/warningConfig/findByIds.json")
	@ResponseBody
	public List<WarningConfig> findWarningConfigListByIds(Long[] ids) {

		return warningConfigManager.findWarningConfigListByIds(Arrays
				.asList(ids));
	};

	@RequestMapping("/warningConfig/findById.json")
	@ResponseBody
	public WarningConfig findWarningConfigListById(Long id) {
		WarningConfig wcs= warningConfigManager.findWarningConfigById(id);
		if(wcs==null ){
			return null;	
		}
		return wcs;
	};

	/**
	 * @Description: 分页获取WarningConfig列表
	 * @param queryBean
	 * @return Pagination<WarningConfig>
	 * @throws
	 */
	@RequestMapping("/warningConfig/page.json")
	@ResponseBody
	public Pagination<WarningConfig> findWarningConfigListByQueryMapWithPage(
			@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0) {
			Sort sort = new Sort("id", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		return warningConfigManager.findWarningConfigListByQueryMapWithPage(
				queryBean.getPage(), sorts, queryBean.getParaMap());
	}

	/**
	 * 通过ids批量启用或禁用WarningConfig 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/warningConfig/enableOrDisableByIds.json")
	@ResponseBody
	public BackWarnEntity enableOrDisableWarningConfigByIds(Long[] ids,
			Integer state) {
		warningConfigManager.enableOrDisableWarningConfigByIds(
				Arrays.asList(ids), state);
		return SUCCESS;
	}

	/**
	 * 通过ids批量删除WarningConfig 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/warningConfig/removeByIds.json")
	@ResponseBody
	public BackWarnEntity removeWarningConfigByIds(Long[] ids) {
		warningConfigManager.removeWarningConfigByIds(Arrays.asList(ids));
		return SUCCESS;
	}
}
