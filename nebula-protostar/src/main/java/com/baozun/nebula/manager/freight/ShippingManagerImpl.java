/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.freight;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import loxia.support.excel.ExcelKit;
import loxia.support.excel.ExcelManipulatorFactory;
import loxia.support.excel.ExcelReader;
import loxia.support.excel.ExcelUtil;
import loxia.support.excel.ReadStatus;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.AddressUtils;
import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.freight.DistributionModeDao;
import com.baozun.nebula.dao.freight.ShippingFeeConfigDao;
import com.baozun.nebula.dao.freight.ShippingTemeplateDao;
import com.baozun.nebula.dao.freight.TemeplateDistributionModeDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.freight.command.ExpShippingFeeCommand;
import com.baozun.nebula.freight.command.ImpShippingFeeCommand;
import com.baozun.nebula.freight.command.ShippingFeeConfigCommand;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.freight.ShippingFeeConfig;
import com.baozun.nebula.model.freight.TemeplateDistributionMode;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;

/**
 * @author jumbo
 *
 */
@Transactional
@Service("shippingManager")
public class ShippingManagerImpl implements ShippingManager{

	@Autowired
	private ShippingTemeplateDao shippingTemeplateDao;
	
	@Autowired
	private ShippingFeeConfigDao shippingFeeConfigDao;
	
	@Autowired
	private DistributionModeDao distributionModeDao;
	
	@Autowired
	private TemeplateDistributionModeDao temeplateDistributionModeDao;
	
	@Autowired
	private ExcelManipulatorFactory excelFactory;
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.freight.ShippingManager#removeShippingTemplateByIds(java.util.List)
	 */
	@Override
	public void removeShippingTemplateByIds(List<Long> ids) {
		
		shippingTemeplateDao.deleteAllByPrimaryKey(ids);
		
		// 删除 运费配置表中的数据
		shippingFeeConfigDao.deleteShippingFeeConfigsByTemeplateIds(ids);
		
	}

	@Override
	public void importShippingFeeFile(InputStream is,
			Long templateId) {
		
		BusinessException topE = null, currE = null;

		Map<String, Object> map = new HashMap<String, Object>();
		List<ImpShippingFeeCommand> list = new ArrayList<ImpShippingFeeCommand>();
		map.put("impShippingFeeCommand", list);
		
		ExcelReader reader = excelFactory.createExcelReader("shippingUpload");
		ReadStatus status = reader.readSheet(is, 0, map);
		
		if (status.getStatus() != ReadStatus.STATUS_SUCCESS) {
			List<String> messageList = ExcelKit.getInstance().getReadStatusMessages(status, Locale.SIMPLIFIED_CHINESE);
			for (String message : messageList) {
				BusinessException e = new BusinessException(message);
				if (topE == null) {
					topE = e; // b-101 : Cell{}错误, new Object[]{ExcelUtil.getCell(1,2)}
					currE = e;
				} else {
					currE.setLinkedException(e);
					currE = e;
				}

			}
		}
		
		if (topE != null)
			throw topE;

		dataValidateAndSave(list, templateId);
	}
	
