package solr.repository.skuItem;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import com.baozun.nebula.solr.base.command.ItemForSolrCommand;
//import com.baozun.nebula.solr.base.dao.SolrGeneralDao;
//import com.baozun.nebula.solr.base.manager.SolrManager;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:spring.xml" })
public class SolrManagerImplTest extends AbstractJUnit4SpringContextTests{
	
	private static final Logger		log	= LoggerFactory.getLogger(SolrManagerImplTest.class);
	
//	@Autowired
//    protected SolrManager solrManager;
//	
//	@Autowired
//	protected SolrGeneralDao solrGeneralDao;
	
	/**
	 * 测试SolrManagerImpl
	 */
	@Test
	public void managerImplTest(){
		
//		solrManager.findAllItemCommandFormSolrByField("code:abc", "code desc", 0, 1, 0, 6);
	}

	
	/**
	 * 测试SolrGeneralDao
	 */
	@Test
	public void daoTest(){
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
//		List<String> ids =new ArrayList<String>();
//		ids.add(i1.getId().toString());
//		ids.add(i2.getId().toString());
////		solrGeneralDao.createOrUpdateIndex(i1);
////		solrGeneralDao.deleteById("2");
////		solrGeneralDao.deleteByIds(ids);
////		solrGeneralDao.batchUpdateIndex(list);
////		solrGeneralDao.cleanAll();
//		
//		try {
////			List<ItemForSolrCommand> it = solrGeneralDao.queryAll();
////			for(ItemForSolrCommand ic : it){
////				System.out.print(" ### id == "+ic.getId()+" code == "+ic.getCode()+" ### ");
////				
////			}
//			SolrQuery solr = new SolrQuery();
//			solr.setQuery("code:abc");
//			solr.setRows(1);
//			solr.setStart(0);
//			solr.setSortField("code", SolrQuery.ORDER.asc);
//			List<ItemForSolrCommand> itSolr = solrGeneralDao.queryBysolrQuery(solr);
//			
//			
////			List<ItemForSolrCommand> itSolr = (List<ItemForSolrCommand>) solrGeneralDao.queryByField("code:abc", "code desc", 0, 1);
//			for(ItemForSolrCommand ic : itSolr){
//				System.out.print(" ### id == "+ic.getId()+" code == "+ic.getCode()+" ### ");
//				
//			}
//			
//		} catch (SolrServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		log.debug("daoTest success");
	}
}
