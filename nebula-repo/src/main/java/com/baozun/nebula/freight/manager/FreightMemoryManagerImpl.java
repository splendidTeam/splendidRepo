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
package com.baozun.nebula.freight.manager;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.freight.DistributionModeDao;
import com.baozun.nebula.dao.freight.ShippingFeeConfigDao;
import com.baozun.nebula.dao.freight.ShippingTemeplateDao;
import com.baozun.nebula.dao.freight.SupportedAreaDao;
import com.baozun.nebula.freight.command.ShippingFeeConfigCommand;
import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.freight.memory.DistributionCommand;
import com.baozun.nebula.freight.memory.ShippingFeeConfigMap;
import com.baozun.nebula.freight.memory.ShippingTemeplateMap;
import com.baozun.nebula.freight.memory.ShopShippingTemeplateCommand;
import com.baozun.nebula.freight.memory.ShopShippingTemeplateMap;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.freight.ShippingFeeConfig;
import com.baozun.nebula.model.freight.ShippingTemeplate;
import com.baozun.nebula.model.freight.SupportedArea;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.feilong.core.Validator;

/**
 * The Class FreightMemoryManagerImpl.
 *
 * @author Tianlong.Zhang
 */
@Transactional(readOnly = true)
@Service("freigthMemoryManager")
public class FreightMemoryManagerImpl implements FreightMemoryManager{

    /** The distribution list. */
    private List<DistributionCommand> distributionList;

    /** The shipping temeplate map. */
    private ShippingTemeplateMap shippingTemeplateMap;

    /** The shop shipping temeplate map. */
    private ShopShippingTemeplateMap shopShippingTemeplateMap;

    /** The shipping fee config map. */
    private ShippingFeeConfigMap shippingFeeConfigMap;

    /** The supported area dao. */
    @Autowired
    private SupportedAreaDao supportedAreaDao;

    /** The distribution mode dao. */
    @Autowired
    private DistributionModeDao distributionModeDao;

    /** The shipping temeplate dao. */
    @Autowired
    private ShippingTemeplateDao shippingTemeplateDao;

    /** The shipping fee config dao. */
    @Autowired
    private ShippingFeeConfigDao shippingFeeConfigDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.freight.manager.FreightMemoryManager#loadFreightInfosFromDB()
     */
    @Override
    public void loadFreightInfosFromDB(){
        //从数据库中加载物流列表.
        buildDistributionCommandList();

        //从数据库中加载运费配置信息Map.
        loadShippingFeeConfigMap();

        //从数据库中加载运费模板 map.
        loadShippingTemeplateMap();

        //从数据库中加载 店铺运费模板map.
        loadShopShippingTemeplateMap();
    }

    /**
     * 从数据库中加载物流列表.
     */
    private void buildDistributionCommandList(){
        //获取所有的物流方式
        List<DistributionMode> distributionModeList = distributionModeDao.getAllDistributionMode();

        if (isNullOrEmpty(distributionModeList)){
            distributionList = null;
            return;
        }
        List<DistributionCommand> distributionCommandList = new ArrayList<>();

        List<SupportedArea> supporedAreaList = supportedAreaDao.getAllSuppoertedArea();
        for (DistributionMode distributionMode : distributionModeList){//遍历所有的物流方式
            DistributionCommand distributionCommand = buildDistributionCommand(supporedAreaList, distributionMode);
            distributionCommandList.add(distributionCommand);
        }
        distributionList = distributionCommandList;
    }

    /**
     * @param supporedAreaList
     * @param distributionMode
     * @return
     * @since 5.3.2.4
     */
    private DistributionCommand buildDistributionCommand(List<SupportedArea> supporedAreaList,DistributionMode distributionMode){
        Long distributionModeId = distributionMode.getId();

        List<SupportedAreaCommand> whiteAreaList = new ArrayList<>();
        Map<Long, List<SupportedAreaCommand>> blackAreaListMap = new HashMap<>();
        for (SupportedArea supportedArea : supporedAreaList){// 便利所有的 支持区域。
            SupportedAreaCommand areaCmd = new SupportedAreaCommand();
            areaCmd = (SupportedAreaCommand) ConvertUtils.convertFromTarget(areaCmd, supportedArea);
            if (supportedArea.getDistributionModeId().equals(distributionModeId)){// 根据支持区域的 物流方式Id 进行匹配
                if (SupportedArea.WHITE_TYPE.equals(supportedArea.getType())){ // 白名单
                    whiteAreaList.add(areaCmd);
                }

                if (SupportedArea.BLACK_TYPE.equals(supportedArea.getType())){//黑名单
                    Long groupNo = supportedArea.getGroupNo();
                    List<SupportedAreaCommand> blackAreaCmdList = blackAreaListMap.get(groupNo);
                    if (blackAreaCmdList != null){// 如果 期号 已经在map中存在，那么修改 blackAreaCmdList 内容
                        if (SupportedArea.BLACK_TYPE.equals(supportedArea.getType())){
                            blackAreaCmdList.add(areaCmd);
                        }
                    }else{// 如果不存在， 创建 blackAreaCmdList ，将area放入 list 之后，再将list put 入map中
                        blackAreaCmdList = new ArrayList<>();

                        if (SupportedArea.BLACK_TYPE.equals(supportedArea.getType())){
                            blackAreaCmdList.add(areaCmd);
                        }
                        blackAreaListMap.put(groupNo, blackAreaCmdList);
                    }
                }
            }
        }

        DistributionCommand distributionCommand = new DistributionCommand();
        distributionCommand.setDistributionModeId(distributionModeId);
        distributionCommand.setDistributionModeName(distributionMode.getName());
        distributionCommand.setWhiteAreaList(whiteAreaList);
        distributionCommand.setBlackAreaListMap(blackAreaListMap);
        return distributionCommand;
    }

