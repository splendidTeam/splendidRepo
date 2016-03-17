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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.freight.DistributionModeCommand;
import com.baozun.nebula.dao.freight.ShippingFeeConfigDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.freight.command.ExpShippingFeeCommand;
import com.baozun.nebula.freight.command.ShippingFeeConfigCommand;
import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.freight.DistributionManager;
import com.baozun.nebula.manager.freight.ShippingManager;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.freight.ShippingTemeplate;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.zk.FreightWatchInvoke;
import com.baozun.nebula.zk.ZooKeeperOperator;

/**
 * @author jumbo
 *
 */
@Controller
public class ShippingController extends BaseController{

	@Autowired
	private LogisticsManager logisticsManager;
	
	@Autowired
	private ShopManager			shopManager;
	
	@Autowired
	private ShippingManager			shippingManager;
	
	@Autowired
	private DistributionManager			distributionManager;
	
	@Autowired
	private ShippingFeeConfigDao shippingFeeConfigDao;
	
	@Autowired
	private ExcelManipulatorFactory excelFactory;
	
	@Autowired
	private ZooKeeperOperator zooKeeperOperator;
	
	private static final String		DEFAULT_PATH							= "excel/";
	private static final String		FILE_NAME								= "shipping_fee_import.xlsx";
	
	/** 进入运费模板 */
	@RequestMapping(value = "/freight/shippingTemeplateList.htm")
	public String entryShippingTemeplate(Model model) {
		return "/freight/shipping/shipping-temeplate-list";
	}
	
	/** 运费模板列表 */
	@RequestMapping(value = "/freight/shippingTemeplateList.json")
	@ResponseBody
	public Pagination<ShippingTemeplate> distributionList(Model model,@QueryBeanParam QueryBean queryBean) {
		// 查询orgId
		UserDetails userDetails = this.getUserDetails();

		ShopCommand shopCommand = null;
		Long shopId = 0L;

		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 根据orgId查询shopId
		if (currentOrgId != null){
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();
		}
		Map<String, Object> paraMap = queryBean.getParaMap();
		String calculationType = (String) paraMap.get("calculationtype");
		if(Validator.isNotNullOrEmpty(calculationType)){
			paraMap.remove("calculationtype");
			calculationType = calculationType.replaceAll("%", "");
			paraMap.put("calculationtype", calculationType.trim());
		}
		Sort[] sorts = queryBean.getSorts();
		if(Validator.isNullOrEmpty(sorts)){
			sorts=Sort.parse("ISDEFAULT desc");
		}
		Pagination<ShippingTemeplate> temeplateList = logisticsManager
			.findShippingTemeplateList(queryBean.getPage(), sorts, paraMap,shopId);
		return temeplateList;
	}
	
	/** 全部生效运费模板 */
	@RequestMapping(value = "/freight/enableShippingTemplate.htm")
	@ResponseBody
	public Object enableShippingTemeplate( Model model ) {
		zooKeeperOperator.noticeZkServer(FreightWatchInvoke.LISTEN_PATH);
		return SUCCESS;
	}
	
	/** 进入新建运费模板 */
	@RequestMapping(value = "/freight/createShippingTemplate.htm")
	public String createShippingTemeplate(@RequestParam("id")Long id, Model model ) {
		List<DistributionMode> distributionModes =  distributionManager.getAllDistributionModeList();
		List<DistributionModeCommand> dbms = new ArrayList<DistributionModeCommand>();
		if(distributionModes != null){
			for (DistributionMode dbm : distributionModes) {
				DistributionModeCommand dbmc = new DistributionModeCommand();
				dbmc.setId(dbm.getId());
				dbmc.setName(dbm.getName());
				dbms.add(dbmc);
			}
		}
		if(Validator.isNotNullOrEmpty(id)){//编辑
			ShippingTemeplateCommand shipping = logisticsManager.findShippingTemeplateCommandById(id);
			model.addAttribute("shippingTemplate", shipping);
			
			List<DistributionMode> DistributionList = distributionManager.getDistributionModeListByTemplateId(id);
			model.addAttribute("distributionModeList", DistributionList);
			if(DistributionList != null){
				for (DistributionMode distributionMode : DistributionList) {
					for (DistributionModeCommand command : dbms) {
						if(distributionMode.getId().equals(command.getId())){
							command.setSelected(true);
						}
					}
				}
			}
		}
		//添加物流方式
		model.addAttribute("dbms", dbms);
		return "/freight/shipping/shipping-temeplate-create";
	}
	
