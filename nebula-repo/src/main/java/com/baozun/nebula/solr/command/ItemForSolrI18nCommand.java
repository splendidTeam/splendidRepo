package com.baozun.nebula.solr.command;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;

public class ItemForSolrI18nCommand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 唯一值。其他id用于业务属性，仅仅用于作为标识solr索引的id。 **/
	@Field
	private Long id;
	
	/**
	 * 商品编码
	 */
	@Field
	private String code;
	
	/**
	 * 关键字
	 */
	@Field
	private String keyword;
	
	/**
	 * 商品名称
	 */
	@Field
	private String title;
	
	/**
	 * 动态属性值
	 */
	@Field("dynamic_title_*")
	private Map<String, String> dynamicTitle;
	
	/**
	 * 副标题
	 */
	@Field
	private String subTitle;
	
	/**
	 * 动态属性值
	 */
	@Field("dynamic_subTitle_*")
	private Map<String, String> dynamicSubTitle;
	
	/**
	 * 商品概述
	 */
	@Field
	private String sketch;
	
	/**
	 * 动态属性值
	 */
	@Field("dynamic_sketch_*")
	private Map<String, String> dynamicSketch;
	
	/**
	 * 商品详细描述
	 */
	@Field
	private String description;
	
	@Field("dynamic_description_*")
	private Map<String, String> dynamicDescription;
	
	@Field
	private String descriptionForSearch;
	
	@Field("dynamic_descriptionForSearch_*")
	private Map<String, String> dynamicDescriptionForSearch;
	

	/**
	 * 商店或供货商名称
	 */
	@Field
	private String shopName;
	
	/**
	 * 行业
	 */
	@Field
	private String industryName;
	
	/**
	 * 分组
	 */
	@Field
	private String style;
	
	@Field
	private String groupStyle;
	
	/**
	 * 商品在分组中顺序
	 */
	@Field
	private Integer sort_no;
	
	/**
	 * 默认排序
	 */
	@Field
	private Integer default_sort;
	
	/** 吊牌价 */
	@Field
	private Double list_price;
	
	/** 销售价 */
	@Field
	private Double sale_price;
	
	/**
	 * 修改时间
	 */
	@Field
	private Date modifyTime;
	
	/**
	 * 上架时间
	 */
	@Field
	private Date listTime;
	
	/**
	 * 活跃开始时间
	 */
	@Field
	private Date activeBeginTime;
	
	/**
	 * 销量
	 */
	@Field
	private Integer salesCount;
	
	/**
	 * 商品评分
	 */
	@Field
	private Float rankavg;
	
	/**
	 * 库存
	 */
	@Field
	private Integer itemCount;
	
	/**
	 * 浏览量
	 */
	@Field
	private Integer viewCount;
	
	/**
	 * 被收藏次数
	 */
	@Field
	private Integer favoredCount;
	
	/**
	 * seo搜索关键字
	 */
	@Field
	private String seoKeywords;
	
	@Field("dynamic_seoKeywords_*")
	private Map<String, String> dynamicSeoKeywords;
	
	/**
	 * seo搜索关键字
	 */
	@Field
	private String seoDescription;
	
	@Field("dynamic_seoDescription_*")
	private Map<String, String> dynamicSeoDescription;
	
	/**
	 * seoTitle
	 */
	@Field
	private String seoTitle;
	
	
	/**
	 * 动态属性值
	 */
	@Field("dynamic_seoTitle_*")
	private Map<String, String> dynamicSeoTitle;
	/**
	 * 图片或颜色
	 */
	@Field
	private List<String> imageUrl;
	
	@Field("dynamic_image_url_*")
	private Map<String,  List<String>> dynamicImageUrl;
	
	
	/**
	 * 通过activeBeanTime来判断是否显示
	 */
	//@Field
	private Boolean itemIsDisplay;

	/**
	 * 下架时间
	 */
	@Field
	private Date delistTime;

	/**
	 * 动态属性值
	 */
	@Field("dynamic_forsearchName_*")
	private Map<String, List<String>> dynamicNameForSearchMap;

	
	/** 可扩展的动态属性Map(用户可用关键字搜索) **/
	@Field("dynamic_forsearch_*")
	private Map<String, List<String>> dynamicForSearchMap;
	
	@Field("dynamic_withoutsearchName_*")
	private Map<String, List<String>> dynamicNameWithoutSearchMap;

	
	/** 可扩展的动态属性Map(用户不可用关键字搜索) **/
	@Field("dynamic_withoutsearch_*")
	private Map<String, List<String>> dynamicWithoutSearchMap;
	
	
	/** 可扩展的动态用户属性Map **/
	@Field("dynamic_customerValue_*")
	private Map<String, String> dynamicForCustomerMap;
	
	/**
	 * 分类排序
	 */
	@Field("category_order_*")
	private Map<String, Integer> categoryOrder;
	
	/**
	 * 分类父级
	 */
	@Field("category_Parent_*")
	private Map<String, Long> categoryParent;
	
	/**
	 * 分类
	 */
	@Field("category_name_*")
	private Map<String, String> categoryName;
	
	@Field("dynamic_category_name_*")
	private Map<String, String> dynamicCategoryName;
	
	
	/**
	 * 分类CODE
	 */
	@Field("category_code_*")
	private Map<String, String> categoryCode;
	
	/**
	 * 分类CODE集合
	 */
	@Field("all_category_codes")
	private List<String> allCategoryCodes;
	
	/**
	 * 分类ID集合
	 */
	@Field("all_category_ids")
	private List<Long> allCategoryIds;
	
	
	/** 拼音全拼 汉字在后**/
	@Field("pinyin_a")
	private List<String> pinyinAllList_A;
	
	/** 拼音全拼 汉字在前**/
	@Field("pinyin_b")
	private List<String> pinyinAllList_B;
	
	/**
	 * 该商品的分类树
	 * 数据格式为：
	 * 
	 * 		1
	 * 		1-11
	 * 		1-11-1101
	 */
	@Field("category_tree")
	private List<String>	categoryTree;
	
	/**
	 * 该商品对应的导航树
	 * 数据格式为：
	 * 
	 * 		1
	 * 		1-11
	 * 		1-11-1101 	
	 */
	@Field("navigation_tree")
	private List<String>	navigationTree;
	
	
	/**
	 * 渠道
	 */
	@Field
	private List<String> channels;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getSketch() {
		return sketch;
	}

	public void setSketch(String sketch) {
		this.sketch = sketch;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}


	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}


	public Double getList_price() {
		return list_price;
	}

	public void setList_price(Double list_price) {
		this.list_price = list_price;
	}

	public Double getSale_price() {
		return sale_price;
	}

	public void setSale_price(Double sale_price) {
		this.sale_price = sale_price;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getListTime() {
		return listTime;
	}

	public void setListTime(Date listTime) {
		this.listTime = listTime;
	}

	public Date getDelistTime() {
		return delistTime;
	}

	public void setDelistTime(Date delistTime) {
		this.delistTime = delistTime;
	}


	public Map<String, List<String>> getDynamicForSearchMap() {
		return dynamicForSearchMap;
	}

	public void setDynamicForSearchMap(Map<String, List<String>> dynamicForSearchMap) {
		this.dynamicForSearchMap = dynamicForSearchMap;
	}

	public List<String> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(List<String> imageUrl) {
		this.imageUrl = imageUrl;
	}


	public Map<String, Integer> getCategoryOrder() {
		return categoryOrder;
	}

	public void setCategoryOrder(Map<String, Integer> categoryOrder) {
		this.categoryOrder = categoryOrder;
	}

	public Map<String, String> getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(Map<String, String> categoryName) {
		this.categoryName = categoryName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<String> getChannels() {
		return channels;
	}

	public void setChannels(List<String> channels) {
		this.channels = channels;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}
	
	public Integer getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	
	public Float getRankavg() {
		return rankavg;
	}

	public void setRankavg(Float rankavg) {
		this.rankavg = rankavg;
	}

	public Date getActiveBeginTime() {
		return activeBeginTime;
	}

	public void setActiveBeginTime(Date activeBeginTime) {
		this.activeBeginTime = activeBeginTime;
	}

	public Boolean getItemIsDisplay() {
		return itemIsDisplay;
	}

	public void setItemIsDisplay(Boolean itemIsDisplay) {
		this.itemIsDisplay = itemIsDisplay;
	}

	public Map<String, List<String>> getDynamicNameForSearchMap() {
		return dynamicNameForSearchMap;
	}

	public void setDynamicNameForSearchMap(
			Map<String, List<String>> dynamicNameForSearchMap) {
		this.dynamicNameForSearchMap = dynamicNameForSearchMap;
	}


	public Map<String, String> getDynamicForCustomerMap() {
		return dynamicForCustomerMap;
	}

	public void setDynamicForCustomerMap(Map<String, String> dynamicForCustomerMap) {
		this.dynamicForCustomerMap = dynamicForCustomerMap;
	}

	public Map<String, Long> getCategoryParent() {
		return categoryParent;
	}

	public void setCategoryParent(Map<String, Long> categoryParent) {
		this.categoryParent = categoryParent;
	}


	public List<String> getPinyinAllList_A() {
		return pinyinAllList_A;
	}

	public void setPinyinAllList_A(List<String> pinyinAllList_A) {
		this.pinyinAllList_A = pinyinAllList_A;
	}

	public List<String> getPinyinAllList_B() {
		return pinyinAllList_B;
	}

	public void setPinyinAllList_B(List<String> pinyinAllList_B) {
		this.pinyinAllList_B = pinyinAllList_B;
	}


	public Map<String, String> getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Map<String, String> categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setGroupStyle(String groupStyle) {
		this.groupStyle = groupStyle;
	}

	public String getGroupStyle() {
		return groupStyle;
	}

	public void setSort_no(Integer sort_no) {
		this.sort_no = sort_no;
	}

	public Integer getSort_no() {
		return sort_no;
	}

	public Integer getDefault_sort() {
		return default_sort;
	}

	public void setDefault_sort(Integer defaultSort) {
		default_sort = defaultSort;
	}

	public void setFavoredCount(Integer favoredCount) {
		this.favoredCount = favoredCount;
	}

	public Integer getFavoredCount() {
		return favoredCount;
	}

	public void setDescriptionForSearch(String descriptionForSearch) {
		this.descriptionForSearch = descriptionForSearch;
	}

	public String getDescriptionForSearch() {
		return descriptionForSearch;
	}

	public Map<String, List<String>> getDynamicNameWithoutSearchMap() {
		return dynamicNameWithoutSearchMap;
	}

	public void setDynamicNameWithoutSearchMap(
			Map<String, List<String>> dynamicNameWithoutSearchMap) {
		this.dynamicNameWithoutSearchMap = dynamicNameWithoutSearchMap;
	}

	public Map<String, List<String>> getDynamicWithoutSearchMap() {
		return dynamicWithoutSearchMap;
	}

	public void setDynamicWithoutSearchMap(
			Map<String, List<String>> dynamicWithoutSearchMap) {
		this.dynamicWithoutSearchMap = dynamicWithoutSearchMap;
	}

	public Map<String, String> getDynamicTitle() {
		return dynamicTitle;
	}

	public void setDynamicTitle(Map<String, String> dynamicTitle) {
		this.dynamicTitle = dynamicTitle;
	}

	public Map<String, String> getDynamicSubTitle() {
		return dynamicSubTitle;
	}

	public void setDynamicSubTitle(Map<String, String> dynamicSubTitle) {
		this.dynamicSubTitle = dynamicSubTitle;
	}

	public Map<String, String> getDynamicSketch() {
		return dynamicSketch;
	}

	public void setDynamicSketch(Map<String, String> dynamicSketch) {
		this.dynamicSketch = dynamicSketch;
	}

	public Map<String, String> getDynamicSeoTitle() {
		return dynamicSeoTitle;
	}

	public void setDynamicSeoTitle(Map<String, String> dynamicSeoTitle) {
		this.dynamicSeoTitle = dynamicSeoTitle;
	}

	public Map<String, String> getDynamicCategoryName() {
		return dynamicCategoryName;
	}

	public void setDynamicCategoryName(Map<String, String> dynamicCategoryName) {
		this.dynamicCategoryName = dynamicCategoryName;
	}

	public Map<String, String> getDynamicDescriptionForSearch() {
		return dynamicDescriptionForSearch;
	}

	public void setDynamicDescriptionForSearch(
			Map<String, String> dynamicDescriptionForSearch) {
		this.dynamicDescriptionForSearch = dynamicDescriptionForSearch;
	}

	public Map<String, String> getDynamicDescription() {
		return dynamicDescription;
	}

	public void setDynamicDescription(Map<String, String> dynamicDescription) {
		this.dynamicDescription = dynamicDescription;
	}

	public Map<String, String> getDynamicSeoKeywords() {
		return dynamicSeoKeywords;
	}

	public void setDynamicSeoKeywords(Map<String, String> dynamicSeoKeywords) {
		this.dynamicSeoKeywords = dynamicSeoKeywords;
	}

	public Map<String, String> getDynamicSeoDescription() {
		return dynamicSeoDescription;
	}

	public void setDynamicSeoDescription(
			Map<String, String> dynamicSeoDescription) {
		this.dynamicSeoDescription = dynamicSeoDescription;
	}

	public Map<String, List<String>> getDynamicImageUrl() {
		return dynamicImageUrl;
	}

	public void setDynamicImageUrl(Map<String, List<String>> dynamicImageUrl) {
		this.dynamicImageUrl = dynamicImageUrl;
	}

	public List<String> getAllCategoryCodes() {
		return allCategoryCodes;
	}

	public void setAllCategoryCodes(List<String> allCategoryCodes) {
		this.allCategoryCodes = allCategoryCodes;
	}

	public List<Long> getAllCategoryIds() {
		return allCategoryIds;
	}

	public void setAllCategoryIds(List<Long> allCategoryIds) {
		this.allCategoryIds = allCategoryIds;
	}

	public List<String> getCategoryTree() {
		return categoryTree;
	}

	public List<String> getNavigationTree() {
		return navigationTree;
	}

	public void setCategoryTree(List<String> categoryTree) {
		this.categoryTree = categoryTree;
	}

	public void setNavigationTree(List<String> navigationTree) {
		this.navigationTree = navigationTree;
	}
}
