package com.baozun.nebula.command;

public class MemberGroupRelationCommand implements Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5203770164600914992L;
	
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private Long memberId;
	private String groupName;
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
}
