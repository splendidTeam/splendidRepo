package com.baozun.nebula.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SuggestCommand;
import com.baozun.nebula.solr.dao.SolrGeneralDao;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.solr.manager.SolrManager;
import com.baozun.nebula.solr.utils.SolrOrderSort;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SolrTest {

	@Autowired
	private SolrGeneralDao solrGeneralDao;

	@Autowired
	private ItemSolrManager skuManager;

	@Autowired
	protected SolrServer solrServer;

	@Autowired
	private SolrManager solrManager;

	@Autowired
	private ItemSolrManager itemSolrManager;

	 @Test
	public void testCleanAll() throws IOException {
		System.out.println("Clean All result is  " + skuManager.cleanAll());
	}

	//
	// //@Test
	// public void testDeleteItem(){
	// List<String> ids = new ArrayList<String>();
	// System.out.println("Delete Item result is  "+skuManager.deleteItem(ids));
	// }
	//
	@Test
	public void testSaveOrUpdateItem() {
		 //List<Long> itemIds = new ArrayList<Long>();
		 //itemIds.add(Long.parseLong("7253"));
		// itemSolrManager.saveOrUpdateItem(itemIds);
		 //System.out.println("Clean All result is  "+skuManager.cleanAll());
		itemSolrManager.reRefreshAllItem();
	}

	@Test
	public void testQueryItem() {
		QueryConditionCommand queryConditionCommand = new QueryConditionCommand();
		// List<String> channels = new ArrayList<String>();
		// channels.add("c_1_1");
		// queryConditionCommand.setChannelId(channels);
		// List<String> color = new ArrayList<String>();
		// color.add("1");
		// queryConditionCommand.setColor(color);
		// queryConditionCommand.setKeyword("xx");
		Map<String, String> test = new HashMap<String, String>();
		
		//668=[853], 670=[855]
		
		
		//test.put("191", "跑步鞋");
		//queryConditionCommand.setCategory_name(test);
		//queryConditionCommand.setIsSpread(true);
		String[] facetFields = null;
		SolrOrderSort[] order = null;
		//String groupField = "code";
		DataFromSolr dataFromSolr = skuManager.queryItemForAll(10,
				queryConditionCommand, null, null, null, 1);
		List<ItemResultCommand> a = dataFromSolr.getItems().getItems();
////		Integer b = dataFromSolr.getNumber();
//		// Map<String, Map<String, Long>> c = dataFromSolr.getFacetResult();
//		//
//		// System.out.println(b);
//		// System.out.println("\n\t");
//		// System.out.println("Facet:");
//		// for(String key:c.keySet()){
//		// System.out.println(key+":"+c.get(key));
//		// }
//
//		System.out.println("\n\t");
//		System.out.println("Item:");
//
		for (ItemResultCommand itemResultCommand : a) {
			System.out.println(itemResultCommand.getCode());
		}

	}

	//@Test
	public void spellcheck() {
		String keyWord = "diannao";
		SuggestCommand suggestCommand = itemSolrManager.keywordSpellSpeculation(keyWord);
		System.out.println(suggestCommand.getHitNum());
	}

}
