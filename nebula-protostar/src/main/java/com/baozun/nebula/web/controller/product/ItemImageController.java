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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.product.ItemImageLangCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品图片管理controller
 */
@Controller
public class ItemImageController extends BaseController {

	@SuppressWarnings("unused")
	private static final Logger	log						= LoggerFactory.getLogger(ItemImageController.class);

	@Autowired
	private ItemManager			itemManager;

	@Autowired
	private ShopManager			shopManager;
	
	@Autowired
	private ItemSolrManager		itemSolrManager;

	@Autowired
	private ChooseOptionManager	chooseOptionManager;

	@Autowired
	private SdkMataInfoManager	sdkMataInfoManager;

	/** 缩略图规格 */
	private static final String	THUMBNAIL_CONFIG		= "THUMBNAIL_CONFIG";
	/** 图片类型 */
	private static final String	IMAGE_TYPE				= "IMAGE_TYPE";
	/** 是否通过图片类型分组显示  */
	private static final String	IS_IMAGE_TYPE_GROUP		= "IS_IMAGE_TYPE_GROUP";

	/** 商品没有颜色属性的ItemPropertiesID */
	private static final Long	ITEM_NO_COLOR_PROP		= 0L;
	/**
	 * 上传图片的根目录
	 */
	@Value("#{meta['upload.img.base']}")
	private String				uploadImgBase			= "";

	/**
	 * 上传图片的根临时目录
	 */
	@Value("#{meta['upload.img.base.tmp']}")
	private String				uploadImgBaseTmp		= "";

	/**
	 * 上传图片的域名
	 */
	@Value("#{meta['upload.img.domain.base']}")
	private String				UPLOAD_IMG_DOMAIN		= "";

	/**
	 * 到商品图片管理页面
	 * 
	 * @param itemId
	 * @param model
	 * @return :String
	 * @date 2014-3-4 下午08:25:12
	 */
	@RequestMapping("/itemImage/toAddItemImage.htm")
	public String toAddItemImage(@RequestParam("itemId") Long itemId, Model model) {
		Item item = itemManager.findItemById(itemId);
		ItemInfo itemInfo = itemManager.findItemInfoByItemId(itemId);
		// 根据行业Id和店铺Id查找对应属性和属性值
		List<DynamicPropertyCommand> dynamicPropertyCommandList = this.findDynamicPropertisByShopIdAndIndustryId(item
				.getIndustryId());
		// 查找商品属性及属性值
		List<ItemProperties> itemPropertiesList = itemManager.findItemPropertiesListyByItemId(Long.valueOf(itemId));
		// 缩略图规格
		List<ChooseOption> thumbnailConfig = chooseOptionManager
				.findEffectChooseOptionListByGroupCode(THUMBNAIL_CONFIG);
		
		
		
		List<ChooseOption> imageTypeList = chooseOptionManager.findEffectChooseOptionListByGroupCode(IMAGE_TYPE);
		
		//图片类型-尺寸集合
		List<String> size =null;
		Map<String,List<String>> typeSizeMap =new HashMap<String, List<String>>();
		for (ChooseOption typeOp : imageTypeList) {
			for (ChooseOption thumbOp : thumbnailConfig) {
				if(thumbOp.getOptionLabel().endsWith(typeOp.getOptionValue())){
					size =new ArrayList<String>();
					for (String sz : thumbOp.getOptionValue().split("\\|")) {
						size.add(sz);
					}
					typeSizeMap.put(thumbOp.getOptionLabel(), size);
				}
			}
		}
		
		
		
		Map<String, List<ItemImage>> imageMapByType = new HashMap<String, List<ItemImage>>();
		Map<String, String> imageTypeMap = new HashMap<String, String>();
		for (ChooseOption imageType : imageTypeList) {
			String type = imageType.getOptionValue();
			if (!imageMapByType.containsKey(type)) {
				imageMapByType.put(type, new ArrayList<ItemImage>());
			}
			imageTypeMap.put(type, imageType.getOptionLabel());
		}

		/** 商品是否存在颜色属性 */
		/** 商品颜色属性Map */
		Map<Property, List<PropertyValue>> propertyValueMapByProperty = new HashMap<Property, List<PropertyValue>>();
		boolean hasColorProp = false;
		for (DynamicPropertyCommand dynamicPropertyCommand : dynamicPropertyCommandList) {
			Property property = dynamicPropertyCommand.getProperty();
			if (property.getIsColorProp()) {
				hasColorProp = true;
				propertyValueMapByProperty.put(property, dynamicPropertyCommand.getPropertyValueList());
			}
		}

		List<ItemImage> itemImageList = itemManager.findItemImageByItemPropAndItemId(null, itemId, null);

		Map<Long, Map<String, List<ItemImage>>> imageListByTypeByItemPropertiesId = new HashMap<Long, Map<String, List<ItemImage>>>();
		Map<Long, List<ItemImage>> imageMapByItemPropertiesId = findItemColorProperties(dynamicPropertyCommandList,
				itemPropertiesList, itemImageList, propertyValueMapByProperty);

		for (Map.Entry<Long, List<ItemImage>> entry : imageMapByItemPropertiesId.entrySet()) {
			Long key = entry.getKey();
			Map<String, List<ItemImage>> tmpImageMapByType = processItemImageData(entry.getValue(), imageMapByType);
			imageListByTypeByItemPropertiesId.put(key, tmpImageMapByType);
		}

		model.addAttribute("hasColorProp", hasColorProp);
		if (hasColorProp) {
			model.addAttribute("propertyValueMapByProperty", propertyValueMapByProperty);
		}
		String isImageTypeGroupStr = sdkMataInfoManager.findValue(IS_IMAGE_TYPE_GROUP);
		model.addAttribute("isImageTypeGroup", Boolean.valueOf(isImageTypeGroupStr));
		
		model.addAttribute("imageListByTypeByItemPropertiesId", imageListByTypeByItemPropertiesId);
		model.addAttribute("thumbnailConfig", thumbnailConfig);
		model.addAttribute("imageTypeMap", imageTypeMap);
		model.addAttribute("thumbnailConfigStr", JsonFormatUtil.format(thumbnailConfig));
		model.addAttribute("itemInfo", itemInfo);
		model.addAttribute("item", item);
		model.addAttribute("itemProperties", itemPropertiesList);
		model.addAttribute("baseImageUrl", UPLOAD_IMG_DOMAIN);
		model.addAttribute("typeSizeMap", typeSizeMap);
		
		return "/product/item/item-image";
	}

