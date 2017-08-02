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
 */
package com.baozun.nebula.manager.column;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.column.ColumnComponentCommand;
import com.baozun.nebula.command.column.ColumnModuleCommand;
import com.baozun.nebula.command.column.ColumnPageCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.dao.column.ColumnComponentDao;
import com.baozun.nebula.dao.column.ColumnModuleDao;
import com.baozun.nebula.dao.column.ColumnPageDao;
import com.baozun.nebula.dao.column.ColumnPublishedDao;
import com.baozun.nebula.dao.column.ColumnPublishedHistoryDao;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.ItemImageDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.SchedulerManager;
import com.baozun.nebula.model.column.ColumnComponent;
import com.baozun.nebula.model.column.ColumnModule;
import com.baozun.nebula.model.column.ColumnPage;
import com.baozun.nebula.model.column.ColumnPublished;
import com.baozun.nebula.model.column.ColumnPublishedHistory;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.manager.SdkColumnManager;
import com.baozun.nebula.task.DynamicTask;
import com.baozun.nebula.task.PublishModuleTask;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.image.ImageOpeartion;

/**
 * 模块管理Manager实现
 * 
 * @author chenguang.zhou
 * @date 2014年4月4日 上午9:17:24
 */
@Service
@Transactional
public class ColumnManagerImpl implements ColumnManager{

    private static final Logger log = LoggerFactory.getLogger(ColumnManagerImpl.class);

    @Autowired
    private ColumnComponentDao columnComponentDao;

    @Autowired
    private ColumnModuleDao columnModuleDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ItemImageDao itemImageDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private SdkColumnManager sdkColumnManager;

    @Autowired
    private ColumnPublishedDao columnPublishedDao;

    @Autowired
    private ColumnPublishedHistoryDao columnPublishedHistoryDao;

    @Autowired
    private ColumnPageDao columnPageDao;

    @Autowired
    private SchedulerManager schedulerManager;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void saveColumnComponent(ColumnComponent[] columnComponents,String customBaseUrl,String moduleCode,String pageCode,String publishTime,Long targetId) throws Exception{
        ColumnModule columnModule = columnModuleDao.findColumnModuleByCode(moduleCode, pageCode);
        if (columnModule == null){
            throw new BusinessException(ErrorCodes.MODULE_NOT_EXIST);
        }
        Long moduleId = columnModule.getId();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date publishTimeDate = null;
        if (StringUtils.isNotBlank(publishTime)){
            try{
                publishTimeDate = format.parse(publishTime);
                columnModule.setPublishTime(publishTimeDate);
                columnModuleDao.updateColModPubTimById(columnModule.getPublishTime(), moduleId);
            }catch (ParseException e1){
                e1.printStackTrace();
            }
        }
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("moduleId", moduleId);
        if (null != targetId){
            paraMap.put("targetId", targetId);
        }
        List<ColumnComponent> columnCompList = columnComponentDao.findEffectColumnComponentListByQueryMap(paraMap);
        /** 模块的组件集合 */
        Map<Long, ColumnComponent> dbColumnCompMap = new HashMap<Long, ColumnComponent>();
        for (ColumnComponent columnComponent : columnCompList){
            dbColumnCompMap.put(columnComponent.getId(), columnComponent);
        }
        Integer sortNo = 0;
        for (ColumnComponent columnComponent : columnComponents){

            Integer sortNoForPage = columnComponent.getSortNo();
            if (sortNoForPage != null){
                sortNo = sortNoForPage;
            }

            Long id = columnComponent.getId();
            columnComponent.setImg(ImageOpeartion.imageUrlConvert(columnComponent.getImg(), customBaseUrl, true));
            columnComponent.setSortNo(sortNo);
            columnComponent.setModuleId(moduleId);
            if (id == null){
                columnComponentDao.save(columnComponent);
            }else{
                ColumnComponent dbColumnComp = dbColumnCompMap.get(id);
                /** 比对数据是不一致, 一致就不修改, 不一致就修改 */
                if (!isColumnComponentUpdate(dbColumnComp, columnComponent)){
                    columnComponentDao.updateComlumCompById(
                                    columnComponent.getId(),
                                    columnComponent.getDescription(),
                                    columnComponent.getExt(),
                                    columnComponent.getImg(),
                                    columnComponent.getSortNo(),
                                    columnComponent.getTargetId(),
                                    columnComponent.getTitle(),
                                    columnComponent.getUrl(),
                                    columnComponent.getImgHeight(),
                                    columnComponent.getImgWidth());
                }
                dbColumnCompMap.remove(id);
            }
            sortNo++;
        }
        /** 要删除的组件id */
        List<Long> removeList = new ArrayList<Long>();

        for (Map.Entry<Long, ColumnComponent> entry : dbColumnCompMap.entrySet()){
            removeList.add(entry.getKey());
        }
        /** 通过ids删除组件 */
        if (removeList.size() > 0){
            columnComponentDao.removeColumnComponentByIds(removeList);
        }

        Date now = new Date();
        if (null != publishTimeDate && publishTimeDate.compareTo(now) > 0){
            addTask(pageCode, moduleCode, publishTimeDate);
        }
    }

