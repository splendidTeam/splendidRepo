package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;

public interface SdkCustomizeFilterExecuteManager extends BaseManager{
	/**
	 * 自定义商品、会员筛选器接口
	 * 返回时商品Id、会员Id列表
	 * @return
	 */
	public List<Long> execute();
}