	/**
	 * 查询商品的颜色属性
	 * 
	 * @return
	 */
	private Map<Long, List<ItemImage>> findItemColorProperties(List<DynamicPropertyCommand> dynamicPropertyCommandList,
			List<ItemProperties> itemPropertiesList, List<ItemImage> itemImageList,
			Map<Property, List<PropertyValue>> propertyValueMapByProperty) {
		Map<Long, List<ItemImage>> imageMapByItemPropertiesId = new HashMap<Long, List<ItemImage>>();

		/** 存在颜色属性 */
		for (Map.Entry<Property, List<PropertyValue>> entry : propertyValueMapByProperty.entrySet()) {
			for (ItemProperties itemProperties : itemPropertiesList) {
				Long itemPropertiesId = itemProperties.getId();
				if (null != itemPropertiesId && itemProperties.getPropertyId().equals(entry.getKey().getId())) {
					if (!imageMapByItemPropertiesId.containsKey(itemPropertiesId)) {
						imageMapByItemPropertiesId.put(itemProperties.getId(), new ArrayList<ItemImage>());
					}
				}
			}
		}

		/** 没有颜色属性 */
		if (null == propertyValueMapByProperty || propertyValueMapByProperty.size() <= 0) {
			imageMapByItemPropertiesId.put(ITEM_NO_COLOR_PROP, new ArrayList<ItemImage>());
		}

		/** 当商品没有颜色属性时, map的key设为0L */
		for (Map.Entry<Long, List<ItemImage>> entry : imageMapByItemPropertiesId.entrySet()) {
			Long key = entry.getKey();
			List<ItemImage> tmpImageList = new ArrayList<ItemImage>();
			for (ItemImage itemImage : itemImageList) {
				if (ITEM_NO_COLOR_PROP.equals(key) || key.equals(itemImage.getItemProperties())) {
					tmpImageList = entry.getValue();
					tmpImageList.add(itemImage);
				}
			}
			imageMapByItemPropertiesId.put(key, tmpImageList);
		}
		return imageMapByItemPropertiesId;
	}

