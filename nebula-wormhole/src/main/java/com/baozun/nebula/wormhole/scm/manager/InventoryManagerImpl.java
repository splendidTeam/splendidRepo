/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
 *
 */
package com.baozun.nebula.wormhole.scm.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.model.product.SkuInventoryChangeLog;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryChangeLogManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.wormhole.mq.entity.SkuInventoryV5;
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;
import com.baozun.nebula.wormhole.scm.handler.SyncInventoryHandler;
import com.baozun.nebula.wormhole.scm.timing.SyncSalesOrderManager;

/**
 * @author yfxie
 *
 */
@Service("inventoryManager")
public class InventoryManagerImpl implements InventoryManager{

    private final Logger                    logger      = LoggerFactory.getLogger(InventoryManagerImpl.class);

    @Autowired
    private SdkSkuInventoryDao              sdkSkuInventoryDao;

    @Autowired
    private SdkMsgManager                   sdkMsgManager;

    @Autowired
    private OrderManager                    sdkOrderService;

    @Autowired
    private SyncSalesOrderManager           syncSalesOrderManager;

    @Autowired
    private SdkSkuManager                   sdkSkuManager;

    @Autowired
    private SdkSkuInventoryChangeLogManager skuInventoryChangeLogManager;

    @Autowired(required = false)
    private SyncInventoryHandler            syncInventoryHandler;

    private final Integer                   STORAGE_IN  = 1;

    private final Integer                   STORAGE_OUT = 2;

    /**
     * 逐个信息处理全量同步记录
     * 
     * @param siList
     */
    @Override
    @Transactional
    public void syncFullInventory(List<SkuInventoryV5> siList,List<Long> msgIdList){
        if (!Validator.isNotNullOrEmpty(siList)){
            return;
        }
        List<String> extentionCodeList = new ArrayList<String>();
        for (SkuInventoryV5 skuInv : siList){
            extentionCodeList.add(skuInv.getExtentionCode());
        }
        /** 获取extensionCode和库存的map信息 */
        Map<String, SkuInventory> skuInvMap = getSkuInventoryMap(extentionCodeList);
        /** 获取订单行中未被oms确认的订单行信息 */
        Map<String, Integer> skuNumInOrder = getSkuNumOfOrderStatusNotChanged(siList);
        Map<String, Sku> skuMap = getSkuMap(extentionCodeList);
        /** 逐个处理sku信息 */
        for (SkuInventoryV5 skuInvV5 : siList){
            if (Validator.isNotNullOrEmpty(skuInvV5.getExtentionCode()) && null != skuMap.get(skuInvV5.getExtentionCode())){
                handleSkuFullInv(skuInvV5, skuInvMap.get(skuInvV5.getExtentionCode()), skuNumInOrder.get(skuInvV5.getExtentionCode()));
            }
        }
        if (null != syncInventoryHandler){
            syncInventoryHandler.syncFullInventory(siList);
        }
        /** 设置MsgReceiveContent库存同步记录标志 */
        sdkMsgManager.updateMsgRecContIsProByIds(msgIdList);
        logger.info("全量同步记录完成");
    }

    /**
     * 根据同步记录获取sku信息
     * 
     * @param siList
     * @return
     */
    private Map<String, Sku> getSkuMap(List<String> extentionCodes){
        Map<String, Sku> retMap = new HashMap<String, Sku>();
        if (Validator.isNotNullOrEmpty(extentionCodes)){
            List<Sku> skuList = sdkSkuManager.findSkuByOutIds(extentionCodes);
            if (Validator.isNotNullOrEmpty(skuList)){
                for (Sku sku : skuList){
                    retMap.put(sku.getOutid(), sku);
                }
            }
        }
        return retMap;
    }

