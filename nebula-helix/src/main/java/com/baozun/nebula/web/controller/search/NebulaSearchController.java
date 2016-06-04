package com.baozun.nebula.web.controller.search;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.GroupParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.model.product.ItemCollection;
import com.baozun.nebula.sdk.manager.SdkItemCollectionManager;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.FacetFilterHelper;
import com.baozun.nebula.search.FacetGroup;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.search.convert.SolrQueryConvert;
import com.baozun.nebula.search.manager.SearchManager;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.web.controller.search.convert.ItemListViewCommandConverter;
import com.baozun.nebula.web.controller.search.form.SearchForm;
import com.baozun.nebula.web.controller.search.viewcommand.ItemListViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;

/**
 * 搜索相关方法controller
 * <ol>
 * <li>{@link #searchPage(searchForm,request,response,model)}</li>
 * <li>{@link #navigationPage(navId,request,response,model)}</li>
 * </ol>
 * </ol>
 * </blockquote>
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月21日 上午11:22:13
 */
public class NebulaSearchController extends NebulaAbstractSearchController{

	/** log定义 */
	private static final Logger			LOG						= LoggerFactory.getLogger(NebulaSearchController.class);

	public static final String			ITEM_LIST				= "item.list";

	public static final String			SEARCH_NO_RESULT		= "item.search-no-result";

	public static final String			ITEM_LIST_VIEW_COMMOND	= "itemListViewCommand";

	public static final String			ITEM_LIST_SEARCH_TITLE	= "keyword";

	@Autowired
	@Qualifier("solrQueryConvert")
	private SolrQueryConvert			solrQueryConvert;

	@Autowired
	private SearchManager				searchManager;

	@Autowired
	@Qualifier("facetFilterHelper")
	private FacetFilterHelper			facetFilterHelper;

	@Autowired
	private SdkItemCollectionManager	sdkItemCollectionManager;

	/**
	 * 搜索列表页面
	 * 
	 * @return String
	 * @param searchForm
	 *            页面传来的搜索参数，主要有搜索关键字、页码、每页条数(默认是20)、筛选条件、分类等
	 * @param request
	 * @param response
	 * @param model
	 * @author 冯明雷
	 * @time 2016年4月21日上午11:25:30
	 */
	public String searchPage(@ModelAttribute SearchForm searchForm,HttpServletRequest request,HttpServletResponse response,Model model){
		// 将页面传来的参数searchForm转换为 searchCommand
		SearchCommand searchCommand = new SearchCommand();
		PropertyUtil.copyProperties(searchCommand, searchForm);

		ItemCollection collection = null;
		Long navigationId = searchCommand.getNavigationId();
		if (Validator.isNotNullOrEmpty(navigationId)) {
			collection = sdkItemCollectionManager.findItemCollectionByNavigationId(Long.valueOf(navigationId));
		}

		// 将 searchCommand 中 filterConditionStr,categoryConditionStr 转成FacetParameter
		searchParamProcess(searchCommand, collection);

		// 创建solrquery对象
		SolrQuery solrQuery = solrQueryConvert.convert(searchCommand);
		LOG.debug("solr solrQuery before setFacet:" + solrQuery.toString());

		// set facet相关信息
		setFacet(solrQuery, searchCommand);
		LOG.debug("solr solrQuery after setFacet:" + solrQuery.toString());

		// 设置权重信息
		Boost boost = createBoost(searchCommand, collection);
		searchManager.setSolrBoost(solrQuery, boost);
		LOG.debug("solr solrQuery after setSolrBoost:" + solrQuery.toString());

		// 查询
		SearchResultPage<ItemForSolrCommand> searchResultPage = searchManager.search(solrQuery);
		
		if (Validator.isNotNullOrEmpty(searchCommand.getSearchWord())) {
			model.addAttribute(ITEM_LIST_SEARCH_TITLE, searchCommand.getSearchWord());
		}
		
		// 是否分组显示
		String isGroup = solrQuery.get(GroupParams.GROUP);
		//如果是分组
		if(Validator.isNotNullOrEmpty(isGroup) && Boolean.parseBoolean(isGroup)){
			if (searchResultPage == null || searchResultPage.getItemsListWithGroup() == null|| searchResultPage.getItemsListWithGroup().getCount()==0l) {
				LOG.info("[SOLR_SEARCH_RESULT] Solr query result is empty. time:[{}]", new Date());
				return SEARCH_NO_RESULT;
			}
		}else{
			//如果不是分组
			if (searchResultPage == null || searchResultPage.getItemsListWithOutGroup() == null || searchResultPage.getItemsListWithOutGroup().getCount()==0l) {
				LOG.info("[SOLR_SEARCH_RESULT] Solr query result is empty. time:[{}]", new Date());
				return SEARCH_NO_RESULT;
			}
		}

		// 页面左侧筛选项
		List<FacetGroup> facetGroups = facetFilterHelper.createFilterResult(searchResultPage, searchCommand.getFacetParameters());

		// 设置facetGroup是否显示
		setExcludeFacetGroup(collection, facetGroups);

		searchResultPage.setFacetGroups(facetGroups);

		// 将SearchResultPage<ItemForSolrCommand> 转换成页面需要的itemListView对象
		ItemListViewCommandConverter listViewCommandConverter = new ItemListViewCommandConverter();
		ItemListViewCommand itemListViewCommand = listViewCommandConverter.convert(searchResultPage);

		model.addAttribute(ITEM_LIST_VIEW_COMMOND, itemListViewCommand);

		return ITEM_LIST;
	}