	/**
	 * 保存运费模板
	 * @return
	 */
	@RequestMapping("/freight/saveShippingTemplate.json")
	@ResponseBody
	public Object saveTemplate(ShippingTemeplateCommand shippingTemeplateCmd,
			HttpServletRequest request,HttpServletResponse response,Model model){
		// 查询orgId
		UserDetails userDetails = this.getUserDetails();

		ShopCommand shopCommand = null;
		Long shopId = 0L;

		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 根据orgId查询shopId
		if (currentOrgId != null){
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();
		}
		shippingTemeplateCmd.setShopId(shopId);
		if(Validator.isNotNullOrEmpty(shippingTemeplateCmd.getId())){//修改
			logisticsManager.updateShippingTemeplate(shippingTemeplateCmd);
		}else{
			logisticsManager.saveShippingTemeplate(shippingTemeplateCmd);
		}
		return SUCCESS;
	}
	
	/**
	 * 通过ids批量删除
	 * 
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping("/freight/butchRemoveShippingTemplate.json")
	@ResponseBody
	public Object deleteItem(@RequestParam("ids") List<Long> ids, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		shippingManager.removeShippingTemplateByIds(ids);
		return SUCCESS;
	}
	/**
	 * 删除运费模板
	 * @return
	 */
	@RequestMapping("/freight/deleteShippingTemplate.htm")
	@ResponseBody
	public Object deleteTemplate(@RequestParam("id") Long id,
			HttpServletRequest request,HttpServletResponse response){
		logisticsManager.removeShippingTemeplate(id);
		return SUCCESS;
	}
	
	/**
	 * 设置默认运费模板
	 * @return
	 */
	@RequestMapping("/freight/setDefaultShippingTemplate.json")
	@ResponseBody
	public Object setDefaultTemplate(@RequestParam("id")Long id,
			HttpServletRequest request,HttpServletResponse response,Model model){
		// 查询orgId
		UserDetails userDetails = this.getUserDetails();

		ShopCommand shopCommand = null;
		Long shopId = 0L;

		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 根据orgId查询shopId
		if (currentOrgId != null){
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();
		}
		logisticsManager.updateShippingTemeplateIsDefault(shopId, id, ShippingTemeplate.ISDEFAULT);
		return SUCCESS;
	}
	
	
	// 查看运费表
	@RequestMapping("/freight/shippingFeeConfigList.htm")
	public String shippingFeeConfigList(@RequestParam("templateId")Long templateId,Model model){
		ShippingTemeplateCommand template = logisticsManager.findShippingTemeplateCommandById(templateId);
		model.addAttribute("shippingTemplate", template);
		return "/freight/shipping/shipping-fee-config";
	}
	
	/** 动态获取运费表*/
	@RequestMapping("/freight/shippingFeeConfigList.json")
	@ResponseBody
	public Pagination<ShippingFeeConfigCommand> findShippingFeeConfigJson(@RequestParam("templateId")Long templateId, 
			Model model,@QueryBeanParam QueryBean queryBean){
		ShippingTemeplateCommand template = logisticsManager.findShippingTemeplateCommandById(templateId);
		List<ShippingFeeConfigCommand> config = template.getFeeConfigs();
		Pagination<ShippingFeeConfigCommand> args = 
			new Pagination<ShippingFeeConfigCommand>(config,config.size(),1,1,0,Integer.MAX_VALUE);
		return args;
	}
	
