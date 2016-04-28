package com.baozun.nebula.sdk.manager.impl;

import java.util.Date;
import java.util.List;

import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.baseinfo.NavigationDao;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;

/**
 * 
 * @author 阳羽
 * @createtime 2014-2-10 下午12:27:19
 */
@Transactional
@Service("sdkNavigationManager")
public class SdkNavigationManagerImpl implements SdkNavigationManager {
	
	@Autowired
	private NavigationDao navigationDao;

	@Override
	@Transactional(readOnly=true)
	public List<Navigation> findNavigationList(Sort[] sorts) {
		return navigationDao.findAllNavigationList(sorts);
	}

	@Override
	public Navigation createOrUpdateNavigation(Navigation navigation) {
		if (new Long(-1).equals(navigation.getId())) {
			return navigationDao.save(navigation);
		} else {
			Navigation dbNavi = navigationDao.getByPrimaryKey(navigation.getId());
			dbNavi.setIsNewWin(navigation.getIsNewWin());
			dbNavi.setLifecycle(navigation.getLifecycle());
			dbNavi.setModifyTime(new Date());
			dbNavi.setName(navigation.getName());
			dbNavi.setOpeartorId(navigation.getOpeartorId());
			dbNavi.setParam(navigation.getParam());
			dbNavi.setSort(navigation.getSort());
			dbNavi.setType(navigation.getType());
			dbNavi.setUrl(navigation.getUrl());
			
			//如果该导航状态为失效，则禁用其所有子导航
			if (dbNavi.getLifecycle().equals(0)) {
				disableAllChildren(dbNavi.getId());
			}
			
			return dbNavi;
		}
	}

	@Override
	public void removeNavigationById(Long id) {
		navigationDao.removeNavigationById(id);
	}
	
	/**
	 * 根据ID，递归禁用其所有子孙导航
	 * @param id
	 */
	private void disableAllChildren(Long id){
		List<Navigation> list = navigationDao.findNavigationListByParentId(id);
		if (null != list && list.size() > 0) {
			for (Navigation n : list) {
				n = navigationDao.getByPrimaryKey(n.getId());
				n.setLifecycle(0);
				disableAllChildren(n.getId());
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public void sortNavigationsByIds(List<Long> idList, Long userId) {
		int sort = 1;
		for (Long id : idList) {
			Navigation navi = navigationDao.getByPrimaryKey(id);
			navi.setModifyTime(new Date());
			navi.setOpeartorId(userId);
			navi.setSort(sort);
			sort ++;
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<Navigation> findAvailableNavigationList(Sort[] sorts) {
		return navigationDao.findAvailableNavigationList(sorts);
	}

	@Override
	@Transactional(readOnly = true)
	public Navigation findEffectNavigationByUrl(String url) {
		// TODO Auto-generated method stub
		return navigationDao.findEffectNavigationByUrl(url);
	}
}



