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

package com.baozun.nebula.web.controller.baseinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.command.baseinfo.NavigationCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.baseinfo.NavigationManager;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.PropertyManager;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemCollection;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkItemCollectionManager;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.FacetParameter;
import com.baozun.nebula.search.FacetType;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.search.convert.SolrQueryConvert;
import com.baozun.nebula.search.manager.SearchManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.factory.NebulaSolrQueryFactory;
import com.baozun.nebula.solr.utils.FilterUtil;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.command.DynamicPropertyCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

import loxia.dao.Sort;

/**
 * 菜单导航管理
 * 
 * @author - 项硕
 */
@Controller
public class NavigationController extends BaseController{

	private static final Logger			log	= LoggerFactory.getLogger(NavigationController.class);
	/** 逗号分隔符 */
	private final static String		SEPARATORCHARS_COMMA	= ",";

	@Autowired
	private NavigationManager			navigationManager;

	@Autowired
	private CategoryManager				categoryManager;

	@Autowired
	private PropertyManager				propertyManager;

	@Autowired
	private SdkItemCollectionManager sdkItemCollectionManager;
	
	@Autowired
	private SearchManager				searchManager;
	
	//protostar只提供默认转定，每个商城需要自定义转换类
	private  SolrQueryConvert		solrQueryConvert =null;
	
	protected  String CONFIG = "config/metainfo.properties";
	
	protected  String SOL_RQUERY_CONVERT_STRING = ProfileConfigUtil.findPro(CONFIG).getProperty("solrQueryConvert.class");
	
