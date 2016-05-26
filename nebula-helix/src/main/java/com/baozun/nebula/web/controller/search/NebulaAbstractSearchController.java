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
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.model.product.ItemCollection;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.FacetGroup;
import com.baozun.nebula.search.FacetParameter;
import com.baozun.nebula.search.FacetType;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.search.manager.SearchManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.factory.NebulaSolrQueryFactory;
import com.baozun.nebula.solr.utils.FilterUtil;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

public abstract class NebulaAbstractSearchController extends BaseController{

	/** 逗号分隔符 */
	private final static String	SEPARATORCHARS_COMMA	= ",";

	/** -分隔符 */
	private final static String	SEPARATORCHARS_MINUS	= "-";

	/** 空格 */
	private final static String	SEPARATORCHARS_SPACE	= " ";

	/** 属性 */
	private static final String	PROPERTY_VARIABLE		= "p";

	/** 分类 */
	private static final String	CATEGORY_VARIABLE		= "c";

	@Autowired
	private SearchManager		searchManager;

	/**
	 * 将 searchCommand 里面的filterConditionStr 转成 FacetParameter 用做后面solr查询
	 * 
	 * @param searchCommand
	 */
	protected void searchParamProcess(SearchCommand searchCommand,ItemCollection collection){
		List<FacetParameter> facetParameters = new ArrayList<FacetParameter>();

		// ***************************************导航部分
		if (Validator.isNotNullOrEmpty(collection)) {
			SearchCommand temp = collectionToSearchCommand(collection);
			if (temp != null && temp.getFacetParameters() != null) {
				facetParameters.addAll(temp.getFacetParameters());
			}
		}

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

		// ***************************************价格范围筛选部分
		List<FacetParameter> facetParametersWithRange = setFacetParametersWithRange(searchCommand);
		if (Validator.isNotNullOrEmpty(facetParametersWithRange)) {
			facetParameters.addAll(facetParametersWithRange);
		}

		searchCommand.setFacetParameters(facetParameters);
	}

