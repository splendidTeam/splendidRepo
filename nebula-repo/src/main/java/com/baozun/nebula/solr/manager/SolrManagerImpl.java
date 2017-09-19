package com.baozun.nebula.solr.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.FieldStatsInfo;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.command.ItemTagRelationCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.command.SolrGroup;
import com.baozun.nebula.solr.command.SolrGroupCommand;
import com.baozun.nebula.solr.command.SolrGroupData;
import com.baozun.nebula.solr.dao.SolrGeneralDao;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utilities.common.LangUtil;

import loxia.dao.Pagination;
import net.sf.json.JSON;

@Service("SolrManager")
public class SolrManagerImpl<T, PK extends Serializable> implements SolrManager{

    private static final Logger  log = LoggerFactory.getLogger(SolrManagerImpl.class);

    @Autowired
    protected SolrGeneralDao     solrGeneralDao;

    @Override
    public Pagination<ItemSolrCommand> findAllItemCommandFormSolrByField(String queryString,String sortString,Integer startNum,Integer rowsNum,Integer currentPage,Integer size){
        return null;

    }

    @Override
    public SolrGroupData<T> findItemCommandFormSolrBySolrQueryWithOutGroup(SolrQuery solrQuery){
        SolrGroupData<T> solrGroupData = new SolrGroupData<T>();
        try{
            QueryResponse queryResponse = solrGeneralDao.queryBysolrQuery(solrQuery);
            SolrDocumentList solrDocumentList = queryResponse.getResults();
            Map<String, FieldStatsInfo> fieldStatsInfo = queryResponse.getFieldStatsInfo();
            if (null != solrDocumentList){
                List<T> beans = null;
                boolean i18n = LangProperty.getI18nOnOff();
                if (i18n){
                    beans = convertSolrDocumentListToBeansI18n(solrDocumentList);
                }else{
                    beans = convertSolrDocumentListToBeans(solrDocumentList);
                }

                solrGroupData.setNumFound(solrDocumentList.getNumFound());
                setFacetData(solrGroupData, queryResponse);
                solrGroupData.setSolrCommandMap(beans);
            }
            if (null != fieldStatsInfo){
                solrGroupData.setFieldStatsInfo(fieldStatsInfo);
            }
        }catch (SolrServerException e){
            log.error("SolrManagerImpl findItemCommandFormSolrBySolrQuery error: " + e.toString());
            e.printStackTrace();
        }
        return solrGroupData;
    }

    @Override
    public SolrGroupData<T> findItemCommandFormSolrBySolrQueryWithGroup(SolrQuery solrQuery){
        SolrGroupData<T> solrGroupData = new SolrGroupData<T>();
        try{
            QueryResponse queryResponse = solrGeneralDao.queryBysolrQuery(solrQuery);
            GroupResponse groupResponse = queryResponse.getGroupResponse();
            Map<String, FieldStatsInfo> fieldStatsInfo = queryResponse.getFieldStatsInfo();
            if (null != groupResponse){
                Map<String, SolrGroupCommand<T>> solrGroupCommandMap = new HashMap<String, SolrGroupCommand<T>>();
                List<GroupCommand> groupCommandList = groupResponse.getValues();
                Integer count = 0;
                boolean i18n = LangProperty.getI18nOnOff();
                for (GroupCommand groupCommand : groupCommandList){
                    SolrGroupCommand<T> solrGroupCommand = new SolrGroupCommand<T>();

                    int matches = groupCommand.getMatches();
                    String name = groupCommand.getName();
                    Integer ngroups = groupCommand.getNGroups();

                    List<Group> groupList = groupCommand.getValues();
                    List<SolrGroup<T>> values = new ArrayList<SolrGroup<T>>();
                    for (Group group : groupList){
                        SolrDocumentList solrDocumentList = group.getResult();
                        List<T> beans = null;
                        if (i18n){
                            beans = convertSolrDocumentListToBeansI18n(solrDocumentList);
                        }else{
                            beans = convertSolrDocumentListToBeans(solrDocumentList);
                        }
                        SolrGroup<T> solrGroup = new SolrGroup<T>();
                        solrGroup.setNumFound(solrDocumentList.getNumFound());
                        //solrGroup.setNgroups(group.getGroupValue());
                        solrGroup.setBeans(beans);
                        values.add(solrGroup);
                    }

                    solrGroupCommand.setMatches(matches);
                    solrGroupCommand.setName(name);
                    solrGroupCommand.setNgroups(ngroups);
                    count += ngroups;
                    solrGroupCommand.setItemForSolrCommandList(values);
                    solrGroupCommandMap.put(name, solrGroupCommand);
                }

                setFacetData(solrGroupData, queryResponse);
                solrGroupData.setSolrGroupCommandMap(solrGroupCommandMap);
                solrGroupData.setNumFound(Long.parseLong(count + ""));
            }
            if (null != fieldStatsInfo){
                solrGroupData.setFieldStatsInfo(fieldStatsInfo);
            }
        }catch (SolrServerException e){
            log.error("SolrManagerImpl findItemCommandFormSolrBySolrQuery error: " + e.toString());
            e.printStackTrace();
        }
        return solrGroupData;
    }

