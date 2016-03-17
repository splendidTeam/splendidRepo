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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.solr.utils.Validator;

/**
 * @author Tianlong.Zhang
 *
 */
@Service("freightManager")
public class FreightManagerImpl implements FreightManager{
	
	private LogisticsManager logisticsManager;

	@Override
	public ShippingTemeplateCommand saveShippingTemeplate(
			ShippingTemeplateCommand shippingTemeplate) {
		ShippingTemeplateCommand cmd = logisticsManager.saveShippingTemeplate(shippingTemeplate);
		
		if(cmd!=null){
			//TODO 通知 ShippingTemeplateMap ShopShippingTemeplateMap  shippingFeeConfigMap  变更
			
		}
		return cmd;
	}

	@Override
	@Transactional(readOnly=true)
	public ShippingTemeplateCommand findShippingTemeplateCommandById(
			Long temeplateId) {
		return logisticsManager.findShippingTemeplateCommandById(temeplateId);
	}

	@Override
	public boolean removeShippingTemeplate(Long temeplate) {
		
		boolean result = logisticsManager.removeShippingTemeplate(temeplate);
		if(result){
			//TODO 通知 ShippingTemeplateMap ShopShippingTemeplateMap  shippingFeeConfigMap变更
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.freight.manager.FreightManager#updateShippingTemeplate(com.baozun.nebula.freight.command.ShippingTemeplateCommand)
	 */
	@Override
	public boolean updateShippingTemeplate(
			ShippingTemeplateCommand shippingTemeplateCmd) {
		boolean result = logisticsManager.updateShippingTemeplate(shippingTemeplateCmd);
		if(result){
			//TODO 通知 ShippingTemeplateMap ShopShippingTemeplateMap  shippingFeeConfigMap变更
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.freight.manager.FreightManager#saveSupportedAreas(java.util.List)
	 */
	@Override
	public List<SupportedAreaCommand> saveSupportedAreas(
			List<SupportedAreaCommand> supportedAreaCommandList) {
		List<SupportedAreaCommand> resultList = logisticsManager.saveSupportedAreas(supportedAreaCommandList);
		if(Validator.isNotNullOrEmpty(resultList)){
			// TODO  通知 DistributionCommand 变更
		}
		
		return resultList;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.freight.manager.FreightManager#deleteSupportedAreas(java.util.List)
	 */
	@Override
	public boolean deleteSupportedAreas(List<Long> ids) {
		boolean result = logisticsManager.deleteSupportedAreas(ids);
		if(result){
			// TODO 通知 DistributionCommand 变更
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.freight.manager.FreightManager#saveDistributionMode(com.baozun.nebula.sdk.command.logistics.DistributionModeCommand)
	 */
	@Override
	public DistributionModeCommand saveDistributionMode(
			DistributionModeCommand cmd) {
		DistributionModeCommand savedCmd = logisticsManager.saveDistributionMode(cmd);
		if(savedCmd!=null){
			// TODO 通知 DistributionCommand 变更
		}
		
		return savedCmd;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.freight.manager.FreightManager#deleteDistributionMode(java.lang.Long)
	 */
	@Override
	public boolean deleteDistributionMode(Long distributionModeId) {
		boolean result = logisticsManager.deleteDistributionMode(distributionModeId);
		if(result){
			// TODO 通知 DistributionCommand 变更
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.freight.manager.FreightManager#updateDistributionMode(com.baozun.nebula.sdk.command.logistics.DistributionModeCommand)
	 */
	@Override
	public boolean updateDistributionMode(DistributionModeCommand cmd) {
		boolean result = logisticsManager.updateDistributionMode(cmd);
		if(result){
			// TODO 通知 DistributionCommand 变更
		}
		
		return result;
	}

}
