package com.baozun.nebula.sdk.manager;
 
import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.ProductComboDetailsCommand;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.manager.BaseManager;

public interface SdkItemTagRuleManager extends BaseManager{
	/**
	 * 分页查询
	 * @return
	 */  
	public Pagination<ItemTagRuleCommand> findCustomProductComboList(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap, Long shopId);

	/** 
	 * 保存商品筛选器
	 * @return
	 */
	public void saveItemTagRule(ItemTagRuleCommand command);
	
	/**
	 * 查询所有筛选器
	 * @return
	 */
	public List<ItemTagRuleCommand> findAllAvailableCustomProductComboList();

	/**
	 * 通过shopId查询所有筛选器
	 * @return
	 */
	public List<ItemTagRuleCommand> findAllAvailableCustomProductComboListByShopId(Long shopId);
	
	/**
	 * @param 更新
	 */
	public void update(ItemTagRuleCommand command);
	/**
	 * 通过筛选器名称查询
	 * @return
	 */
	public ItemTagRuleCommand findCustomProductCombo(String comboName);

	/**
	 *  禁止或开启商品组合
	 * @return
	 */
	public void enableOrDisableProductGroupById(Long id, Integer activeMark);
	
	/**
	 * 通过id查询筛选器
	 * @return
	 */
	public ItemTagRuleCommand findCustomProductComboById(Long id);
	
	/**
	 * 检查商品是否均在分类中
	 * @param items
	 * @param categorys
	 */
	public boolean checkCategoryWithItem(String items, String categorys);

	/**
	 * 根据类型查询列表
	 * @param type
	 * @param shopId
	 * @return
	 */
	public List<ItemTagRuleCommand> findCustomProductComboListByType(Integer type, Long shopId);

	/**
	 * 根据ID查询商品筛选器详情
	 * @param id 原子类型筛选器ID
	 * @return
	 */
	public ProductComboDetailsCommand findDetailsById(Long id);

	/**
	 * 根据ID查询商品筛选器详情列表
	 * @param id 组合类型筛选器ID
	 * @return
	 */
	public List<ProductComboDetailsCommand> findDetailsListById(Long id);
	
	/**
	 * 解析商品筛选器表达式
	 * @param exp
	 * @return
	 */
	public ProductComboDetailsCommand findItemComboListByExpression(
			String exp);
	
	/**
	 * 根据id列表，查询商品筛选器
	 * 
	 * @param idList
	 * @return
	 */
	public List<ItemTagRuleCommand> findEffectItemTagRuleListByIdList(@QueryParam("idList") List<Long> idList);
}
