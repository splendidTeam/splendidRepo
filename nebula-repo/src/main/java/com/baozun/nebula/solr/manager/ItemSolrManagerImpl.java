package com.baozun.nebula.solr.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.command.ItemSolrI18nCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.handler.ItemForSolrCommandHandler;
import com.baozun.nebula.sdk.manager.SdkItemVisibilityManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.ItemDataFromSolr;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SolrGroupData;
import com.baozun.nebula.solr.command.SuggestCommand;
import com.baozun.nebula.solr.command.SuggestDetailCommand;
import com.baozun.nebula.solr.convert.CommandConvert;
import com.baozun.nebula.solr.factory.SolrQueryFactory;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.spring.SpringUtil;

@Service("itemSolrManager")
@Transactional
public class ItemSolrManagerImpl<T> implements ItemSolrManager {

	private static final Integer rowsNum = 20;

	private static final Logger log = LoggerFactory
			.getLogger(ItemSolrManagerImpl.class);

	@Autowired
	private SolrManager solrManager;

	@Autowired
	private ItemInfoManager itemInfoManager;

	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;
	
	@Autowired
	private  SdkItemVisibilityManager sdkItemVisibilityManager;

	@Autowired(required = false)
	private ItemForSolrCommandHandler customizeItemForSolrHandler;
	
