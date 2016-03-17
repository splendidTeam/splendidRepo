package com.baozun.nebula.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 加类类相关
 * 可以进行动态加载
 * @author Justin Hu
 *
 */
public class ClassLoaderUtils {

	
	/**
	 * 载入某些url地址对应的jar
	 * @param urls
	 * @throws Exception
	 */
	public static void loadJarUrls(List<URL> urls) throws Exception{
		
		
		URLClassLoader cl = (URLClassLoader)Thread.currentThread().getContextClassLoader();
		  
    	Class sysclass = URLClassLoader.class;
		Method method = null;
		
		method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
		 
    	method.setAccessible(true);
    	
    	for(URL url:urls){
    		method.invoke(cl, url);
    	}
	}
	
	/**
	 * 载入目录
	 * @param paths 以逗号分隔的多个路径
	 * @throws Exception
	 */
	public static void loadRepositoryDir(String paths) throws Exception{
	
		
		 if (paths == null || paths.length() == 0)  
	            return;  
		 
		 StringTokenizer st = new StringTokenizer(paths, ",");  
	        while (st.hasMoreTokens()) {  
	            String jarPath = st.nextToken();  
	  
	            File jarDir = new File(jarPath);  
	            if (!jarDir.isDirectory())  
	                continue;  
	    		
	    		File[] jarFiles = jarDir.listFiles();  
	    		
	    		List<URL> urls=new ArrayList<URL>();
	    		
	    		 for (File jarFile : jarFiles) {  
	                 try {
	                	 urls.add(jarFile.toURI().toURL()) ;                 	
	                	 
	                	
	                 } catch (MalformedURLException e) {
	                	 e.printStackTrace();
	                 }  
	             }  
	    		 
	    		 loadJarUrls(urls);
	        } 
	}
	
	/**
	 * description:由一个jar文件中取出所有的类名
	 * 
	 * @param file
	 *            *.jar，绝对路径
	 * @return 返回所有驱动类
	 */
	public static List<String> getClassNames(String file) {
		JarFile jar = null;
		List<String> list = new ArrayList<String>();
		try {
			jar = new JarFile(file);
		} catch (IOException e) {
			return null;
		}
		Enumeration<JarEntry> entries = jar.entries();

		
		String name;
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			//如果是.class为结尾
			if (entry.getName().endsWith(".class")) {
				name = entry.getName();
				name = name.substring(0, name.length() - 6);
				name = name.replaceAll("/", ".");
				list.add(name);
				
			}
		}

		return list;
	}
	
}
