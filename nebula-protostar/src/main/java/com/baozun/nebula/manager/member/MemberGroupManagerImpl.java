package com.baozun.nebula.manager.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.member.MemberGroupDao;
import com.baozun.nebula.dao.member.MemberGroupRelationDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.web.command.MemberGroupRelationResultCommand;


@Transactional
@Service("MemberGroupManager")
public class MemberGroupManagerImpl implements MemberGroupManager{
	
	private static final Logger	log	= LoggerFactory.getLogger(MemberGroupManagerImpl.class);
	
	@Autowired
	private MemberGroupDao memberGroupDao;
	
	@Autowired
	private MemberGroupRelationDao memberGroupRelationDao;
	
	@Autowired
	private MemberManager memberManager;
	
	@Override
	@Transactional(readOnly = true)
	public List<MemberGroup> findMemberGroupListByGroupIds(Long[] groupIds){
		// TODO Auto-generated method stub
		List<Long> groupId = new ArrayList<Long>();
		for (Long id : groupIds){
			groupId.add(id);
		}
		return memberGroupDao.findMemberGroupListByIds(groupId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean validateGroupName(String groupName){
		// TODO Auto-generated method stub
		
		MemberGroup memberGroup = memberGroupDao.validateGroupName(groupName);
		
		if(memberGroup!=null){
			return true;
		}
		return false;
	}

	public void  createOrUpdateMemberGroup(MemberGroup memberGroup){
		// TODO Auto-generated method stub
		memberGroupDao.save(memberGroup);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<MemberGroup> findMemberGroupListByQueryMap(Map<String,Object> paraMap,Sort[] sorts){
		// TODO Auto-generated method stub
		
		List<MemberGroup> memberGroupList= memberGroupDao.findMemberGroupListByQueryMap(paraMap,sorts);
		
		return memberGroupList;
	}

	public boolean removeGroupByIds(List<Long> ids){
		// TODO Auto-generated method stub
		
		int expected = ids.size();
		int actual = memberGroupDao.removeMemberGroupByIds(ids);
		
		if(expected==actual){
			return true;
		}else{
			throw new BusinessException(ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { expected, actual });
		}
	}
	
	public MemberGroupRelationResultCommand bindMemberGroup(Long[] memberIds,Long[] groupIds){
		// TODO Auto-generated method stub
		// 无须重复关联 会员
		Map<String, List<Member>> repeatMap = new HashMap<String, List<Member>>();
		// 关联成功 会员
		Map<String, List<Member>> successMap = new HashMap<String, List<Member>>();

		// 关联 分类 失败的会员
		Map<String, List<Member>> failMap = new HashMap<String, List<Member>>();

		// 查询出跟此次要关联的数据存在重复的数据
		List<MemberGroupRelation> compareMemberGroupRelationList = memberGroupRelationDao.findMemberGroupRelationByMemberIdAndGroupId(memberIds, groupIds);

		// 根据分类Id数组查询会员分类
		List<MemberGroup> memberGroupList = this.findMemberGroupListByGroupIds(groupIds);
		// 根据会员id数组查询会员
		List<Member> MemberList = memberManager.findMemberListByMemberIds(memberIds);

		for (Long groupId : groupIds){
			MemberGroup memberGroup = null;
			for (MemberGroup memberGroup1 : memberGroupList){
				if (memberGroup1.getId().equals(groupId)){
					memberGroup = memberGroup1;
				}
			}
			String groupName = memberGroup.getName();

			for (Long memberId : memberIds){

				Member member = null;
				for (Member Member1 : MemberList){
					if (Member1.getId().equals(memberId)){
						member = Member1;
					}
				}
				// 判断 要插入的 商品+分类 是否 在关系表中已经存在 默认不存在
				boolean isRepeat = false;
				// 首先跟list比较看是否已经存在要添加关联的数据
				if (Validator.isNotNullOrEmpty(compareMemberGroupRelationList)){

					for (MemberGroupRelation memberGroupRelation : compareMemberGroupRelationList){
						isRepeat = memberGroupRelation.getGroupId().equals(groupId) && memberGroupRelation.getMemberId().equals(memberId);
						if (isRepeat){
							break;
						}
					}

					if (isRepeat){
						putMemberToMap(repeatMap, groupName, member);
						continue;
						}
					}

					// 如果 没有重复的 我们就插入 , 此处加入判断 便于我们代码阅读
					if (!isRepeat){
						try{
							// 执行关联操作
							Integer result = memberGroupRelationDao.bindMemberGroup(memberId, groupId);
							if (Integer.valueOf(1).equals(result)){
								putMemberToMap(successMap, groupName, member);
							}else{
								putMemberToMap(failMap, groupName, member);
							}
						}catch (Exception e){
							e.printStackTrace();
							log.info("addMemberCategory error message: categoryName:{},MemberId:{}", groupName, memberId);
							putMemberToMap(failMap, groupName, member);
						}
					}
				}
			}

			MemberGroupRelationResultCommand memberGroupRelationResultCommand = new MemberGroupRelationResultCommand();
			memberGroupRelationResultCommand.setFailMap(failMap);
			memberGroupRelationResultCommand.setRepeatMap(repeatMap);
			memberGroupRelationResultCommand.setSuccessMap(successMap);
				
			//去重Memberid,更新t_pd_Member:isaddcategory
				
			List<Long> ids=new ArrayList<Long>();
			Set<Long> set=new HashSet<Long>();
			for (Map.Entry<String, List<Member>> entry : successMap.entrySet()) {
				List<Member> successMemberList	= (List<Member>)entry.getValue();
					
					
				for(Member Member:successMemberList){
					set.add(Member.getId());
				}
					
			}
			Iterator<Long> it=set.iterator();
			while(it.hasNext()){
				ids.add(it.next());
			}
			//memberManager.updateMemberIsAddGroup(ids, 1);
				
				
			return memberGroupRelationResultCommand;
	}
	
	/**
	 * 设置 map 信息
	 * 
	 * @param map
	 *            map repeatMap,successMap,failMap
	 * @param categoryName
	 *            分类名称
	 * @param item
	 *            item
	 */
	private void putMemberToMap(Map<String, List<Member>> map,String memberName,Member member){
		List<Member> memberList = map.get(memberName);

		if (Validator.isNullOrEmpty(memberList)){
			memberList = new ArrayList<Member>();
		}
		memberList.add(member);
		map.put(memberName, memberList);
	}

	public boolean unBindMemberGroup(Long[] memberIds,Long groupId){
		// TODO Auto-generated method stub
		/*int expected = memberIds.length;
		int actual = */
		memberGroupRelationDao.unBindMemberGroup(memberIds, groupId);
		//if (expected == actual){
			List<Long> ids=new ArrayList<Long>();
			for(int i = 0;i <memberIds.length;i++){
				ids.add(memberIds[i]);
			}
			//筛选出剔除掉该分类后还属于其他分类的itemId,然后remove
			
			List<MemberGroupRelation> mgrList=null;
			
			for(int i = ids.size()-1;i >= 0;i--){
				mgrList=new ArrayList<MemberGroupRelation>();
				mgrList=memberGroupRelationDao.findMemberGroupRelationListByMemberId(ids.get(i));
				if(mgrList!=null&&mgrList.size()>0){
					ids.remove(i);
				}
			}
			//memberManager.updateMemberIsAddGroup(ids, 0);
			
			return true;
		/*}else{
			throw new BusinessException(ErrorCodes.NATIVEUPDATE_ROWCOUNT_NOTEXPECTED, new Object[] { expected, actual });
			// throw new EffectRangeUnexpectedException(expected, actual);
		}*/
	}

	/**
	 * 如果所选会员中没有一个是属于该分组的，返回false
	 */
	public Boolean validateUnBindByMemberIdsAndGroupId(Long[] memberIds,Long groupId){
		// TODO Auto-generated method stub
		Long[] groupIds =new Long[]{groupId};
		
		// 查询出跟此次要关联的数据存在重复的数据
				List<MemberGroupRelation> compareMemberGroupRelationList = memberGroupRelationDao.findMemberGroupRelationByMemberIdAndGroupId(memberIds, groupIds);
		
		Boolean flag =compareMemberGroupRelationList!=null&&compareMemberGroupRelationList.size()>0?true:false;
		
		return flag;
	}

}