	@RequestMapping("/i18n/itemImage/toAddItemImage.htm")
	public String toAddItemImageI18n(@RequestParam("itemId") Long itemId, Model model) {
		Item item = itemManager.findItemById(itemId);
		ItemInfo itemInfo = itemManager.findItemInfoByItemId(itemId);
		// 根据行业Id和店铺Id查找对应属性和属性值
		List<DynamicPropertyCommand> dynamicPropertyCommandList = this.findDynamicPropertisByShopIdAndIndustryId(item
				.getIndustryId());
		// 查找商品属性及属性值
		List<ItemProperties> itemPropertiesList = itemManager.findItemPropertiesListyByItemId(Long.valueOf(itemId));
		// 缩略图规格
		List<ChooseOption> thumbnailConfig = chooseOptionManager
				.findEffectChooseOptionListByGroupCode(THUMBNAIL_CONFIG);
		List<ChooseOption> imageTypeList = chooseOptionManager.findEffectChooseOptionListByGroupCode(IMAGE_TYPE);
		
		//图片类型-尺寸集合
		List<String> size =null;
		Map<String,List<String>> typeSizeMap =new HashMap<String, List<String>>();
		for (ChooseOption typeOp : imageTypeList) {
			for (ChooseOption thumbOp : thumbnailConfig) {
				if(thumbOp.getOptionLabel().endsWith(typeOp.getOptionValue())){
					size =new ArrayList<String>();
					for (String sz : thumbOp.getOptionValue().split("\\|")) {
						size.add(sz);
					}
					typeSizeMap.put(thumbOp.getOptionLabel(), size);
				}
			}
		}
		
		
		Map<String, List<ItemImageLangCommand>> imageMapByType = new HashMap<String, List<ItemImageLangCommand>>();
		Map<String, String> imageTypeMap = new HashMap<String, String>();
		for (ChooseOption imageType : imageTypeList) {
			String type = imageType.getOptionValue();
			if (!imageMapByType.containsKey(type)) {
				imageMapByType.put(type, new ArrayList<ItemImageLangCommand>());
			}
			imageTypeMap.put(type, imageType.getOptionLabel());
		}

		/** 商品是否存在颜色属性 */
		/** 商品颜色属性Map */
		Map<Property, List<PropertyValue>> propertyValueMapByProperty = new HashMap<Property, List<PropertyValue>>();
		boolean hasColorProp = false;
		for (DynamicPropertyCommand dynamicPropertyCommand : dynamicPropertyCommandList) {
			Property property = dynamicPropertyCommand.getProperty();
			if (property.getIsColorProp()) {
				hasColorProp = true;
				propertyValueMapByProperty.put(property, dynamicPropertyCommand.getPropertyValueList());
			}
		}

		List<ItemImageLangCommand> itemImageList = itemManager.findItemImageByItemPropAndItemIdI18n(null, itemId, null);

		Map<Long, Map<String, List<ItemImageLangCommand>>> imageListByTypeByItemPropertiesId = new HashMap<Long, Map<String, List<ItemImageLangCommand>>>();
		Map<Long, List<ItemImageLangCommand>> imageMapByItemPropertiesId = findItemColorPropertiesI18n(dynamicPropertyCommandList,
				itemPropertiesList, itemImageList, propertyValueMapByProperty);

		for (Map.Entry<Long, List<ItemImageLangCommand>> entry : imageMapByItemPropertiesId.entrySet()) {
			Long key = entry.getKey();
			Map<String, List<ItemImageLangCommand>> tmpImageMapByType = processItemImageDataI18n(entry.getValue(), imageMapByType);
			imageListByTypeByItemPropertiesId.put(key, tmpImageMapByType);
		}

		model.addAttribute("hasColorProp", hasColorProp);
		if (hasColorProp) {
			model.addAttribute("propertyValueMapByProperty", propertyValueMapByProperty);
		}
		String isImageTypeGroupStr = sdkMataInfoManager.findValue(IS_IMAGE_TYPE_GROUP);
		model.addAttribute("isImageTypeGroup", Boolean.valueOf(isImageTypeGroupStr));
		
		model.addAttribute("imageListByTypeByItemPropertiesId", imageListByTypeByItemPropertiesId);
		model.addAttribute("thumbnailConfig", thumbnailConfig);
		model.addAttribute("imageTypeMap", imageTypeMap);
		model.addAttribute("thumbnailConfigStr", JsonFormatUtil.format(thumbnailConfig));
		model.addAttribute("itemInfo", itemInfo);
		model.addAttribute("item", item);
		model.addAttribute("itemProperties", itemPropertiesList);
		model.addAttribute("baseImageUrl", UPLOAD_IMG_DOMAIN);
		model.addAttribute("typeSizeMap", typeSizeMap);
		return "/product/item/item-image";
	}
	private Map<Long, List<ItemImageLangCommand>> findItemColorPropertiesI18n(List<DynamicPropertyCommand> dynamicPropertyCommandList,
			List<ItemProperties> itemPropertiesList, List<ItemImageLangCommand> itemImageList,
			Map<Property, List<PropertyValue>> propertyValueMapByProperty) {
		Map<Long, List<ItemImageLangCommand>> imageMapByItemPropertiesId = new HashMap<Long, List<ItemImageLangCommand>>();

		/** 存在颜色属性 */
		for (Map.Entry<Property, List<PropertyValue>> entry : propertyValueMapByProperty.entrySet()) {
			for (ItemProperties itemProperties : itemPropertiesList) {
				Long itemPropertiesId = itemProperties.getId();
				if (null != itemPropertiesId && itemProperties.getPropertyId().equals(entry.getKey().getId())) {
					if (!imageMapByItemPropertiesId.containsKey(itemPropertiesId)) {
						imageMapByItemPropertiesId.put(itemProperties.getId(), new ArrayList<ItemImageLangCommand>());
					}
				}
			}
		}

		/** 没有颜色属性 */
		if (null == propertyValueMapByProperty || propertyValueMapByProperty.size() <= 0) {
			imageMapByItemPropertiesId.put(ITEM_NO_COLOR_PROP, new ArrayList<ItemImageLangCommand>());
		}

		/** 当商品没有颜色属性时, map的key设为0L */
		for (Map.Entry<Long, List<ItemImageLangCommand>> entry : imageMapByItemPropertiesId.entrySet()) {
			Long key = entry.getKey();
			List<ItemImageLangCommand> tmpImageList = new ArrayList<ItemImageLangCommand>();
			if(itemImageList!=null){
				for (ItemImageLangCommand itemImage : itemImageList) {
					if (ITEM_NO_COLOR_PROP.equals(key) || key.equals(itemImage.getItemProperties())) {
						tmpImageList = entry.getValue();
						tmpImageList.add(itemImage);
					}
				}
			}
			
			imageMapByItemPropertiesId.put(key, tmpImageList);
		}
		return imageMapByItemPropertiesId;
	}
	/**
	 * 处理商品图片数据, 通过商品图片类型分组
	 * 
	 * @param itemImageList
	 * @param imageMapByType
	 *            - key: 图片类型, value: 图片集合
	 * @return
	 */
	private Map<String, List<ItemImage>> processItemImageData(List<ItemImage> itemImageList,
			Map<String, List<ItemImage>> imageMapByType) {
		Map<String, List<ItemImage>> tmpImageMapByType = new HashMap<String, List<ItemImage>>();

		for (Map.Entry<String, List<ItemImage>> entry : imageMapByType.entrySet()) {
			tmpImageMapByType.put(entry.getKey(), new ArrayList<ItemImage>());
		}

		Set<String> typeSet = new HashSet<String>();
		for (ItemImage itemImage : itemImageList) {
			String type = itemImage.getType();
			List<ItemImage> tmpImageList = tmpImageMapByType.get(type);
			if (null == tmpImageList) {
				throw new BusinessException(ErrorCodes.ITEM_IMAGE_TYPE_NOT_EXISTS, new Object[] { type });
			}
			tmpImageList.add(itemImage);
			tmpImageMapByType.put(type, tmpImageList);
			typeSet.add(type);
		}

		for (Map.Entry<String, List<ItemImage>> entry : imageMapByType.entrySet()) {
			if (!typeSet.contains(entry.getKey())) {
				tmpImageMapByType.put(entry.getKey(), entry.getValue());
			}
		}

		return tmpImageMapByType;
	}
	