    @Override
    @Transactional
    public void syncIncrementInventory(List<SkuInventoryV5> siList,List<Long> msgIdList){
        Map<String, List<SkuInventoryV5>> skuInv5Map = new HashMap<String, List<SkuInventoryV5>>();
        /** 将同步sku信息 根据extentionCode分组 */
        generateSkuInvMapByExtentionCode(skuInv5Map, siList);
        Map<String, Sku> skuMap = getSkuMapInIncInv(siList);
        if (Validator.isNotNullOrEmpty(skuInv5Map)){
            /** 根据extensionCode逐条处理 */
            for (java.util.Map.Entry<String, List<SkuInventoryV5>> msgContent : skuInv5Map.entrySet()){
                SkuInventoryV5 invV5 = sumIncSkuV5(msgContent.getKey(), msgContent.getValue());
                if (null != invV5 && null != skuMap.get(invV5.getExtentionCode())){//判断sku是否存在
                    syncIncrementInventory(invV5);
                    if (null != syncInventoryHandler){
                        syncInventoryHandler.syncIncrementInventory(invV5);
                    }
                }
            }
        }
        sdkMsgManager.updateMsgRecContIsProByIds(msgIdList);
        logger.info("增量同步记录完成");
    }

    /**
     * 获取增量同步的sku map
     * 
     * @param siList
     * @return
     */
    private Map<String, Sku> getSkuMapInIncInv(List<SkuInventoryV5> siList){
        Map<String, Sku> skuMap = new HashMap<String, Sku>();
        if (Validator.isNotNullOrEmpty(siList)){
            List<String> extentionCodeList = new ArrayList<String>();
            for (SkuInventoryV5 skuV5 : siList){
                extentionCodeList.add(skuV5.getExtentionCode());
            }
            return getSkuMap(extentionCodeList);
        }
        return skuMap;
    }

    /**
     * 将增量同步记录进行合并
     * 
     * @param skuInvList
     * @return
     */
    private SkuInventoryV5 sumIncSkuV5(String extensionCode,List<SkuInventoryV5> skuInvList){
        if (!Validator.isNotNullOrEmpty(extensionCode) || !Validator.isNotNullOrEmpty(skuInvList)){
            return null;
        }
        SkuInventory skuInv = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(extensionCode);
        SkuInventoryV5 skuInvV5Sum = new SkuInventoryV5();
        Date lastSyncTime = null;
        int totalNum = 0;
        if (null == skuInv){
            /** 当基线时间在 日志更新时间之前 可以更新 */
            for (SkuInventoryV5 skuInvV5 : skuInvList){
                if (null == lastSyncTime){
                    lastSyncTime = skuInvV5.getOpTime();
                }
                //设置当前库存=当前库存+变化量
                if (STORAGE_IN.equals(skuInvV5.getDirection())){ //入库
                    totalNum = totalNum + Math.abs(Integer.parseInt(skuInvV5.getQty() + ""));
                }else if (STORAGE_OUT.equals(skuInvV5.getDirection())){ //出库
                    totalNum = totalNum - Math.abs(Integer.parseInt(skuInvV5.getQty() + ""));
                }
                if (null == lastSyncTime || skuInvV5.getOpTime().after(lastSyncTime)){
                    lastSyncTime = skuInvV5.getOpTime();
                }
            }
        }else{
            lastSyncTime = skuInv.getLastSyncTime();
            /** 当基线时间在 日志更新时间之前 可以更新 */
            for (SkuInventoryV5 skuInvV5 : skuInvList){
                if (null == lastSyncTime){
                    lastSyncTime = skuInvV5.getOpTime();
                }
                if (null == skuInv.getBaselineTime() || skuInv.getBaselineTime().before(skuInvV5.getOpTime())){
                    //设置当前库存=当前库存+变化量
                    if (STORAGE_IN.equals(skuInvV5.getDirection())){ //入库
                        totalNum = totalNum + Math.abs(Integer.parseInt(skuInvV5.getQty() + ""));
                    }else if (STORAGE_OUT.equals(skuInvV5.getDirection())){ //出库
                        totalNum = totalNum - Math.abs(Integer.parseInt(skuInvV5.getQty() + ""));
                    }
                    if (null == lastSyncTime || skuInvV5.getOpTime().after(lastSyncTime)){
                        lastSyncTime = skuInvV5.getOpTime();
                    }
                }
            }
        }
        skuInvV5Sum.setExtentionCode(extensionCode);
        if (totalNum > 0){
            skuInvV5Sum.setDirection(STORAGE_IN);
        }else{
            skuInvV5Sum.setDirection(STORAGE_OUT);
        }
        skuInvV5Sum.setQty(Long.parseLong("" + Math.abs(totalNum)));
        skuInvV5Sum.setType(2);
        skuInvV5Sum.setOpTime(lastSyncTime);
        return skuInvV5Sum;
    }

