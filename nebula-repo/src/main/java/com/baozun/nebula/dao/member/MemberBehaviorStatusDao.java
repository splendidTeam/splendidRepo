package com.baozun.nebula.dao.member;

import com.baozun.nebula.model.member.MemberBehaviorStatus;
import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

public interface MemberBehaviorStatusDao extends GenericEntityDao<MemberBehaviorStatus,Long>{

    @NativeQuery(model=MemberBehaviorStatus.class)
    public MemberBehaviorStatus findMemberBehaviorStatusByTypeAndMemberId(@QueryParam("type")String type,@QueryParam("id") Long memberId);
}
