/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.freight.memory;

import java.util.HashMap;
import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.freight.command.ShippingTemeplateCommand;

/**
 * 店铺ID为KEY 运费模板对象为value
 * @author Tianlong.Zhang
 *
 */
public class ShopShippingTemeplateMap extends HashMap<Long, ShopShippingTemeplateCommand> implements Command{

	private static final long serialVersionUID = 4328292098073369010L;
	
}
