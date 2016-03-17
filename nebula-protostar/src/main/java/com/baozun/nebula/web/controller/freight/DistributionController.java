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
package com.baozun.nebula.web.controller.freight;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.support.excel.ExcelManipulatorFactory;
import loxia.support.excel.ExcelWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baozun.nebula.api.utils.AddressUtils;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.freight.command.ExpSupportedAreaCommand;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.manager.freight.DistributionManager;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.SdkFreightManager;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 物流方式
 * 
 * @author jumbo
 * 
 */
@Controller
public class DistributionController extends BaseController {

	@Autowired
	private SdkFreightManager sdkFreightManager;

	@Autowired
	private DistributionManager distributionManager;

	@Autowired
	private LogisticsManager logisticsManager;
	
	@Autowired
	private ExcelManipulatorFactory excelFactory;

	private static final String		DEFAULT_PATH							= "excel/";
	private static final String		FILE_NAME								= "supported_area_import.xlsx";
	
	/** 进入物流方式 */
	@RequestMapping(value = "/freight/distributionMode.htm")
	public String entryDistribution(Model model) {
		return "/freight/distribution/distribution-mode";
	}

	/** 物流方式列表 */
	@RequestMapping(value = "/freight/distributionModeList.json")
	@ResponseBody
	public Pagination<DistributionMode> distributionList(Model model) {
		List<DistributionMode> distributionlist = sdkFreightManager
				.getAllDistributionMode();
		Pagination<DistributionMode> page = new Pagination<DistributionMode>(
				distributionlist, distributionlist.size(), 1, 1, 0,
				Integer.MAX_VALUE);
		return page;
	}

	/** 进入新建物流方式 */
	@RequestMapping(value = "/freight/createDistributionMode.htm")
	public String createDistribution(@RequestParam("id") Long id, Model model) {
		if (Validator.isNotNullOrEmpty(id)) {// 编辑
			DistributionMode distribution = sdkFreightManager
					.findDistributionModeById(id);
			model.addAttribute("distributionMode", distribution);
		}
		return "/freight/distribution/distribution-create";
	}

	/**
	 * 保存物流方式
	 * 
	 * @return
	 */
	@RequestMapping("/freight/saveDistributionMode.json")
	@ResponseBody
	public Object saveTemplate(DistributionModeCommand cmd,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		distributionManager.saveOrUpdateDistributionMode(cmd);
		return SUCCESS;
	}

	/**
	 * 通过ids批量删除
	 * 
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping("/freight/butchRemove.json")
	@ResponseBody
	public Object deleteItem(@RequestParam("ids") List<Long> ids, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		distributionManager.removeDistributionModeByIds(ids);
		return SUCCESS;
	}

	/**
	 * 删除物流方式
	 * 
	 * @return
	 */
	@RequestMapping("/freight/deleteDistributionMode.htm")
	@ResponseBody
	public Object deleteTemplate(@RequestParam("id") Long id,
			HttpServletRequest request, HttpServletResponse response) {
		logisticsManager.deleteDistributionMode(id);
		return SUCCESS;
	}

	// 物流支持区域
	@RequestMapping("/freight/supportedAreaList.htm")
	public String supportedAreaList(
			@RequestParam("distributionModeId") Long distributionModeId,
			Model model) {
		DistributionMode distribution = sdkFreightManager
				.findDistributionModeById(distributionModeId);
		model.addAttribute("distributionMode", distribution);
		List<Address> addressList = new ArrayList<Address>();
		List<Address> provienceList = AddressUtil.getProviences();
		addressList.addAll(provienceList);
//		for (Address p : provienceList) {
//			List<Address> cityList = AddressUtil.getSubAddressByPid(p.getId());
//			if (Validator.isNotNullOrEmpty(cityList)) {
//				addressList.addAll(cityList);
//			}
//		}
		model.addAttribute("addresslist", addressList);
		return "/freight/distribution/supported-area-list";
	}