    /**
     * 把extensionCode和sku的同步记录组成
     * 
     * @param retMap
     * @param skuInvSpList
     */
    private void generateSkuInvMapByExtentionCode(Map<String, List<SkuInventoryV5>> retMap,List<SkuInventoryV5> skuInvSpList){
        if (Validator.isNotNullOrEmpty(skuInvSpList)){
            if (null == retMap){
                retMap = new HashMap<String, List<SkuInventoryV5>>();
            }
            for (SkuInventoryV5 invV5 : skuInvSpList){
                List<SkuInventoryV5> v5List = null;
                String extensionCode = invV5.getExtentionCode();
                if (retMap.containsKey(extensionCode)){
                    v5List = retMap.get(extensionCode);
                }else{
                    v5List = new ArrayList<SkuInventoryV5>();
                }
                v5List.add(invV5);
                retMap.put(extensionCode, v5List);
            }
        }
    }

    /**
     * 逐个信息处理增量同步记录
     * 
     * @param siList
     */
    public void syncIncrementInventory(SkuInventoryV5 skuInvV5){
        if (null == skuInvV5){
            return;
        }
        SkuInventory skuInv = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(skuInvV5.getExtentionCode());
        Integer logType = SkuInventoryChangeLog.TYPE_INCREASE;
        if (null == skuInv){
            //如果现在库存还不存在的时候
            skuInv = new SkuInventory();
            int availableQty = 0;
            int qty = Integer.parseInt(skuInvV5.getQty() + "");
            if (STORAGE_IN.equals(skuInvV5.getDirection())){
                availableQty = availableQty + Math.abs(qty);
            }else if (STORAGE_OUT.equals(skuInvV5.getDirection())){
                availableQty = availableQty - Math.abs(qty);
                logType = SkuInventoryChangeLog.TYPE_REDUCE;
            }
            skuInv.setAvailableQty(availableQty);
            skuInv.setLastSyncTime(skuInvV5.getOpTime());
            skuInv.setExtentionCode(skuInvV5.getExtentionCode());
            sdkSkuInventoryDao.save(skuInv);
            //保存变更日志
            saveChangeLogBySkuInventoryV5(skuInvV5, logType);
        }else{
            /** 当基线时间在 日志更新时间之前 可以更新 */
            if (null == skuInv.getBaselineTime() || skuInv.getBaselineTime().before(skuInvV5.getOpTime())){
                //设置当前库存=当前库存+变化量
                //int availableQty = skuInv.getAvailableQty();
                int availableQty = 0;
                int qty = Integer.parseInt(skuInvV5.getQty() + "");
                if (STORAGE_IN.equals(skuInvV5.getDirection())){
                    availableQty = availableQty + Math.abs(qty);
                }else if (STORAGE_OUT.equals(skuInvV5.getDirection())){
                    availableQty = availableQty - Math.abs(qty);
                    logType = SkuInventoryChangeLog.TYPE_REDUCE;
                }
                //skuInv.setAvailableQty(availableQty);
                if (null == skuInv.getLastSyncTime() || skuInv.getLastSyncTime().before(skuInvV5.getOpTime())){
                    skuInv.setLastSyncTime(skuInvV5.getOpTime());
                }
                //sdkSkuInventoryDao.addSkuInventory(extentionCode, count);
                sdkSkuInventoryDao.addSkuInventoryById(skuInv.getId(), availableQty, skuInv.getLastSyncTime());
                //保存变更日志
                saveChangeLogBySkuInventoryV5(skuInvV5, logType);
            }
        }
    }

