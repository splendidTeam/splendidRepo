package com.baozun.nebula.sdk.manager.impl;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.find;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.AddressUtils;
import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.freight.DistributionModeDao;
import com.baozun.nebula.dao.freight.ProductShippingTemeplateDao;
import com.baozun.nebula.dao.freight.ShippingFeeConfigDao;
import com.baozun.nebula.dao.freight.ShippingProviderDao;
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
import com.baozun.nebula.model.freight.ProductShippingTemeplate;
import com.baozun.nebula.model.freight.ShippingFeeConfig;
import com.baozun.nebula.model.freight.ShippingTemeplate;
import com.baozun.nebula.model.freight.SupportedArea;
import com.baozun.nebula.model.freight.TemeplateDistributionMode;
import com.baozun.nebula.model.salesorder.Logistics;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.baozun.nebula.sdk.command.logistics.ItemFreightInfoCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.logistics.ShippingProviderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;
import com.feilong.core.util.CollectionsUtil;

@Transactional
@Service("logisticsManager")
public class LogisticsManagerImpl implements LogisticsManager{

    private static final Logger log = LoggerFactory.getLogger(LogisticsManagerImpl.class);

    @Autowired
    private FreightMemoryManager freigthMemoryManager;

    @Autowired
    private SdkLogisticsDao sdkLogisticsDao;

    @Autowired
    private ShippingFeeConfigDao shippingFeeConfigDao;

    @Autowired
    private ShippingTemeplateDao shippingTemeplateDao;

    @Autowired
    private ProductShippingTemeplateDao productShippingFeeTemeplateDao;

    @Autowired
    private ShippingProviderDao shippingProviderDao;

    @Autowired
    private SupportedAreaDao supportedAreaDao;

    @Autowired
    private DistributionModeDao distributionModeDao;

    @Autowired
    private TemeplateDistributionModeDao temeplateDistributionModeDao;

    @Override
    public List<Address> findProvinceList(){
        return AddressUtil.getProviences();
    }

    @Override
    public List<Address> findCities(Long provinceId){
        return AddressUtil.getCities(provinceId);
    }

    @Override
    public List<Address> findCounties(Long cityId){
        return AddressUtil.getCounties(cityId);
    }

    @Override
    public List<Address> findTowns(Long areaId){
        return AddressUtil.getTowns(areaId);
    }

    // @Override
    // public BigDecimal findFreight(List<ItemFreightInfoCommand> itemList,
    // String areaId, Integer shippingProviderId) {
    //
    // /**
    // * 首件单位 3
    // 续件单位 2
    // 首件价格 10
    // 续件价格 5
    //
    // 买7件 价格 10 + （(7-3)/2）*5 要注意续件 如果不是整除的话 加上一个续件价格
    // */
    //
    // BigDecimal result = BigDecimal.ZERO;
    //
    // for(ItemFreightInfoCommand ifiCmd :itemList){
    // Long count = ifiCmd.getCount();
    //
    // if(count<1){
    // throw new IllegalArgumentException();
    // }
    //
    //
    // ShippingFeeConfig cfg =
    // productShippingFeeTemeplateDao.findProductShippingTemeplateByItemIdCityId(ifiCmd.getItemId(),areaId,shippingProviderId);
    //
    // if(cfg == null){//如果不是城市的话，看是否配置该城市对应的省份信息
    // Address address = AddressUtil.getAddressById(Long.parseLong(areaId));
    // if(address == null){
    // throw new IllegalArgumentException();
    // }
    // cfg =
    // productShippingFeeTemeplateDao.findProductShippingTemeplateByItemIdProvienceId(ifiCmd.getItemId(),
    // String.valueOf(address.getpId()), shippingProviderId);
    // if(cfg == null){
    // throw new BusinessException(Constants.ADDRESS_NOT_EXISTS);
    // }
    // }
    // BigDecimal firstPartPrice = cfg.getFirstPartPrice();//首件价格
    // Integer firstPartUnit = cfg.getFirstPartUnit();//首件单位 意思是 首件价格 的的数量。eg
    // 按件的话，首件是两件，价格是5
    // BigDecimal subsequentPartPrice = cfg.getSubsequentPartPrice();//续件价格
    // Integer subsequentPartUnit = cfg.getSubsequentPartUnit();//续件单位
    //
    // result = firstPartPrice;
    // if(count > firstPartUnit){
    // Long last = count - firstPartUnit;
    // Long a =
    // (last%subsequentPartUnit==0)?(last/subsequentPartUnit):((last/subsequentPartUnit)+1);
    // BigDecimal d= subsequentPartPrice.multiply(new BigDecimal(a));
    // result = result.add(d);
    // }
    // }
    //
    // return result;
    // }

