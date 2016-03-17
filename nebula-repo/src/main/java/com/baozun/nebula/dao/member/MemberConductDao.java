package com.baozun.nebula.dao.member;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.model.member.MemberConduct;


public interface MemberConductDao extends GenericEntityDao<MemberConduct,Long>{
	
	
	@NativeQuery(model=MemberConductCommand.class)
	public MemberConductCommand findMemberConductCommandById(@QueryParam("id")Long id);
	
	
	@NativeQuery(model=MemberConduct.class)
	public MemberConduct findMemberConductById(@QueryParam("id")Long id);
	
}