	private Map<String, List<ItemImageLangCommand>> processItemImageDataI18n(List<ItemImageLangCommand> itemImageList,
			Map<String, List<ItemImageLangCommand>> imageMapByType) {
		Map<String, List<ItemImageLangCommand>> tmpImageMapByType = new HashMap<String, List<ItemImageLangCommand>>();

		for (Map.Entry<String, List<ItemImageLangCommand>> entry : imageMapByType.entrySet()) {
			tmpImageMapByType.put(entry.getKey(), new ArrayList<ItemImageLangCommand>());
		}

		Set<String> typeSet = new HashSet<String>();
		for (ItemImageLangCommand itemImage : itemImageList) {
			String type = itemImage.getType();
			List<ItemImageLangCommand> tmpImageList = tmpImageMapByType.get(type);
			if (null == tmpImageList) {
				throw new BusinessException(ErrorCodes.ITEM_IMAGE_TYPE_NOT_EXISTS, new Object[] { type });
			}
			tmpImageList.add(itemImage);
			tmpImageMapByType.put(type, tmpImageList);
			typeSet.add(type);
		}

		for (Map.Entry<String, List<ItemImageLangCommand>> entry : imageMapByType.entrySet()) {
			if (!typeSet.contains(entry.getKey())) {
				tmpImageMapByType.put(entry.getKey(), entry.getValue());
			}
		}

		return tmpImageMapByType;
	}

