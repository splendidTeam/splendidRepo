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
package com.baozun.nebula.sdk.manager.impl;

import static com.baozun.nebula.freight.memory.ShippingFeeConfigMap.KEY_CONNECTOR;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.find;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueSet;
import static com.feilong.core.util.CollectionsUtil.select;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.AddressUtils;
import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.freight.DistributionModeDao;
import com.baozun.nebula.dao.freight.ShippingFeeConfigDao;
import com.baozun.nebula.dao.freight.ShippingTemeplateDao;
import com.baozun.nebula.dao.freight.SupportedAreaDao;
import com.baozun.nebula.dao.freight.TemeplateDistributionModeDao;
import com.baozun.nebula.dao.salesorder.SdkLogisticsDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.freight.calculation.BaseFreightStrategy;
import com.baozun.nebula.freight.calculation.FreightStrategy;
import com.baozun.nebula.freight.calculation.UnitFreightStrategy;
import com.baozun.nebula.freight.calculation.WeightFreightStrategy;
import com.baozun.nebula.freight.command.ShippingFeeConfigCommand;
import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.freight.manager.FreightMemoryManager;
import com.baozun.nebula.freight.memory.DistributionCommand;
import com.baozun.nebula.freight.memory.ShippingFeeConfigMap;
import com.baozun.nebula.freight.memory.ShopShippingTemeplateCommand;
import com.baozun.nebula.freight.memory.ShopShippingTemeplateMap;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.freight.ShippingFeeConfig;
import com.baozun.nebula.model.freight.ShippingTemeplate;
import com.baozun.nebula.model.freight.SupportedArea;
import com.baozun.nebula.model.freight.TemeplateDistributionMode;
import com.baozun.nebula.model.salesorder.Logistics;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.baozun.nebula.sdk.command.logistics.ItemFreightInfoCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.feilong.core.bean.ConvertUtil;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * The Class LogisticsManagerImpl.
 */
@Transactional
@Service("logisticsManager")
public class LogisticsManagerImpl implements LogisticsManager{

    /** The freigth memory manager. */
    @Autowired
    private FreightMemoryManager freigthMemoryManager;

    /** The sdk logistics dao. */
    @Autowired
    private SdkLogisticsDao sdkLogisticsDao;

    /** The shipping fee config dao. */
    @Autowired
    private ShippingFeeConfigDao shippingFeeConfigDao;

    /** The shipping temeplate dao. */
    @Autowired
    private ShippingTemeplateDao shippingTemeplateDao;

    /** The supported area dao. */
    @Autowired
    private SupportedAreaDao supportedAreaDao;

    /** The distribution mode dao. */
    @Autowired
    private DistributionModeDao distributionModeDao;

