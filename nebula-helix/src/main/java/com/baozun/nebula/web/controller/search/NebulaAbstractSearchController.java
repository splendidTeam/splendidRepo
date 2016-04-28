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


import com.alibaba.fastjson.JSON;

import com.baozun.nebula.manager.product.ItemCollectionManager;

import com.baozun.nebula.model.product.ItemCollection;
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
	private final static String		SEPARATORCHARS_COMMA	= ",";

	/** -分隔符 */
	private final static String		SEPARATORCHARS_MINUS	= "-";

	@Autowired
	private SearchManager			searchManager;

	@Autowired
	private ItemCollectionManager	itemCollectionManager;

	/**
	 * 将 searchCommand 里面的filterConditionStr 转成 FacetParameter 用做后面solr查询
	 * 
	 * @param searchCommand
	 */
	protected void searchParamProcess(SearchCommand searchCommand){
		List<FacetParameter> facetParameters = new ArrayList<FacetParameter>();

		// ***************************************导航部分
		// TODO

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

		searchCommand.setFacetParameters(facetParameters);
	}
	
	/**
	 * 将后台设置的导航的商品范围，转成searchCommand对象
	 * @param collection
	 * @return
	 */
	protected SearchCommand collectionToSearchCommand(ItemCollection collection) {
		SearchCommand searchCommand = new SearchCommand();
		
		String facetParameters = collection.getFacetParameters();
		List<FacetParameter> params =JSON.parseArray(facetParameters,FacetParameter.class);
		searchCommand.setFacetParameters(params);
		return searchCommand;

		
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
				facetFields.add(SkuItemParam.dynamicCondition + propertyId);
			}
		}

		// 设置solrQuery的facetFiled
		NebulaSolrQueryFactory.setFacetField(facetFields.toArray(new String[facetFields.size()]), solrQuery);
	}

	/**
	 * 创建solr boost对象，用做默认排序的权重打分
	 * 
	 * @return
	 */
	protected Boost createBoost(SearchCommand searchCommand){
		Boost boost = new Boost();

		// 设置商品置顶
		setBoostBq(boost, searchCommand);

		// 搜索搜索关键字不为空
		setBoostPfAndbf(boost, searchCommand.getSearchWord());

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
					FacetParameter facetParameter = new FacetParameter(SkuItemParam.category_tree);
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
		ItemCollection itemCollection = itemCollectionManager.findItemCollectionByNavigationId(searchCommand.getNavigationId());

		if (itemCollection != null) {
			String sequence = itemCollection.getSequence();
			if (sequence != null) {
				String[] itemIdStrs = sequence.split(SEPARATORCHARS_COMMA);
				if (itemIdStrs != null && itemIdStrs.length > 0) {
					StringBuffer bq = new StringBuffer();
					for (int i = 0; i < itemIdStrs.length; i++){
						// 最高分是指定商品的个数*100
						Integer score = itemIdStrs.length * 100;

						// 商品id
						String itemId = itemIdStrs[1];
						// 计算得分
						score = score - i * 10;

						bq.append("id:" + itemId + "^" + score);

						if (i > itemIdStrs.length - 1) {
							bq.append(" OR ");
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
