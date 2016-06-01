/**
"type:" * Copyright (c) 2012 Baozun All Rights Reserved.

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemPresalseInfoCommand;
import com.baozun.nebula.command.ItemPropertyCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.SkuPropertyCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.ItemInfoCommand;
import com.baozun.nebula.command.product.ItemPropertiesCommand;
import com.baozun.nebula.command.product.ItemSortScoreCommand;
import com.baozun.nebula.command.promotion.ItemPropertyMutlLangCommand;
import com.baozun.nebula.command.promotion.SkuPropertyMUtlLangCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.manager.product.ItemLangManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.manager.product.ItemPresaleInfoManager;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemProValGroupRelation;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.ItemSortScore;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.ShopProperty;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.manager.SdkItemSortScoreManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.InputStreamCacher;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.google.gson.Gson;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.support.json.JSONArray;

/**
 * 鍟嗗搧绠＄悊controller
 * 
 * @author xingyu.liu
 */
@Controller
public class ItemController extends BaseController{

	private static final Logger					log					= LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private IndustryManager						industryManager;

	@Autowired
	private CategoryManager						categoryManager;

	@Autowired
	private ItemManager							itemManager;

	@Autowired
	private ShopManager							shopManager;

	@Autowired
	private ItemCategoryManager					itemCategoryManager;

	@Autowired
	private ChooseOptionManager					chooseOptionManager;

	@Autowired
	private PropertyManager						propertyManager;

	@Autowired
	private SdkMataInfoManager					sdkMataInfoManager;

	// 榛樿鎺掑簭
	@Autowired
	private SdkItemSortScoreManager				sdkItemSortScoreManager;

	@Autowired
	private ItemSolrManager						itemSolrManager;

	@Autowired
	private SdkI18nLangManager					sdkI18nLangManager;

	@Autowired
	private MessageSource						messageSource;

	@Autowired
	private ItemPresaleInfoManager				itemPresaleInfoManager;

	// 缂╃暐鍥捐鏍�
	// private static final String THUMBNAIL_CONFIG = "THUMBNAIL_CONFIG";

	// private static final Integer MULTI_CHOICE = 4;
	//
	// private static final Integer CUSTOM_MULTI_CHOICE = 5;

	/**
	 * 涓婁紶鍥剧墖鐨勫煙鍚�
	 */
	@Value("#{meta['upload.img.domain.base']}")
	private String								UPLOAD_IMG_DOMAIN	= "";

	private static Map<String, HSSFWorkbook>	userExcelFile		= new ConcurrentHashMap<String, HSSFWorkbook>();

	@Autowired
	private ItemLangManager						itemLangManager;

	/**
	 * 椤甸潰璺宠浆 鏂板鍟嗗搧
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/item/createItem.htm")
	public String createItem(Model model){
		// 鏌ヨorgId
//		UserDetails userDetails = this.getUserDetails();
//
//		ShopCommand shopCommand = null;
//		Long shopId = 0L;
//
//		Long currentOrgId = userDetails.getCurrentOrganizationId();
//		// 鏍规嵁orgId鏌ヨshopId
//		if (currentOrgId != null){
//			shopCommand = shopManager.findShopByOrgId(currentOrgId);
//			shopId = shopCommand.getShopid();
//		}

		Sort[] sorts = Sort.parse("id desc");
		// 鑾峰彇琛屼笟淇℃伅
//		List<Map<String, Object>> industryList = processIndusgtryList(shopManager.findAllIndustryList(sorts), shopId);
		List<Map<String, Object>> industryList = processIndusgtryList(shopManager.findAllIndustryList(sorts));
		model.addAttribute("industryList", industryList);
		// 鍒嗙被鍒楄〃
		sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		// 缂╃暐鍥捐鏍�
		// List<ChooseOption> thumbnailConfig = chooseOptionManager.findEffectChooseOptionListByGroupCode(THUMBNAIL_CONFIG);
		// model.addAttribute("thumbnailConfig", thumbnailConfig);
		String itemCodeValidMsg = messageSource.getMessage(
				ErrorCodes.BUSINESS_EXCEPTION_PREFIX + ErrorCodes.ITEM_CODE_VALID_ERROR,
				new Object[] {},
				Locale.SIMPLIFIED_CHINESE);
		model.addAttribute("itemCodeValidMsg", itemCodeValidMsg);
		String pdValidCode = sdkMataInfoManager.findValue(MataInfo.PD_VALID_CODE);
		model.addAttribute("pdValidCode", pdValidCode);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("isStyleEnable", isEnableStyle());
		return "/product/item/add-item";
	}

	private boolean isEnableStyle(){
		String styleValue = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_STYLE);

		boolean isStyleEnable = false;
		if (styleValue != null){
			isStyleEnable = new Boolean(styleValue);
		}

		return isStyleEnable;
	}

	/**
	 * 瀵归〉闈㈢殑鏁版嵁鑺傜偣杩涜鍒ゆ柇瀛樺偍
	 * 
	 * @param industryList
	 * @return
	 */
	private List<Map<String, Object>> processIndusgtryList(List<Industry> industryList){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
//		List<ShopProperty> shopPropertyList = new ArrayList<ShopProperty>();
//		if (shopId != null){
//			shopPropertyList = shopManager.findShopPropertyByshopId(shopId);
//		}
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
			
			map.put("isShow", true);

//			if (shopPropertyList != null){
//				for (int i = 0; i < shopPropertyList.size(); i++){
//					if (indu.getId().equals(shopPropertyList.get(i).getIndustryId())){
//						map.put("checked", "true");
//						// map.put("open", true);
//						break;
//					}
//				}
//			}
			resultList.add(map);
		}
//		if (shopPropertyList != null){
//			for (int i = 0; i < shopPropertyList.size(); i++){
//				for (Map<String, Object> map : resultList){
//					String industryId = shopPropertyList.get(i).getIndustryId().toString();
//					String mapId = map.get("id").toString();
//					if (industryId.equals(mapId)){
//						searchChecked(resultList, shopPropertyList.get(i).getIndustryId().toString());
//					}
//
//				}
//
//			}
//		}

		return resultList;
	}

	// 閫掑綊鐢ㄤ簬绛涢�塩hecked