    /**
     * 获取库存和extentionCode的map
     * 
     * @param extentionCodeList
     * @return
     */
    private Map<String, SkuInventory> getSkuInventoryMap(List<String> extentionCodeList){
        Map<String, SkuInventory> skuInvMap = new HashMap<String, SkuInventory>();
        if (Validator.isNotNullOrEmpty(extentionCodeList)){
            List<SkuInventory> skuInvList = sdkSkuInventoryDao.findSkuInventoryByExtentionCodes(extentionCodeList);
            if (Validator.isNotNullOrEmpty(skuInvList)){
                for (SkuInventory skuInv : skuInvList){
                    skuInvMap.put(skuInv.getExtentionCode(), skuInv);
                }
            }
        }
        return skuInvMap;
    }

    /**
     * 处理单个sku全量库存
     * 
     * @param skuInv
     */
    private void handleSkuFullInv(SkuInventoryV5 skuInvV5,SkuInventory skuInv,Integer hasUsedNum){
        if (null == skuInvV5){
            return;
        }
        //设置当前库存=库存数量-目前未被SCM确认接受的有效订单中的商品数量
        int availableQty = Integer.parseInt(skuInvV5.getQty() + "");
        if (null != hasUsedNum){
            availableQty = availableQty - hasUsedNum;
        }
        if (null == skuInv){
            //sku存在才会到这个方法里面 
            skuInv = new SkuInventory();
            skuInv.setExtentionCode(skuInvV5.getExtentionCode());
            skuInv.setAvailableQty(availableQty);
            skuInv.setBaselineTime(skuInvV5.getOpTime());
            sdkSkuInventoryDao.save(skuInv);
            //保存变更日志
            saveChangeLogBySkuInventoryV5(skuInvV5, SkuInventoryChangeLog.TYPE_FULL);
        }else{
            /** 当基线时间在 日志更新时间之前 可以更新 */
            if ((null == skuInv.getBaselineTime() || skuInv.getBaselineTime().before(skuInvV5.getOpTime()))){
                skuInv.setAvailableQty(availableQty);
                skuInv.setBaselineTime(skuInvV5.getOpTime());
                //skuInv.setVersion(nowDate);
                sdkSkuInventoryDao.updateInventoryById(
                                skuInv.getId(),
                                skuInv.getAvailableQty(),
                                skuInv.getLastSyncTime(),
                                skuInv.getBaselineTime());
                //保存变更日志
                saveChangeLogBySkuInventoryV5(skuInvV5, SkuInventoryChangeLog.TYPE_FULL);
            }
        }
    }

    /**
     * 获取已经被使用的库存 但是oms还未检测到的
     * 1. 订单已经形成 但是oms还未确认的
     * 2. 订单已经形成，但是在库存同步之后的记录
     * 
     * @param siList
     * @return
     */
    private Map<String, Integer> getSkuNumOfOrderStatusNotChanged(List<SkuInventoryV5> siList){
        Map<String, Integer> skuInvNotUsedMap = new HashMap<String, Integer>();
        if (Validator.isNotNullOrEmpty(siList)){
            List<String> extentionList = new ArrayList<String>();
            for (SkuInventoryV5 invV5 : siList){
                extentionList.add(invV5.getExtentionCode());
            }
            /** 1. 库存同步之后的订单 */
            /*
             * 次数获取的时间为同一条同步记录中 opTime最小的一个时间，是为了尽量减少因为时间差而导致的超买
             * getNotCalInvNum此方法会进一步判断
             */
            List<SalesOrderCommand> orderCommList = sdkOrderService
                            .findOrderByExntentionListAndOrderCreateTime(extentionList, getMinOpTime(siList));
            /** 2. 之前未被oms确认的订单 */
            //订单状态为新建和库存没有在oms确认的订单
            List<Integer> orderStatus = new ArrayList<Integer>();
            orderStatus.add(SalesOrder.SALES_ORDER_STATUS_NEW);
            //orderStatus.add(SalesOrder.SALES_ORDER_STATUS_TOOMS);
            List<SalesOrderCommand> orderCommand2 = sdkOrderService.findOrderByExntentionListAndOrderStatus(extentionList, orderStatus);
            /** 3. 获取订单中对应的extensionCode的数量 */
            //订单已经去重
            Map<String, List<SalesOrderCommand>> orderMap = genOrderCommandMap(orderCommList, orderCommand2);
            for (SkuInventoryV5 invV5 : siList){
                Integer num = getNotCalInvNum(invV5, orderMap.get(invV5.getExtentionCode()));
                skuInvNotUsedMap.put(invV5.getExtentionCode(), num);
            }
        }
        return skuInvNotUsedMap;
    }