    @Override
    public void saveColumnComponent(ColumnComponent[] columnComponents,String customBaseUrl,String moduleCode,String pageCode,String publishTime) throws Exception{
        ColumnModule columnModule = columnModuleDao.findColumnModuleByCode(moduleCode, pageCode);
        if (columnModule == null){
            throw new BusinessException(ErrorCodes.MODULE_NOT_EXIST);
        }
        Long moduleId = columnModule.getId();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date publishTimeDate = null;
        if (StringUtils.isNotBlank(publishTime)){
            try{
                publishTimeDate = format.parse(publishTime);
                columnModule.setPublishTime(publishTimeDate);
                columnModuleDao.updateColModPubTimById(columnModule.getPublishTime(), moduleId);
            }catch (ParseException e1){
                e1.printStackTrace();
            }
        }
        List<ColumnComponent> columnCompList = columnComponentDao.findColumnCompListByModuleId(moduleId);
        /** 模块的组件集合 */
        Map<Long, ColumnComponent> dbColumnCompMap = new HashMap<Long, ColumnComponent>();
        for (ColumnComponent columnComponent : columnCompList){
            dbColumnCompMap.put(columnComponent.getId(), columnComponent);
        }
        Integer sortNo = 0;
        for (ColumnComponent columnComponent : columnComponents){
            Long id = columnComponent.getId();
            columnComponent.setImg(com.baozun.nebula.utils.image.ImageOpeartion.imageUrlConvert(columnComponent.getImg(), customBaseUrl, true));

            Integer sortNoForPage = columnComponent.getSortNo();
            if (sortNoForPage != null){
                sortNo = sortNoForPage;
            }

            columnComponent.setSortNo(sortNo);
            columnComponent.setModuleId(moduleId);
            if (id == null){
                columnComponentDao.save(columnComponent);
            }else{
                ColumnComponent dbColumnComp = dbColumnCompMap.get(id);
                /** 比对数据是不一致, 一致就不修改, 不一致就修改 */
                if (!isColumnComponentUpdate(dbColumnComp, columnComponent)){
                    columnComponentDao.updateComlumCompById(
                                    columnComponent.getId(),
                                    columnComponent.getDescription(),
                                    columnComponent.getExt(),
                                    columnComponent.getImg(),
                                    columnComponent.getSortNo(),
                                    columnComponent.getTargetId(),
                                    columnComponent.getTitle(),
                                    columnComponent.getUrl(),
                                    columnComponent.getImgHeight(),
                                    columnComponent.getImgWidth());
                }
                dbColumnCompMap.remove(id);
            }
            sortNo++;
        }
        /** 要删除的组件id */
        List<Long> removeList = new ArrayList<Long>();

        for (Map.Entry<Long, ColumnComponent> entry : dbColumnCompMap.entrySet()){
            removeList.add(entry.getKey());
        }
        /** 通过ids删除组件 */
        if (removeList.size() > 0){
            columnComponentDao.removeColumnComponentByIds(removeList);
        }

        Date now = new Date();
        if (null != publishTimeDate && publishTimeDate.compareTo(now) > 0){
            addTask(pageCode, moduleCode, publishTimeDate);
        }

    }

