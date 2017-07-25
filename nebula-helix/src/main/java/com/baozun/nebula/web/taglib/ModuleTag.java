

package com.baozun.nebula.web.taglib;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.command.cms.CmsModuleInstanceVersionCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.cms.CmsTemplateHtml;
import com.baozun.nebula.sdk.manager.cms.SdkCmsModuleInstanceManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsModuleInstanceManagerImpl;
import com.baozun.nebula.utils.cache.GuavaAbstractLoadingCache;
import com.feilong.core.Validator;
import com.google.common.base.Optional;

@Component
public class ModuleTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private CacheManager cacheManager;
	
	private SdkCmsModuleInstanceManager sdkCmsModuleInstanceManager;
	
	private static WebApplicationContext applicationContext;
	
	private Logger logger = LoggerFactory.getLogger(ModuleTag.class);
	@Override
	public int doStartTag() throws JspException {

		if(applicationContext == null){
			applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.pageContext.getServletContext());
		}
		
		if(sdkCmsModuleInstanceManager == null){
			sdkCmsModuleInstanceManager = (SdkCmsModuleInstanceManager)applicationContext.getBean("sdkCmsModuleInstanceManager");
		}
		if(cacheManager == null) {
			cacheManager = (CacheManager)applicationContext.getBean("dataCacheManager");
		}
		JspWriter out = this.pageContext.getOut();
		
		
		try{
			/**
			 * 1、获取当前标签对应模块版本的缓存（静态map）
			 * 2、判断标签的模块是否在缓存时间段内
			 * 	 2.1、如果在就获取缓存模块版本的数据
			 * 		2.1.1、先保存发布模块实例版本，将发布版本推送到发布表中
			 *      2.1.2、重新更新发布队列缓存
			 *      2.1.3、重新更新发布模块的内容到缓存中
			 *   2.2、不在就返回基础版本的数据
			 * 
			 */
			//map里的data数据是由显示页面的html,version构成
			CmsTemplateHtml cmsTemplateHtml = SdkCmsModuleInstanceManagerImpl.moduleMap.get(code);
			String data = "";
			boolean isBase = true;
			if(Validator.isNotNullOrEmpty(cmsTemplateHtml)){
				Long currentVerison = cmsTemplateHtml.getVersionId();
				data = cmsTemplateHtml.getData();
				logger.info("--------------------------------------> current version is " + currentVerison+", data is "+data);
				Optional<Map<String, List<CmsModuleInstanceVersionCommand>>> hitResult = cache.getValue(CacheKeyConstant.CMS_MODULE_KEY + ":" + CacheKeyConstant.CMS_MODULE_VERSION_KEY);
				Map<String, List<CmsModuleInstanceVersionCommand>> publishVersionsQueue = hitResult.isPresent() ? hitResult.get() : null;
				if(Validator.isNotNullOrEmpty(publishVersionsQueue)){
					List<CmsModuleInstanceVersionCommand> versions = publishVersionsQueue.get(code);
					Date now = new Date();
					if(Validator.isNotNullOrEmpty(versions)){
						for(CmsModuleInstanceVersionCommand version : versions){
							//判断当前时段是否有发布的新版本在队里发布队列里
							if(now.compareTo(version.getStart_time()) >= 0 && now.compareTo(version.getEnd_time()) <= 0){
								//在发布队列外中，基础模块或者版本模块切换到下一个版本模块
								if(!version.getId().equals(currentVerison)){
									//因为zk的通知是不能当次请求，所以当前的这次请求,不会页面改变造成页面,造成了脏页面
									sdkCmsModuleInstanceManager.loadModuleMap();
									CmsTemplateHtml templatehtml = SdkCmsModuleInstanceManagerImpl.moduleMap.get(code);
									data = templatehtml.getData();
								}
								isBase = false;
							}
						}
						//在发布队列外，版本模块切换到基础模块
						if(isBase && currentVerison != -1L){
							sdkCmsModuleInstanceManager.loadModuleMap();
							CmsTemplateHtml templatehtml = SdkCmsModuleInstanceManagerImpl.moduleMap.get(code);
							data = templatehtml.getData();
						}
					}else{
						//在发布队列外，版本模块切换到基础模块
						if(currentVerison != -1L){
							sdkCmsModuleInstanceManager.loadModuleMap();
							CmsTemplateHtml templatehtml = SdkCmsModuleInstanceManagerImpl.moduleMap.get(code);
							data = templatehtml.getData();
						}
					}
				}
				//加入缓存服务器挂掉之后，重新版本队列，并发布新版本html内容到缓存中
				else{
					if(currentVerison != -1L){
						sdkCmsModuleInstanceManager.loadModuleMap();
						CmsTemplateHtml templatehtml = SdkCmsModuleInstanceManagerImpl.moduleMap.get(code);
						data = templatehtml.getData();
					}
				}


			}
			
			out.println(data);
		}
		catch(Exception e){
			logger.error("", e);
		}
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 启用0.5分钟缓存
	 */
	private GuavaAbstractLoadingCache<String, Optional<Map<String, List<CmsModuleInstanceVersionCommand>>>> cache = new GuavaAbstractLoadingCache<String, Optional<Map<String, List<CmsModuleInstanceVersionCommand>>>>(
			100, 30) {
		@Override
		protected Optional<Map<String, List<CmsModuleInstanceVersionCommand>>> fetchData(String key) {
			Map<String, List<CmsModuleInstanceVersionCommand>> publishVersionsQueue = cacheManager.getMapObject(CacheKeyConstant.CMS_MODULE_KEY, CacheKeyConstant.CMS_MODULE_VERSION_KEY);
			return Optional.fromNullable(publishVersionsQueue);
		}
	};

	
}