    /**
     * 获取当前时间之前最小的opTime时间
     * 
     * @return
     */
    private Date getMinOpTime(List<SkuInventoryV5> siList){
        Date minTime = new Date();
        if (Validator.isNotNullOrEmpty(siList)){
            for (SkuInventoryV5 v5 : siList){
                if (v5.getOpTime().before(minTime)){
                    minTime = v5.getOpTime();
                }
            }
        }
        return minTime;
    }

    /**
     * 获取oms没有计算的库存数量
     * 
     * @param invV5
     * @return
     */
    private Integer getNotCalInvNum(SkuInventoryV5 invV5,List<SalesOrderCommand> orderList){
        int num = 0;
        if (Validator.isNotNullOrEmpty(orderList)){
            Map<String, OrderStatusV5> orderStatusV5Map = new HashMap<String, OrderStatusV5>();
            List<OrderStatusV5> orderStatusV5 = syncSalesOrderManager.getNotHandledSoOrder();
            if (Validator.isNotNullOrEmpty(orderStatusV5)){
                for (OrderStatusV5 osV5 : orderStatusV5){
                    orderStatusV5Map.put(osV5.getBsOrderCode(), osV5);
                }
            }
            for (SalesOrderCommand orderCommand : orderList){
                // 1: 当订单创建时间在同步之后  直接减
                if (invV5.getOpTime().before(orderCommand.getCreateTime())){
                    num = num + caculateNum(orderCommand.getOrderLines(), invV5.getExtentionCode());
                }else{
                    // 2：当订单创建时间再同步之前  订单没有被oms确认 则减
                    String oCode = orderCommand.getCode();
                    Integer logisticsStatus = orderCommand.getLogisticsStatus();
                    OrderStatusV5 orderStatusV52 = orderStatusV5Map.get(oCode);
                    if ((Objects.equals(SalesOrder.SALES_ORDER_STATUS_NEW, logisticsStatus)
                                    || Objects.equals(SalesOrder.SALES_ORDER_STATUS_TOOMS, logisticsStatus))
                                    && (null == orderStatusV52 || orderStatusV52.getOpTime().after(invV5.getOpTime()))){
                        num = num + caculateNum(orderCommand.getOrderLines(), invV5.getExtentionCode());
                    }
                }
            }
        }
        return num;
    }

    /**
     * 计算商品数量
     * 
     * @param orderLines
     * @return
     */
    private Integer caculateNum(List<OrderLineCommand> orderLines,String extentionCode){
        int calNum = 0;
        if (Validator.isNotNullOrEmpty(orderLines)){
            for (OrderLineCommand line : orderLines){
                if (null != line.getExtentionCode() && line.getExtentionCode().equals(extentionCode)){
                    calNum = calNum + line.getCount();
                }
            }
        }
        return calNum;
    }

