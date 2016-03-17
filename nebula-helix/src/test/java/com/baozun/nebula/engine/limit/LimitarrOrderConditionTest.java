package com.baozun.nebula.engine.limit;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class LimitarrOrderConditionTest  extends AbstractTransactionalJUnit4SpringContextTests{
	
	private static final Logger log = LoggerFactory
			.getLogger(LimitarrOrderConditionTest.class);

//	@Autowired
//	private EffectImprovedDao effectImprovedDao;
//	@Autowired
//	private EffectEngine effect;
//    
    /**
	 * 测试LimitaryAllSkuNumsOrderCondition
	 
    @Test
    public void testLimitaryAllSkuNumsOrderCondition(){
    	
    	
    	LimitaryEngine limitary = new LimitaryEngine();
    	limitary.buildEngine();
//    	EffectEngine effect = new EffectEngine();
//    	effect.buildEngine();
    	
    	log.info("limitary.buildEngine()","");
    	
    	
    }*/
    
    /**
     * 
     
    @Test
    public void testEffectCondition(){
    	
    	EffectEngine effect = new EffectEngine();
    	effect.buildEngine();
    	
    	EngineSku sku = new EngineSku();
    	sku.setBrandId("4");
    	sku.setState("0");
    	
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("EngineSku", sku);
    	EngineContext context = new EngineContext();
    	context.setParameters(parameters);
    	
    	
    	LimitaryRuleBean ruleBean = effect.getEffectConditionMap().get("4");
    	ConditionGroup conditionGroup = ruleBean.getConditionGroup();
    	List<Condition> list = conditionGroup.getConditions();
//    	EffectStateCondition condition = (EffectStateCondition) list.get(0);
    	EffectIfIncludeCondition condition = (EffectIfIncludeCondition) list.get(0);
    	boolean falag = condition.doCondition(context);
    	
    	log.debug("return flag === ",falag);
    	
    }
        
	    @Test
        public void testLimitCondition(){
    	
    	LimitaryEngine effect = new LimitaryEngine();
    	effect.buildEngine();
    	
    	
    	ShoppingCartSummary shoppingCartSummary  = new ShoppingCartSummary();
    	ShoppingCartSku sku1 = new ShoppingCartSku();
    	ShoppingCartSku sku2 = new ShoppingCartSku();
    	
    	List<ShoppingCartSku> skuList = new ArrayList<ShoppingCartSku>();
    	
    	sku1.setId(1L);
    	sku1.setQuantity(1);
    	
    	sku2.setId(2L);
    	sku2.setQuantity(2);
    	
    	skuList.add(sku1);
    	skuList.add(sku2);
    	shoppingCartSummary.setSkuList(skuList);
    	
    	
    	EngineContext context = new EngineContext();
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("ShoppingCartSummary", shoppingCartSummary);
    	context.setParameters(parameters);
    	
    	
    	LimitaryRuleBean ruleBean = effect.getLimitaryConditionMap().get("1");
    	ConditionGroup conditionGroup = ruleBean.getConditionGroup();
    	List<Condition> list = conditionGroup.getConditions();
//    	EffectStateCondition condition = (EffectStateCondition) list.get(0);
    	LimitaryQtyCondition condition = (LimitaryQtyCondition) list.get(0);
    	boolean falag = condition.doCondition(context);
    	
    	log.debug("return flag === ",falag);
    	
    }
  
    */
	
}
