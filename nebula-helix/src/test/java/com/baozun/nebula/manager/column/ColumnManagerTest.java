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
package com.baozun.nebula.manager.column;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.column.ColumnComponentCommand;
import com.baozun.nebula.command.column.ColumnModuleCommand;
import com.baozun.nebula.command.column.ColumnPageCommand;
import com.baozun.nebula.model.column.ColumnModule;
import com.baozun.nebula.model.product.Category;

/**
 * @author Tianlong.Zhang
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ColumnManagerTest {
	
	@Autowired
	private ColumnManager columnManager;
	
	@Test
	public void findColumnModuleMapByPageCode(){
		ColumnPageCommand result = columnManager.findColumnModuleMapByPageCode("home");
		Map<String, ColumnModuleCommand>  resultMap = result.getColumnModuleMap();
 
		StringBuilder sb = new StringBuilder(); 
		System.out.println("******************");
		for(String key :resultMap.keySet()){
			ColumnModuleCommand module = resultMap.get(key);
			sb.append("\n");
			sb.append(module.getId()).append("\t");
			sb.append(module.getName()).append("\t");
			sb.append(module.getCode()).append("\t");
			sb.append(module.getType()).append("\t");
			
			List<ColumnComponentCommand> cptList= module.getComponentList();
			for(ColumnComponentCommand c :cptList){
				sb.append("\n").append("\t\t");
				sb.append(c.getId()).append("\t");
				sb.append(c.getTitle()).append("\t");
				
				if(ColumnModule.TYPE_CATEGORY.equals(module.getType())){
					Category ctg = c.getCategory();
					sb.append("分类 ").append(ctg.getName());
				}
				
				if(ColumnModule.TYPE_ITEM.equals(module.getType())){
					ItemCommand item = c.getItemCommand();
					sb.append("商品 ").append(item.getCode()).append("  "+item.getSalePrice());
				}
				
			}
			
			
			sb.append("\n");
			sb.append("\n");
		}
		
		System.out.println(sb.toString());
	}

}