	/**
	 * 保存商品图片
	 * 
	 * @param changePropertyJson
	 * @param itemId
	 * @return :Object
	 * @date 2014-3-4 下午08:25:00
	 */
	@RequestMapping("/itemImage/saveItemImage.json")
	@ResponseBody
	public Object saveItemImage(@ArrayCommand() ItemImage[] itemImages, @RequestParam("itemId") Long itemId) {
		String isImageTypeGroupStr = sdkMataInfoManager.findValue(IS_IMAGE_TYPE_GROUP);
		itemManager.createOrUpdateItemImage(itemImages, itemId, UPLOAD_IMG_DOMAIN, Boolean.valueOf(isImageTypeGroupStr));
		
		// 上架商品才刷新商品索引
		Item item = itemManager.findItemById(itemId);
		if(Item.LIFECYCLE_ENABLE.equals(item.getLifecycle())){
			List<Long> itemIdsForSolr = new ArrayList<Long>();
			itemIdsForSolr.add(itemId);
			Boolean isSuccess = itemSolrManager.saveOrUpdateItem(itemIdsForSolr);
			if(!isSuccess){
				throw new BusinessException(ErrorCodes.SOLR_SETTING_UPDATE_FAIL);
			}
		}
		return SUCCESS;
	}

	@RequestMapping("/i18n/itemImage/saveItemImage.json")
	@ResponseBody
	public Object saveItemImageI18n(@I18nCommand ItemImageLangCommand[] itemImages, @RequestParam("itemId") Long itemId) {
		String isImageTypeGroupStr = sdkMataInfoManager.findValue(IS_IMAGE_TYPE_GROUP);
		itemManager.createOrUpdateItemImageIi18n(itemImages, itemId, UPLOAD_IMG_DOMAIN, Boolean.valueOf(isImageTypeGroupStr));
		return SUCCESS;
	}
	/**
	 * 通过ItemId删除商品图片
	 * 
	 * @param itemId
	 * @return :Object
	 */
	@RequestMapping("/itemImage/removeItemImageByItemId.json")
	@ResponseBody
	public Object removeItemImageByItemId(@RequestParam("itemId") Long itemId) {
		itemManager.removeItemImageByItemId(itemId);
		return SUCCESS;
	}

	/**
	 * 通过行业id查询动态属性
	 * 
	 * @param industryId
	 * @return
	 */
	public List<DynamicPropertyCommand> findDynamicPropertisByShopIdAndIndustryId(Long industryId) {
		// 查询orgId
		UserDetails userDetails = this.getUserDetails();

		ShopCommand shopCommand = null;
		Long shopId = 0L;

		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 根据orgId查询shopId
		if (currentOrgId != null) {
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();
		}
		// 根据行业Id和店铺Id查找对应属性和属性值
		List<DynamicPropertyCommand> dynamicPropertyCommandList = itemManager.findDynamicPropertis(shopId, industryId);
		return dynamicPropertyCommandList;
	}

	// 将数据库中取出来的 商品描述中的本地服务器地址加上domain
	public String addDefinedDomainInDesc(String desc, String defineDomain) {
		Document doc = Jsoup.parse(desc);
		Elements es = doc.select("img");
		for (int i = 0; i < es.size(); i++) {
			Element e = es.get(i);
			// System.out.println(e.attr("src"));
			String imgStr = e.attr("src");

			e.attr("src", getFullDomainUrl(defineDomain, imgStr.trim()));
		}
		desc = doc.select("body").html();
		return desc;
	}

