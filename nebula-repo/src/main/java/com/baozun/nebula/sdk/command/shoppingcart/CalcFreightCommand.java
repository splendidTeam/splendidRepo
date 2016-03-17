package com.baozun.nebula.sdk.command.shoppingcart;

import com.baozun.nebula.command.Command;

/**
 * 计算运费需要的信息
 * @author 阳羽
 * @createtime 2014-4-3 上午09:49:01
 */
public class CalcFreightCommand  implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -330679101726834533L;

	// 物流方式Id
	private  Long distributionModeId;
	
	//省id
	private Long  provienceId;
	
	//市id
	private Long cityId;
	
	//县id
	private Long countyId;
	
	// 乡id
	private Long townId;

	public CalcFreightCommand() {
		super();
	}

	public CalcFreightCommand(Long distributionModeId, Long provienceId,
			Long cityId, Long countyId, Long townId) {
		super();
		this.distributionModeId = distributionModeId;
		this.provienceId = provienceId;
		this.cityId = cityId;
		this.countyId = countyId;
		this.townId = townId;
	}

	public Long getDistributionModeId() {
		return distributionModeId;
	}

	public void setDistributionModeId(Long distributionModeId) {
		this.distributionModeId = distributionModeId;
	}

	public Long getProvienceId() {
		return provienceId;
	}

	public void setProvienceId(Long provienceId) {
		this.provienceId = provienceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}
}
