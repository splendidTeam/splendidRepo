package com.baozun.nebula.i18n;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.baozun.nebula.manager.i18n.I18n;
import com.baozun.nebula.manager.i18n.I18nColumn;

public class ScanningI18nTablesUtil {

	private static Set<Class<?>> i18nTables = new HashSet<Class<?>>();

	static List<String> fns = new ArrayList<String>();
	static List<String> cns = new ArrayList<String>();

	/**
	 * @author 何波
	 * @Description: 获取i18n表
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> getI18nTables(String scannPackages)
			throws ClassNotFoundException {
		if (i18nTables.size() > 0) {
			return i18nTables;
		}
		if (StringUtils.isEmpty(scannPackages)) {
			return i18nTables;
		}
		String[] sps = scannPackages.split(",");
		if (sps == null || sps.length == 0) {
			return i18nTables;
		}
		// 是否使用默认过滤器true使用
		ClassPathScanningCandidateComponentProvider packageScan = new ClassPathScanningCandidateComponentProvider(
				false);
		packageScan.addIncludeFilter(new AnnotationTypeFilter(I18n.class));
		for (String pack : sps) {
			Set<BeanDefinition> bds = packageScan.findCandidateComponents(pack);
			for (BeanDefinition beanDefinition : bds) {
				Class<?> clazz = Class.forName(beanDefinition
						.getBeanClassName());
				I18n i18n = clazz.getAnnotation(I18n.class);
				Table tab = clazz.getAnnotation(Table.class);
				if (i18n != null && tab != null) {
					i18nTables.add(clazz);
				}
			}
		}
		return i18nTables;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		getI18nTables("com.baozun.nebula.model");
		for (Class<?> cls : i18nTables) {
			String clsName = cls.getName();
			//System.out.println(clsName);
			Table t  = cls.getAnnotation(Table.class);
			String tabName =t.name().toLowerCase();
			getfname(cls);
			getcname(cls);
			for (int i = 0; i < fns.size(); i++) {
				StringBuffer sql = new StringBuffer();
				String fn = fns.get(i);
				String cn = cns.get(i);
				sql.append("insert into t_mutl_lang_resource(id,class_name,table_name,field_name,column_name) values(");
				sql.append(" nextval('s_t_mutl_lang_resource'),");
				sql.append("'"+clsName+"',");
				sql.append("'"+tabName+"',");
				sql.append("'"+fn+"',");
				sql.append("'"+cn+"');");
				sql.append("\n");
				System.out.println(sql);
			}
			fns.clear();
			cns.clear();
		}
	}

	public static void getfname(Class<?> cls) {
		Field[] fields = cls.getDeclaredFields();
		for (Field f : fields) {
			//System.out.println(f.getName());
			I18nColumn i18nColumn = f.getAnnotation(I18nColumn.class);
			if (i18nColumn != null ) {
				fns.add(f.getName());
			}
		}

	}
	
	public static void getcname(Class<?> cls) {
		Method[] fields = cls.getDeclaredMethods();
		for (Method f : fields) {
			//System.out.println(f.getName());
			Column column = f.getAnnotation(Column.class);
			String mn = getFieldName(f.getName());
			if(!fns.contains(mn)){
				continue;
			}
			if (column != null) {
				cns.add(column.name().toLowerCase());
			}
		}

	}
	
	public static String getFieldName(String arg) {
		String str = arg.substring(3,arg.length());
		str = str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toLowerCase());
		return str;
	}
}