	/**
	 * 导航着陆页的。navigationfilter 直接跳转到这个连接
	 * 
	 * @param navId
	 * @param request
	 * @param response
	 * @param model
	 * 			@requestMapping("/sys/navigation")
	 * @return
	 */
	public String navigationPage(
			@RequestParam(value = "nid") Long nid,
			@RequestParam(value = "cid") Long cid,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){
		ItemCollection collection = sdkItemCollectionManager.findItemCollectionById(cid);
		if (Validator.isNotNullOrEmpty(collection)) {
			SearchCommand searchCommand = collectionToSearchCommand(collection);
			
			searchCommand.setNavigationId(nid);
			searchCommand.setFilterConditionStr(request.getParameter("filterConditionStr"));
			searchCommand.setCategoryConditionStr(request.getParameter("categoryConditionStr"));
			searchCommand.setNavigationConditionStr(request.getParameter("navigationConditionStr"));
			searchCommand.setFilterParamOrder(request.getParameter("filterParamOrder"));

			// ***************** 下面这些查询和searchPage是一致的
			// 创建solrquery对象
			SolrQuery solrQuery = solrQueryConvert.convert(searchCommand);

			// set facet相关信息
			setFacet(solrQuery, searchCommand);

			// 设置权重信息
			Boost boost = createBoost(searchCommand, collection);
			searchManager.setSolrBoost(solrQuery, boost);

			// 查询
			SearchResultPage<ItemForSolrCommand> searchResultPage = searchManager.search(solrQuery);
			
			// 是否分组显示
			String isGroup = solrQuery.get(GroupParams.GROUP);
			//如果是分组
			if(Validator.isNotNullOrEmpty(isGroup) && Boolean.parseBoolean(isGroup)){
				if (searchResultPage == null || searchResultPage.getItemsListWithGroup() == null|| searchResultPage.getItemsListWithGroup().getCount()==0l) {
					LOG.info("[SOLR_SEARCH_RESULT] Solr query result is empty. time:[{}]", new Date());
					return SEARCH_NO_RESULT;
				}
			}else{
				//如果不是分组
				if (searchResultPage == null || searchResultPage.getItemsListWithOutGroup() == null || searchResultPage.getItemsListWithOutGroup().getCount()==0l) {
					LOG.info("[SOLR_SEARCH_RESULT] Solr query result is empty. time:[{}]", new Date());
					return SEARCH_NO_RESULT;
				}
			}
			
			// 页面左侧筛选项
			List<FacetGroup> facetGroups = facetFilterHelper.createFilterResult(searchResultPage, searchCommand.getFacetParameters());
			// 设置facetGroup是否显示
			setExcludeFacetGroup(collection, facetGroups);

			searchResultPage.setFacetGroups(facetGroups);

			// 将SearchResultPage<ItemForSolrCommand> 转换成页面需要的itemListView对象
			ItemListViewCommandConverter listViewCommandConverter = new ItemListViewCommandConverter();
			ItemListViewCommand itemListViewCommand = listViewCommandConverter.convert(searchResultPage);

			model.addAttribute(ITEM_LIST_VIEW_COMMOND, itemListViewCommand);

			if (Validator.isNotNullOrEmpty(searchCommand.getSearchWord())) {
				model.addAttribute(ITEM_LIST_SEARCH_TITLE, searchCommand.getSearchWord());
			}

		}
		return ITEM_LIST;
	}