	/**
	 * 验证并保存
	 * 保存时清空旧的数据
	 */
	private void dataValidateAndSave(List<ImpShippingFeeCommand> list, Long templateId) {
		
		int feeCosIndex = 0;
		int feeRowIndex = 3;
		if (Validator.isNotNullOrEmpty(list)) {
			List<ShippingFeeConfigCommand> feeList = new ArrayList<ShippingFeeConfigCommand>();
			Map<String,DistributionMode> modeMap = new HashMap<String,DistributionMode>();
			
			for (ImpShippingFeeCommand cmd : list) {
				feeCosIndex = 0;
				String dname = cmd.getDistributionModeName().trim();
				if (StringUtils.isNotBlank(dname)) {
					ShippingFeeConfigCommand fee = new ShippingFeeConfigCommand();
					
					//1物流方式验证
					DistributionMode mode = modeMap.get(dname);
					if(Validator.isNullOrEmpty(mode)){
						mode = distributionModeDao.findDistributionModeByName(dname);
						if(Validator.isNullOrEmpty(mode)){
							BusinessException e = new BusinessException(ErrorCodes.IMPORT_FEE_DISTRIBUTION_IS_NO, new Object[] {
									1, ExcelUtil.getCellIndex(feeRowIndex, feeCosIndex)});
							throw e;
						}else{
							modeMap.put(dname, mode);
						}
					}
					
					fee.setDistributionModeId(mode.getId());
					//2地址验证 ,只能一个（上海市-闸北区  “-”分割）
					feeCosIndex++;
					String province = cmd.getProvince();
					Address address = null;
					if(Validator.isNotNullOrEmpty(province)){
						List<Address> plist = AddressUtil.getProviences();
						for(Address a: plist){
							if(a.getName().equals(province.trim())){
								address = a;
								break;
							}
						}
						if(Validator.isNullOrEmpty(address)){
							BusinessException e = new BusinessException(ErrorCodes.IMPORT_FEE_DEST_AREA_NOT_EXIST, new Object[] {
									1, ExcelUtil.getCellIndex(feeRowIndex, feeCosIndex)});
							throw e;
						}
						feeCosIndex++;
						
						String city = cmd.getCity();
						address = checkAddress(address, city, feeRowIndex, feeCosIndex);
						feeCosIndex++;
						
						String area = cmd.getArea();
						address = checkAddress(address, area, feeRowIndex, feeCosIndex);
						feeCosIndex++;
						
						String town = cmd.getTown();
						address = checkAddress(address, town, feeRowIndex, feeCosIndex);
						
					}
					if(Validator.isNullOrEmpty( address)){
						BusinessException e = new BusinessException(ErrorCodes.IMPORT_FEE_DEST_AREA_NOT_EXIST, new Object[] {
								1, ExcelUtil.getCellIndex(feeRowIndex, feeCosIndex)});
						throw e;
					}
					fee.setDestAreaId(address.getId().toString());
					//todo
					//3运费数字验证//自动验证
					feeCosIndex++;
					fee.setFirstPartUnit(cmd.getFirstPartUnit());
					feeCosIndex++;
					fee.setSubsequentPartUnit(cmd.getSubsequentPartUnit());
					feeCosIndex++;
					fee.setFirstPartPrice(cmd.getFirstPartPrice());
					feeCosIndex++;
					fee.setSubsequentPartPrice(cmd.getSubsequentPartPrice());
					feeCosIndex++;
					fee.setBasePrice(cmd.getBasePrice());
					fee.setShippingTemeplateId(templateId);
					
					//4物流，且地址重复验证
					for(ShippingFeeConfigCommand f1 :feeList){
						if(f1.getDistributionModeId() == fee.getDistributionModeId() &&
								f1.getDestAreaId() == fee.getDestAreaId()){
							BusinessException e = new BusinessException(ErrorCodes.IMPORT_FEE_DISTRIBUTION_AREA_SAME, new Object[] {
									1, ExcelUtil.getCellIndex(feeRowIndex, 0)});
							throw e;
						}
					}
					feeList.add(fee);
					
					feeRowIndex++;
				}
			}
			saveImpFeeCommand(feeList, modeMap, templateId);
		} 
	}
	
	
	private Address checkAddress(Address address, String area, int row, int cos) {
		List<Address> addresslist = null;
		Address add = address;
		if(Validator.isNotNullOrEmpty(address) && Validator.isNotNullOrEmpty(area)){
			add = null;
			addresslist = AddressUtil.getSubAddressByPid(address.getId());
			for(Address a: addresslist){
				if(a.getName().equals(area.trim())){
					add = a;
					break;
				}
			}
			if(Validator.isNullOrEmpty(add)){
				BusinessException e = new BusinessException(ErrorCodes.IMPORT_FEE_DEST_AREA_NOT_EXIST, new Object[] {
						1, ExcelUtil.getCellIndex(row, cos)});
				throw e;
			}
		}
		return add;
	}
	
