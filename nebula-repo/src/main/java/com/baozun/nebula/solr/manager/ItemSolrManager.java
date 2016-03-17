package com.baozun.nebula.solr.manager;

import java.util.List;

import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.ItemDataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SuggestCommand;
import com.baozun.nebula.solr.utils.SolrOrderSort;

public interface ItemSolrManager {

	/**
	 * 查询结果集:
	 * @param rows 		   每页个数
	 * @param queryConditionCommand 查询条件
	 * @param facetFields 维度
	 * @param order 升序/降序
	 * @param groupField 分组字段
	 * @param currentPage 当前页
	 * @return DataFromSolr
	 */
	@Deprecated
	DataFromSolr queryItemForAll(int rows,QueryConditionCommand queryConditionCommand,String[] facetFields, SolrOrderSort[] order, String groupField,Integer currentPage);
	
	/**
	 * 查询结果集:
	 * 改方法新增根据CODE list查找对应商品，并且支持设置自定义分组
	 * @param rows 		   每页个数
	 * @param queryConditionCommand 查询条件
	 * @param facetFields 维度
	 * @param order 升序/降序
	 * @param groupField 分组字段
	 * @param currentPage 当前页
	 * @return DataFromSolr
	 * 
	 */
	ItemDataFromSolr queryAllEligibleItemData(int rows,QueryConditionCommand queryConditionCommand,String[] facetFields, SolrOrderSort[] order, String groupField,Integer currentPage);
	
	
	
	
	/**
	 * 查询结果集,并且强制不允许group,此方法会忽略metainfo的hasStyle
	 * @param rows 		   每页个数
	 * @param queryConditionCommand 查询条件
	 * @param facetFields 维度
	 * @param order 升序/降序
	 * @param groupField 分组字段
	 * @param currentPage 当前页
	 * @return DataFromSolr
	 */
	DataFromSolr queryItemForAllNotGroup(int rows,QueryConditionCommand queryConditionCommand,String[] facetFields, SolrOrderSort[] order, Integer currentPage);
	
	
	/**
	 * 查询结果集:
	 * @param queryConditionCommand 查询条件
	 * @return DataFromSolr
	 */
	List<ItemResultCommand> queryItemByCode(QueryConditionCommand queryConditionCommand);
	
	/**
	 * 查询单个商品(目前暂不使用)
	 * @return ItemSolrCommand
	 */
	ItemSolrCommand queryBySku(QueryConditionCommand queryConditionCommand);

	/**
	 * 保存：根据商品Id将商品从SOLR中删除
	 * @param ids 传入的商品Id
	 */
	Boolean deleteItem(List<Long> ids);
	
	/**
	 * 将solr中所有信息清除
	 */
	Boolean cleanAll();

	/**
	 * 保存或更新：会先将solr中对应的商品删除，然后将修改后的商品信息刷入
	 * @param ids 传入的新商品的信息
	 */
	Boolean saveOrUpdateItem(List<Long> itemIds);
	
	 Boolean saveOrUpdateItemI18n(List<Long> itemIds);
	/**
	 * 刷新solr
	 */
	Boolean reRefreshAllItem();
	
	/**
	 * 拼音联想
	 */
	SuggestCommand keywordSpellSpeculation(String keyWord);

}
