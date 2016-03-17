package com.baozun.nebula.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * create table 表名 select * from 表名 insert into 表名 delete from 表名 update 表名
 * select * from 表名 inner(left\right) join 表名 truncate table 表名 drop table 表名
 */

public class VelocityTemplateAspectTest {
	public static final Logger log = LoggerFactory
			.getLogger(VelocityTemplateAspectTest.class);

	public static void main(String[] args) throws ClassNotFoundException {
//		String sql1 = readTxtFile();
//		Set<String> tables = SqlAspect.getSqlTabNameT_(sql1);
//		log.debug("匹配出来的表:{}", tables);
//		if (tables != null && tables.size() > 0) {
//			for (String name : tables) {
//				sql1 = sql1.replaceAll(" {0,1}" + name + " {1,1}", " " + name
//						+ "_" + "zh ");
//			}
//		}
//		System.out.println(sql1);
//		tables = SqlAspect.getSqlTabName(sql1);
//		log.debug("匹配出来的表:{}", tables);
	}

	public static void getSqlTabName(String sql) {
		Pattern p = Pattern
				.compile("(?i)(?<=(?:from|into|update|join)\\s{1,1000})(\\w+)");
		Matcher m = p.matcher(sql);
		Set<String> tables = new HashSet<String>();
		while (m.find()) {
			tables.add(m.group());
		}
		p = Pattern.compile("(?i)from(.*?)(?i)(where|inner|left|right)");
		m = p.matcher(sql);
		while (m.find()) {
			String name = m.group(1).trim();
			if (name.indexOf(",") > -1) {
				String[] arr = name.split(",");
				if (arr.length > 0) {
					for (String str : arr) {
						String[] ss = str.trim().split(" +");
						for (String s : ss) {
							if (StringUtils.isNotEmpty(s)) {
								tables.add(s);
								break;
							}
						}
					}
				}
			} else {
				tables.add(name);
			}
		}
		for (String string : tables) {
			System.out.println(string);
		}
	}

	public static String readTxtFile() {
		StringBuffer sb = new StringBuffer();
		try {
			String encoding = "UTF-8";
			File file = new File(
					"D:/baocun-ws/nebula-repo-5.1.15b/src/test/resources/test.sql");
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt + "\n");
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return sb.toString();

	}
}
