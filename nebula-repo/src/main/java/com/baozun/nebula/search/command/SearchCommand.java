package com.baozun.nebula.search.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.search.FacetParameter;
import com.baozun.nebula.search.FacetTagType;
import com.feilong.core.Validator;

public class SearchCommand{

	/**
	 * 搜索关键字
	 */
	private String						searchWord;

	/**
	 * 过滤条件(属性)<br/>
	 * 参数格式：属性Id-属性值Id,属性Id-属性值Id....
	 */
	private String						filterConditionStr;

	/**
	 * 过滤条件(分类)<br/>
	 * 参数格式：父级分类id-父级分类id-分类id,父级分类id-父级分类id-分类id......
	 */
	private String						categoryConditionStr;

	/**
	 * 导航Id
	 */
	private Long						navigationId;

	/**
	 * 导航
	 */
	private String						navigationConditionStr;

	/**
	 * 过滤条件参数顺序，用户页面点击筛选项时候的顺序。 记录这个顺序用于后面facet tag作用<br/>
	 * 参数格式：c分类id,r价格,p属性id,p属性id,c分类id,r价格......
	 */
	private String						filterParamOrder;

	/**
	 * 价格范围的过滤条件：比如价格范围等<br/>
	 * 参数格式：开始价格-结束价格
	 */
	private String						priceRangeConditionStr;

	/**
	 * 后端查询参数（solr.fq）,从这个值里面体现filterquery之间的关系
	 */
	private List<FacetParameter>		facetParameters;

	/**
	 * 排序
	 */
	private String						sortStr;

	/**
	 * 页大小
	 */
	private Integer						pageSize	= 20;

	/**
	 * 第几页 zero based
	 */
	private Integer						pageNumber;

	/**
	 * 排除字段查询语句 这里传入的应该是直接写好的fq。如：-id:45,排除id为45的数据
	 */
	private List<ExcludeSearchCommand>	excludeList;

	/**
	 * 打tag的facet属性
	 */
	private FacetTagType				facetTagType;

	/**
	 * 自定义数据，未来某些简单或者过于定制的数据扩展可以直接在这里处理。
	 */
	private Map<String, Object>			extraData	= new HashMap<String, Object>(0);

	public String getFilterParamOrder(){
		return filterParamOrder;
	}

	public void setFilterParamOrder(String filterParamOrder){
		this.filterParamOrder = filterParamOrder;
	}

	public String getSearchWord(){
		return searchWord;
	}

	public String getFilterConditionStr(){
		return filterConditionStr;
	}

	public String getSortStr(){
		return sortStr;
	}

	public Integer getPageSize(){
		return pageSize;
	}

	public Integer getPageNumber(){
		return pageNumber;
	}

	public void setSearchWord(String searchWord){
		this.searchWord = searchWord;
	}

	public void setFilterConditionStr(String filterConditionStr){
		this.filterConditionStr = filterConditionStr;
	}

	public List<FacetParameter> getFacetParameters(){
		return facetParameters;
	}

	public void setFacetParameters(List<FacetParameter> facetParameters){
		this.facetParameters = facetParameters;
	}

	public void setSortStr(String sortStr){
		this.sortStr = sortStr;
	}

	public void setPageSize(Integer pageSize){
		this.pageSize = pageSize;
	}

	public void setPageNumber(Integer pageNumber){
		this.pageNumber = pageNumber;
	}

	/**
	 * get categoryConditionStr
	 * 
	 * @return categoryConditionStr
	 */
	public String getCategoryConditionStr(){
		return categoryConditionStr;
	}

	/**
	 * set categoryConditionStr
	 * 
	 * @param categoryConditionStr
	 */
	public void setCategoryConditionStr(String categoryConditionStr){
		this.categoryConditionStr = categoryConditionStr;
	}

	/**
	 * get navigationConditionStr
	 * 
	 * @return navigationConditionStr
	 */
	public String getNavigationConditionStr(){
		return navigationConditionStr;
	}

	/**
	 * set navigationConditionStr
	 * 
	 * @param navigationConditionStr
	 */
	public void setNavigationConditionStr(String navigationConditionStr){
		this.navigationConditionStr = navigationConditionStr;
	}

	/**
	 * get navigationId
	 * 
	 * @return navigationId
	 */
	public Long getNavigationId(){
		return navigationId;
	}

	/**
	 * set navigationId
	 * 
	 * @param navigationId
	 */
	public void setNavigationId(Long navigationId){
		this.navigationId = navigationId;
	}

	/**
	 * get priceRangeConditionStr
	 * 
	 * @return priceRangeConditionStr
	 */
	public String getPriceRangeConditionStr(){
		return priceRangeConditionStr;
	}

	/**
	 * set priceRangeConditionStr
	 * 
	 * @param priceRangeConditionStr
	 */
	public void setPriceRangeConditionStr(String priceRangeConditionStr){
		this.priceRangeConditionStr = priceRangeConditionStr;
	}

	public List<ExcludeSearchCommand> getExcludeList(){
		return excludeList;
	}

	public void setExcludeList(List<ExcludeSearchCommand> excludeList){
		this.excludeList = excludeList;
	}

	/**
	 * @return the extraData
	 */
	public Map<String, Object> getExtraData(){
		return extraData;
	}

	/**
	 * @param extraData
	 *            the extraData to set
	 */
	public void setExtraData(Map<String, Object> extraData){
		this.extraData = extraData;
	}

	public FacetTagType getFacetTagType(){
		return facetTagType;
	}

	public void setFacetTagType(FacetTagType facetTagType){
		this.facetTagType = facetTagType;
	}
	
	/**
	 * 获取最后一个筛选的filter
	 * @return String
	 * @author 冯明雷
	 * @time 2016年6月30日下午2:28:14
	 */
	public String getLastFilter(){
		if(Validator.isNotNullOrEmpty(this.filterParamOrder)){
			String[] strs = filterParamOrder.split(",");
			return strs[strs.length - 1];
		}
		return null;
	}

}