	/**
	 * solr查询商品,只查询商品，不进行facet
	 * 
	 * @return ItemListViewCommand
	 * @param searchForm
	 * @param request
	 * @param response
	 * @param model
	 * @author 冯明雷
	 * @time 2016年5月10日下午4:22:06
	 */
	public ItemListViewCommand getItemListViewCommand(
			@ModelAttribute SearchForm searchForm,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){

		// 将页面传来的参数searchForm转换为 searchCommand
		SearchCommand searchCommand = new SearchCommand();
		PropertyUtil.copyProperties(searchCommand, searchForm);

		ItemCollection collection = null;
		Long navigationId = searchCommand.getNavigationId();
		if (Validator.isNotNullOrEmpty(navigationId)) {
			collection = sdkItemCollectionManager.findItemCollectionByNavigationId(Long.valueOf(navigationId));
		}

		// 将 searchCommand 中 filterConditionStr,categoryConditionStr 转成FacetParameter
		searchParamProcess(searchCommand, collection);

		// 创建solrquery对象
		SolrQuery solrQuery = solrQueryConvert.convert(searchCommand);
		LOG.debug("solr solrQuery before setFacet:" + solrQuery.toString());

		// 设置权重信息
		Boost boost = createBoost(searchCommand, collection);
		searchManager.setSolrBoost(solrQuery, boost);
		LOG.debug("solr solrQuery after setSolrBoost:" + solrQuery.toString());

		// 查询
		SearchResultPage<ItemForSolrCommand> searchResultPage = searchManager.search(solrQuery);		
		
		// 是否分组显示
		String isGroup = solrQuery.get(GroupParams.GROUP);
		//如果是分组
		if(Validator.isNotNullOrEmpty(isGroup) && Boolean.parseBoolean(isGroup)){
			if (searchResultPage == null || searchResultPage.getItemsListWithGroup() == null|| searchResultPage.getItemsListWithGroup().getCount()==0l) {
				LOG.info("[SOLR_SEARCH_RESULT] Solr query result is empty. time:[{}]", new Date());
				return null;
			}
		}else{
			//如果不是分组
			if (searchResultPage == null || searchResultPage.getItemsListWithOutGroup() == null || searchResultPage.getItemsListWithOutGroup().getCount()==0l) {
				LOG.info("[SOLR_SEARCH_RESULT] Solr query result is empty. time:[{}]", new Date());
				return null;
			}
		}

		// 将SearchResultPage<ItemForSolrCommand> 转换成页面需要的itemListView对象
		ItemListViewCommandConverter listViewCommandConverter = new ItemListViewCommandConverter();
		ItemListViewCommand itemListViewCommand = listViewCommandConverter.convert(searchResultPage);

		model.addAttribute(ITEM_LIST_VIEW_COMMOND, itemListViewCommand);
		return itemListViewCommand;

	}
}
