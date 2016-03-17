package com.baozun.nebula.scope;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AbstractScopeConditionResult;
import com.baozun.nebula.calculateEngine.condition.CrowdScopeConditionResult;
import com.baozun.nebula.calculateEngine.condition.ItemScopeConditionResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class CrowdScopeTest {

	@Test
	public void crowdScopeTest() {

		// List<CustomProductCombo> customProductComboList = new
		// ArrayList<CustomProductCombo>();
		// CustomProductCombo customProductCombo1 = new CustomProductCombo();
		// customProductCombo1.setId(Long.parseLong("1"));
		// customProductCombo1.setComboType(1);
		// customProductCombo1.setComboExpression("pid:in(12,34)");
		//
		// CustomProductCombo customProductCombo2 = new CustomProductCombo();
		// customProductCombo2.setId(Long.parseLong("2"));
		// customProductCombo2.setComboType(2);
		// customProductCombo2.setComboExpression("cid:in(29)&pid:in(30)");
		//
		// CustomProductCombo customProductCombo3 = new CustomProductCombo();
		// customProductCombo3.setId(Long.parseLong("3"));
		// customProductCombo3.setComboType(2);
		// customProductCombo3.setComboExpression("cid:in(31)");
		//
		// CustomProductCombo customProductCombo = new CustomProductCombo();
		// customProductCombo.setId(Long.parseLong("4"));
		// customProductCombo.setComboType(4);
		// customProductCombo.setComboExpression("cmb:in(1,2)");
		//
		// customProductComboList.add(customProductCombo);
		// customProductComboList.add(customProductCombo1);
		// customProductComboList.add(customProductCombo2);
		// customProductComboList.add(customProductCombo3);
		// EngineManager.getInstance().setItemScopeList(customProductComboList);
		//
		// List<CustomMemberGroup> customMemberGroupList = new
		// ArrayList<CustomMemberGroup>();
		// CustomMemberGroup customMemberGroup1 = new CustomMemberGroup();
		// customMemberGroup1.setId(Long.parseLong("1"));
		// customMemberGroup1.setGroupType(1);
		// customMemberGroup1.setGroupExpression("mid:in(12,34)");
		//
		// CustomMemberGroup customMemberGroup2 = new CustomMemberGroup();
		// customMemberGroup2.setId(Long.parseLong("2"));
		// customMemberGroup2.setGroupType(2);
		// customMemberGroup2.setGroupExpression("grpid:in(29)&mid:in(30)");
		//
		// CustomMemberGroup customMemberGroup3 = new CustomMemberGroup();
		// customMemberGroup3.setId(Long.parseLong("3"));
		// customMemberGroup3.setGroupType(2);
		// customMemberGroup3.setGroupExpression("grpid:in(31)");
		//
		// CustomMemberGroup customMemberGroup4 = new CustomMemberGroup();
		// customMemberGroup4.setId(Long.parseLong("4"));
		// customMemberGroup4.setGroupType(4);
		// customMemberGroup4.setGroupExpression("cmb:in(1,2)");
		//
		// customMemberGroupList.add(customMemberGroup1);
		// customMemberGroupList.add(customMemberGroup2);
		// customMemberGroupList.add(customMemberGroup3);
		// customMemberGroupList.add(customMemberGroup4);
		//
		//
		//
		// EngineManager.getInstance().setCrowdScopeList(customMemberGroupList);
		EngineManager.getInstance().build();

		Map<String, AbstractScopeConditionResult> itemScopeConditionResultMap = EngineManager.getInstance().getItemScopeEngine().getItemScopeMap();
		for (String key : itemScopeConditionResultMap.keySet()) {
			ItemScopeConditionResult itemScopeConditionResult = (ItemScopeConditionResult) itemScopeConditionResultMap.get(key);
			// System.out.println("result:    "+key+"    "+itemScopeConditionResult.getResult("301",
			// "34"));
		}

		Map<String, AbstractScopeConditionResult> crowdScopeConditionResultMap = EngineManager.getInstance().getCrowdScopeEngine().getCrowdScopeMap();
		for (String key : crowdScopeConditionResultMap.keySet()) {
			CrowdScopeConditionResult crowdScopeConditionResult = (CrowdScopeConditionResult) crowdScopeConditionResultMap.get(key);
			// System.out.println("result:    " + key + "    " +
			// crowdScopeConditionResult.getResult("301", "34"));
		}

	}

}
