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
 */
package com.baozun.nebula.manager.baseinfo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import loxia.dao.Sort;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.baseinfo.NavigationCommand;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.constants.CacheConstants;
import com.baozun.nebula.constants.MetaInfoConstants;
import com.baozun.nebula.dao.baseinfo.NavigationDao;
import com.baozun.nebula.dao.baseinfo.NavigationLangDao;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.baozun.nebula.model.baseinfo.NavigationLang;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.baozun.nebula.utils.Validator;

/**
 * 
 * @author - 项硕
 */
@Transactional(readOnly = true)
@Service("navigationManager")
public class NavigationManagerImpl implements NavigationManager{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(NavigationManagerImpl.class);
	
	//URL正则表达式
	private static final String REG_URL = "^(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?$";
	
	@Autowired
	private SdkNavigationManager sdkNavigationManager;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private SdkMataInfoManager sdkMataInfoManager;
	@Autowired
	private NavigationDao navigationDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private NavigationLangDao navigationLangDao;
	@Value("#{meta['cache.nav.key']}")
	private String	cacheKeyNavigationList	= "";

	@Deprecated
	@Override
	public List<Map<String, Object>> findNavigationList(HttpServletRequest request) {
		List<Map<String, Object>> rsList = new ArrayList<Map<String,Object>>();
		List<Navigation> list = sdkNavigationManager.findAvailableNavigationList(Navigation.COMMOM_SORTS);
		
		for (Navigation n : list) {
			Map<String, Object> attr = new HashMap<String, Object>();
			attr.put("id", n.getId());
			attr.put("name", n.getName());
			
			String url = n.getUrl();
			
			if (StringUtils.isNotBlank(url) && (! url.matches(REG_URL))) {
				url = request.getContextPath() + url;
			}

			if (Navigation.TYPE_CATEGORY.equals(n.getType())) {	//分类类型
				if (StringUtils.isBlank(n.getUrl())) {	//如果URL为空，则使用默认分类路径
					attr.put("url", request.getContextPath() + "/assignUrl/" + n.getParam());
				} else {	//如果URL不为空，则使用该URL
					attr.put("url", url);
				}
			} else {	//URL类型
				attr.put("url", n.getUrl());
			}
			attr.put("sortno", n.getSort());
			attr.put("isnew", n.getIsNewWin());
			attr.put("parentid", n.getParentId());
			
			rsList.add(attr);
		}
		return rsList;
	}
	
	/**
	 * @see com.baozun.nebula.manager.baseinfo.NavigationManager#findStoreNavigation()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<NavigationCommand> findStoreNavigation() {
		List<NavigationCommand> result = null;
		cacheKeyNavigationList = Validator.isNullOrEmpty(cacheKeyNavigationList) ? CacheConstants.CACHE_KEY_NAVIGATION_LIST : cacheKeyNavigationList;
		// ①走cache
		try {
			result = cacheManager
					.getObject(cacheKeyNavigationList);
			if (CollectionUtils.isNotEmpty(result)) {
				return result;
			}
		} catch (Exception e) {
			log.warn(String.format("从Redis缓存中取值异常(kye = %s)",
					cacheKeyNavigationList), e);
		}

		// ②取DB，assemble navigation
		String rootNavIdStr = sdkMataInfoManager
				.findValue(MetaInfoConstants.MATA_KEY_NAVIGATION_ROOT_ID);
		if (Validator.isNullOrEmpty(rootNavIdStr)) {
			log.warn(String.format("配置参数“%s”的值为空",
					MetaInfoConstants.MATA_KEY_NAVIGATION_ROOT_ID));
			return null;
		}
		Long rootNavId = Long.valueOf(rootNavIdStr);

		Sort[] sorts = Sort.parse("parent_id asc ,sort asc");
		List<Navigation> navigationlist = navigationDao
				.findAvailableNavigationList(sorts);
		if (Validator.isNullOrEmpty(navigationlist)) {
			log.warn("导航数据为空");
			return null;
		}

		// Map<id, Category>
		Map<Long, Category> categoryMap = findCategoryByCategoryNavigation(navigationlist);
		result = buildSubNavTrees(navigationlist, rootNavId, "", categoryMap);

		// ③存cache
		try {
			cacheManager.setObject(cacheKeyNavigationList,
					result, TimeInterval.SECONDS_PER_HOUR);
		} catch (Exception e) {
			log.warn(String.format("设置Redis缓存异常(kye = %s)",
					cacheKeyNavigationList), e);
		}

		return result;
	}

	/**
	 * 查找“分类”类型的导航所对应的Category (category.id = navigation.param where
	 * navigation.type = 2)
	 * 
	 * @param navigations
	 * @return Map<id, Category>
	 */
	private Map<Long, Category> findCategoryByCategoryNavigation(
			List<Navigation> navigations) {

		List<Long> categoryIds = new ArrayList<Long>();
		for (Navigation navigation : navigations) {
			if (Navigation.TYPE_CATEGORY.equals(navigation.getType())) {
				categoryIds.add(navigation.getParam());
			}
		}
		List<Category> categorys = categoryDao
				.findCategoryListByCategoryIds(categoryIds
						.toArray(new Long[categoryIds.size()]));

		Map<Long, Category> categoryMap = new HashMap<Long, Category>();
		for (Category category : categorys) {
			categoryMap.put(category.getId(), category);
		}

		return categoryMap;
	}