    /**
     * 判断版块组件中的数据是否有修改过
     * 
     * @param dbColumnComp
     * @param columnComp
     * @return
     */
    private Boolean isColumnComponentUpdate(ColumnComponent dbColumnComp,ColumnComponent columnComp){
        if (dbColumnComp == null){
            return false;
        }
        /** description */
        String dbDesc = dbColumnComp.getDescription();
        String desc = columnComp.getDescription();
        if (StringUtils.isNotBlank(dbDesc) || StringUtils.isNotBlank(desc)){
            if (StringUtils.isNotBlank(dbDesc)){
                if (!dbDesc.equals(desc)){
                    return false;
                }
            }else{
                if (!desc.equals(dbDesc)){
                    return false;
                }
            }
        }
        /** ext */
        String dbExt = dbColumnComp.getExt();
        String ext = columnComp.getExt();
        if (StringUtils.isNotBlank(dbExt) || StringUtils.isNotBlank(ext)){
            if (StringUtils.isNotBlank(dbExt)){
                if (!dbExt.equals(ext)){
                    return false;
                }
            }else{
                if (!ext.equals(dbExt)){
                    return false;
                }
            }
        }
        /** img */
        String dbImg = dbColumnComp.getImg();
        String img = columnComp.getImg();
        if (StringUtils.isNotBlank(dbImg) || StringUtils.isNotBlank(img)){
            if (StringUtils.isNotBlank(dbImg)){
                if (!dbImg.equals(img)){
                    return false;
                }
            }else{
                if (!img.equals(dbImg)){
                    return false;
                }
            }
        }

        /** url */
        String dbUrl = dbColumnComp.getUrl();
        String url = columnComp.getUrl();
        if (StringUtils.isNotBlank(dbUrl) || StringUtils.isNotBlank(url)){
            if (StringUtils.isNotBlank(dbUrl)){
                if (!dbUrl.equals(url)){
                    return false;
                }
            }else{
                if (!url.equals(dbUrl)){
                    return false;
                }
            }
        }
        /** targetId */
        Long dbTargetId = dbColumnComp.getTargetId();
        Long targetId = columnComp.getTargetId();
        if (dbTargetId != null || targetId != null){
            if (dbTargetId != null){
                if (!dbTargetId.equals(targetId)){
                    return false;
                }
            }else{
                if (!targetId.equals(dbTargetId)){
                    return false;
                }
            }
        }
        /** title */
        String dbTitle = dbColumnComp.getTitle();
        String title = columnComp.getTitle();
        if (StringUtils.isNotBlank(dbTitle) || StringUtils.isNotBlank(title)){
            if (StringUtils.isNotBlank(dbTitle)){
                if (!dbTitle.equals(title)){
                    return false;
                }
            }else{
                if (!title.equals(dbTitle)){
                    return false;
                }
            }
        }
        /** sort_NO */
        Integer dbSortNo = dbColumnComp.getSortNo();
        Integer sortNo = columnComp.getSortNo();
        if (dbSortNo != null || sortNo != null){
            if (dbSortNo != null){
                if (!dbSortNo.equals(sortNo)){
                    return false;
                }
            }else{
                if (!sortNo.equals(dbSortNo)){
                    return false;
                }
            }
        }

        Integer dbWidth = dbColumnComp.getImgWidth();
        Integer width = columnComp.getImgWidth();
        if (dbWidth != null || width != null){
            if (dbWidth != null){
                if (!dbWidth.equals(width)){
                    return false;
                }
            }else{
                if (!width.equals(dbWidth)){
                    return false;
                }
            }
        }

        Integer dbHeight = dbColumnComp.getImgHeight();
        Integer height = columnComp.getImgHeight();
        if (dbHeight != null || height != null){
            if (dbHeight != null){
                if (!dbHeight.equals(height)){
                    return false;
                }
            }else{
                if (!height.equals(dbHeight)){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public List<ColumnComponentCommand> findColumnComponentByPageCode(String pageCode){
        ColumnPage columnPage = columnPageDao.findColumnPageByPageCode(pageCode);
        if (null == columnPage){
            throw new BusinessException(ErrorCodes.COLUMN_PAGE_NOT_EXIST);
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pageId", columnPage.getId());
        List<ColumnModuleCommand> columnModuleCommandList = columnModuleDao.findColumnModuleCommandListByQueryMap(paramMap);
        /** key: module id ; value: ColumnModuleCommand */
        Map<Long, ColumnModuleCommand> columnModuleMap = new HashMap<Long, ColumnModuleCommand>();
        for (ColumnModuleCommand columnModuleCommand : columnModuleCommandList){
            columnModuleMap.put(columnModuleCommand.getId(), columnModuleCommand);
        }

        List<ColumnComponentCommand> columnCompCommandList = columnComponentDao.findColumnComponentByPageCode(pageCode);

        return processColumnComponentData(columnCompCommandList, columnModuleMap);
    }

    /**
     * 处理columnComponentCommand中的数据
     * 
     * @param ColumnComponentCommandList
     * @return
     */
    private List<ColumnComponentCommand> processColumnComponentData(List<ColumnComponentCommand> columnCompCommandList,Map<Long, ColumnModuleCommand> columnModuleMap){
        List<Long> itemIds = new ArrayList<Long>();
        List<Long> categoryIds = new ArrayList<Long>();
        for (ColumnComponentCommand columnComponentCommand : columnCompCommandList){
            ColumnModuleCommand columnModuleCommand = columnModuleMap.get(columnComponentCommand.getModule_id());
            Long targetId = columnComponentCommand.getTargetId();
            Integer type = columnModuleCommand.getType();
            /**
             * 新品热推管理, 精品推荐默认商品管理, 最热排行管理都是商品 精品推荐默认分类管理 是商品分类
             */
            if (null == targetId || null == type){
                continue;
            }
            if (type.equals(ColumnModule.TYPE_ITEM)){
                itemIds.add(targetId);
            }else if (type.equals(ColumnModule.TYPE_CATEGORY)){
                categoryIds.add(targetId);
            }
        }
        /** item image */
        List<ItemImage> itemImageList = itemImageDao.findItemImageByItemIds(itemIds, ItemImage.IMG_TYPE_LIST);
        Map<Long, List<ItemImage>> itemImageListMap = new HashMap<Long, List<ItemImage>>();
        List<ItemImage> itemImages = null;
        for (Long itemId : itemIds){
            itemImages = new ArrayList<ItemImage>();
            for (ItemImage itemIamge : itemImageList){
                if (itemId.equals(itemIamge.getItemId())){
                    itemImages.add(0, itemIamge);
                }
            }
            itemImageListMap.put(itemId, itemImages);
        }
        /** item */
        List<ItemCommand> itemCommandList = itemDao.findItemCommandListByIds(itemIds);
        Map<Long, ItemCommand> itemCommandMap = new HashMap<Long, ItemCommand>();
        for (ItemCommand itemCommand : itemCommandList){
            Long itemId = itemCommand.getId();
            itemCommand.setItemImageList(itemImageListMap.get(itemId));
            itemCommandMap.put(itemId, itemCommand);
        }

        /** category */
        List<Category> categoryList = categoryDao.findCategoryListByIds(categoryIds);
        Map<Long, Category> categoryMap = new HashMap<Long, Category>();
        for (Category category : categoryList){
            categoryMap.put(category.getId(), category);
        }

        for (ColumnComponentCommand columnComponentCommand : columnCompCommandList){
            ColumnModuleCommand columnModuleCommand = columnModuleMap.get(columnComponentCommand.getModule_id());
            Long targetId = columnComponentCommand.getTargetId();
            Integer type = columnModuleCommand.getType();
            if (null == targetId || null == type){
                continue;
            }

            if (ColumnModule.TYPE_ITEM.equals(type)){
                columnComponentCommand.setItemCommand(itemCommandMap.get(targetId));
            }else if (ColumnModule.TYPE_CATEGORY.equals(type)){
                columnComponentCommand.setCategory(categoryMap.get(targetId));
            }

        }

        return columnCompCommandList;
    }

    /**
     * 添加定时任务(定时发布)
     * 
     * @param pageCode
     * @param moduleCode
     * @param publishTime
     * @throws Exception
     */
    private void addTask(String pageCode,String moduleCode,Date publishTime) throws Exception{
        // 先删除已经有的
        DynamicTask task = new PublishModuleTask(this, pageCode, moduleCode);

        String taskName = task.getTaskName();

        schedulerManager.removeTask(task.getTaskName());

        // 添加新的
        schedulerManager.addTask(task, "invoke", publishTime, taskName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.column.ColumnManager#publishColumnPage(java.lang.String)
     */
    @Override
    public boolean publishColumnPage(String pageCode){
        ColumnPageCommand pageCmd = sdkColumnManager.findColumnModuleMapByPageCode(pageCode);

        Map<String, ColumnModuleCommand> moduleMap = pageCmd.getColumnModuleMap();
        if (Validator.isNotNullOrEmpty(moduleMap)){
            Date publishedDate = new Date();
            ObjectMapper mapper = new ObjectMapper();

            List<Long> idsToBedel = new ArrayList<Long>();

            for (String key : moduleMap.keySet()){
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("pageCode", pageCode);
                paramMap.put("moduleCode", key);
                List<ColumnPublished> list = columnPublishedDao.findColumnPublishedListByQueryMap(paramMap);

                if (Validator.isNotNullOrEmpty(list)){
                    for (ColumnPublished p : list){
                        idsToBedel.add(p.getId());
                    }
                }
            }
            // 删除t_col_column_published表中的对应的数据
            columnPublishedDao.deleteAllByPrimaryKey(idsToBedel);
            // columnPublishedDao.removeColumnPublishedByIds(idsToBedel);

            for (String key : moduleMap.keySet()){
                ColumnModuleCommand module = moduleMap.get(key);
                List<ColumnComponentCommand> cptCmdList = module.getComponentList();

                List<ColumnComponent> columnComponentList = new ArrayList<ColumnComponent>(cptCmdList.size());

                if (Validator.isNotNullOrEmpty(cptCmdList)){
                    for (ColumnComponentCommand ccCmd : cptCmdList){
                        ColumnComponent cc = new ColumnComponent();
                        cc = (ColumnComponent) ConvertUtils.convertFromTarget(cc, ccCmd);
                        columnComponentList.add(cc);
                    }
                }

                String value = null;

                // 转换成json格式的数据
                if (Validator.isNotNullOrEmpty(columnComponentList)){
                    try{
                        value = mapper.writeValueAsString(columnComponentList);
                    }catch (Exception e){
                        log.error(e.getMessage());

                        // 转json失败 ， 回滚事务
                        throw new RuntimeException();
                    }
                }

                // 在t_col_column_published和t_col_column_published_history表中添加数据
                ColumnPublished published = new ColumnPublished();
                ColumnPublishedHistory history = new ColumnPublishedHistory();

                published.setModuleCode(key);
                published.setPageCode(pageCode);
                published.setPublishedTime(publishedDate);
                published.setValue(value);

                history.setModuleCode(key);
                history.setPageCode(pageCode);
                history.setPublishedTime(publishedDate);
                history.setValue(value);

                columnPublishedDao.save(published);
                columnPublishedHistoryDao.save(history);
            }
            // 删除缓存中的数据
            cacheManager.removeMapValue(CacheKeyConstant.COLUMN_KEY, pageCode);
            return true;
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.column.ColumnManager#publishColumnModule(java.lang.String, java.lang.String)
     */
    @Override
    public boolean publishColumnModule(String pageCode,String moduleCode){

        // 根据 pageCode 和 moduleCode 查出对应模块
        ColumnModule module = columnModuleDao.findColumnModuleByPageCodeAndModuleCode(pageCode, moduleCode);

        if (Validator.isNotNullOrEmpty(module)){

            // 查出已经发布过的东西，并且删除
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("pageCode", pageCode);
            paramMap.put("moduleCode", moduleCode);
            List<ColumnPublished> list = columnPublishedDao.findColumnPublishedListByQueryMap(paramMap);

            List<Long> idsToBedel = new ArrayList<Long>();

            if (Validator.isNotNullOrEmpty(list)){
                for (ColumnPublished p : list){
                    idsToBedel.add(p.getId());
                }
            }

            columnPublishedDao.deleteAllByPrimaryKey(idsToBedel);

            Long moduleId = module.getId();
            List<ColumnComponent> componentList = columnComponentDao.findColumnCompListByModuleId(moduleId);
            if (Validator.isNotNullOrEmpty(componentList)){
                String value = null;

                Date publishedDate = new Date();
                ObjectMapper mapper = new ObjectMapper();

                if (Validator.isNotNullOrEmpty(componentList)){

                    try{
                        value = mapper.writeValueAsString(componentList);
                    }catch (Exception e){
                        log.error(e.getMessage());

                        // 转json失败 ， 回滚事务
                        throw new RuntimeException();
                    }
                }

                ColumnPublished published = new ColumnPublished();
                ColumnPublishedHistory history = new ColumnPublishedHistory();

                published.setModuleCode(moduleCode);
                published.setPageCode(pageCode);
                published.setPublishedTime(publishedDate);
                published.setValue(value);

                history.setModuleCode(moduleCode);
                history.setPageCode(pageCode);
                history.setPublishedTime(publishedDate);
                history.setValue(value);

                columnPublishedDao.save(published);
                columnPublishedHistoryDao.save(history);

                return true;
            }
        }
        return false;
    }

    @Override
    public List<ColumnModuleCommand> findColumnModuleByPageCode(String pageCode){
        ColumnPage columnPage = columnPageDao.findColumnPageByPageCode(pageCode);
        if (null == columnPage){
            throw new BusinessException(ErrorCodes.COLUMN_PAGE_NOT_EXIST);
        }
        List<ColumnModuleCommand> columnModuleCommandList = columnModuleDao.findColumnModuleCommandByPageCode(pageCode);
        List<Long> moduleIdList = new ArrayList<Long>();
        /** key: module id ; value: ColumnModuleCommand */
        Map<Long, ColumnModuleCommand> columnModuleMap = new HashMap<Long, ColumnModuleCommand>();
        for (ColumnModuleCommand columnModuleCommand : columnModuleCommandList){
            moduleIdList.add(columnModuleCommand.getId());
            columnModuleMap.put(columnModuleCommand.getId(), columnModuleCommand);
        }
        // columnComponent
        List<ColumnComponentCommand> columnComponentList = columnComponentDao.findColumnComponentCommandListByModuleIds(moduleIdList);
        List<ColumnComponentCommand> columnCompCommandList = processColumnComponentData(columnComponentList, columnModuleMap);

        /****** key: module_id, value: ColumnComponentCommand集合 ******/
        Map<Long, List<ColumnComponentCommand>> columnComponentMap = new HashMap<Long, List<ColumnComponentCommand>>();
        for (ColumnModuleCommand columnModuleCommand : columnModuleCommandList){
            List<ColumnComponentCommand> componentList = new ArrayList<ColumnComponentCommand>();
            Long moduleId = columnModuleCommand.getId();
            for (ColumnComponentCommand columnComponentCommand : columnCompCommandList){
                if (moduleId.equals(columnComponentCommand.getModule_id())){
                    componentList.add(columnComponentCommand);
                }
            }
            columnComponentMap.put(moduleId, componentList);
        }

        for (ColumnModuleCommand columnModuleCommand : columnModuleCommandList){
            columnModuleCommand.setComponentList(columnComponentMap.get(columnModuleCommand.getId()));
            columnModuleCommand.setPageId(columnPage.getId());
            columnModuleCommand.setPageCode(columnPage.getCode());
        }
        return columnModuleCommandList;
    }

    @Override
    public List<ColumnComponent> findColumnCompByTargetId(Long targetId,String moduleCode,String pageCode){

        ColumnModule columnModule = columnModuleDao.findColumnModuleByCode(moduleCode, pageCode);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("targetId", targetId);
        paraMap.put("moduleId", columnModule.getId());
        List<ColumnComponent> columnComponentList = columnComponentDao.findEffectColumnComponentListByQueryMap(paraMap);
        return columnComponentList;
    }
}
