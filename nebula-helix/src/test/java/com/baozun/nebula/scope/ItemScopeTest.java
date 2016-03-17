package com.baozun.nebula.scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.calculateEngine.common.EngineManager;
import com.baozun.nebula.calculateEngine.condition.AbstractScopeConditionResult;
import com.baozun.nebula.calculateEngine.condition.ItemScopeConditionResult;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml", "classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ItemScopeTest {

	@Test
	public void itemScopeTest() {
		List<ItemTagRuleCommand> ItemTagRuleCommandList = new ArrayList<ItemTagRuleCommand>();
		ItemTagRuleCommand ItemTagRuleCommand1 = new ItemTagRuleCommand();
		ItemTagRuleCommand1.setId(Long.parseLong("1"));
		ItemTagRuleCommand1.setType(1);
		ItemTagRuleCommand1.setExpression("pid:in(12,34)");

		ItemTagRuleCommand ItemTagRuleCommand2 = new ItemTagRuleCommand();
		ItemTagRuleCommand2.setId(Long.parseLong("2"));
		ItemTagRuleCommand2.setType(2);
		ItemTagRuleCommand2.setExpression("call()&!pid:in(1)");

		ItemTagRuleCommand ItemTagRuleCommand3 = new ItemTagRuleCommand();
		ItemTagRuleCommand3.setId(Long.parseLong("3"));
		ItemTagRuleCommand3.setType(2);
		ItemTagRuleCommand3.setExpression("cid:in(31)");

		ItemTagRuleCommand ItemTagRuleCommand = new ItemTagRuleCommand();
		ItemTagRuleCommand.setId(Long.parseLong("4"));
		ItemTagRuleCommand.setType(4);
		ItemTagRuleCommand.setExpression("cmb:in(1,2)");

		ItemTagRuleCommandList.add(ItemTagRuleCommand);
		ItemTagRuleCommandList.add(ItemTagRuleCommand1);
		ItemTagRuleCommandList.add(ItemTagRuleCommand2);
		ItemTagRuleCommandList.add(ItemTagRuleCommand3);
		List<Long> itemCaIds = new ArrayList<Long>();
		itemCaIds.add(34L);
		EngineManager.getInstance().setItemScopeList(ItemTagRuleCommandList);
		EngineManager.getInstance().build();
		Map<String, AbstractScopeConditionResult> itemScopeConditionResultList = EngineManager.getInstance().getItemScopeEngine().getItemScopeMap();
		for (String key : itemScopeConditionResultList.keySet()) {
			ItemScopeConditionResult itemScopeConditionResult = (ItemScopeConditionResult) itemScopeConditionResultList.get(key);

			System.out.println("result:    " + key + "    " + itemScopeConditionResult.getResult(1L, itemCaIds));

			// System.out.println("result:    "+key+"    "+itemScopeConditionResult.getResult("301",
			// "34"));
		}
	}

}
