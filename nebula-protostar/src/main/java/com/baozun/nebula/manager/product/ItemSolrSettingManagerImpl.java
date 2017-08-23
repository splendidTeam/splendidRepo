/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCategoryCommand;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.dao.product.IndustryDao;
import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.DataFromSolr;
import com.baozun.nebula.solr.command.QueryConditionCommand;
import com.baozun.nebula.solr.manager.ItemSolrManager;
import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * ItemSolrManagerImpl.
 *
 * @author: shiyang.lv
 * @date: page.getSize()14年4月29日
 */
@Service
@Transactional
public class ItemSolrSettingManagerImpl implements ItemSolrSettingManager{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemSolrSettingManagerImpl.class);

    /** The item solr manager. */
    @Autowired
    private ItemSolrManager itemSolrManager;

    /** The item category dao. */
    @Autowired
    private ItemCategoryDao itemCategoryDao;

    /** The industry dao. */
    @Autowired
    private IndustryDao industryDao;

    /** The item dao. */
    @Autowired
    private ItemDao itemDao;

    /** The shop manager. */
    @Autowired
    private ShopManager shopManager;

    /** The item manager. */
    @Autowired
    private ItemManager itemManager;

    /** The can do map. */
    private Map<Long, Boolean> canDoMap = new ConcurrentHashMap<Long, Boolean>();

    /** The Constant KEY_WORDS. */
    private static final String KEY_WORDS = "keyWords";

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.product.ItemSolrSettingManager#findItemListByQueryMap(loxia.dao.Page, loxia.dao.Sort[], java.util.Map, java.lang.Long)
     */
    @Override
    public Pagination<ItemCommand> findItemListByQueryMap(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId){

        ShopCommand shop = shopManager.findShopById(shopId);
        String keyWords = (String) paraMap.get(KEY_WORDS);
        DataFromSolr itemSolr = findDataFromSolr(page, shop != null ? shop.getShopname() : null, keyWords);

        // solr信息分页计算
        Long numsFound = 0L;
        if (null != itemSolr && null != itemSolr.getNumber()){
            numsFound = itemSolr.getNumber();
        }
        Integer start = page.getStart();
        Integer size = page.getSize();
        Integer totalPage = 1;
        Integer curPage = 1;

        if (numsFound % size == 0){
            totalPage = (int) (numsFound / size);
        }else{
            totalPage = (int) (numsFound / size + 1);
        }
        if ((start + 1) % size == 0){
            curPage = (int) ((start + 1) / size);
        }else{
            curPage = (int) ((start + 1) / size + 1);
        }
        itemSolr.getItems().setCurrentPage(curPage);
        itemSolr.getItems().setTotalPages(totalPage);
        itemSolr.getItems().setItems(itemSolr.getItems().getCurrentPageItem());

        List<ItemResultCommand> itemResultCommandList = itemSolr.getItems().getItems();

        List<ItemCommand> resultList = convertItemCommand(itemResultCommandList);

        //封装到Pagination
        Pagination<ItemCommand> pagination = new Pagination<ItemCommand>();
        pagination.setItems(resultList);
        pagination.setStart(start);
        pagination.setSize(size);
        pagination.setTotalPages(totalPage);
        pagination.setCurrentPage(curPage);
        pagination.setCount(numsFound);

        return pagination;
    }

    /**
     * 从solr获取DataFromSolr.
     *
     * @param page
     *            the page
     * @param shopName
     *            the shop name
     * @param keyWords
     *            the key words
     * @return the data from solr
     */
    private DataFromSolr findDataFromSolr(Page page,String shopName,String keyWords){

        QueryConditionCommand queryConditionCommand = new QueryConditionCommand();
        queryConditionCommand.setLifecycle(1);
        queryConditionCommand.setShopName(shopName);
        queryConditionCommand.setKeyword(keyWords);

        Integer curPage = 1;

        if ((page.getStart() + 1) % page.getSize() == 0){
            curPage = (page.getStart() + 1) / page.getSize();
        }else{
            curPage = (page.getStart() + 1) / page.getSize() + 1;
        }

        SolrOrderSort[] order = new SolrOrderSort[] { new SolrOrderSort(SkuItemParam.listTime, "desc") };

        DataFromSolr itemSolr = itemSolrManager.queryItemForAllNotGroup(page.getSize(), queryConditionCommand, null, order, curPage);

        return itemSolr;
    }

    /**
     * ItemResultCommand转换为ItemCommand.
     *
     * @param itemResultCommands
     *            the item result commands
     * @return the list
     */
    private List<ItemCommand> convertItemCommand(List<ItemResultCommand> itemResultCommands){

        //获取当前List中的ItemId数组并根据其查询
        Long[] itemIds = getItemIdsFormItemResultCommands(itemResultCommands);
        List<Item> items = itemManager.findItemListByItemIds(itemIds);

        // 分类
        List<ItemCategoryCommand> itemCategory = itemCategoryDao.findAllItemCategoryList();
        List<Industry> industries = industryDao.findAllIndustryList();

        List<ItemCommand> itemCommands = new ArrayList<ItemCommand>();
        for (ItemResultCommand itemResult : itemResultCommands){
            Item thisItem = null;

            //获取当前itemResult对应Item
            for (Item item : items){
                if (itemResult.getId().get(0).equals(item.getId())){
                    thisItem = item;
                    break;
                }
            }

            String code = itemResult.getCode().get(0);
            String title = itemResult.getTitle().get(0);
            Long itemId = itemResult.getId().get(0);
            Long industryId = thisItem != null ? thisItem.getIndustryId() : null;

            //绑定分类名
            ItemCommand itemCommand = new ItemCommand();
            List<String> categoryNames = new ArrayList<String>();
            for (ItemCategoryCommand categoryCommand : itemCategory){
                if (categoryCommand.getItemId().equals(itemId)){
                    categoryNames.add(categoryCommand.getCategoryName());
                }
            }

            //绑定行业名
            for (Industry industry : industries){
                if (industry.getId().equals(industryId)){
                    itemCommand.setIndustryName(industry.getName());
                    break;
                }
            }

            itemCommand.setId(itemId);
            itemCommand.setCode(code);
            itemCommand.setTitle(title);
            itemCommand.setCategoryNames(categoryNames);
            itemCommands.add(itemCommand);
        }

        return itemCommands;
    }

    /**
     * 获取当前List中的ItemId数组.
     *
     * @param itemResultCommands
     *            the item result commands
     * @return the item ids form item result commands
     */
    private Long[] getItemIdsFormItemResultCommands(List<ItemResultCommand> itemResultCommands){
        List<Long> ids = new ArrayList<Long>();
        for (ItemResultCommand itemResultCommand : itemResultCommands){
            if (itemResultCommand.getId() != null){
                ids.add(itemResultCommand.getId().get(0));
            }
        }
        return ids.toArray(new Long[] {});
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.product.ItemSolrSettingManager#deleteAll(java.lang.Long)
     */
    @Override
    public Boolean deleteAll(Long shopId){
        Boolean isOk = false;
        Boolean canDo = getCanDo(shopId);
        try{
            if (canDo){
                canDoMap.put(shopId, false);
                List<Long> ids = findItemIdsByShopId(shopId);
                isOk = itemSolrManager.deleteItem(ids);
                canDoMap.remove(shopId);
            }
        }catch (Exception e){
            canDoMap.remove(shopId);
        }
        return isOk;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.product.ItemSolrSettingManager#reRefreshAll(java.lang.Long)
     */
    @Override
    public Boolean reRefreshAll(Long shopId){
        Boolean isOk = false;
        Boolean canDo = getCanDo(shopId);
        try{
            if (canDo){
                System.out.println("------------------------refreshAll");
                //状态变更为正在执行业务
                canDoMap.put(shopId, false);
                List<Long> ids = findItemIdsByShopId(shopId);
                boolean i18n = LangProperty.getI18nOnOff();
                if (i18n){
                    isOk = itemSolrManager.saveOrUpdateItemI18n(ids);
                }else{
                    isOk = itemSolrManager.saveOrUpdateItem(ids);
                }
                //状态变更为业务执行完毕
                canDoMap.remove(shopId);
            }
        }catch (Exception e){
            //如果出现异常也将状态变更为执行完毕
            canDoMap.remove(shopId);
        }
        return isOk;
    }

    /**
     * 获取当前shopId下所有商品.
     *
     * @param shopId
     *            the shop id
     * @return the list
     */
    private List<Long> findItemIdsByShopId(Long shopId){
        return itemDao.findItemIdsByShopId(shopId);
    }

    /**
     * Gets the can do.
     *
     * @param shopId
     *            the shop id
     * @return the can do
     */
    private Boolean getCanDo(Long shopId){
        if (null == shopId){
            return false;
        }
        if (!canDoMap.containsKey(shopId)){
            canDoMap.put(shopId, true);
        }
        return canDoMap.get(shopId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.product.ItemSolrSettingManager#batchOperationItemSolrIndex(java.util.List, java.lang.Long, java.lang.Integer)
     */
    @Override
    public Boolean batchOperationItemSolrIndex(List<String> itemCodeList,Long shopId,Integer type){
        List<Long> itemIdsForSolr = buildItemIdsForSolr(itemCodeList, shopId, Objects.equals(type, 0));

        //---------------------------------------------------------------------
        if (type == 1){
            return saveOrUpdate(itemIdsForSolr);
        }else if (type == 0){

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("will delete solr index:[{}]", JsonUtil.format(itemIdsForSolr));
            }

            return itemSolrManager.deleteItem(itemIdsForSolr);
        }
        return null;
    }

    /**
     * Save or update.
     *
     * @param itemIdsForSolr
     *            the item ids for solr
     * @return the boolean
     * @since 5.3.2.25
     */
    private Boolean saveOrUpdate(List<Long> itemIdsForSolr){
        if (LangProperty.getI18nOnOff()){
            return itemSolrManager.saveOrUpdateItemI18n(itemIdsForSolr);
        }
        return itemSolrManager.saveOrUpdateItem(itemIdsForSolr);
    }

    /**
     * Builds the item ids for solr.
     *
     * @param itemCodeList
     *            the item code list
     * @param shopId
     *            the shop id
     * @param isDelete
     *            是否是删除
     * @return the list
     * @since 5.3.2.25
     */
    private List<Long> buildItemIdsForSolr(List<String> itemCodeList,Long shopId,boolean isDelete){
        List<Item> itemList = itemDao.findItemListByCodes(itemCodeList, shopId);

        if (isDelete){//since 5.3.2.25
            return getPropertyValueList(itemList, "id");
        }

        //---------------------------------------------------------------------

        //不是删除 那么就是更新了 
        List<Long> itemIdsForSolr = new ArrayList<>();
        for (Item item : itemList){
            //但是可能有商品是批量下架 通过sql 来处理的
            if (Item.LIFECYCLE_ENABLE.equals(item.getLifecycle())){
                itemIdsForSolr.add(item.getId());
            }
        }
        return itemIdsForSolr;
    }
}
