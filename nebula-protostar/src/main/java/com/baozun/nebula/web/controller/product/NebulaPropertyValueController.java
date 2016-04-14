/**
 * 
 */
package com.baozun.nebula.web.controller.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.product.PropertyValueCommand;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueGroup;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.manager.product.SdkPropertyManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

/**
 * 商品属性值控制器 <br/>
 * 实现以下功能点
 * <ol>
 * <li>根据属性创建属性值（i18n）</li>
 * <li>“属性值分组”创建，修改</li>
 * <li>“属性值”和“属性值分组”的关联</li>
 * <li>“属性值分组”下面属性值的排序</li>
 * <li>属性值 导入导出功能</li>
 * </ol>
 * 
 * @author viktor huang
 * @date Apr 6, 2016 5:39:53 PM
 */
@Controller
public class NebulaPropertyValueController extends BaseController{

	private static final Logger	LOGGER				= LoggerFactory.getLogger(NebulaPropertyValueController.class);
	
	@Autowired
	private PropertyManager		propertyManager;

	@Autowired
	private SdkPropertyManager	sdkPropertyManager;

	@Autowired
	private SdkI18nLangManager	sdkI18nLangManager;
	
	/**
	 * 进入属性值编辑页面 ， 渲染选择属性下的属性值分组和属性值
	 * 
	 * @param request
	 * @param model
	 * @param propertyId
	 *            属性id
	 * @return
	 */
	@RequestMapping(value = "/i18n/property/editPropertyValue.htm")
	public String showEditPropertyValue(
			HttpServletRequest request,
			Model model,
			@RequestParam(value = "propertyId",required = true) Long propertyId){

		Property property = propertyManager.findPropertyById(propertyId);
		model.addAttribute("property", property);
		// 设置属性组分组只针对于属性类型为‘多选’类型
		if (property.getEditingType().equals(Property.EDITING_TYPE_MULTI_SELECT)){
			List<PropertyValueGroup> propertyValueGroupList = sdkPropertyManager.findProValueGroupByPropertyId(propertyId);
			model.addAttribute("propertyValueGroupList", propertyValueGroupList);
		}

		List<PropertyValue> propertyValueList = propertyManager.findPropertyValueList(propertyId);
		model.addAttribute("propertyValueList", propertyValueList);

		return "/product/property/edit-property-value";

	}

	/**
	 * 新建或更新商品属性值
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param groupId
	 *            属性值组id
	 * @param propertyValues
	 *            属性值
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/i18n/property/addOrUpdatePropertyValue.json",method = RequestMethod.POST)
	public BackWarnEntity addOrUpdatePropertyValue(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "groupId",required = false) Long groupId,
			@I18nCommand PropertyValueCommand propertyValues){

		try{
			sdkPropertyManager.addOrUpdatePropertyValue(groupId, propertyValues);
		}catch (Exception e){
			LOGGER.error("", e);
			return FAILTRUE;
		}
		return SUCCESS;
	}

	/**
	 * 分页查询属性值（i18n）
	 * 
	 * @param model
	 * @param propertyId
	 *            属性id
	 * @param proValueGroupId
	 *            属性值组id
	 * @param queryBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/i18n/property/findPropertyValueByPage.json")
	public Pagination<PropertyValueCommand> findPropertyValueByPage(
			Model model,
			@RequestParam(value = "propertyId",required = true) Long propertyId,
			@RequestParam(value = "proValueGroupId",required = false) Long proValueGroupId,
			@QueryBeanParam QueryBean queryBean){

		Pagination<PropertyValueCommand> result = sdkPropertyManager.findPropertyValueByPage(
				queryBean.getPage(),
				queryBean.getSorts(),
				propertyId,
				proValueGroupId);

		return result;
	}

	/**
	 * 渲染进入修改或者新增属性值组页面
	 * 
	 * @param request
	 * @param model
	 * @param type
	 *            操作类型（0=new || 1=update）
	 * @param groupId
	 *            属性值组id
	 * @param propertyId
	 *            对应的属性
	 * @return
	 */
	@RequestMapping(value = "/i18n/property/showAddOrUpdateGroup.htm")
	public String showAddOrUpdateGroup(
			HttpServletRequest request,
			Model model,
			@RequestParam(value = "type",required = true) int type,
			@RequestParam(value = "groupId",required = false) Long groupId,
			@RequestParam(value = "propertyId",required = true) Long propertyId){

		if (type == 0){
			// 新建
			List<PropertyValue> propertyValueList = propertyManager.findPropertyValueList(propertyId);
			// 没有与属性值组绑定的属性值
			model.addAttribute("freeProValueList", propertyValueList);
		}else{

			// 已经加入到属性值组的PropertyValue
			List<PropertyValue> boundProValueList = sdkPropertyManager.findBoundGroupPropertyValue(groupId);
			model.addAttribute("boundProValueList", boundProValueList);
			// 还没有加入到属性值组的PropertyValue
			List<PropertyValue> freeProValueList = sdkPropertyManager.findFreeGroupPropertyValue(propertyId, groupId);
			model.addAttribute("freeProValueList", freeProValueList);
			PropertyValueGroup proValueGroup = sdkPropertyManager.findProValueGroupById(groupId);
			model.addAttribute("proValueGroup", proValueGroup);
		}
		model.addAttribute("propertyId", propertyId);

		return "/product/property/property-value-group";
	}

