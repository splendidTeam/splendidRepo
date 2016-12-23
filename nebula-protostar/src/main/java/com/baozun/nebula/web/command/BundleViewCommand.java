/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
*
 
* This software is the confidential and proprietary information of Baozun.
 
* You shall not disclose such Confidential Information and shall use it only in
 
* accordance with the terms of the license agreement you entered into
 
* with Baozun.
 
*
 
* BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 
* SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 
* IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 
* PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 
* SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 
* THIS SOFTWARE OR ITS DERIVATIVES.
 
*
 
*/
package com.baozun.nebula.web.command;

import java.io.Serializable;
import java.util.List;

import com.baozun.nebula.model.bundle.Bundle;

/**
 * @author yue.ch
 * @time 2016年6月14日 下午7:36:56
 */
public class BundleViewCommand extends Bundle implements Serializable {

	private static final long serialVersionUID = 4859381209260269883L;
	
	private List<BundleElementViewCommand> bundleElementViewCommands;

	public List<BundleElementViewCommand> getBundleElementViewCommands() {
		return bundleElementViewCommands;
	}

	public void setBundleElementViewCommands(List<BundleElementViewCommand> bundleElementViewCommands) {
		this.bundleElementViewCommands = bundleElementViewCommands;
	}

}
