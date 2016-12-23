package com.baozun.nebula.web.controller.product;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.ItemExportImportManager;
import com.baozun.nebula.manager.product.SkuExportImportManager;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.utils.InputStreamCacher;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品SKU的导出和导入
 * 
 * @author chenguang.zhou 2015年5月28日
 */
@Controller
public class SkuExportImportController  extends BaseController {
	private static final Logger		log	= LoggerFactory.getLogger(SkuExportImportController.class);

	@Autowired
	private ShopManager				shopManager;

	@Autowired
	private SkuExportImportManager	skuExportImportManager;

	@Autowired
	private ItemSolrManager			itemSolrManager;
	
	@Autowired
	private ItemExportImportManager	itemExportImportManager;
	
	
	
	/**
	 * 商品导出或模板导出
	 * 
	 * @param industryId
	 * @param selectCodes
	 * @param itemCodes
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sku/skuExport.htm", method = RequestMethod.POST)
	@ResponseBody
	public String itemExport(@RequestParam("skuIndustryId") Long industryId,
			@RequestParam("selectCodes") String[] selectCodes,
			@RequestParam(value = "itemCodes", required = false) String itemCodes,
			HttpServletRequest request, HttpServletResponse response) {
		
		Long shopId = shopManager.getShopId(getUserDetails());
		String path = "excel/tplt-sku-export-import.xls";
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
		if (log.isDebugEnabled()) {
			log.debug("selected properties have {}.", Arrays.asList(selectCodes).toString());
			log.debug("export item excel file path is {}.", file.getPath());
		}

		String fileName = file.getName();
		OutputStream outputStream = null;

		// HttpServletResponse Header设置
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
		
		try {
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
			outputStream = response.getOutputStream();
			HSSFWorkbook xls = skuExportImportManager.itemExport(shopId, industryId, selectCodes, itemCodes, file);
			if(xls != null){
				xls.write(outputStream);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(outputStream != null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return "json";
	}
	
	
	/**
	 * 导入商品
	 * @param industryId
	 * @param mFile
	 * @return
	 */
	@RequestMapping(value = "/sku/skuImport.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity itemImport(HttpServletRequest request, HttpServletResponse response){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("skuExcelFile");
		BackWarnEntity result = new BackWarnEntity(Boolean.TRUE, "");
		
		Long shopId = shopManager.getShopId(getUserDetails());
		InputStreamCacher cacher = null;
		try {
			boolean i18n = LangProperty.getI18nOnOff();
			if(i18n){
				cacher = new InputStreamCacher(file.getInputStream());
				skuExportImportManager.itemImport(cacher.getInputStream(), shopId);
			}else{
				throw new BusinessException(ErrorCodes.IMPORT_SKU_IL8N_CLOSE);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BusinessException e){
			Boolean flag = Boolean.TRUE;
			List<String> errorMessages = new ArrayList<String>();
			BusinessException linkedException = e;
			while(flag){
				String message = "";
				if(linkedException.getErrorCode() == 0){
					message = linkedException.getMessage();
				}else{
					if(linkedException.getArgs() == null){
						message = getMessage(linkedException.getErrorCode());
					}else{
						message = getMessage(linkedException.getErrorCode(), linkedException.getArgs());
					}
					
				}
				errorMessages.add(message);
				if(linkedException.getLinkedException() == null){
					flag = Boolean.FALSE;
				}else{
					linkedException = linkedException.getLinkedException();
				}
			}
			//String userFilekey = addErrorInfo(errorMessages, cacher, response, name);
			//返回信息
			result.setIsSuccess(Boolean.FALSE);
			result.setDescription(errorMessages.toString());
			//rs.put("userFilekey", userFilekey);
		}
		return result;
	}
}
