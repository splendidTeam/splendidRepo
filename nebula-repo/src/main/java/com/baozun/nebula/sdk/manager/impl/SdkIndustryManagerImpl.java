package com.baozun.nebula.sdk.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.IndustryDao;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.sdk.manager.SdkIndustryManager;

/**
 * 
 * 商品行业sdkManager实现类
 * 
 * @author chenguang.zhou
 * @date 2014年6月13日 下午2:49:46
 */
@Service("sdkIndustryManager")
@Transactional
public class SdkIndustryManagerImpl implements SdkIndustryManager {

	@Autowired
	private IndustryDao industryDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Industry> findIndustryListByNames(List<String> industryNames) {
		
		return industryDao.findIndustryListByNames(industryNames);
	}
	
}
