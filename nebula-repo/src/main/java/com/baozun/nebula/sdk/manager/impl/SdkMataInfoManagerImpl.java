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
 *
 */
package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.curator.ZKWatchPath;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.I18nLangWatchInvoke;
import com.baozun.nebula.curator.invoke.SystemConfigWatchInvoke;
import com.baozun.nebula.dao.system.MataInfoDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.feilong.core.Validator;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * @author Tianlong.Zhang
 *
 */
@Service("sdkMataInfoManager")
public class SdkMataInfoManagerImpl implements SdkMataInfoManager{

    @Autowired
    private MataInfoDao                mataInfoDao;

    @Autowired
    private ZkOperator          zkOperator;
    
    @Autowired(required=false)
    private ZKWatchPath			zkWatchPath;

    private static Map<String, String> metaMap = new ConcurrentHashMap<String, String>();

    @Override
    public String findValue(String key){
        if (metaMap.isEmpty()){
            initMetaMap();
        }
        return metaMap.get(key);
    }

    @Override
    @Transactional(readOnly = true)
    public void initMetaMap(){

        Map<String, String> newMetaMap = new HashMap<String, String>();
        List<MataInfo> mataInfoList = mataInfoDao.findAllEffectMataInfoList();
        List<String> deleteCode = new ArrayList<String>();

        if (Validator.isNotNullOrEmpty(mataInfoList)){
            for (MataInfo info : mataInfoList){
                newMetaMap.put(info.getCode(), info.getValue());
                metaMap.put(info.getCode(), info.getValue());
            }

            for (Map.Entry<String, String> entry : metaMap.entrySet()){
                String code = entry.getKey();
                if (!newMetaMap.containsKey(code)){
                    deleteCode.add(code);
                }
            }

            for (String dc : deleteCode){
                metaMap.remove(dc);
            }
        }
    }

    /**
     * 保存MataInfo
     * 
     */
    @Transactional
    public MataInfo saveMataInfo(MataInfo model){
        Long id = model.getId();
        MataInfo mataInfo = null;
        if (id != null){
            MataInfo dbModel = findMataInfoById(id);
            dbModel.setValue(model.getValue());
            dbModel.setDeclare(model.getDeclare());
            mataInfo = mataInfoDao.save(dbModel);
        }else{
            String code = model.getCode();
            if (findMataInfoByCode(code) != null){
                throw new BusinessException("参数已经存在!");
            }
            model.setLifecycle(1);
            mataInfo = mataInfoDao.save(model);
        }
        zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(SystemConfigWatchInvoke.class));
        return mataInfo;
    }

    /**
     * 通过id获取MataInfo
     * 
     */
    @Transactional(readOnly = true)
    public MataInfo findMataInfoById(Long id){

        return mataInfoDao.getByPrimaryKey(id);
    }

    /**
     * 获取所有MataInfo列表
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<MataInfo> findAllMataInfoList(){

        return mataInfoDao.findAllMataInfoList();
    };

    /**
     * 通过ids获取MataInfo列表
     * 
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    public List<MataInfo> findMataInfoListByIds(List<Long> ids){
        return mataInfoDao.findMataInfoListByIds(ids);
    }

    /**
     * 通过参数map获取MataInfo列表
     * 
     * @param paraMap
     * @return
     */
    @Transactional(readOnly = true)
    public List<MataInfo> findMataInfoListByQueryMap(Map<String, Object> paraMap){
        return mataInfoDao.findMataInfoListByQueryMap(paraMap);
    }

    /**
     * 分页获取MataInfo列表
     * 
     * @param start
     * @param size
     * @param paraMap
     * @param sorts
     * @return
     */
    @Transactional(readOnly = true)
    public Pagination<MataInfo> findMataInfoListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return mataInfoDao.findMataInfoListByQueryMapWithPage(page, sorts, paraMap);
    }

    /**
     * 通过ids批量启用或禁用MataInfo
     * 设置lifecycle =0 或 1
     * 
     * @param ids
     * @return
     */
    @Transactional
    public void enableOrDisableMataInfoByIds(List<Long> ids,Integer state){
        mataInfoDao.enableOrDisableMataInfoByIds(ids, state);
        zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(SystemConfigWatchInvoke.class));
    }

    /**
     * 通过ids批量删除MataInfo
     * 设置lifecycle =2
     * 
     * @param ids
     * @return
     */
    @Transactional
    public void removeMataInfoByIds(List<Long> ids){
        mataInfoDao.removeMataInfoByIds(ids);
        zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(SystemConfigWatchInvoke.class));
    }

    /**
     * 获取有效的MataInfo列表
     * lifecycle =1
     * 
     * @param ids
     * @return
     */
    @Transactional(readOnly = true)
    public List<MataInfo> findAllEffectMataInfoList(){
        return mataInfoDao.findAllEffectMataInfoList();
    }

    /**
     * 通过参数map获取有效的MataInfo列表
     * 强制加上lifecycle =1
     * 
     * @param paraMap
     * @return
     */
    @Transactional(readOnly = true)
    public List<MataInfo> findEffectMataInfoListByQueryMap(Map<String, Object> paraMap){
        return mataInfoDao.findEffectMataInfoListByQueryMap(paraMap);
    }

    /**
     * 分页获取有效的MataInfo列表
     * 强制加上lifecycle =1
     * 
     * @param start
     * @param size
     * @param paraMap
     * @param sorts
     * @return
     */
    @Transactional(readOnly = true)
    public Pagination<MataInfo> findEffectMataInfoListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
        return mataInfoDao.findEffectMataInfoListByQueryMapWithPage(page, sorts, paraMap);
    }

    @Override
    @Transactional(readOnly = true)
    public MataInfo findMataInfoByCode(String code){
        return mataInfoDao.findMataInfoByCode(code);
    }
}
