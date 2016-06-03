/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.salesorder.SdkConsigneeDao;
import com.baozun.nebula.dao.salesorder.SdkOrderDao;
import com.baozun.nebula.model.salesorder.Consignee;
import com.baozun.nebula.sdk.command.ConsigneeCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkSecretManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年5月13日 下午4:04:01 
 * @version   
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SecretInfoTest {
	
	private static final Logger                      log =LoggerFactory.getLogger(SecretInfoTest.class);
	
	@Autowired
	private SdkConsigneeDao	sdkConsigneeDao;
	
	@Autowired
	private SdkSecretManager						 sdkSecretManager;
	
	@Autowired
    private SdkOrderDao                              sdkOrderDao;
	
	@Before
	public void setM(){
		ProfileConfigUtil.setMode("dev");
	}
	
	@Test
	@Rollback(false)
	public void testS() {
		ConsigneeCommand consigneeCommand =sdkConsigneeDao.findConsigneeOrderId(41l);
		Consignee consignee = new Consignee();
        ConvertUtils.convertFromTarget(consignee, consigneeCommand);
		encryptConsignee(consignee);
		consignee.setOrderId(10012l);
		sdkConsigneeDao.save(consignee);
	}
	
	@Test
	public void testF(){
		SalesOrderCommand salesOrderCommand = sdkOrderDao.findOrderByCode("139953548611200097", 1);
		decryptSalesOrderCommand(salesOrderCommand);
		log.debug("sssss:{}", JsonUtil.format(salesOrderCommand));
		
	}
	
	private void decryptSalesOrderCommand(SalesOrderCommand salesOrderCommand){

		sdkSecretManager.decrypt(salesOrderCommand, new String[] {
				"name",
				"buyerName",
				"country",
				"province",
				"city",
				"area",
				"town",
				"address",
				"postcode",
				"tel",
				"buyerTel",
//				"mobile", 该字段数据库里最大长度为20,加密后保存失败
				"email" });
	}
	
	private void encryptConsignee(Consignee consignee){

		sdkSecretManager.encrypt(consignee, new String[] {
				"name",
				"buyerName",
				"country",
				"province",
				"city",
				"area",
				"town",
				"address",
				"postcode",
				"tel",
				"buyerTel",
				"email" });
	}

}
