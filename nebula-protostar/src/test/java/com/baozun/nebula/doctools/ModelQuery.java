package com.baozun.nebula.doctools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ModelQuery {

	private static String javaFilePath="E:/workspace4.3/nebula-wormhole-entity/src/main/java/com/baozun/nebula/wormhole/mq/MqMsgHead.java";
	private static int startLine=18;
	private static int endLine=51;
	private static String SEP="\t";
	
	private String processLine(String source){
		
		String[] arg=source.split("private");
		
		String memo=arg[0].replaceAll("[/*\n\t ]", "");
		
		String code=arg[1].trim();
		
		int index=code.indexOf(" ");
		
		if(index==-1){
			index=code.indexOf("\t");
		}
		else{
			int indext=code.indexOf("\t");
			if(indext!=-1){
				index=index<indext?index:indext;
			}
		}
		
		String type=code.substring(0,index);
		
		String name=code.substring(index).replaceAll("[ \t;]", "");
		
		return name+SEP+type+SEP+memo;
		
	}
	
	private List<String> parseFile(BufferedReader br) throws Exception{
		List<String> results=new ArrayList<String>();
		
		String key=null;
		String result="";
		int line=0;
		while((key=br.readLine())!=null){
			line++;
			
			if(line>=startLine&&line<=endLine){
				if(key.indexOf("/*")!=-1){
					result=key;
				}
				else if(key.indexOf(";")!=-1){
					results.add(processLine(result+key));
				}
				else{
					result+=key;
				}
			}
			else if(line>endLine){
				break;
			}
			
				
		}
		
		return results;
	}
	
	public void output(String path)throws Exception{
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "utf-8"));
		
		List<String> results=parseFile(br);
		
		for(String str:results){
			System.out.println(str);
		}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		new ModelQuery().output(javaFilePath);
		
	}

}
