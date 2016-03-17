package com.baozun.nebula.calculateEngine.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.calculateEngine.param.ScopeType;
import com.baozun.nebula.sdk.manager.impl.SdkCustomizeFilterLoader;
import com.baozun.nebula.sdk.manager.impl.SdkEngineManagerImpl;

/**
 * 用于将范围表达式进行解析
 * 
 * @author jumbo
 * 
 */
public class AtomicAction {

	private final static Logger log = LoggerFactory.getLogger(AtomicAction.class);
	
	private String[] infoList;

	private String type;

	/**
	 * 存储解析后的表达式
	 * 
	 */
	public void setScopeAction(String[] cOrpId) {
		this.infoList = cOrpId;
	}

	/**
	 * 根据传入的商品或分类Id判断返回结果
	 * 
	 * @return
	 */
	public Boolean isOnScope(String paramId) {
		Boolean flag = false;
		if (null == infoList || infoList.length < 1) {
			flag = true;
		} else {
			for (String id : infoList) {
				if (id.equals(paramId)) {
					return true;
				}
			}
		}
		return flag;
	}
	/**
	 * 根据传入的商品或分类Id判断返回结果
	 * 
	 * @return
	 */
	public Boolean isOnCustomScope(String paramId) {
		Boolean flag = false;
		if (paramId==null || paramId.isEmpty()==true)
			return flag;
		if (null == infoList || infoList.length < 1) {
			flag = true;
		} else 
		{	
			if (ScopeType.EXP_CUSTOM_MEM.equals(this.getType()) || ScopeType.EXP_CUSTOM_PID.equals(this.getType())) {
				for (String id : infoList) {
					log.info(String.format("自定义%2$s过滤器%1$s开始加载！",id,(ScopeType.EXP_CUSTOM_MEM.equals(this.getType())?"会员":"商品")));
					List<Long> listIds = SdkCustomizeFilterLoader.load(id);
					log.info(String.format("自定义%2$s过滤器%1$s加载成功！",id,(ScopeType.EXP_CUSTOM_MEM.equals(this.getType())?"会员":"商品")));
					if (null == listIds || listIds.size()<0)
						return flag;
					if (listIds.contains(Long.valueOf(paramId))) {
						return true;
					}
				}
			}
		}
		return flag;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
