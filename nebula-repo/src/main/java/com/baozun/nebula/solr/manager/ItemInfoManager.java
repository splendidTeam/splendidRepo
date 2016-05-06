package com.baozun.nebula.solr.manager;

import java.util.List;

import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.command.ItemSolrI18nCommand;
public interface ItemInfoManager {
	
	/**
	 * 根据IDList查询商品
	 * @param itemId
	 * @return
	 */
	public List<ItemSolrCommand> findItemCommandByItemId(List<Long> itemId);
	
	List<ItemSolrI18nCommand> findItemCommandByItemIdI18n(List<Long> itemId);

	/**
	 * 查询所有商品
	 * @param itemId
	 * @return
	 */
	public List<ItemSolrCommand> findItemCommand();
	
	/**
	 * 根据IDList查询商品
	 * @param itemId
	 * @return
	 */	
	public List<ItemSolrCommand> setItemSolrCommand(List<Long> itemIds);
	
	/**
	 * 商品的一些扩展信息，比如销量、收藏量、评论量、评分等。
	 * @param itemId
	 * @return
	 */
	public ItemSolrCommand findItemExtraViewCommand(Long itemId);
}