	/**
	 * 封装command
	 * 
	 * @param navigations
	 *            有效的导航数据记录
	 * @param pId
	 *            构造该节点下一级的导航树林
	 * @param currentPath
	 *            该节点由分类code构成的导航路径，‘/’分割，
	 * @param categoryMap
	 *            “分类”类型导航多对应的category，Map<id, category>
	 * @return
	 */
	private NavigationCommand buildCommand(Navigation navigation, Long pId,
			String currentPath, Map<Long, Category> categoryMap) {
		NavigationCommand command = new NavigationCommand();
		command.setId(navigation.getId());
		command.setParentId(pId);
		command.setSelf(navigation);
		// 分类类型导航的URL拼接符
		String boundsymbolStr = sdkMataInfoManager
				.findValue(MetaInfoConstants.MATA_KEY_CATEGORY_NAVIGATION_BOUND_SYMBOL);
		if (Validator.isNullOrEmpty(boundsymbolStr)) {
			boundsymbolStr = "-";
		}
		// 菜单导航的URL拼接模板
		String urlTemplateStr = sdkMataInfoManager
				.findValue(MetaInfoConstants.MATA_KEY_NAVIGATION_URL_TEMPLATE);
		if (Validator.isNullOrEmpty(urlTemplateStr)) {
			urlTemplateStr = "/{category-part}/category.htm";
		}
		// 菜单导航的root顶级接节点id
		String rootNavIdStr = sdkMataInfoManager
				.findValue(MetaInfoConstants.MATA_KEY_NAVIGATION_ROOT_ID);
		
		if (Navigation.TYPE_CATEGORY.equals(navigation.getType())) {
			if (rootNavIdStr.equals(String.valueOf(pId))) {
				// 拼接分类path
				command.setPath(currentPath + "/"
						+ categoryMap.get(navigation.getParam()).getCode());
			} else {
				command.setPath(currentPath + boundsymbolStr
						+ categoryMap.get(navigation.getParam()).getCode());
			}

			if (StringUtils.isBlank(navigation.getUrl())
					|| "''".equals(navigation.getUrl())) {
				command.setUrl(urlTemplateStr.replace("/{category-part}", command.getPath()));
			} else {
				command.setUrl(navigation.getUrl());
			}
		} else {
			command.setUrl(navigation.getUrl());
		}
		setI18nNameFieldOfCommand(command, navigation);

		return command;
	}

	private void setI18nNameFieldOfCommand(
			NavigationCommand navigationCommand, Navigation navigation) {
		boolean i18n = LangProperty.getI18nOnOff();
		if (i18n) {
			List<NavigationLang> navigationLangs = navigationLangDao
					.findNavigationLangListByNavidAndLangs(navigation.getId(),
							MutlLang.i18nLangs());
			if (Validator.isNotNullOrEmpty(navigationLangs)) {
				String[] values = new String[MutlLang.i18nSize()];
				String[] langs = new String[MutlLang.i18nSize()];
				for (int i = 0; i < navigationLangs.size(); i++) {
					NavigationLang navigationLang = navigationLangs.get(i);
					values[i] = navigationLang.getName();
					langs[i] = navigationLang.getLang();
				}
				MutlLang mutlLang = new MutlLang();
				mutlLang.setValues(values);
				mutlLang.setLangs(langs);
				navigationCommand.setName(mutlLang);
			}
		} else {
			SingleLang singleLang = new SingleLang();
			singleLang.setValue(navigation.getName());
			navigationCommand.setName(singleLang);
		}
	}

	/**
	 * 构造下层导航菜单
	 * 
	 * @param navigations
	 *            有效的导航数据记录
	 * @param pId
	 *            构造该节点下一级的导航树林
	 * @param currentPath
	 *            该节点由分类code构成的导航路径，‘/’分割，
	 * @param categoryMap
	 *            “分类”类型导航多对应的category，Map<id, category>
	 * @return
	 */
	private List<NavigationCommand> buildSubNavigationCommands(
			List<Navigation> navigations, Long pId, String currentPath,
			Map<Long, Category> categoryMap) {

		List<NavigationCommand> result = new ArrayList<NavigationCommand>();

		for (Navigation navigation : navigations) {
			if (navigation.getParentId().equals(pId)) {

				NavigationCommand command = buildCommand(navigation, pId,
						currentPath, categoryMap);
				result.add(command);
			}
		}

		return result;
	}

	/**
	 * (递归)构造navId下一级的导航树林
	 * 
	 * @param navigations
	 *            有效的导航数据记录
	 * @param navId
	 *            构造该节点下一级的导航树林
	 * @param currentPath
	 *            该节点由分类code构成的导航路径，分割符可以从MetaInfo（MATA_KEY_CATEGORY_NAVIGATION_BOUND_SYMBOL）取得，默认为‘-’，
	 * @param categoryMap
	 *            “分类”类型导航多对应的category，Map<id, category>
	 * @return 导航command树林
	 */
	private List<NavigationCommand> buildSubNavTrees(
			List<Navigation> navigations, Long navId, String currentPath,
			Map<Long, Category> categoryMap) {

		// 构造下层导航菜单
		List<NavigationCommand> navCommands = buildSubNavigationCommands(
				navigations, navId, currentPath, categoryMap);

		for (NavigationCommand navCommand : navCommands) {
			List<NavigationCommand> buildSubNavTrees = buildSubNavTrees(
					navigations, navCommand.getId(), navCommand.getPath(),
					categoryMap);
			navCommand.setSubNavigations(buildSubNavTrees);
		}

		return navCommands;
	}

}