	// 进入导入运费表
	@RequestMapping("/freight/openShippingFeeConfig.htm")
	public String openShippingFeeConfig(@RequestParam("templateId")Long templateId, Model model){
		List<ShippingFeeConfigCommand> configList = shippingFeeConfigDao.findShippingFeeConfigsByTemeplateId(templateId);
		if(Validator.isNotNullOrEmpty(configList)){
			model.addAttribute("existFee", true);
		}else{
			model.addAttribute("existFee", false);
		}
		model.addAttribute("templateId", templateId);
		return "/freight/shipping/import-shipping-fee-config";
	}
	
	
	/**
	 * 下载EXCEL模版
	 * @param response
	 */
	@RequestMapping(value = "/freight/shippingFeeImport.xlsx", method = RequestMethod.GET)
	public void downloadTemplate(@RequestParam("templateId") Long templateId, HttpServletResponse response) {
		String path = DEFAULT_PATH + FILE_NAME;
		ShippingTemeplateCommand template = logisticsManager.findShippingTemeplateCommandById(templateId);
		ExcelWriter writer = excelFactory.createExcelWriter("shippingExport");
		Map<String, Object> beans = new HashMap<String, Object>();
		ExpShippingFeeCommand expFeeName = getShippingFeeTitle(template);
		beans.put("expTemplateName", expFeeName);
		beans.put("expShippingName", expFeeName);
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
	 * 运费表导出
	 * @param response
	 */
	@RequestMapping(value = "/freight/shippingFeeExport.xlsx", method = RequestMethod.GET)
	public void export(@RequestParam("templateId") Long templateId, HttpServletResponse response) {
		String path = DEFAULT_PATH + FILE_NAME;
		ShippingTemeplateCommand template = logisticsManager.findShippingTemeplateCommandById(templateId);
		ExcelWriter writer = excelFactory.createExcelWriter("shippingExport");
		Map<String, Object> beans = new HashMap<String, Object>();
		List<ExpShippingFeeCommand> list = shippingManager.exportShippingFeeConfigCommandList(templateId);
		ExpShippingFeeCommand expFeeName = getShippingFeeTitle(template);
		beans.put("expTemplateName", expFeeName);
		beans.put("expShippingName", expFeeName);
		beans.put("expShippingList", list);
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
	
	private ExpShippingFeeCommand getShippingFeeTitle(ShippingTemeplateCommand template) {
		ExpShippingFeeCommand expFee = new ExpShippingFeeCommand();
		
		String province = "目的地(省/自治区)";
		String city = "目的地(市/区)";
		String area = "目的地(区/县)";
		String town = "目的地(乡/镇/街道)";
		String distributionModeName = "物流方式";
		String firstPartUnit = "";
		String firstPartPrice = "";
		String subsequentPartUnit = "";
		String subsequentPartPrice = "";
		String templateName = template.getName()+"运费表";
		String calculationType = template.getCalculationType();
		if("unit".equals(calculationType)){
			firstPartUnit = "首件运费单位";
			firstPartPrice = "首件运费价格";
			subsequentPartUnit = "续件运费单位";
			subsequentPartPrice = "续件运费价格";
		}else if("weight".equals(calculationType)){
			firstPartUnit = "首重运费单位";
			firstPartPrice = "首重运费价格";
			subsequentPartUnit = "续重运费单位";
			subsequentPartPrice = "续重运费价格";
		}else if("base".equals(calculationType)){
			firstPartUnit = "首基础运费单位";
			firstPartPrice = "首基础运费价格";
			subsequentPartUnit = "续基础运费单位";
			subsequentPartPrice = "续基础运费价格";
		}
		expFee.setTemplateName(templateName);
		expFee.setProvince(province);
		expFee.setArea(area);
		expFee.setCity(city);
		expFee.setTown(town);
		expFee.setDistributionModeName(distributionModeName);
		expFee.setFirstPartPrice(firstPartPrice);
		expFee.setFirstPartUnit(firstPartUnit);
		expFee.setSubsequentPartPrice(subsequentPartPrice);
		expFee.setSubsequentPartUnit(subsequentPartUnit);
		return expFee;
	}
	
	/**
	 * 导入运费表
	 * 为解决uploadify的406错误，直接在response中写
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/freight/shippingFeeImport.json", method = RequestMethod.POST)
	public void uploadshippingFee(
			@RequestParam("templateId") Long templateId,
			HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> rs = new HashMap<String, Object>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		
		try {
			shippingManager.importShippingFeeFile(file.getInputStream(), templateId);
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
