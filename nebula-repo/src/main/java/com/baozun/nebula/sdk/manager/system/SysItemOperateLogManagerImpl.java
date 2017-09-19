package com.baozun.nebula.sdk.manager.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.system.SystemOperateLogDao;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.model.system.SystemOperateLog;
import com.baozun.nebula.sdk.manager.SdkItemCategoryManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.feilong.core.util.CollectionsUtil;
import com.google.gson.Gson;

@Service("saveSysItemOperateLog")
public class SysItemOperateLogManagerImpl implements SysItemOperateLogManager{

    @Autowired
    private SdkItemManager              sdkItemManager;

    @Autowired
    private SdkItemCategoryManager      sdkItemCategoryManager;

    @Autowired
    private SystemOperateLogDao        sysItemOperateLogDao;
    
    @Autowired
    private SdkMataInfoManager          sdkMataInfoManager;
    
    @Autowired
    private ItemDao                     itemDao;

    /** 自动下架状态 */
    private static final String LIFECYCLE_OFF_SHELVES_AUTO = "LIFECYCLE_OFF_SHELVES_AUTO";
    
    @Override
    public void saveSysOperateLog(Long targetId,Long userId,Long type){
        //判断商品当前状态是否为自动下架，如果符合条件则记录日志并将状态刷为下架状态
        ItemCommand itemCommand = sdkItemManager.findItemCommandById(targetId);
        String lifecycle_off_shelves_auto = sdkMataInfoManager.findValue(LIFECYCLE_OFF_SHELVES_AUTO);
        if (lifecycle_off_shelves_auto.equals(itemCommand.getLifecycle())){
            List<Long> itemIds = new ArrayList<Long>();
            itemIds.add(targetId);
            //动态属性
            List<ItemProperties> itemProperties = sdkItemManager.findItemPropertiesByItemId(targetId);
            //获取sku集合
            List<Sku> skuList = sdkItemManager.findSkuByItemId(targetId);
            List<String> extentionCodeList = CollectionsUtil.getPropertyValueList(skuList, "outid");
            //sku库存信息
            List<SkuInventory> skuInventory = sdkItemManager.findSkuInventoryByExtentionCodes(extentionCodeList);
            //图片信息
            List<ItemImage> itemImage = sdkItemManager.findItemImageByItemIds(itemIds);
            //商品分类
            List<ItemCategory> itemCategory = sdkItemCategoryManager.findItemCategoryListByItemId(targetId);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("itemCommand", itemCommand);
            map.put("itemProperties", itemProperties);
            map.put("skuList", skuList);
            map.put("skuInventory", skuInventory);
            map.put("itemImage", itemImage);
            map.put("itemCategory", itemCategory);

            Gson gson = new Gson();
            String itemAllInfoCommandJson = gson.toJson(map);

            SystemOperateLog systemOperateLog = new SystemOperateLog();
            systemOperateLog.setContext(itemAllInfoCommandJson);
            systemOperateLog.setTargetId(targetId+"");;
            //如果是wormhole同步商品信息的操作我觉得optId可以记为-1，或者一个很大的数
            systemOperateLog.setOptId(userId);
            /** 1.单个操作，2.系统推送，3.导入 */
            systemOperateLog.setOptType(type);
            systemOperateLog.setCreateTime(new Date());
            sysItemOperateLogDao.save(systemOperateLog);
            //更新自动下架状态为下架
            String updateListTimeFlag = sdkMataInfoManager.findValue(MataInfo.UPDATE_ITEM_LISTTIME);
            itemDao.enableOrDisableItemByIds(itemIds, BaseModel.LIFECYCLE_DISABLE, updateListTimeFlag);
        }
    }
}
