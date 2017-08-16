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
package com.baozun.nebula.web.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import com.baozun.nebula.command.SkuPropertyCommand;
import com.baozun.nebula.curator.ZKWatchPath;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.EngineWatchInvoke;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.SchedulerManager;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.sdk.manager.EmailTemplateManager;



/**
 * @author Justin
 *
 */
@Controller
public class DemoController  {

	@Autowired
	 ApplicationContext webContext;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private ZkOperator zkOperator;
	
	@Autowired
	private ZKWatchPath zkWatchPath;


	@Autowired
	RequestMappingInfoHandlerMapping rmhm;
	
	@Value("#{meta['upload.img.base335533']}")
	private  String UPLOAD_IMG_BASE="";
	
	private DefaultListableBeanFactory makeDefaultListableBeanFactory(ApplicationContext context){
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
		
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
		
		return defaultListableBeanFactory;
	}
	
	

	/**
	 * 注册bean
	 * @param fullClassName
	 * @param context
	 * @throws Exception
	 */
	private void register(String fullClassName,ApplicationContext context )throws Exception{
		
		Class c = Class.forName(fullClassName);

		int index =fullClassName.lastIndexOf(".");
		String className=fullClassName.substring(index+1);
		
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder .genericBeanDefinition(c);  
		 
		makeDefaultListableBeanFactory(context).registerBeanDefinition(className,beanDefinitionBuilder.getRawBeanDefinition());  
		//defaultSingletonBeanRegistry.registerSingleton(className, c.newInstance());
	}
	
	/**
	 * 注册bean
	 * @param fullClassName
	 * @param context
	 * @throws Exception
	 */
	private void register(Class clazz,ApplicationContext context )throws Exception{
		
		//Class c = Class.forName(fullClassName);

		int index =clazz.getName().lastIndexOf(".");
		String className=clazz.getName().substring(index+1);
		
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);  
		 
