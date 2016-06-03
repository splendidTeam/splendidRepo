package com.baozun.nebula.manager.cms;

import java.util.Map;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsPageInstance;

/**
 * 页面实例
 * @author Justin Hu
 *
 */
public interface PublishCmsPageInstanceManager extends BaseManager{


	
	
	/**
	 * 查看发布后的页面
	 * 
	 *  1.先从缓存取出，如取不到走下面的步骤
	 *  2.取出模板数据
	 *  3.取出发布表中的数据
	 *  4.整合成html代码
	 *  5.替换所有base的标识 
	 *  6.保存到缓存中
	 *  7.根据使用公共头尾的情况返回到不同的jsp中 
	 * @param pageInstance
	 * @return map, key=data的表示html代码 key=useCommonHeader 表示是否公共头尾("true")
	 */
	public Map<String,String> findPublishPage(CmsPageInstance pageInstance);

}