    /**
     * 从数据库中加载运费模板 map.
     */
    private void loadShippingTemeplateMap(){
        List<ShippingTemeplate> tmpList = shippingTemeplateDao.findAllShippingTemeplate();
        if (Validator.isNotNullOrEmpty(tmpList)){
            ShippingTemeplateMap shippingTemeplateMap1 = new ShippingTemeplateMap();

            for (ShippingTemeplate tmp : tmpList){
                ShippingTemeplateCommand cmd = new ShippingTemeplateCommand();
                cmd = (ShippingTemeplateCommand) ConvertUtils.convertFromTarget(cmd, tmp);

                Long templateId = tmp.getId();
                List<ShippingFeeConfigCommand> cfgList = shippingFeeConfigDao.findShippingFeeConfigsByTemeplateId(templateId);

                cmd.setFeeConfigs(cfgList);

                setDistributionModeCommandList(cmd);

                shippingTemeplateMap1.put(tmp.getId(), cmd);
            }

            this.shippingTemeplateMap = shippingTemeplateMap1;
        }

    }

    /**
     * 从数据库中加载 店铺运费模板map.
     */
    private void loadShopShippingTemeplateMap(){
        List<ShippingTemeplate> tmpList = shippingTemeplateDao.findAllShippingTemeplate();

        if (Validator.isNotNullOrEmpty(tmpList)){
            ShopShippingTemeplateMap shopShippingTemeplateMap1 = new ShopShippingTemeplateMap();

            for (ShippingTemeplate tmp : tmpList){
                Long shopId = tmp.getShopId();
                Long templateId = tmp.getId();
                List<ShippingFeeConfigCommand> cfgList = shippingFeeConfigDao.findShippingFeeConfigsByTemeplateId(templateId);

                ShippingTemeplateCommand cmd = new ShippingTemeplateCommand();
                cmd = (ShippingTemeplateCommand) ConvertUtils.convertFromTarget(cmd, tmp);
                cmd.setFeeConfigs(cfgList);
                setDistributionModeCommandList(cmd);

                ShopShippingTemeplateCommand sstCmd = shopShippingTemeplateMap1.get(shopId);
                if (null != sstCmd){
                    List<ShippingTemeplateCommand> cmdList = sstCmd.getShippingTemeplateList();

                    if (cmdList != null){
                        cmdList.add(cmd);
                    }else{
                        cmdList = new ArrayList<ShippingTemeplateCommand>();
                        cmdList.add(cmd);

                        sstCmd.setShippingTemeplateList(cmdList);
                        shopShippingTemeplateMap1.put(shopId, sstCmd);
                    }
                }else{
                    sstCmd = new ShopShippingTemeplateCommand();
                    List<ShippingTemeplateCommand> cmdList = new ArrayList<ShippingTemeplateCommand>();
                    cmdList.add(cmd);

                    sstCmd.setShippingTemeplateList(cmdList);
                    shopShippingTemeplateMap1.put(shopId, sstCmd);
                }

            }

            // 设置 每个 ShopShippingTemeplateCommand 的 distrubutionModeList 
            List<DistributionCommand> distributionCommandList = getDistributionList();

            for (Long shopId : shopShippingTemeplateMap1.keySet()){
                ShopShippingTemeplateCommand sstCmd = shopShippingTemeplateMap1.get(shopId);

                List<ShippingTemeplateCommand> temeplateCmdList = sstCmd.getShippingTemeplateList();

                if (null != temeplateCmdList){
                    Set<Long> distributionModeIdSet = new HashSet<Long>();

                    for (ShippingTemeplateCommand stCmd : temeplateCmdList){

                        // 获取单个店铺下 每个模板的 物流方式Id
                        List<DistributionModeCommand> modeList = stCmd.getDistributionModes();

                        if (Validator.isNotNullOrEmpty(modeList)){
                            for (DistributionModeCommand dmCmd : modeList){
                                Long distributionModeId = dmCmd.getId();
                                distributionModeIdSet.add(distributionModeId);
                            }

                        }
                    }

                    // 根据 物流方式ID 找出 支持本店铺的 DistributionModeCommand
                    List<DistributionModeCommand> curShopDistributionModeCommandList = null;

                    if (Validator.isNotNullOrEmpty(distributionModeIdSet)){

                        curShopDistributionModeCommandList = new ArrayList<DistributionModeCommand>();
                        for (Long modeId : distributionModeIdSet){
                            for (DistributionCommand distributionCmd : distributionCommandList){
                                if (modeId.equals(distributionCmd.getDistributionModeId())){
                                    DistributionModeCommand dmCmd = new DistributionModeCommand();
                                    dmCmd.setId(distributionCmd.getDistributionModeId());
                                    dmCmd.setName(distributionCmd.getDistributionModeName());
                                    curShopDistributionModeCommandList.add(dmCmd);
                                }
                            }
                        }

                    }

                    sstCmd.setDistributionModeList(curShopDistributionModeCommandList);
                }
            }

            this.shopShippingTemeplateMap = shopShippingTemeplateMap1;
        }
    }