    @Override
    public LogisticsCommand findLogisticsByOrderId(Long orderId){
        return sdkLogisticsDao.findLogisticsByOrderId(orderId);
    }

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

        if (Validator.isNotNullOrEmpty(configList)){

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
     * 
     * @author 何波
     * @Description: 保存运费模板与物流方式关系
     * @param dbmIds
     * @param temeplateId
     * void
     * @throws
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

    @Override
    public ShippingTemeplateCommand findShippingTemeplateCommandById(Long temeplateId){
        ShippingTemeplate temeplate = shippingTemeplateDao.getByPrimaryKey(temeplateId);
        List<ShippingFeeConfigCommand> configList = shippingFeeConfigDao.findShippingFeeConfigsByTemeplateId(temeplateId);

        return getShippingTemeplateCommand(temeplate, configList);
    }

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

    @Override
    public boolean removeShippingTemeplate(Long temeplateId){
        // //先查看这个模板是否关联了 具体的商品，如果有，则不能删除，抛出异常
        // Integer count =
        // productShippingFeeTemeplateDao.findUsedCountByTemeplateId(temeplateId);
        // if(count>0){
        // throw new
        // BusinessException(Constants.PRODUCT_SHIPPING_FEE_CONNECTED);
        // }
        //
        // //如果没有 ,删除 (不用删除 商品运费表里的信息，因为上一步已经确定了没有该记录)
        //
        // //删除 模板基本信息
        // shippingTemeplateDao.deleteByPrimaryKey(temeplateId);
        //
        // //删除 模板里的运费配置
        // shippingFeeConfigDao.deleteShippingFeeConfigsByTemeplateId(temeplateId);
        //
        // //删除 商品运费表里所有此Id 的数据
        // productShippingFeeTemeplateDao.removeAllShippingFeeConfigsByTemeplateId(temeplateId);

        // 删除 运费模板表中的数据
        shippingTemeplateDao.deleteByPrimaryKey(temeplateId);

        // 删除 运费配置表中的数据
        shippingFeeConfigDao.deleteShippingFeeConfigsByTemeplateId(temeplateId);

        return true;
    }

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
        if (Validator.isNotNullOrEmpty(configList)){

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

    @Override
    public boolean applyItemShippingTemeplate(Long itemId,Long temeplateId){
        ProductShippingTemeplate ptmp = productShippingFeeTemeplateDao.findProductShippingTemeplate(itemId, temeplateId);

        // 如果没有，则新增
        if (null == ptmp){
            ptmp = new ProductShippingTemeplate();
            ptmp.setItemId(itemId);
            ptmp.setShippingTemeplateId(temeplateId);

            if (null == productShippingFeeTemeplateDao.save(ptmp)){
                throw new BusinessException(Constants.Apply_PRODUCT_TEMEPLATE_FAILURE);
            }
        }else{// 如果有该商品的记录则修改
            if (1 != productShippingFeeTemeplateDao.updateProductShippingTemeplate(itemId, temeplateId)){
                // 应该只有一条记录被修改，如果不是，则抛出异常
                throw new BusinessException(Constants.Apply_PRODUCT_TEMEPLATE_FAILURE);
            }
        }

        return true;
    }

    @Override
    public Pagination<ShippingTemeplate> findShippingTemeplateList(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId){
        return shippingTemeplateDao.findShippingTemeplateList(page, sorts, paraMap, shopId);
    }

    @Override
    public boolean removeItemShippingTemeplate(Long itemId){
        Integer result = productShippingFeeTemeplateDao.removeItemShippingTemeplate(itemId);
        log.info("removeItemShippingTemeplate result :" + result);
        return true;
    }

    @Override
    public List<ShippingProviderCommand> findLogisticsTypes(Long itemId,String areaId){
        return shippingProviderDao.findLogisticsTypes(itemId, areaId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.LogisticsManager#findDistributeMode(java
     * .lang.Long, java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public List<DistributionMode> findDistributeMode(Long shopId,Long provienceId,Long cityId,Long countyId,Long townId){

        // 拿到所有的物流方式 列表
        List<DistributionCommand> distributionCommandList = freigthMemoryManager.getDistributionList();

        // 拿到店铺运费模板的map
        ShopShippingTemeplateMap shopShippingTemeplateMap = freigthMemoryManager.getShopShippingTemeplateMap();

        // 当前店铺 的物流方式Id set
        Set<Long> distributionModeIdSet = new HashSet<Long>();

        if (null != shopShippingTemeplateMap){

            // 获得当前店铺的map
            ShopShippingTemeplateCommand sstCmd = shopShippingTemeplateMap.get(shopId);

            // 支持当前店铺的物流方式的结果列表
            List<DistributionModeCommand> distributionModeList = null;

            // 当前店铺 的模板列表
            List<ShippingTemeplateCommand> shippingTemeplateList = null;

            // if(null!=sstCmd){
            // distributionModeList = sstCmd.getDistributionModeList();
            // }
            //
            // if(Validator.isNotNullOrEmpty(distributionModeList)){
            // for(DistributionModeCommand dmCmd : distributionModeList){
            // distributionModeIdSet.add(dmCmd.getId());
            // }
            // }else{
            // return null;
            // }

            if (null != sstCmd){
                shippingTemeplateList = sstCmd.getShippingTemeplateList();
            }else{
                return null;
            }

            // 根据当前模板，找到支持该模板的物流方式
            if (Validator.isNotNullOrEmpty(shippingTemeplateList)){

                // XXX 目前仅考虑店铺只有一个模板
                ShippingTemeplateCommand spCmd = shippingTemeplateList.get(0);
                //查找默认运费模板
                for (ShippingTemeplateCommand stc : shippingTemeplateList){
                    if (stc.isDefault()){
                        spCmd = stc;
                    }
                }
                //ShippingTemeplateCommand spCmd = shippingTemeplateList.get(0);
                Long stId = spCmd.getId();
                List<TemeplateDistributionMode> tdmList = temeplateDistributionModeDao.getDistributionModeByTemeplateId(stId);

                if (Validator.isNotNullOrEmpty(tdmList)){
                    for (TemeplateDistributionMode tdCmd : tdmList){
                        distributionModeIdSet.add(tdCmd.getDistributionModeId());
                    }
                }else{
                    return null;
                }

            }else{
                return null;
            }

        }else{
            return null;
        }

        // 根据 物流方式ID 找出 支持本商铺的 DistributionCommand
        List<DistributionCommand> curShopDistributionCommandList = null;
        if (Validator.isNotNullOrEmpty(distributionModeIdSet)){
            curShopDistributionCommandList = new ArrayList<DistributionCommand>();

            for (Long modeId : distributionModeIdSet){
                for (DistributionCommand distributionCmd : distributionCommandList){
                    if (modeId.equals(distributionCmd.getDistributionModeId())){
                        curShopDistributionCommandList.add(distributionCmd);
                    }
                }
            }

        }else{
            return null;
        }

        if (null != curShopDistributionCommandList && curShopDistributionCommandList.size() > 0){// 遍历
                                                                                                     // 支持
                                                                                                 // 本商铺的
                                                                                                 // 物流列表

            List<DistributionMode> modeList = new ArrayList<DistributionMode>();

            // 遍历 支持 本商铺的 物流列表
            for (DistributionCommand distributionCommand : curShopDistributionCommandList){// 遍历
                                                                                               // 支持
                                                                                           // 本商铺的
                                                                                           // 物流列表
                                                                                           // 开始

                Long modeId = distributionCommand.getDistributionModeId();
                String modeName = distributionCommand.getDistributionModeName();
                List<SupportedAreaCommand> whiteList = distributionCommand.getWhiteAreaList();
                Map<Long, List<SupportedAreaCommand>> curBlackMap = distributionCommand.getBlackAreaListMap();

                for (SupportedAreaCommand saCmd : whiteList){// 遍历 单个物流 的白名单开始

                    Long groupNo = saCmd.getGroupNo();
                    List<SupportedAreaCommand> blackList = curBlackMap.get(groupNo);

                    if (provienceId != null){
                        if (saCmd.getAreaId().equals(provienceId.toString())){// 看省
                                                                                  // 是不是在
                                                                              // whiteList里

                            // 看 市 县 乡 是不是在黑名单中 ，如果都不在黑名单中 ，说明 此物流方式支持该地址 TODO
                            // 如果 coutnId 不存在 就不判断 isAreaInBlackList countyId 和
                            // townId
                            if (!(isAreaInBlackList(cityId, blackList) || isAreaInBlackList(countyId, blackList) || isAreaInBlackList(townId, blackList))){

                                modeList.add(getDistributionMode(modeId, modeName));
                                break;
                            }

                        }
                    }

                    if (cityId != null){
                        if (saCmd.getAreaId().equals(cityId.toString())){// 看 市
                                                                             // 是不是在whiteList
                                                                         // 看 县 是不是在黑名单中
                                                                         // 看 乡 是不是在黑名单中

                            if (!(isAreaInBlackList(countyId, blackList) || isAreaInBlackList(townId, blackList))){

                                modeList.add(getDistributionMode(modeId, modeName));
                                break;
                            }

                        }
                    }

                    if (null != countyId){
                        if (saCmd.getAreaId().equals(countyId.toString())){// 看县
                                                                               // 是不是在whiteList
                                                                           // 看 乡 是不是在黑名单中
                            if (!(isAreaInBlackList(townId, blackList))){

                                modeList.add(getDistributionMode(modeId, modeName));
                                break;
                            }

                        }
                    }

                    if (null != townId){
                        if (saCmd.getAreaId().equals(townId.toString())){// 看乡是不是在whiteList
                            modeList.add(getDistributionMode(modeId, modeName));
                            break;
                        }
                    }

                }// 遍历 单个物流 的白名单结束

            }// 遍历 支持 本商铺的 物流列表 结束

            if (modeList.size() > 0){
                return modeList;
            }else{
                return null;
            }

        }else{
            return null;
        }

    }

    private DistributionMode getDistributionMode(Long modeId,String modeName){
        DistributionMode supportedMode = new DistributionMode();
        supportedMode.setId(modeId);
        supportedMode.setName(modeName);

        return supportedMode;
    }

    /**
     * 判断区域Id 是否在 黑名单里
     * 
     * @param areaId
     * @param blackList
     * @return
     */
    private boolean isAreaInBlackList(Long areaId,List<SupportedAreaCommand> blackList){

        if (null == areaId){
            return false;
        }

        if (null == blackList || blackList.size() < 1){
            return false;
        }

        for (SupportedAreaCommand blackArea : blackList){

            // AreaId
            String blackAreaId = blackArea.getAreaId();
            if (blackAreaId != null && blackAreaId.equals(areaId.toString())){
                return true;
            }

            // TODO designate

        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.LogisticsManager#findFreight(java.util.
     * List, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long,
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public BigDecimal findFreight(List<ItemFreightInfoCommand> itemList,Long distributionModeId,Long shopId,Long provienceId,Long cityId,Long countyId,Long townId){
        // 获得 店铺运费模板Map
        ShopShippingTemeplateMap shopShippingTemeplateMap = freigthMemoryManager.getShopShippingTemeplateMap();

        // 获得 该店铺的运费模板信息
        ShopShippingTemeplateCommand sstCmd = shopShippingTemeplateMap.get(shopId);

        // 获得 该店铺的运费模板列表
        List<ShippingTemeplateCommand> temeplateList = sstCmd.getShippingTemeplateList();

        //如果没有传物流方式 设置默认物流方式
        if (null == distributionModeId){
            List<DistributionMode> distributeModelList = findDistributeMode(shopId, provienceId, cityId, countyId, townId);
            distributionModeId = distributeModelList.get(0).getId();
        }

        ShippingTemeplateCommand defaultTemplate = temeplateList.get(0);
        //查找默认运费模板
        for (ShippingTemeplateCommand stc : temeplateList){
            if (stc.isDefault()){
                defaultTemplate = stc;
            }
        }

        // 根据
        ShippingFeeConfigCommand feeCmd = getFeeConfig(defaultTemplate, distributionModeId, provienceId, cityId, countyId, townId);

        ShippingTemeplateCommand temeplate = defaultTemplate;
        if (feeCmd == null){
            return temeplate.getDefaultFee();
        }else{
            String calType = temeplate.getCalculationType();

            FreightStrategy strategy = null;

            // 根据不同的type 选择不同的算法
            if (ShippingTemeplate.CAL_TYPE_BY_UNIT.equals(calType)){
                strategy = new UnitFreightStrategy();
            }else if (ShippingTemeplate.CAL_TYPE_BY_WEIGHT.equals(calType)){
                strategy = new WeightFreightStrategy();
            }else if (ShippingTemeplate.CAL_TYPE_BY_BASE.equals(calType)){
                strategy = new BaseFreightStrategy();
            }
            return strategy.cal(feeCmd, itemList);
        }
    }

    /**
     * 根据 运费模板，物流方式id ,省id ,cityId ，countyId，townId 来获取符合条件的运费配置信息
     * 
     * @param shippingTemeplate
     *            运费模板
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
     * @return
     */
    private ShippingFeeConfigCommand getFeeConfig(ShippingTemeplateCommand shippingTemeplate,Long distributionModeId,Long provienceId,Long cityId,Long countyId,Long townId){
        ShippingFeeConfigCommand feeCmd = null;

        if (shippingTemeplate != null){
            // XXX 目前店铺只有一个 ShippingTemeplateCommand
            ShippingTemeplateCommand temeplate = shippingTemeplate;
            Long temeplateId = temeplate.getId();

            ShippingFeeConfigMap feeConfigMap = freigthMemoryManager.getShippingFeeConfigMap();

            // 模板id-物流方式id-目的地id为key
            StringBuilder keyPrefixSb = new StringBuilder();
            keyPrefixSb.append(temeplateId).append(ShippingFeeConfigMap.KEY_CONNECTOR).append(distributionModeId).append(ShippingFeeConfigMap.KEY_CONNECTOR);

            if (null != townId){
                StringBuilder key = new StringBuilder(keyPrefixSb).append(townId);
                feeCmd = feeConfigMap.get(key.toString());

                if (feeCmd != null){
                    return feeCmd;
                }
            }

            if (null != countyId){
                StringBuilder key = new StringBuilder(keyPrefixSb).append(countyId);
                feeCmd = feeConfigMap.get(key.toString());

                if (feeCmd != null){
                    return feeCmd;
                }
            }

            if (null != cityId){
                StringBuilder key = new StringBuilder(keyPrefixSb).append(cityId);
                feeCmd = feeConfigMap.get(key.toString());

                if (feeCmd != null){
                    return feeCmd;
                }
            }

            if (null != provienceId){
                StringBuilder key = new StringBuilder(keyPrefixSb).append(provienceId);
                feeCmd = feeConfigMap.get(key.toString());

                if (feeCmd != null){
                    return feeCmd;
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.LogisticsManager#saveSupportedArea(com.
     * baozun.nebula.freight.memory.SupportedAreaCommand)
     */
    @Override
    public List<SupportedAreaCommand> saveSupportedAreas(List<SupportedAreaCommand> supportedAreaCommandList){

        List<SupportedAreaCommand> savedList = new ArrayList<SupportedAreaCommand>();

        // 分组号 不在 manager 里边定，因为可能涉及到 批量保存多个分组的情况
        // Long groupNo = System.currentTimeMillis();

        for (SupportedAreaCommand supportedAreaCommand : supportedAreaCommandList){

            SupportedArea sa = new SupportedArea();
            sa = (SupportedArea) ConvertUtils.convertFromTarget(sa, supportedAreaCommand);
            // sa.setGroupNo(groupNo);
            SupportedArea savedArea = supportedAreaDao.save(sa);

            supportedAreaCommand = (SupportedAreaCommand) ConvertUtils.convertFromTarget(supportedAreaCommand, savedArea);
            savedList.add(supportedAreaCommand);
        }

        if (savedList.size() > 0){
            return savedList;
        }else{
            return null;
        }

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
        if (rows.equals(ids.size())){
            return true;
        }
        return false;
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
        savedCmd = (DistributionModeCommand) ConvertUtils.convertFromTarget(savedCmd, savedMode);

        return savedCmd;
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

    @Override
    public Logistics saveLogistics(Logistics logistics){
        return sdkLogisticsDao.save(logistics);
    }

    @Override
    public List<LogisticsCommand> findLogisticsListByQueryMap(Map<String, Object> queryMap){
        return sdkLogisticsDao.findLogisticsListByQueryMap(queryMap);
    }

    @Override
    public Boolean hasDistributionMode(CalcFreightCommand calcFreightCommand,Long shopId){
        // 通过收货地址获取支持的物流方式
        List<DistributionMode> distributionModeList = findDistributeMode(shopId, calcFreightCommand.getProvienceId(), calcFreightCommand.getCityId(), calcFreightCommand.getCountyId(), calcFreightCommand.getTownId());

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
        if (Validator.isNullOrEmpty(shopId)){
            return shippingTemeplateDao.updateShippingTemeplateById(id, isDefault);
        }else{
            if (isDefault){
                shippingTemeplateDao.updateShippingTemeplateByShopId(shopId, ShippingTemeplate.NOTDEFAULT);
            }
            return shippingTemeplateDao.updateShippingTemeplateById(id, isDefault);
        }
    }

}
