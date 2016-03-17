package com.baozun.nebula.manager.i18n;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.baozun.nebula.model.i18n.MutlLangResource;
/**
 * 		
* @Description: 自动创建需要国际化实体对应的表
* @author 何波
* @date 2014年11月27日 下午5:33:00 
*
 */
@Component
public class AutoCreateI18nTable implements InitializingBean {
	public static final Logger log = LoggerFactory
			.getLogger(AutoCreateI18nTable.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		//获取需要i18n的表
		List<MutlLangResource> lists = jdbcTemplate.query("select *from t_mutl_lang_resource", new MutlLangResourceRowMapper());
		Map<String, String>  i18nTabs = new HashMap<String, String>();
		List<String> tabs = new ArrayList<String>();
 		if(lists != null && lists.size() > 0){
			//类名分组
			for (MutlLangResource list1 : lists) {
				String tabName = list1.getTableName();
				if(StringUtils.isEmpty(tabName)){
					throw new RuntimeException("国际化元数据表信息错误:\n表名有空");
				}
				if(tabs.contains(tabName)){
					continue;
				}
				tabs.add(tabName);
				StringBuffer sql = new StringBuffer();
				String idName = list1.getIdColumnName();
				if(StringUtils.isEmpty(idName)){
					throw new RuntimeException("国际化元数据表信息错误:\n表名:"+tabName+"没有配置关联id列名");
				}
				sql.append(String.valueOf(idName)+",");
				for (MutlLangResource list2 : lists) {
					String tabName1 = list2.getTableName();
					if(tabName.equals(tabName1)){
						String cname = list2.getColumnName();
						if(StringUtils.isEmpty(cname)){
							throw new RuntimeException("国际化元数据表信息错误:\n表名:"+tabName+"出现有没有列名的行");
						}
						sql.append(cname+",");
					}
				}
				if(StringUtils.isNotEmpty(sql.toString())){
					String cols = sql.substring(0, sql.length()-1);
					i18nTabs.put(tabName, cols);
				}
			}
		}
	
		if(i18nTabs.size()==0){
			return;
		}
		Set<String> allTabs = getAllTableNames();
		Set<String>  i18nTabKeys = i18nTabs.keySet();
		for (String tab : i18nTabKeys) {
			String newtab = (tab+"_lang").toLowerCase();
			//如果表不存在就创建
			if(!allTabs.contains(newtab)){
				StringBuilder sql =  new StringBuilder();
				sql.append(" create table "+newtab +" as ");
				sql.append(" select "+i18nTabs.get(tab)+" from "+tab);
				sql.append(" where 1=2");
				jdbcTemplate.update(sql.toString());
				String  addCol = "alter table "+newtab+" add column lang varchar(20)";
				jdbcTemplate.update(addCol);
			}
		}
	}
	
	private Set<String> getAllTableNames() {
		Set<String> dbTableNames = new HashSet<String>();
		try {
			Connection conn = jdbcTemplate.getDataSource().getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
			// 表名列表
			ResultSet rest = dbmd.getTables(null, null, "%", null);
			// 输出 table_name
			while (rest.next()) {
				dbTableNames.add(rest.getString("TABLE_NAME").toLowerCase());
			}
		} catch (Exception e) {
			log.error("查询表出错：", e);
		}
		return dbTableNames;
	}
}