	/**
	 * 将后台设置的导航的商品范围，转成searchCommand对象
	 * 
	 * @param collection
	 * @return
	 */
	protected SearchCommand collectionToSearchCommand(ItemCollection collection){
		SearchCommand searchCommand = new SearchCommand();

		String facetParameters = collection.getFacetParameters();
		List<FacetParameter> params = JSON.parseArray(facetParameters, FacetParameter.class);
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
	protected void setFacet(SolrQuery solrQuery,SearchCommand searchCommand){
		// facetFileds，set防止重复数据
		Set<String> facetFields = new HashSet<String>();

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

		// ***************************************************设置分类的facet
		// 如果最后点击的是分类
		if (CATEGORY_VARIABLE.equals(variable)) {
			facetFields.add("{!ex=lastFilterTag}" + SkuItemParam.category_tree);
		}else{
			facetFields.add(SkuItemParam.category_tree);
		}

		// **************************************************设置属性的facet
		List<SearchConditionCommand> cmdList = searchManager.findConditionByCategoryIdsWithCache(new ArrayList<Long>());
		// 如果为null或数量小于1
		if (null == cmdList || cmdList.size() < 1) {
			return;
		}

		for (SearchConditionCommand cmd : cmdList){
			// 如果筛选条件是常规类型
			if (SearchCondition.NORMAL_TYPE.equals(cmd.getType())) {
				// 筛选条件对应的属性id
				Long propertyId = cmd.getPropertyId();

				// 如果最后点击的是属性
				if (PROPERTY_VARIABLE.equals(variable)) {
					String filter = PROPERTY_VARIABLE + propertyId;
					if (lastFilerStr.equals(filter)) {
						facetFields.add("{!ex=lastFilterTag}" + SkuItemParam.dynamicCondition + propertyId);
					}else{
						facetFields.add(SkuItemParam.dynamicCondition + propertyId);
					}
				}else{
					facetFields.add(SkuItemParam.dynamicCondition + propertyId);
				}
			}else if (SearchCondition.SALE_PRICE_TYPE.equals(cmd.getType())) {
				// 如果筛选条件是价格区间类型

				// 根据筛选条件查询筛选条件项
				List<SearchConditionItemCommand> searchConditionItemCommands = searchManager
						.findCoditionItemByCoditionIdWithCache(cmd.getId());

				// 循环各个筛选条件项，用facetQuery获得数量
				for (SearchConditionItemCommand scItemCmd : searchConditionItemCommands){
					if (null != scItemCmd) {
						Integer min = scItemCmd.getAreaMin();
						Integer max = scItemCmd.getAreaMax();
						if (null != min && null != max && min <= max) {
							String areaStr = FilterUtil.paramConverToArea(min.toString(), max.toString());
							StringBuilder sb = new StringBuilder();
							sb.append(SkuItemParam.sale_price).append(":").append(areaStr);
							facetFields.add(sb.toString());
						}
					}
				}
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
	protected Boost createBoost(SearchCommand searchCommand,ItemCollection collection){
		Boost boost = new Boost();

		if (Validator.isNotNullOrEmpty(searchCommand.getSortStr())) {
			return boost;
		}

		// 设置商品置顶
		setBoostBq(boost, collection);

		// 搜索搜索关键字不为空
		setBoostQfAndPf(boost, searchCommand.getSearchWord());

		// 用函数计算某个字段的权重
		setBoostBf(boost, searchCommand);

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
	 * searchCommand中的范围相关条件设置
	 * 
	 * @return List<FacetParameter>
	 * @param searchCommand
	 * @author 冯明雷
	 * @time 2016年4月26日下午6:26:38
	 */
	protected List<FacetParameter> setFacetParametersWithRange(SearchCommand searchCommand){
		List<FacetParameter> facetParameters = new ArrayList<FacetParameter>();

		String rangeConditionStr = searchCommand.getPriceRangeConditionStr();
		// 如果范围筛选条件不为空
		if (Validator.isNotNullOrEmpty(rangeConditionStr)) {
			List<String> values = new ArrayList<String>();
			
			String[] strs=rangeConditionStr.split(SEPARATORCHARS_COMMA);
			for (String rangeStr : strs){
				String min=StringUtils.substringBeforeLast(rangeStr, SEPARATORCHARS_MINUS);
				String max=StringUtils.substringAfterLast(rangeStr, SEPARATORCHARS_MINUS);
				String areaStr = FilterUtil.paramConverToArea(min, max);
				
				values.add(areaStr);
			}
			
			FacetParameter facetParameter = new FacetParameter(SkuItemParam.sale_price);
			facetParameter.setValues(values);
			facetParameter.setFacetType(FacetType.RANGE);

			facetParameters.add(facetParameter);			
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
	protected void setBoostBq(Boost boost,ItemCollection collection){
		// 根据商品指定排序表中的数据，将商品置顶
		if (collection != null) {
			String sequence = collection.getSequence();
			if (Validator.isNotNullOrEmpty(sequence)) {
				String[] itemIdStrs = sequence.split(SEPARATORCHARS_COMMA);
				if (itemIdStrs != null && itemIdStrs.length > 0) {
					StringBuffer bq = new StringBuffer();
					for (int i = 0; i < itemIdStrs.length; i++){
						// 最高分是指定商品的个数*100
						Integer score = itemIdStrs.length * 100;

						// 商品id
						String itemId = itemIdStrs[i];

						if (Validator.isNotNullOrEmpty(itemId)) {
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
	protected void setBoostQfAndPf(Boost boost,String searchKeyWord){
		if (Validator.isNullOrEmpty(searchKeyWord)) {
			return;
		}
		boolean i18n = LangProperty.getI18nOnOff();

		// 转义特殊字符
		searchKeyWord = NebulaSolrQueryFactory.escape(searchKeyWord);

		// 设置serachkeyword的权重，匹配到哪个字段的时候先显示出来
		StringBuffer qf = new StringBuffer();

		// 各个字段的分数加起来总共等于1
		qf.append(SkuItemParam.style + "^0.3" + SEPARATORCHARS_SPACE);
		qf.append(SkuItemParam.itemCode + "^0.25" + SEPARATORCHARS_SPACE);
		qf.append(SkuItemParam.allCategoryCodes + "^0.15" + SEPARATORCHARS_SPACE);
		if (i18n) {
			double titleScore = 0.15;
			int langCount = MutlLang.i18nLangs().size();
			double score = titleScore / langCount;

			for (int i = 0; i < langCount; i++){
				String lang = MutlLang.i18nLangs().get(i);
				qf.append(SkuItemParam.dynamic_title + lang + "^" + score + SEPARATORCHARS_SPACE);
				if (i < langCount - 1) {
					qf.append(SkuItemParam.dynamic_subTitle + lang + "^" + score + SEPARATORCHARS_SPACE);
				}else{
					qf.append(SkuItemParam.dynamic_subTitle + lang + "^" + score);
				}
			}
		}else{
			qf.append(SkuItemParam.title + "^0.15" + SEPARATORCHARS_SPACE);
			qf.append(SkuItemParam.subTitle + "^0.15");
		}

		boost.setQf(qf.toString());

		// 需要匹配的字段,以空格分隔
		StringBuffer pf = new StringBuffer();
		pf.append(SkuItemParam.style + SEPARATORCHARS_SPACE);// 款号
		pf.append(SkuItemParam.itemCode + SEPARATORCHARS_SPACE);// 商品code
		pf.append(SkuItemParam.allCategoryCodes + SEPARATORCHARS_SPACE);// 分类code
		if (i18n) {
			int langCount = MutlLang.i18nLangs().size();
			for (int i = 0; i < langCount; i++){
				String lang = MutlLang.i18nLangs().get(i);

				pf.append(SkuItemParam.dynamic_title + lang + SEPARATORCHARS_SPACE);
				if (i < langCount - 1) {
					pf.append(SkuItemParam.dynamic_subTitle + lang + SEPARATORCHARS_SPACE);
				}else{
					pf.append(SkuItemParam.dynamic_subTitle + lang);
				}
			}
		}else{
			pf.append(SkuItemParam.title + SEPARATORCHARS_SPACE);// 商品名称
			pf.append(SkuItemParam.subTitle);// 副标题
		}

		boost.setPf(pf.toString());
	}

	/**
	 * 用函数计算某个字段的权重(项目自己去实现)
	 * 
	 * @return void
	 * @param boost
	 * @param searchCommand
	 * @author 冯明雷
	 * @time 2016年5月9日下午2:30:02
	 */
	protected void setBoostBf(Boost boost,SearchCommand searchCommand){

	}

	/**
	 * 设置排除显示的facetGroup
	 * 
	 * @return void
	 * @param collection
	 * @param facetGroups
	 * @author 冯明雷
	 * @time 2016年5月16日下午3:22:30
	 */
	protected void setExcludeFacetGroup(ItemCollection collection,List<FacetGroup> facetGroups){
		if (collection != null) {
			String facetParameters = collection.getFacetParameters();
			List<FacetParameter> params = JSON.parseArray(facetParameters, FacetParameter.class);
			if (params != null) {
				for (FacetParameter facetParameter : params){
					if (FacetType.PROPERTY.equals(facetParameter.getFacetType())) {
						for (FacetGroup facetGroup : facetGroups){
							Long id = facetGroup.getId();
							if (id != null && facetParameter.getName().equals(SkuItemParam.dynamicCondition + id.intValue())
									&& !facetGroup.getIsCategory()) {
								facetGroup.setIsShow(false);
								break;
							}
						}
					}
				}
			}
		}
	}

}
