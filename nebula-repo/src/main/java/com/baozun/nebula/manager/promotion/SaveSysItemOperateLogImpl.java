package com.baozun.nebula.manager.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.ItemAllInfoCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.system.SysItemOperateLogDao;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemProperties;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.model.system.SysItemOperateLog;
import com.baozun.nebula.sdk.manager.SdkItemCategoryManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.feilong.core.util.CollectionsUtil;
import com.google.gson.Gson;

@Service("saveSysItemOperateLog")
public class SaveSysItemOperateLogImpl implements SaveSysItemOperateLog{

    @Autowired
    private SdkItemManager              sdkItemManager;

    @Autowired
    private SdkItemCategoryManager      sdkItemCategoryManager;

    @Autowired
    private SysItemOperateLogDao        sysItemOperateLogDao;
    
    @Autowired
    private SdkMataInfoManager          sdkMataInfoManager;
    
    @Autowired
    private ItemDao                     itemDao;

    /** 自动下架状态 */
    private static final Integer LIFECYCLE_OFF_SHELVES_AUTO = 4;
    
    /** 下架状态 */
    private static final Integer LIFECYCLE_OFF_SHELVES_MANUALLY = 0;
    
    @Override
    public void SaveSysItemOperateLog(Long itemId,Long userId,Long type){
        //判断商品当前状态是否为自动下架，如果符合条件则记录日志并将状态刷为下架状态
        ItemCommand itemCommand = sdkItemManager.findItemCommandById(itemId);
        if (LIFECYCLE_OFF_SHELVES_AUTO.equals(itemCommand.getLifecycle())){
            List<Long> itemIds = new ArrayList<Long>();
            itemIds.add(itemId);
            //动态属性
            List<ItemProperties> itemProperties = sdkItemManager.findItemPropertiesByItemId(itemId);
            //获取sku集合
            List<Sku> skuList = sdkItemManager.findSkuByItemId(itemId);
            List<String> extentionCodeList = CollectionsUtil.getPropertyValueList(skuList, "outid");
            //sku库存信息
            List<SkuInventory> skuInventory = sdkItemManager.findSkuInventoryByExtentionCodes(extentionCodeList);
            //图片信息
            List<ItemImage> itemImage = sdkItemManager.findItemImageByItemIds(itemIds);
            //商品分类
            List<ItemCategory> itemCategory = sdkItemCategoryManager.findItemCategoryListByItemId(itemId);

            ItemAllInfoCommand itemAllInfoCommand = new ItemAllInfoCommand();

            itemAllInfoCommand = (ItemAllInfoCommand) ConvertUtils.convertFromTarget(itemAllInfoCommand, itemCommand);

            itemAllInfoCommand.setItemProperties(itemProperties);
            itemAllInfoCommand.setSku(skuList);
            itemAllInfoCommand.setSkuInventory(skuInventory);
            itemAllInfoCommand.setItemImage(itemImage);
            itemAllInfoCommand.setItemCategory(itemCategory);

            Gson gson = new Gson();
            String itemAllInfoCommandJson = gson.toJson(itemAllInfoCommand);

            SysItemOperateLog SysItemOperateLog = new SysItemOperateLog();
            SysItemOperateLog.setContext(itemAllInfoCommandJson);
            SysItemOperateLog.setItemId(itemId);
            //如果是wormhole同步商品信息的操作我觉得optId可以记为-1，或者一个很大的数
            SysItemOperateLog.setOptId(userId);
            /** 1.单个操作，2.系统推送，3.导入 */
            SysItemOperateLog.setOptType(type);
            SysItemOperateLog.setCreateTime(new Date());
            sysItemOperateLogDao.save(SysItemOperateLog);
            //更新自动下架状态为下架
            String updateListTimeFlag = sdkMataInfoManager.findValue(MataInfo.UPDATE_ITEM_LISTTIME);
            itemDao.enableOrDisableItemByIds(itemIds, LIFECYCLE_OFF_SHELVES_MANUALLY, updateListTimeFlag);
        }
    }
}