    /**
     * Sets the distribution mode command list.
     *
     * @param stCmd
     *            the new distribution mode command list
     */
    private void setDistributionModeCommandList(ShippingTemeplateCommand stCmd){

        Set<Long> distributionModeIdSet = new HashSet<Long>();
        List<ShippingFeeConfigCommand> cfgList = stCmd.getFeeConfigs();
        List<DistributionCommand> distributionCommandList = getDistributionList();

        // 收集物流方式ID
        if (Validator.isNotNullOrEmpty(cfgList)){
            for (ShippingFeeConfigCommand cfgCmd : cfgList){
                Long distributionModeId = cfgCmd.getDistributionModeId();
                distributionModeIdSet.add(distributionModeId);
            }

            List<DistributionModeCommand> result = new ArrayList<DistributionModeCommand>();

            for (Long modeId : distributionModeIdSet){
                for (DistributionCommand distributionCmd : distributionCommandList){
                    if (modeId.equals(distributionCmd.getDistributionModeId())){
                        //						curShopDistributionCommandList.add(distributionCmd);
                        DistributionModeCommand dmCmd = new DistributionModeCommand();
                        dmCmd.setId(modeId);
                        dmCmd.setName(distributionCmd.getDistributionModeName());
                        result.add(dmCmd);
                    }
                }
            }

            stCmd.setDistributionModes(result);
        }

    }

    /**
     * 从数据库中加载运费配置信息Map.
     */
    public void loadShippingFeeConfigMap(){
        List<ShippingFeeConfig> configList = shippingFeeConfigDao.findAllShippingFeeConfig();
        ShippingFeeConfigMap map = new ShippingFeeConfigMap();
        if (Validator.isNotNullOrEmpty(configList)){

            for (ShippingFeeConfig cfg : configList){
                String areaId = cfg.getDestAreaId();
                Long distributionModeId = cfg.getDistributionModeId();
                Long spId = cfg.getShippingTemeplateId();

                //模板id-物流方式id-目的地id为key  

                StringBuilder sb = new StringBuilder();
                sb.append(spId).append(ShippingFeeConfigMap.KEY_CONNECTOR).append(distributionModeId).append(ShippingFeeConfigMap.KEY_CONNECTOR).append(areaId);

                ShippingFeeConfigCommand cmd = new ShippingFeeConfigCommand();
                cmd = (ShippingFeeConfigCommand) ConvertUtils.convertFromTarget(cmd, cfg);

                map.put(sb.toString(), cmd);
            }
        }
        this.shippingFeeConfigMap = map;
    }

    /**
     * Gets the distribution list.
     *
     * @return the cmdList
     */
    @Override
    public List<DistributionCommand> getDistributionList(){
        return distributionList;
    }

    /**
     * Gets the shipping temeplate map.
     *
     * @return the shippingTemeplateMap
     */
    @Override
    public ShippingTemeplateMap getShippingTemeplateMap(){
        return shippingTemeplateMap;
    }

    /**
     * Gets the shop shipping temeplate map.
     *
     * @return the shopShippingTemeplateMap
     */
    @Override
    public ShopShippingTemeplateMap getShopShippingTemeplateMap(){
        return shopShippingTemeplateMap;
    }

    /**
     * Gets the shipping fee config map.
     *
     * @return the shippingFeeConfigMap
     */
    @Override
    public ShippingFeeConfigMap getShippingFeeConfigMap(){
        return shippingFeeConfigMap;
    }

}