	/**
	 * 更新或修改属性值组和组与属性值的关联
	 * 
	 * @param request
	 * @param model
	 * @param propertyId
	 *            属性id
	 * @param groupId
	 *            属性值组id
	 * @param groupName
	 *            属性值组名称
	 * @param propertyValueIds
	 *            要关联到此属性值组的属性值ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/i18n/property/saveOrUpdatePropertyValueGroup.json")
	public BackWarnEntity saveOrUpdatePropertyValueGroup(
			HttpServletRequest request,
			Model model,
			@RequestParam("propertyId") Long propertyId,
			@RequestParam("groupId") Long groupId,
			@RequestParam("groupName") String groupName,
			@RequestParam("propertyValueIds") String propertyValueIds){
		try{
			// 保存PropertyValueGroup
			PropertyValueGroup propertyValueGroup = sdkPropertyManager.savePropertyValueGroup(propertyId, groupId, groupName);
			groupId = propertyValueGroup.getId();
			List<Long> valueIds = new ArrayList<Long>();
			// 处理【属性值】和【属性值组】的关联
			if (Validator.isNotNullOrEmpty(propertyValueIds)){
				String[] strIds = propertyValueIds.split(",");
				if (strIds.length > 0){
					for (int i = 0; i < strIds.length; i++){
						Long valueId = Long.valueOf(strIds[i].toString());
						valueIds.add(valueId);
					}
				}else{
					valueIds.add(Long.valueOf(propertyValueIds.toString()));
				}
			}

			sdkPropertyManager.bindPropertyValueAndProValueGroup(valueIds, groupId);
			return SUCCESS;

		}catch (Exception e){
			LOGGER.error("", e);
			return FAILTRUE;
		}

	}

	/**
	 * 根据属性id查询下面所有的属性值ORDER BY proValue.sort_no ASC
	 * 
	 * @param propertyId
	 *            属性id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/i18n/property/findAllPropertyValueByPropertyId.json",method = RequestMethod.POST)
	public BackWarnEntity findAllPropertyValueByPropertyId(@RequestParam("propertyId") Long propertyId){
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		Page page = new Page(0, 10000);
		try{
			Pagination<PropertyValueCommand> properyValuePage = sdkPropertyManager.findPropertyValueByPage(page, null, propertyId, null);
			List<PropertyValueCommand> propertyValueList = properyValuePage.getItems();
			backWarnEntity.setIsSuccess(true);
			backWarnEntity.setDescription(propertyValueList);
		}catch (Exception e){
			LOGGER.error("", e);
			backWarnEntity.setIsSuccess(false);
		}
		return backWarnEntity;
	}

	// @ResponseBody
	// @RequestMapping(value = "/i18n/property/exportPropertyValue.json", method = RequestMethod.POST)
	public String itemExport(
			@RequestParam("industryId") Long industryId,
			@RequestParam("selectCodes") String[] selectCodes,
			@RequestParam(value = "itemCodes",required = false) String itemCodes,
			HttpServletRequest request,
			HttpServletResponse response){

		return "json";
	}
	
	
	/**
	 * 下载属性值模板
	 * @return String
	 * @param model
	 * @param request
	 * @param response
	 * @param propertyId
	 * @author 冯明雷
	 * @time 2016年4月13日下午3:52:27
	 */
	@RequestMapping(value = "/product/tplt_property_value_import.xls")
	@ResponseBody
	public String downLoadTmplOfPropertyValue(
			@RequestParam("propertyId") Long propertyId,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){
		String path = "excel/tplt_property_value_import.xlsx";
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
		InputStream is = null;
		OutputStream out = null;		
		response.setDateHeader("Expires", -1);
		try{
			is = new FileInputStream(file);
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		try{
			out = response.getOutputStream();
			
			
			Property property = propertyManager.findPropertyById(propertyId);
			List<PropertyValue> propertyValueList = propertyManager.findPropertyValueList(property.getId());			
			
			HSSFWorkbook xls = new HSSFWorkbook(is);			
			//样式
			HSSFCellStyle titleCellStyle = xls.createCellStyle();
			titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			titleCellStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
			titleCellStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
			titleCellStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
			titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	
			
			
			// 第一个sheet
			HSSFSheet sheet = xls.getSheetAt(0);
			
			
			//第二行第一列
			sheet.getRow(1).createCell(0).setCellValue("属性值ID（只读）");
			if(LangProperty.getI18nOnOff()){
				List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();
				int i=1;
				for (I18nLang i18nLang : i18nLangs){
					HSSFCell cell = sheet.getRow(1).createCell(i);
					cell.setCellStyle(titleCellStyle);
					cell.setCellValue("属性值（"+i18nLang.getValue()+"，必填）");
					i++;
				}
			}else{
				//第二行第二列
				HSSFCell cell = sheet.getRow(1).createCell(1);
				sheet.getRow(1).createCell(1).setCellValue("属性值（必填）");
				cell.setCellValue("属性值（必填）");
			}
			
			
			//从第三行开始是具体的值
			int i=2;
			HSSFRow codeRow = null;
			for (PropertyValue propertyValue : propertyValueList){
				sheet.createRow(i);
				codeRow=sheet.getRow(i);
				
				codeRow.createCell(0).setCellStyle(titleCellStyle);
				codeRow.createCell(0).setCellValue(propertyValue.getId());
				
				codeRow.createCell(1).setCellValue(propertyValue.getValue());
				i++;
			}

			String fileName=file.getName();
			fileName=fileName.substring(0, fileName.lastIndexOf("."))+"_"+new Date().getTime()+".xlsx";
			
			// 下载
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			
			xls.write(out);
		}catch (Exception e1){
			e1.printStackTrace();
		}finally{
			try{
				out.flush();
				out.close();
				is.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		
		return "json";
	}
	
	

}
