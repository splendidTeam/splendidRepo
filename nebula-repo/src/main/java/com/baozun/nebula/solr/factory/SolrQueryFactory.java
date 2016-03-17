package com.baozun.nebula.solr.factory;

import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.common.params.FacetParams;
import org.apache.solr.common.params.GroupParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utils.spring.SpringUtil;
import com.baozun.nebula.utilities.common.LangUtil;

public class SolrQueryFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(SolrQueryFactory.class);

	/**
	 * 创建查询SolrQuery 将QueryConditionCommand组合成SOLR查询语句
	 * 
	 * @param queryConditionCommand
	 * @return
	 */
	public static SolrQuery createSolrQuery(
			QueryConditionCommand queryConditionCommand, SolrQuery solrQuery) {
		if (Validator.isNullOrEmpty(queryConditionCommand.getKeyword())) {
			solrQuery.setQuery("*:*");
		} else {
			add_FQKEYWORD(queryConditionCommand.getKeyword(), solrQuery);
		}

		add_FQAccurateForStringList(solrQuery,
				queryConditionCommand.getChannelId(), SkuItemParam.channelId);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getCode(),
				SkuItemParam.itemCode);
		add_FQAccurateForStringList(solrQuery,
				queryConditionCommand.getCodeList(), SkuItemParam.itemCode);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getShopName(),
				SkuItemParam.shopName);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getSubTitle(),
				SkuItemParam.subTitle);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getTag(),
				SkuItemParam.tagName);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getRankavg(),
				SkuItemParam.rankavg);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getViewCount(), SkuItemParam.viewCount);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getItemCount(), SkuItemParam.itemCount);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getSalesCount(), SkuItemParam.salesCount);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getSubTitle(),
				SkuItemParam.seoTitle);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getSeoDescription(),
				SkuItemParam.seoDescription);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getSeoKeywords(),
				SkuItemParam.seoKeywords);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getIndustryName(),
				SkuItemParam.industryName);

		add_FQAccurateForString(solrQuery, queryConditionCommand.getSketch(),
				SkuItemParam.sketch);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getDescription(),
				SkuItemParam.description);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getTitle(),
				SkuItemParam.title);
		add_FQAccurateForLongMap(solrQuery,
				queryConditionCommand.getDynamicConditionMap(),
				SkuItemParam.dynamicCondition);
		add_FQAccurateForStringMap(solrQuery,
				queryConditionCommand.getDynamicConditionMapForString(),
				SkuItemParam.dynamicConditionValueForSearch);
		add_FQAccurateForLongMap(solrQuery,
				queryConditionCommand.getDynamicConditionWithOutSearchMap(),
				SkuItemParam.dynamicConditionWithOutSearch);
		add_FQAccurateForStringMap(solrQuery,
				queryConditionCommand
						.getDynamicConditionWithOutSearchMapForString(),
				SkuItemParam.dynamicConditionValueWithOutSearch);
		add_FQAccurateForCategoryStringMap(solrQuery,
				queryConditionCommand.getCategory_name(),
				SkuItemParam.categoryname);
		add_FQForArea(solrQuery, queryConditionCommand.getList_price(),
				SkuItemParam.list_price);
		add_FQForArea(solrQuery, queryConditionCommand.getSale_price(),
				SkuItemParam.sale_price);
		add_FQTime(solrQuery);
		add_FQAccurateForStringListExcludeConditions(solrQuery, queryConditionCommand.getExcludeMap());
		// setGroupBySolrQuery(queryConditionCommand,solrQuery);
		solrQuery.setRows(Integer.MAX_VALUE);
		addItemvisibilityQuery(queryConditionCommand, solrQuery);
		return solrQuery;
	}
	
	public static SolrQuery createSolrQueryI18n(
			QueryConditionCommand queryConditionCommand, SolrQuery solrQuery) {
		
		if (Validator.isNullOrEmpty(queryConditionCommand.getKeyword())) {
			solrQuery.setQuery("*:*");
		} else {
			add_FQKEYWORD(queryConditionCommand.getKeyword(), solrQuery);
		}
		String lang = LangUtil.getCurrentLang();
		String lang_ = lang+"_";
		add_FQAccurateForStringList(solrQuery,
				queryConditionCommand.getChannelId(), SkuItemParam.channelId);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getCode(),
				SkuItemParam.itemCode);
		add_FQAccurateForStringList(solrQuery,
				queryConditionCommand.getCodeList(), SkuItemParam.itemCode);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getShopName(),
				SkuItemParam.shopName);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getSubTitle(),
				SkuItemParam.subTitle);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getTag(),
				SkuItemParam.tagName);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getRankavg(),
				SkuItemParam.rankavg);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getViewCount(), SkuItemParam.viewCount);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getItemCount(), SkuItemParam.itemCount);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getSalesCount(), SkuItemParam.salesCount);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getSubTitle(),
				SkuItemParam.seoTitle);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getSeoDescription(),
				SkuItemParam.seoDescription);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getSeoKeywords(),
				SkuItemParam.seoKeywords);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getIndustryName(),
				SkuItemParam.industryName);

		add_FQAccurateForString(solrQuery, queryConditionCommand.getSketch(),
				SkuItemParam.sketch);
		add_FQAccurateForString(solrQuery,
				queryConditionCommand.getDescription(),
				SkuItemParam.description);
		add_FQAccurateForString(solrQuery, queryConditionCommand.getTitle(),
				SkuItemParam.title);
		add_FQAccurateForLongMap(solrQuery,
				queryConditionCommand.getDynamicConditionMap(),
				SkuItemParam.dynamicCondition);
		add_FQAccurateForStringMap(solrQuery,
				queryConditionCommand.getDynamicConditionMapForString(),
				SkuItemParam.dynamicConditionValueForSearch);
		add_FQAccurateForLongMap(solrQuery,
				queryConditionCommand.getDynamicConditionWithOutSearchMap(),
				SkuItemParam.dynamicConditionWithOutSearch);
		add_FQAccurateForStringMap(solrQuery,
				queryConditionCommand
						.getDynamicConditionWithOutSearchMapForString(),
				SkuItemParam.dynamicConditionValueWithOutSearch);
		//国际化分类查询
		add_FQAccurateForCategoryStringMap(solrQuery,
				queryConditionCommand.getCategory_name(),
				SkuItemParam.dynamic_category_name+lang_);
		add_FQForArea(solrQuery, queryConditionCommand.getList_price(),
				SkuItemParam.list_price);
		add_FQForArea(solrQuery, queryConditionCommand.getSale_price(),
				SkuItemParam.sale_price);
		add_FQTime(solrQuery);
		add_FQAccurateForStringListExcludeConditions(solrQuery, queryConditionCommand.getExcludeMap());
		// setGroupBySolrQuery(queryConditionCommand,solrQuery);
		solrQuery.setRows(Integer.MAX_VALUE);
		return solrQuery;
	}

	/**
	 * 创建查询SolrQuery 创建以CODE主的查询
	 * 
	 * @param queryConditionCommand
	 * @return
	 */
	public static SolrQuery createSolrQueryByCode(
			QueryConditionCommand queryConditionCommand, SolrQuery solrQuery) {
		solrQuery.setQuery("*:*");
		if (null == queryConditionCommand.getCodeList()
				|| queryConditionCommand.getCodeList().size() < 1) {
			solrQuery.setQuery("code:0");
		} else {
			add_FQAccurateForStringList(solrQuery,
					queryConditionCommand.getCodeList(), SkuItemParam.itemCode);
		}
		solrQuery.setRows(Integer.MAX_VALUE);
		return solrQuery;
	}

	/**
	 * 设置fields
	 * 
	 * @param facetFields
	 * @param solrQuery
	 * @return
	 */
	public static SolrQuery setFacetField(String[] facetFields,
			SolrQuery solrQuery) {
		if (Validator.isNotNullOrEmpty(facetFields)) {
			solrQuery.set(FacetParams.FACET, true);
			for (String facetField : facetFields) {
				if (facetField.split(":").length > 1) {
					solrQuery.addFacetQuery(facetField);
				} else {
					solrQuery.addFacetField(facetField);
				}
			}
		}
		return solrQuery;
	}

	/**
	 * 分组设置
	 * 
	 * @param queryConditionCommand
	 * @param solrQuery
	 * @return
	 */
	public static SolrQuery setGroup(
			QueryConditionCommand queryConditionCommand, SolrQuery solrQuery) {
		String groupName = "tagId";
		solrQuery.set(GroupParams.GROUP, true);
		solrQuery.set(GroupParams.GROUP_TOTAL_COUNT, true);
		solrQuery.set(GroupParams.GROUP_LIMIT, 200);
		solrQuery.set(GroupParams.GROUP_FORMAT, "grouped");
		String groupNameList = "";
		String groupSortList = "";
		if (null != queryConditionCommand.getGroupNames()
				&& queryConditionCommand.getGroupNames().size() > 0) {
			List<String> groupNames = queryConditionCommand.getGroupNames();
			for (int i = 0; i < groupNames.size(); i++) {
				groupNameList += groupNames.get(i).toString();
				if (i < groupNames.size() - 1) {
					groupNameList += ",";
				}
			}
			solrQuery.set(GroupParams.GROUP_FIELD, groupNameList);
		} else {
			solrQuery.set(GroupParams.GROUP_FIELD, groupName);
		}

		if (null != queryConditionCommand.getGroupSorts()
				&& queryConditionCommand.getGroupSorts().size() > 0) {
			List<String> groupSorts = queryConditionCommand.getGroupSorts();
			for (int i = 0; i < groupSorts.size(); i++) {
				groupSortList += groupSorts.get(i).toString();
				if (i < groupSorts.size() - 1) {
					groupSortList += ",";
				}
			}
			solrQuery.set(GroupParams.GROUP_SORT, groupSortList);
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
	public static void setSort(SolrQuery solrQuery, SolrOrderSort[] order) {
		if (Validator.isNullOrEmpty(order)) {
			/*
			 * 设置默认排序
			 */
			solrQuery.addSortField(SkuItemParam.default_sort, ORDER.desc);
		} else {
			for (SolrOrderSort sort : order) {
				solrQuery.addSortField(sort.getField(), sort.getType()
						.equalsIgnoreCase(Sort.ASC) ? ORDER.asc : ORDER.desc);
			}
		}
	}

	/**
	 * 根据queryConditionCommand设置SolrQuery分组属性
	 * 
	 * @param queryConditionCommand
	 * @param solrQuery
	 * @return
	 */
	public static SolrQuery setFacetBySolrQuery(
			QueryConditionCommand queryConditionCommand, SolrQuery solrQuery) {
		solrQuery.set(FacetParams.FACET, true);
		if (Validator.isNotNullOrEmpty(queryConditionCommand.getFacetNames())) {
			for (String facetStr : queryConditionCommand.getFacetNames()) {
				solrQuery.set(FacetParams.FACET_FIELD, facetStr);
			}
		}

		if (Validator.isNotNullOrEmpty(queryConditionCommand.getFacetSorts())) {
			for (String facetSort : queryConditionCommand.getFacetSorts()) {
				solrQuery.set(FacetParams.FACET_SORT, facetSort);
			}
		}

		solrQuery.set(FacetParams.FACET_DATE_START, "");
		solrQuery.set(FacetParams.FACET_DATE_END, "");
		// FACET_DATE_GAP取值为+1MONTH表示将FACET_DATE_START和FACET_DATE_END之间的记录按每个月进行分组
		// +在这里必须改用%2B代替
		solrQuery.set(FacetParams.FACET_DATE_GAP, "%2B1MONTH");
		// false表示经过FACET_DATE_GAP分组后，最后分组是最后一个月一号到下一个月的一号时间段，为true则是最后一个月一号到二十五号时间段
		solrQuery.set(FacetParams.FACET_DATE_HARD_END, false);
		// FACET_MISSING默认为"",为on时则代表查出Facet为null的记录
		solrQuery.set(FacetParams.FACET_MISSING, "");
		// FACET_OFFSET和FACET_LIMIT一起配合达到分页效果
		solrQuery.set(FacetParams.FACET_OFFSET, 0);
		// Facet字段返回条数
		solrQuery.set(FacetParams.FACET_LIMIT, 200);
		return solrQuery;
	}

	/**
	 * 根据queryConditionCommand设置SolrQuery分组属性
	 * 
	 * @param queryConditionCommand
	 * @param solrQuery
	 * @return
	 */
	public static SolrQuery setGroupBySolrQuery(
			QueryConditionCommand queryConditionCommand, SolrQuery solrQuery) {
		solrQuery.set(GroupParams.GROUP, true);
		if (Validator.isNotNullOrEmpty(queryConditionCommand.getGroupNames())) {
			for (String groupStr : queryConditionCommand.getGroupNames()) {
				solrQuery.set(GroupParams.GROUP_FIELD, groupStr);
			}
		}

		if (Validator.isNotNullOrEmpty(queryConditionCommand.getGroupSorts())) {
			for (String groupSort : queryConditionCommand.getGroupSorts()) {
				solrQuery.set(GroupParams.GROUP_SORT, groupSort);
			}
		}

		solrQuery.set(GroupParams.GROUP_OFFSET, 0);
		// Group字段返回条数
		solrQuery.set(GroupParams.GROUP_LIMIT, 200);

		solrQuery.set(GroupParams.GROUP_TOTAL_COUNT, true);

		solrQuery.set(GroupParams.GROUP_FORMAT, "grouped");
		return solrQuery;
	}

	/**
	 * 设置精确搜索条件
	 */
	public static void add_FQAccurateForString(SolrQuery solrQuery,
			String word, String type) {
		if (Validator.isNotNullOrEmpty(word)) {
			String fq_keyword = type + ":" + escape(word);
			solrQuery.addFilterQuery(fq_keyword);
		}
	}

	/**
	 * 设置关键字搜索
	 * 
	 * @param groupField
	 * @param solrQuery
	 * @return
	 */
	public static void add_FQKEYWORD(String keyword, SolrQuery solrQuery) {
		solrQuery.setQuery(SkuItemParam.keyword + ":" + escape(keyword));
	}

	/**
	 * 设置价格搜索条件
	 */
	public static void add_FQForArea(SolrQuery solrQuery, String areaWord,
			String type) {
		String fq_keyword = "";
		if (Validator.isNotNullOrEmpty(areaWord)) {
			fq_keyword += type + ":" + areaWord;
			solrQuery.addFilterQuery(fq_keyword);
		}
	}

	/**
	 * 设置精确搜索条件
	 */
	public static void add_FQAccurateForStringList(SolrQuery solrQuery,
			List<String> words, String type) {
		if (null != words && words.size() > 0) {
			String fq_keyword = "";
			int size = words.size();
			for (int i = 0; i < size; i++) {
				fq_keyword += type + ":" + escape(words.get(i));
				if (i < size - 1) {
					fq_keyword += " OR ";
				}
			}
			solrQuery.addFilterQuery(fq_keyword);
		}
	}

	/**
	 * 设置时间搜索条件
	 */
	public static void add_FQTime(SolrQuery solrQuery) {
		String fq_keyword = SkuItemParam.activeBeginTime + ":[* TO NOW]";
		solrQuery.addFilterQuery(fq_keyword);
	}

	/**
	 * 设置动态条件
	 */
	public static void add_FQAccurateForLongMap(SolrQuery solrQuery,
			Map<Long, List<Long>> words, String type) {
		if (null != words && words.size() > 0) {
			for (Long key : words.keySet()) {
				String fq_keyword = "";
				List<Long> info = words.get(key);
				int size = info.size();
				int infoCount = 0;
				for (int i = 0; i < size; i++) {
					fq_keyword += type + key + ":"
							+ String.valueOf(info.get(i));
					if (infoCount < size - 1) {
						fq_keyword += " OR ";
					}
					infoCount++;
				}
				solrQuery.addFilterQuery(fq_keyword);
			}

		}
	}

	public static void add_FQAccurateForStringMap(SolrQuery solrQuery,
			Map<Long, List<String>> words, String type) {
		if (null != words && words.size() > 0) {
			for (Long key : words.keySet()) {
				String fq_keyword = "";
				List<String> info = words.get(key);
				int size = info.size();
				int infoCount = 0;
				for (int i = 0; i < size; i++) {
					fq_keyword += type + key + ":" + info.get(i);
					if (infoCount < size - 1) {
						fq_keyword += " OR ";
					}
					infoCount++;
				}
				solrQuery.addFilterQuery(fq_keyword);
			}

		}
	}

	/**
	 * 设置分类查询语句
	 * 
	 * @param solrQuery
	 * @param words
	 * @param type
	 */
	public static void add_FQAccurateForCategoryStringMap(SolrQuery solrQuery,
			Map<String, String> words, String type) {
		if (null != words && words.size() > 0) {
			String fq_keyword = "";
			int AllCount = 0;
			int AllInfoSize = words.size();
			for (String key : words.keySet()) {
				fq_keyword += type + key + ":" + escape(words.get(key));
				if (AllCount < AllInfoSize - 1) {
					fq_keyword += " AND ";
				}
				AllCount++;
			}
			solrQuery.addFilterQuery(fq_keyword);
		}
	}

	/**
	 * 设置精确排除条件
	 */
	public static void add_FQAccurateForStringListExcludeConditions(
			SolrQuery solrQuery, Map<String, List<String>> words) {
		if (null != words && words.size() > 0) {
			String fq_keyword = "";
			int count = words.size();
			for (String key : words.keySet()) {
				count--;
				List<String> valueList = words.get(key);
				if (null != valueList && valueList.size() > 0) {
					int size = valueList.size();
					fq_keyword += key + ":(";
					for (int i = 0; i < size; i++) {
						fq_keyword += "-" + escape(valueList.get(i));
						if (i < size - 1) {
							fq_keyword += " AND ";
						}
					}
					fq_keyword += ")";
					solrQuery.addFilterQuery(fq_keyword);
					fq_keyword = "";
				}
			}
		}
	}

	public static String escape(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// These characters are part of the query syntax and must be escaped
			if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '('
					|| c == ')' || c == ':' || c == '^' || c == '[' || c == ']'
					|| c == '\"' || c == '{' || c == '}' || c == '~'
					|| c == '*' || c == '?' || c == '|' || c == '&') {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}
	public static  void addItemvisibilityQuery(QueryConditionCommand queryConditionCommand, SolrQuery solrQuery){
		SdkMataInfoManager sdkMataInfoManager = (SdkMataInfoManager) SpringUtil.getBean("sdkMataInfoManager");
		String visibility =sdkMataInfoManager.findValue("product.visibility");
		if(!(visibility!= null && visibility.equals("true"))){
			return;
		}
		String allDisplay = queryConditionCommand.getAllDisplay();
		
		if(allDisplay != null){
			if(allDisplay.equals(SkuItemParam.all_display_y)){
				add_FQAccurateForString(solrQuery,allDisplay, "allDisplay");
			}else{
				//两个条件是  或 关系
				StringBuilder sb = new StringBuilder();
				List<String> visiblePersons = queryConditionCommand.getVisiblePersons();
				if(visiblePersons==null || visiblePersons.size()==0){
					add_FQAccurateForString(solrQuery,allDisplay, "allDisplay");
				}else{
					for (int i = 0; i < visiblePersons.size(); i++) {
						String  vp = visiblePersons.get(i);
						sb.append(" OR visiblePersons:"+vp+"");
					
					}
					solrQuery.addFilterQuery("allDisplay:"+SkuItemParam.all_display_y+sb.toString());
				}
				
			}
		}
	}
}
