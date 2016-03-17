package com.baozun.nebula.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.baozun.nebula.solr.utils.JsonUtil;

public class AddressUtilTest {

	public void taobaoAddress() throws HttpException, IOException {
		String url = "http://lsp.wuliu.taobao.com/locationservice/addr/output_address_town.do?l3=";
		
		BufferedReader reader = new BufferedReader(new FileReader("C:/Users/jumbo/Desktop/address.txt"));
		FileWriter writer = new FileWriter("C:/Users/jumbo/Desktop/full-address.txt");
		StringBuffer sb = new StringBuffer();
		String s;
		while ((s = reader.readLine()) != null) {
			sb.append(s);
		}
		Map<String, Object> map = JsonUtil.toMap(sb.toString());
		HttpClient http = new HttpClient();
		for (String key : map.keySet()) {
			//System.out.println(key);
			HttpMethod method = new GetMethod(url + key);
			http.executeMethod(method);
			String responseStr = method.getResponseBodyAsString();
			if (responseStr.contains("success:true")) {
				Matcher matcher = Pattern.compile("result:[{]([^}]+)[}]").matcher(responseStr);
				if (matcher.find()) {
					responseStr = matcher.group(1);
					matcher = Pattern.compile("('[0-9]+':[\\[][^\\]]+[\\]])").matcher(responseStr);
					while (matcher.find()) {
						String rs = matcher.group(1) + "," + "\n";
						writer.write(rs);
					}
				}
			}
			
		}
		writer.close();
		reader.close();
	}
}