	/**
	 * 保存运费信息
	 */
	private void saveImpFeeCommand(List<ShippingFeeConfigCommand> feeList,Map<String,DistributionMode> modeMap
			,Long templateId) {
		
		if (Validator.isNotNullOrEmpty(feeList)) {
			//删除旧数据
			shippingFeeConfigDao.deleteShippingFeeConfigsByTemeplateId(templateId);
			//删除物流关系
			temeplateDistributionModeDao.deleteTemeplateDistributionByTemeplateId(templateId);
			
			List<ShippingFeeConfigCommand> savedList = new ArrayList<ShippingFeeConfigCommand>();
			
			for (ShippingFeeConfigCommand configCmd : feeList) {
				ShippingFeeConfig feeConfig = new ShippingFeeConfig();
				feeConfig = (ShippingFeeConfig) ConvertUtils.convertTwoObject(feeConfig, configCmd);
				feeConfig = shippingFeeConfigDao.save(feeConfig);
				
				if (null != feeConfig) {
					ShippingFeeConfigCommand savedConfigCmd = new ShippingFeeConfigCommand();
					savedConfigCmd = (ShippingFeeConfigCommand) ConvertUtils.convertTwoObject(savedConfigCmd, feeConfig);
					savedList.add(savedConfigCmd);
				}
				
			}
			// 如果 保存的 和 传入的 list size 不同 ，则说明保存异常
			if (savedList.size() != feeList.size()) {
				throw new BusinessException(Constants.SAVE_SHIPPING_CONFIG_FAILURE);
			}
			
			//模板物流关系表
			List<TemeplateDistributionMode> tdList = temeplateDistributionModeDao.getDistributionModeByTemeplateId(templateId);
			boolean temp = false;
			for (String key : modeMap.keySet()) {
				temp = false;
				Long dId = modeMap.get(key).getId();
				for(TemeplateDistributionMode tdm :tdList){
					if(tdm.getDistributionModeId().equals(dId)){
						temp = true;
						break;
					}
				}
				if(!temp){
					TemeplateDistributionMode td = new TemeplateDistributionMode();
					td.setDistributionModeId(dId);
					td.setShippingTemeplateId(templateId);
					td.setVersion(new Date());
					temeplateDistributionModeDao.save(td);
				}
			}
		}
	}

	@Override
	public List<ExpShippingFeeCommand> exportShippingFeeConfigCommandList(
			Long templateId) {
		List<ShippingFeeConfigCommand> configList = shippingFeeConfigDao.findShippingFeeConfigsByTemeplateId(templateId);
		List<ExpShippingFeeCommand> impFeeList = new ArrayList<ExpShippingFeeCommand>();
		for(ShippingFeeConfigCommand cmd: configList){
			cmd.setDestAreaName(AddressUtils.getFullAddressName(Long.parseLong(cmd.getDestAreaId()),AddressUtils.SYMBOL_LINE));
			ExpShippingFeeCommand  impFee = new ExpShippingFeeCommand();
			impFee.setFirstPartPrice(
					Validator.isNotNullOrEmpty(cmd.getFirstPartPrice())?cmd.getFirstPartPrice().toString():null);
			impFee.setFirstPartUnit(
					Validator.isNotNullOrEmpty(cmd.getFirstPartUnit())? cmd.getFirstPartUnit().toString():null);
			impFee.setSubsequentPartPrice(
					Validator.isNotNullOrEmpty(cmd.getSubsequentPartPrice())?cmd.getSubsequentPartPrice().toString():null);
			impFee.setSubsequentPartUnit(
					Validator.isNotNullOrEmpty(cmd.getSubsequentPartUnit())?cmd.getSubsequentPartUnit().toString():null);
			impFee.setDistributionModeName(cmd.getDistributionModeName());
			
			String[] strs = cmd.getDestAreaName().split(AddressUtils.SYMBOL_LINE);
			if(Validator.isNotNullOrEmpty(strs)){
				if(strs.length >=1 &&Validator.isNotNullOrEmpty(strs[0])){
					impFee.setProvince(strs[0]);
					if(strs.length >=2){
						impFee.setCity(strs[1]);
						if(strs.length >=3){
							impFee.setArea(strs[2]);
							if(strs.length >=4){
								impFee.setTown(strs[3]);
							}
						}
					}
				}
			}
			impFeeList.add(impFee);
		}
		return impFeeList;
	}

}
