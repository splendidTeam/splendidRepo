package com.baozun.nebula.manager.i18n;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.support.CommonBeanRowMapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.baozun.nebula.dao.i18n.MutlLangResourceDao;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.i18n.MutlLangResource;
import com.baozun.nebula.utilities.common.LangUtil;

@Component
public class MutlLangManager implements Ordered {
	public static final Logger log = LoggerFactory.getLogger(MutlLangManager.class);
	
	@Value("#{meta['i18n.on.off']}")
	private String i18n_on_off;
	
	@Autowired
	private  MutlLangResourceDao mutlLangResourceDao;
	//存储国际化表
	private  static   Map<String, List<MutlLangResource>>  i18nCls = null;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String i18n_tab_subfix = "_lang";
	
	@Around("this(loxia.dao.GenericEntityDao)")
	public Object doI18n(ProceedingJoinPoint pjp) throws Throwable {
		//检查是否启用国际化aop
		if(i18n_on_off == null || i18n_on_off.equals("off") || !i18n_on_off.equals("on")){
			return pjp.proceed();
		}
		Object[] args = pjp.getArgs();
		if(args == null || args.length == 0 ||args.length > 1){
			return pjp.proceed(args);
		}
		//检查参数是否有继承BaseModel
		boolean isBaseModel = true;
		for (Object arg : args) {
			if(arg instanceof BaseModel){
				isBaseModel = false;
			}
		}
		//没有就不处理
		if(isBaseModel){
			return pjp.proceed(args);
		}
		initI18nCls();
		Object crtArgs = args[0];
		Object cloneArgs = crtArgs.getClass().newInstance();
		
		PropertyUtils.copyProperties(cloneArgs, args[0]);
		//如果参数有有国际化数据 且不是默认国际化数据时 设置成默认国际化数据 
		Object cloneObj = setDefaultLangData(args);
		Object result = pjp.proceed(args);
		//处理参数
		dealArgs(cloneArgs,crtArgs);
		if(result == null){
			return result;
		}
		if(cloneObj != null && result == crtArgs){
			Object id = PropertyUtils.getProperty(crtArgs, "id");
			PropertyUtils.setProperty(cloneArgs, "id", id);
			try {
				Object version = PropertyUtils.getProperty(crtArgs, "version");
				PropertyUtils.setProperty(cloneArgs, "version", version);
			} catch (Exception e) {
				log.debug("该对象没有verison");
			}
			//返回当前的国际化数据
			return cloneArgs;
		}
		return result;
	}

