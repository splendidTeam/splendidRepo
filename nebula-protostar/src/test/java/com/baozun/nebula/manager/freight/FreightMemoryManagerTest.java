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
package com.baozun.nebula.manager.freight;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.freight.manager.FreightMemoryManager;
import com.baozun.nebula.freight.memory.DistributionCommand;
import com.baozun.nebula.freight.memory.ShippingFeeConfigMap;
import com.baozun.nebula.freight.memory.ShippingTemeplateMap;
import com.baozun.nebula.freight.memory.ShopShippingTemeplateMap;

/**
 * @author Tianlong.Zhang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class FreightMemoryManagerTest {
	
	@Autowired
	private FreightMemoryManager freightMemoryManager;
	
	/**
	 * 将数据库的 载物流列表 ，运费模板 ，店铺运费模板 ，运费配置信息加载到内存中
	 * 
	 */
	@Test
	public void loadFreightFromDB(){
		freightMemoryManager.loadFreightInfosFromDB();
	}
	
	/**
	 * 从数据库中加载物流列表
	 */
	@Test
	public void loadDistributionList(){
		freightMemoryManager.loadDistributionList();
		System.out.println();
	}
	
	/**
	 * 从数据库中加载运费模板 map
	 * 
	 * map 中的ShippingTemeplateCommand  没有具体的运费配置信息
	 */
	@Test
	public void loadShippingTemeplateMap(){
		freightMemoryManager.loadShippingTemeplateMap();
		System.out.println();
	}
	
	/**
	 * 从数据库中加载 店铺运费模板map
	 * 
	 * map 中的ShippingTemeplateCommand  没有具体的运费配置信息
	 */
	@Test
	public void loadShopShippingTemeplateMap(){
		freightMemoryManager.loadShopShippingTemeplateMap();
		System.out.println();
	}
	
	/**
	 * 从数据库中加载运费配置信息Map
	 */
	@Test
	public void loadShippingFeeConfigMap(){
		freightMemoryManager.loadShopShippingTemeplateMap();
		System.out.println();
	}
	
	/**
	 * 从内存中获得物流列表
	 * @return the cmdList
	 */
	@Test
	public void getDistributionList(){
		freightMemoryManager.loadDistributionList();
		 List<DistributionCommand> list = freightMemoryManager.getDistributionList();
		 assertNotNull(list);
	}

	/**
	 * 从内存中获得运费模板 map
	 * @return the shippingTemeplateMap
	 */
	@Test
	public void getShippingTemeplateMap(){
		freightMemoryManager.loadFreightInfosFromDB();
		freightMemoryManager.loadShippingTemeplateMap();
		ShippingTemeplateMap map =freightMemoryManager.getShippingTemeplateMap();
		assertNotNull(map); 
	}

	/**
	 * 从内存中获得 店铺运费模板map
	 */
	@Test
	public void getShopShippingTemeplateMap(){
		freightMemoryManager.loadFreightInfosFromDB();
//		freightMemoryManager.loadShopShippingTemeplateMap();
		ShopShippingTemeplateMap map = freightMemoryManager.getShopShippingTemeplateMap();
		assertNotNull(map);
	}

	/**
	 *从内存中获得 运费配置map
	 */
	@Test
	public void getShippingFeeConfigMap(){
//		freightMemoryManager.loadShippingFeeConfigMap();
		freightMemoryManager.loadFreightInfosFromDB();
		ShippingFeeConfigMap map = freightMemoryManager.getShippingFeeConfigMap();
		assertNotNull(map);	
	}
}
