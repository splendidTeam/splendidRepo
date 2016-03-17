package com.baozun.nebula.manager.member;

import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.web.command.MemberGroupRelationResultCommand;


public interface MemberGroupManager extends BaseManager{
	
	/**
	 * 查询所有的分组
	 * @return
	 */
	List<MemberGroup> findMemberGroupListByQueryMap(Map<String,Object> paraMap,Sort[] sorts);
	
	/**
	 * 逻辑删除会员分组
	 * @param ids
	 * @return
	 */
	
	boolean removeGroupByIds(List<Long> ids);
	
	/**
	 * 根据分组ids获取分组
	 * @param groupIds
	 * @return
	 */
	
	List<MemberGroup> findMemberGroupListByGroupIds(Long[] groupIds);
	
	/**
	 * 判断会员分组名是否重复
	 * @param groupName
	 * @return
	 */
	
	boolean validateGroupName(String groupName);
	
	/**
	 * 增加会员分组
	 * @param groupName
	 */
	
	public void  createOrUpdateMemberGroup(MemberGroup memberGroup);
	
	/**
	 * 将一组会员关联到一个或者多个分类下
	 * 
	 * @param memberIds
	 *            会员id 数组
	 * @param groupIds
	 *            分类id数组
	 * @return 详细记录 操作 失败 重复 成功情况,以供前台文案提示展示
	 */
	MemberGroupRelationResultCommand bindMemberGroup(Long[] memberIds,Long[] groupIds);
	
	
	/**
	 * 把一个或者多个会员从一个分组下解除关联(关系表物理删除)
	 * 
	 * @param memberIds
	 *            会员id 数组
	 * @param groupId
	 *            分组id
	 * @return
	 */
	boolean unBindMemberGroup(Long[] memberIds,Long groupId);
	
	/**
	 * 校验脱离分类时选择的会员
	 * @param itemIds
	 * @param categoryId
	 * @return
	 */
	Boolean validateUnBindByMemberIdsAndGroupId(Long[] memberIds,Long groupId);
}
