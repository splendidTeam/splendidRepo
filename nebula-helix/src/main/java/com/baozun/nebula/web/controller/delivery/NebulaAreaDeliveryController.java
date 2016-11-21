/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-4
 */
package com.baozun.nebula.web.controller.delivery;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.model.delivery.AreaDeliveryMode;
import com.baozun.nebula.model.delivery.DeliveryArea;
import com.baozun.nebula.sdk.manager.SdkAreaDeliveryModeManager;
import com.baozun.nebula.sdk.manager.SdkDeliveryAreaManager;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.delivery.viewcommand.DeliveryAreaViewCommand;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;

/**
 * @Description 三级下拉省市区，物流配送控制层
 * {@link #supportDistributionInfo(String areaCode,Model model)}按三级下拉省市区code判断该地址支持物流配送方式情况
 * 
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-4
 */
public class NebulaAreaDeliveryController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NebulaAreaDeliveryController.class);

	@Autowired
	private SdkAreaDeliveryModeManager areaModeManager;

	@Autowired
	private SdkDeliveryAreaManager deliveryareaManager;

	/**
	 * 
	 * @Description 判断地址区域是否支持当日达，次日达等物流配送方式
	 * @RequestMapping(value = "/supportDistributionInfo.json", method = RequestMethod.GET)
	 * @param areaCode
	 * @param model
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-7
	 */
	public DefaultReturnResult supportDistributionInfo(Long areaCode,
			Model model) {
		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
		defaultReturnResult.setResult(true);
		judgeDelivery(areaCode, defaultReturnResult);
		return defaultReturnResult;
	}

	// 判断是否支持当日达和COD
	private void judgeDelivery(Long areaCode, DefaultReturnResult defaultReturnResult) {
		boolean flag = false;
		DeliveryArea area = deliveryareaManager.findEnableDeliveryAreaByCode(areaCode+"");
		DeliveryAreaViewCommand areaViewCommand = (DeliveryAreaViewCommand) ConvertUtils
				.convertTwoObject(new DeliveryAreaViewCommand(), area);
		if (Validator.isNotNullOrEmpty(area)) {
			AreaDeliveryMode areaMode = areaModeManager.findAreaDeliveryModeByAreaId(area.getId());
			if (null != areaMode) {
				areaViewCommand = (DeliveryAreaViewCommand) ConvertUtils.convertTwoObject(areaViewCommand, areaMode);
				defaultReturnResult.setReturnObject(areaMode);
				String dateFormat = "HH:mm:ss";
				Date now = DateUtil.toDate(DateUtil.toString(new Date(), dateFormat), dateFormat);
				if (AreaDeliveryMode.YES.equals(areaViewCommand.getFirstDayDelivery())
						&& Validator.isNotNullOrEmpty(areaViewCommand.getFirstDeliveryStartTime())
						&& Validator.isNotNullOrEmpty(areaViewCommand.getFirstDeliveryEndTime())) {
					// 时间是否允许
					Date beginTime = DateUtil.toDate(areaViewCommand.getFirstDeliveryStartTime(),dateFormat);
					Date endTime = DateUtil.toDate(areaViewCommand.getFirstDeliveryEndTime(),dateFormat);
					flag = DateUtil.isInTime(now, beginTime, endTime);
					defaultReturnResult.setResult(flag);

				}

				// 是否支持次日达
				if (AreaDeliveryMode.YES.equals(areaViewCommand.getSecondDayDelivery())
						&& Validator.isNotNullOrEmpty(areaViewCommand.getSecondDeliveryStartTime())
						&& Validator.isNotNullOrEmpty(areaViewCommand.getSecondDeliveryEndTime())) {
					// 时间是否允许
					Date beginTime = DateUtil.toDate(areaViewCommand.getSecondDeliveryStartTime(),dateFormat);
					Date endTime = DateUtil.toDate(areaViewCommand.getSecondDeliveryEndTime(),dateFormat);
					flag = DateUtil.isInTime(now, beginTime, endTime);
					defaultReturnResult.setResult(flag);
				}

				if (AreaDeliveryMode.YES.equals(areaViewCommand.getThirdDayDelivery())
						&& Validator.isNotNullOrEmpty(areaViewCommand.getThirdDeliveryStartTime())
						&& Validator.isNotNullOrEmpty(areaViewCommand.getThirdDeliveryEndTime())) {
					// 时间是否允许
					Date beginTime = DateUtil.toDate(areaViewCommand.getThirdDeliveryStartTime(),dateFormat);
					Date endTime = DateUtil.toDate(areaViewCommand.getThirdDeliveryEndTime(),dateFormat);
					flag = DateUtil.isInTime(now, beginTime, endTime);
					defaultReturnResult.setResult(flag);
				}
				defaultReturnResult.setReturnObject(areaViewCommand);
			} else {
				defaultReturnResult.setResult(false);
				defaultReturnResult.setStatusCode("deliveryArea.has.not.areamode");
			}
		} else {
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("deliveryArea.not.exist");
		}
	}
	
}
