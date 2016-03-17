package com.baozun.nebula.manager.product;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.TagCommand;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemTag;
import com.baozun.nebula.model.product.ItemTagRelation;
import com.baozun.nebula.web.command.ItemCategoryResultCommand;


public interface ItemTagManager{
	
	/**
	 * 将一组商品关联到一个或者多个标签下
	 * 
	 * @param itemIds
	 *            商品id 数组
	 * @param categoryIds
	 *            分类id数组
	 * @return 详细记录 操作 失败 重复 成功情况,以供前台文案提示展示
	 */
	ItemCategoryResultCommand bindItemTag(Long[] itemIds,Long[] tagIds);
	
	
	/**
	 * 把一个或者多个商品从一个标签下解除关联(关系表物理删除)
	 * 
	 * @param itemIds
	 *            商品id 数组
	 * @param tagId
	 *            标签id
	 * @return
	 */
	boolean unBindItemTag(Long[] itemIds,Long tagId);

	/**
	 * 查询所有有效的标签
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	List<TagCommand> findEffectItemTagListByQueryMap(Map<String,Object> paraMap,Sort[] sorts);
	
	/**
	 * 获取已标签商品
	 * 		单个商品若属于多个标签，标签按","号隔开
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */

	Pagination<ItemCommand> findItemTagListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId);
	
	/**
	 * 获取未标签商品
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @param shopId
	 * @return
	 */
	Pagination<ItemCommand> findItemNoTagListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId);
	
	/**
	 * 根据标签Id数组查询商品标签
	 * 
	 * @param tagIds
	 *            标签id数组
	 * @return
	 */
	List<ItemTag> findTagListByTagIds(Long[] tagIds);
	
	/**
	 * 查找当前itemId所有标签条目
	 * 
	 * @param tagIds
	 *            标签id数组
	 * @return
	 */
	List<ItemTagRelation> findItemTagRelationListByItemId(Long tagIds);
	
	/**
	 * 校验脱离标签时选择的商品
	 * @param itemIds
	 * @param tagId
	 * @return
	 */
	Boolean validateUnBindByItemIdsAndTagId(Long[] itemIds,Long tagId);
	
	/**
	 * 增加商品标签
	 * @param tagName
	 */
	
	public void  createOrUpdateItemTag(ItemTag itemTag);
	
	/**
	 * 判断商品标签名是否重复
	 * @param tagName
	 * @return
	 */
	
	boolean validateTagName(String tagName);
	
	/**
	 * 逻辑删除商品标签
	 * @param ids
	 * @return
	 */
	
	boolean removeTagByIds(List<Long> ids);
}