	// 异步加载数据
	@RequestMapping("/freight/ztreeAsyncLoadData.json")
	@ResponseBody
	public String ztreeAsyncLoadAreaList(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		if (Validator.isNullOrEmpty(id)) {
			id = 1L;
		}
		List<Address> list = AddressUtil.getSubAddressByPid(id);
//		model.addAttribute("addresslist", list);
		JSONArray jsonArray = new JSONArray();
		for(Address a:list){
			JSONObject  obj = new JSONObject();
			obj.put("id", a.getId());
			obj.put("name", a.getName());
			obj.put("pId", a.getpId());
			if(Validator.isNotNullOrEmpty(AddressUtil.getSubAddressByPid(a.getId()))){
				obj.put("isParent", true);
			}
			jsonArray.add(obj);
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("html/text");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsonArray.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}

	/** 动态获取物流支持区域 */
	@RequestMapping("/freight/supportedAreaList.json")
	@ResponseBody
	public Pagination<SupportedAreaCommand> findItemListJson(
			@RequestParam("distributionId") Long distributionId, Model model,
			@QueryBeanParam QueryBean queryBean) {

		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0) {
			sorts = new Sort[2];
			Sort sort = new Sort("sa.group_no", "asc");
			sorts[0] = sort;

			Sort sort1 = new Sort("sa.type", "desc");
			sorts[1] = sort1;
		}

		Map<String, Object> paraMap = queryBean.getParaMap();
		paraMap.put("distributionModeId", distributionId);
		Pagination<SupportedAreaCommand> args = distributionManager
				.findSupportedAreaByQueryMapWithPage(queryBean.getPage(),
						sorts, paraMap);
		return args;
	}

