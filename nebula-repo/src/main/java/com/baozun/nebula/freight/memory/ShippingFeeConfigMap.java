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

import com.baozun.nebula.command.Command;
import com.baozun.nebula.freight.command.ShippingFeeConfigCommand;

/**
 * 模板id-物流方式id-目的地id为key value为物流对像
 * 
 * @author Tianlong.Zhang
 *
 */
public class ShippingFeeConfigMap extends HashMap<String, ShippingFeeConfigCommand> implements Command{

    private static final long serialVersionUID = 396619284737318006L;

    public static final String KEY_CONNECTOR = "-";
}
