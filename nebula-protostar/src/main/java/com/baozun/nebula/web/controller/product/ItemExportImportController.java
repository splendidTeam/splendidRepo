package com.baozun.nebula.web.controller.product;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.dao.Sort;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.ItemExportImportManager;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.sdk.manager.system.SysItemOperateLogManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.utils.InputStreamCacher;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

/**
 * 商品的导出和导入
 * 
 * @author chenguang.zhou 2015年5月28日
 */
@Controller
public class ItemExportImportController extends BaseController{

    private static final Logger     log = LoggerFactory.getLogger(ItemExportImportController.class);

    @Autowired
    private ShopManager             shopManager;

    @Autowired
    private ItemExportImportManager itemExportImportManager;

    @Autowired
    private ItemSolrManager         itemSolrManager;

    @Autowired
    private SdkMataInfoManager      sdkMataInfoManager;

    @Autowired
    private PropertyDao             propertyDao;

    /**
     * 转到商品导出和导入页面
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/item/itemExportImport.htm",method = RequestMethod.GET)
    public String itemExprotImport(Model model){
        Sort[] sorts = Sort.parse("id desc");
        // 获取行业信息
        List<Industry> industryList = shopManager.findAllIndustryList(sorts);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isSaleProp", true);
        List<Property> propertyList = propertyDao.findEffectPropertyListByQueryMap(map);

        model.addAttribute("industryList", industryList);
        model.addAttribute("propertyList", propertyList);
        
        String batchThreshold = sdkMataInfoManager.findValue("batch.update.item.threshold");
        if(Validator.isNullOrEmpty(batchThreshold)){
            batchThreshold = "100";
        }
        model.addAttribute("batchThreshold", batchThreshold);
        
        return "product/item/item-export-import";
    }

    /**
     * 获取行业的属性
     * 
     * @param industryId
     * @return
     */
    @RequestMapping(value = "/item/findPropertyByIndustryId.json",method = RequestMethod.POST)
    @ResponseBody
    public BackWarnEntity findPropertyByIndustryId(@RequestParam("industryId") Long industryId){
        BackWarnEntity result = new BackWarnEntity(true, null);
        Sort[] sorts = new Sort[1];
        sorts[0] = new Sort("p.id", "asc");
        List<Property> propertyList = shopManager.findPropertyListByIndustryId(industryId, sorts);

        List<Property> notSalesList = new ArrayList<Property>();

        if (Validator.isNotNullOrEmpty(propertyList)){
            for (Property property : propertyList){
                if (!property.getIsSaleProp()){
                    notSalesList.add(property);
                }
            }
        }

        result.setDescription(propertyList);
        return result;

    }

    /**
     * 商品导出或模板导出
     * 
     * @param industryId
     * @param selectCodes
     * @param itemCodes
     * @param request
     * @param response
     */
    @RequestMapping(value = "/item/itemExport.htm",method = RequestMethod.POST)
    @ResponseBody
    public String itemExport(
            @RequestParam("industryId") Long industryId,
            @RequestParam("selectCodes") String[] selectCodes,
            @RequestParam(value = "itemCodes",required = false) String itemCodes,
            HttpServletRequest request,
            HttpServletResponse response){

        Long shopId = shopManager.getShopId(getUserDetails());
        String path = "excel/tplt-item-export-import.xls";
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());
        if (log.isDebugEnabled()){
            log.debug("selected properties have {}.", Arrays.asList(selectCodes).toString());
            log.debug("export item excel file path is {}.", file.getPath());
        }

        String fileName = file.getName();
        OutputStream outputStream = null;

        // HttpServletResponse Header设置
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);

        try{
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            outputStream = response.getOutputStream();
            HSSFWorkbook xls = itemExportImportManager.itemExport(shopId, industryId, selectCodes, itemCodes, file);
            if (xls != null){
                xls.write(outputStream);
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if (outputStream != null){
                try{
                    outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return "json";
    }

    /**
     * 导入商品
     * 
     * @param industryId
     * @param mFile
     * @return
     */
    @RequestMapping(value = "/item/itemImport.json",method = RequestMethod.POST)
    @ResponseBody
    public BackWarnEntity itemImport(HttpServletRequest request,HttpServletResponse response){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("excelFile");
        BackWarnEntity result = new BackWarnEntity(Boolean.TRUE, "");

        Long shopId = shopManager.getShopId(getUserDetails());
        InputStreamCacher cacher = null;
        try{
            cacher = new InputStreamCacher(file.getInputStream());
            List<Long> itemIdsForSolr = itemExportImportManager.itemImport(cacher.getInputStream(), shopId);
            // 更新商品索引信息
            if (itemIdsForSolr != null && !itemIdsForSolr.isEmpty()){
                if (log.isDebugEnabled()){
                    log.debug("update item solr index item id list is {}", itemIdsForSolr.toString());
                }

                Boolean isSuccess = Boolean.FALSE;
                boolean i18n = LangProperty.getI18nOnOff();
                if (i18n){
                    isSuccess = itemSolrManager.saveOrUpdateItemI18n(itemIdsForSolr);
                }else{
                    isSuccess = itemSolrManager.saveOrUpdateItem(itemIdsForSolr);
                }

                if (!isSuccess){
                    throw new BusinessException(ErrorCodes.SOLR_SETTING_UPDATE_FAIL);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (BusinessException e){
            Boolean flag = Boolean.TRUE;
            List<String> errorMessages = new ArrayList<String>();
            BusinessException linkedException = e;
            while (flag){
                String message = "";
                if (linkedException.getErrorCode() == 0){
                    message = linkedException.getMessage();
                }else{
                    if (linkedException.getArgs() == null){
                        message = getMessage(linkedException.getErrorCode());
                    }else{
                        message = getMessage(linkedException.getErrorCode(), linkedException.getArgs());
                    }

                }
                errorMessages.add(message);
                if (linkedException.getLinkedException() == null){
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
