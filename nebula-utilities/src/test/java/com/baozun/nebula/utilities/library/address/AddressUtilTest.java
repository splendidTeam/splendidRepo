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
package com.baozun.nebula.utilities.library.address;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.junit.Test;

import com.baozun.nebula.utilities.common.Validator;

/**
 * @author Tianlong.Zhang
 *
 */
public class AddressUtilTest {

	
	public static void main(String... args) throws Exception{
//		int i=310100;
//		Long id = new Long(i);
//		AddressUtil.getSubAddressByPid(id);
//		long start = System.currentTimeMillis();
//		Address address = AddressUtil.getAddressById(id);
//		System.out.println(address.getName()+ " "+address.getId()+" "+address.getpId());
//		
//		List<Address> addressList = AddressUtil.getSubAddressByPid(id);
//		for(Address add : addressList){
//			System.out.println(add.getId()+"  "+add.getName());
//		}
//		String json = AddressUtil.generateAddressJson();
//		AddressUtil.generateJsFile("e:/a.js");
//		long end = System.currentTimeMillis();
//		System.out.println(end - start);
//		System.out.println(json);
//		System.out.println("&&&&&&&&&&&&&&&&&&&&");
//		
//		long start1 = System.currentTimeMillis();
////		Address address = AddressUtil.getAddressById(id);
////		String json1 = AddressUtil.generateAddressJson();
//		AddressUtil.generateJsFile("e:/a.js");
//		long end1 = System.currentTimeMillis();
//		System.out.println(end1 - start1);
		
		
//		System.out.println(address.getId()+"  "+address.getName()+"  " + address.getSpelling() );
//		
//		System.out.println("=================================");
//		List<Address> addressList = AddressUtil.getSubAddressByPid(id);
//		start = System.currentTimeMillis();
//		System.out.println(start - end);
//		for(Address a: addressList){
//			System.out.println(a.getId()+"  "+a.getName()+"  " + a.getSpelling() );
//		}
		
		showNoneSubArea();
	}
	
	// 显示没有子区域的地方
	public static void showNoneSubArea() throws JsonGenerationException, JsonMappingException, IOException{
		List<Address> provienceList =  AddressUtil.getSubAddressByPid(1L);
		
		StringBuilder sb = new StringBuilder();
		
		for(Address provience : provienceList){// 省
			Long provienceId = provience.getId();
			String provienceName = provience.getName();
			List<Address> cityList = AddressUtil.getSubAddressByPid(provienceId);
			
			System.out.println("======================");
			System.out.println(provienceName +" 省 结果：");
			
			for(Address city : cityList){// 市
				Long cityId = city.getId();
				String cityName = city.getName();
				List<Address> countyList = AddressUtil.getSubAddressByPid(cityId);
				
				if(Validator.isNotNullOrEmpty(countyList)){
//					System.out.println("\t"+cityName +" 市结果：" );
					for(Address county : countyList){// 县
						Long countyId = county.getId();
						String countyName = county.getName();
						List<Address> townList = AddressUtil.getSubAddressByPid(countyId);
						if(Validator.isNotNullOrEmpty(townList)){
							
						}else{
//							System.out.println("\t\t 县级 "+countyName +" 没有乡存在");
						}
					}
				}else{
					System.out.println("\t市 "+cityName +" 下边没有县存在");
					
//					List<Address> addressList = CopyOfAddressUtil.getSubAddressByPid(cityId);
//					if(Validator.isNotNullOrEmpty(addressList)){
//						ObjectMapper mapper = new ObjectMapper();
//						for(Address add:addressList){
//							
//							ObjectWriter ow =mapper.writerWithType(Map.class);
//							String addressStr = mapper.writeValueAsString(add);
//							System.out.println("\t\t 新查出来的 "+addressStr);
//							sb.append(convertAddressToJson(add)).append("\n");
//						}
//						
//					}else{
//						System.out.println("\t市 "+cityName +" 在全地址下，依然无子区域");
//					}
				}
			}
		}
		
		
		System.out.println("*************************");
		System.out.println(sb.toString());
	}
	
	private static String convertAddressToJson(Address address){
		StringBuilder sb = new StringBuilder();
		String yinhao = "\"";
		//"810308":["葵青区","810300","kui qing qu"],
		sb.append(yinhao).append(address.getId()).append(yinhao).append(":[").append(yinhao);
		sb.append(address.getName()).append(yinhao).append(",").append(yinhao);
		sb.append(address.getpId()).append(yinhao).append(",").append(yinhao);
		sb.append(address.getSpelling()).append(yinhao).append("],");
		return sb.toString();
	}
}
