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
 */
package com.baozun.nebula.web.controller.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.ShopProperty;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.utils.ZipUtil;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品导入
 * 
 * @author lxy
 */
@Controller
public class SkuImportController extends BaseController{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(SkuImportController.class);
	
	private static long			maxUploadPackageSize	= 300 * 1024 * 1024;

	private static Float		quality					= 1.0f;

	private static Pattern		imgSubFolderReg			= null;
	
	@Autowired
	private ShopManager			shopManager;
	
	@Autowired
	private ItemManager			itemManager;
	
	@Autowired
	private IndustryManager		industryManager;

	

		
	/**
	 * 图片
	 * 
	 * @param request
	 * @param model
	 * @param response
	 */
	@RequestMapping(value = "/sku/picUpload.json",method = RequestMethod.POST)
	@ResponseBody
	public String skuImgUpload(@RequestParam(required = true,value = "imgPackage") MultipartFile imgPackage,
			HttpServletRequest request,
			Model model){
		model.asMap().clear();
		File tmpFolder = new File("sdafdasdfasdfdasfaf/picd" + File.separator);
		try{
			if (imgPackage != null && imgPackage.getSize() < maxUploadPackageSize){
				if (tmpFolder.exists() == false){
					tmpFolder.mkdirs();
				}

				// 解压图片
				int unzipCount = ZipUtil.unzipFiles(imgPackage.getInputStream(), tmpFolder, imgSubFolderReg);
				
				model.addAttribute("msg", "成功上传[" + imgPackage.getOriginalFilename() + "], 处理的源图片数量：" + unzipCount);
				model.addAttribute("flag", true);
			}else{
				model.addAttribute("msg", "上传失败：文件为空或大于" + maxUploadPackageSize + "个字节");
				model.addAttribute("flag", false);
			}
		}catch (Exception e){// TODO 应该是捕获exception，而不是ioexception，10-31出现了一个png图片名包含中文引起的IllArgumentExcepiton，再增加一个：如果异常了，就删除这个目录
			log.debug("IOException :", e);
			model.addAttribute("msg", "上传失败：IO读写错误，请确认图片路径后联系技术人员</br>" + e.getMessage());
			model.addAttribute("flag", false);
			e.printStackTrace();
		}
		return "json";
	}
	
	//-------------------------------------------------------------------------
	@RequestMapping(value = "/item/toImportSku.htm")
	public String toImport(Model model){
		Long shopId = shopManager.getShopId(getUserDetails());
		Sort[] sorts = Sort.parse("id desc");
		// 获取行业信息
		List<Map<String, Object>> industryList = processIndusgtryList(shopManager.findAllIndustryList(sorts), shopId);
		model.addAttribute("industryList", industryList);
		return "/product/item/import-sku";
	}
	private List<DynamicPropertyCommand> getSalesProp(Long industryId){
		List<DynamicPropertyCommand> dynamicPropertyCommandList = this.findDynamicPropertisByShopIdAndIndustryId(industryId);
		//销售属性
		List<DynamicPropertyCommand> salePropList = new ArrayList<DynamicPropertyCommand>();
		if(Validator.isNotNullOrEmpty(dynamicPropertyCommandList)){
			for(DynamicPropertyCommand dynamicPropertyCommand :dynamicPropertyCommandList){
				if(dynamicPropertyCommand.getProperty().getIsSaleProp()){ 
					salePropList.add(dynamicPropertyCommand);
				}
			}
		}
		return salePropList;
	}
	
	
	public List<DynamicPropertyCommand> findDynamicPropertisByShopIdAndIndustryId(Long industryId){
		Long shopId = shopManager.getShopId(getUserDetails());
		// 根据行业Id和店铺Id查找对应属性和属性值
		List<DynamicPropertyCommand> dynamicPropertyCommandList = itemManager.findDynamicPropertis(shopId, industryId);
		return dynamicPropertyCommandList;
	}
	
	/**
	 * 对页面的数据节点进行判断存储
	 * 
	 * @param industryList
	 * @return
	 */
	private List<Map<String, Object>> processIndusgtryList(List<Industry> industryList,Long shopId){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<ShopProperty> shopPropertyList = new ArrayList<ShopProperty>();
		if (shopId != null){
			shopPropertyList = shopManager.findShopPropertyByshopId(shopId);
		}
		for (Industry indu : industryList){
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("id", indu.getId());
			map.put("pId", null == indu.getParentId() ? 0 : indu.getParentId());
			map.put("indu_name", indu.getName());
			map.put("open", null == indu.getParentId() ? true : false);
			for (Industry sec_indu : industryList){
				if ((sec_indu.getParentId()).equals(indu.getId())){
					map.put("noCheck", true);
					break;
				}

			}

			if (shopPropertyList != null){
				for (int i = 0; i < shopPropertyList.size(); i++){
					if (indu.getId().equals(shopPropertyList.get(i).getIndustryId())){
						map.put("checked", "true");
						// map.put("open", true);
						break;
					}
				}
			}
			resultList.add(map);
		}
		if (shopPropertyList != null){
			for (int i = 0; i < shopPropertyList.size(); i++){
				for (Map<String, Object> map : resultList){
					String industryId = shopPropertyList.get(i).getIndustryId().toString();
					String mapId = map.get("id").toString();
					if (industryId.equals(mapId)){
						searchChecked(resultList, shopPropertyList.get(i).getIndustryId().toString());
					}

				}

			}
		}

		return resultList;
	}

	// 递归用于筛选checked
	static void searchChecked(List<Map<String, Object>> resultList,String id){
		for (Map<String, Object> map : resultList){
			if (map.get("id").toString().equals(id)){
				map.put("isShow", true);
				if (!map.get("pId").toString().equals("0")){
					searchChecked(resultList, map.get("pId").toString());
				}
			}

		}
	}

}
