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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.product.PropertyValueCommand;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueGroup;
import com.baozun.nebula.model.product.PropertyValueLang;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.manager.product.SdkPropertyManager;
import com.baozun.nebula.utils.InputStreamCacher;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.PropertyValueUploadCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;
import com.feilong.core.util.RandomUtil;

import loxia.dao.Page;
import loxia.dao.Pagination;

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

	private static final Logger	LOGGER	= LoggerFactory.getLogger(NebulaPropertyValueController.class);

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

		Property property = sdkPropertyManager.findPropertyById(propertyId);
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
			BackWarnEntity backWarnEntity = new BackWarnEntity();
			backWarnEntity.setIsSuccess(true);
			Long proValueId = propertyValues.getId();
			Long propertyId = propertyValues.getPropertyId();

			MutlLang lang = (MutlLang) propertyValues.getValue();
			// 语言
			String[] langs = lang.getLangs();
			// 值
			String[] values = lang.getValues();
			Map<String, String> validateResult = new HashMap<String, String>();
			/**
			 * 判断值是否重复
			 */
			for (int i = 0; i < langs.length; i++){
				PropertyValue propertyValue = sdkPropertyManager.findCountByPVIdAndLangValue(propertyId, values[i]);
				boolean existFlag = Validator.isNotNullOrEmpty(propertyValue);
				
				if (Validator.isNotNullOrEmpty(proValueId)){
					// update ,count可以 == 1
					Long existId = existFlag ? propertyValue.getId() : null;
					//如果存在相同的值，并且id和要操作的id不相等
					if (existFlag && !proValueId.equals(existId)){
						backWarnEntity.setIsSuccess(false);
						validateResult.put(langs[i], "1004");
					}
				}else{
					// 新增
					if (existFlag){
						backWarnEntity.setIsSuccess(false);
						validateResult.put(langs[i], "1004");
					}
				}

			}
			if (!backWarnEntity.getIsSuccess()){
				backWarnEntity.setDescription(validateResult);
				return backWarnEntity;
			}
			// 如果没有重复-->添加或修改属性值
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

		Pagination<PropertyValueCommand> result = sdkPropertyManager
				.findPropertyValueByPage(queryBean.getPage(), queryBean.getSorts(), propertyId, proValueGroupId);

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
			@RequestParam(value = "propertyId",required = true) Long propertyId,
			@RequestParam(value = "propertyName",required = true) String propertyName){

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
		model.addAttribute("propertyName", propertyName);

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

	/**
	 * 根据属性值id删除属性值 会处理 【PropertyValue,PropertyValueLang,PropertyValueGroupRelation】
	 * 
	 * @param pvIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/i18n/property/deletePropertyValuesByIds.json",method = RequestMethod.POST)
	public BackWarnEntity deletePropertyValuesByIds(@RequestParam("pvIds") String pvIds){
		BackWarnEntity backWarnEntity = new BackWarnEntity();

		try{

			String[] split = pvIds.split(",");

			List<Long> ids = new ArrayList<Long>();
			if (Validator.isNotNullOrEmpty(split)){
				for (String str : split){
					ids.add(Long.valueOf(str));
				}
				sdkPropertyManager.deletePropertyValueById(ids);
			}
			backWarnEntity.setIsSuccess(true);
		}catch (Exception e){
			LOGGER.error("", e);
			backWarnEntity.setIsSuccess(false);
		}
		return backWarnEntity;
	}

	/**
	 * 根据属性id查询下面所有的属性值ORDER BY proValue.sort_no ASC
	 * 
	 * @param propertyId
	 *            属性id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/i18n/property/updatePropertyValueSortNoById.json",method = RequestMethod.POST)
	public BackWarnEntity updatePropertyValueSortNoById(@RequestParam("result") String result){
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		try{
			String[] orderStrings = result.split("-");
			for (int i = 0; i < orderStrings.length; i++){
				String[] temp = orderStrings[i].split(",");
				Long pvId = Long.valueOf(temp[0]);
				Integer sortNo = Integer.valueOf(temp[1]);
				Integer newSortNo = Integer.valueOf(temp[2]);
				if (!sortNo.equals(newSortNo)){
					sdkPropertyManager.updatePropertyValueSortById(pvId, newSortNo);
				}
			}
			backWarnEntity.setIsSuccess(true);
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
	 * 
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
		boolean i18nOnOff = LangProperty.getI18nOnOff();
		List<I18nLang> i18nLangs = sdkI18nLangManager.geti18nLangCache();

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

			List<PropertyValueLang> propertyValueLangs = null;
			if (i18nOnOff){
				propertyValueLangs = propertyManager.findPropertyValueLangByPropertyId(property.getId());
			}

			HSSFWorkbook xls = new HSSFWorkbook(is);

			// 第一个sheet
			HSSFSheet sheet = xls.getSheetAt(0);

			// 如果有国际化，动态添加字段
			if (i18nOnOff){
				// 获取到第一个单元格的样式
				HSSFRow titleRow = sheet.getRow(2);
				HSSFCellStyle titleCellStyle = titleRow.getCell(0).getCellStyle();

				// 第一行，用来表示当前这一列的值是什么语言的
				HSSFRow langRow = sheet.createRow(0);

				// 从第四列开始
				int i = 3;
				for (I18nLang i18nLang : i18nLangs){
					langRow.createCell(i).setCellValue(i18nLang.getKey());

					HSSFCell cell = titleRow.createCell(i);
					cell.setCellStyle(titleCellStyle);
					cell.setCellValue("属性值--" + i18nLang.getValue() + "（文本，必填）");
					i++;
				}
			}

			// 从第三行开始是具体的值
			int i = 3;
			HSSFRow codeRow = null;
			for (PropertyValue propertyValue : propertyValueList){
				codeRow = sheet.createRow(i);

				codeRow.createCell(0).setCellValue(propertyValue.getId());
				codeRow.createCell(1).setCellValue(propertyValue.getValue());
				codeRow.createCell(2).setCellValue(propertyValue.getSortNo());

				if (i18nOnOff){
					if (propertyValueLangs != null){
						int j = 3;
						for (I18nLang i18nLang : i18nLangs){
							for (PropertyValueLang propertyValueLang : propertyValueLangs){
								if (i18nLang.getKey().equals(propertyValueLang.getLang())){
									codeRow.createCell(j).setCellValue(propertyValueLang.getValue());
									j++;
									break;
								}
							}
						}
					}
				}
				i++;
			}

			String fileName = file.getName();
			fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + RandomUtil.createRandomWithLength(4) + ".xlsx";

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

	/**
	 * 上传属性值
	 * 
	 * @return void
	 * @param request
	 * @param model
	 * @param response
	 * @author 冯明雷
	 * @time 2016年4月14日下午3:02:25
	 */
	@RequestMapping(value = "/sku/propertyValueUpload.json",method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity propertyValueUpload(
			@RequestParam("propertyId") Long propertyId,
			HttpServletRequest request,
			Model model,
			HttpServletResponse response){
		boolean isSuccess = true;
		String description = "";

		boolean i18nOnOff = LangProperty.getI18nOnOff();

		response.setContentType("text/html;charset=UTF-8");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");

		InputStreamCacher cacher = null;
		try{
			cacher = new InputStreamCacher(file.getInputStream());
		}catch (IOException e1){
			e1.printStackTrace();
			return new BackWarnEntity(false, "上传失败，IO读写错误!");
		}

		HSSFWorkbook importXls = null;
		try{
			importXls = new HSSFWorkbook(cacher.getInputStream());
		}catch (IOException e){
			e.printStackTrace();
			return new BackWarnEntity(false, "上传失败，IO读写错误!");
		}

		HSSFSheet sheet = importXls.getSheetAt(0);
		try{
			List<String> langs = new ArrayList<>();
			if (i18nOnOff){
				// 第一行是语言
				HSSFRow langRow = sheet.getRow(0);
				int index = langRow.getLastCellNum();
				for (int i = 3; i < index; i++){
					String value = getCellValue(langRow.getCell(i));
					if (Validator.isNotNullOrEmpty(value)){
						langs.add(value);
					}else{
						isSuccess = false;
						description = "上传失败，第一列语言不能修改，不能为空!";
						break;
					}
				}
			}

			if (isSuccess){
				List<PropertyValueUploadCommand> propertyValueCommandList = new ArrayList<PropertyValueUploadCommand>();

				int lastRow = sheet.getLastRowNum();
				// 从第四行读起
				for (int rowNum = 3; rowNum <= lastRow; rowNum++){
					HSSFRow row = sheet.getRow(rowNum);
					PropertyValueUploadCommand propertyValueCommand = new PropertyValueUploadCommand();

					String value = getCellValue(row.getCell(0));
					if (Validator.isNotNullOrEmpty(value)){
						propertyValueCommand.setId(Long.valueOf(value));
					}

					value = getCellValue(row.getCell(1));
					if (Validator.isNotNullOrEmpty(value)){
						propertyValueCommand.setValue(value);
					}else{
						isSuccess = false;
						description = "上传失败，第" + rowNum + "列属性值不能为空!";
						break;
					}

					value = getCellValue(row.getCell(2));
					if (Validator.isNotNullOrEmpty(value)){
						propertyValueCommand.setSortNo(Integer.parseInt(value));
					}else{
						isSuccess = false;
						description = "上传失败，第" + rowNum + "列排序不能为空!";
						break;
					}

					if (i18nOnOff){
						Map<String, String> valueLangMap = new HashMap<String, String>();

						int lastCellNum = row.getLastCellNum();
						// 从第四列读起
						for (int cellNum = 3; cellNum < lastCellNum; cellNum++){
							value = getCellValue(row.getCell(cellNum));
							if (Validator.isNotNullOrEmpty(value)){
								valueLangMap.put(langs.get(cellNum - 3), value);
							}else{
								isSuccess = false;
								description = "上传失败，第" + rowNum + "列属性值--" + langs.get(cellNum - 3) + "不能为空!";
								break;
							}
						}

						if (!isSuccess){
							break;
						}
						propertyValueCommand.setValueLangMap(valueLangMap);
					}
					propertyValueCommandList.add(propertyValueCommand);
				}

				if (isSuccess && propertyValueCommandList.size() > 0){
					propertyManager.createOrUpdatePropertyValueByUpload(propertyValueCommandList, propertyId);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return new BackWarnEntity(false, "上传失败，系统异常!");
		}

		BackWarnEntity backWarnEntity = new BackWarnEntity(isSuccess, description);
		return backWarnEntity;
	}

	/**
	 * 获取单元格属性值
	 * 
	 * @return String
	 * @param cell
	 * @author 冯明雷
	 * @time 2016年4月14日下午4:09:16
	 */
	private String getCellValue(HSSFCell cell){
		String cellValue = "";
		DecimalFormat df = new DecimalFormat("#");
		if (cell == null){
			return null;
		}

		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:
				HSSFRichTextString richStringCellValue = cell.getRichStringCellValue();
				if (Validator.isNotNullOrEmpty(richStringCellValue)){
					cellValue = richStringCellValue.getString().trim();
				}
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				double numericCellValue = cell.getNumericCellValue();
				if (Validator.isNotNullOrEmpty(numericCellValue)){
					cellValue = df.format(numericCellValue).toString();
				}
				break;
			default:
				cellValue = "";
		}
		return cellValue;
	}

}