	@RequestMapping(value = "/freight/toImportSupportedArea.htm")
	public String toImportArea(
			@RequestParam("distributionId") Long distributionId, Model model) {
		List<SupportedAreaCommand> args = distributionManager.findSupportedAreasByDistributionId(distributionId);
		if(Validator.isNotNullOrEmpty(args)){
			model.addAttribute("existArea", true);
		}else{
			model.addAttribute("existArea", false);
		}
		model.addAttribute("distributionId", distributionId);
		return "/freight/distribution/import-supported-area";
	}

	
	// 地址查询
	@RequestMapping("/freight/searchAreaData.json")
	@ResponseBody
	public Pagination<Address> searchAreaList(
			@QueryBeanParam QueryBean queryBean,
			Model model) {
		List<Address> addressList = new ArrayList<Address>();
		Map<String, Object> paraMap = queryBean.getParaMap();
		String name = (String) paraMap.get("name");
		Long areaId = (Long) paraMap.get("areaId");
		
		if(Validator.isNotNullOrEmpty(name) && Validator.isNotNullOrEmpty(areaId)){
			name = name.substring(1, name.length()-1);
			Address address = AddressUtil.getAddressById(areaId);
			if(Validator.isNotNullOrEmpty(address)){
				if (address.getName().indexOf(name) != -1) {
					Address aobj = new Address();
					aobj.setId(address.getId());
					aobj.setName(AddressUtils.getFullAddressName(address.getId(),AddressUtils.SYMBOL_ARROW));
					addressList.add(aobj);
				}
			}
		}else if(Validator.isNullOrEmpty(name) && Validator.isNotNullOrEmpty(areaId)){
			Address address = AddressUtil.getAddressById(areaId);
			if(Validator.isNotNullOrEmpty(address)){
				Address aobj = new Address();
				aobj.setId(address.getId());
				aobj.setName(AddressUtils.getFullAddressName(address.getId(),AddressUtils.SYMBOL_ARROW));
				addressList.add(aobj);
			}
		}else if(Validator.isNotNullOrEmpty(name) && Validator.isNullOrEmpty(areaId)){
			name = name.substring(1, name.length()-1);
			List<Address> nameList = AddressUtil.getAddressesByName(name);
			if(Validator.isNotNullOrEmpty(nameList)){
				for(Address a : nameList){
					a.setName(AddressUtils.getFullAddressName(a.getId(),AddressUtils.SYMBOL_ARROW));
					addressList.add(a);
				}
			}
		}

		Pagination<Address> page = new Pagination<Address>(
				addressList, addressList.size(), 1, 1, 0,
				Integer.MAX_VALUE);
		return page;
	}
	
	
	/**
	 * 下载EXCEL模版
	 * @param response
	 */
	@RequestMapping(value = "/freight/supportedAreaImport.xlsx", method = RequestMethod.GET)
	public void downloadTemplate(@RequestParam("distributionId") Long distributionId,HttpServletResponse response) {
		String path = DEFAULT_PATH + FILE_NAME;
		DistributionMode distribution = sdkFreightManager.findDistributionModeById(distributionId);
		ExcelWriter writer = excelFactory.createExcelWriter("supportedAreaExport");
		Map<String, Object> beans = new HashMap<String, Object>();
		ExpSupportedAreaCommand expArea = getSupportedAreaTitle(distribution);
		beans.put("expDistributionName", expArea);		
		try {
			response.setHeader("Content-type", "application/force-download");
			response.setHeader("Content-Transfer-Encoding", "Binary");
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ FILE_NAME+"\"");
			writer.write(path, response.getOutputStream(), beans);
		} catch (Exception e) {
			e.printStackTrace();
			throw  new BusinessException(ErrorCodes.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 支持区域导出
	 * @param response
	 */
	@RequestMapping(value = "/freight/supportedAreaExport.xlsx", method = RequestMethod.GET)
	public void export(@RequestParam("distributionId") Long distributionId, HttpServletResponse response) {
		String path = DEFAULT_PATH + FILE_NAME;
		DistributionMode distribution = sdkFreightManager.findDistributionModeById(distributionId);
		ExcelWriter writer = excelFactory.createExcelWriter("supportedAreaExport");
		Map<String, Object> beans = new HashMap<String, Object>();
		ExpSupportedAreaCommand expArea = getSupportedAreaTitle(distribution);
		List<ExpSupportedAreaCommand> list = distributionManager.exportSupportedAreasByDistributionId(distributionId);
		beans.put("expDistributionName", expArea);
		beans.put("expSupportedAreaList", list);
		
		try {
			response.setHeader("Content-type", "application/force-download");
			response.setHeader("Content-Transfer-Encoding", "Binary");
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ FILE_NAME+"\"");
			writer.write(path, response.getOutputStream(), beans);
		} catch (Exception e) {
			e.printStackTrace();
			throw  new BusinessException(ErrorCodes.SYSTEM_ERROR);
		}
	}
	
	private ExpSupportedAreaCommand getSupportedAreaTitle(DistributionMode distribution) {
		ExpSupportedAreaCommand expArea = new ExpSupportedAreaCommand();
		expArea.setDistributionName(distribution.getName()+"支持区域");
		return expArea;
	}
	
	/**
	 * 导入支持区域
	 * 为解决uploadify的406错误，直接在response中写
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/freight/supportedAreaImport.json", method = RequestMethod.POST)
	public void uploadSupportedArea(
			@RequestParam("distributionId") Long distributionId,
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> rs = new HashMap<String, Object>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		
		try {
			distributionManager.importSupportedAreaFile(file.getInputStream(), distributionId);
		} catch (BusinessException e) {
			e.printStackTrace();
			
			Boolean flag = true;
			List<String> errorMessages = new ArrayList<String>();
			BusinessException linkedException = e;
			while(flag){
				String message = "";
				if(linkedException.getErrorCode()==0){
					message = linkedException.getMessage();
				}else{
					if(null==linkedException.getArgs()){
						message = getMessage(linkedException.getErrorCode());
					}else{
						message = getMessage(linkedException.getErrorCode(),linkedException.getArgs());
					}
					
				}
				errorMessages.add(message);
			    
				if(null==linkedException.getLinkedException()){
					flag = false;
				}else{
					linkedException = linkedException.getLinkedException();
				}
			}
			//返回值
			rs.put("isSuccess", false);
			rs.put("description", errorMessages);
			returnRes(response, rs);
		} catch (IOException e) {
			e.printStackTrace();
			BusinessException be = new BusinessException(ErrorCodes.SYSTEM_ERROR);
			rs.put("isSuccess", false);
			rs.put("description", be.getMessage());
			returnRes(response, rs);
		}
		rs.put("isSuccess", true);
		returnRes(response, rs);
	}
	
	private void returnRes(HttpServletResponse response, Map<String, Object> rs) {
		PrintWriter out = null;
		try {
			response.setContentType("text/json;charset=UTF-8");
			out = response.getWriter();
			out.write(JsonFormatUtil.format(rs));
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

}
