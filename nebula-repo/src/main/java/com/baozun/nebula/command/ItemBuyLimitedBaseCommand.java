package com.baozun.nebula.command;

import java.util.List;

/**
 * 这个command用于存放计算限购数量的一些条件
 * 作为处理逻辑的上下文传递对象
 * 
 * 如果有新增字段需要继承此类进行扩展
 * @author chengchao
 *
 */
public class ItemBuyLimitedBaseCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1812173138264476451L;

	/**
	 * 商品Code
	 */
	private String itemCode;
	
	/**
	 * 商品Id
	 */
	private Long itemId;
	
	/**
	 * memberId
	 */
	private Long memberId;
	
	/**
	 * 分组ID，用于处理会员间绑定关系 
	 */
	private Long memberGroupId;
	
	/**
	 * 来源： 自注册 微博登录 QQ登录 等。 value在ChooseOption中配置
	 */
	private Integer memberSource;
	
	/**
	 *  渠道
	 */
	private List<String>		channels;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getMemberGroupId() {
		return memberGroupId;
	}

	public void setMemberGroupId(Long memberGroupId) {
		this.memberGroupId = memberGroupId;
	}

	public Integer getMemberSource() {
		return memberSource;
	}

	public void setMemberSource(Integer memberSource) {
		this.memberSource = memberSource;
	}

	public List<String> getChannels() {
		return channels;
	}

	public void setChannels(List<String> channels) {
		this.channels = channels;
	}
	
}
