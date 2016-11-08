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
import com.baozun.nebula.sdk.manager.delivery.SdkAreaDeliveryModeManager;
import com.baozun.nebula.sdk.manager.delivery.SdkDeliveryAreaManager;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.delivery.viewcommand.DeliveryAreaViewCommand;
import com.feilong.core.Validator;

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
	public DefaultReturnResult supportDistributionInfo(String areaCode,
			Model model) {
		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
		defaultReturnResult.setResult(true);
		judgeDelivery(areaCode, defaultReturnResult);
		return defaultReturnResult;
	}

	// 判断是否支持当日达和COD
	private void judgeDelivery(String areaCode,
			DefaultReturnResult defaultReturnResult) {
		boolean flag = false;
		DeliveryArea area = deliveryareaManager.findEnableDeliveryAreaByCode(areaCode);
		DeliveryAreaViewCommand areaViewCommand = (DeliveryAreaViewCommand) ConvertUtils
				.convertTwoObject(new DeliveryAreaViewCommand(), area);
		if (Validator.isNotNullOrEmpty(area)) {
			AreaDeliveryMode areaMode = areaModeManager
					.findAreaDeliveryModeByAreaId(area.getId());
			if (null != areaMode) {
				areaViewCommand = (DeliveryAreaViewCommand) ConvertUtils
						.convertTwoObject(areaViewCommand, areaMode);
				defaultReturnResult.setReturnObject(areaMode);
				DateFormat df = new SimpleDateFormat("HH:mm:ss");
				Date now = new Date();
				try {
					now = df.parse(df.format(now));
					if (areaViewCommand.getFirstDayDelivery().equals(
							AreaDeliveryMode.YES)
							&& Validator.isNotNullOrEmpty(areaViewCommand
									.getFirstDeliveryStartTime())
							&& Validator.isNotNullOrEmpty(areaViewCommand
									.getFirstDeliveryEndTime())) {
						// 时间是否允许
						now = df.parse(df.format(now));
						flag = (now.before(df.parse(areaMode
								.getFirstDeliveryEndTime())))
								&& (now.after(df.parse(areaMode
										.getFirstDeliveryStartTime())));
						defaultReturnResult.setResult(flag);

					}

					// 是否支持次日达
					if (areaMode.getSecondDayDelivery().equals(
							AreaDeliveryMode.YES)
							&& Validator.isNotNullOrEmpty(areaMode
									.getSecondDeliveryStartTime())
							&& Validator.isNotNullOrEmpty(areaMode
									.getSecondDeliveryEndTime())) {
						// 时间是否允许
						now = df.parse(df.format(now));
						flag = (now.before(df.parse(areaMode
								.getSecondDeliveryEndTime())))
								&& (now.after(df.parse(areaMode
										.getSecondDeliveryStartTime())));
						defaultReturnResult.setResult(flag);
					}

					if (areaMode.getThirdDayDelivery().equals(
							AreaDeliveryMode.YES)
							&& Validator.isNotNullOrEmpty(areaMode
									.getThirdDeliveryStartTime())
							&& Validator.isNotNullOrEmpty(areaMode
									.getThirdDeliveryEndTime())) {
						// 时间是否允许
						flag = (now.before(df.parse(areaMode
								.getSecondDeliveryEndTime())))
								&& (now.after(df.parse(areaMode
										.getSecondDeliveryStartTime())));
						defaultReturnResult.setResult(flag);
					}
					defaultReturnResult.setReturnObject(areaViewCommand);
				} catch (ParseException e) {
					LOGGER.error("DateFormat Format ERROR !", e);
				}
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
