package com.baozun.nebula.solr.command;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.solr.utils.PaginationForSolr;

public class ItemDataFromSolr {
	/**
	 * 聚合后前数
	 */
	private Long number;
	
	/**
	 * 商品信息无分组
	 */
	private PaginationForSolr<ItemForSolrCommand> items;
	
	/**
	 * 商品信息无分组（提供商品所有信息）
	 */
	private PaginationForSolr<ItemSolrCommand> itemsListWithOutGroup;
	
	/**
	 * 商品信息无分组（提供商品所有信息）
	 */
	private PaginationForSolr<List<ItemSolrCommand>> itemsListWithGroup;
	
	/**
	 * 商品fenlei信息
	 */
	private ItemCategoryCommand itemCategoryCommand;
	
	
	/**
	 * Facet聚合结果信息
	 * 
	 * id:[valueid1=XXXX,valueid2=XXX],id:[valueid1=XXXX,valueid2=XXX]
	 * 
	 */
	private Map<String, Map<String, Long>> facetMap;
	
	/**
	 * FacetQuery聚合结果信息
	 * 
	 * id1:[ 0 TO 100]=XXX,id2:[ 0 TO 100]=XXX
	 * 
	 */
	private Map<String, Integer> facetQueryMap;

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}


	/**
	 * @return the items
	 */
	public PaginationForSolr<ItemForSolrCommand> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(PaginationForSolr<ItemForSolrCommand> items) {
		this.items = items;
	}

	public Map<String, Map<String, Long>> getFacetMap() {
		return facetMap;
	}

	public void setFacetMap(Map<String, Map<String, Long>> facetMap) {
		this.facetMap = facetMap;
	}

	

	public ItemCategoryCommand getItemCategoryCommand() {
		return itemCategoryCommand;
	}

	public void setItemCategoryCommand(ItemCategoryCommand itemCategoryCommand) {
		this.itemCategoryCommand = itemCategoryCommand;
	}

	public Map<String, Integer> getFacetQueryMap() {
		return facetQueryMap;
	}

	public void setFacetQueryMap(Map<String, Integer> facetQueryMap) {
		this.facetQueryMap = facetQueryMap;
	}

	public PaginationForSolr<ItemSolrCommand> getItemsListWithOutGroup() {
		return itemsListWithOutGroup;
	}

	public void setItemsListWithOutGroup(
			PaginationForSolr<ItemSolrCommand> itemsListWithOutGroup) {
		this.itemsListWithOutGroup = itemsListWithOutGroup;
	}

	public PaginationForSolr<List<ItemSolrCommand>> getItemsListWithGroup() {
		return itemsListWithGroup;
	}

	public void setItemsListWithGroup(
			PaginationForSolr<List<ItemSolrCommand>> itemsListWithGroup) {
		this.itemsListWithGroup = itemsListWithGroup;
	}

}
