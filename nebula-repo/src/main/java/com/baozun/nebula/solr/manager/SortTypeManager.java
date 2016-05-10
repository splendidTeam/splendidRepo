package com.baozun.nebula.solr.manager;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.solr.utils.SolrOrderSort;

/**
 * solr排序的接口
 * @author 冯明雷
 * @version 1.0
 * @time 2016年5月10日  下午5:26:59
 */
public interface SortTypeManager extends BaseManager{
	
	/**
	 * 获取solr的排序(各商城自己重写)
	 * @return SolrOrderSort[]
	 * @param sortStr
	 * @author 冯明雷
	 * @time 2016年5月10日下午5:27:20
	 */
	SolrOrderSort[] getSolrOrder(String sortStr);
}
