package com.baozun.nebula.command;

import java.util.List;

import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.model.salesorder.SoReturnLine;

public class ReturnApplicationCommand {
	
	private SoReturnApplication returnApplication;

	private List<SoReturnLine> returnLineList;
	
	private SoReturnApplicationDeliveryInfo soReturnApplicationDeliveryInfo;

	public SoReturnApplicationDeliveryInfo getSoReturnApplicationDeliveryInfo() {
		return soReturnApplicationDeliveryInfo;
	}


	public void setSoReturnApplicationDeliveryInfo(
			SoReturnApplicationDeliveryInfo soReturnApplicationDeliveryInfo) {
		this.soReturnApplicationDeliveryInfo = soReturnApplicationDeliveryInfo;
	}


	public SoReturnApplication getReturnApplication() {
		return returnApplication;
	}


	public void setReturnApplication(SoReturnApplication returnApplication) {
		this.returnApplication = returnApplication;
	}


	public List<SoReturnLine> getReturnLineList() {
		return returnLineList;
	}


	public void setReturnLineList(List<SoReturnLine> returnLineList) {
		this.returnLineList = returnLineList;
	}

	
	
}
