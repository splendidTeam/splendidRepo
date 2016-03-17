package com.baozun.nebula.tools;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.utils.FileUtils;


public class CsvFileProcess {
	
	/**
	 * 所有的行
	 * 每隔一段时间输出清理一下
	 */
	private List<String[]> rowList=new ArrayList<String[]>();
	
	private String[] headrow=null;

	private  String[] splitRow(String source){
		String[] rows=source.split("\r\n");
		
		return rows;
	}
	
	private  String[] splieCol(String source){
		
		return source.split(",");
	}
	
	
	private void outItem(){
		
		String result="<tbody><tr>";
		String itemcode="0";
		
		for( int i=0;i<headrow.length-1;i++){
			String headcol=headrow[i];
			result+="<td>"+headcol+"</td>";
		}
		
		result+="</tr>";
		
		for(String[] cols:rowList){
			result+="<tr>";
			for(int i=0;i<cols.length-1;i++){
				String col=cols[i];
				result+="<td>"+col+"</td>";
			}
			itemcode=cols[cols.length-1];
			result+="</tr>";
		}
		
		result+="</tbody>";
		
		
		
		String sql="INSERT INTO \"public\".\"t_pd_item_properties\" (\"id\", \"create_time\", \"item_id\", \"modify_time\", \"picurl\", \"property_id\", \"property_value\", \"propertyvalue_id\", \"version\") VALUES (nextval('SEQ_T_PD_ITEM_PROPERTIES'), now(), (select id from t_pd_item where code='"+itemcode+"'), NULL, '', '24', '"+result+"', NULL,now());";
		
		System.out.println(sql);
		rowList.clear();
	}
	
	private void init() throws Exception{
		String str=FileUtils.readFile("c:/rookie/女童-上衣.csv", "GBK");
		String[] rows=splitRow(str);
		
		headrow=splieCol(rows[0]);
		//有多少个列
		int colsize=headrow.length;
		String lastItemCode=null;
		boolean first=true;
		for(String row:rows){
			if(first){
				
				first=false;
				continue;
			}
			String[] cols=splieCol(row);
			if(lastItemCode!=null&&!lastItemCode.equals(cols[colsize-1])){
				outItem();
				
			}
			lastItemCode=cols[colsize-1];
			rowList.add(cols);
		}
		
		outItem();
	}
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new CsvFileProcess().init();
		
	}

}
