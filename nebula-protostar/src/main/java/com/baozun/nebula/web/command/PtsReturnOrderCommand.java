package com.baozun.nebula.web.command;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;

/**
 * 退换货订单
 * @author dongliang
 *
 */
public class PtsReturnOrderCommand extends ReturnOrderCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1791421649806963331L;
	
	/**
	 * 商品主图
	 */
	private String itemPic;
	
	/**
	 * 商品名称
	 */
	private String itemName;
	
	/**
	 * 商品数量
	 */
	
	private Integer itemCount;
	
	/**
	 * 服务类型名称
	 */
	private String ServiceName;
	
	/**
	 * 申请状态
	 */
	private String appStatus;

	public String getItemPic() {
		return itemPic;
	}

	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}

	public String getServiceName() {
		return ServiceName;
	}

	public void setServiceName(String serviceName) {
		ServiceName = serviceName;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}


}
