/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.sdk.handler;

import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;

/**
 * 刷新solr的扩展点，商城可以实现该接口加入一些自定义的内容
 *
 */
public interface ItemForSolrCommandHandler extends HandlerBase {
	
	/**
	 * 非国际化商城的扩展点
	 */
	ItemForSolrCommand setCustomItemForSolrCommand(ItemForSolrCommand itemForSolrCommand);
	
	/**
	 * 多语言商城的扩展点
	 */
	ItemForSolrI18nCommand setCustomItemForSolrCommandI18n(ItemForSolrI18nCommand itemForSolrI18nCommand);

}
