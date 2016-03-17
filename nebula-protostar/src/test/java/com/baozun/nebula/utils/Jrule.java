package com.baozun.nebula.utils;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Jrule {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String json="[{'id':'637','pId':'539','value':'红','pName':'颜色'},"
				+ "{'id':'683','pId':'572','value':'42','pName':'尺寸'}] ";
		Gson gson =new Gson();
		
		List<Pro> pList =gson.fromJson(json, new TypeToken<List<Pro>>(){}.getType());
		for (int i = 0; i < pList.size(); i++) {
			System.out.println(pList.get(i).toString());
		}
	}
	class Pro{
		String id;
		String pId;
		String value;
		String pName;
		public String getpId() {
			return pId;
		}
		public void setpId(String pId) {
			this.pId = pId;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getpName() {
			return pName;
		}
		public void setpName(String pName) {
			this.pName = pName;
		}
		@Override
		public String toString() {
			return "Pro [id=" + id + ", pId=" + pId + ", value=" + value
					+ ", pName=" + pName + "]";
		}
		
		
	}

}
