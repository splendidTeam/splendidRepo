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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
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
import com.baozun.nebula.dao.freight.SupportedAreaDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.freight.command.ExpSupportedAreaCommand;
import com.baozun.nebula.freight.command.ImpSupportedAreaCommand;
import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.model.freight.DistributionMode;
import com.baozun.nebula.model.freight.SupportedArea;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.SdkFreightManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;

/**
 * @author jumbo
 *
 */
@Transactional
@Service("distributionManager")
public class DistributionManagerImpl implements DistributionManager{

	@Autowired
	private LogisticsManager logisticsManager;
	
	@Autowired
	private SdkFreightManager sdkFreightManager;
	
	@Autowired
	private DistributionModeDao distributionModeDao;
	
	@Autowired
	private SupportedAreaDao supportedAreaDao;
	
	@Autowired
	private ExcelManipulatorFactory excelFactory;
	
	private static final String		WHITE_TYPE_CH							= "白名单";
	private static final String		BLACK_TYPE_CH						    = "黑名单";
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.freight.DistributionManager#saveOrUpdateDistributionMode(com.baozun.nebula.model.freight.DistributionMode)
	 */
	@Override
	public DistributionModeCommand saveOrUpdateDistributionMode(DistributionModeCommand model) {
		DistributionMode dbModel = distributionModeDao.findDistributionModeByName(model.getName());
		if (Validator.isNotNullOrEmpty(dbModel) && (!dbModel.getId().equals(model.getId()))) // 名称重复
			throw new BusinessException(ErrorCodes.DISTRIBUTION_REPEATED_NAME);
		
		if(Validator.isNotNullOrEmpty(model.getId())){//修改
			boolean temp = logisticsManager.updateDistributionMode(model);
			if(temp){
				return model;
			}
			return null;
		}else{
			//保存
			DistributionModeCommand command = logisticsManager.saveDistributionMode(model);
			return command;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.freight.DistributionManager#findSupportedAreaByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
	 */
	@Override
	public Pagination<SupportedAreaCommand> findSupportedAreaByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> searchParam) {
		Pagination<SupportedAreaCommand> command = sdkFreightManager.findSupportedAreaByQueryMapWithPage(page, sorts, searchParam);
		if(command !=null){
			List<SupportedAreaCommand> areaList=command.getItems();
	        for(SupportedAreaCommand supportedArea:areaList){ 
	        	supportedArea.setArea(AddressUtils.getFullAddressName(Long.parseLong(supportedArea.getAreaId()),AddressUtils.SYMBOL_ARROW));
	        } 
		} 
		return command;
	}

	
	@Override
	public void removeDistributionModeByIds(List<Long> ids) {
		distributionModeDao.deleteAllByPrimaryKey(ids);
		
	}

	@Override
	public List<DistributionMode> getDistributionModeListByTemplateId(
			Long templateId) {
		return distributionModeDao.getDistributionModeListByTemplateId(templateId);
	}

	@Override
	public void importSupportedAreaFile(InputStream is, Long distributionId)
	{
		BusinessException topE = null, currE = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<ImpSupportedAreaCommand> list = new ArrayList<ImpSupportedAreaCommand>();
		map.put("impSupportedAreaList", list);
		ExcelReader reader = excelFactory.createExcelReader("supportedAreaUpload");
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

		dataValidateAndSave(list, distributionId);
	}
	
	/**
	 * 验证并保存
	 * 保存时清空旧的数据
	 */
	public void dataValidateAndSave(List<ImpSupportedAreaCommand> list, Long distributionId) {
		
		int areaCosIndex = 0;
		int areaRowIndex = 3;
		Long groupNo = 1L;
		Long groupNoValue = 1L;
		if (Validator.isNotNullOrEmpty(list)) {
			List<SupportedAreaCommand> areaList = new ArrayList<SupportedAreaCommand>();
			
			for (ImpSupportedAreaCommand cmd : list) {
				areaCosIndex = 0;
				String pname = cmd.getProvince();
				if (StringUtils.isNotBlank(pname)) {
					SupportedAreaCommand supportedarea = new SupportedAreaCommand();
					
					//1地址验证 ,只能一个（上海市-闸北区  “-”分割）
					
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
									1, ExcelUtil.getCellIndex(areaRowIndex, areaCosIndex)});
							throw e;
						}
						areaCosIndex++;
						
						String city = cmd.getCity();
						address = checkAddress(address, city, areaRowIndex, areaCosIndex);
						areaCosIndex++;
						
						String county = cmd.getCounty();
						address = checkAddress(address, county, areaRowIndex, areaCosIndex);
						areaCosIndex++;
						
						String town = cmd.getTown();
						address = checkAddress(address, town, areaRowIndex, areaCosIndex);
						
					}
					if(Validator.isNullOrEmpty( address)){
						BusinessException e = new BusinessException(ErrorCodes.IMPORT_FEE_DEST_AREA_NOT_EXIST, new Object[] {
								1, ExcelUtil.getCellIndex(areaRowIndex, areaCosIndex)});
						throw e;
					}
					supportedarea.setAreaId(address.getId().toString());
					areaCosIndex++;
					//2名单验证
					String sType = cmd.getType().trim();
					if(WHITE_TYPE_CH.equals(sType)){
						supportedarea.setType(SupportedArea.WHITE_TYPE);
						//3分组
						groupNo = System.currentTimeMillis() + groupNoValue;
						groupNoValue++;
					}else if(BLACK_TYPE_CH.equals(sType)){
						supportedarea.setType(SupportedArea.BLACK_TYPE);
					}else{
						BusinessException e = new BusinessException(ErrorCodes.IMPORT_AREA_TYPE_ERROR, new Object[] {
								1, ExcelUtil.getCellIndex(areaRowIndex, areaCosIndex)});
						throw e;
					}
					
					if(groupNo == 1){
						BusinessException e = new BusinessException(ErrorCodes.IMPORT_AREA_GROUPNO_IS_NULL, new Object[] {
								1, areaRowIndex});
						throw e;
					}
					supportedarea.setGroupNo(groupNo);
					supportedarea.setDistributionModeId(distributionId);
					
					//4地址重复验证
					for(SupportedAreaCommand f1 :areaList){
						if(f1.getAreaId().equals(supportedarea.getAreaId())){
							BusinessException e = new BusinessException(ErrorCodes.IMPORT_AREA_SUPPORTEDAREA_SAME, new Object[] {
									1, areaRowIndex});
							throw e;
						}
					}
					
					areaList.add(supportedarea);
					areaRowIndex++;
				}
			}
			saveImpAreaCommand(areaList , distributionId);
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
	 * 保存区域信息
	 */
	private void saveImpAreaCommand(List<SupportedAreaCommand> areaList,Long distributionId) {
		
		if (Validator.isNotNullOrEmpty(areaList)) {
			//删除旧数据
			supportedAreaDao.deleteSupportedAreasByDistributionId(distributionId);
			
			List<SupportedAreaCommand> savedList = new ArrayList<SupportedAreaCommand>();
			
			for (SupportedAreaCommand cmd : areaList) {
				SupportedArea area = new SupportedArea();
				area = (SupportedArea) ConvertUtils.convertTwoObject(area, cmd);
				area = supportedAreaDao.save(area);
				
				if (null != area) {
					SupportedAreaCommand savedCmd = new SupportedAreaCommand();
					savedCmd = (SupportedAreaCommand) ConvertUtils.convertTwoObject(savedCmd, area);
					savedList.add(savedCmd);
				}
			}
			// 如果 保存的 和 传入的 list size 不同 ，则说明保存异常
			if (savedList.size() != areaList.size()) {
				throw new BusinessException(Constants.SAVE_SHIPPING_CONFIG_FAILURE);
			}
		}
	}

	@Override
	public List<ExpSupportedAreaCommand> exportSupportedAreasByDistributionId(
			Long distributionId) {
		List<SupportedAreaCommand> areaList = supportedAreaDao.findSupportedAreasByDistributionId(distributionId);
		List<ExpSupportedAreaCommand> impAreaList = new ArrayList<ExpSupportedAreaCommand>();
		for(SupportedAreaCommand cmd : areaList){
			cmd.setArea(AddressUtils.getFullAddressName(Long.parseLong(cmd.getAreaId()),AddressUtils.SYMBOL_LINE));
			if(SupportedArea.WHITE_TYPE.equals(cmd.getType())){
				cmd.setType(WHITE_TYPE_CH);
			}else if(SupportedArea.BLACK_TYPE.equals(cmd.getType())){
				cmd.setType(BLACK_TYPE_CH);
			}
			ExpSupportedAreaCommand impArea = new ExpSupportedAreaCommand();
			impArea = (ExpSupportedAreaCommand) ConvertUtils.convertTwoObject(impArea, cmd);
			String[] strs = cmd.getArea().split(AddressUtils.SYMBOL_LINE);
			if(Validator.isNotNullOrEmpty(strs)){
				if(strs.length >=1 &&Validator.isNotNullOrEmpty(strs[0])){
					impArea.setProvince(strs[0]);
					if(strs.length >=2){
						impArea.setCity(strs[1]);
						if(strs.length >=3){
							impArea.setCounty(strs[2]);
							if(strs.length >=4){
								impArea.setTown(strs[3]);
							}
						}
					}
				}
			}
			impAreaList.add(impArea);
		}
		return impAreaList;
	}

	@Override
	public List<SupportedAreaCommand> findSupportedAreasByDistributionId(
			Long distributionId) {
		return supportedAreaDao.findSupportedAreasByDistributionId(distributionId);
	}

	@Override
	public List<DistributionMode> getAllDistributionModeList() {
		
		return distributionModeDao.getAllDistributionModeList();
	}
}
