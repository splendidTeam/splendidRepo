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
package com.baozun.nebula.dao.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.model.auth.OrgType;

/**
 * @author li.feng 
 * 
 * 2013-7-3下午02:16:57
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
@TransactionConfiguration(defaultRollback = false)
public class ShopCommandDaoTest extends AbstractTransactionalJUnit4SpringContextTests{

	private static final Logger	log	= LoggerFactory.getLogger(ShopCommandDao.class);

	@Autowired
	private ShopCommandDao		shopCommandDao;

	/**
	 * Test method for {@link com.baozun.nebula.dao.product.ShopCommandDao#findShopListByOrgaTypeId(java.lang.Long, java.util.Map)}.
	 */
	@Test
	public final void testFindShopListByOrgaTypeId(){
//		Long orgaTypeId = 1L;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("orgaTypeId", OrgType.ID_SHOP_TYPE);
		Page page = new Page(0, 100);
		Sort[] sorts = null;
		List<ShopCommand> shopCommandList = shopCommandDao.findShopListByOrgaTypeId(paraMap,sorts);
		for (ShopCommand s : shopCommandList){

			log.info("shopCommandList is shopname" + s.getShopname());
		}

	}

}