	/**
	 * 上传图片的域名
	 */
	@Value("#{meta['upload.img.domain.base']}")
	private String				UPLOAD_IMG_DOMAIN	= "";
	
		
	/**
	 * 前往页面 将 分类列表 与 导航列表 传给页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/base/navigation.htm",method = RequestMethod.GET)
	public String navigationList(Long navigationId,Model model){
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> cateList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", cateList);
		log.debug(JsonFormatUtil.format(cateList));

		List<DynamicPropertyCommand> dynamicPropertyCommand = propertyManager.findAllDynamicPropertyCommand();
		model.addAttribute("dynamicPropertyCommand", dynamicPropertyCommand);

		List<Navigation> naviList = navigationManager.findAllNavigationList(Navigation.COMMOM_SORTS);
		model.addAttribute("navigationList", naviList);
		model.addAttribute("navigationId", navigationId);
		log.debug(JsonFormatUtil.format(naviList));
		return "system/navigation/navigation";
	}

	/**
	 * 加载导航树
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/base/navigationTree.json",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> navigationTree(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Navigation> list = navigationManager.findAllNavigationList(Navigation.COMMOM_SORTS);
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", 0);
		root.put("name", "ROOT");
		root.put("state", 1);
		root.put("open", true);
		root.put("root", true);
		nodeList.add(root);
		for (int i = 0; i < list.size(); i++){
			Map<String, Object> node = new HashMap<String, Object>();
			Navigation navi = list.get(i);
			node.put("id", navi.getId());
			node.put("pId", navi.getParentId());
			node.put("name", navi.getName());
			node.put("state", navi.getLifecycle());
			node.put("diy_type", navi.getType());
			node.put("diy_param", navi.getParam());
			node.put("diy_sort", navi.getSort());
			node.put("diy_url", navi.getUrl());
			node.put("diy_isNewWin", navi.getIsNewWin());
			if (navi.getParentId() != null) {
				node.put("open", 0L == navi.getParentId());
			}else{
				node.put("open", false);
			}
			nodeList.add(node);
		}
		map.put("tree", JsonFormatUtil.format(nodeList));
		log.debug(JsonFormatUtil.format(nodeList));
		return map;
	}

	/**
	 * 新增或更新导航
	 * 
	 * @param navigation
	 * @return
	 */
	@RequestMapping(value = "/base/saveOrUpdateNavigation.json",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createOrUpdateNavigation(Navigation navigation){
		Map<String, Object> rs = new HashMap<String, Object>();
		navigation.setOpeartorId(getUserDetails().getUserId());
		try{
			rs.put("model", navigationManager.createOrUpdateNavigation(navigation));
			rs.put("isSuccess", true);
		}catch (Exception e){
			e.printStackTrace();
			rs.put("isSuccess", false);
		}
		log.debug(JsonFormatUtil.format(rs));
		return rs;
	}

	@RequestMapping(value = "/i18n/base/saveOrUpdateNavigation.json",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createOrUpdateNavigationI18n(@I18nCommand NavigationCommand navigationCommand,String naviStr){
		Map<String, Object> rs = new HashMap<String, Object>();
		navigationCommand.setOpeartorId(getUserDetails().getUserId());
		navigationCommand.setFacetParameterList(getFacetParametersFromStr(naviStr));
		try{
			rs.put("model", navigationManager.i18nCreateOrUpdateNavigation(navigationCommand));
			rs.put("isSuccess", true);
		}catch (BusinessException e){
			// e.printStackTrace();
			String errMsg = null;
			if (e.getArgs() == null) {
				errMsg = getMessage(e.getErrorCode());
			}else{
				errMsg = getMessage(e.getErrorCode(), e.getArgs());
			}

			rs.put("isSuccess", false);
			rs.put("errMsg", errMsg);
			return rs;
		}
		log.debug(JsonFormatUtil.format(rs));
		return rs;
	}

	@RequestMapping("/i18n/navigation/findNavigationLangByNavigationId.json")
	@ResponseBody
	public NavigationCommand findNavigationLangByNavigationId(@RequestParam("navigationId") Long navigationId){
		return navigationManager.i18nFindNavigationLangByNavigationId(navigationId);
	}

	/**
	 * 删除导航
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/base/removeNavigation.json",method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity removeNavigation(@RequestParam Long id){
		try{
			navigationManager.removeNavigationById(id);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			return FAILTRUE;
		}
	}

	/**
	 * 排序导航
	 * @param ids  已排序的导航id集（升序）
	 * @return
	 */
	@RequestMapping(value = "/base/sortNavigation.json",method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity sortNavigation(@RequestParam String ids){
		log.debug("ids:  " + ids);
		try{
			navigationManager.sortNavigationsByIds(ids, getUserDetails().getUserId());
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			return FAILTRUE;
		}
	}
	
	/***
	 * 进入导航商品排序
	 * @param navigationId
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/navigation/itemSort.htm" })
	public String itemCategoryNew(@RequestParam Long navigationId,Model model){
		//导航节点集合
		List<Navigation> naviList = navigationManager.findAllNavigationList(Navigation.COMMOM_SORTS);
		model.addAttribute("navigationList", naviList);
		
		//已排序商品集合
		SearchResultPage<ItemForSolrCommand>  resultPage =  getSortedList(navigationId);
		model.addAttribute("resultPage", resultPage);
		
		//导航ID
		model.addAttribute("navigationId", navigationId);
		model.addAttribute("UPLOAD_IMG_DOMAIN", UPLOAD_IMG_DOMAIN);
		
		return "/system/navigation/item-navigation-sort";
	}

	/**
	 * 未排序商品列表
	 */
	@RequestMapping(value = "/navigation/{navigationId}/itemUnsortedList.json")
	@ResponseBody
	public SearchResultPage<ItemForSolrCommand> findItemCtListJson(@PathVariable Long navigationId){
		return  getUnsortedList(navigationId);
	}

	/**
	 * 已排序商品列表
	 */
	@RequestMapping(value = "/navigation/{navigationId}/itemSortedList.htm")
	public String findSortedItem(@PathVariable Long navigationId,Model model){
		SearchResultPage<ItemForSolrCommand>  resultPage =  getSortedList(navigationId);
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("isSuccess", "success");
		model.addAttribute("UPLOAD_IMG_DOMAIN", UPLOAD_IMG_DOMAIN);
		return "/system/navigation/item-navigation-detail";
	}

	/**
	 * 修改商品排序
	 * @time 2015年7月30日 上午10:28:56
	 */
	@RequestMapping(value = "/navigation/updateItemSort.json",method = RequestMethod.POST)
	@ResponseBody
	public Object updateItemSort(@RequestParam String sequence, @RequestParam Long navigationId, Model model) {
		BackWarnEntity result = new BackWarnEntity(Boolean.TRUE.booleanValue(), "");
		ItemCollection itemCollection = sdkItemCollectionManager.findItemCollectionByNavigationId(navigationId);
		if (itemCollection == null) {
			result.setIsSuccess(false);
			result.setDescription("未关联商品集合");

		}else{
			ItemCollection ref = navigationManager.updateSequenceById(itemCollection.getId(), sequence);
			if (ref == null) {
				result.setIsSuccess(false);
				result.setDescription("更新失败");
			}
		}
		return result;
	}
	
	
	/**
	 * 添加商品排序
	 */
	@RequestMapping({ "/navigation/addSortItem.json" })
	@ResponseBody
	public Object addSortItem(Long navigationId,String itemCodes) {
		BackWarnEntity result = new BackWarnEntity(Boolean.TRUE.booleanValue(), "添加成功");
		if(navigationId==null){
			result.setIsSuccess(false);
			result.setDescription("请选择导航节点");
		}
		if(itemCodes==null || itemCodes.length()==0){
			result.setIsSuccess(false);
			result.setDescription("输入商品编码为空");
		}
		//商品Code去重复
		Set<String> tempSet= new TreeSet<String>();
		String[] itemCodeArray = itemCodes.split(",");
		Collections.addAll(tempSet, itemCodeArray);
		if(tempSet.size()!=itemCodeArray.length){
			itemCodeArray = new String[tempSet.size()];
			itemCodeArray = tempSet.toArray(itemCodeArray);
		}
		if(!allInNavigationSolr(itemCodeArray,navigationId)){
			result.setIsSuccess(false);
			result.setDescription("商品编码不在该导航下");
		}
		if(!navigationManager.AddSortedItemCodeById(navigationId,itemCodeArray)){
			result.setIsSuccess(false);
			result.setDescription("添加失败");
		}else{
			result.setDescription(itemCodeArray);
		}
		return result;
	}
	
	

	/**
	 * 删除商品排序
	 */
	@RequestMapping({ "/navigation/removeSortItem.json" })
	@ResponseBody
	public Object removeSortItem(Long navigationId,String itemCodes) {
		BackWarnEntity result = new BackWarnEntity(Boolean.TRUE.booleanValue(), "删除成功");
		if(navigationId==null){
			result.setIsSuccess(false);
			result.setDescription("请选择导航节点");
		}
		if(itemCodes==null || itemCodes.length()==0){
			result.setIsSuccess(false);
			result.setDescription("请勾选待删除排序的商品");
		}
		String[] codes = itemCodes.split(",");
		if(!allInNavigationSolr(codes,navigationId)){
			result.setIsSuccess(false);
			result.setDescription("商品编码不在该导航下");
		}
		if(!navigationManager.removeSortedItemCodeById(navigationId, itemCodes.split(","))){
			result.setIsSuccess(false);
			result.setDescription("删除失败");
		}
		return result;
	}
	
	
	private boolean allInNavigationSolr(String[] codes,Long navigationId ){
		 SearchResultPage<ItemForSolrCommand> searchResultPage= searchNavigation(navigationId);
		for (String code : codes){
			if(code.length()==0){
				continue;
			}
			if (code != null && code.length() > 0) {
				boolean haveFlg = false;
				for(ItemForSolrCommand command :searchResultPage.getItems()){
					if (code.equals(command.getCode())) {
						haveFlg = true;
						break;
					}
				}
				if(!haveFlg){
					return false;
				}
			}
		}
		return true;
	}
	
	
	private List<FacetParameter> getFacetParametersFromStr(String naviStr){
		if (naviStr == null || naviStr.length() == 0) {
			return null;
		}

		List<FacetParameter> types = new ArrayList<FacetParameter>();

		String[] infos = naviStr.split(";");

		for (String info : infos){
			if (info.length() == 0) {
				continue;
			}

			String[] params = info.split(",");

			// name,type,value|name,type,value
			if (params.length != 3) {
				continue;
			}

			// params[0] 名称
			FacetParameter facetParameter = new FacetParameter(params[0]);

			// params[1]类型
			if (FacetType.CATEGORY.toString().equals(params[1])) {
				facetParameter.setFacetType(FacetType.CATEGORY);
			}else if (FacetType.PROPERTY.toString().equals(params[1])) {
				facetParameter.setFacetType(FacetType.PROPERTY);
			}

			// params[2] 类型树
			facetParameter.setValues(Arrays.asList(params[2].split("_")));

			types.add(facetParameter);
		}
		return types;
	}

	
	public SearchResultPage<ItemForSolrCommand> getUnsortedList(Long navigationId){
		ItemCollection itemCollection = sdkItemCollectionManager.findItemCollectionByNavigationId(navigationId);
		if(itemCollection==null ){
			return null;
		}
		if(itemCollection.getSequence()==null||itemCollection.getSequence().length()==0){
			return searchNavigation(navigationId);
		}
		SearchResultPage<ItemForSolrCommand> searchResultPage = searchNavigation(navigationId);
		String[] idList = itemCollection.getSequence().split(",");
		for (String itemId : idList){
			if (itemId != null && itemId.length() > 0) {
				Iterator<ItemForSolrCommand> iterator = searchResultPage.getItems().iterator();
				while (iterator.hasNext()){
					if (itemId.equals(iterator.next().getId().toString())) {
						iterator.remove();
						break;
					}
				}
			}
		}
		searchResultPage.setCount(searchResultPage.getItems().size());
		return searchResultPage;
	}
	
	public  SearchResultPage<ItemForSolrCommand> getSortedList(Long navigationId){
		ItemCollection itemCollection = sdkItemCollectionManager.findItemCollectionByNavigationId(navigationId);
		//商品集合不存在
		if(itemCollection==null || itemCollection.getSequence()==null||itemCollection.getSequence().length()==0){
			return null;
		}
		SearchResultPage<ItemForSolrCommand> searchResultPage = searchNavigation(navigationId);
		SearchResultPage<ItemForSolrCommand>  sortList = new  SearchResultPage<ItemForSolrCommand>();
		List<ItemForSolrCommand> commands = new ArrayList<ItemForSolrCommand>();
		String[] idList  = itemCollection.getSequence().split(",");
		for(String itemId:idList){
			//检索结果存在
			boolean sureFlg=false;
			ItemForSolrCommand command = null;
			if(itemId!=null && itemId.length()>0){
				for(ItemForSolrCommand curCommand:searchResultPage.getItems()){
					if(itemId.equals(curCommand.getId().toString())){
						command = curCommand;
						sureFlg = true;
						break;
					}
				}
			}
			
			if(sureFlg){
				commands.add(command);
			}
		}
		
		sortList.setItems(commands);
		
		sortList.setCurrentPage(searchResultPage.getCurrentPage());
		sortList.setCount(commands.size());
		sortList.setSize(searchResultPage.getSize());
		sortList.setStart(searchResultPage.getStart());
		sortList.setTotalPages(searchResultPage.getTotalPages());
		
		return sortList;
	}
	
	private SearchResultPage<ItemForSolrCommand>  searchNavigation(Long navigationId){
		SearchResultPage<ItemForSolrCommand> searchResultPage =null;
		ItemCollection collection = sdkItemCollectionManager.findItemCollectionByNavigationId(navigationId);
		
		//初始化Solr转换器
		if(solrQueryConvert==null){
			try{
				solrQueryConvert =  (SolrQueryConvert) Class.forName(SOL_RQUERY_CONVERT_STRING).newInstance();
			}catch (Exception e){
				log.error(e.getMessage());
			}
		}
		if (Validator.isNotNullOrEmpty(collection)) {
				SearchCommand searchCommand = collectionToSearchCommand(collection);

				// ***************** 下面这些查询和searchPage是一致的
				// 创建solrquery对象
				SolrQuery solrQuery = solrQueryConvert.convert(searchCommand);

				// set facet相关信息
				setFacet(solrQuery);

				// 设置权重信息
				Boost boost = createBoost(searchCommand);
				searchManager.setSolrBoost(solrQuery, boost);

				// 查询
				searchResultPage = searchManager.search(solrQuery);
		}
		return searchResultPage;

	}
	
	protected SearchCommand collectionToSearchCommand(ItemCollection collection) {
		SearchCommand searchCommand = new SearchCommand();
		
		String facetParameters = collection.getFacetParameters();
		List<FacetParameter> params =JSON.parseArray(facetParameters,FacetParameter.class);
		searchCommand.setFacetParameters(params);
		
		Navigation navi = navigationManager.findByItemCollectionId(collection.getId());
		searchCommand.setNavigationId(navi.getId());
		
		return searchCommand;
	}
	
	
	protected void setFacet(SolrQuery solrQuery){
		// facetFileds，set防止重复数据
		Set<String> facetFields = new HashSet<String>();

		// ***************************************************设置分类的facet
		facetFields.add(SkuItemParam.category_tree);

		// **************************************************设置属性的facet
		List<SearchConditionCommand> cmdList = searchManager.findConditionByCategoryIdsWithCache(new ArrayList<Long>());
		// 如果为null或数量小于1
		if (null == cmdList || cmdList.size() < 1) {
			return;
		}

		for (SearchConditionCommand cmd : cmdList){
			Long propertyId = cmd.getPropertyId();
			if (propertyId != null) {
				if(SearchCondition.NORMAL_TYPE.equals(cmd.getType())){
					facetFields.add(SkuItemParam.dynamicCondition + propertyId);
				}else if(SearchCondition.SALE_PRICE_TYPE.equals(cmd.getType())){
					List<SearchConditionItemCommand> searchConditionItemCommands= searchManager.findCoditionItemByCoditionIdWithCache(cmd.getId());
					for(SearchConditionItemCommand scItemCmd : searchConditionItemCommands){
						if(null!= scItemCmd){
							Integer min = scItemCmd.getAreaMin();
							Integer max = scItemCmd.getAreaMax();
							if(null!=min&&null!=max&&min<=max){
								String areaStr = FilterUtil.paramConverToArea(min.toString(), max.toString());
								StringBuilder sb = new StringBuilder();
								sb.append("{!ex=priceTag}"+SkuItemParam.sale_price).append(":").append(areaStr);
							}
						}
					}
				}
			}
		}

		// 设置solrQuery的facetFiled
		NebulaSolrQueryFactory.setFacetField(facetFields.toArray(new String[facetFields.size()]), solrQuery);
	}

	
	protected Boost createBoost(SearchCommand searchCommand){
		Boost boost = new Boost();

		// 设置商品置顶
		setBoostBq(boost, searchCommand);

		// 搜索搜索关键字不为空
		setBoostPfAndbf(boost, searchCommand.getSearchWord());

		return boost;
	}

	/**
	 * 设置boost中的bq属性
	 * 
	 * @return void
	 * @param boost
	 * @param searchCommand
	 * @author 冯明雷
	 * @time 2016年4月27日下午2:46:24
	 */
	protected void setBoostBq(Boost boost,SearchCommand searchCommand){
		// 根据商品指定排序表中的数据，将商品置顶
		ItemCollection itemCollection = sdkItemCollectionManager.findItemCollectionByNavigationId(searchCommand.getNavigationId()); 

		if (itemCollection != null) {
			String sequence = itemCollection.getSequence();
			if (Validator.isNotNullOrEmpty(sequence)) {
				String[] itemIdStrs = sequence.split(SEPARATORCHARS_COMMA);
				if (itemIdStrs != null && itemIdStrs.length > 0) {
					StringBuffer bq = new StringBuffer();
					for (int i = 0; i < itemIdStrs.length; i++){
						// 最高分是指定商品的个数*100
						Integer score = itemIdStrs.length * 100;

						// 商品id
						String itemId = itemIdStrs[i];
						
						if(Validator.isNotNullOrEmpty(itemId)){
							
							// 计算得分
							score = score - i * 10;

							bq.append("id:" + itemId + "^" + score);

							if (i > itemIdStrs.length - 1) {
								bq.append(" OR ");
							}
							
						}
						

					}
					boost.setBq(bq.toString());
				}
			}
		}

	}
	
	
	/**
	 * 设置搜索关键字权重(商城可以重写这个方法)
	 * 
	 * @return void
	 * @param boost
	 * @param searchCommand
	 * @author 冯明雷
	 * @time 2016年4月27日下午2:51:19
	 */
	protected void setBoostPfAndbf(Boost boost,String searchKeyWord){
		if (Validator.isNullOrEmpty(searchKeyWord)) {
			return;
		}
		// 转义特殊字符
		searchKeyWord = NebulaSolrQueryFactory.escape(searchKeyWord);

		// 设置serachkeyword的权重，匹配到哪个字段的时候先显示出来
		StringBuffer qf = new StringBuffer();

		// 各个字段的分数加起来总共等于1
		qf.append(SkuItemParam.style + ":" + searchKeyWord + "^0.3" + SEPARATORCHARS_COMMA);
		qf.append(SkuItemParam.itemCode + ":" + searchKeyWord + "^0.25" + SEPARATORCHARS_COMMA);
		qf.append(SkuItemParam.title + ":" + searchKeyWord + "^0.15" + SEPARATORCHARS_COMMA);
		qf.append(SkuItemParam.subTitle + ":" + searchKeyWord + "^0.15" + SEPARATORCHARS_COMMA);
		qf.append(SkuItemParam.allCategoryCodes + ":" + searchKeyWord + "^0.1" + SEPARATORCHARS_COMMA);
		qf.append(SkuItemParam.categoryname + "*:" + searchKeyWord + "^0.05" + SEPARATORCHARS_COMMA);

		boost.setQf(qf.toString());

		// 需要匹配的字段,以逗号分隔
		StringBuffer pf = new StringBuffer();
		pf.append(SkuItemParam.style + SEPARATORCHARS_COMMA);// 款号
		pf.append(SkuItemParam.itemCode + SEPARATORCHARS_COMMA);// 商品code
		pf.append(SkuItemParam.title + SEPARATORCHARS_COMMA);// 商品名称
		pf.append(SkuItemParam.subTitle + SEPARATORCHARS_COMMA);// 副标题
		pf.append(SkuItemParam.allCategoryCodes + SEPARATORCHARS_COMMA);// 分类code
		pf.append(SkuItemParam.categoryname + "*");// 分类名称

		boost.setPf(pf.toString());
	}
}