	@SuppressWarnings("unchecked")
	@Override
	public DataFromSolr queryItemForAll(int rows,
			QueryConditionCommand queryConditionCommand, String[] facetFields,
			SolrOrderSort[] order, String groupField, Integer currentPage) {
		SolrGroupData<T> solrGroup = new SolrGroupData<T>();
		DataFromSolr dataFromSolr = new DataFromSolr();
		SolrQuery solrQuery = new SolrQuery();
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			SolrQueryFactory.createSolrQueryI18n(queryConditionCommand, solrQuery);
		}else{
			SolrQueryFactory.createSolrQuery(queryConditionCommand, solrQuery);
		}
		SolrQueryFactory.setSort(solrQuery, order);
		SolrQueryFactory.setFacetField(facetFields, solrQuery);
		String style = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_STYLE);
		if (Boolean.parseBoolean(style)) {
			List<String> groupName = new ArrayList<String>();
			groupName.add("style");
			queryConditionCommand.setGroupNames(groupName);
			SolrQueryFactory.setGroup(queryConditionCommand, solrQuery);

			log.debug(solrQuery.toString());

			solrGroup = solrManager
					.findItemCommandFormSolrBySolrQueryWithGroup(solrQuery);

			dataFromSolr = CommandConvert
					.solrGroupConverterDataFromSolrWithGroup(solrGroup,
							currentPage, rows);
		} else {
			solrGroup = solrManager
					.findItemCommandFormSolrBySolrQueryWithOutGroup(solrQuery);

			log.debug(solrQuery.toString());
			dataFromSolr = CommandConvert
					.solrGroupConverterDataFromSolrWithOutGroup(solrGroup,
							currentPage, rows);
		}

		log.debug(solrQuery.toString());

		return dataFromSolr;
	}

	@Override
	public DataFromSolr queryItemForAllNotGroup(int rows,
			QueryConditionCommand queryConditionCommand, String[] facetFields,
			SolrOrderSort[] order, Integer currentPage) {
		// TODO Auto-generated method stub
		SolrGroupData<T> solrGroup = new SolrGroupData<T>();
		DataFromSolr dataFromSolr = new DataFromSolr();
		SolrQuery solrQuery = new SolrQuery();
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			SolrQueryFactory.createSolrQueryI18n(queryConditionCommand, solrQuery);
		}else{
			SolrQueryFactory.createSolrQuery(queryConditionCommand, solrQuery);
		}
		SolrQueryFactory.setSort(solrQuery, order);
		SolrQueryFactory.setFacetField(facetFields, solrQuery);
		String style = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_STYLE);

		solrGroup = solrManager
				.findItemCommandFormSolrBySolrQueryWithOutGroup(solrQuery);

		log.debug(solrQuery.toString());
		dataFromSolr = CommandConvert
				.solrGroupConverterDataFromSolrWithOutGroup(solrGroup,
						currentPage, rows);

		log.debug(solrQuery.toString());

		return dataFromSolr;
	}

	@Override
	public ItemSolrCommand queryBySku(
			QueryConditionCommand queryConditionCommand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteItem(List<Long> ids) {
		List<String> strList = new ArrayList<String>();
		for (Long id : ids) {
			strList.add(id.toString());
		}
		return solrManager.deledteItemCommandToSolr(strList);
	}

	@Override
	public Boolean cleanAll() {
		return solrManager.cleanAll();
	}

	@Override
	public Boolean saveOrUpdateItem(List<Long> itemIds) {
		List<ItemSolrCommand> itemCommandList = itemInfoManager
				.findItemCommandByItemId(itemIds);
		if (null == itemCommandList || itemCommandList.size() < 1) {
			return false;
		}
		List<ItemForSolrCommand> itemForSolrCommandList = new ArrayList<ItemForSolrCommand>();
		for (ItemSolrCommand itemSolrCommand : itemCommandList) {			
			ItemForSolrCommand itemForSolrCommand = CommandConvert
					.itemCommandConverterItemForSolrCommand(itemSolrCommand);
			//设置商品可见性属性
			setItemVisibility(itemForSolrCommand);
			/* 
			 * 获取商城的设置
			 */
			if(null != customizeItemForSolrHandler){
				try {
						customizeItemForSolrHandler.setCustomItemForSolrCommand(itemForSolrCommand);
				} catch(Exception e){
					log.error(e.getMessage(), e);
				}
			}			
			itemForSolrCommandList.add(itemForSolrCommand);
		}
		return solrManager.updateItemCommandToSolr(itemForSolrCommandList);
	}

	/**
	 * 
	* @author 何波
	* @Description:(商品国际化搜索处理) 
	* @param itemIds
	* @return   
	* Boolean   
	* @throws
	 */
	public Boolean saveOrUpdateItemI18n(List<Long> itemIds) {
		//修改成把所有国际化语言查询出来
		List<ItemSolrI18nCommand> itemCommandList = itemInfoManager.findItemCommandByItemIdI18n(itemIds);
		if (null == itemCommandList || itemCommandList.size() < 1) {
			return false;
		}
		List<ItemForSolrI18nCommand> itemForSolrCommandList = new ArrayList<ItemForSolrI18nCommand>();
		for (ItemSolrI18nCommand itemSolrCommand : itemCommandList) {
			ItemForSolrI18nCommand itemForSolrI18nCommand = CommandConvert.itemCommandConverterItemForSolrI18nCommand(itemSolrCommand);
			
			/* 
			 * 获取商城的设置
			 */
			if(null != customizeItemForSolrHandler){
				try {
						customizeItemForSolrHandler.setCustomItemForSolrCommandI18n(itemForSolrI18nCommand);
				} catch(Exception e){
					log.error(e.getMessage(), e);
				}
			}	
			
			itemForSolrCommandList.add(itemForSolrI18nCommand);
			
		}
		return solrManager.updateItemCommandToSolrI18n(itemForSolrCommandList);
	}
	@Override
	public Boolean reRefreshAllItem() {
		List<ItemSolrCommand> itemCommandList = itemInfoManager
				.findItemCommand();
		if (null == itemCommandList || itemCommandList.size() < 1) {
			return false;
		}
		List<ItemForSolrCommand> itemForSolrCommandList = new ArrayList<ItemForSolrCommand>();
		for (ItemSolrCommand itemSolrCommand : itemCommandList) {
			ItemForSolrCommand itemForSolrCommand = CommandConvert
					.itemCommandConverterItemForSolrCommand(itemSolrCommand);
			//设置商品可见性属性
			setItemVisibility(itemForSolrCommand);
			/* 
			 * 获取商城的设置
			 */
			if(null != customizeItemForSolrHandler){
				try {
						customizeItemForSolrHandler.setCustomItemForSolrCommand(itemForSolrCommand);
				} catch(Exception e){
					e.printStackTrace();
				}
			}	
			itemForSolrCommandList.add(itemForSolrCommand);
		}
		return solrManager.batchUpdateItemCommandToSolr(itemForSolrCommandList);
	}

	@Override
	public SuggestCommand keywordSpellSpeculation(String keyWord) {
		SuggestCommand suggestCommand = new SuggestCommand();
		SolrQuery query = new SolrQuery();
		query.set("q", keyWord);
		query.set("qt", "/suggest");
		query.set("spellcheck", "true");
		query.set("spellcheck.build", "true");
		query.addFilterQuery("spread:0");

		Map<String, String> spellCheckMap = new HashMap<String, String>();
		List<SuggestDetailCommand> detailMap = new ArrayList<SuggestDetailCommand>();
		QueryResponse rsp = solrManager.query(query);

		SpellCheckResponse re = rsp.getSpellCheckResponse();

		for (Suggestion s : re.getSuggestions()) {
			List<String> list = s.getAlternatives();
			for (int i = 0; i < list.size(); i++) {
				String[] spellStr = list.get(i).split("@");

				if (spellStr.length > 1) {
					String parentheses = "[\u4E00-\u9FA5]";
					Pattern pattern = Pattern.compile(parentheses);
					Matcher matcher_f = pattern.matcher(spellStr[0]);
					Matcher matcher_l = pattern
							.matcher(spellStr[spellStr.length - 1]);

					if (!matcher_f.find() && matcher_l.find()) {
						spellCheckMap.put(spellStr[spellStr.length - 1],
								spellStr[spellStr.length - 1]);
					} else {
						spellCheckMap.put(spellStr[0], spellStr[0]);
					}
				} else {
					spellCheckMap.put(spellStr[0], spellStr[0]);
				}
			}
		}
		getDetailMap(detailMap, spellCheckMap);

		suggestCommand.setSuggestDetailCommand(detailMap);
		return suggestCommand;
	}

	public void getDetailMap(List<SuggestDetailCommand> detailMap,
			Map<String, String> spellCheckMap) {
		SolrQuery queryFordetail = new SolrQuery();
		for (String key : spellCheckMap.keySet()) {
			queryFordetail.set("q", "keyword:" + key);
			queryFordetail.addFilterQuery("spread:0");
			QueryResponse rspDetail = solrManager.query(queryFordetail);
			Long count = rspDetail.getResults().getNumFound();
			SuggestDetailCommand suggestDetailCommand = new SuggestDetailCommand();
			suggestDetailCommand.setTitle(key);
			suggestDetailCommand.setCount(count);
			queryFordetail.clear();
			detailMap.add(suggestDetailCommand);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemResultCommand> queryItemByCode(
			QueryConditionCommand queryConditionCommand) {
		SolrGroupData<T> solrGroup = new SolrGroupData<T>();
		List<ItemResultCommand> items = new ArrayList<ItemResultCommand>();
		SolrQuery solrQuery = new SolrQuery();
		SolrQueryFactory
				.createSolrQueryByCode(queryConditionCommand, solrQuery);
		solrGroup = solrManager
				.findItemCommandFormSolrBySolrQueryWithOutGroup(solrQuery);
		log.debug(solrQuery.toString());
		items = CommandConvert.solrGroupConverterDataFromSolrByCode(solrGroup);
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ItemDataFromSolr queryAllEligibleItemData(int rows,
			QueryConditionCommand queryConditionCommand, String[] facetFields,
			SolrOrderSort[] order, String groupField, Integer currentPage) {
		SolrGroupData<T> solrGroup = new SolrGroupData<T>();
		ItemDataFromSolr dataFromSolr = new ItemDataFromSolr();
		SolrQuery solrQuery = new SolrQuery();
		boolean i18n = LangProperty.getI18nOnOff();
		if(i18n){
			SolrQueryFactory.createSolrQueryI18n(queryConditionCommand, solrQuery);
		}else{
			SolrQueryFactory.createSolrQuery(queryConditionCommand, solrQuery);
		}
		SolrQueryFactory.setSort(solrQuery, order);
		SolrQueryFactory.setFacetField(facetFields, solrQuery);
		String style = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_STYLE);
		if (Boolean.parseBoolean(style)) {
			List<String> groupName = new ArrayList<String>();
			if(Validator.isNullOrEmpty(groupField)){
				groupName.add("style");
			}else{
				groupName.add(groupField);
			}
			queryConditionCommand.setGroupNames(groupName);
			SolrQueryFactory.setGroup(queryConditionCommand, solrQuery);

			log.debug(solrQuery.toString());

			solrGroup = solrManager
					.findItemCommandFormSolrBySolrQueryWithGroup(solrQuery);

			dataFromSolr = CommandConvert
					.solrGroupConverterItemDataFromSolrWithGroup(solrGroup,
							currentPage, rows);
		} else {
			solrGroup = solrManager
					.findItemCommandFormSolrBySolrQueryWithOutGroup(solrQuery);

			log.debug(solrQuery.toString());
			dataFromSolr = CommandConvert
					.solrGroupConverterItemDataFromSolrWithOutGroup(solrGroup,
							currentPage, rows);
		}

		log.debug(solrQuery.toString());

		return dataFromSolr;
	}
	
	private void  setItemVisibility(ItemForSolrCommand ifsc){
		SdkMataInfoManager sdkMataInfoManager = (SdkMataInfoManager) SpringUtil.getBean("sdkMataInfoManager");
		String visibility =sdkMataInfoManager.findValue("product.visibility");
		if(!(visibility!= null && visibility.equals("true"))){
			return;
		}
		Long itemId = ifsc.getId();
		//通过itemid查询商品可见性
		Set<Long> members = sdkItemVisibilityManager.getMembersByItemId(itemId);
		if(members == null || members.size()==0){
			ifsc.setAllDisplay(SkuItemParam.all_display_y);
		}else{
			ifsc.setAllDisplay(SkuItemParam.all_display_n);
			List<String> vps = new ArrayList<String>();
			for (Long mem : members) {
				vps.add(String.valueOf(mem));
			}
			ifsc.setVisiblePersons(vps);
		}
	}
}
