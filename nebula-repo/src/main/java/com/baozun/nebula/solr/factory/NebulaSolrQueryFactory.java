package com.baozun.nebula.solr.factory;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.common.params.FacetParams;
import org.apache.solr.common.params.GroupParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.search.FacetParameter;
import com.baozun.nebula.search.FacetType;
import com.baozun.nebula.search.command.ExcludeSearchCommand;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.feilong.core.Validator;

import loxia.dao.Sort;

public class NebulaSolrQueryFactory{

	private static final Logger	LOG					= LoggerFactory.getLogger(NebulaSolrQueryFactory.class);

	/** 属性 */
	private static final String	PROPERTY_VARIABLE	= "p";

	/** 分类 */
	private static final String	CATEGORY_VARIABLE	= "c";

	/**
	 * 创建查询SolrQuery 将SearchCommand组合成SOLR查询语句
	 * 
	 * @return SolrQuery
	 * @param searchCommand
	 * @param solrQuery
	 * @author 冯明雷
	 * @time 2016年4月22日下午3:16:45
	 */
	public static SolrQuery createSolrQuery(SearchCommand searchCommand,SolrQuery solrQuery){
		if (Validator.isNullOrEmpty(searchCommand.getSearchWord())) {
			solrQuery.setQuery("*:*");
		}else{
			addFqKeyword(searchCommand.getSearchWord(), solrQuery);
		}

		// 最后一个点击的过滤条件
		String lastFilerStr = "";

		// 点击顺序
		String filterParamOrder = searchCommand.getFilterParamOrder();
		if (filterParamOrder != null) {
			String[] strs = filterParamOrder.split(",");
			lastFilerStr = strs[strs.length - 1];
		}

		String variable = "";
		if (Validator.isNotNullOrEmpty(lastFilerStr)) {
			if (lastFilerStr.indexOf(CATEGORY_VARIABLE) > -1) {
				variable = CATEGORY_VARIABLE;
			}
			if (lastFilerStr.indexOf(PROPERTY_VARIABLE) > -1) {
				variable = PROPERTY_VARIABLE;
			}
		}

		List<FacetParameter> facetParameters = searchCommand.getFacetParameters();
		if (Validator.isNotNullOrEmpty(facetParameters)) {
			for (int i = 0; i < facetParameters.size(); i++){
				FacetParameter facetParameter = facetParameters.get(i);
				List<String> values = facetParameter.getValues();

				// 如果是价格范围的条件
				if (FacetType.RANGE.equals(facetParameter.getFacetType())) {
					addFqForPriceArea(solrQuery, facetParameter.getValues(), facetParameter.getName());
				}else{
					// 分类、属性、导航的条件
					for (String value : values){
						String[] strs = value.split("-");
						variable = variable + strs[strs.length - 1];

						if (variable.equals(lastFilerStr)) {
							addFqAccurateForStringListWithTag(solrQuery, facetParameter.getValues(), facetParameter.getName());
							break;
						}else{
							addFqAccurateForStringList(solrQuery, facetParameter.getValues(), facetParameter.getName());
							break;
						}
					}
				}
			}
		}
		
		//排除字段列表，设置排除字段的查询
		List<ExcludeSearchCommand> excludeList = searchCommand.getExcludeList();
		if(Validator.isNotNullOrEmpty(excludeList)){
			for(ExcludeSearchCommand excludeCommand : excludeList){
				StringBuilder fqStrBuilder = new StringBuilder();
				if(Validator.isNotNullOrEmpty(excludeCommand.getFieldName()) && Validator.isNotNullOrEmpty(excludeCommand.getValues())){
					fqStrBuilder.append("-").append(excludeCommand.getFieldName()).append(":(");
					List<String> values = excludeCommand.getValues();
					for(int i=0;i<values.size();i++){
						fqStrBuilder.append(values.get(i));
						if(i < values.size()-1){
							fqStrBuilder.append(" OR ");
						}
						if(i == values.size()-1){
							fqStrBuilder.append(")");
						}
					}
					
					solrQuery.addFilterQuery(fqStrBuilder.toString());
				}
			}
		}

		// 设置时间
		addFqTime(solrQuery);

		// 设置solr查询第几页和查询多少行
		Integer pageNumber = searchCommand.getPageNumber();
		if (null == pageNumber || pageNumber < 1) {
			// 默认
			pageNumber = 1;
		}

		Integer rows = searchCommand.getPageSize();
		if (rows != null) {
			Integer start = (pageNumber - 1) * rows;
			solrQuery.setStart(start);
			solrQuery.setRows(rows);
		}else{
			solrQuery.setRows(Integer.MAX_VALUE);
		}
		return solrQuery;
	}

	/**
	 * Sets the sort.
	 * 
	 * @param solrQuery
	 *            the solr query
	 * @param sorts
	 *            the sorts
	 */
	public static void setSort(SolrQuery solrQuery,SolrOrderSort[] order){
		if (Validator.isNullOrEmpty(order)) {
			// 设置默认排序
			solrQuery.addSort(SkuItemParam.default_sort, ORDER.desc);
		}else{
			for (SolrOrderSort sort : order){
				solrQuery.addSort(sort.getField(), sort.getType().equalsIgnoreCase(Sort.ASC) ? ORDER.asc : ORDER.desc);
			}
		}
	}

