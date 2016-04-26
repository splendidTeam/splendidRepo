package com.baozun.nebula.web.controller.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.FacetFilterHelper;
import com.baozun.nebula.search.FacetGroup;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.search.convert.SolrQueryConvert;
import com.baozun.nebula.search.manager.SearchManager;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.factory.SortTypeEnum;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.baozun.nebula.web.controller.search.form.SearchForm;
import com.feilong.core.bean.PropertyUtil;

/**
 * 搜索相关方法controller
 * <ol>
 * <li>{@link #searchPage(searchForm,request,response,model)}</li>
 * </ol>
 * <h3>searchPage方法,主要有以下几点:</h3> <blockquote>
 * <ol>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ol>
 * </blockquote>
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月21日 上午11:22:13
 */
public class NebulaSearchController extends NebulaAbstractSearchController{

	/** log定义 */
	private static final Logger	LOG	= LoggerFactory.getLogger(NebulaSearchController.class);

	@Autowired
	@Qualifier("simpleGroupSolrQueryConvert")
	private SolrQueryConvert	solrQueryConvert;

	@Autowired
	private SearchManager		searchManager;

	@Autowired
	private FacetFilterHelper	facetFilterHelper;

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

		// 将 searchCommand 中 filterConditionStr 转成FacetParameter
		searchParamProcess(searchCommand);

		// 创建solrquery对象
		SolrQuery solrQuery = solrQueryConvert.convert(searchCommand);

		// set facet相关信息
		setFacet(solrQuery, searchCommand);

		// 设置权重信息
		Boost boost = this.createBoost();
		searchManager.setSolrBoost(solrQuery, boost);

		// 查询
		SearchResultPage<ItemForSolrCommand> solrDataPage = searchManager.search(solrQuery);

		// 页面左侧筛选项
		List<FacetGroup> facetGroups = facetFilterHelper.createFilterResult(solrDataPage);

		// 将SearchResultPage<ItemForSolrCommand> 转换成页面需要的itemListView对象

		return "item.list";
	}
}