    /** The temeplate distribution mode dao. */
    @Autowired
    private TemeplateDistributionModeDao temeplateDistributionModeDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#findLogisticsByOrderId(java.lang.Long)
     */
    @Override
    public LogisticsCommand findLogisticsByOrderId(Long orderId){
        return sdkLogisticsDao.findLogisticsByOrderId(orderId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#saveShippingTemeplate(com.baozun.nebula.freight.command.ShippingTemeplateCommand)
     */
    @Override
    public ShippingTemeplateCommand saveShippingTemeplate(ShippingTemeplateCommand shippingTemeplateCmd){
        // 保存基本 运费模板的基本信息
        ShippingTemeplate st = new ShippingTemeplate();
        st = (ShippingTemeplate) ConvertUtils.convertTwoObject(st, shippingTemeplateCmd);
        if (st.isDefault()){// 设置其他为非默认
            shippingTemeplateDao.updateShippingTemeplateByShopId(st.getShopId(), ShippingTemeplate.NOTDEFAULT);
        }
        st = shippingTemeplateDao.save(st);

        Long stId = st.getId();
        ShippingTemeplateCommand saveShippingTemeplateCommand = new ShippingTemeplateCommand();
        saveShippingTemeplateCommand = (ShippingTemeplateCommand) ConvertUtils.convertModelToApi(saveShippingTemeplateCommand, st);
        //保存物流方式与运费模板关系
        List<Long> dbmIds = shippingTemeplateCmd.getDistributionModeIds();
        saveShippingToDistributionMode(dbmIds, stId);
        // 保存运费配置信息
        List<ShippingFeeConfigCommand> configList = shippingTemeplateCmd.getFeeConfigs();

        if (isNotNullOrEmpty(configList)){

            List<ShippingFeeConfigCommand> savedList = new ArrayList<ShippingFeeConfigCommand>();

            for (ShippingFeeConfigCommand configCmd : configList){
                configCmd.setShippingTemeplateId(stId);
                ShippingFeeConfig feeConfig = new ShippingFeeConfig();
                feeConfig = (ShippingFeeConfig) ConvertUtils.convertTwoObject(feeConfig, configCmd);
                feeConfig = shippingFeeConfigDao.save(feeConfig);

                if (null != feeConfig){
                    ShippingFeeConfigCommand savedConfigCmd = new ShippingFeeConfigCommand();
                    savedConfigCmd = (ShippingFeeConfigCommand) ConvertUtils.convertTwoObject(savedConfigCmd, feeConfig);
                    savedList.add(savedConfigCmd);
                }

            }

            // 如果 保存的 和 传入的 list size 不同 ，则说明保存异常
            if (savedList.size() != configList.size()){
                throw new BusinessException(Constants.SAVE_SHIPPING_CONFIG_FAILURE);
            }

            saveShippingTemeplateCommand.setFeeConfigs(savedList);
        }

        return saveShippingTemeplateCommand;
    }

    /**
     * Save shipping to distribution mode.
     *
     * @author 何波
     * @param dbmIds
     *            the dbm ids
     * @param temeplateId
     *            void
     * @Description: 保存运费模板与物流方式关系
     */
    private void saveShippingToDistributionMode(List<Long> dbmIds,Long temeplateId){
        if (dbmIds != null && temeplateId != null){
            for (Long distributionModeId : dbmIds){
                temeplateDistributionModeDao.deleteTemeplateDistributionByTemeplateId(temeplateId);
                TemeplateDistributionMode model = new TemeplateDistributionMode();
                model.setShippingTemeplateId(temeplateId);
                model.setDistributionModeId(distributionModeId);
                temeplateDistributionModeDao.save(model);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#findShippingTemeplateCommandById(java.lang.Long)
     */
    @Override
    public ShippingTemeplateCommand findShippingTemeplateCommandById(Long temeplateId){
        ShippingTemeplate temeplate = shippingTemeplateDao.getByPrimaryKey(temeplateId);
        List<ShippingFeeConfigCommand> configList = shippingFeeConfigDao.findShippingFeeConfigsByTemeplateId(temeplateId);
        return getShippingTemeplateCommand(temeplate, configList);
    }

    /**
     * Gets the shipping temeplate command.
     *
     * @param temeplate
     *            the temeplate
     * @param configList
     *            the config list
     * @return the shipping temeplate command
     */
    private ShippingTemeplateCommand getShippingTemeplateCommand(ShippingTemeplate temeplate,List<ShippingFeeConfigCommand> configList){
        if (null == temeplate){
            return null;
        }

        ShippingTemeplateCommand tmpCmd = new ShippingTemeplateCommand();
        tmpCmd = (ShippingTemeplateCommand) ConvertUtils.convertModelToApi(tmpCmd, temeplate);

        if ((null != configList) && configList.size() > 0){
            for (ShippingFeeConfigCommand feeCfg : configList){
                feeCfg.setCalculationType(tmpCmd.getCalculationType());
                feeCfg.setDestAreaName(AddressUtils.getFullAddressName(Long.parseLong(feeCfg.getDestAreaId()), AddressUtils.SYMBOL_ARROW));
            }
        }
        tmpCmd.setFeeConfigs(configList);
        return tmpCmd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#removeShippingTemeplate(java.lang.Long)
     */
    @Override
    public boolean removeShippingTemeplate(Long temeplateId){
        // 删除 运费模板表中的数据
        shippingTemeplateDao.deleteByPrimaryKey(temeplateId);

        // 删除 运费配置表中的数据
        shippingFeeConfigDao.deleteShippingFeeConfigsByTemeplateId(temeplateId);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#updateShippingTemeplate(com.baozun.nebula.freight.command.ShippingTemeplateCommand)
     */
    @Override
    public boolean updateShippingTemeplate(ShippingTemeplateCommand shippingTemeplateCmd){
        long tmpId = shippingTemeplateCmd.getId();
        if (shippingTemeplateCmd.isDefault()){// 设置其他为非默认
            shippingTemeplateDao.updateShippingTemeplateByShopId(shippingTemeplateCmd.getShopId(), ShippingTemeplate.NOTDEFAULT);
        }
        int effectedRows = shippingTemeplateDao.updateShippingTemeplate(shippingTemeplateCmd.getId(), shippingTemeplateCmd.getName(), shippingTemeplateCmd.getCalculationType(), shippingTemeplateCmd.isDefault(), shippingTemeplateCmd.getDefaultFee());
        if (effectedRows != 1){
            throw new BusinessException(Constants.SHIPPING_TEMEPLATE_UPDATE_FAILURE);
        }
        //保存运费模板与物流方式的关系
        List<Long> dbmIds = shippingTemeplateCmd.getDistributionModeIds();
        saveShippingToDistributionMode(dbmIds, tmpId);
        // XXX 或者是 先对比，然后进行 修改？ 找出来 新增的，删除的，要修改的？
        // 删除，再重新插入数据
        shippingFeeConfigDao.deleteShippingFeeConfigsByTemeplateId(shippingTemeplateCmd.getId());

        List<ShippingFeeConfigCommand> configList = shippingTemeplateCmd.getFeeConfigs();
        if (isNotNullOrEmpty(configList)){

            int savedCount = 0;
            for (ShippingFeeConfigCommand configCmd : configList){
                configCmd.setShippingTemeplateId(tmpId);
                ShippingFeeConfig feeConfig = new ShippingFeeConfig();
                feeConfig = (ShippingFeeConfig) ConvertUtils.convertTwoObject(feeConfig, configCmd);
                feeConfig = shippingFeeConfigDao.save(feeConfig);

                if (null != feeConfig){
                    savedCount++;
                }
            }

            if (savedCount != configList.size()){
                throw new BusinessException(Constants.SAVE_SHIPPING_CONFIG_FAILURE);
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#findShippingTemeplateList(loxia.dao.Page, loxia.dao.Sort[], java.util.Map, java.lang.Long)
     */
    @Override
    public Pagination<ShippingTemeplate> findShippingTemeplateList(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId){
        return shippingTemeplateDao.findShippingTemeplateList(page, sorts, paraMap, shopId);
    }

    /**
     * Find distribute mode.
     *
     * @param shopId
     *            the shop id
     * @param calcFreightCommand
     *            the calc freight command
     * @return the list
     * @since 5.3.2.4
     */
    private List<DistributionMode> findDistributeMode(Long shopId,CalcFreightCommand calcFreightCommand){
        //优化 see http://jira.baozun.cn/browse/NB-388

        //获得当前店铺的物流方式列表
        List<DistributionCommand> currentShopDistributionCommandList = getCurrentShopDistributionCommandList(shopId, calcFreightCommand);
        return buildDistributionModeList(calcFreightCommand, currentShopDistributionCommandList);
    }

    /**
     * 获得当前店铺的物流方式列表.
     *
     * @param shopId
     *            the shop id
     * @param calcFreightCommand
     *            the calc freight command
     * @return the current shop distribution command list
     * @since 5.3.2.4
     */
    private List<DistributionCommand> getCurrentShopDistributionCommandList(Long shopId,CalcFreightCommand calcFreightCommand){
        Set<Long> distributionModeIdSet = buildDistributionModeIdSet(shopId, calcFreightCommand);

        //---------------------------------------------------------------------------
        // 拿到所有的物流方式 列表
        List<DistributionCommand> distributionCommandList = freigthMemoryManager.getDistributionList();

        // 根据 物流方式ID 找出 支持本商铺的 DistributionCommand
        List<DistributionCommand> curShopDistributionCommandList = select(distributionCommandList, "distributionModeId", distributionModeIdSet);
        if (isNullOrEmpty(curShopDistributionCommandList)){
            return null;
        }
        return curShopDistributionCommandList;
    }

    /**
     * Builds the distribution mode id set.
     *
     * @param shopId
     *            the shop id
     * @param calcFreightCommand
     *            the calc freight command
     * @return the 设置
     * @since 5.3.2.4
     */
    private Set<Long> buildDistributionModeIdSet(Long shopId,CalcFreightCommand calcFreightCommand){
        Long distributionModeId = calcFreightCommand.getDistributionModeId();
        if (isNotNullOrEmpty(distributionModeId)){
            return new HashSet<>(ConvertUtil.toList(distributionModeId));
        }

        ShippingTemeplateCommand defaultShippingTemeplateCommand = getShippingTemeplateCommand(shopId, null);
        Long shippingTemeplateCommandId = defaultShippingTemeplateCommand.getId();

        List<TemeplateDistributionMode> temeplateDistributionModeList = temeplateDistributionModeDao.getDistributionModeByTemeplateId(shippingTemeplateCommandId);
        if (isNullOrEmpty(temeplateDistributionModeList)){
            return null;
        }
        //---------------------------------------------------------------------------
        // 当前店铺 的物流方式Id set
        return getPropertyValueSet(temeplateDistributionModeList, "distributionModeId");
    }

    /**
     * Builds the distribution mode list.
     *
     * @param calcFreightCommand
     *            the calc freight command
     * @param curShopDistributionCommandList
     *            the cur shop distribution command list
     * @return the list
     * @since 5.3.2.4
     */
    private List<DistributionMode> buildDistributionModeList(CalcFreightCommand calcFreightCommand,List<DistributionCommand> curShopDistributionCommandList){
        // 遍历支持本商铺的物流列表
        List<DistributionMode> supportDistributionModeList = new ArrayList<>();

        Long provienceId = calcFreightCommand.getProvienceId();
        Long cityId = calcFreightCommand.getCityId();
        Long countyId = calcFreightCommand.getCountyId();
        Long townId = calcFreightCommand.getTownId();

        // 遍历支持本商铺的 物流列表
        for (DistributionCommand distributionCommand : curShopDistributionCommandList){
            Long modeId = distributionCommand.getDistributionModeId();
            String modeName = distributionCommand.getDistributionModeName();
            List<SupportedAreaCommand> whiteSupportedAreaCommandList = distributionCommand.getWhiteAreaList();
            Map<Long, List<SupportedAreaCommand>> curBlackMap = distributionCommand.getBlackAreaListMap();

            DistributionMode buildDistributionMode = buildDistributionMode(modeId, modeName);
            for (SupportedAreaCommand supportedAreaCommand : whiteSupportedAreaCommandList){
                Long groupNo = supportedAreaCommand.getGroupNo();
                List<SupportedAreaCommand> blackSupportedAreaCommandList = curBlackMap.get(groupNo);

                String areaId = supportedAreaCommand.getAreaId();

                if (provienceId != null && areaId.equals(provienceId.toString())){// 看省 是不是在 whiteList里
                    // 看 市 县 乡 是不是在黑名单中 ，如果都不在黑名单中 ，说明 此物流方式支持该地址 
                    // 如果 coutnId 不存在 就不判断 isAreaInBlackList countyId 和  townId
                    if (!(isAreaInBlackList(cityId, blackSupportedAreaCommandList) || isAreaInBlackList(countyId, blackSupportedAreaCommandList) || isAreaInBlackList(townId, blackSupportedAreaCommandList))){
                        supportDistributionModeList.add(buildDistributionMode);
                        break;
                    }
                }

                if (cityId != null && areaId.equals(cityId.toString())){// 看 市 是不是在whiteList
                    // 看县 是不是在黑名单中
                    // 看 乡 是不是在黑名单中
                    if (!(isAreaInBlackList(countyId, blackSupportedAreaCommandList) || isAreaInBlackList(townId, blackSupportedAreaCommandList))){
                        supportDistributionModeList.add(buildDistributionMode);
                        break;
                    }
                }

                if (null != countyId && areaId.equals(countyId.toString())){// 看县是不是在whiteList
                    if (!(isAreaInBlackList(townId, blackSupportedAreaCommandList))){ // 看 乡 是不是在黑名单中
                        supportDistributionModeList.add(buildDistributionMode);
                        break;
                    }
                }

                if (null != townId && areaId.equals(townId.toString())){// 看乡是不是在whiteList
                    supportDistributionModeList.add(buildDistributionMode);
                    break;
                }
            }
        }
        return supportDistributionModeList;
    }

    /**
     * Builds the distribution mode.
     *
     * @param modeId
     *            the mode id
     * @param modeName
     *            the mode name
     * @return the distribution mode
     */
    private DistributionMode buildDistributionMode(Long modeId,String modeName){
        DistributionMode distributionMode = new DistributionMode();
        distributionMode.setId(modeId);
        distributionMode.setName(modeName);
        return distributionMode;
    }

    /**
     * 判断区域Id 是否在 黑名单里.
     *
     * @param areaId
     *            the area id
     * @param blackList
     *            the black list
     * @return true, if is area in black list
     */
    private boolean isAreaInBlackList(Long areaId,List<SupportedAreaCommand> blackList){
        if (null == areaId){
            return false;
        }
        if (null == blackList || blackList.size() < 1){
            return false;
        }
        for (SupportedAreaCommand blackArea : blackList){
            String blackAreaId = blackArea.getAreaId();
            if (blackAreaId != null && blackAreaId.equals(areaId.toString())){
                return true;
            }
            // TODO designate
        }
        return false;
    }

    /**
     * 计算运费.
     *
     * @param itemList
     *            货物信息
     * @param distributionModeId
     *            物流方式Id
     * @param shopId
     *            店铺Id
     * @param provienceId
     *            省id
     * @param cityId
     *            市id
     * @param countyId
     *            县id
     * @param townId
     *            乡id
     * @return the big decimal
     */
    public BigDecimal findFreight(List<ItemFreightInfoCommand> itemList,Long distributionModeId,Long shopId,Long provienceId,Long cityId,Long countyId,Long townId){
        //如果没有传物流方式 设置默认物流方式
        if (null == distributionModeId){
            CalcFreightCommand calcFreightCommand = new CalcFreightCommand(distributionModeId, provienceId, cityId, countyId, townId);
            List<DistributionMode> distributeModelList = findDistributeMode(shopId, calcFreightCommand);
            distributionModeId = distributeModelList.get(0).getId();
        }
        ShippingTemeplateCommand shippingTemeplateCommand = getShippingTemeplateCommand(shopId, distributionModeId);
        Validate.notNull(shippingTemeplateCommand, "shippingTemeplateCommand can't be null!");

        // 根据
        ShippingFeeConfigCommand shippingFeeConfigCommand = buildShippingFeeConfigCommand(shippingTemeplateCommand.getId(), distributionModeId, provienceId, cityId, countyId, townId);

        if (shippingFeeConfigCommand == null){
            return shippingTemeplateCommand.getDefaultFee();
        }

        String calculationType = shippingTemeplateCommand.getCalculationType();

        FreightStrategy freightStrategy = buildFreightStrategy(calculationType);
        return freightStrategy.cal(shippingFeeConfigCommand, itemList);
    }

    /**
     * Builds the freight strategy.
     *
     * @param calculationType
     *            the calculation type
     * @return the freight strategy
     * @since 5.3.2.4
     */
    private FreightStrategy buildFreightStrategy(String calculationType){
        // 根据不同的type 选择不同的算法
        if (ShippingTemeplate.CAL_TYPE_BY_UNIT.equals(calculationType)){
            return new UnitFreightStrategy();
        }else if (ShippingTemeplate.CAL_TYPE_BY_WEIGHT.equals(calculationType)){
            return new WeightFreightStrategy();
        }else if (ShippingTemeplate.CAL_TYPE_BY_BASE.equals(calculationType)){
            return new BaseFreightStrategy();
        }
        throw new UnsupportedOperationException("calculationType:[" + calculationType + "] not support!");
    }

    /**
     * Gets the default shipping temeplate command.
     *
     * @param shopId
     *            the shop id
     * @param distributionModeId
     *            物流方式id, 如果传入 那么就去找对应的物流模板; 否则取默认的物流模板
     * @return the default shipping temeplate command
     * @since 5.3.2.4
     */
    private ShippingTemeplateCommand getShippingTemeplateCommand(Long shopId,Long distributionModeId){
        // 获得店铺运费模板Map
        ShopShippingTemeplateMap shopShippingTemeplateMap = freigthMemoryManager.getShopShippingTemeplateMap();

        // 获得 该店铺的运费模板信息
        ShopShippingTemeplateCommand shopShippingTemeplateCommand = shopShippingTemeplateMap.get(shopId);
        // 获得 该店铺的运费模板列表
        List<ShippingTemeplateCommand> shippingTemeplateCommandList = shopShippingTemeplateCommand.getShippingTemeplateList();

        ShippingTemeplateCommand defaultShippingTemeplateCommand = null;
        //查找默认运费模板
        for (ShippingTemeplateCommand shippingTemeplateCommand : shippingTemeplateCommandList){
            if (null != distributionModeId){
                List<DistributionModeCommand> distributionModeCommandList = shippingTemeplateCommand.getDistributionModes();
                if (isNotNullOrEmpty(distributionModeCommandList) && null != find(distributionModeCommandList, "id", distributionModeId)){
                    return shippingTemeplateCommand;
                }
            }

            if (shippingTemeplateCommand.isDefault()){
                defaultShippingTemeplateCommand = shippingTemeplateCommand;

                if (null == distributionModeId){
                    break;
                }
            }
        }

        //如果  distributionModeId 是null,那么返回默认的运费模板
        if (null == distributionModeId){
            return defaultShippingTemeplateCommand;
        }

        return null;
    }

    /**
     * 根据 运费模板，物流方式id ,省id ,cityId ，countyId，townId 来获取符合条件的运费配置信息.
     *
     * @param templateId
     *            the template id
     * @param distributionModeId
     *            物流方式Id
     * @param provienceId
     *            省Id
     * @param cityId
     *            市id
     * @param countyId
     *            县id
     * @param townId
     *            乡id
     * @return the fee config
     */
    private ShippingFeeConfigCommand buildShippingFeeConfigCommand(Long templateId,Long distributionModeId,Long provienceId,Long cityId,Long countyId,Long townId){
        ShippingFeeConfigMap shippingFeeConfigMap = freigthMemoryManager.getShippingFeeConfigMap();

        String keyPrefixSb = buildKeyPrefixSb(distributionModeId, templateId);
        if (null != townId){
            ShippingFeeConfigCommand shippingFeeConfigCommand = shippingFeeConfigMap.get(keyPrefixSb + townId);
            if (shippingFeeConfigCommand != null){
                return shippingFeeConfigCommand;
            }
        }

        if (null != countyId){
            ShippingFeeConfigCommand shippingFeeConfigCommand = shippingFeeConfigMap.get(keyPrefixSb + countyId);
            if (shippingFeeConfigCommand != null){
                return shippingFeeConfigCommand;
            }
        }

        if (null != cityId){
            ShippingFeeConfigCommand shippingFeeConfigCommand = shippingFeeConfigMap.get(keyPrefixSb + cityId);
            if (shippingFeeConfigCommand != null){
                return shippingFeeConfigCommand;
            }
        }

        if (null != provienceId){
            ShippingFeeConfigCommand shippingFeeConfigCommand = shippingFeeConfigMap.get(keyPrefixSb + provienceId);
            if (shippingFeeConfigCommand != null){
                return shippingFeeConfigCommand;
            }
        }
        return null;
    }

    /**
     * Builds the key prefix sb.
     *
     * @param distributionModeId
     *            the distribution mode id
     * @param templateId
     *            the template id
     * @return the string
     * @since 5.3.2.4
     */
    private String buildKeyPrefixSb(Long distributionModeId,Long templateId){
        // 模板id-物流方式id-目的地id为key
        StringBuilder keyPrefixSb = new StringBuilder();
        keyPrefixSb.append(templateId);
        keyPrefixSb.append(KEY_CONNECTOR);
        keyPrefixSb.append(distributionModeId);
        keyPrefixSb.append(KEY_CONNECTOR);
        return keyPrefixSb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#saveSupportedAreas(java.util.List)
     */
    @Override
    public List<SupportedAreaCommand> saveSupportedAreas(List<SupportedAreaCommand> supportedAreaCommandList){
        List<SupportedAreaCommand> savedList = new ArrayList<>();

        // 分组号 不在 manager 里边定，因为可能涉及到 批量保存多个分组的情况
        // Long groupNo = System.currentTimeMillis();

        for (SupportedAreaCommand supportedAreaCommand : supportedAreaCommandList){
            SupportedArea sa = new SupportedArea();
            sa = (SupportedArea) ConvertUtils.convertFromTarget(sa, supportedAreaCommand);
            SupportedArea savedArea = supportedAreaDao.save(sa);

            savedList.add((SupportedAreaCommand) ConvertUtils.convertFromTarget(supportedAreaCommand, savedArea));
        }
        return savedList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.LogisticsManager#deleteSupportedAreas(java
     * .util.List)
     */
    @Override
    public boolean deleteSupportedAreas(List<Long> ids){
        Integer rows = supportedAreaDao.deleteSupportedAreas(ids);
        return rows.equals(ids.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.LogisticsManager#saveDistributionMode(com
     * .baozun.nebula.sdk.command.logistics.DistributionModeCommand)
     */
    @Override
    public DistributionModeCommand saveDistributionMode(DistributionModeCommand cmd){
        DistributionMode mode = new DistributionMode();
        mode.setName(cmd.getName());
        DistributionMode savedMode = distributionModeDao.save(mode);

        DistributionModeCommand savedCmd = new DistributionModeCommand();
        return (DistributionModeCommand) ConvertUtils.convertFromTarget(savedCmd, savedMode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.LogisticsManager#deleteDistributionMode
     * (java.lang.Long)
     */
    @Override
    public boolean deleteDistributionMode(Long distributionModeId){
        distributionModeDao.deleteByPrimaryKey(distributionModeId);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.LogisticsManager#updateDistributionMode
     * (com.baozun.nebula.sdk.command.logistics.DistributionModeCommand)
     */
    @Override
    public boolean updateDistributionMode(DistributionModeCommand cmd){
        Integer rows = distributionModeDao.updateDistributionMode(cmd.getId(), cmd.getName());
        return rows.equals(1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#saveLogistics(com.baozun.nebula.model.salesorder.Logistics)
     */
    @Override
    public Logistics saveLogistics(Logistics logistics){
        return sdkLogisticsDao.save(logistics);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#findLogisticsListByQueryMap(java.util.Map)
     */
    @Override
    public List<LogisticsCommand> findLogisticsListByQueryMap(Map<String, Object> queryMap){
        return sdkLogisticsDao.findLogisticsListByQueryMap(queryMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#hasDistributionMode(com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand, java.lang.Long)
     */
    @Override
    public Boolean hasDistributionMode(CalcFreightCommand calcFreightCommand,Long shopId){
        //优化 see http://jira.baozun.cn/browse/NB-386

        // 通过收货地址获取支持的物流方式
        List<DistributionMode> distributionModeList = findDistributeMode(shopId, calcFreightCommand);
        if (isNullOrEmpty(distributionModeList)){
            return false;
        }

        if (isNullOrEmpty(calcFreightCommand.getDistributionModeId())){
            return true;
        }

        return null != find(distributionModeList, "id", calcFreightCommand.getDistributionModeId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.LogisticsManager#updateShippingTemeplateIsDefault(java.lang.Long, java.lang.Long, boolean)
     */
    @Override
    public Integer updateShippingTemeplateIsDefault(Long shopId,Long id,boolean isDefault){
        if (isNullOrEmpty(shopId)){
            return shippingTemeplateDao.updateShippingTemeplateById(id, isDefault);
        }
        if (isDefault){
            shippingTemeplateDao.updateShippingTemeplateByShopId(shopId, ShippingTemeplate.NOTDEFAULT);
        }
        return shippingTemeplateDao.updateShippingTemeplateById(id, isDefault);
    }

}
