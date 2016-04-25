package solr.repository.skuItem;


import org.apache.solr.client.solrj.SolrServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;





/**
 * 构建solr 索引使用的test
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:spring.xml" })
public class AddOrUpdateSolrTest extends AbstractJUnit4SpringContextTests{

	private static final Logger		log	= LoggerFactory.getLogger(AddOrUpdateSolrTest.class);



	protected SolrServer		solrServer;

	/**
	 * 测试创建单个对象索引
	 
	@Test
	public void batchUpdateIndex(){
		BaseSkuCommand baseSkuCommand = new BaseSkuCommand();
		baseSkuCommand.setId(1L);
		baseSkuCommand.setCode("abc");
		baseSkuCommand.setColor("red");
		

		Long l = new Date().getTime();
		try{
			solrServer = new CommonsHttpSolrServer("http://localhost:2002/solr-nebula/sku");//测试多核选择需要测试的那个
			
			
      
			 try {        
				 UpdateResponse response = solrServer.addBean(baseSkuCommand);
				 solrServer.commit();
  
				 } 
			 catch (Exception e) 
			 {       
				 e.printStackTrace();
			 } 
			

		}catch (Exception e){
			log.error(" Solr batchSave "+e.getMessage());
			throw new SolrException("Batch save model failed for modelClass BaseSkuCommand," + e.getMessage());
		}
		
		System.out.print("end..." + (new Date().getTime() - l) / 1000);
	}*/

	
	/**
	 * 测试创建集合索引
	 */
	@Test
	public void createOrUpdateIndex(){
//		
//		ItemForSolrCommand i1 = new ItemForSolrCommand();
//		i1.setId(2L);
////		s1.setCategoryName("category1");
//		i1.setCode("abc");
//		i1.setColor("red");
//		
//		ItemForSolrCommand i2 = new ItemForSolrCommand();
//		i2.setId(3L);
////		s2.setCategoryName("category2");
//		i2.setCode("efg");
//		i2.setColor("blue");
//		
//		List<ItemForSolrCommand> list = new ArrayList<ItemForSolrCommand>();
//		list.add(i1);
//		list.add(i2);
//		
//		
//		
//		
//	      
//		 try {        
//			 
//			 solrServer = new CommonsHttpSolrServer("http://localhost:2002/solr-nebula/sku");//测试多核选择需要测试的那个
//			 UpdateResponse response = solrServer.addBeans(list);
//			 solrServer.commit();
//
//			 } 
//		 catch (Exception e) 
//		 {       
//			 e.printStackTrace();
//		 } 
//		
	}

}
