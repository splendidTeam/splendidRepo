package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.dao.rule.CustomizeFilterClassDao;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.rule.CustomizeFilterClass;
import com.baozun.nebula.sdk.manager.SdkCustomizeFilterExecuteManager;

/**
 * 自定义方法模板用例，步骤：
 * 1，店铺自行实施
 * 2，在“自定义筛选器条件管理”中注册
 * 3，在筛选器中用刚刚注册的实例生成一个筛选器
 * 4，和活动绑定
 * @author jumbo
 *
 */
@Transactional
@Service("sdkCustomExampleManagerImpl")
public class SdkCustomExampleManagerImpl implements SdkCustomizeFilterExecuteManager{
	
	@Autowired
	private CustomizeFilterClassDao customizeFilterClassDao;
	
	//TODO 修改成访问店铺数据的方法
	//@Autowired
	//ShopMemberDao shopMemberDao;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public List<Long> execute() {
		//自定义筛选器提供的缓存机制，也可自行考虑缓存机制
		//TODO 修改为店铺的自定义服务名称
		String serviceName = "sdkCustomExampleManagerImpl";	
		String cacheKey = CacheKeyConstant.PROMTION_CUSTOMIZE_FILTER_PREFIX + serviceName;
		String cacheVersionKey = CacheKeyConstant.PROMTION_CUSTOMIZE_FILTER_PREFIX + serviceName + "_Version";
		
		List<Long> listIds = new ArrayList<Long>();
 
		CustomizeFilterClass cmdClass = customizeFilterClassDao.findCustomizeFilterClassListByServiceName(serviceName);
		if (null == cmdClass) return null;
		Integer cacheTime = cmdClass.getCacheSecond();
		Date currentVersion = cmdClass.getVersion();
		if (cacheTime > 0)
		{
			//缓存里有，取缓存
			Object cache = cacheManager.getObject(cacheKey);
			Object cacheVersion = cacheManager.getObject(cacheVersionKey);
			
			if (null != cache && null != cacheVersion && currentVersion.compareTo((Date)cacheVersion)<=0)
			{
				//缓存里有，且没过期，取缓存
				listIds = (List<Long>)cache;
				return listIds;
			}			
			
			//TODO 缓存过期，取一次DB。更新数据，同时更新新时间标杆。
			listIds = getFromDBForExample();
			cacheManager.setObject(cacheKey,listIds,cacheTime);
			cacheManager.setObject(cacheVersionKey,currentVersion,cacheTime);
		}else
		{
			//TODO 不缓存，每次取DB。不建议使用。或者自己走缓存。
			listIds = getFromDBForExample();
		}
		return listIds;
	}
	/**
	 * 请自行修改
	 * 这里只是个说明要取数据库的例子
	 * @return
	 */
	private List<Long> getFromDBForExample()
	{
		List<Long> memberIds = new ArrayList<Long>();
		//TODO 修改成访问店铺数据的方法
		memberIds.add(311L);
		//memberIds = shopMemberDao.findFirstBuyMemberIds(ShopMemberConstants.SOURCE_JNJ_MEMBER_SOURCE_REG);
		
		return memberIds;
	}

}