    @Override
    public QueryResponse query(SolrQuery solrQuery){
        try{
            return solrGeneralDao.queryBysolrQuery(solrQuery);
        }catch (SolrServerException e){
            log.error("SolrManagerImpl query by solrQuery error: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean deledteItemCommandToSolr(List<String> ids){
        return solrGeneralDao.deleteByIds(ids);
    }

    @Override
    public Boolean batchUpdateItemCommandToSolr(List<ItemForSolrCommand> itemList){
        boolean flag = false;
        if (Validator.isNotNullOrEmpty(itemList)){
            flag = solrGeneralDao.batchUpdateIndex(itemList);
        }else{
            log.error("SolrManagerImpl -> batchUpdateItemCommandToSolr itemCommandList is null!");
        }
        return flag;
    }

    @Override
    public Boolean updateItemCommandToSolr(List<ItemForSolrCommand> itemList){
        boolean flag = false;
        if (Validator.isNotNullOrEmpty(itemList)){
            flag = solrGeneralDao.updateIndex(itemList);
        }else{
            log.error("SolrManagerImpl -> batchUpdateItemCommandToSolr itemCommandList is null!");
        }
        return flag;
    }

    @Override
    public Boolean cleanAll(){
        return solrGeneralDao.cleanAll();
    }

    /**
     * 设置通用的 数据.
     * 
     * @param baseSolrData
     *            the base solr data
     * @param queryResponse
     *            the query response
     */
    private void setFacetData(BaseSolrData baseSolrData,QueryResponse queryResponse){
        Map<String, Map<String, Long>> facetMap = getFacetMap(queryResponse);
        Map<String, Integer> facetQuery = queryResponse.getFacetQuery();

        baseSolrData.setFacetQueryMap(facetQuery);
        baseSolrData.setFacetMap(facetMap);
    }

    /**
     * debug FacetField.
     * 
     * @param queryResponse
     *            the query response
     * @return the facet map
     */
    protected Map<String, Map<String, Long>> getFacetMap(QueryResponse queryResponse){
        Map<String, Map<String, Long>> map = null;
        List<FacetField> facetFields = queryResponse.getFacetFields();
        if (Validator.isNotNullOrEmpty(facetFields)){
            map = new HashMap<String, Map<String, Long>>();
            for (FacetField facetField : facetFields){
                String facetFieldName = facetField.getName();
                List<Count> values = facetField.getValues();
                if (Validator.isNotNullOrEmpty(values)){
                    Map<String, Long> facetMap = new LinkedHashMap<String, Long>();
                    for (Count count : values){
                        String countName = count.getName();
                        Long countCount = count.getCount();
                        facetMap.put(countName, countCount);
                    }
                    map.put(facetFieldName, facetMap);
                }
            }
            if (log.isDebugEnabled()){
                log.debug(JsonFormatUtil.format(map));
            }
        }
        return map;
    }

    public Pagination<ItemSolrCommand> setPagination(
            Pagination<ItemSolrCommand> pageItemCommand,
            List<ItemForSolrCommand> list,
            List<ItemSolrCommand> itemList,
            Integer startNum,
            Integer currentPage,
            Integer size){
        pageItemCommand.setItems(itemList);
        pageItemCommand.setStart(startNum);
        if (currentPage < 1)
            currentPage = 1;
        pageItemCommand.setCurrentPage(currentPage);
        if (size < 1)
            size = 6;
        pageItemCommand.setSize(size);
        pageItemCommand.setCount(list.size());
        if (list.size() % size == 0){
            pageItemCommand.setTotalPages(list.size() / size);
        }else{
            pageItemCommand.setTotalPages(list.size() / size + 1);
        }

        return pageItemCommand;
    }

    /**
     * @param item
     * @param icList
     * @param iyList
     * @param itList
     * @param activeBeginTime
     * @param activeEndTime
     * @param channels
     * @return
     */
    public ItemForSolrCommand itemAndRelationEntityToItemForSolrCommand(
            ItemSolrCommand item,
            List<ItemCategoryCommand> icList,
            List<ItemCategory> iyList,
            List<ItemTagRelationCommand> itList,
            Date activeBeginTime,
            Date activeEndTime,
            List<String> channels){
        return null;
    }

    @Override
    public JSON itemForSolrCommandToJSON(QueryConditionCommand queryConditionCommand,Integer startNum,Integer rowsNum,Integer currentPage,Integer size){
        return null;
    }

    @Override
    public String itemForSolrCommandToXML(QueryConditionCommand queryConditionCommand,Integer startNum,Integer rowsNum,Integer currentPage,Integer size){
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<T> convertSolrDocumentListToBeans(SolrDocumentList solrDocumentList){
        DocumentObjectBinder documentObjectBinder = new DocumentObjectBinder();
        Class<T> modelClass = (Class<T>) ItemForSolrCommand.class;
        List<T> beans = documentObjectBinder.getBeans(modelClass, solrDocumentList);
        return beans;
    }

    @SuppressWarnings("unchecked")
    private List<T> convertSolrDocumentListToBeansI18n(SolrDocumentList solrDocumentList){
        DocumentObjectBinder documentObjectBinder = new DocumentObjectBinder();
        List<ItemForSolrI18nCommand> beans = documentObjectBinder.getBeans(ItemForSolrI18nCommand.class, solrDocumentList);
        if (beans != null && beans.size() > 0){
            String lang = LangUtil.getCurrentLang();
            if (lang.equals(MutlLang.defaultLang())){
                return (List<T>) copyItemForSolrCommandProperties(beans);
            }
            //处理国际化信息
            for (ItemForSolrI18nCommand command : beans){
                String title = getCurrentI18nVal(command.getDynamicTitle(), SkuItemParam.dynamic_title);
                command.setTitle(title);
                String subTitle = getCurrentI18nVal(command.getDynamicSubTitle(), SkuItemParam.dynamic_subTitle);
                command.setSubTitle(subTitle);
                String seoTitle = getCurrentI18nVal(command.getDynamicSeoTitle(), SkuItemParam.dynamic_seoTitle);
                command.setSeoTitle(seoTitle);
                String sketch = getCurrentI18nVal(command.getDynamicSketch(), SkuItemParam.dynamic_sketch);
                command.setSketch(sketch);
                String seoKeywords = getCurrentI18nVal(command.getDynamicSeoKeywords(), SkuItemParam.dynamic_seoKeywords);
                command.setSeoKeywords(seoKeywords);
                String seoDescription = getCurrentI18nVal(command.getDynamicSeoDescription(), SkuItemParam.dynamic_seoDescription);
                command.setSeoDescription(seoDescription);
                String description = getCurrentI18nVal(command.getDynamicSeoDescription(), SkuItemParam.dynamic_description);
                command.setDescription(description);
                String descriptionForSearch = getCurrentI18nVal(command.getDynamicDescriptionForSearch(), SkuItemParam.dynamic_descriptionForSearch);
                command.setDescriptionForSearch(descriptionForSearch);
                List<String> imageUrl = getCurrentI18nListVal(command.getDynamicImageUrl(), SkuItemParam.dynamic_imageUrl);
                command.setImageUrl(imageUrl);
            }
            return (List<T>) copyItemForSolrCommandProperties(beans);
        }

        return null;
    }

    private List<ItemForSolrCommand> copyItemForSolrCommandProperties(List<ItemForSolrI18nCommand> beans){
        List<ItemForSolrCommand> datas = new ArrayList<ItemForSolrCommand>();
        for (ItemForSolrI18nCommand command : beans){
            ItemForSolrCommand solrCommand = new ItemForSolrCommand();
            
            solrCommand.setId(command.getId());
            solrCommand.setCode(command.getCode());
            solrCommand.setKeyword(command.getKeyword());
            solrCommand.setTitle(command.getTitle());
            solrCommand.setSubTitle(command.getSubTitle());
            solrCommand.setSketch(command.getSketch());
            solrCommand.setDescription(command.getDescription());
            solrCommand.setShopName(command.getShopName());
            solrCommand.setIndustryName(command.getIndustryName());
            solrCommand.setStyle(command.getStyle());
            solrCommand.setGroupStyle(command.getGroupStyle());
            solrCommand.setSort_no(command.getSort_no());
            solrCommand.setDefault_sort(command.getDefault_sort());
            solrCommand.setList_price(command.getList_price());
            solrCommand.setSale_price(command.getSale_price());
            solrCommand.setModifyTime(command.getModifyTime());
            solrCommand.setListTime(command.getListTime());
            solrCommand.setActiveBeginTime(command.getActiveBeginTime());
            solrCommand.setSalesCount(command.getSalesCount());
            solrCommand.setRankavg(command.getRankavg());
            solrCommand.setItemCount(command.getItemCount());
            solrCommand.setViewCount(command.getViewCount());
            solrCommand.setFavoredCount(command.getFavoredCount());
            solrCommand.setSeoKeywords(command.getSeoKeywords());
            solrCommand.setSeoDescription(command.getSeoDescription());
            solrCommand.setSeoTitle(command.getSeoTitle());
            solrCommand.setImageUrl(command.getImageUrl());
            solrCommand.setItemIsDisplay(command.getItemIsDisplay());
            solrCommand.setDynamicNameForSearchMap(command.getDynamicNameForSearchMap());
            solrCommand.setDynamicForSearchMap(command.getDynamicForSearchMap());
            solrCommand.setDynamicNameWithoutSearchMap(command.getDynamicNameWithoutSearchMap());
            solrCommand.setDynamicWithoutSearchMap(command.getDynamicWithoutSearchMap());
            solrCommand.setDynamicForCustomerMap(command.getDynamicForCustomerMap());
            solrCommand.setCategoryOrder(command.getCategoryOrder());
            solrCommand.setCategoryParent(command.getCategoryParent());
            solrCommand.setCategoryName(command.getCategoryName());
            solrCommand.setCategoryCode(command.getCategoryCode());
            solrCommand.setAllCategoryCodes(command.getAllCategoryCodes());
            solrCommand.setAllCategoryIds(command.getAllCategoryIds());
            solrCommand.setCategoryTree(command.getCategoryTree());
            solrCommand.setNavigationTree(command.getNavigationTree());
            
            solrCommand.setPinyinAllList_A(command.getPinyinAllList_A());
            solrCommand.setPinyinAllList_B(command.getPinyinAllList_B());
            solrCommand.setChannels(command.getChannels());
            
            datas.add(solrCommand);
        }
        return datas;
    }

    /**
     * @author 何波 @Description: 取出当前语言对应的值 @param map @param prefix @return String @throws
     */
    private String getCurrentI18nVal(Map<String, String> map,String prefix){
        if (map == null || map.size() == 0){
            return null;
        }
        Set<String> keys = map.keySet();
        String lang = LangUtil.getCurrentLang();
        for (String key : keys){
            if (key.equals(prefix + lang)){
                String value = map.get(key);
                if (StringUtils.isBlank(value)){
                    return null;
                }
                return value;
            }
        }
        return null;

    }

    private List<String> getCurrentI18nListVal(Map<String, List<String>> map,String prefix){
        if (map == null || map.size() == 0){
            return null;
        }
        Set<String> keys = map.keySet();
        String lang = LangUtil.getCurrentLang();
        for (String key : keys){
            if (key.equals(prefix + lang)){
                List<String> list = map.get(key);
                if (list == null || list.size() == 0){
                    return null;
                }
                return list;
            }
        }
        return null;

    }

    /**
     * 设置start 和row.
     * 
     * @param pageNumber
     *            the page number
     * @param rows
     *            the rows
     * @param solrQuery
     *            the solr query
     * @return the integer
     */
    protected void setStartAndRows(Integer pageNumber,int rows,SolrQuery solrQuery){
        if (null == pageNumber || pageNumber < 1){
            // 默认
            pageNumber = 1;
        }
        Integer start = (pageNumber - 1) * rows;
        solrQuery.setStart(start);
        solrQuery.setRows(rows);
    }

    @Override
    public Boolean updateItemCommandToSolrI18n(List<ItemForSolrI18nCommand> itemList){
        boolean flag = false;
        if (Validator.isNotNullOrEmpty(itemList)){
            flag = solrGeneralDao.updateIndexI18n(itemList);
        }else{
            log.error("SolrManagerImpl -> batchUpdateItemCommandToSolr itemCommandList is null!");
        }
        return flag;
    }

}
