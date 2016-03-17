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
package com.baozun.nebula.manager.sdk.manger;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.freight.command.ShippingFeeConfigCommand;
import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.freight.manager.FreightMemoryManager;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.freight.ShippingTemeplate;
import com.baozun.nebula.model.freight.SupportedArea;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.baozun.nebula.sdk.command.logistics.ItemFreightInfoCommand;

import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;

/**
 * @author Tianlong.Zhang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class LogisticsManagerTest {
	private static final Logger log = LoggerFactory.getLogger(LogisticsManagerTest.class);
	
	@Autowired
	private LogisticsManager logisticsManager; 
	
	@Autowired
	private FreightMemoryManager freightMemoryManager;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	public void test1(){
		Session session = sessionFactory.openSession();
		session.get(DistributionMode.class, 2);
	}
	
	@Test
	public void saveSupportedAreas(){
		List<SupportedAreaCommand> supportedAreaCommandList = new ArrayList<SupportedAreaCommand>();
		
		// 查出上海市 的 区 然后 设置为 顺丰快递 支持的白名单
		Long pid = 310100L; // 上海
		Long distributionModeId = 3L;
		List<Address> addressList = AddressUtil.getSubAddressByPid(pid);
		
		Long groupNo = System.currentTimeMillis();
		//上海
		supportedAreaCommandList.add(getSupportedAreaCommand(pid,distributionModeId,SupportedArea.WHITE_TYPE,groupNo));
		
		//崇明
		supportedAreaCommandList.add(getSupportedAreaCommand(310230L,distributionModeId,SupportedArea.BLACK_TYPE,groupNo));
		
		// 奉贤
		supportedAreaCommandList.add(getSupportedAreaCommand(310120L,distributionModeId,SupportedArea.BLACK_TYPE,groupNo));
		
//		for(Address address :addressList){
//			SupportedAreaCommand sac = new SupportedAreaCommand();
//			sac.setAreaId(address.getId().toString());
//			sac.setDistributionModeId(distributionModeId);
//			sac.setType(SupportedArea.WHITE_TYPE);
//			
//			supportedAreaCommandList.add(sac);
//		}
		
		List<SupportedAreaCommand> savedList = logisticsManager.saveSupportedAreas(supportedAreaCommandList);
		assertEquals(savedList.size(),supportedAreaCommandList.size());
		
	}
	
	@Test
	public void findDistributeMode(){
		
		Long shopId = 273L;
		Long provienceId = 310000L;// 上海
		Long cityId = 310100L;// 上海市
		Long countyId = 310101L;// 黄浦区
		
//		Long countyId = 310230L;//310230  崇明
		
		Long townId = 310101013L;// 外滩街道 
		
//		Long provienceId = 410000L;// 河南
//		Long cityId = 411300L;// 南阳市
////		Long countyId = 310101L;// 黄浦区
//		
//		Long countyId = 411326L;//411322L;//310230  崇明
//		
//		Long townId =null;// 310101013L;// 外滩街道 
		freightMemoryManager.loadFreightInfosFromDB();
		List<DistributionMode> list = logisticsManager.findDistributeMode(shopId,provienceId, cityId, countyId, townId);
	
		assertNotNull(list);
		System.out.println("****************");
		for(DistributionMode d:list){
			System.out.println(d.getName());
		}
	}
	
	@Test
	public void deleteSupportedAreas(){
		List<Long> ids = new ArrayList<Long>();
//		ids.add(5L);
//		ids.add(6L);
//		ids.add(7L);
		
		for(long i=5;i<14;i++){
			ids.add(i);
		}
		assertTrue(logisticsManager.deleteSupportedAreas(ids));
	}
	
	
	@Test
	public void testSaveDistributionMode(){
		DistributionModeCommand cmd = new DistributionModeCommand();
		
		cmd.setName("顺丰快递");
		cmd = logisticsManager.saveDistributionMode(cmd);
		assertNotNull(cmd);
		cmd.setName("顺丰当日达");
		cmd = logisticsManager.saveDistributionMode(cmd);
		assertNotNull(cmd);
		cmd.setName("圆通快递");
		cmd = logisticsManager.saveDistributionMode(cmd);
		assertNotNull(cmd);
	}
	
	@Test
	public void delDistributionMode(){
		boolean result = logisticsManager.deleteDistributionMode(4L);
		assertTrue(result);
	}
	
	@Test
	public void updateDistributionMode(){
		DistributionModeCommand cmd = new DistributionModeCommand();
		cmd.setId(3L);
		cmd.setName("顺丰当日达1");
		boolean result = logisticsManager.updateDistributionMode(cmd);
		assertTrue(result);
	}
	
	
	
	private SupportedAreaCommand getSupportedAreaCommand(Long areaId,Long distributionModeId,String type, Long groupNo){
		SupportedAreaCommand cmd = new SupportedAreaCommand();
		cmd.setAreaId(areaId.toString());
		cmd.setDistributionModeId(distributionModeId);
		cmd.setType(type);
		cmd.setGroupNo(groupNo);
		
		return cmd;
	}
	
	@Test
	public void saveShippingTemeplate(){
		ShippingTemeplateCommand shippingTemeplate = new ShippingTemeplateCommand();
		shippingTemeplate.setDefault(true);
		
		for(int i=1;i<2;i++){
			String name="按重量类型模板276";
//			name += i;
			shippingTemeplate.setName(name);
			shippingTemeplate.setCalculationType(ShippingTemeplate.CAL_TYPE_BY_WEIGHT);
			shippingTemeplate.setShopId(276L);
			shippingTemeplate.setDefaultFee(new BigDecimal(5));
			
			shippingTemeplate.setFeeConfigs(getConfigList());
			ShippingTemeplateCommand savedCmd = logisticsManager.saveShippingTemeplate(shippingTemeplate);
		
			assertNotNull(savedCmd.getId());
			StringBuilder sb = new StringBuilder();
			sb.append("saved tmpId is ").append(savedCmd.getId()).append("\n");
			
			for(ShippingFeeConfigCommand cfgCmd : savedCmd.getFeeConfigs()){
				sb.append(getShippingFeeConfigCommandSb(cfgCmd));
			}
			

			log.info(sb.toString());
		}
	}
	
	@Test
	public void saveShippingTemeplate1(){
		ShippingTemeplateCommand shippingTemeplate = new ShippingTemeplateCommand();
		shippingTemeplate.setDefault(true);
		
		for(int i=1;i<2;i++){
			String name="飞利浦导入数据";
//			name += i;
			shippingTemeplate.setName(name);
			shippingTemeplate.setCalculationType(ShippingTemeplate.CAL_TYPE_BY_UNIT);
			shippingTemeplate.setShopId(276L);
			shippingTemeplate.setDefaultFee(new BigDecimal(5));
			
			shippingTemeplate.setFeeConfigs(getConfigList1());
			ShippingTemeplateCommand savedCmd = logisticsManager.saveShippingTemeplate(shippingTemeplate);
		
			assertNotNull(savedCmd.getId());
			StringBuilder sb = new StringBuilder();
			sb.append("saved tmpId is ").append(savedCmd.getId()).append("\n");
			
			for(ShippingFeeConfigCommand cfgCmd : savedCmd.getFeeConfigs()){
				sb.append(getShippingFeeConfigCommandSb(cfgCmd));
			}
			

			log.info(sb.toString());
		}
	}
	
	@Test
	public void findShippingTemeplateCommandById(){
		Long id = 2L;
		ShippingTemeplateCommand savedCmd = logisticsManager.findShippingTemeplateCommandById(id);
		assertNotNull(savedCmd.getId());
		StringBuilder sb = new StringBuilder();
		sb.append("saved tmpId is ").append(savedCmd.getId()).append("\n");
		sb.append("saved tmpId getName() is ").append(savedCmd.getName()).append("\n");
		sb.append("saved tmpId etType() is ").append(savedCmd.getCalculationType()).append("\n");
		
		for(ShippingFeeConfigCommand cfgCmd : savedCmd.getFeeConfigs()){
			sb.append(getShippingFeeConfigCommandSb(cfgCmd));
		}
		
		log.info(sb.toString());
	}
	
	@Test
	public void updateShippingTemeplate(){
		Long id = 2L;
		ShippingTemeplateCommand savedCmd = logisticsManager.findShippingTemeplateCommandById(id);
		
		System.out.println(getShippingTemeplateCommandStr(savedCmd));
		
		System.out.println("======================================");
		savedCmd.setName("修改过的模板名称1");
		savedCmd.getFeeConfigs().get(0).setFirstPartPrice(new BigDecimal(30));
		
		boolean result = logisticsManager.updateShippingTemeplate(savedCmd);
		assertTrue(result);
		
		savedCmd = logisticsManager.findShippingTemeplateCommandById(id);
		System.out.println(getShippingTemeplateCommandStr(savedCmd));
		
	}
	
	@Test
	public void applyShippingTemeplate(){
		Long itemId = 3058L;
		Long temeplateId = 76L;
//		assertTrue(logisticsManager.applyShippingTemeplate(itemId, temeplateId));
	}
	
	@Test
	public void removeShippingTemeplate(){
		Long temeplateId = 10L;
		assertTrue(logisticsManager.removeShippingTemeplate(temeplateId));
	}
	
	@Test
	public void findShippingTemeplateList(){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("shopId", 300L);
		Page page = new Page();
		page.setSize(5);
		Pagination<ShippingTemeplate> p =logisticsManager.findShippingTemeplateList(page, null, paraMap,1L);
		System.out.println("***********************");
		System.out.println("total:" +p.getTotalPages());
		for(ShippingTemeplate st : p.getItems()){
			System.out.println(st.getId());
		}
	}
	
	@Test
	public void findFreight(){
		
		this.freightMemoryManager.loadFreightInfosFromDB();
	
		List<ItemFreightInfoCommand> cmdList = new ArrayList<ItemFreightInfoCommand>();
		
		cmdList.add(getItemFreightInfoCommand(333L,3,2.0));
		cmdList.add(getItemFreightInfoCommand(334L,15,5.0));
		Long distributionModeId = 3L;
		Long shopId = 273L ;
		
		Long provienceId = 310000L;// 上海
		Long cityId = 310100L;// 上海市
//		Long countyId = 310101L;// 黄浦区
		Long countyId = 310230L;//310230  崇明
		
		Long townId = 310101013L;// 外滩街道 
		
		
		BigDecimal result = logisticsManager.findFreight(cmdList, distributionModeId, shopId, provienceId, cityId, countyId, townId);
	
		System.out.println("result is "+result.toString());
	}
	
	private ItemFreightInfoCommand getItemFreightInfoCommand(Long itemId,Integer count,Double weight){
		ItemFreightInfoCommand cmd = new ItemFreightInfoCommand();
		cmd.setItemId(itemId);
		cmd.setCount(count);
		cmd.setWeight(weight);
		
		return cmd;
	}
	
	@Test
	public void removeItemShippingTemeplate(){
		logisticsManager.removeItemShippingTemeplate(3000L);
	}
	
	private List<ShippingFeeConfigCommand> getConfigList1(){
		List<ShippingFeeConfigCommand> list =new ArrayList<ShippingFeeConfigCommand>();
		
		list.add(ShippingFeeConfigCommand("北京",8));
		list.add(ShippingFeeConfigCommand("安徽省",7));
		list.add(ShippingFeeConfigCommand("澳门特别行政区",30));
		list.add(ShippingFeeConfigCommand("福建省",8));
		list.add(ShippingFeeConfigCommand("甘肃省",12));
		list.add(ShippingFeeConfigCommand("广东省",8));
		list.add(ShippingFeeConfigCommand("广西壮族自治区",10));
		list.add(ShippingFeeConfigCommand("贵州省",10));
		list.add(ShippingFeeConfigCommand("海南省",10));
		
		list.add(ShippingFeeConfigCommand("河北省",8));
		list.add(ShippingFeeConfigCommand("河南省",8));
		list.add(ShippingFeeConfigCommand("黑龙江省",10));
		list.add(ShippingFeeConfigCommand("湖北省",8));
		list.add(ShippingFeeConfigCommand("湖南省",8));
		list.add(ShippingFeeConfigCommand("吉林省",10));
		list.add(ShippingFeeConfigCommand("江苏省",5));
		list.add(ShippingFeeConfigCommand("江西省",8));
		list.add(ShippingFeeConfigCommand("辽宁省",10));
		list.add(ShippingFeeConfigCommand("内蒙古自治区",12));
		list.add(ShippingFeeConfigCommand("宁夏回族自治区",12));
		list.add(ShippingFeeConfigCommand("山东省",8));
		list.add(ShippingFeeConfigCommand("青海省",12));
		list.add(ShippingFeeConfigCommand("山西省",10));
		list.add(ShippingFeeConfigCommand("陕西省",10));
		
		list.add(ShippingFeeConfigCommand("台湾省",35));
		list.add(ShippingFeeConfigCommand("四川省",10));
		list.add(ShippingFeeConfigCommand("上海",5));
		list.add(ShippingFeeConfigCommand("天津",8));
		list.add(ShippingFeeConfigCommand("西藏自治区",12));
		list.add(ShippingFeeConfigCommand("香港特别行政区",30));
		list.add(ShippingFeeConfigCommand("浙江省",5));
		list.add(ShippingFeeConfigCommand("重庆",10));
		list.add(ShippingFeeConfigCommand("云南省",10));
		list.add(ShippingFeeConfigCommand("新疆维吾尔自治区",12));
		
		
		
		return list;
	}
	
	
	private List<ShippingFeeConfigCommand> getConfigList(){
		List<ShippingFeeConfigCommand> list =new ArrayList<ShippingFeeConfigCommand>();
		
		//黄埔区  顺丰当日达
		list.add(getShippingFeeConfigCommand(7,2,3,1,2,3L,310101L));
		
		//崇明  顺丰当日达
		list.add(getShippingFeeConfigCommand(7,2,3,1,2,3L,310230L));
		
		//南京市鼓楼区  顺丰当日达
		list.add(getShippingFeeConfigCommand(7,2,3,1,2,3L,320102L));
		
//		for(int i =0 ;i <1;i++){
//			ShippingFeeConfigCommand cmd =new ShippingFeeConfigCommand();
//
//			cmd.setFirstPartPrice(new BigDecimal(2));
//			cmd.setFirstPartUnit(3);
//			cmd.setSubsequentPartPrice(new BigDecimal(1));
//			cmd.setSubsequentPartUnit(2);
//			cmd.setDistributionModeId(1L);
//			cmd.setDestAreaId(310108+"");
//			list.add(cmd);
//		}
		return list;
	}
	
	private ShippingFeeConfigCommand ShippingFeeConfigCommand(String areaName ,Integer FirstPartPrice){
		
		Long areaId = null;
		List<Address> addressList = AddressUtil.getSubAddressByPid(1L);
		
		for(Address a : addressList){
			if(a.getName().equals(areaName)){
				areaId = a.getId();
			}
		}
		
		if(areaId == null){
			throw new RuntimeException();
		}
		
		ShippingFeeConfigCommand cmd = new ShippingFeeConfigCommand();
		
		
		cmd.setFirstPartPrice(new BigDecimal(FirstPartPrice));
		cmd.setFirstPartUnit(Integer.MAX_VALUE);
		cmd.setDistributionModeId(2L);
		cmd.setDestAreaId(areaId.toString());
		cmd.setBasePrice(new BigDecimal(1));
		
		return cmd;
	}
	
	private ShippingFeeConfigCommand getShippingFeeConfigCommand(
			int basePrice,
			int FirstPartPrice,
			Integer FirstPartUnit,
			int SubsequentPartPrice,
			Integer SubsequentPartUnit,
			Long DistributionModeId,
			Long AreaId){
		ShippingFeeConfigCommand cmd = new ShippingFeeConfigCommand();
		
		cmd.setFirstPartPrice(new BigDecimal(FirstPartPrice));
		cmd.setFirstPartUnit(FirstPartUnit);
		cmd.setSubsequentPartPrice(new BigDecimal(SubsequentPartPrice));
		cmd.setSubsequentPartUnit(SubsequentPartUnit);
		cmd.setDistributionModeId(DistributionModeId);
		cmd.setDestAreaId(AreaId.toString());
		cmd.setBasePrice(new BigDecimal(basePrice));
		
		return cmd;
	}

	private StringBuilder getShippingFeeConfigCommandSb(ShippingFeeConfigCommand cfgCmd){
		if(null == cfgCmd){
			return null;
		} 

		StringBuilder sb = new StringBuilder();
		
		sb.append("  cfgCmd.getId()").append(cfgCmd.getId());
		sb.append("  ShippingTemeplateId()").append(cfgCmd.getShippingTemeplateId());
		sb.append("  FirstPartPrice()").append(cfgCmd.getFirstPartPrice());
		sb.append("\n");
		return sb;
	}
	
	private String getShippingTemeplateCommandStr(ShippingTemeplateCommand savedCmd){
		StringBuilder sb = new StringBuilder();
		sb.append("tmpId is ").append(savedCmd.getId()).append("\n");
		sb.append("tmpId getName() is ").append(savedCmd.getName()).append("\n");
		sb.append("tmpId getType() is ").append(savedCmd.getCalculationType()).append("\n");
		
		for(ShippingFeeConfigCommand cfgCmd : savedCmd.getFeeConfigs()){
			sb.append(getShippingFeeConfigCommandSb(cfgCmd));
		}
		
		return sb.toString();
	}
}