		makeDefaultListableBeanFactory(context).registerBeanDefinition(className,beanDefinitionBuilder.getRawBeanDefinition());  
		//defaultSingletonBeanRegistry.registerSingleton(className, c.newInstance());
		
		
	}
	
	/**
	 * 通过全类名获取简单类名
	 * @param fullName
	 * @return
	 */
	private String getSimpleClassName(String fullName){
		int index =fullName.lastIndexOf(".");
		String className=fullName.substring(index+1);
		
		return className;
	}
	
	private void destroy(String fullClassName ,Object beanInstance)throws Exception{
		
		Class c = Class.forName(fullClassName);
		
		int index =fullClassName.lastIndexOf(".");
		String className=fullClassName.substring(index+1);
		
	}
	/**
	 * 获取urlmap
	 * @return
	 * @throws Exception
	 */
	private MultiValueMap<String, RequestMappingInfo> queryUrlMap() throws Exception{
		
		Field field=AbstractHandlerMethodMapping.class.getDeclaredField("urlMap");
		if(!field.isAccessible()){
			field.setAccessible(true);
		}
		
		MultiValueMap<String, RequestMappingInfo> urlMap=(MultiValueMap<String, RequestMappingInfo>)field.get(rmhm);
		
		return urlMap;
	}
	/**
	 * 获取url地址
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private List<String> queryUrls(Class clazz)throws Exception{
		List<String> urls=new ArrayList<String>();
		
		for(Method method:clazz.getMethods()){
			Annotation[] annos=method.getAnnotations();
			if(annos!=null&&annos.length>0){
				for(Annotation anno:annos){
					//确定是RequestMapping
					if(anno instanceof RequestMapping){
						RequestMapping rm=(RequestMapping)anno;
						for(String url:rm.value()){
							urls.add(url);
						}
					}
				}
			}
		}
		
		return urls;
	}
	
	@RequestMapping(value = "/demo/destory.htm")
	@ResponseBody
	public String testDes(HttpServletRequest request)throws Exception{

		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		
/*		Method method=ReflectionUtils.findMethod(TestController.class, "show");
		String[] urls=null;
		for(Annotation anno: method.getAnnotations()){
			if(anno instanceof RequestMapping){
				urls=((RequestMapping)anno).value();
			}
		}
		for(String url:urls){
			Field field=ReflectionUtils.findField(RequestMappingHandlerMapping.class, "urlMap");
			MultiValueMap<String, RequestMappingInfo> urlMap=(MultiValueMap<String, RequestMappingInfo>)ReflectionUtils.getField(field, rmhm);
		}*/
		
		 return "success";
	}
	
	
	@RequestMapping(value = "/demo/testObj.json")
	@ResponseBody
	public String testObj(HttpServletRequest request,
		@ModelAttribute("sku") SkuPropertyCommand sku)throws Exception{
		
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		
		// TestService sercice2=(TestService)applicationContext.getBean("TestService");
		
		// System.out.println(sercice2);
		
		//cacheManager.setValue("test2", "222");
		try{
		System.out.println(cacheManager.getValue("test2"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		List<String> list=new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		
		User user=new User();
		//user.setEmail("asdfsdf@asfsdf.com");
		//cacheManager.setObject("testUser", user);
		//user=cacheManager.getObject("testUser");
		
		//cacheManager.pushToListHead("pushList","t1");
		//cacheManager.pushToListHead("pushList","t2");
		//cacheManager.pushToListHead("pushList","t3");
		System.out.println("pop:"+cacheManager.popListHead("pushList"));
		
		
		 return "success";
	} 
	

	
	@RequestMapping(value = "/demo/testObj.htm")
	
	public String testObjHtml(HttpServletRequest request)throws Exception{
		
		
		 return "test";
	} 
	
	@RequestMapping(value = "/demo/queryBean.htm")
	@ResponseBody
	public String queryBean(HttpServletRequest request)throws Exception{
		
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		
		// TestService sercice2=(TestService)applicationContext.getBean("TestService");
		
		// System.out.println(sercice2);
		 return "success";
	}
	
	public Boolean registerUrl(Class clazz)throws Exception{
		//还需解决重复注册url的事情
				
		List<String> urls=queryUrls(clazz);
		
		MultiValueMap<String, RequestMappingInfo> urlMap=queryUrlMap();
		for(String url:urls){
			List<RequestMappingInfo> rmi=urlMap.get(url);
			if(rmi!=null&&rmi.size()>0){
				System.out.println("exists controller");
				return false;
			}
			
		}
		
		Method method=AbstractHandlerMethodMapping.class.getDeclaredMethod("detectHandlerMethods",Object.class);
		if(!method.isAccessible()){
			method.setAccessible(true);
		}
		
		ReflectionUtils.invokeMethod(method, rmhm, getSimpleClassName(clazz.getName()));
		
	//	 rmhm.afterPropertiesSet();
		 return true;
	}
	
	/*@RequestMapping(value = "/demo/loadjar.htm")
	@ResponseBody
	public String loadjar(HttpServletRequest request) throws Exception{
		
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

		
		
		
		List<String> classNameList=ClassLoaderUtils.getClassNames("c:/testjar/nebula-plugin-5.0.0.jar");
		
		File file=new File("c:/testjar/nebula-plugin-5.0.0.jar");
		List<URL> urlList=new ArrayList<URL>();
		urlList.add(file.toURI().toURL());
		ClassLoaderUtils.loadJarUrls(urlList);
		
		for(String className:classNameList){
			Class clazz=Class.forName(className);
			if(className.lastIndexOf("Controller")!=-1){
				
				 register(clazz,webContext);
				 registerUrl(clazz);
				 
			}
			else{
			 register(clazz,applicationContext);
			}
		}
		
		
		 
		 Object obj=webContext.getBean("TestController");
		
		return "success";
	}*/
	
	@RequestMapping(value = "/demo/register.htm")
	@ResponseBody
	public String register(HttpServletRequest request) throws Exception{
		
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

		Class daoClass=Class.forName("com.baozun.nebula.test.TestDao");
		Class serviceClass=Class.forName("com.baozun.nebula.test.TestService");
		Class controllerClass=Class.forName("com.baozun.nebula.test.TestController");
		
		Class demo3ControllerClass=Class.forName("com.baozun.nebula.web.controller.Demo3Controller");
		
		register(daoClass,applicationContext);
			
		 register(serviceClass,applicationContext);
		
		//注到bean到controller的application
		 register(controllerClass,webContext);
		 registerUrl(controllerClass);
		 
		 register(demo3ControllerClass,webContext);
		 registerUrl(demo3ControllerClass);
		
		
		 
		 Object obj=webContext.getBean("TestController");
		
		return "success";
	}
	
	@RequestMapping(value = "/demo/test.htm")
	@ResponseBody
	public String test(HttpServletRequest request) throws Exception{
		
	
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

		//注到bean到manager的application
		 register("com.baozun.nebula.test.TestDao",applicationContext);
		
		 register("com.baozun.nebula.test.TestService",applicationContext);
		
		//注到bean到controller的application
		 register("com.baozun.nebula.test.TestController",webContext);

		 //分开注册，但controller还是能引用到manager context的bean
		 
		// TestController sercice2=(TestController)webContext.getBean("TestController");
 

		return "success";
	}
	
	@RequestMapping(value = "/demo/print.htm")
	@ResponseBody
	public String print(HttpServletRequest request) throws Exception{
		
 

		return "print";
	}
	
	//@RequestMapping(value = "/cms/*")
	//@ResponseBody
	public String cms(HttpServletRequest request) throws Exception{
		
 

		return "print";
	}
	
	@RequestMapping(value = "/ztree/init.htm")

	public String showZtree(HttpServletRequest request) throws Exception{
		
 

		return "ztree";
	}
	
	@RequestMapping(value="/demo/testzk.htm")
	@ResponseBody
	public Object testZk()throws Exception{
		
		boolean result=zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(EngineWatchInvoke.class));
		
		return result;
	}
	

	@RequestMapping(value = "/ztree/queryZNode.json")
	@ResponseBody
	public List<Map<String,Object>> queryZNode(@RequestParam(value="id",required=false)String id,@RequestParam(value="name",required=false)String name){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		if(id==null){
			id="0";
			
			Map<String,Object> map=new HashMap<String,Object>();
			
			map.put("id", 0);				//当前结点id
			map.put("pId", -1);				//父结点id
			map.put("name", "结点0");		//结点名称
			map.put("open", true);			//默认是否展开
			map.put("state", "1");
			map.put("sort", 1);
			map.put("isParent", true);		//是否父结点
			list.add(map);
		}
		
		
		int r=Math.abs(new Random().nextInt()*1000%100);
		for(int i=1;i<5;i++){
			Map<String,Object> map=new HashMap<String,Object>();
			
			map.put("id", r+i);
			map.put("pId", Integer.parseInt(id));
			map.put("name", "结点"+(r+i));
			map.put("open", true);
			map.put("state", "1");
			map.put("sort", i+1);
			map.put("isParent", false);
			list.add(map);
		}
		
		
		return list;
	}
	
	@Autowired
	private SchedulerFactoryBean scheduler;

	
	@RequestMapping(value = "/Schedu/test.json")
	@ResponseBody
	public Object testDynamicSchedu() throws Exception{
		
		MethodInvokingJobDetailFactoryBean mifb=new MethodInvokingJobDetailFactoryBean();
				
		mifb.setTargetObject(new Object());
		mifb.setTargetMethod("test");
		mifb.setConcurrent(false);
		mifb.setName("testSchedulerManagerJob2");
		mifb.afterPropertiesSet();
		
		CronTriggerBean ctb=new CronTriggerBean();
		ctb.setJobDetail(mifb.getObject());
		ctb.setCronExpression("0 59 18 9 4 ?");
		ctb.setName("testSchedulerManager2");
		ctb.getNextFireTime();
		//Scheduler.setTriggers(new Trigger[]{ctb});
		//scheduler.getScheduler().scheduleJob(ctb);
		scheduler.getScheduler().deleteJob("testSchedulerManagerJob2", Scheduler.DEFAULT_GROUP);
		scheduler.getScheduler().scheduleJob(mifb.getObject(), ctb);
		
		
		
		//scheduler.getScheduler().rescheduleJob("testSchedulerManager", Scheduler.DEFAULT_GROUP, ctb);
		
		//JobDetail jobDetail = new JobDetail("testJob", Scheduler.DEFAULT_GROUP, TestSchedulerManager.class);
		
	     //  long end=System.currentTimeMillis()+9000L;
	     // SimpleTrigger trigger=new SimpleTrigger("test",null,new Date(),new Date(end),10,3000L);
	    //  scheduler.getScheduler().scheduleJob(jobDetail, trigger);
		
		return "true";
	}
	
	@Autowired
	private SchedulerManager schedulerManager;
	

	
	@RequestMapping("/demo/timer.htm")
	@ResponseBody
	public String testTimer(@RequestParam String date,@RequestParam String value) throws Exception{
	
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		//schedulerManager.addTask(schedulerPublishedManager, "test", sdf.parse(date), "testTimer"+value);
		return "success";
	}
	
	@Autowired
	private EmailTemplateManager emailTemplateManager;
	
	@RequestMapping("/demo/testemail.json")
	@ResponseBody
	public String testemail() throws Exception{
		 Map<String,Object> dataMap=new HashMap<String,Object>();
		 dataMap.put("name", "还不错,可以看一下");
		 dataMap.put("link", "http://www.qq.com");
		
//		emailTemplateManager.sendEmail("qiang.hu@baozun.cn","EMAIL_CREATE_ORDER_SUCCESS", dataMap);
//		emailTemplateManager.sendEmail("10045466@qq.com","EMAIL_CREATE_ORDER_SUCCESS", dataMap);
//		emailTemplateManager.sendEmail("10045466@qq.com","EMAIL_FORGET_PASSWORD", dataMap);
//		emailTemplateManager.sendEmail("justin_wd@163.com","EMAIL_CREATE_ORDER_SUCCESS", dataMap);
//		emailTemplateManager.sendEmail("justin_wd@163.com","EMAIL_FORGET_PASSWORD", dataMap);
		return "success";
	}
}
