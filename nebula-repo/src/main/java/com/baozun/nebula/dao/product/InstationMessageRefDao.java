/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.dao.product;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.system.InstationMessageTemplate;
import com.baozun.nebula.model.system.InstationSendMessageRef;

/**
 * @ClassName: InstationMessageTemplateDao
 * @Description:(站内信息发送记录DAO)
 * @author GEWEI.LU
 * @date 2016年1月15日 下午3:16:25
 */
public interface InstationMessageRefDao extends GenericEntityDao<InstationSendMessageRef, Long> {
	
	/** 
	* @Title: findTempletBymemberid 
	* @Description:(查询用户自己的站内信息) 
	* @param @param memberid
	* @param @return    设定文件 
	* @return InstationMessageTemplate    返回类型 
	* @throws 
	* @date 2016年1月20日 上午11:45:32 
	* @author GEWEI.LU   
	*/
	@NativeQuery(model = InstationMessageTemplate.class)
	InstationMessageTemplate findTempletBymemberid(@QueryParam("memberid") Long memberid);

	
}
