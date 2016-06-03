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
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.zk.SystemConfigWatchInvoke;
import com.baozun.nebula.zk.ZooKeeperOperator;

/**
 * MataInfoController
 * 
 * @author 何波
 * 
 */
@Controller
public class MetaInfoController extends BaseController {

	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;

	@Autowired
	private ZooKeeperOperator zooKeeperOperator;

	@RequestMapping("/mataInfo/list.htm")
	public String list() {
		return "/system/metainfo/mataInfo";
	}

	/**
	 * 保存MataInfo
	 * 
	 */
	@RequestMapping("/mataInfo/save.json")
	@ResponseBody
	public BackWarnEntity saveMataInfo(MataInfo model) {
		try {
			sdkMataInfoManager.saveMataInfo(model);
		} catch (BusinessException e) {
			BackWarnEntity  back = new BackWarnEntity();
			back.setIsSuccess(false);
			back.setDescription(e.getMessage());
			return back;
		}
		return SUCCESS;
	}

	/**
	 * 通过id获取MataInfo
	 * 
	 */
	@RequestMapping("/mataInfo/findByid.json")
	@ResponseBody
	public MataInfo findMataInfoById(Long id) {

		return sdkMataInfoManager.findMataInfoById(id);
	}

	/**
	 * 获取所有MataInfo列表
	 * 
	 * @return
	 */
	@RequestMapping("/mataInfo/findAll.json")
	@ResponseBody
	public List<MataInfo> findAllMataInfoList() {

		return sdkMataInfoManager.findAllMataInfoList();
	};

	/**
	 * 通过ids获取MataInfo列表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/mataInfo/findByIds.json")
	@ResponseBody
	public List<MataInfo> findMataInfoListByIds(Long[] ids) {

		return sdkMataInfoManager.findMataInfoListByIds(Arrays.asList(ids));
	};

	/**
	 * @Description: 分页获取MataInfo列表
	 * @param queryBean
	 * @return Pagination<MataInfo>
	 * @throws
	 */
	@RequestMapping("/mataInfo/page.json")
	@ResponseBody
	public Pagination<MataInfo> findMataInfoListByQueryMapWithPage(
			@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0) {
			Sort sort = new Sort("id", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		return sdkMataInfoManager.findMataInfoListByQueryMapWithPage(
				queryBean.getPage(), sorts, queryBean.getParaMap());
	}

	/**
	 * 通过ids批量启用或禁用MataInfo 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/mataInfo/enableOrDisableByIds.json")
	@ResponseBody
	public BackWarnEntity enableOrDisableMataInfoByIds(Long[] ids, Integer state) {
		sdkMataInfoManager.enableOrDisableMataInfoByIds(Arrays.asList(ids),
				state);
		return SUCCESS;
	}

	/**
	 * 通过ids批量删除MataInfo 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/mataInfo/removeByIds.json")
	@ResponseBody
	public BackWarnEntity removeMataInfoByIds(Long[] ids) {
		sdkMataInfoManager.removeMataInfoByIds(Arrays.asList(ids));
		return SUCCESS;
	}

	@RequestMapping("/mataInfo/findById.json")
	@ResponseBody
	public MataInfo findById(Long id) {
		return sdkMataInfoManager.findMataInfoById(id);
	}
	/**
	 * 
	 * @author 何波
	 * @Description: 刷新内存数据 
	 * @throws
	 */
	@RequestMapping("/mataInfo/activeMetaInfo.json")
	@ResponseBody
	private BackWarnEntity activeMetaInfo() {
		zooKeeperOperator.noticeZkServer(SystemConfigWatchInvoke.LISTEN_PATH);
		return SUCCESS;
	}
}
