package com.baozun.nebula.manager.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.dao.member.MemberGroupRelationDao;
import com.baozun.nebula.dao.member.MemberPersonalDataDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.sdk.command.EngineMemberCommand;


@Transactional
@Service("CommonManager")
public class EngineManagerImpl implements EngineManager {
	
	
	@Autowired
	private MemberPersonalDataDao memberPersonalDataDao;
	
	@Autowired
	private MemberGroupRelationDao memberGroupRelationDao;

	@Override
	public EngineMemberCommand findEngineMemberDataByMemberId(Long memberId) {
		MemberPersonalDataCommand memberPersonalDataCommand=memberPersonalDataDao.findById(memberId);
		EngineMemberCommand	engineMemberCommand =null;
		if(memberPersonalDataCommand!=null ){
			engineMemberCommand=new EngineMemberCommand();
			engineMemberCommand.setId(memberPersonalDataCommand.getId());
			//engineMemberCommand.setChanneNo(member.get)
			
			engineMemberCommand.setBirthday(memberPersonalDataCommand.getBirthday());						
			engineMemberCommand.setAge(getAge(memberPersonalDataCommand.getBirthday()));
			engineMemberCommand.setCreateTime(memberPersonalDataCommand.getRegisterTime());
			engineMemberCommand.setLoginEmail(memberPersonalDataCommand.getLoginEmail());
			engineMemberCommand.setLoginMobile(memberPersonalDataCommand.getLoginMobile());
			List<MemberGroupRelation> memberGroupRelations= memberGroupRelationDao.findMemberGroupRelationListByMemberId(memberId);
			List<String> groupIds=new ArrayList<String>();
			for(MemberGroupRelation MemberGroupRelation:memberGroupRelations){
				groupIds.add(String.valueOf(MemberGroupRelation.getGroupId()));
			}
			engineMemberCommand.setGroup(groupIds);
			engineMemberCommand.setState(String.valueOf(memberPersonalDataCommand.getType()));
		}
		return engineMemberCommand;
	}
	
	/**
	 * 根据出生日期计算年龄
	 * @param birthDay
	 * @return
	 */
	private   Integer getAge(Date birthDay) { 
        Calendar calendar = new GregorianCalendar(); 

        if (calendar.before(birthDay)) { 
            throw new BusinessException(ErrorCodes.BIRTHDAY_BEFORE_NOW); 
        } 

        int yearNow = calendar.get(Calendar.YEAR); 
        int monthNow = calendar.get(Calendar.MONTH)+1; 
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH); 
        
        calendar.setTime(birthDay); 
        int yearBirth = calendar.get(Calendar.YEAR); 
        int monthBirth = calendar.get(Calendar.MONTH); 
        int dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH); 

        int age = yearNow - yearBirth; 

        if (monthNow <= monthBirth) { 
            if (monthNow == monthBirth) { 
                if (dayOfMonthNow < dayOfMonthBirth) { //如果月份相等，判断天数不相等就是age--
                    age--; 
                } 
            } else { 
                age--; 
            } 
        } 

        return age; 
    }
	
}
