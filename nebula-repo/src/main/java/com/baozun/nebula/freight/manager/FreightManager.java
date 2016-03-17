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
package com.baozun.nebula.freight.manager;

import java.util.List;

import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;

/**
 * 
 * 运费信息管理Manager 
 * 
 * 每个方法不仅会操作数据库（ 通过LogisticsManager），而且在操作数据库成功之后 通过zooKeeper 通知其他结点 修改 内存中的数据
 * 此Manager 实现类 无 transcational注解 因为调用的 LogisticsManager 有
 * @author Tianlong.Zhang
 *
 */
public interface FreightManager extends BaseManager{
//********************* 运费模板
	
	/**
	 * 新增运费模板
	 * @param shippingTemeplate
	 * @return
	 */
	ShippingTemeplateCommand saveShippingTemeplate(ShippingTemeplateCommand shippingTemeplate); 
	
	/**
	 * 根据运费模板Id 查询模板数据
	 * @param temeplateId
	 * @return
	 */
	ShippingTemeplateCommand findShippingTemeplateCommandById(Long temeplateId);
	
	/**
	 * 删除运费模板
	 * @param temeplate
	 * @return  true  或者BusinessException  如果该模板Id 还和其他商品有关联的话
	 */
	boolean removeShippingTemeplate(Long temeplate);
	
	/**
	 * 更新运费模板
	 * @param shippingTemeplateCmd
	 * @return
	 */
	boolean updateShippingTemeplate(ShippingTemeplateCommand shippingTemeplateCmd);
	
	
	//************************************* 支持的区域
	
	/**
	 * 增加物流方式支持的区域
	 * @param supportedAreaCommandList
	 * @return
	 */
	List<SupportedAreaCommand> saveSupportedAreas(List<SupportedAreaCommand> supportedAreaCommandList);
	
	// 不提供 单个 支持的物流区域的方法。   修改一个支持的物流区域 没有意义。  因为只能 修改 areaId 或者代称。这样在物理意义上其实对应了删除和新增。
//	boolean updateSupportedArea(SupportedAreaCommand supportedAreaCommand);
	
	/**
	 * 删除物流方式支持的区域
	 * @param ids
	 * @return
	 */
	boolean deleteSupportedAreas(List<Long> ids);
	
	//****************************** 物流方式
	
	/**
	 * 增加物流方式
	 * @param cmd
	 * @return
	 */
	DistributionModeCommand saveDistributionMode(DistributionModeCommand cmd);
	
	/**
	 * 删除物流方式
	 * @param distributionModeId
	 * @return
	 */
	boolean deleteDistributionMode(Long distributionModeId);
	
	/**
	 * 修改物流方式
	 * @param cmd
	 * @return
	 */
	boolean updateDistributionMode(DistributionModeCommand cmd);
}