	// 将传过来的商品描述中的 中图像地址 （如果是本地上传，则改为去掉服务器域名）
	public String removeDefinedDomainInDesc(String desc, String defineDomain) {
		Document doc = Jsoup.parse(desc);
		Elements es = doc.select("img");
		for (int i = 0; i < es.size(); i++) {
			Element e = es.get(i);
			// System.out.println(e.attr("src"));
			String imgStr = e.attr("src");

			e.attr("src", getRemoveDomainUrl(defineDomain, imgStr.trim()));
		}
		desc = doc.select("body").html();
		return desc;
	}

	public String getRemoveDomainUrl(String domain, String imgStr) {
		String newUrl = "";
		if (null != imgStr && imgStr.startsWith(domain)) {
			newUrl = imgStr.replace(domain, "");
			return newUrl;
		} else {
			return imgStr;
		}

	}

	public String getFullDomainUrl(String domain, String imgStr) {
		String newUrl = "";
		if (null != imgStr && !imgStr.startsWith("http://")) {
			newUrl = domain + imgStr;
			return newUrl;
		} else {
			return imgStr;
		}
	}

	/**
	 * 通过PropertyValueId查询ItemImage
	 * 
	 * @param propertyValueId
	 * @return :Object
	 * @date 2014-1-23 上午10:37:51
	 */
	@RequestMapping("/itemImage/findItemImageByItemPropAndItemId.json")
	@ResponseBody
	public Object findItemImageByItemPropAndItemId(@RequestParam("itemProperties") String itemProperties,
			@RequestParam("itemId") Long itemId,
			@RequestParam(required = false, value = "propertyValueId") Long propertyValueId) {
		BackWarnEntity result = new BackWarnEntity();
		List<ItemImage> itemImageList = itemManager.findItemImageByItemPropAndItemId(itemProperties, itemId,
				propertyValueId);
		result.setDescription(itemImageList);
		result.setIsSuccess(true);
		return result;
	}

	// ***************************批量上传商品图片*****************************************
	/**
	 * 到上传商品图片的页面
	 * 
	 * @return
	 */
	@RequestMapping("/itemImage/toImportSkuImg.htm")
	public String toBatchImportItemImg(Model model) {
		String defaultUploadType = sdkMataInfoManager.findValue(MataInfo.KEY_DEFAULT_ITEM_IMAGE_UPLOAD_TYPE);
		model.addAttribute("defaultUploadType", defaultUploadType);
		return "/product/item/import-item-image";
	}

	/**
	 * 处理上传的商品图片
	 * 
	 * @param itemImgFile
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/itemImage/uploadItemImgZip.json", method = RequestMethod.POST)
	@ResponseBody
	public Object uploadItemImgZip(
			@RequestParam(value = "itemImgFile", required = false) CommonsMultipartFile itemImgFile,
			@RequestParam("type") String uploadType, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询orgId
		UserDetails userDetails = this.getUserDetails();

		ShopCommand shopCommand = null;
		Long shopId = 0L;

		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 根据orgId查询shopId
		if (currentOrgId != null) {
			shopCommand = shopManager.findShopByOrgId(currentOrgId);
			shopId = shopCommand.getShopid();
		}

		String path = uploadImgBase;
		String tmpPath = uploadImgBaseTmp;
		String fileName = itemImgFile.getFileItem().getName();
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}

		File tmp = new File(tmpPath);
		if (!tmp.exists()) {
			tmp.mkdirs();
		}
		File zipFile = new File(tmpPath + "/" + fileName);
		itemImgFile.getFileItem().write(zipFile);
		List<Long> itemIds = itemManager.importItemImgFromFile(path, zipFile, shopId, uploadType);
		
		if(Validator.isNotNullOrEmpty(itemIds)){
			Boolean isSuccess = Boolean.FALSE;
			boolean i18n = LangProperty.getI18nOnOff();
			if(i18n){
				isSuccess = itemSolrManager.saveOrUpdateItemI18n(itemIds);
			}else{
				isSuccess = itemSolrManager.saveOrUpdateItem(itemIds);
			}
			
			if(!isSuccess){
				throw new BusinessException(ErrorCodes.SOLR_SETTING_UPDATE_FAIL);
			}
		}
	 
		result.put("data", "success");
		return result;
	}
}
