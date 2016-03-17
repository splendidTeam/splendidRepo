/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Pagination;
import loxia.support.excel.ExcelKit;
import loxia.support.excel.ExcelReader;
import loxia.support.excel.ReadStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.product.ItemColorReference;
import com.baozun.nebula.model.product.ItemColorValueRef;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.ItemColorValueRefManager;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

import net.sf.json.JSONArray;
/**
 * @ClassName: ItemColorValueRefUploading
 * @Description: (商品筛选色颜色对照上传)
 * @author gewei.lu <gewei.lu@baozun.cn>
 * @date 2016年1月4日 下午5:51:41
 * 
 */
@Controller
public class ItemColorRefUploading extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(ItemColorRefUploading.class);
	
	@Autowired
	private ExcelReader ItemColorValueRefReader;
	
	@Autowired
	private ItemColorValueRefManager  ItemColorValueRefManager;

	/** 
	* @Title: ItemColorValueRefSkip 
	* @Description:  (跳转到下载页面) 
	* @param   response
	* @param      设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping(value = "/product/itemColorList.htm", method = RequestMethod.GET)
	public String ItemColorValueRefSkip(HttpServletResponse response) { 
		return "product/item/itemColorValueRef";
	}
	
 
	
	/** 
	* @Title: ItemColorValueRefList 
	* @Description:  (加载数据带条件筛选) 
	* @param   model
	* @param   queryBean
	* @param   request
	* @param   response
	* @param  
	* @param   IOException
	* @param   ParseException    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping(value="/itemColor/itemColorValueRefList.json")
	@ResponseBody
	public Pagination<ItemColorValueRef> promotionEditList(Model model,@QueryBeanParam QueryBean queryBean) {
		Map<String, Object> paraMap = queryBean.getParaMap();
		String  filterColor =(String) paraMap.get("filterColor");
		if(Validator.isNotNullOrEmpty(filterColor)){
			paraMap.put("filterColor", "%"+filterColor+"%");
		}
		paraMap.put("sorted", false);
		ItemColorValueRefManager.itemColorValueReflistMap();
		return ItemColorValueRefManager.findItemColorValueReflist(queryBean.getPage(),queryBean.getSorts(), queryBean.getParaMap());
	}
	
	/**
	 * @Title: downloadTemplate
	 * @Description:  (下载模板)
	 * @param response
	 *            设定文件
	 * @return void 无返回类型
	 * @throws
	 */
	@RequestMapping(value = "/itemColor/itemColorValueRef.xls", method = RequestMethod.GET)
	public void downloadTemplate(HttpServletResponse response) {
		String path = "excel/itemColorValueRef.xlsx";
		try {
			downloadExcel(response, path);
		} catch (BusinessException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}

	/**
	 * @Title: downloadTemplate
	 * @Description:  (下载模板)
	 * @param response
	 *            设定文件
	 * @return void 无返回类型
	 * @throws
	 */
	private void downloadExcel(HttpServletResponse response, String path) {
		File file = new File(Thread.currentThread().getContextClassLoader()
				.getResource(path).getPath());
		response.setHeader("Content-type", "application/force-download");
		response.setHeader("Content-Transfer-Encoding", "Binary");
		response.setHeader("Content-length", "" + file.length());
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Disposition", "inline;filename=\"" + path
				+ "\"");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ file.getName() + "\"");
		try {
			OutputStream os = null;
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				os = response.getOutputStream();
				byte[] buf = new byte[1024];
				while (is.read(buf) != -1) {
					os.write(buf);
				}
			} finally {
				is.close();
				os.close();
			}
		} catch (Exception e) {
			log.info(" 文件读取失败");
		}
	}

	/**
	 * @Title: skuPriceInfoUpload
	 * @Description:  (解析Excel)
	 * @param request
	 * @param model
	 * @param response
	 * @param 设定文件
	 * @return Object 返回类型 Object
	 * @throws
	 */
	@RequestMapping(value = "/itemColor/itemColorValueRefAnalysis.json", method = RequestMethod.POST)
	@ResponseBody
	public Object skuPriceInfoUpload(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		response.setContentType("text/html;charset=UTF-8");
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("Filedata");

		String fileName = file.getOriginalFilename();

		map.put("fileName", fileName);

		List<ItemColorValueRef> ItemColorValueReflist = new ArrayList<ItemColorValueRef>();

		boolean parseExcleFileResult = validateSkuPriceInfoXlsFile(request,
				file, map, ItemColorValueReflist);

		map.put("flag", parseExcleFileResult);

		if (!parseExcleFileResult) {
			map.put("flag", false);
			return map;
		}
		return map;
	}

	/**
	 * @Title: skuPriceInfoUpload
	 * @Description:  (解析Excel)
	 * @param request
	 * @param model
	 * @param response
	 * @param 设定文件
	 * @return Object 返回类型 Object
	 * @throws
	 */
	public boolean validateSkuPriceInfoXlsFile(HttpServletRequest request,
			CommonsMultipartFile file, Map<String, Object> map,
			List<ItemColorValueRef> ItemColorValueReflist) {
		boolean flag = true;
		StringBuffer sbErr = new StringBuffer();
		InputStream is = new ByteArrayInputStream(file.getBytes());
		Map<String, Object> beans = new HashMap<String, Object>();
		 
		beans.put("ItemColorValueReflist", ItemColorValueReflist);

		ReadStatus rs = ItemColorValueRefReader.readSheet(is, 0, beans);

		if (rs.getStatus() != ReadStatus.STATUS_SUCCESS) {
			flag = false;
			List<String> messageList = ExcelKit.getInstance()
					.getReadStatusMessages(rs, Locale.SIMPLIFIED_CHINESE);
			for (String message : messageList) {
				sbErr.append("解析Excel文件出错：" + message + "<br>\n");
			}
			map.put("errMessage", sbErr.toString());
			return flag;
		}
		List<String> messagelist = new ArrayList<String>();
		for (ItemColorValueRef ItemColorValueRef : ItemColorValueReflist) {
			if (Validator.isNullOrEmpty(ItemColorValueRef.getFilterColor())) {
				messagelist.add("校验解析信息出错：筛选色名称筛选色名称为空" + "<br>\n");
				log.debug("校验解析信息出错：筛选色名称筛选色名称为空");
			} else if (Validator.isNullOrEmpty(ItemColorValueRef
					.getFilterColorValue())) {
				messagelist.add("校验解析信息出错：筛选色 色值为空" + "<br>\n");
				log.debug("校验解析信息出错：筛选色 色值为空");
			} else if (Validator.isNullOrEmpty(ItemColorValueRef.getItemColor())) {
				messagelist.add("校验解析信息出错：商品色名称为空" + "<br>\n");
				log.debug("校验解析信息出错：商品色名称为空");
			} else if (Validator.isNullOrEmpty(ItemColorValueRef
					.getItemtColorValue())) {
				messagelist.add("校验解析信息出错：商品色名称为空" + "<br>\n");
				log.debug("校验解析信息出错：商品色名称为空");
			}
			if (!Validator.isNullOrEmpty(ItemColorValueRef)) {
				ItemColorValueRef.setCreateTime(new Date());
				ItemColorValueRefManager.saveColorReflist(ItemColorValueRef);
			}
		}
		map.put("errorList", messagelist);
		flag = true;
		return flag;
	}
	
	
	
	/** 
	* @Title: delectItemColorValueRefList 
	* @Description:  (删除) 
	* @param   model
	* @param   queryBean
	* @param   request
	* @param   response
	* @param @return
	* @param @throws IOException
	* @param @throws ParseException    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("/itemColor/removeItemColorValueById.json")
	@ResponseBody
	public void removeItemColorValueById(Model model, @QueryBeanParam QueryBean queryBean, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		    String id = request.getParameter("id");
		    int ret=0;
		    if (Validator.isNotNullOrEmpty(id)) {
		    	ret = ItemColorValueRefManager.delectColorReflist(Long.valueOf(id));
			}
			response.setCharacterEncoding("UTF-8");
			if (ret==1) {
				response.getWriter().print(1);
			}else{
			    response.getWriter().print(0);
			}
	}
	
	
	/** 
	* @Title: itemcolorcheck 
	* @Description:(检查已选商品颜色在筛选对照表里面是否存在) 
	* @param @param model
	* @param @param request
	* @param @param response
	* @param @throws IOException    设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月13日 下午12:46:04 
	* @author GEWEI.LU   
	*/
	@RequestMapping(value="/itemColor/itemcolorcheck.json")
	@ResponseBody
	public void itemcolorcheck(Model model ,HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<String> checkcolormap=new ArrayList<String>();
		String itemcolor=request.getParameter("itemcolor");
		String temColorReflistMapValue=null;
		Map<String, String> itemColorValueReflistMap=ItemColorValueRefManager.itemColorValueReflistMap();
		String[] itemcolorArr = itemcolor.split(",");
		//对照表是否符合
		for (String itemColorValueReflistMapkey : itemColorValueReflistMap.keySet()) {
			String colormapkey=itemColorValueReflistMapkey;
			for (int i = 0; i < itemcolorArr.length; i++) {
				if(colormapkey.equals(itemcolorArr[i])){
					temColorReflistMapValue=itemColorValueReflistMap.get(colormapkey);
					checkcolormap.add(temColorReflistMapValue);
				}
			}
		}
		JSONArray jsonObj = JSONArray.fromObject(checkcolormap);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(jsonObj);
	}
	

	/** 
	* @Title: checkcolorvalue 
	* @Description:(添加筛选颜色的对按照数据) 
	* @param @param model
	* @param @param request
	* @param @param response
	* @param @throws IOException    设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月13日 上午10:08:37 
	* @author GEWEI.LU   
	*/
	@RequestMapping(value="/itemColor/savecolorvalue.htm")
	@ResponseBody
	public void checkcolorvalue(Model model ,HttpServletRequest request, HttpServletResponse response) throws IOException {
		String itemcolor=request.getParameter("itemcolor");
		String code=request.getParameter("code");
		String[] screencolorvaluecolorArr = itemcolor.split(",");
		Map<String,String> parametermap = new HashMap<String, String>();
		Map<String,Long> paraMap = new HashMap<String, Long>();
		
		Map<String, String> itemColorValueReflistMap=ItemColorValueRefManager.itemColorValueReflistMap();
		for (String itemColorValueReflistMapkey : itemColorValueReflistMap.keySet()) {
			String colormapkey=itemColorValueReflistMapkey;
			for (int i = 0; i < screencolorvaluecolorArr.length; i++) {
				if(colormapkey.equals(screencolorvaluecolorArr[i])){
					String temColorReflistMapValue=itemColorValueReflistMap.get(colormapkey);
					colormapkey=itemColorValueReflistMapkey;
					parametermap.put("code",code);
					parametermap.put("item_color",Constants.ITEM_COLOR);
					parametermap.put("filter_color",Constants.FILTER_COLOR);
					PropertyValue propertyValueref=ItemColorValueRefManager.findItempropertyValue(parametermap,temColorReflistMapValue);
					PropertyValue propertyValue=ItemColorValueRefManager.findItempropertyValue(parametermap,colormapkey);
					if(propertyValueref!=null && propertyValue!=null){
						paraMap.put("syscolorid", propertyValueref.getId());
						paraMap.put("itemcolorid", propertyValue.getId());
						List<ItemColorReference> itemColorReferencelist=ItemColorValueRefManager.findItItemColorReferenceList(paraMap);
						if(itemColorReferencelist.size()<=0){
							ItemColorReference  itemColorReference = new ItemColorReference();
							itemColorReference.setColorId(propertyValueref.getId());
							itemColorReference.setItemColorId(propertyValue.getId());
							ItemColorValueRefManager.saveItemColorValueRef(itemColorReference);
						}
					}
				}
			}
		}
	}
}
