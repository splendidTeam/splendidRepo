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
package com.baozun.nebula.manager.product;

import java.util.List;

import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.web.command.BundleElementViewCommand;

/**
 * @author yue.ch
 * @time 2016年5月25日 下午5:55:56
 */
public interface BundleManager extends BaseManager {

	Bundle createOrUpdate(BundleCommand bundle);
	
	List<BundleElementViewCommand> loadBundleElement(BundleElementViewCommand[] commands);
	
	List<BundleElementViewCommand> loadBundleSku(BundleElementViewCommand[] commands);
}
