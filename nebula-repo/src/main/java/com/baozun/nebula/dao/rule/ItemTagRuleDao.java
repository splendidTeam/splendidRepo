package com.baozun.nebula.dao.rule;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.model.rule.ItemTagRule;

/**
 * 
 * @author 项硕
 */
public interface ItemTagRuleDao extends GenericEntityDao<ItemTagRule, Long> {

	/**
	 * 根据条件查询信息完整的组合商品
	 * 
	 * @param page
	 * @param sorts
	 * @param queryMap
	 * @return
	 */
	@NativeQuery(model = ItemTagRuleCommand.class, pagable = true, withGroupby = true)
	public Pagination<ItemTagRuleCommand> findCustomProductComboList(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap, @QueryParam("shopId") Long shopId);

	/**
	 * 根据自定义分组名字查询
	 * 
	 * @return
	 */
	@NativeQuery(model = ItemTagRuleCommand.class)
	ItemTagRuleCommand findTagRuleByName(@QueryParam("name") String name);

	@NativeQuery(model = ItemTagRuleCommand.class)
	List<ItemTagRuleCommand> findAll();

	/**
	 * 通过id查询商品组合信息
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = ItemTagRuleCommand.class)
	ItemTagRuleCommand findCustomProductComboById(@QueryParam("id") Long id);

	/**
	 * 根据类型查询列表
	 * 
	 * @param type
	 * @param shopId
	 * @return
	 */
	@NativeQuery(model = ItemTagRuleCommand.class)
	public List<ItemTagRuleCommand> findCustomProductComboListByType(@QueryParam("type") Integer type, @QueryParam("shopId") Long shopId);

	/**
	 * 查询所有生效的自定义分组
	 * 
	 * @return
	 */
	@NativeQuery(model = ItemTagRuleCommand.class)
	public List<ItemTagRuleCommand> findAllAvailableCustomProductComboList();

	/**
	 * 通过shopId查询所有生效的自定义分组
	 * 
	 * @return
	 */
	@NativeQuery(model = ItemTagRuleCommand.class)
	public List<ItemTagRuleCommand> findAllAvailableCustomProductComboListByShopId(@QueryParam("shopId") Long shopId);

	/**
	 * 根据id列表，查询商品筛选器
	 * 
	 * @param idList
	 * @return
	 */
	@NativeQuery(model = ItemTagRuleCommand.class)
	public List<ItemTagRuleCommand> findEffectItemTagRuleListByIdList(@QueryParam("idList") List<Long> idList);
}