    /**
     * 获取extendCode和订单的对应关系
     * 
     * @param orderCommList
     * @param orderCommand2
     * @return
     */
    private Map<String, List<SalesOrderCommand>> genOrderCommandMap(
                    List<SalesOrderCommand> orderCommList,
                    List<SalesOrderCommand> orderCommand2){
        List<SalesOrderCommand> orderHandleList = new ArrayList<SalesOrderCommand>();
        if (Validator.isNotNullOrEmpty(orderCommList)){
            orderHandleList.addAll(orderCommList);
        }
        if (Validator.isNotNullOrEmpty(orderCommand2)){
            orderHandleList.addAll(orderCommand2);
        }
        //订单id列表
        List<Long> orderIdList = new ArrayList<Long>();
        //订单map
        Map<Long, SalesOrderCommand> orderCommandMap = new HashMap<Long, SalesOrderCommand>();
        //订单id和订单行对应关系 一方面方便关联调用，另一方面顺便去重
        Map<Long, List<OrderLineCommand>> orderLineCommandMap = new HashMap<Long, List<OrderLineCommand>>();
        //extentioncode和订单id列表对应关系，value用set是为了防止订单重复
        Map<String, Set<Long>> extentionOrderMap = new HashMap<String, Set<Long>>();
        if (Validator.isNotNullOrEmpty(orderHandleList)){
            for (SalesOrderCommand orderCommand : orderHandleList){
                orderIdList.add(orderCommand.getId());
                orderCommandMap.put(orderCommand.getId(), orderCommand);
            }
            List<OrderLineCommand> oLines = sdkOrderService.findOrderDetailListByOrderIds(orderIdList);
            for (OrderLineCommand line : oLines){
                Long orderId = line.getOrderId();
                String entenCode = line.getExtentionCode();
                //订单行列表
                List<OrderLineCommand> oListIn = null;
                //订单id列表
                Set<Long> orderIdSet = null;
                if (orderLineCommandMap.containsKey(orderId)){
                    oListIn = orderLineCommandMap.get(orderId);
                }else{
                    oListIn = new ArrayList<OrderLineCommand>();
                }
                oListIn.add(line);
                /** 映射订单和订单行列表 */
                orderLineCommandMap.put(orderId, oListIn);
                if (extentionOrderMap.containsKey(entenCode)){
                    orderIdSet = extentionOrderMap.get(entenCode);
                }else{
                    orderIdSet = new HashSet<Long>();
                }
                orderIdSet.add(line.getOrderId());
                /** 映射extentionCode和订单id列表 */
                if (null != entenCode){
                    extentionOrderMap.put(entenCode, orderIdSet);
                }
            }
        }
        return genOrderCommandMap(extentionOrderMap, orderLineCommandMap, orderCommandMap);
    }

    /**
     * 获取extendCode和订单的对应关系
     * 
     * @param extentionOrderMap
     * @param orderLineCommandMap
     * @param orderCommandMap
     * @return
     */
    private Map<String, List<SalesOrderCommand>> genOrderCommandMap(
                    Map<String, Set<Long>> extentionOrderMap,
                    Map<Long, List<OrderLineCommand>> orderLineCommandMap,
                    Map<Long, SalesOrderCommand> orderCommandMap){
        Map<String, List<SalesOrderCommand>> retMap = new HashMap<String, List<SalesOrderCommand>>();
        if (Validator.isNotNullOrEmpty(extentionOrderMap)){
            for (java.util.Map.Entry<String, Set<Long>> extenMap : extentionOrderMap.entrySet()){
                List<SalesOrderCommand> orderComandList = new ArrayList<SalesOrderCommand>();
                String extentionCode = extenMap.getKey();
                Set<Long> orderIds = extenMap.getValue();
                if (Validator.isNotNullOrEmpty(orderIds)){
                    for (Long oId : orderIds){
                        SalesOrderCommand command = orderCommandMap.get(oId);
                        command.setOrderLines(orderLineCommandMap.get(oId));
                        orderComandList.add(command);
                    }
                }
                retMap.put(extentionCode, orderComandList);
            }
        }
        return retMap;
    }

    private void saveChangeLogBySkuInventoryV5(SkuInventoryV5 skuInventory,Integer type){
        SkuInventoryChangeLog log = new SkuInventoryChangeLog();
        log.setCreateTime(new Date());
        log.setExtentionCode(skuInventory.getExtentionCode());
        log.setQty(Integer.valueOf(skuInventory.getQty().toString()));
        log.setSource(SkuInventoryChangeLog.SOURCE_OMS);
        log.setType(type);
        try{
            skuInventoryChangeLogManager.saveSkuInventoryChangeLog(log);
        }catch (Exception e){
            logger.warn("sync oms inventory error : unable to save inventory change log.");
        }
    }

}