	/**
	 * 设置分组聚合的参数
	 * 
	 * @return SolrQuery
	 * @param solrQuery
	 * @param orders
	 * @author 冯明雷
	 * @time 2016年4月25日上午10:56:50
	 */
	public static SolrQuery setGroup(SolrQuery solrQuery,SolrOrderSort[] orders){
		String groupName = SkuItemParam.style;

		solrQuery.set(GroupParams.GROUP, true);
		solrQuery.set(GroupParams.GROUP_TOTAL_COUNT, true);
		solrQuery.set(GroupParams.GROUP_LIMIT, -1);
		solrQuery.set(GroupParams.GROUP_FORMAT, "grouped");
		solrQuery.set(GroupParams.GROUP_FACET, true);
		solrQuery.set(GroupParams.GROUP_FIELD, groupName);

		if (null != orders) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < orders.length; i++){
				SolrOrderSort solrOrderSort = orders[i];
				sb.append(solrOrderSort.getField() + " " + solrOrderSort.getType());
				if (i < orders.length - 1) {
					sb.append(",");
				}
			}
			solrQuery.set(GroupParams.GROUP_SORT, sb.toString());
		}

		return solrQuery;
	}

	/**
	 * 设置facetField
	 * 
	 * @return SolrQuery
	 * @param facetFields
	 * @param solrQuery
	 * @author 冯明雷
	 * @time 2016年4月26日下午2:17:50
	 */
	public static SolrQuery setFacetField(String[] facetFields,SolrQuery solrQuery){
		if (Validator.isNotNullOrEmpty(facetFields)) {
			solrQuery.set(FacetParams.FACET, true);
			for (String facetField : facetFields){
				if (facetField.split(":").length > 1) {
					solrQuery.addFacetQuery(facetField);
				}else{
					solrQuery.addFacetField(facetField);
				}
			}
		}
		return solrQuery;
	}

	/**
	 * 设置关键字搜索
	 * 
	 * @param groupField
	 * @param solrQuery
	 * @return
	 */
	public static void addFqKeyword(String keyword,SolrQuery solrQuery){
		solrQuery.setQuery(SkuItemParam.keyword + ":" + escape(keyword));
	}

	/**
	 * 设置精确搜索条件
	 */
	public static void addFqAccurateForStringList(SolrQuery solrQuery,List<String> words,String type){
		if (null != words && words.size() > 0) {
			String fq_keyword = "";
			int size = words.size();
			for (int i = 0; i < size; i++){
				fq_keyword += type + ":" + escape(words.get(i));
				if (i < size - 1) {
					fq_keyword += " OR ";
				}
			}
			solrQuery.addFilterQuery(fq_keyword);
		}
	}

	/**
	 * 设置精确搜索条件的同时打tag，用来facet
	 * 
	 * @return void
	 * @param solrQuery
	 * @param words
	 * @param type
	 * @author 冯明雷
	 * @time 2016年4月27日下午5:21:50
	 */
	public static void addFqAccurateForStringListWithTag(SolrQuery solrQuery,List<String> words,String type){
		if (null != words && words.size() > 0) {
			String fq_keyword = "(";
			int size = words.size();
			for (int i = 0; i < size; i++){
				fq_keyword += escape(words.get(i));
				if (i < size - 1) {
					fq_keyword += " OR ";
				}
			}
			fq_keyword += ")";
			solrQuery.addFilterQuery("{!tag=tag1}" + type + ":" + fq_keyword);
			solrQuery.addFacetField("{!ex=tag1}" + type);
		}
	}

	public static String escape(String s){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			// These characters are part of the query syntax and must be escaped
			if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':' || c == '^' || c == '[' || c == ']'
					|| c == '\"' || c == '{' || c == '}' || c == '~' || c == '*' || c == '?' || c == '|' || c == '&') {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 设置时间搜索条件
	 */
	public static void addFqTime(SolrQuery solrQuery){
		String fq_keyword = SkuItemParam.activeBeginTime + ":[* TO NOW]";
		solrQuery.addFilterQuery(fq_keyword);
	}

	/**
	 * 设置价格搜索条件
	 * 
	 * @return void
	 * @param solrQuery
	 * @param areaWord
	 * @param type
	 * @author 冯明雷
	 * @time 2016年4月29日下午4:29:55
	 */
	public static void addFqForPriceArea(SolrQuery solrQuery,List<String> priceAreaWords,String type){
		String fq_keyword = "{!tag=priceTag}" + type;
		try{
			Integer max_price = null;
			Integer min_price = null;
			min_price = Integer.parseInt(priceAreaWords.get(0));
			max_price = Integer.parseInt(priceAreaWords.get(1));
			if (min_price < 1) {
				min_price = 1;
			}
			fq_keyword += ":[" + min_price + " TO " + max_price + "]";
		}catch (Exception e){
			LOG.error(e.getMessage());
			fq_keyword += ":[0 TO *]";
		}

		if (Validator.isNotNullOrEmpty(fq_keyword))
			solrQuery.addFilterQuery(fq_keyword);

	}

}
