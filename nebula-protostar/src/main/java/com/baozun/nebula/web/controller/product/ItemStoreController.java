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
package com.baozun.nebula.web.controller.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import loxia.dao.Sort;
import loxia.support.json.JSONArray;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.IndustryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.google.gson.Gson;

/**
 * @author jumbo description 库存管理
 * 
 */
@Controller
public class ItemStoreController extends BaseController {

	private final Logger		log					= LoggerFactory.getLogger(ItemStoreController.class);

	@Autowired
	private ItemManager			itemManager;
	@Autowired
	private ShopManager			shopManager;
	@Autowired
	private CategoryManager		categoryManager;
	@Autowired
	private IndustryManager		industryManager;
	@Autowired
	private ItemCategoryManager	itemCategoryManager;
	@Autowired
	private ChooseOptionManager	chooseOptionManager;
	@Autowired
	private SdkItemManager		sdkItemManager;
	@Autowired
	private PropertyManager		propertyManager;

	/**
	 * 上传图片的域名
	 */
	@Value("#{meta['upload.img.domain.base']}")
	private String				UPLOAD_IMG_DOMAIN	= "";

	@RequestMapping("/item/updateItemStore.htm")
	public String updateItemStore(@RequestParam("itemId") Long itemId, Model model) {

		// 分类列表
		Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);

		// 查找商品编码、所属行业
		Item item = itemManager.findItemById(Long.valueOf(itemId));
		ItemInfo itemInfo = itemManager.findItemInfoByItemId(itemId);
		// 根据行业Id查找行业
		Industry industry = industryManager.findIndustryById(item.getIndustryId());
		// 查找商品分类
		Long[] categoryIds;
		List<Category> categories = null;
		List<ItemCategory> itemCategoryList = itemCategoryManager.findItemCategoryListByItemId(Long.valueOf(itemId));
		int i = 0;
		if (itemCategoryList != null && itemCategoryList.size() > 0) {
			categoryIds = new Long[itemCategoryList.size()];
			for (ItemCategory itemCategory : itemCategoryList) {
				categoryIds[i] = itemCategory.getCategoryId();
				i++;
			}
			// 根据分类Id数组查询商品分类
			categories = categoryManager.findCategoryListByCategoryIds(categoryIds);
		}
		// 查找商品属性及属性值
		List<ItemProperties> itemProperties = itemManager.findItemPropertiesListyByItemId(Long.valueOf(itemId));
		// 根据行业Id和店铺Id查找对应属性和属性值
		List<DynamicPropertyCommand> dynamicPropertyCommandList = this.findDynamicPropertisByShopIdAndIndustryId(item
				.getIndustryId());
		List<Object> propertyIdArray = new ArrayList<Object>();
		List<Object> propertyNameArray = new ArrayList<Object>();
		List<Object> mustCheckArray = new ArrayList<Object>();

		if (dynamicPropertyCommandList != null && dynamicPropertyCommandList.size() > 0) {
			for (DynamicPropertyCommand dynamicPropertyCommand : dynamicPropertyCommandList) {
				Property property = dynamicPropertyCommand.getProperty();
				if (property.getIsSaleProp() && 
						(Property.EDITING_TYPE_MULTI_SELECT.equals(property.getEditingType()) 
								|| Property.EDITING_TYPE_CUSTOM_MULTI_SELECT.equals(property.getEditingType()))){
					propertyIdArray.add(property.getId());
					propertyNameArray.add("'" + property.getName() + "'");
				}
				if (!property.getIsSaleProp()&& dynamicPropertyCommand.getPropertyValueList().size() > 0) {
					if (property.getRequired() && property.getEditingType() == 4) {
						mustCheckArray.add("'" + property.getName() + "'");
					}
				}
			}
		}
		// 根据itemId查找Sku
		List<Sku> skuList = itemManager.findSkuByItemId(item.getId());
		List<Object> newSkuList = new ArrayList<Object>();
		// sku销售价
		List<BigDecimal> salePrices = new ArrayList<BigDecimal>();
		// sku销售价
		List<BigDecimal> listPrices = new ArrayList<BigDecimal>();
		
		// int tempStoreNum = 0;
		List<String> extentionCodeList = new ArrayList<String>();
		for (Sku sku : skuList) {
			extentionCodeList.add(sku.getOutid());
		}
		// 库存量
		List<SkuInventory> storeNum = sdkItemManager.findSkuInventoryByExtentionCodes(extentionCodeList);
		for (Sku sku : skuList) {
//			sku.setProperties(sku.getProperties());
			newSkuList.add(sku);
			if (sku.getSalePrice() != null) {
				salePrices.add(sku.getSalePrice());
			}
			if (sku.getListPrice() != null) {
				listPrices.add(sku.getListPrice());
			}
		}

		Gson sg = new Gson();
		String skuJaStr = sg.toJson(newSkuList);
		String itemPropertiesStr = sg.toJson(itemProperties);
		String storeNumStr = sg.toJson(storeNum);

		JSONArray dynamicPropertyCommandListJson = new JSONArray(dynamicPropertyCommandList, "***");
		String dynamicPropertyCommandListJsonStr = dynamicPropertyCommandListJson.toString();
		model.addAttribute("industry", industry);
		model.addAttribute("id", item.getId());
		model.addAttribute("code", item.getCode());
		model.addAttribute("title", itemInfo.getTitle());
		model.addAttribute("dynamicPropertyCommandList", dynamicPropertyCommandList);
		model.addAttribute("dynamicPropertyCommandListJsonStr", dynamicPropertyCommandListJsonStr);
		model.addAttribute("propertyIdArray", propertyIdArray);
		model.addAttribute("propertyNameArray", propertyNameArray);
		model.addAttribute("categories", categories);
		model.addAttribute("itemProperties", itemProperties);
		model.addAttribute("storeNumStr", storeNumStr);
		model.addAttribute("skuList", skuJaStr);
		model.addAttribute("salePrices", salePrices);
		model.addAttribute("listPrices", listPrices);
		model.addAttribute("itemPropertiesStr", itemPropertiesStr);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("mustCheckArray", mustCheckArray);
		return "/product/item/item-store";
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
		if (desc == null)
			return "";
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

	public String getFullDomainUrl(String domain, String imgStr) {
		String newUrl = "";
		if (null != imgStr && !imgStr.startsWith("http://")) {
			newUrl = domain + imgStr;
			return newUrl;
		} else {
			return imgStr;
		}
	}

	@RequestMapping("/item/saveSkuInventory.json")
	@ResponseBody
	public Object saveSkuInventory(@ArrayCommand(dataBind = true) String[] extentionCode, // extentionCode
			@ArrayCommand(dataBind = true) String[] availableQty,// 库存数量
			HttpServletRequest request) throws Exception {

		SkuInventory skuInventory = null;
		Integer available = 0;
		for (int i = 0; i < extentionCode.length; i++) {
			skuInventory = sdkItemManager.getSkuInventoryByExtentionCode(extentionCode[i]);
			available = "".equals(availableQty[i]) ? 0 : Integer.parseInt(availableQty[i]);
			if (skuInventory == null) {
				skuInventory = new SkuInventory();
			}
			skuInventory.setAvailableQty(available);
			skuInventory.setExtentionCode(extentionCode[i]);
			sdkItemManager.saveSkuInventory(skuInventory,getUserDetails().getUserId());
		}

		BackWarnEntity backWarnEntity = new BackWarnEntity(true, null);
		// backWarnEntity.setErrorCode(item.getId().intValue());
		return backWarnEntity;
	}
}
