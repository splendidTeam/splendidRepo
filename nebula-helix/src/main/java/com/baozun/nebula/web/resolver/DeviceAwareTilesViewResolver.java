package com.baozun.nebula.web.resolver;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.mobile.device.util.ResolverUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

public class DeviceAwareTilesViewResolver extends TilesViewResolver {

	// rewrite tiles view name for different web client
	private String normalPrefix = "";
	private String mobilePrefix = "";
	private String tabletPrefix = "";
	private String commonPrefix = "";
	private String normalSuffix = "";
	private String mobileSuffix = "";
	private String tabletSuffix = "";
	private String commonSuffix = "";

	public String getCommonPrefix() {
		return commonPrefix;
	}

	public void setCommonPrefix(String commonPrefix) {
		this.commonPrefix = commonPrefix;
	}

	public String getCommonSuffix() {
		return commonSuffix;
	}

	public void setCommonSuffix(String commonSuffix) {
		this.commonSuffix = commonSuffix;
	}

	public String getNormalPrefix() {
		return normalPrefix;
	}

	public void setNormalPrefix(String normalPrefix) {
		this.normalPrefix = normalPrefix;
	}

	public String getMobilePrefix() {
		return mobilePrefix;
	}

	public void setMobilePrefix(String mobilePrefix) {
		this.mobilePrefix = mobilePrefix;
	}

	public String getTabletPrefix() {
		return tabletPrefix;
	}

	public void setTabletPrefix(String tabletPrefix) {
		this.tabletPrefix = tabletPrefix;
	}

	public String getNormalSuffix() {
		return normalSuffix;
	}

	public void setNormalSuffix(String normalSuffix) {
		this.normalSuffix = normalSuffix;
	}

	public String getMobileSuffix() {
		return mobileSuffix;
	}

	public void setMobileSuffix(String mobileSuffix) {
		this.mobileSuffix = mobileSuffix;
	}

	public String getTabletSuffix() {
		return tabletSuffix;
	}

	public void setTabletSuffix(String tabletSuffix) {
		this.tabletSuffix = tabletSuffix;
	}

	/**
	 * rewrite viewName for different web client
	 * 
	 * @see org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver
	 * 
	 * @param viewName
	 * @return
	 */
	protected String getDeviceViewNameInternal(String viewName) {
		// 以下特殊格式的 viewName 无需适配客户端平台
		// 以“redict:”“forward:”或配置的“commonPrefix”开头；或以配置的“commonSuffix”结尾；
		if (viewName.startsWith(REDIRECT_URL_PREFIX)
				|| viewName.startsWith(FORWARD_URL_PREFIX)) {
			return viewName;
		} else if (StringUtils.isNotBlank(commonPrefix)
				&& viewName.startsWith(commonPrefix)) {
			return viewName;
		} else if (StringUtils.isNotBlank(commonSuffix)
				&& viewName.endsWith(commonSuffix)) {
			return viewName;
		}

		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
		HttpServletRequest request = ((ServletRequestAttributes) attrs)
				.getRequest();
		Device device = DeviceUtils.getCurrentDevice(request);
		SitePreference sitePreference = SitePreferenceUtils
				.getCurrentSitePreference(request);
		String resolvedViewName = viewName;

		if (ResolverUtils.isNormal(device, sitePreference)) {
			resolvedViewName = getNormalPrefix() + viewName + getNormalSuffix();
		} else if (ResolverUtils.isMobile(device, sitePreference)) {
			resolvedViewName = getMobilePrefix() + viewName + getMobileSuffix();
		} else if (ResolverUtils.isTablet(device, sitePreference)) {
			resolvedViewName = getTabletPrefix() + viewName + getTabletSuffix();
		}

		return resolvedViewName;
	}

	@Override
	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		logger.debug("tiles view name： " + viewName);
		String deviceViewName = getDeviceViewNameInternal(viewName);
		logger.debug("tiles view name - 侦测平台类型后调整为： " + deviceViewName);

		return super.resolveViewName(deviceViewName, locale);
	}

}