	@Override
	public int getOrder() {
		return 0;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	private  Object  setDefaultLangData(Object[] args) throws Exception{
		Object cloneObj = null;
		String lang = LangUtil.getCurrentLang();
		log.debug("当前语言:"+lang);
		if(StringUtils.isEmpty(lang)){
			lang = LangUtil.ZH_CN;
			log.debug("当前语言为空,使用默认语言:"+lang);
			return cloneObj;
		}
		if(lang.equals(LangUtil.ZH_CN)){
			//不处理
			return cloneObj;
		}
		for (Object bean : args) {
			if(bean instanceof BaseModel){
				Class<?> cls = bean.getClass();
				String clsName = cls.getName();
				//如果 是国际化类才处理
				if(i18nCls.containsKey(clsName)){
					cloneObj = cls.newInstance();
					PropertyUtils.copyProperties(cloneObj, bean);
					//获取国际化属性值 
					List<MutlLangResource> list  = i18nCls.get(clsName);
					if(list != null && list.size() > 0){
						String tabName = list.get(0).getTableName();
						if(StringUtils.isEmpty(tabName)){
							throw new RuntimeException("国际化元数据表信息错误:\n表名有空");
						}
						String idfName =  list.get(0).getIdFieldName();
						if(StringUtils.isEmpty(idfName)){
							throw new RuntimeException("国际化元数据表信息错误:表名:"+tabName+"中属性id_field_name有空");
						}
						String idCName =  list.get(0).getIdColumnName();
						if(StringUtils.isEmpty(idCName)){
							throw new RuntimeException("国际化元数据表信息错误:表名:"+tabName+"中列id_column_name有空");
						}
						tabName= tabName + i18n_tab_subfix;
						String querySql = "select *from "+ tabName+" where  "+idCName+"= ? and lang = ?";
						//获取数据id
						Object id = PropertyUtils.getProperty(bean,idfName);
						//获取国际化数据默认值
						List<Object> i18nObjs =  jdbcTemplate.query(querySql, new CommonBeanRowMapper(cls),new Object[]{id,LangUtil.ZH_CN});
						//不处理
						if(id == null){
							for (MutlLangResource resource : list) {
								PropertyUtils.setProperty(bean, resource.getFieldName(), null);
							}
							return cloneObj;
						}
						if(i18nObjs==null || i18nObjs.size()==0){
							for (MutlLangResource resource : list) {
								PropertyUtils.setProperty(bean, resource.getFieldName(), null);
							}
							return cloneObj;
						}else{
							Object i18nObj = i18nObjs.get(0);
							for (MutlLangResource resource : list) {
								String name = resource.getFieldName();
								Object val = PropertyUtils.getProperty(i18nObj, name);
								PropertyUtils.setProperty(bean, name, val);
							}
							return cloneObj;
						}
					}
				}
			}
		}
		return cloneObj;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private  void dealArgs(Object bean,Object args) throws Exception{
		String lang = LangUtil.getCurrentLang();
		log.debug("当前语言:"+lang);
		if(StringUtils.isEmpty(lang)){
			lang = LangUtil.ZH_CN;
			log.debug("当前语言为空,使用默认语言:"+lang);
		}
		if(bean instanceof BaseModel){
			Class<?> cls = bean.getClass();
			String clsName = cls.getName();
			log.debug("class name :{}", clsName);
			//如果 是国际化类才处理
			if(i18nCls.containsKey(clsName)){
				//获取国际化属性值 
				List<MutlLangResource> list  = i18nCls.get(clsName);
				if(list != null && list.size() > 0){
					String tabName = list.get(0).getTableName();
					if(StringUtils.isEmpty(tabName)){
						throw new RuntimeException("国际化元数据表信息错误:\n表名有空");
					}
					StringBuilder cols = new StringBuilder();
					StringBuilder preCompile = new StringBuilder();
					StringBuilder updateCols = new StringBuilder();
					List<Object>  params = new   ArrayList<Object>();
					String idfName =  list.get(0).getIdFieldName();
					if(StringUtils.isEmpty(idfName)){
						throw new RuntimeException("国际化元数据表信息错误:表名:"+tabName+"中属性id_field_name有空");
					}
					String idCName =  list.get(0).getIdColumnName();
					if(StringUtils.isEmpty(idCName)){
						throw new RuntimeException("国际化元数据表信息错误:表名:"+tabName+"中列id_column_name有空");
					}
					int size = list.size();
					for (int i = 0; i < size; i++) {
						 MutlLangResource  mlr = list.get(i); 
						 String fieldName = mlr.getFieldName();
						 if(StringUtils.isEmpty(fieldName)){
							throw new RuntimeException("国际化元数据表信息错误:\n表名:"+tabName+"中属性值有空");
						 }
						 Object value = PropertyUtils.getProperty(bean,fieldName);
						 params.add(value);
						 String col = mlr.getColumnName();
						 if(StringUtils.isEmpty(fieldName)){
							throw new RuntimeException("国际化元数据表信息错误:\n表名:"+tabName+"中列名有空");
						 }
						 cols.append(col+",");
						 preCompile.append("?,");
						 if((i+1)==size){
							 updateCols.append(col+"=?");
						 }else{
							 updateCols.append(col+"=?,");
						 }
					}
					//获取数据id
					Object id = PropertyUtils.getProperty(args,idfName);
					if(id == null){
						 throw new RuntimeException("从"+clsName+"中未获取到数据id值");
					}
					tabName = tabName + i18n_tab_subfix;
					//检查对应表中是否对应数据
					String querySql = "select count(*)from "+ tabName+" where  "+idCName+"= ? and lang = ?";
					//检查是新增还是修改
					int i18nObj =  jdbcTemplate.queryForInt(querySql, new CommonBeanRowMapper(cls),new Object[]{id,lang});
					if(i18nObj == 0){
						 //新增
						 cols.append("id,");
						 preCompile.append("?,");
						 cols.append("lang");
						 preCompile.append("?");
						 params.add(id);
						 params.add(lang);
						 StringBuilder insertSql = new StringBuilder();
						 insertSql.append("insert into "+tabName);
						 insertSql.append(" ("+cols+") ");
						 insertSql.append(" values("+preCompile+")");
						 jdbcTemplate.update(insertSql.toString(), params.toArray());
					 }else{
						 //修改
						 params.add(id);
						 params.add(lang);
						 StringBuilder updateSql = new StringBuilder();
						 updateSql.append("update "+tabName);
						 updateSql.append(" set "+updateCols);
						 updateSql.append(" where  id = ?");
						 updateSql.append(" and  lang = ?");
						 jdbcTemplate.update(updateSql.toString(), params.toArray());
					 }
				}
				
			}
	}
		
	}
	private void  initI18nCls(){
		if(i18nCls != null && i18nCls.size() > 0){
			return;
		}
		i18nCls = new HashMap<String, List<MutlLangResource>>();
		//由于拦截的是loxia.dao.GenericEntityDao的子类会导致死循环 所以使用jdbcTemplate
		List<MutlLangResource> lists = jdbcTemplate.query("select *from t_mutl_lang_resource", new MutlLangResourceRowMapper());
		
		List<String> clsNames = new ArrayList<String>();
 		if(lists != null && lists.size() > 0){
			//类名分组
			for (MutlLangResource list1 : lists) {
				String clsName1 = list1.getClassName();
				if(clsNames.contains(clsName1)){
					continue;
				}
				clsNames.add(clsName1);
				List<MutlLangResource> mlrs = new ArrayList<MutlLangResource>();
				for (MutlLangResource list2 : lists) {
					String clsName2 = list2.getClassName();
					if(clsName1.equals(clsName2)){
						mlrs.add(list2);
					}
				}
				i18nCls.put(clsName1, mlrs);
			}
			
		}
	}
}