//	static void searchChecked(List<Map<String, Object>> resultList,String id){
//		for (Map<String, Object> map : resultList){
//			if (map.get("id").toString().equals(id)){
//				map.put("isShow", true);
//				if (!map.get("pId").toString().equals("0")){
//					searchChecked(resultList, map.get("pId").toString());
//				}
//			}
//
//		}
//	}

	/**
	 * 涓嬩竴姝ユ寜閽�
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/item/findDynamicPropertis.json")
	@ResponseBody
	public Object findDynamicPropertis(Model model,@RequestParam("industryId") Long industryId){
		// 鏍规嵁琛屼笟Id鏌ユ壘瀵瑰簲灞炴�у拰灞炴�у��
		List<DynamicPropertyCommand> dynamicPropertyCommandList =itemManager.findDynamicPropertisByIndustryId(industryId);
		SUCCESS.setDescription(dynamicPropertyCommandList);
		return SUCCESS;
	}

	@RequestMapping("/item/findDynamicPropertisJson.json")
	@ResponseBody
	public Object findDynamicPropertisJson(Model model,@RequestParam("industryId") Long industryId){
		// 鏍规嵁琛屼笟Id鏌ユ壘瀵瑰簲灞炴�у拰灞炴�у��
		List<DynamicPropertyCommand> dynamicPropertyCommandList = itemManager.findDynamicPropertisByIndustryId(industryId);
		JSONArray dynamicPropertyCommandListJson = new JSONArray(dynamicPropertyCommandList, "***");
		String dynamicPropertyCommandListJsonStr = dynamicPropertyCommandListJson.toString();
		SUCCESS.setDescription(dynamicPropertyCommandListJsonStr);
		return SUCCESS;
	}

	
	@RequestMapping("/item/findProGroupInfo.json")
	@ResponseBody
	public Object findProGroupInfo(Model model,@RequestParam("proGroupId") Long proGroupId,@RequestParam("propertyId") Long propertyId){
		// 閫氳繃灞炴�у�煎垎缁処D鎵惧埌鐩稿鐨勫睘鎬у�煎垪琛�
		DynamicPropertyCommand dynamicPropertyCommand = propertyManager.findByProGroupIdAndPropertyId(proGroupId,propertyId);
		SUCCESS.setDescription(dynamicPropertyCommand);
		return SUCCESS;
	}
	
	
//	public List<DynamicPropertyCommand> findDynamicPropertisByShopIdAndIndustryId(Long industryId){
//		// 鏌ヨorgId
//		UserDetails userDetails = this.getUserDetails();
//
//		ShopCommand shopCommand = null;
//		Long shopId = 0L;
//
//		Long currentOrgId = userDetails.getCurrentOrganizationId();
//		// 鏍规嵁orgId鏌ヨshopId
//		if (currentOrgId != null){
//			shopCommand = shopManager.findShopByOrgId(currentOrgId);
//			shopId = shopCommand.getShopid();
//		}
//		// 鏍规嵁琛屼笟Id鍜屽簵閾篒d鏌ユ壘瀵瑰簲灞炴�у拰灞炴�у��
//		List<DynamicPropertyCommand> dynamicPropertyCommandList = itemManager.findDynamicPropertisByIndustryId(industryId);
//		return dynamicPropertyCommandList;
//	}

	@RequestMapping("/item/saveItem.json")
	@ResponseBody
	public Object saveItem(@ModelAttribute() ItemCommand itemCommand,@ArrayCommand(dataBind = true) Long[] propertyValueIds, // 鍟嗗搧鍔ㄦ�佸睘鎬�
			@ArrayCommand(dataBind = true) Long[] categoriesIds,// 鍟嗗搧鍒嗙被Id
			@ArrayCommand() ItemProperties[] iProperties,// 鏅�氬晢鍝佸睘鎬�
			@ArrayCommand(dataBind = true) Long[] propertyIds,// 鐢ㄦ埛濉啓鐨勫晢鍝佸睘鎬у�肩殑灞炴�d
			@ArrayCommand(dataBind = true) String[] propertyValueInputs,// 鐢ㄦ埛杈撳叆鐨� 鍟嗗搧閿�鍞睘鎬х殑 灞炴�у�� 锛堝浜庡閫夋潵璇� 鏄� pvId,pvId 瀵逛簬鑷畾涔夊閫夋潵璇存槸 aa||bb锛�
			HttpServletRequest request) throws Exception{

		// 鏌ヨorgId
		UserDetails userDetails = this.getUserDetails();
		ShopCommand shopCommand = null;
		Long shopId = 0L;
		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 鏍规嵁orgId鏌ヨshopId
		if (currentOrgId != null){
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();
		}

		itemCommand.setShopId(shopId);
		// 灏嗕紶杩囨潵鐨勪笂浼犲浘鐗囦腑 鏄笂浼犵殑鍥剧墖鏇挎崲涓轰笉鍚煙鍚嶇殑鍥剧墖
		itemCommand.setDescription(removeDefinedDomainInDesc(itemCommand.getDescription(), UPLOAD_IMG_DOMAIN));

		SkuPropertyCommand[] skuPropertyCommandArray = getCmdArrrayFromRequest(request, propertyIds, propertyValueInputs);

//		List<ItemProValGroupRelation> groupRelation = getItemProValueGroupRelation(request,propertyIds);
		
		// 淇濆瓨鍟嗗搧
		Item item = itemManager.createOrUpdateItem(itemCommand, propertyValueIds, categoriesIds, iProperties, skuPropertyCommandArray);

		if (item.getLifecycle().equals(Item.LIFECYCLE_ENABLE)){
			List<Long> itemIdsForSolr = new ArrayList<Long>();
			itemIdsForSolr.add(item.getId());
			itemSolrManager.saveOrUpdateItem(itemIdsForSolr);
		}

		BackWarnEntity backWarnEntity = new BackWarnEntity(true, null);
		backWarnEntity.setErrorCode(item.getId().intValue());
		return backWarnEntity;
	}

	/**
	 * @author 浣曟尝
	 * @Description: 澶勭悊鍟嗗搧灞炴�у拰鍟嗗搧鍥介檯鍖栦俊鎭�
	 * @param itemCommand
	 * @param propertyValueIds
	 * @param categoriesIds
	 * @param iProperties
	 * @param propertyIds
	 * @param propertyValueInputs
	 * @param request
	 * @return
	 * @throws Exception
	 *             Object
	 * @throws
	 */
	@RequestMapping("/i18n/item/saveItem.json")
	@ResponseBody
	public Object saveItemI18n(@I18nCommand ItemInfoCommand itemCommand,@ArrayCommand(dataBind = true) Long[] propertyValueIds, // 鍟嗗搧鍔ㄦ�佸睘鎬�
			@ArrayCommand(dataBind = true) Long[] categoriesIds,// 鍟嗗搧鍒嗙被Id
			@I18nCommand ItemPropertiesCommand[] iProperties,// 鏅�氬晢鍝佸睘鎬�
			@ArrayCommand(dataBind = true) Long[] propertyIds,// 鐢ㄦ埛濉啓鐨勫晢鍝佸睘鎬у�肩殑灞炴�d
			@ArrayCommand(dataBind = true) String[] propertyValueInputs,// 鐢ㄦ埛杈撳叆鐨� 鍟嗗搧閿�鍞睘鎬х殑 灞炴�у�� 锛堝浜庡閫夋潵璇� 鏄� pvId,pvId 瀵逛簬鑷畾涔夊閫夋潵璇存槸 aa||bb锛�-鑷畾涔夊閫�
			@ArrayCommand(dataBind = true) String[] propertyValueInputIds,// --澶氶��
			HttpServletRequest request) throws Exception{
		// 鏌ヨorgId
		UserDetails userDetails = this.getUserDetails();
		ShopCommand shopCommand = null;
		Long shopId = 0L;
		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 鏍规嵁orgId鏌ヨshopId
		if (currentOrgId != null){
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();
		}

		itemCommand.setShopId(shopId);
		// 灏嗕紶杩囨潵鐨勪笂浼犲浘鐗囦腑 鏄笂浼犵殑鍥剧墖鏇挎崲涓轰笉鍚煙鍚嶇殑鍥剧墖
		dealDescImgUrl(itemCommand);
		SkuPropertyMUtlLangCommand[] skuPropertyCommandArray = getCmdArrrayFromRequestI18n(
				request,
				propertyIds,
				propertyValueInputs,
				propertyValueInputIds);
//		List<ItemProValGroupRelation> groupRelation = getItemProValueGroupRelation(request,propertyIds);
		// 淇濆瓨鍟嗗搧
		Item item = itemLangManager.createOrUpdateItem(itemCommand, propertyValueIds, categoriesIds, iProperties, skuPropertyCommandArray);

		if (item.getLifecycle().equals(Item.LIFECYCLE_ENABLE)){
			List<Long> itemIdsForSolr = new ArrayList<Long>();
			itemIdsForSolr.add(item.getId());
			boolean i18n = LangProperty.getI18nOnOff();
			if (i18n){
				itemSolrManager.saveOrUpdateItemI18n(itemIdsForSolr);
			}else{
				itemSolrManager.saveOrUpdateItem(itemIdsForSolr);
			}
		}

		BackWarnEntity backWarnEntity = new BackWarnEntity(true, null);
		backWarnEntity.setErrorCode(item.getId().intValue());
		return backWarnEntity;
	}

	/**
	 * @author 浣曟尝
	 * @Description: 澶勭悊鎻忚堪涓緭鍏ョ殑鍥剧墖閾炬帴
	 * @param itemCommand
	 *            void
	 * @throws
	 */
	private void dealDescImgUrl(ItemInfoCommand itemCommand){
		LangProperty langPropertyDesc = itemCommand.getDescription();
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang mutlLang = (MutlLang) langPropertyDesc;
			String[] values = mutlLang.getValues();
			if (values != null && values.length > 0){
				String[] newValues = new String[values.length];
				for (int i = 0; i < values.length; i++){
					String val = removeDefinedDomainInDesc(values[i], UPLOAD_IMG_DOMAIN);
					newValues[i] = val;
				}
				mutlLang.setValues(newValues);
			}
			itemCommand.setDescription(mutlLang);
		}else{
			SingleLang singleLang = (SingleLang) langPropertyDesc;
			String desc = singleLang.getValue();
			desc = removeDefinedDomainInDesc(desc, UPLOAD_IMG_DOMAIN);
			singleLang.setValue(desc);
			itemCommand.setDescription(singleLang);
		}
	}

	private SkuPropertyCommand[] getCmdArrrayFromRequest(HttpServletRequest request,Long[] propertyIds,String[] propertyValueInputs){
		List<SkuPropertyCommand> cmdList = new ArrayList<SkuPropertyCommand>();

		// 濡傛灉 propertyIds锛宲ropertyValueInputs 涓嶅悓鏃朵负null 鎴栬�呬簩鑰呴暱搴︿笉绛� 锛岃鏄庢暟鎹笉涓�鑷�
		if ((propertyIds != null && propertyValueInputs != null) && (propertyIds.length != propertyValueInputs.length)){
			return null;
		}
		// 璇存槑鏄病鏈夐攢鍞睘鎬х殑 鍙湁涓�涓猻ku
		if (propertyValueInputs == null || propertyValueInputs.length == 0){
			SkuPropertyCommand spc = new SkuPropertyCommand();

			String idKey = "skuId";
			String codeKey = "skuCode";
			String spKey = "skuSalePrice";
			String lpKey = "skuListPrice";

			String code = request.getParameter(codeKey);
			BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
			String listPriceStr = request.getParameter(lpKey);
			BigDecimal listPrice = getPriceFromStr(listPriceStr);
			Long skuId = getSkuIdFromStr(request.getParameter(idKey));

			spc.setId(skuId);
			spc.setCode(code);
			spc.setListPrice(listPrice);
			spc.setSalePrice(salePrice);
			spc.setPropertyList(new ArrayList<ItemPropertyCommand>());

			cmdList.add(spc);

			SkuPropertyCommand[] cmdArray = new SkuPropertyCommand[cmdList.size()];
			return cmdList.toArray(cmdArray);
		}else{// propertyManager
			if (propertyValueInputs.length == 1){
				Property p = propertyManager.findPropertiesById(propertyIds[0]);
				String[] inputArray = getInputArrayByProperty(p, propertyValueInputs[0]);
				for (String input : inputArray){

					String codeKey = input + "_code";
					String spKey = input + "_salePrice";
					String lpKey = input + "_listPrice";
					String idKey = input + "_id";
					Long skuId = getSkuIdFromStr(request.getParameter(idKey));

					String code = request.getParameter(codeKey);

					// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
					if (code == null || "".equals(code.trim())){
						continue;
					}

					SkuPropertyCommand spc = new SkuPropertyCommand();

					List<ItemPropertyCommand> ipcList = new ArrayList<ItemPropertyCommand>();

					ItemPropertyCommand ipc = getItemPropertyCommand(p, input);
					ipcList.add(ipc);

					spc.setId(skuId);
					// String code = request.getParameter(codeKey);
					BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
					String listPriceStr = request.getParameter(lpKey);
					BigDecimal listPrice = getPriceFromStr(listPriceStr);

					spc.setCode(code);
					spc.setListPrice(listPrice);
					spc.setSalePrice(salePrice);
					spc.setPropertyList(ipcList);

					cmdList.add(spc);

				}

				SkuPropertyCommand[] cmdArray = new SkuPropertyCommand[cmdList.size()];
				return cmdList.toArray(cmdArray);
			}

			if (propertyValueInputs.length == 2){
				Property p1 = propertyManager.findPropertiesById(propertyIds[0]);
				Property p2 = propertyManager.findPropertiesById(propertyIds[1]);

				String[] inputArray1 = getInputArrayByProperty(p1, propertyValueInputs[0]);
				String[] inputArray2 = getInputArrayByProperty(p2, propertyValueInputs[1]);

				for (String input1 : inputArray1){
					for (String input2 : inputArray2){

						String prefix = input1 + "_" + input2 + "_";
						String codeKey = prefix + "code";
						String spKey = prefix + "salePrice";
						String lpKey = prefix + "listPrice";
						String idKey = prefix + "id";
						Long skuId = getSkuIdFromStr(request.getParameter(idKey));

						String code = request.getParameter(codeKey);

						if (code == null || "".equals(code.trim())){// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
							continue;
						}

						SkuPropertyCommand spc = new SkuPropertyCommand();

						List<ItemPropertyCommand> ipcList = new ArrayList<ItemPropertyCommand>();

						ItemPropertyCommand ipc1 = getItemPropertyCommand(p1, input1);
						ItemPropertyCommand ipc2 = getItemPropertyCommand(p2, input2);
						ipcList.add(ipc1);
						ipcList.add(ipc2);

						BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
						String listPriceStr = request.getParameter(lpKey);
						if (StringUtil.isBlank(listPriceStr)){
							listPriceStr = null;
						}
						BigDecimal listPrice = this.getPriceFromStr(listPriceStr);

						spc.setId(skuId);
						spc.setCode(code);
						spc.setListPrice(listPrice);
						spc.setSalePrice(salePrice);
						spc.setPropertyList(ipcList);

						cmdList.add(spc);
					}
				}
				SkuPropertyCommand[] cmdArray = new SkuPropertyCommand[cmdList.size()];
				return cmdList.toArray(cmdArray);
			}
		}
		return null;
	}

	private SkuPropertyMUtlLangCommand[] getCmdArrrayFromRequestI18n(
			HttpServletRequest request,
			Long[] propertyIds,
			String[] propertyValueInputs,
			String[] propertyValueInputIds){
		List<SkuPropertyMUtlLangCommand> cmdList = new ArrayList<SkuPropertyMUtlLangCommand>();
		// 璇存槑鏄病鏈夐攢鍞睘鎬х殑 鍙湁涓�涓猻ku
		if (Validator.isNullOrEmpty(propertyValueInputs) && Validator.isNullOrEmpty(propertyValueInputIds)){
			SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();

			String idKey = "skuId";
			String codeKey = "skuCode";
			String spKey = "skuSalePrice";
			String lpKey = "skuListPrice";

			String code = request.getParameter(codeKey);
			BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
			String listPriceStr = request.getParameter(lpKey);
			BigDecimal listPrice = getPriceFromStr(listPriceStr);
			Long skuId = getSkuIdFromStr(request.getParameter(idKey));

			spc.setId(skuId);
			spc.setCode(code);
			spc.setListPrice(listPrice);
			spc.setSalePrice(salePrice);
			spc.setPropertyList(new ArrayList<ItemPropertyMutlLangCommand>());

			cmdList.add(spc);

			SkuPropertyMUtlLangCommand[] cmdArray = new SkuPropertyMUtlLangCommand[cmdList.size()];
			return cmdList.toArray(cmdArray);
		}else{
			if (LangProperty.getI18nOnOff()){
				return extractiConvertSkuByI18nSwitchOn(request, propertyIds, propertyValueInputs, propertyValueInputIds, cmdList);
			}else{
				return extractiConvertSkuByI18nSwitchOff(request, propertyIds, propertyValueInputs, propertyValueInputIds, cmdList);
			}

		}
	}

	private SkuPropertyMUtlLangCommand[] extractiConvertSkuByI18nSwitchOn(
			HttpServletRequest request,
			Long[] propertyIds,
			String[] propertyValueInputs,
			String[] propertyValueInputIds,
			List<SkuPropertyMUtlLangCommand> cmdList){
		int langSize = 0;
		List<I18nLang> i18nLangList = sdkI18nLangManager.geti18nLangCache();
		langSize = Validator.isNotNullOrEmpty(i18nLangList) ? i18nLangList.size() : 0;

		/*
		 * if(langSize==0){ throw Exception(""); }
		 */

		// 浠呬竴涓嚜瀹氫箟澶氶�夋垨澶氶��
		if ((propertyValueInputIds.length == 1 && Validator.isNullOrEmpty(propertyValueInputs))
				|| (propertyValueInputs.length == 2 * langSize && Validator.isNullOrEmpty(propertyValueInputIds))){
			Property p = propertyManager.findPropertiesById(propertyIds[0]);
			String newInputs = null;
			if (Validator.isNullOrEmpty(propertyValueInputs)){
				newInputs = propertyValueInputIds[0];
			}else{
				for (int i = 0; i < 2 * langSize; i++){
					if (propertyValueInputs[i].equals(LangUtil.getCurrentLang())){
						newInputs = propertyValueInputs[i - 1];
						break;
					}
				}
			}
			String[] inputArray = getInputArrayByProperty(p, newInputs);
			int num = 0;
			for (String input : inputArray){

				String codeKey = input + "_code";
				String spKey = input + "_salePrice";
				String lpKey = input + "_listPrice";
				String idKey = input + "_id";
				Long skuId = getSkuIdFromStr(request.getParameter(idKey));

				String code = request.getParameter(codeKey);

				// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
				if (code == null || "".equals(code.trim())){
					continue;
				}
				String[] values = getValues(propertyValueInputs);
				String[] langs = getLangs(propertyValueInputs);
				SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();

				List<ItemPropertyMutlLangCommand> ipcList = new ArrayList<ItemPropertyMutlLangCommand>();

				ItemPropertyMutlLangCommand ipc = getItemPropertyMutlLangCommand(p, input, values, langs, num);
				ipcList.add(ipc);

				spc.setId(skuId);
				// String code = request.getParameter(codeKey);
				BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
				String listPriceStr = request.getParameter(lpKey);
				BigDecimal listPrice = getPriceFromStr(listPriceStr);

				spc.setCode(code);
				spc.setListPrice(listPrice);
				spc.setSalePrice(salePrice);
				spc.setPropertyList(ipcList);

				cmdList.add(spc);
				num++;
			}

		}else if ((propertyValueInputIds.length >= 2 && Validator.isNullOrEmpty(propertyValueInputs))
				|| (propertyValueInputs.length >= 4 * langSize && Validator.isNullOrEmpty(propertyValueInputIds))){
			// 涓や釜鑷畾涔夊閫夋垨涓や釜澶氶��
			if (propertyValueInputs.length >= 4 * langSize && Validator.isNullOrEmpty(propertyValueInputIds)){
				// 涓や釜鑷畾涔夊閫�
				// 鎸夊師鏈夐『搴�
				Set<Long> set = new LinkedHashSet<Long>();
				for (Long l : propertyIds){
					set.add(l);
				}
				List<Long> newPropertyIds = new ArrayList<Long>(set);

				List<Property> properties = new ArrayList<Property>();
				Property p1 = propertyManager.findPropertiesById(newPropertyIds.get(0));
				Property p2 = propertyManager.findPropertiesById(newPropertyIds.get(1));
				properties.add(p1);
				properties.add(p2);

				String[] newPropertyValueInputs1 = new String[propertyValueInputs.length / 2];
				String[] newPropertyValueInputs2 = new String[propertyValueInputs.length / 2];
				int np = 0, nq = 0;
				for (int i = 0; i < propertyValueInputs.length; i++){
					if (i % 4 == 0 || (i - 1) % 4 == 0){
						newPropertyValueInputs1[np] = propertyValueInputs[i];
						np++;
					}else{
						newPropertyValueInputs2[nq] = propertyValueInputs[i];
						nq++;
					}
				}

				String[] values1 = getValues(newPropertyValueInputs1);
				String[] langs1 = getLangs(newPropertyValueInputs1);
				String[] values2 = getValues(newPropertyValueInputs2);
				String[] langs2 = getLangs(newPropertyValueInputs2);

				String[] inputArray1 = null;
				String[] inputArray2 = null;

				// 榛樿璇█涓嬫爣
				int dp1 = 0;
				int dp2 = 2;

				for (int i = 0; i < 2 * langSize; i++){
					if (newPropertyValueInputs1[i].equals(LangUtil.getCurrentLang())){
						dp1 = i - 1;
						break;
					}
				}
				for (int i = 0; i < 2 * langSize; i++){
					if (newPropertyValueInputs2[i].equals(LangUtil.getCurrentLang())){
						dp2 = i - 1;
						break;
					}
				}

				inputArray1 = getInputArrayByProperty(p1, newPropertyValueInputs1[dp1]);
				inputArray2 = getInputArrayByProperty(p2, newPropertyValueInputs2[dp2]);

				int outerNum = 0;
				int innerNum = 0;
				for (String input1 : inputArray1){
					innerNum = 0;
					for (String input2 : inputArray2){

						String prefix = input1 + "_" + input2 + "_";
						String codeKey = prefix + "code";
						String spKey = prefix + "salePrice";
						String lpKey = prefix + "listPrice";
						String idKey = prefix + "id";
						Long skuId = getSkuIdFromStr(request.getParameter(idKey));

						String code = request.getParameter(codeKey);

						if (code == null || "".equals(code.trim())){// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
							continue;
						}

						SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();
						List<ItemPropertyMutlLangCommand> ipcList = new ArrayList<ItemPropertyMutlLangCommand>();
						if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p1.getEditingType())){
							ItemPropertyMutlLangCommand ipc1 = getItemPropertyMutlLangCommand(p1, input1, values1, langs1, outerNum);
							ipcList.add(ipc1);
						}
						if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p2.getEditingType())){
							ItemPropertyMutlLangCommand ipc2 = getItemPropertyMutlLangCommand(p2, input2, values2, langs2, innerNum);
							ipcList.add(ipc2);
						}

						BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
						String listPriceStr = request.getParameter(lpKey);
						if (StringUtil.isBlank(listPriceStr)){
							listPriceStr = null;
						}
						BigDecimal listPrice = this.getPriceFromStr(listPriceStr);

						spc.setId(skuId);
						spc.setCode(code);
						spc.setListPrice(listPrice);
						spc.setSalePrice(salePrice);
						spc.setPropertyList(ipcList);
						cmdList.add(spc);
						innerNum++;
					}
					outerNum++;
				}

			}else{
				// 涓変釜鍙婂叾浠ヤ笂閿�鍞睘鎬�
				List<Property> properties = new ArrayList<Property>();
				Map<String, Property> propInputMap = new HashMap<String, Property>();
				List<List<String>> list = new ArrayList<List<String>>();
				for (int i = 0; i < propertyIds.length; i++){
					// 鏍规嵁id鑾峰彇鍟嗗搧灞炴��
					Property prop = propertyManager.findPropertiesById(propertyIds[i]);
					properties.add(prop);
					// 灏哄 [624,546] 棰滆壊[847,1426] 瀛ｈ妭[27,28]
					String[] inputArray = getInputArrayByProperty(prop, propertyValueInputIds[i]);
					List<String> keyIds = Arrays.asList(inputArray);
					list.add(keyIds);
					if (!keyIds.isEmpty()){
						for (String key : keyIds){
							// [624,灏哄] [546,灏哄]
							propInputMap.put(key, prop);
						}
					}
				}

				int outerNum = 0;

				List<List<String>> result = buildTable(null, list, 0);

				for (List<String> rList : result){
					String prefix = "";
					List<ItemPropertyMutlLangCommand> ipcList = new ArrayList<ItemPropertyMutlLangCommand>();
					// ====绗�1琛宺List: [624, 847]
					// ====绗�2琛宺List: [546, 847]
					for (String r : rList){
						prefix += r + "_";
						Property prop = propInputMap.get(r);
						if (Property.EDITING_TYPE_MULTI_SELECT.equals(prop.getEditingType())){
							ItemPropertyMutlLangCommand ipc1 = getItemPropertyMutlLangCommand(prop, r, null, null, outerNum);
							ipcList.add(ipc1);
						}
					}
					// 鎷兼帴key
					String codeKey = prefix + "code";
					String spKey = prefix + "salePrice";
					String lpKey = prefix + "listPrice";
					String idKey = prefix + "id";
					Long skuId = getSkuIdFromStr(request.getParameter(idKey));

					String code = request.getParameter(codeKey);

					if (code == null || "".equals(code.trim())){// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
						continue;
					}

					SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();

					BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
					String listPriceStr = request.getParameter(lpKey);
					if (StringUtil.isBlank(listPriceStr)){
						listPriceStr = null;
					}
					BigDecimal listPrice = this.getPriceFromStr(listPriceStr);

					spc.setId(skuId);
					spc.setCode(code);
					spc.setListPrice(listPrice);
					spc.setSalePrice(salePrice);
					spc.setPropertyList(ipcList);
					cmdList.add(spc);

				}
			}

		}else if (Validator.isNotNullOrEmpty(propertyValueInputs) && Validator.isNotNullOrEmpty(propertyValueInputIds)
				&& propertyValueInputIds.length == 1 && propertyValueInputs.length >= 2 * langSize){
			// 涓�涓嚜瀹氫箟澶氶�夈�佷竴涓閫�
			Set<Long> set = new LinkedHashSet<Long>();
			for (Long l : propertyIds){
				set.add(l);
			}
			List<Long> newPropertyIds = new ArrayList<Long>(set);
			List<Property> properties = new ArrayList<Property>();
			Property p1 = propertyManager.findPropertiesById(newPropertyIds.get(0));
			Property p2 = propertyManager.findPropertiesById(newPropertyIds.get(1));
			properties.add(p1);
			properties.add(p2);

			// String mutlIds = propertyValueInputs[2];
			// String[] newPropertyValueInputs = getMutlPropertyValuesOrLangs(propertyValueInputs);

			String[] values = getValues(propertyValueInputs);
			String[] langs = getLangs(propertyValueInputs);

			String[] inputArray1 = null;
			String[] inputArray2 = null;
			for (Property p : properties){
				// 鎵惧嚭澶氶��
				if (Property.EDITING_TYPE_MULTI_SELECT.equals(p.getEditingType())){
					inputArray2 = getInputArrayByProperty(p, propertyValueInputIds[0]);
				}else{
					inputArray1 = getInputArrayByProperty(p, propertyValueInputs[0]);
				}
			}
			int outerNum = 0;
			int innerNum = 0;
			// 椤甸潰鎸塸1銆乸2灞炴�ч『搴忎紶閫掑弬鏁�,椤哄簭纭畾浜哻ode鐨勫墠缂�
			// inputArray2-澶氶��;
			// inputArray1->鑷畾涔夊閫�
			for (String input1 : inputArray1){
				innerNum = 0;
				for (String input2 : inputArray2){
					String prefix = input2 + "_" + input1 + "_";
					if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p1.getEditingType())){
						prefix = input1 + "_" + input2 + "_";
					}

					String codeKey = prefix + "code";
					String spKey = prefix + "salePrice";
					String lpKey = prefix + "listPrice";
					String idKey = prefix + "id";
					Long skuId = getSkuIdFromStr(request.getParameter(idKey));

					String code = request.getParameter(codeKey);

					if (code == null || "".equals(code.trim())){// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
						continue;
					}

					SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();
					List<ItemPropertyMutlLangCommand> ipcList = new ArrayList<ItemPropertyMutlLangCommand>();
					for (Property p : properties){
						// 鎵惧嚭澶氶��
						if (Property.EDITING_TYPE_MULTI_SELECT.equals(p.getEditingType())){
							ItemPropertyMutlLangCommand ipc1 = getItemPropertyMutlLangCommand(p, input2, values, langs, innerNum);
							ipcList.add(ipc1);
						}else{
							ItemPropertyMutlLangCommand ipc2 = getItemPropertyMutlLangCommand(p, input1, values, langs, outerNum);
							ipcList.add(ipc2);
						}
					}
					BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
					String listPriceStr = request.getParameter(lpKey);
					if (StringUtil.isBlank(listPriceStr)){
						listPriceStr = null;
					}
					BigDecimal listPrice = this.getPriceFromStr(listPriceStr);

					spc.setId(skuId);
					spc.setCode(code);
					spc.setListPrice(listPrice);
					spc.setSalePrice(salePrice);
					spc.setPropertyList(ipcList);
					cmdList.add(spc);
					innerNum++;
				}
				outerNum++;
			}

		}
		SkuPropertyMUtlLangCommand[] cmdArray = new SkuPropertyMUtlLangCommand[cmdList.size()];
		return cmdList.toArray(cmdArray);
	}

	private SkuPropertyMUtlLangCommand[] extractiConvertSkuByI18nSwitchOff(
			HttpServletRequest request,
			Long[] propertyIds,
			String[] propertyValueInputs,
			String[] propertyValueInputIds,
			List<SkuPropertyMUtlLangCommand> cmdList){

		// 浠呬竴涓嚜瀹氫箟澶氶�夋垨澶氶��
		if ((propertyValueInputIds.length == 1 && Validator.isNullOrEmpty(propertyValueInputs))
				|| (propertyValueInputs.length == 1 && Validator.isNullOrEmpty(propertyValueInputIds))){
			Property p = propertyManager.findPropertiesById(propertyIds[0]);
			String newInputs = null;
			if (Validator.isNullOrEmpty(propertyValueInputs)){
				newInputs = propertyValueInputIds[0];
			}else{
				newInputs = propertyValueInputs[0];
			}
			String[] inputArray = getInputArrayByProperty(p, newInputs);
			int num = 0;
			for (String input : inputArray){

				String codeKey = input + "_code";
				String spKey = input + "_salePrice";
				String lpKey = input + "_listPrice";
				String idKey = input + "_id";
				Long skuId = getSkuIdFromStr(request.getParameter(idKey));

				String code = request.getParameter(codeKey);

				// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
				if (code == null || "".equals(code.trim())){
					continue;
				}
				String[] values = getValues(propertyValueInputs);
				String[] langs = getLangs(propertyValueInputs);
				SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();

				List<ItemPropertyMutlLangCommand> ipcList = new ArrayList<ItemPropertyMutlLangCommand>();

				ItemPropertyMutlLangCommand ipc = getItemPropertyMutlLangCommand(p, input, values, langs, num);
				ipcList.add(ipc);

				spc.setId(skuId);
				// String code = request.getParameter(codeKey);
				BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
				String listPriceStr = request.getParameter(lpKey);
				BigDecimal listPrice = getPriceFromStr(listPriceStr);

				spc.setCode(code);
				spc.setListPrice(listPrice);
				spc.setSalePrice(salePrice);
				spc.setPropertyList(ipcList);

				cmdList.add(spc);
				num++;
			}

		}else if ((propertyValueInputIds.length >= 2 && Validator.isNullOrEmpty(propertyValueInputs))
				|| (propertyValueInputs.length >= 2 && Validator.isNullOrEmpty(propertyValueInputIds))){
			// 涓や釜鑷畾涔夊閫夋垨涓や釜澶氶��
			if (propertyValueInputs.length == 2 && Validator.isNullOrEmpty(propertyValueInputIds)){
				// 涓や釜鑷畾涔夊閫�
				// 鎸夊師鏈夐『搴�
				Set<Long> set = new LinkedHashSet<Long>();
				for (Long l : propertyIds){
					set.add(l);
				}
				List<Long> newPropertyIds = new ArrayList<Long>(set);

				List<Property> properties = new ArrayList<Property>();
				Property p1 = propertyManager.findPropertiesById(newPropertyIds.get(0));
				Property p2 = propertyManager.findPropertiesById(newPropertyIds.get(1));
				properties.add(p1);
				properties.add(p2);

				String[] inputArray1 = getInputArrayByProperty(p1, propertyValueInputs[0]);
				String[] inputArray2 = getInputArrayByProperty(p2, propertyValueInputs[1]);

				int outerNum = 0;
				int innerNum = 0;
				for (String input1 : inputArray1){
					innerNum = 0;
					for (String input2 : inputArray2){

						String prefix = input1 + "_" + input2 + "_";
						String codeKey = prefix + "code";
						String spKey = prefix + "salePrice";
						String lpKey = prefix + "listPrice";
						String idKey = prefix + "id";
						Long skuId = getSkuIdFromStr(request.getParameter(idKey));

						String code = request.getParameter(codeKey);

						if (code == null || "".equals(code.trim())){// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
							continue;
						}

						SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();
						List<ItemPropertyMutlLangCommand> ipcList = new ArrayList<ItemPropertyMutlLangCommand>();
						if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p1.getEditingType())){
							ItemPropertyMutlLangCommand ipc1 = getItemPropertyMutlLangCommand(p1, input1, null, null, outerNum);
							ipcList.add(ipc1);
						}
						if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p2.getEditingType())){
							ItemPropertyMutlLangCommand ipc2 = getItemPropertyMutlLangCommand(p2, input2, null, null, innerNum);
							ipcList.add(ipc2);
						}

						BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
						String listPriceStr = request.getParameter(lpKey);
						if (StringUtil.isBlank(listPriceStr)){
							listPriceStr = null;
						}
						BigDecimal listPrice = this.getPriceFromStr(listPriceStr);

						spc.setId(skuId);
						spc.setCode(code);
						spc.setListPrice(listPrice);
						spc.setSalePrice(salePrice);
						spc.setPropertyList(ipcList);
						cmdList.add(spc);
						innerNum++;
					}
					outerNum++;
				}

			}else{
				// 涓変釜鍙婂叾浠ヤ笂閿�鍞睘鎬�
				List<Property> properties = new ArrayList<Property>();
				Map<String, Property> propInputMap = new HashMap<String, Property>();
				List<List<String>> list = new ArrayList<List<String>>();
				for (int i = 0; i < propertyIds.length; i++){
					// 鏍规嵁id鑾峰彇鍟嗗搧灞炴��
					Property prop = propertyManager.findPropertiesById(propertyIds[i]);
					properties.add(prop);
					// 灏哄 [624,546] 棰滆壊[847,1426] 瀛ｈ妭[27,28]
					String[] inputArray = getInputArrayByProperty(prop, propertyValueInputIds[i]);
					List<String> keyIds = Arrays.asList(inputArray);
					list.add(keyIds);
					if (!keyIds.isEmpty()){
						for (String key : keyIds){
							// [624,灏哄] [546,灏哄]
							propInputMap.put(key, prop);
						}
					}
				}

				int outerNum = 0;

				List<List<String>> result = buildTable(null, list, 0);

				for (List<String> rList : result){
					String prefix = "";
					List<ItemPropertyMutlLangCommand> ipcList = new ArrayList<ItemPropertyMutlLangCommand>();
					// ====绗�1琛宺List: [624, 847]
					// ====绗�2琛宺List: [546, 847]
					for (String r : rList){
						prefix += r + "_";
						Property prop = propInputMap.get(r);
						if (Property.EDITING_TYPE_MULTI_SELECT.equals(prop.getEditingType())){
							ItemPropertyMutlLangCommand ipc1 = getItemPropertyMutlLangCommand(prop, r, null, null, outerNum);
							ipcList.add(ipc1);
						}
					}
					// 鎷兼帴key
					String codeKey = prefix + "code";
					String spKey = prefix + "salePrice";
					String lpKey = prefix + "listPrice";
					String idKey = prefix + "id";
					Long skuId = getSkuIdFromStr(request.getParameter(idKey));

					String code = request.getParameter(codeKey);

					if (code == null || "".equals(code.trim())){// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
						continue;
					}

					SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();

					BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
					String listPriceStr = request.getParameter(lpKey);
					if (StringUtil.isBlank(listPriceStr)){
						listPriceStr = null;
					}
					BigDecimal listPrice = this.getPriceFromStr(listPriceStr);

					spc.setId(skuId);
					spc.setCode(code);
					spc.setListPrice(listPrice);
					spc.setSalePrice(salePrice);
					spc.setPropertyList(ipcList);
					cmdList.add(spc);

				}
			}

		}else if (Validator.isNotNullOrEmpty(propertyValueInputs) && Validator.isNotNullOrEmpty(propertyValueInputIds)
				&& propertyValueInputIds.length == 1 && propertyValueInputs.length == 1){
			// 涓�涓嚜瀹氫箟澶氶�夈�佷竴涓閫�
			Set<Long> set = new LinkedHashSet<Long>();
			for (Long l : propertyIds){
				set.add(l);
			}
			List<Long> newPropertyIds = new ArrayList<Long>(set);
			List<Property> properties = new ArrayList<Property>();
			Property p1 = propertyManager.findPropertiesById(newPropertyIds.get(0));
			Property p2 = propertyManager.findPropertiesById(newPropertyIds.get(1));
			properties.add(p1);
			properties.add(p2);

			String[] inputArray1 = null;
			String[] inputArray2 = null;
			for (Property p : properties){
				// 鎵惧嚭澶氶��
				if (Property.EDITING_TYPE_MULTI_SELECT.equals(p.getEditingType())){
					inputArray2 = getInputArrayByProperty(p, propertyValueInputIds[0]);
				}else{
					inputArray1 = getInputArrayByProperty(p, propertyValueInputs[0]);
				}
			}
			int outerNum = 0;
			int innerNum = 0;
			// 椤甸潰鎸塸1銆乸2灞炴�ч『搴忎紶閫掑弬鏁�,椤哄簭纭畾浜哻ode鐨勫墠缂�
			// inputArray2-澶氶��;
			// inputArray1->鑷畾涔夊閫�
			for (String input1 : inputArray1){
				innerNum = 0;
				for (String input2 : inputArray2){
					String prefix = input2 + "_" + input1 + "_";
					if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p1.getEditingType())){
						prefix = input1 + "_" + input2 + "_";
					}

					String codeKey = prefix + "code";
					String spKey = prefix + "salePrice";
					String lpKey = prefix + "listPrice";
					String idKey = prefix + "id";
					Long skuId = getSkuIdFromStr(request.getParameter(idKey));

					String code = request.getParameter(codeKey);

					if (code == null || "".equals(code.trim())){// 濡傛灉娌℃湁濉啓skuCode ,閭ｄ箞灏辫涓� 娌℃湁璇ュ睘鎬х殑sku 涓嶈繘琛屼繚瀛樻垨鑰呬慨鏀�
						continue;
					}

					SkuPropertyMUtlLangCommand spc = new SkuPropertyMUtlLangCommand();
					List<ItemPropertyMutlLangCommand> ipcList = new ArrayList<ItemPropertyMutlLangCommand>();
					for (Property p : properties){
						// 鎵惧嚭澶氶��
						if (Property.EDITING_TYPE_MULTI_SELECT.equals(p.getEditingType())){
							ItemPropertyMutlLangCommand ipc1 = getItemPropertyMutlLangCommand(p, input2, null, null, innerNum);
							ipcList.add(ipc1);
						}else{
							ItemPropertyMutlLangCommand ipc2 = getItemPropertyMutlLangCommand(p, input1, null, null, outerNum);
							ipcList.add(ipc2);
						}
					}
					BigDecimal salePrice = getPriceFromStr(request.getParameter(spKey));
					String listPriceStr = request.getParameter(lpKey);
					if (StringUtil.isBlank(listPriceStr)){
						listPriceStr = null;
					}
					BigDecimal listPrice = this.getPriceFromStr(listPriceStr);

					spc.setId(skuId);
					spc.setCode(code);
					spc.setListPrice(listPrice);
					spc.setSalePrice(salePrice);
					spc.setPropertyList(ipcList);
					cmdList.add(spc);
					innerNum++;
				}
				outerNum++;
			}

		}
		SkuPropertyMUtlLangCommand[] cmdArray = new SkuPropertyMUtlLangCommand[cmdList.size()];
		return cmdList.toArray(cmdArray);
	}

	private String[] getMutlPropertyValuesOrLangs(String[] inputs){
		String[] values = new String[inputs.length - 1];
		int num = 0;
		for (int i = 0; i < inputs.length; i++){
			String str = inputs[i];
			if (i != 2){
				values[num] = str;
				num++;
			}
		}
		return values;
	}

	private String[] getValues(String[] inputs){
		String[] values = new String[MutlLang.i18nSize()];
		int num = 0;
		for (int i = 0; i < inputs.length; i++){
			if (i % 2 == 0){
				values[num] = inputs[i];
				num++;
			}
		}
		return values;

	}

	private String[] getLangs(String[] inputs){
		String[] langs = new String[MutlLang.i18nSize()];
		int num = 0;
		for (int i = 0; i < inputs.length; i++){
			if (i % 2 == 1){
				langs[num] = inputs[i];
				num++;
			}
		}
		return langs;

	}

	private Map<String, String[]> getValues(Property p,String[] values){
		String[] vs = new String[MutlLang.i18nSize()];
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (int i = 0; i < MutlLang.i18nLangs().size(); i++){
			for (int j = 0; j < values.length; j++){
				String str = values[j];
				if (str.split("\\|\\|").length == 1){
					vs[j] = str.split("\\|\\|")[0];
				}else{
					vs[j] = str.split("\\|\\|")[i];
				}
			}
			map.put(MutlLang.i18nLangs().get(i), vs);
		}
		return map;

	}

	private Long getSkuIdFromStr(String idStr){
		if (StringUtils.isNotBlank(idStr) && !idStr.equals("null")){
			return new Long(idStr);
		}else{
			return null;
		}
	}

	private BigDecimal getPriceFromStr(String priceStr){
		if (StringUtils.isNotBlank(priceStr)){
			BigDecimal price = new BigDecimal(priceStr);
			return price;
		}else{
			return null;
		}
	}

	private String[] getInputArrayByProperty(Property p,String inputStr){
		if (Property.EDITING_TYPE_MULTI_SELECT.equals(p.getEditingType())){
			return inputStr.split(",");
		}

		if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p.getEditingType())){
			return inputStr.split("\\|\\|");
		}

		return null;
	}

	private ItemPropertyCommand getItemPropertyCommand(Property p,String value){
		ItemPropertyCommand ipc = new ItemPropertyCommand();
		ipc.setpId(p.getId());
		ipc.setpName(p.getName());
		ipc.setId(null);
		ipc.setValue(null);
		if (Property.EDITING_TYPE_MULTI_SELECT.equals(p.getEditingType())){
			ipc.setId(value);
		}

		if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p.getEditingType())){
			ipc.setValue(value);
		}

		return ipc;
	}

	private ItemPropertyMutlLangCommand getItemPropertyMutlLangCommand(Property p,String value,String[] vals,String[] langs,int index){
		ItemPropertyMutlLangCommand ipc = new ItemPropertyMutlLangCommand();
		ipc.setpId(p.getId());
		ipc.setpName(p.getName());
		ipc.setId(null);
		ipc.setValue(null);
		if (Property.EDITING_TYPE_MULTI_SELECT.equals(p.getEditingType())){
			ipc.setId(value);
		}

		if (Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(p.getEditingType())){
			// TODO 璁剧疆澶氳瑷�淇℃伅
			boolean i18n = LangProperty.getI18nOnOff();
			if (i18n){
				MutlLang mutlLang = new MutlLang();
				String[] values = new String[MutlLang.i18nSize()];
				values[0] = value;
				for (int i = 1; i < vals.length; i++){
					String val = vals[i];
					String[] arr = val.split("\\|\\|");
					values[i] = arr[index];
				}
				// mutlLang.setValues(getValues(p, values).get(langs[index]));
				mutlLang.setValues(values);
				mutlLang.setLangs(langs);
				ipc.setValue(mutlLang);
			}else{
				SingleLang singleLang = new SingleLang();
				singleLang.setValue(value);
				ipc.setValue(singleLang);
			}
		}
		return ipc;
	}

	@RequestMapping("/item/validateUpdateSkuCodes.json")
	@ResponseBody
	public Object validateUpdateSkuCodes(@ArrayCommand(dataBind =true) Long[] skuIds,
			@ArrayCommand(dataBind =true) String[] skuCodes){
		boolean flag = true;
		List<String> invalidCodes = new ArrayList<String>();
		
		List<SkuPropertyCommand> skuInfoList = new ArrayList<SkuPropertyCommand>();
		SkuPropertyCommand skuPropertyCommand =null;
		for (int i =0 ; i < skuIds.length ;i++) {
			skuPropertyCommand =new SkuPropertyCommand();
			skuPropertyCommand.setId(skuIds[i]);
			skuPropertyCommand.setCode(skuCodes[i]);
			skuInfoList.add(skuPropertyCommand);
		}
		invalidCodes = itemManager.validateUpdateSkuCode(skuInfoList);
		StringBuilder sb = new StringBuilder();
		
		if (invalidCodes.size() > 0){
			flag = false;
			for (int i = 0; i < invalidCodes.size(); i++){
				sb.append(invalidCodes.get(i));
				if (i != invalidCodes.size() - 1){
					sb.append(",");
				}
			}
		}
		BackWarnEntity backWarnEntity = new BackWarnEntity(flag, null);
		if (!flag){
			backWarnEntity.setDescription(sb.toString());
		}

		return backWarnEntity;
	}

	@RequestMapping("/item/validateSkuCode.json")
	@ResponseBody
	public Object vailidateSkuCode(@RequestParam(value = "skuCodes") String[] skuCodes){

		List<String> invalidCodes = new ArrayList<String>();
		for (String code : skuCodes){
			Sku sku = itemManager.findSkuBySkuCode(code);
			if (sku != null){
				invalidCodes.add(code);
			}
		}
		StringBuilder sb = new StringBuilder();
		boolean flag = true;
		if (invalidCodes.size() > 0){
			flag = false;
		}

		for (int i = 0; i < invalidCodes.size(); i++){
			sb.append(invalidCodes.get(i));
			if (i != invalidCodes.size() - 1){
				sb.append(",");
			}
		}
		BackWarnEntity backWarnEntity = new BackWarnEntity(flag, null);
		if (!flag){
			backWarnEntity.setDescription(sb.toString());
		}
		// backWarnEntity.setErrorCode(item.getId().intValue());
		return backWarnEntity;
	}

	// 灏嗘暟鎹簱涓彇鍑烘潵鐨� 鍟嗗搧鎻忚堪涓殑鏈湴鏈嶅姟鍣ㄥ湴鍧�鍔犱笂domain
	public String addDefinedDomainInDesc(String desc,String defineDomain){
		if (desc == null)
			return "";
		Document doc = Jsoup.parse(desc);
		Elements es = doc.select("img");
		for (int i = 0; i < es.size(); i++){
			Element e = es.get(i);
			// System.out.println(e.attr("src"));
			String imgStr = e.attr("src");

			e.attr("src", getFullDomainUrl(defineDomain, imgStr.trim()));
		}
		desc = doc.select("body").html();
		return desc;
	}

	public LangProperty addDefinedDomainInDesc(LangProperty desc,String defineDomain){
		if (desc == null){
			return null;
		}
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n){
			MutlLang mutlLang = (MutlLang) desc;
			String[] values = mutlLang.getValues();
			if (values != null){
				for (int i = 0; i < values.length; i++){
					String str = values[i];
					if (str == null){
						continue;
					}
					Document doc = Jsoup.parse(str);
					Elements es = doc.select("img");
					for (int j = 0; j < es.size(); j++){
						Element e = es.get(j);
						String imgStr = e.attr("src");
						e.attr("src", getFullDomainUrl(defineDomain, imgStr.trim()));
					}
					str = doc.select("body").html();
					values[i] = str;
				}
				mutlLang.setValues(values);
			}
		}else{
			SingleLang singleLang = (SingleLang) desc;
			String value = singleLang.getValue();
			if (value != null){
				Document doc = Jsoup.parse(value);
				Elements es = doc.select("img");
				for (int j = 0; j < es.size(); j++){
					Element e = es.get(j);
					String imgStr = e.attr("src");
					e.attr("src", getFullDomainUrl(defineDomain, imgStr.trim()));
				}
				value = doc.select("body").html();
				singleLang.setValue(value);
			}
		}

		return desc;
	}

	// 灏嗕紶杩囨潵鐨勫晢鍝佹弿杩颁腑鐨� 涓浘鍍忓湴鍧� 锛堝鏋滄槸鏈湴涓婁紶锛屽垯鏀逛负鍘绘帀鏈嶅姟鍣ㄥ煙鍚嶏級
	public String removeDefinedDomainInDesc(String desc,String defineDomain){
		Document doc = Jsoup.parse(desc);
		Elements es = doc.select("img");
		for (int i = 0; i < es.size(); i++){
			Element e = es.get(i);
			// System.out.println(e.attr("src"));
			String imgStr = e.attr("src");

			e.attr("src", getRemoveDomainUrl(defineDomain, imgStr.trim()));
		}
		desc = doc.select("body").html();
		return desc;
	}

	public String getRemoveDomainUrl(String domain,String imgStr){
		String newUrl = "";
		if (null != imgStr && imgStr.startsWith(domain)){
			newUrl = imgStr.replace(domain, "");
			return newUrl;
		}else{
			return imgStr;
		}

	}

	public String getFullDomainUrl(String domain,String imgStr){
		String newUrl = "";
		if (null != imgStr && !imgStr.startsWith("http://")){
			newUrl = domain + imgStr;
			return newUrl;
		}else{
			return imgStr;
		}
	}

	/**
	 * 椤甸潰璺宠浆 淇敼鍟嗗搧
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/item/updateItem.htm")
	public String updateItem(Model model,@RequestParam("itemId") String itemId){
		// 鍒嗙被鍒楄〃
		Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);

		// 鏌ユ壘鍟嗗搧缂栫爜銆佹墍灞炶涓�
		Item item = itemManager.findItemById(Long.valueOf(itemId));
		// 鏍规嵁琛屼笟Id鏌ユ壘琛屼笟
		Industry industry = industryManager.findIndustryById(item.getIndustryId());
		// 鏌ユ壘鍟嗗搧鍒嗙被
		Long[] categoryIds;
		List<Category> categories = null;
		List<ItemCategory> itemCategoryList = itemCategoryManager.findItemCategoryListByItemId(Long.valueOf(itemId));
		int i = 0;
		if (itemCategoryList != null && itemCategoryList.size() > 0){
			categoryIds = new Long[itemCategoryList.size()];
			for (ItemCategory itemCategory : itemCategoryList){
				categoryIds[i] = itemCategory.getCategoryId();
				i++;
			}
			// 鏍规嵁鍒嗙被Id鏁扮粍鏌ヨ鍟嗗搧鍒嗙被
			categories = categoryManager.findCategoryListByCategoryIds(categoryIds);
		}
		// 鏍规嵁鍟嗗搧id鏌ヨ鍟嗗搧鍒嗙被
		ItemCategoryCommand defaultItemCategory = itemCategoryManager.findDefaultCategoryByItemId(Long.valueOf(itemId));

		// 鏌ユ壘鍟嗗搧灞炴�у強灞炴�у��
		List<ItemPropertiesCommand> itemProperties = itemManager.findItemPropertiesCommandListyByItemId(Long.valueOf(itemId));
		// 鏍规嵁琛屼笟Id鍜屽簵閾篒d鏌ユ壘瀵瑰簲灞炴�у拰灞炴�у��
		List<DynamicPropertyCommand> dynamicPropertyCommandList = itemManager.findDynamicPropertisByIndustryId(item.getIndustryId());
		List<Object> propertyIdArray = new ArrayList<Object>();
		List<Object> propertyNameArray = new ArrayList<Object>();
		List<Object> mustCheckArray = new ArrayList<Object>();

		int j = 0;
		if (dynamicPropertyCommandList != null && dynamicPropertyCommandList.size() > 0){
			for (DynamicPropertyCommand dynamicPropertyCommand : dynamicPropertyCommandList){
				Property property = dynamicPropertyCommand.getProperty();
				if (property.getIsSaleProp()
						&& (Property.EDITING_TYPE_MULTI_SELECT.equals(property.getEditingType()) || Property.EDITING_TYPE_CUSTOM_MULTI_SELECT
								.equals(property.getEditingType()))){
					propertyIdArray.add(property.getId());
					propertyNameArray.add("'" + property.getName() + "'");
				}
				if (!dynamicPropertyCommand.getProperty().getIsSaleProp() && dynamicPropertyCommand.getPropertyValueList().size() > 0){
					if (dynamicPropertyCommand.getProperty().getRequired() && dynamicPropertyCommand.getProperty().getEditingType() == 4){
						mustCheckArray.add("'" + dynamicPropertyCommand.getProperty().getName() + "'");
					}
				}
				j++;
			}
		}

		// 鏍规嵁itemId鏌ユ壘Sku
		List<Sku> skuList = itemManager.findSkuByItemId(item.getId());

		List<Object> newSkuList = new ArrayList<Object>();
		// sku閿�鍞环
		List<BigDecimal> salePrices = new ArrayList<BigDecimal>();
		// sku閿�鍞环
		List<BigDecimal> listPrices = new ArrayList<BigDecimal>();

		for (Sku sku : skuList){

			if (Sku.LIFE_CYCLE_ENABLE.equals(sku.getLifecycle())){
				sku.setProperties(sku.getProperties());
				newSkuList.add(sku);
				if (sku.getSalePrice() != null){
					salePrices.add(sku.getSalePrice());
				}
				if (sku.getListPrice() != null){
					listPrices.add(sku.getListPrice());
				}
			}

		}
		// JSONArray skuJa = new JSONArray(newSkuList, "***");
		// String skuJaStr = skuJa.toString();

		Gson sg = new Gson();
		String skuJaStr = sg.toJson(newSkuList);
		String itemPropertiesStr = sg.toJson(itemProperties);

		JSONArray dynamicPropertyCommandListJson = new JSONArray(dynamicPropertyCommandList, "***");
		String dynamicPropertyCommandListJsonStr = dynamicPropertyCommandListJson.toString();

		// // 缂╃暐鍥捐鏍�
		// List<ChooseOption> thumbnailConfig = chooseOptionManager.findEffectChooseOptionListByGroupCode(THUMBNAIL_CONFIG);
		// model.addAttribute("thumbnailConfig", thumbnailConfig);
		// 鏌ユ壘鍟嗗搧鍚嶇О銆佸晢鍝佹弿杩�
		ItemInfoCommand itemInfo = itemManager.findItemInfoCommandByItemId(Long.valueOf(itemId));
		model.addAttribute("industry", industry);
		model.addAttribute("code", item.getCode());
		model.addAttribute("id", item.getId());
		model.addAttribute("type", itemInfo.getType());
		model.addAttribute("style", itemInfo.getStyle());
		itemInfo.setDescription(addDefinedDomainInDesc(itemInfo.getDescription(), UPLOAD_IMG_DOMAIN));
		model.addAttribute("salePrice", itemInfo.getSalePrice());
		model.addAttribute("listPrice", itemInfo.getListPrice());
		model.addAttribute("dynamicPropertyCommandList", dynamicPropertyCommandList);
		model.addAttribute("dynamicPropertyCommandListJsonStr", dynamicPropertyCommandListJsonStr);
		model.addAttribute("propertyIdArray", propertyIdArray);
		model.addAttribute("propertyNameArray", propertyNameArray);
		model.addAttribute("mustCheckArray", mustCheckArray);
		model.addAttribute("categories", categories);

		model.addAttribute("skuList", skuJaStr);
		model.addAttribute("salePrices", salePrices);
		model.addAttribute("listPrices", listPrices);
		model.addAttribute("defaultItemCategory", defaultItemCategory);
		model.addAttribute("lastSelectPropertyId", itemInfo.getLastSelectPropertyId());
		model.addAttribute("lastSelectPropertyValueId", itemInfo.getLastSelectPropertyValueId());
		model.addAttribute("itemPropertiesStr", itemPropertiesStr);
		model.addAttribute("isStyleEnable", isEnableStyle());
		// 鍥介檯鍖栧睘鎬�
		model.addAttribute("itemProperties", itemProperties);

		model.addAttribute("title", itemInfo.getTitle());
		model.addAttribute("subTilte", itemInfo.getSubTitle());
		model.addAttribute("seoTitle", itemInfo.getSeoTitle());
		model.addAttribute("seoKeywords", itemInfo.getSeoKeywords());
		model.addAttribute("seoDescription", itemInfo.getSeoDescription());
		model.addAttribute("sketch", itemInfo.getSketch());
		model.addAttribute("description", itemInfo.getDescription());
		String itemCodeValidMsg = messageSource.getMessage(
				ErrorCodes.BUSINESS_EXCEPTION_PREFIX + ErrorCodes.ITEM_CODE_VALID_ERROR,
				new Object[] {},
				Locale.SIMPLIFIED_CHINESE);
		model.addAttribute("itemCodeValidMsg", itemCodeValidMsg);
		String pdValidCode = sdkMataInfoManager.findValue(MataInfo.PD_VALID_CODE);
		model.addAttribute("pdValidCode", pdValidCode);
		return "/product/item/update-item";
	}

	/**
	 * 楠岃瘉鍟嗗搧缂栫爜鐨勫敮涓�鎬�
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping("/item/validateItemCode.json")
	@ResponseBody
	public Object validateItemCode(@RequestParam("code") String code){
		// 鏌ヨorgId
		UserDetails userDetails = this.getUserDetails();

		ShopCommand shopCommand = null;
		Long shopId = 0L;

		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 鏍规嵁orgId鏌ヨshopId
		if (currentOrgId != null){
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();

		}
		Integer count = itemManager.validateItemCode(code, shopId);
		if (count > 0){
			return FAILTRUE;
		}else{
			return SUCCESS;
		}
	}

	// 椤甸潰鍟嗗搧绠＄悊
	@RequestMapping("/item/itemList.htm")
	public String itemList(Model model){
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);
		List<Industry> result = industryManager.findAllIndustryList();
		model.addAttribute("industrylist", result);
		String categoryDisplayMode = sdkMataInfoManager.findValue(MataInfo.KEY_PTS_ITEM_LIST_PAGE_CATEGORYNAME_MODE);
		model.addAttribute("categoryDisplayMode", categoryDisplayMode);
		// 鏌ヨ杩欎釜寮�鍏抽厤缃紙寮�鍏崇殑浣滅敤鏄帶鍒跺晢鍝佺鐞嗛〉闈㈡槸鏄剧ず'鏈�杩戜笂鏋舵椂闂�'杩樻槸'鍒濆涓婃灦鏃堕棿'锛�
		String updateListTimeFlag = sdkMataInfoManager.findValue(MataInfo.UPDATE_ITEM_LISTTIME);
		model.addAttribute("updateListTimeFlag", updateListTimeFlag);
		return "/product/item/item-List";
	}

	/**
	 * 鍔ㄦ�佽幏鍙栧晢鍝佸垪琛�
	 * 
	 * @param QueryBean
	 * @param Model
	 * @return
	 */
	@RequestMapping("/item/itemList.json")
	@ResponseBody
	public Pagination<ItemCommand> findItemListJson(Model model,@QueryBeanParam QueryBean queryBean){

		// 鏌ヨorgId
		UserDetails userDetails = this.getUserDetails();
		ShopCommand shopCommand = null;
		Long shopId = 0L;

		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 鏍规嵁orgId鏌ヨshopId
		if (currentOrgId != null){
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			if (shopCommand != null){
				shopId = shopCommand.getShopid();
			}
		}

		Sort[] sorts = queryBean.getSorts();

		if (sorts == null || sorts.length == 0){
			Sort sort = new Sort("tpit.createTime", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}

		Pagination<ItemCommand> args = itemManager.findItemListByQueryMap(queryBean.getPage(), sorts, queryBean.getParaMap(), shopId);

		return args;
	}

	/**
	 * 閫氳繃itemid鍚敤绂佺敤鍟嗗搧
	 * 
	 * @param itemid
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/item/enableOrDisableItem.json")
	public Object enableOrDisableItemById(@RequestParam("itemId") Long itemId,@RequestParam("state") Integer state){
		List<Long> ids = new ArrayList<Long>();
		ids.add(itemId);

		UserDetails userDetails = this.getUserDetails();
		Integer result = itemManager.enableOrDisableItemByIds(ids, state,userDetails.getUsername());
		if (result < 1){
			if (state != 1){
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY_DISABLED_FAIL);
			}else{
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY__ENABLE_FAIL);
			}
		}
		return SUCCESS;
	}

	/**
	 * 閫氳繃itemid鍚敤绂佺敤鍟嗗搧
	 * 
	 * @param itemid
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/item/enableOrDisableItems.json")
	public Object enableOrDisableItemByIds(@RequestParam("itemIds") String itemIds,@RequestParam("state") Integer state){
		String[] array = itemIds.split(",");
		List<Long> ids = new ArrayList<Long>();

		for (String str : array){
			ids.add(Long.parseLong(str));
		}
		UserDetails userDetails = this.getUserDetails();
		Integer result = itemManager.enableOrDisableItemByIds(ids, state, userDetails.getUsername());
		if (result < 1){
			if (state != 1){
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY_DISABLED_FAIL);
			}else{
				throw new BusinessException(ErrorCodes.PRODUCT_PROPERTY__ENABLE_FAIL);
			}
		}
		return SUCCESS;
	}

	/**
	 * 閫氳繃ids閫昏緫鍒犻櫎
	 * 
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping("/item/remove.json")
	public void deleteItem(@RequestParam("ids") List<Long> ids,Model model,HttpServletRequest request,HttpServletResponse response){
		itemManager.removeItemByIds(ids);

		response.addHeader("Content-Type", "text/plain");
		try{
			response.getWriter().write("success");
		}catch (IOException e){
			log.warn("fail to write response content", e);
		}
	}

	@RequestMapping("/item/active.json")
	@ResponseBody
	public Object activeItems(
			@RequestParam("ids") List<Long> ids,
			@RequestParam("activeBeginTime") String activeBeginTimeStr,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws ParseException{
		Date activeBeginTime = null;
		if (StringUtils.isNotBlank(activeBeginTimeStr)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			activeBeginTime = format.parse(activeBeginTimeStr);
		}
		itemManager.activeItemByIds(ids, activeBeginTime);

		return SUCCESS;
	}

	@RequestMapping("/item/updateItemInfoLSPVIdByItemId.json")
	@ResponseBody
	public Object updateItemInfoLSPVIdByItemId(
			@RequestParam("itemId") Long itemId,
			@RequestParam("lastSelectPropertyValueId") Long lastSelectPropertyValueId){
		itemManager.updateItemInfoLSPVIdByItemId(lastSelectPropertyValueId, itemId);
		return SUCCESS;
	}

	/**
	 * 涓嬭浇瀵煎叆鍟嗗搧妯℃澘
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param industryId
	 * @return
	 */
	@RequestMapping(value = "/sku/tplt_sku_import.xls")
	@ResponseBody
	public String downloadSkuInfoTemplate(Model model,HttpServletRequest request,HttpServletResponse response,Long industryId){
		Long shopId = findShopId();
		itemManager.downloadFile(request, response, shopId, industryId);
		return "json";
	}

	/**
	 * 瀵煎叆鍟嗗搧
	 * 
	 * @param request
	 * @param model
	 * @param response
	 */
	@RequestMapping(value = "/sku/skuUpload.json",method = RequestMethod.POST)
	public void skuUpload(HttpServletRequest request,Model model,HttpServletResponse response){
		// 娓呴櫎鐢ㄦ埛閿欒淇℃伅鏂囦欢
		claerUserFile();
		response.setContentType("text/html;charset=UTF-8");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		Long shopId = findShopId();
		String name = file.getOriginalFilename();
		InputStreamCacher cacher = null;
		try{
			cacher = new InputStreamCacher(file.getInputStream());
		}catch (IOException e1){
			e1.printStackTrace();
		}
		Map<String, Object> rs = new HashMap<String, Object>();

		try{
			List<Item> itemList = itemManager.importItemFromFileI18n(file.getInputStream(), shopId);
			// 鍒穝olr
			if (Validator.isNotNullOrEmpty(itemList)){
				List<Long> idList = new ArrayList<Long>();
				for (Item item : itemList){
					idList.add(item.getId());
				}
				boolean i18n = LangProperty.getI18nOnOff();
				if (i18n){
					itemSolrManager.saveOrUpdateItemI18n(idList);
				}else{
					itemSolrManager.saveOrUpdateItem(idList);
				}
			}
		}catch (BusinessException e){
			e.printStackTrace();

			Boolean flag = true;
			List<String> errorMessages = new ArrayList<String>();
			BusinessException linkedException = e;
			while (flag){
				String message = "";
				if (linkedException.getErrorCode() == 0){
					message = linkedException.getMessage();
				}else{
					if (null == linkedException.getArgs()){
						message = getMessage(linkedException.getErrorCode());
					}else{
						message = getMessage(linkedException.getErrorCode(), linkedException.getArgs());
					}

				}
				errorMessages.add(message);

				if (null == linkedException.getLinkedException()){
					flag = false;
				}else{
					linkedException = linkedException.getLinkedException();
				}
			}
			String userFilekey = addErrorInfo(errorMessages, cacher, response, name);
			// 杩斿洖鍊�
			rs.put("isSuccess", false);
			rs.put("description", errorMessages);
			rs.put("userFilekey", userFilekey);
			returnRes(response, rs);
		}catch (IOException e){
			e.printStackTrace();
		}
		rs.put("isSuccess", true);
		returnRes(response, rs);

	}

	private void returnRes(HttpServletResponse response,Map<String, Object> rs){
		PrintWriter out = null;
		try{
			out = response.getWriter();
			out.write(JsonFormatUtil.format(rs));
		}catch (IOException e1){
			e1.printStackTrace();
		}finally{
			if (null != out){
				out.close();
			}
		}
	}

	public Long findShopId(){
		// 鏌ヨorgId
		UserDetails userDetails = this.getUserDetails();

		ShopCommand shopCommand = null;
		Long shopId = 0L;

		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 鏍规嵁orgId鏌ヨshopId
		if (currentOrgId != null){
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();
		}
		return shopId;

	}

	@RequestMapping(value = "/item/findItemCommandByCode.json",method = RequestMethod.POST)
	@ResponseBody
	public Object findItemCommandByCode(@RequestParam("code") String code){
		ItemCommand itemCommnd = itemManager.findItemCommandByCode(code, UPLOAD_IMG_DOMAIN);
		return itemCommnd;
	}

	@RequestMapping("/item/findItemListByCodes.json")
	@ResponseBody
	public Object findItemListByCodes(@ArrayCommand(dataBind = true) String[] itemCodes){
		List<String> itemCodeList = Arrays.asList(itemCodes);
		List<Item> itemList = itemManager.findItemListByCodes(itemCodeList);
		return itemList;
	}

	@RequestMapping("/item/findItemListByIds.json")
	@ResponseBody
	public Object findItemListByIds(@ArrayCommand(dataBind = true) Long[] itemIds){
		List<Long> itemIdList = Arrays.asList(itemIds);
		List<ItemCommand> itemCommandList = itemManager.findItemCommandListByIds(itemIdList);
		return itemCommandList;
	}

	/**
	 * @author 浣曟尝
	 * @Description:
	 * @param model
	 * @return String
	 * @throws
	 */
	@RequestMapping("/item/sortScore.htm")
	public String sortScore(Model model){
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> cateList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", cateList);
		List<Industry> list = industryManager.findAllIndustryList();
		model.addAttribute("industryList", list);
		return "/product/item/sortScore";
	}

	@RequestMapping("/item/sortScoreList.json")
	@ResponseBody
	public Pagination<ItemSortScoreCommand> sortScoreList(Model model,@QueryBeanParam QueryBean queryBean){
		Sort[] sorts = queryBean.getSorts();
		if (ArrayUtils.isEmpty(sorts)){
			sorts = Sort.parse("create_time desc");
		}
		return sdkItemSortScoreManager.findEffectItemSortCommandScoreListByQueryMapWithPage(
				queryBean.getPage(),
				sorts,
				queryBean.getParaMap());
	}

	@RequestMapping("/item/addSortScore.json")
	@ResponseBody
	public BackWarnEntity addSortScore(ItemSortScore model){
		BackWarnEntity result = new BackWarnEntity();
		try{
			sdkItemSortScoreManager.saveItemSortScore(model);
			result.setIsSuccess(true);
			return result;
		}catch (BusinessException e){
			result.setIsSuccess(false);
			result.setDescription("娣诲姞澶辫触");
			log.error("鎺掑簭瑙勫垯娣诲姞澶辫触", e);
			return result;
		}

	}

	@RequestMapping("/item/removeSortScore.json")
	@ResponseBody
	public BackWarnEntity removeSortScore(Long[] ids){
		BackWarnEntity result = new BackWarnEntity();
		try{
			sdkItemSortScoreManager.removeItemSortScoreByIds(Arrays.asList(ids));
			result.setIsSuccess(true);
			return result;
		}catch (BusinessException e){
			result.setIsSuccess(false);
			result.setDescription("鍒犻櫎澶辫触");
			log.error("鎺掑簭瑙勫垯鍒犻櫎澶辫触", e);
			return result;
		}

	}

	@RequestMapping("/item/enableSortScore.json")
	@ResponseBody
	public BackWarnEntity enableSortScore(Long[] ids,Integer state){
		BackWarnEntity result = new BackWarnEntity();
		try{
			sdkItemSortScoreManager.enableOrDisableItemSortScoreByIds(Arrays.asList(ids), state);
			result.setIsSuccess(true);
			return result;
		}catch (BusinessException e){
			result.setIsSuccess(false);
			result.setDescription("鎺掑簭瑙勫垯鍚敤鎴栫鐢ㄥけ璐�");
			log.error("鎺掑簭瑙勫垯鍚敤鎴栫鐢ㄥけ璐�", e);
			return result;
		}

	}

	@RequestMapping("/item/getSortScore.json")
	@ResponseBody
	public ItemSortScore getSortScore(Long id){
		try{
			return sdkItemSortScoreManager.findItemSortScoreById(id);
		}catch (BusinessException e){
			log.error("鑾峰彇鎺掑簭瑙勫垯澶辫触", e);
			return null;
		}
	}

	@RequestMapping("/item/downloadUserFile.xls")
	public void downloadUserFile(HttpServletResponse response,String userFilekey){
		OutputStream out = null;
		try{
			HSSFWorkbook workbook = userExcelFile.get(userFilekey);
			if (workbook == null){
				return;
			}
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.addHeader("Content-Disposition", "attachment; filename=" + userFilekey.split(":")[0]);
			out = response.getOutputStream();
			workbook.write(out);
		}catch (IOException e){
			log.error(e.getMessage(), e);
		}finally{
			userExcelFile.remove(userFilekey);
			try{
				out.flush();
				out.close();
			}catch (IOException e){
				log.error(e.getMessage(), e);
			}
		}
	}

	private String addErrorInfo(List<String> errorMessages,InputStreamCacher cacher,HttpServletResponse response,String name){
		String userFilekey = name + ":" + String.valueOf(System.currentTimeMillis());
		InputStream input = cacher.getInputStream();
		if (input == null || errorMessages == null || errorMessages.size() == 0){
			return null;
		}
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(input);
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// HSSFPatriarch patr = sheet.createDrawingPatriarch();
			for (String msg : errorMessages){
				String[] msgs = msg.split(" ");
				if (msgs.length != 5 && msgs.length != 6){
					continue;
				}
				int sheetAt = Integer.parseInt(msgs[1]) - 1;
				HSSFSheet sheet = workbook.getSheetAt(sheetAt);
				if (msgs.length == 5){
					String rcIndex = msgs[3];
					int[] rc = getRowCellIndex(rcIndex);
					String info = rcIndex + msgs[4];
					HSSFRow row = sheet.getRow(rc[0] - 1);
					HSSFCell cell = row.getCell(rc[1]);
					if (cell == null){
						row.createCell(rc[1]);
						cell = row.getCell(rc[1]);
						// cell.setCellValue("");
					}
					cell.setCellStyle(cellStyle);
					// cell.removeCellComment();
					// 瀹氫箟娉ㄩ噴鐨勫ぇ灏忓拰浣嶇疆
					// HSSFComment comment = patr.createComment(new HSSFClientAnchor(0,0,0,0, (short)4, 2 ,(short) 6, 5));
					// comment.setString(new HSSFRichTextString(info));
					// 璁剧疆娉ㄩ噴鍐呭
					// cell.setCellComment(comment);
				}else if (msgs.length == 6){
					// Sheet 1 鍗曞厓鏍� N8 : 鎵句笉鍒板�笺�恡est1029銆�
					String rcIndex = msgs[3];
					int[] rc = getRowCellIndex(rcIndex);
					String info = rcIndex + msgs[5];
					HSSFRow row = sheet.getRow(rc[0] - 1);
					HSSFCell cell = row.getCell(rc[1]);
					if (cell == null){
						row.createCell(rc[1]);
						cell = row.getCell(rc[1]);
						cell.setCellValue(info);
					}
					cell.setCellStyle(cellStyle);
					// cell.removeCellComment();
					// 瀹氫箟娉ㄩ噴鐨勫ぇ灏忓拰浣嶇疆
					// HSSFComment comment = patr.createComment(new HSSFClientAnchor(0,0,0,0, (short)4, 2 ,(short) 6, 5));
					// comment.setString(new HSSFRichTextString(info));
					// 璁剧疆娉ㄩ噴鍐呭
					// cell.setCellComment(comment);
				}
			}
		}catch (IOException e){
			log.error(e.getMessage(), e);
		}

		userExcelFile.put(userFilekey, workbook);
		return userFilekey;
	}

	private static int getExcelCol(String col){
		col = col.toUpperCase();
		// 浠�-1寮�濮嬭绠�,瀛楁瘝閲�1寮�濮嬭繍绠椼�傝繖绉嶆�绘暟涓嬫潵绠楁暟姝ｅソ鐩稿悓銆�
		int count = -1;
		char[] cs = col.toCharArray();
		for (int i = 0; i < cs.length; i++){
			count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
		}
		return count;
	}

	private int[] getRowCellIndex(String str){
		int[] rc = new int[2];
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		int row = Integer.parseInt(m.replaceAll("").trim());
		int cell = getExcelCol(str.replaceAll(String.valueOf(row), ""));
		rc[0] = row;
		rc[1] = cell;
		return rc;
	}

	/**
	 * @author 浣曟尝
	 * @Description: 娓呴櫎鐢ㄦ埛閿欒淇℃伅鏂囦欢 void
	 * @throws
	 */
	private void claerUserFile(){
		if (userExcelFile.size() > 0){
			Set<String> keys = userExcelFile.keySet();
			for (String key : keys){
				String timeStr = key.split(":")[1];
				Long time = Long.parseLong(timeStr);
				Long ctime = System.currentTimeMillis();
				Long t = ctime - time;
				// 涓�灏忔椂
				if (t > (3600 * 1000)){
					userExcelFile.remove(key);
				}
			}
		}

	}

	/**
	 * @author ylzhang
	 * @Description: void
	 * @throws
	 */
	private static List<List<String>> buildTable(List<List<String>> table,List<List<String>> data,Integer dataRowIndex){
		List<List<String>> result = new ArrayList<List<String>>();

		if (table == null){
			table = result;
		}
		if (dataRowIndex >= data.size()){
			return table;
		}

		// 缁勭粐绗琲涓�"閿�鍞睘鎬�"
		List<String> dataRow = data.get(dataRowIndex);

		// 缁勭粐绗�1涓�"閿�鍞睘鎬�"鏃�
		if (table.size() == 0){
			for (String dataItem : dataRow){
				List<String> targetRow = new ArrayList<String>();
				targetRow.add(dataItem);
				result.add(targetRow);
			}
		}else{
			for (String dataItem : dataRow){
				for (List<String> tableLine : table){
					List<String> targetRow = (ArrayList<String>) ((ArrayList<String>) tableLine).clone();
					targetRow.add(dataItem);
					result.add(targetRow);
				}
			}
		}

		// 閫掑綊缁勭粐涓嬩竴琛屾暟鎹�
		return buildTable(result, data, ++dataRowIndex);
	}

	/**
	 * 灏嗘墍鏈夋瘡琛岀殑灞炴�ф嫾鎴恜rekey
	 * 
	 * @param result
	 * @return
	 */
	private List<String> buildPreKeys(List<List<String>> result){
		List<String> prekeys = new ArrayList<String>();
		for (List<String> lists : result){
			StringBuffer sbf = new StringBuffer();
			for (String list : lists){
				sbf.append(list + "_");
			}
			prekeys.add(sbf.toString());
		}

		return prekeys;
	}

	/**
	 * 鍟嗗搧棰勫敭缂栬緫
	 * 
	 * @param model
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "/item/preSaleEdit.htm")
	public String preSaleEdit(Model model,@RequestParam("itemId") Long itemId){
		ItemPresalseInfoCommand itemPresalseInfoCommand = itemPresaleInfoManager.getItemPresalseInfoCommand(itemId);
		model.addAttribute("itemPresalseInfoCommand", itemPresalseInfoCommand);
		return "/product/item/preSaleEdit";
	}

	/**
	 * @param itemCommand
	 * @param propertyValueIds
	 * @param categoriesIds
	 * @param iProperties
	 * @param propertyIds
	 * @param propertyValueInputs
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/item/updateItemProsaleInfo.json")
	@ResponseBody
	public Object updateItemProsaleInfo(ItemPresalseInfoCommand itemPresalseInfoCommand,HttpServletRequest request)
			throws Exception{
		BackWarnEntity backWarnEntity = new BackWarnEntity(true, null);
		boolean status=itemPresaleInfoManager.validateitemPresalseInfoCommand(itemPresalseInfoCommand);
		if(status){
			itemPresaleInfoManager.updateOrSaveItemPresalseInfo(itemPresalseInfoCommand);
		}else{
			backWarnEntity.setIsSuccess(false);
			 backWarnEntity.setDescription("鏁版嵁寮傚父淇濆瓨澶辫触!");
		}
		return backWarnEntity;
	}
	
//	//鑾峰彇鍟嗗搧灞炴�у�煎垎缁勪俊鎭�
//	private List<ItemProValGroupRelation> getItemProValueGroupRelation(HttpServletRequest request,Long[] propertyIds){
//		List<ItemProValGroupRelation> list = new ArrayList<ItemProValGroupRelation>();
//		if(propertyIds!=null && propertyIds.length>0){
//			for(Long propertyId:propertyIds){
//				String proValGroupId  = request.getParameter("proGroup_"+propertyId);
//				//瀛樺湪鍒嗙粍骞堕�夋嫨
//				if(proValGroupId !=null && proValGroupId.length()>0){
//					ItemProValGroupRelation relation = new ItemProValGroupRelation();
//					relation.setItemPropertyId(propertyId);
//					relation.setPropertyValueGroupId(Long.parseLong(proValGroupId));
//					list.add(relation);
//				}
//			}
//		}
//		return list;
//	}
//	
}
