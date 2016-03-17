package com.baozun.nebula.web.command;

import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

/**
 * 后台下单确认页刷新数据对象
 * @author lxy  
 * @date  2014.6.9
 * @version
 */
public class OrderRefreshDataCommand   implements Command {

	private static final long serialVersionUID = 7742531282465358639L;
	
	/**购物车**/
	private ShoppingCartCommand	shoppingCartCommand;
	
	/**经过引擎之后 coupon是否绑定活动**/
	private Boolean             isCouponBindActive;
	
	/**前端地址**/
	private String frontendBaseUrl;
	
	/**图片地址**/
    private String customBaseUrl;
    
	/**图片大小**/
	private String smallSize;
	
	/**pdp链接前缀(除域名外)**/
	private String  pdpPrefix;
	
	/**会员信息**/
	private MemberPersonalDataCommand memberPersonalDataCommand;
	
	/**会员收货地址**/
	private List<ContactCommand> contacts; 
	
	
	public MemberPersonalDataCommand getMemberPersonalDataCommand() {
		return memberPersonalDataCommand;
	}

	public void setMemberPersonalDataCommand(
			MemberPersonalDataCommand memberPersonalDataCommand) {
		this.memberPersonalDataCommand = memberPersonalDataCommand;
	}

	public List<ContactCommand> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactCommand> contacts) {
		this.contacts = contacts;
	}

	public String getPdpPrefix() {
		return pdpPrefix;
	}

	public void setPdpPrefix(String pdpPrefix) {
		this.pdpPrefix = pdpPrefix;
	}

	public String getCustomBaseUrl() {
		return customBaseUrl;
	}

	public void setCustomBaseUrl(String customBaseUrl) {
		this.customBaseUrl = customBaseUrl;
	}

	public String getFrontendBaseUrl() {
		return frontendBaseUrl;
	}

	public void setFrontendBaseUrl(String frontendBaseUrl) {
		this.frontendBaseUrl = frontendBaseUrl;
	}

	public String getSmallSize() {
		return smallSize;
	}

	public void setSmallSize(String smallSize) {
		this.smallSize = smallSize;
	}

	public ShoppingCartCommand getShoppingCartCommand() {
		return shoppingCartCommand;
	}

	public void setShoppingCartCommand(ShoppingCartCommand shoppingCartCommand) {
		this.shoppingCartCommand = shoppingCartCommand;
	}

	public Boolean getIsCouponBindActive() {
		return isCouponBindActive;
	}

	public void setIsCouponBindActive(Boolean isCouponBindActive) {
		this.isCouponBindActive = isCouponBindActive;
	}    

	
}
