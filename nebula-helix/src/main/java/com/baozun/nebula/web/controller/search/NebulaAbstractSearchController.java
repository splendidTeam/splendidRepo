package com.baozun.nebula.web.controller.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.FacetParameter;
import com.baozun.nebula.search.FacetType;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.search.manager.SearchManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.factory.NebulaSolrQueryFactory;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

public abstract class NebulaAbstractSearchController extends BaseController{

	/** 逗号分隔符 */
	private final static String	SEPARATORCHARS_COMMA	= ",";

	/** -分隔符 */
	private final static String	SEPARATORCHARS_MINUS	= "-";

	@Autowired
	private SearchManager		searchManager;

	/**
	 * 将 searchCommand 里面的filterConditionStr 转成 FacetParameter 用做后面solr查询
	 * 
	 * @param searchCommand
	 */
	protected void searchParamProcess(SearchCommand searchCommand){
		List<FacetParameter> facetParameters = new ArrayList<FacetParameter>();

		// **************************************属性筛选部分
		List<FacetParameter> facetParametersWithProperty = setFacetParametersWithProperty(searchCommand);
		if (Validator.isNotNullOrEmpty(facetParametersWithProperty)) {
			facetParameters.addAll(facetParametersWithProperty);
		}

		// **************************************分类筛选部分
		List<FacetParameter> facetParametersWithCategory = setFacetParametersWithCategory(searchCommand);
		if (Validator.isNotNullOrEmpty(facetParametersWithCategory)) {
			facetParameters.addAll(facetParametersWithCategory);
		}

		// ***************************************导航部分
		// TODO

		searchCommand.setFacetParameters(facetParameters);
	}

	/**
	 * 设置solrfacet信息，需要结合t_pd_search_con 设置
	 * 
	 * @return void
	 * @param solrQuery
	 * @param searchCommand
	 * @author 冯明雷
	 * @time 2016年4月25日下午6:18:02
	 */
	protected void setFacet(SolrQuery solrQuery,SearchCommand searchCommand){

		// facetFileds，set防止重复数据
		Set<String> facetFields = new HashSet<String>();

		// List<String> propertyList = new ArrayList<String>();

		// 筛选条件
		List<FacetParameter> facetParameters = searchCommand.getFacetParameters();

		// 选中的分类id
		List<Long> categoryIds = new ArrayList<Long>();

		// 如果不为空
		if (Validator.isNotNullOrEmpty(facetParameters)) {
			for (FacetParameter facetParameter : facetParameters){
				// 如果是分类筛选
				if (FacetType.CATEGORY.equals(facetParameter.getFacetType())) {

				}
			}
		}
		List<SearchConditionCommand> cmdList = searchManager.findConditionByCategoryIdsWithCache(categoryIds);

		// 如果为null或数量小于1
		if (null == cmdList || cmdList.size() < 1) {
			return;
		}

		// 设置solrQuery的facetFiled
		NebulaSolrQueryFactory.setFacetField(facetFields.toArray(new String[facetFields.size()]), solrQuery);
	}

	/**
	 * 创建solr boost对象，用做默认排序的权重打分
	 * 
	 * @return
	 */
	protected Boost createBoost(){
		Boost boost = new Boost();
		return boost;
	}

	/**
	 * searchCommand中的属性相关条件设置
	 * 
	 * @return List<FacetParameter>
	 * @param searchCommand
	 * @author 冯明雷
	 * @time 2016年4月26日下午6:26:38
	 */
	protected List<FacetParameter> setFacetParametersWithProperty(SearchCommand searchCommand){
		List<FacetParameter> facetParameters = new ArrayList<FacetParameter>();

		// 属性筛选条件
		String filterConditionStr = searchCommand.getFilterConditionStr();
		// 如果属性筛选条件不为空
		if (Validator.isNotNullOrEmpty(filterConditionStr)) {
			// 以逗号分隔开
			String[] filterStrs = StringUtils.split(filterConditionStr, SEPARATORCHARS_COMMA);

			Map<String, FacetParameter> map = new HashMap<String, FacetParameter>();

			for (String filter : filterStrs){
				// 以短横线-分隔开
				if (StringUtils.contains(filter, SEPARATORCHARS_MINUS)) {
					// 名称
					// String name = SkuItemParam.dynamicCondition + StringUtils.substringBefore(filter, SEPARATORCHARS_MINUS);
					String name = StringUtils.substringBefore(filter, SEPARATORCHARS_MINUS);

					// value值
					List<String> values = new ArrayList<String>();
					values.add(StringUtils.substringAfter(filter, SEPARATORCHARS_MINUS));
					FacetParameter facetParameter = new FacetParameter(name);
					facetParameter.setValues(values);
					facetParameter.setFacetType(FacetType.PROPERTY);

					if (map.containsKey(name)) {
						facetParameter.getValues().addAll(map.get(name).getValues());
					}
					map.put(name, facetParameter);
				}
			}

			for (Entry<String, FacetParameter> entry : map.entrySet()){
				facetParameters.add(entry.getValue());
			}
		}
		return facetParameters;
	}

	/**
	 * searchCommand中的分类相关条件设置
	 * 
	 * @return List<FacetParameter>
	 * @param searchCommand
	 * @author 冯明雷
	 * @time 2016年4月26日下午6:26:38
	 */
	protected List<FacetParameter> setFacetParametersWithCategory(SearchCommand searchCommand){
		List<FacetParameter> facetParameters = new ArrayList<FacetParameter>();

		String categoryConditionStr = searchCommand.getCategoryConditionStr();
		// 如果分类筛选条件不为空
		if (Validator.isNotNullOrEmpty(categoryConditionStr)) {
			// 以逗号分隔开
			String[] categoryStrs = StringUtils.split(categoryConditionStr, SEPARATORCHARS_COMMA);

			Map<String, FacetParameter> map = new HashMap<String, FacetParameter>();
			for (String categoryStr : categoryStrs){
				if (StringUtils.contains(categoryStr, SEPARATORCHARS_MINUS)) {
					// 最后一个“-”前面的就是父级节点
					String parent = StringUtils.substringBeforeLast(categoryStr, SEPARATORCHARS_MINUS);

					List<String> values = new ArrayList<String>();
					values.add(categoryStr);
					FacetParameter facetParameter = new FacetParameter(SkuItemParam.categorys);
					facetParameter.setValues(values);
					facetParameter.setFacetType(FacetType.CATEGORY);

					if (map.containsKey(parent)) {
						facetParameter.getValues().addAll(map.get(parent).getValues());
					}
					map.put(parent, facetParameter);
				}
			}
			for (Entry<String, FacetParameter> entry : map.entrySet()){
				facetParameters.add(entry.getValue());
			}
		}

		return facetParameters;
	}

}
