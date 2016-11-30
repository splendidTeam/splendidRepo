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

import static com.baozun.nebula.freight.memory.ShippingFeeConfigMap.KEY_CONNECTOR;
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

    //*********************************************************************

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
        this.distributionList = buildAllDistributionCommandList();

        //从数据库中加载运费配置信息Map.
        this.shippingFeeConfigMap = buildShippingFeeConfigMap();

        //从数据库中加载运费模板 map.
        this.shippingTemeplateMap = buildShippingTemeplateMap();

        //从数据库中加载 店铺运费模板map.
        this.shopShippingTemeplateMap = buildShopShippingTemeplateMap();
    }

    //***********************************************************************************

    /**
     * @return
     * @since 5.3.2.4
     */
    private List<DistributionCommand> buildAllDistributionCommandList(){
        //获取所有的物流方式
        List<DistributionMode> distributionModeList = distributionModeDao.getAllDistributionMode();
        if (isNullOrEmpty(distributionModeList)){
            return null;
        }

        List<DistributionCommand> distributionCommandList = new ArrayList<>();

        List<SupportedArea> supporedAreaList = supportedAreaDao.getAllSuppoertedArea();
        for (DistributionMode distributionMode : distributionModeList){
            distributionCommandList.add(buildDistributionCommand(supporedAreaList, distributionMode));
        }
        return distributionCommandList;
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

        for (SupportedArea supportedArea : supporedAreaList){

            if (supportedArea.getDistributionModeId().equals(distributionModeId)){//根据支持区域的物流方式Id 进行匹配

                SupportedAreaCommand supportedAreaCommand = new SupportedAreaCommand();
                supportedAreaCommand = (SupportedAreaCommand) ConvertUtils.convertFromTarget(supportedAreaCommand, supportedArea);

                if (SupportedArea.WHITE_TYPE.equals(supportedArea.getType())){ // 白名单
                    whiteAreaList.add(supportedAreaCommand);
                }

                if (SupportedArea.BLACK_TYPE.equals(supportedArea.getType())){//黑名单
                    Long groupNo = supportedArea.getGroupNo();
                    List<SupportedAreaCommand> blackSupportedAreaCommandList = blackAreaListMap.get(groupNo);
                    if (blackSupportedAreaCommandList != null){// 如果 期号 已经在map中存在，那么修改 blackAreaCmdList 内容
                        if (SupportedArea.BLACK_TYPE.equals(supportedArea.getType())){
                            blackSupportedAreaCommandList.add(supportedAreaCommand);
                        }
                    }else{// 如果不存在， 创建 blackAreaCmdList ，将area放入 list 之后，再将list put 入map中
                        blackSupportedAreaCommandList = new ArrayList<>();

                        if (SupportedArea.BLACK_TYPE.equals(supportedArea.getType())){
                            blackSupportedAreaCommandList.add(supportedAreaCommand);
                        }
                        blackAreaListMap.put(groupNo, blackSupportedAreaCommandList);
                    }
                }
            }
        }

        //---------------------------------------------------------------------------

        DistributionCommand distributionCommand = new DistributionCommand();
        distributionCommand.setDistributionModeId(distributionModeId);
        distributionCommand.setDistributionModeName(distributionMode.getName());
        distributionCommand.setWhiteAreaList(whiteAreaList);
        distributionCommand.setBlackAreaListMap(blackAreaListMap);
        return distributionCommand;
    }

    /**
     * @return
     * @since 5.3.2.4
     */
    private ShippingTemeplateMap buildShippingTemeplateMap(){
        List<ShippingTemeplate> shippingTemeplateList = shippingTemeplateDao.findAllShippingTemeplate();

        ShippingTemeplateMap shippingTemeplateMap1 = null;
        if (isNotNullOrEmpty(shippingTemeplateList)){
            shippingTemeplateMap1 = new ShippingTemeplateMap();

            for (ShippingTemeplate shippingTemeplate : shippingTemeplateList){
                ShippingTemeplateCommand shippingTemeplateCommand = new ShippingTemeplateCommand();
                shippingTemeplateCommand = (ShippingTemeplateCommand) ConvertUtils.convertFromTarget(shippingTemeplateCommand, shippingTemeplate);

                Long templateId = shippingTemeplate.getId();
                List<ShippingFeeConfigCommand> shippingFeeConfigCommandList = shippingFeeConfigDao.findShippingFeeConfigsByTemeplateId(templateId);
                shippingTemeplateCommand.setFeeConfigs(shippingFeeConfigCommandList);

                setDistributionModeCommandList(shippingTemeplateCommand);

                shippingTemeplateMap1.put(shippingTemeplate.getId(), shippingTemeplateCommand);
            }
        }
        return shippingTemeplateMap1;
    }

    /**
     * @return
     * @since 5.3.2.4
     */
    private ShopShippingTemeplateMap buildShopShippingTemeplateMap(){
        List<ShippingTemeplate> shippingTemeplateList = shippingTemeplateDao.findAllShippingTemeplate();

        ShopShippingTemeplateMap shopShippingTemeplateMap1 = null;

        if (isNotNullOrEmpty(shippingTemeplateList)){
            shopShippingTemeplateMap1 = new ShopShippingTemeplateMap();

            for (ShippingTemeplate shippingTemeplate : shippingTemeplateList){
                Long shopId = shippingTemeplate.getShopId();
                Long templateId = shippingTemeplate.getId();
                List<ShippingFeeConfigCommand> shippingFeeConfigCommandList = shippingFeeConfigDao.findShippingFeeConfigsByTemeplateId(templateId);

                ShippingTemeplateCommand shippingTemeplateCommand = new ShippingTemeplateCommand();
                shippingTemeplateCommand = (ShippingTemeplateCommand) ConvertUtils.convertFromTarget(shippingTemeplateCommand, shippingTemeplate);
                shippingTemeplateCommand.setFeeConfigs(shippingFeeConfigCommandList);
                setDistributionModeCommandList(shippingTemeplateCommand);

                ShopShippingTemeplateCommand shopShippingTemeplateCommand = shopShippingTemeplateMap1.get(shopId);
                if (null != shopShippingTemeplateCommand){
                    List<ShippingTemeplateCommand> cmdList = shopShippingTemeplateCommand.getShippingTemeplateList();

                    if (cmdList != null){
                        cmdList.add(shippingTemeplateCommand);
                    }else{
                        cmdList = new ArrayList<ShippingTemeplateCommand>();
                        cmdList.add(shippingTemeplateCommand);

                        shopShippingTemeplateCommand.setShippingTemeplateList(cmdList);
                        shopShippingTemeplateMap1.put(shopId, shopShippingTemeplateCommand);
                    }
                }else{
                    shopShippingTemeplateCommand = new ShopShippingTemeplateCommand();

                    List<ShippingTemeplateCommand> shippingTemeplateCommandList = new ArrayList<>();
                    shippingTemeplateCommandList.add(shippingTemeplateCommand);

                    shopShippingTemeplateCommand.setShippingTemeplateList(shippingTemeplateCommandList);
                    shopShippingTemeplateMap1.put(shopId, shopShippingTemeplateCommand);
                }

            }

            // 设置 每个 ShopShippingTemeplateCommand 的 distrubutionModeList 
            List<DistributionCommand> distributionCommandList = getDistributionList();

            for (Long shopId : shopShippingTemeplateMap1.keySet()){
                ShopShippingTemeplateCommand shopShippingTemeplateCommand = shopShippingTemeplateMap1.get(shopId);

                List<ShippingTemeplateCommand> shippingTemeplateCommandList = shopShippingTemeplateCommand.getShippingTemeplateList();

                if (null != shippingTemeplateCommandList){
                    Set<Long> distributionModeIdSet = new HashSet<>();

                    for (ShippingTemeplateCommand shippingTemeplateCommand : shippingTemeplateCommandList){
                        // 获取单个店铺下 每个模板的 物流方式Id
                        List<DistributionModeCommand> distributionModeCommandList = shippingTemeplateCommand.getDistributionModes();

                        if (isNotNullOrEmpty(distributionModeCommandList)){
                            for (DistributionModeCommand distributionModeCommand : distributionModeCommandList){
                                Long distributionModeId = distributionModeCommand.getId();
                                distributionModeIdSet.add(distributionModeId);
                            }
                        }
                    }

                    // 根据 物流方式ID 找出支持本店铺的 DistributionModeCommand
                    List<DistributionModeCommand> curShopDistributionModeCommandList = null;

                    if (isNotNullOrEmpty(distributionModeIdSet)){

                        curShopDistributionModeCommandList = new ArrayList<>();
                        for (Long modeId : distributionModeIdSet){
                            for (DistributionCommand distributionCmd : distributionCommandList){
                                if (modeId.equals(distributionCmd.getDistributionModeId())){

                                    DistributionModeCommand distributionModeCommand = new DistributionModeCommand();
                                    distributionModeCommand.setId(distributionCmd.getDistributionModeId());
                                    distributionModeCommand.setName(distributionCmd.getDistributionModeName());
                                    curShopDistributionModeCommandList.add(distributionModeCommand);
                                }
                            }
                        }
                    }
                    shopShippingTemeplateCommand.setDistributionModeList(curShopDistributionModeCommandList);
                }
            }
        }
        return shopShippingTemeplateMap1;
    }

    /**
     * Sets the distribution mode command list.
     *
     * @param shippingTemeplateCommand
     *            the new distribution mode command list
     */
    private void setDistributionModeCommandList(ShippingTemeplateCommand shippingTemeplateCommand){
        Set<Long> distributionModeIdSet = new HashSet<>();
        List<ShippingFeeConfigCommand> shippingFeeConfigCommandList = shippingTemeplateCommand.getFeeConfigs();
        List<DistributionCommand> distributionCommandList = getDistributionList();

        // 收集物流方式ID
        if (isNotNullOrEmpty(shippingFeeConfigCommandList)){
            for (ShippingFeeConfigCommand cfgCmd : shippingFeeConfigCommandList){
                Long distributionModeId = cfgCmd.getDistributionModeId();
                distributionModeIdSet.add(distributionModeId);
            }

            List<DistributionModeCommand> distributionModeCommandList = new ArrayList<DistributionModeCommand>();

            for (Long modeId : distributionModeIdSet){
                for (DistributionCommand distributionCmd : distributionCommandList){
                    if (modeId.equals(distributionCmd.getDistributionModeId())){
                        DistributionModeCommand distributionModeCommand = new DistributionModeCommand();
                        distributionModeCommand.setId(modeId);
                        distributionModeCommand.setName(distributionCmd.getDistributionModeName());
                        distributionModeCommandList.add(distributionModeCommand);
                    }
                }
            }
            shippingTemeplateCommand.setDistributionModes(distributionModeCommandList);
        }
    }

    /**
     * @return
     * @since 5.3.2.4
     */
    private ShippingFeeConfigMap buildShippingFeeConfigMap(){
        List<ShippingFeeConfig> shippingFeeConfigList = shippingFeeConfigDao.findAllShippingFeeConfig();

        ShippingFeeConfigMap buildShippingFeeConfigMap = new ShippingFeeConfigMap();
        if (isNotNullOrEmpty(shippingFeeConfigList)){

            for (ShippingFeeConfig shippingFeeConfig : shippingFeeConfigList){
                String areaId = shippingFeeConfig.getDestAreaId();
                Long distributionModeId = shippingFeeConfig.getDistributionModeId();
                Long shippingTemeplateId = shippingFeeConfig.getShippingTemeplateId();

                ShippingFeeConfigCommand shippingFeeConfigCommand = new ShippingFeeConfigCommand();
                shippingFeeConfigCommand = (ShippingFeeConfigCommand) ConvertUtils.convertFromTarget(shippingFeeConfigCommand, shippingFeeConfig);

                //模板id-物流方式id-目的地id为key  
                String key = buildKey(areaId, distributionModeId, shippingTemeplateId);
                buildShippingFeeConfigMap.put(key, shippingFeeConfigCommand);
            }
        }
        return buildShippingFeeConfigMap;
    }

    /**
     * @param areaId
     * @param distributionModeId
     * @param shippingTemeplateId
     * @return
     * @since 5.3.2.4
     */
    private String buildKey(String areaId,Long distributionModeId,Long shippingTemeplateId){
        //模板id-物流方式id-目的地id为key  
        StringBuilder sb = new StringBuilder();
        sb.append(shippingTemeplateId);
        sb.append(KEY_CONNECTOR);
        sb.append(distributionModeId);
        sb.append(KEY_CONNECTOR);
        sb.append(areaId);
        return sb.toString();
    }

    //*************************************************************************************************

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
