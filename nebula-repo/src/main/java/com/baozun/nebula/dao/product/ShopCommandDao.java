package com.baozun.nebula.dao.product;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Sort;

import com.baozun.nebula.command.ShopCommand;

/**
 * 店铺Dao
 * 
 * @author li.feng
 * 
 * @date 2013-7-2 上午10:50:40
 */
public interface ShopCommandDao extends GenericEntityDao<ShopCommand, Long>{

	/**
	 * 店铺列表
	 * 
	 * @param orgaTypeId
	 * @return
	 */
	@NativeQuery(model = ShopCommand.class)
	List<ShopCommand> findShopListByOrgaTypeId(@QueryParam Map<String, Object> paraMap,Sort[] sorts);
}
