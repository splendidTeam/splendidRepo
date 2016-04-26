package com.baozun.nebula.web.controller.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.product.SearchConditionManager;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.FacetParameter;
import com.baozun.nebula.search.FacetType;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.search.manager.SearchManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
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
	public void searchParamProcess(SearchCommand searchCommand){

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
					String name = SkuItemParam.dynamicCondition + StringUtils.substringBefore(filter, SEPARATORCHARS_MINUS);

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

		searchCommand.setFacetParameters(facetParameters);
	}

	/**
	 * 设置solrfacet信息，需要结合t_pd_search_con 设置
	 * @return void
	 * @param solrQuery
	 * @param searchCommand 
	 * @author 冯明雷
	 * @time 2016年4月25日下午6:18:02
	 */
	public void setFacet(SolrQuery solrQuery,SearchCommand searchCommand){
		//筛选条件
		List<FacetParameter> facetParameters=searchCommand.getFacetParameters();
		
		//选中的分类id
		List<Long> categoryIds=new ArrayList<Long>();
		
		//如果不为空
		if(Validator.isNotNullOrEmpty(facetParameters)){
			for (FacetParameter facetParameter : facetParameters){
				//如果是分类筛选
				if(FacetType.CATEGORY.equals(facetParameter.getFacetType())){
					
				}
			}
		}
		List<SearchConditionCommand> cmdList = searchManager.findConditionByCategoryIdsWithCache(categoryIds);
		
		//如果为null或数量小于1
		if (null == cmdList || cmdList.size() < 1) {
			return ;
		}
		
		
		
		
		
		
	}

	/**
	 * 创建solr boost对象，用做默认排序的权重打分
	 * 
	 * @return
	 */
	public Boost createBoost(){
		Boost boost = new Boost();
		return boost;
	}

}
