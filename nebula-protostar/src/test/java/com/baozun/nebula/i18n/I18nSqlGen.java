package com.baozun.nebula.i18n;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;

import com.baozun.nebula.model.product.PropertyValue;

public class I18nSqlGen {

	public static void main(String[] args) {
		Class<?> cls = PropertyValue.class;
		List<String> list = new ArrayList<String>();
		list.add("value");
		genSql(cls, list, "property_value_id");

	}

	public static void genSql(Class<?> cls, List<String> i18nList, String fpk) {
		Method[] fields = getMethods(cls);
		String tabName = cls.getAnnotation(Table.class).name().toLowerCase();
		String tab1 = tabName + "_lang";
		String ftab1 = tabName.substring(tabName.lastIndexOf("_")+1,
				tabName.length());
		String ftab2 = ftab1 + "_lang";
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		for (int i = 0; i < fields.length; i++) {
			Method f  =  fields[i];
			Column column = f.getAnnotation(Column.class);
			if (column != null) {
				String v = column.name().toLowerCase();
				if(i18nList.contains(v)){
					if((i+1)==fields.length){
						sql.append(" "+ftab2+"."+v+" as " +v+" ");
					}else{
						sql.append(" "+ftab2+"."+v+" as " +v+",");
					}
				}else{
					if((i+1)==fields.length){
						if((i+1)%3==0){
							sql.append(" "+ftab1+"."+v+" as " +v+"\n");
						}else{
							sql.append(" "+ftab1+"."+v+" as " +v+" ");
						}
					}else{
						if((i+1)%3==0){
							sql.append(" "+ftab1+"."+v+" as " +v+",\n");
						}else{
							sql.append(" "+ftab1+"."+v+" as " +v+", ");
						}
					}
					
				}
			}
		}
		sql.append("\n from "+tabName+ " "+ ftab1 +","+tab1+" "+ftab2 +" \n");
		sql.append(" where "+ftab2+".lang =:lang and "+ftab1+".id = "+ftab2+"."+fpk+"\n");
		sql.append(" and "+ftab1+".id = :id ");
		System.out.println(sql.toString());
	}
	
	public static Method[]  getMethods(Class<?> cls){
		Method[] fields = cls.getDeclaredMethods();
		List<Method> list = new ArrayList<Method>();
		for (Method method : fields) {
			Column column = method.getAnnotation(Column.class);
			if (column != null) {
				list.add(method);
			}
		}
		return list.toArray(new Method[]{});
	}
}
