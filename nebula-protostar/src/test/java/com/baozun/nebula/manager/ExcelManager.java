package com.baozun.nebula.manager;

import java.io.BufferedInputStream;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.text.DecimalFormat;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.Arrays;

import java.util.Date;

import java.util.List;

 

import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import org.apache.poi.hssf.usermodel.HSSFRow;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

 

public class ExcelManager {

	private static final String s = "\t";
	private static Map<String,ItemInfo> itemMap = new HashMap<String,ItemInfo>();
 
	public static void gatherInfo( String[][] result ){
		 for(int i=0;i<result.length;i++) {

//	           for(int j=0;j<result[i].length;j++) {
//
//	               result[i][]
//
//	           }
			 
			 String originalCode = result[i][3];
			 String itemName = result[i][4];
			 String colorCode = result[i][5];
			 String color = result[i][6];
			 String size = result[i][8].replace("/", "-");
			 String upcCode = result[i][9].replace("/", "-");
			 String price = result[i][10];
			 String age = result[i][11];
			 
			 String itemCode = getItemCode(originalCode,colorCode);
			 
			 
			 
			 Sku sku = new Sku();
			 sku.setItemCode(itemCode);
			 sku.setSkuCode(upcCode);
			 sku.setSize(size);
			 
			 if(!itemMap.containsKey(itemCode)){
				 
				 ItemInfo item = new ItemInfo();
				 item.setAge(age);
				 item.setColor(color);
				 item.setItemCode(itemCode);
				 item.setListPrice(price);
				 item.setName(itemName);
				 item.setSalePrice(price);
				 item.setStyle(originalCode);
				 
				 itemMap.put(itemCode, item);
				 
			 }
			 
			 itemMap.get(itemCode).getSkuList().add(sku);

	       }
		 
//		 System.out.println(itemMap.size());
		 
//		 System.out.println("=====================================");
		 
		 for(String key : itemMap.keySet()){
			 ItemInfo item = itemMap.get(key);
			 
			 StringBuilder sb = new StringBuilder();
			 sb.append(item.getName());
			
			 sb.append(s);
			 sb.append(item.getItemCode());
			 sb.append(s);
			 
			 sb.append("");
			 sb.append(s);
			 
			 sb.append("");
			 sb.append(s);
			 
			 sb.append(item.getSalePrice());
			 sb.append(s);
			 sb.append(item.getSalePrice());
			 sb.append(s);
			 
			 sb.append("");
			 sb.append(s);
			 sb.append("");
			 sb.append(s);
			 sb.append("");
			 sb.append(s);
			 sb.append("");
			 sb.append(s);
			 
			 sb.append(item.getStyle());
			 sb.append(s);
			 sb.append(item.getBrand());
			 sb.append(s);
			 sb.append(item.getColor());
			 sb.append(s);
			 sb.append(item.getAge());
			 sb.append(s);
			 
			 System.out.println(sb.toString());
			 
			 for(Sku sku : item.getSkuList()){
				 StringBuilder skusb = new StringBuilder();
				 
				 skusb.append(sku.getItemCode());
				 skusb.append(s);
				 skusb.append(sku.getSkuCode());
				 skusb.append(s);
				 skusb.append(sku.getSize());
				 
//				 System.out.println(skusb.toString());
			 }
//			 System.out.println(item.getItemCode()+"\t"+item.getName()+"\t\t"+item.getSalePrice()+"\t"+item.getListPrice()+"\t\t\t\t"+item.getStyle()+"\t"+item.getBrand()+"\t"+item.getColor()+"\t"+item.getAge());
		 }
//		 System.out.println();
		 
	}
	

    public static void main(String[] args) throws Exception {

       File file = new File("e:\\a.xls");

       String[][] result = getData(file, 1);

       int rowLength = result.length;

       for(int i=0;i<rowLength;i++) {

           for(int j=0;j<result[i].length;j++) {

//              System.out.print(result[i][j]+"\t\t");

           }

//           System.out.println();

       }

       gatherInfo(result);
      

    }

    /**

     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行

     * @param file 读取数据的源Excel

     * @param ignoreRows 读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1

     * @return 读出的Excel中数据的内容

     * @throws FileNotFoundException

     * @throws IOException

     */

    public static String[][] getData(File file, int ignoreRows)

           throws FileNotFoundException, IOException {

       List<String[]> result = new ArrayList<String[]>();

       int rowSize = 0;

       BufferedInputStream in = new BufferedInputStream(new FileInputStream(

              file));

       // 打开HSSFWorkbook

       POIFSFileSystem fs = new POIFSFileSystem(in);

       HSSFWorkbook wb = new HSSFWorkbook(fs);

       HSSFCell cell = null;

       for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {

           HSSFSheet st = wb.getSheetAt(sheetIndex);

           // 第一行为标题，不取

           for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {

              HSSFRow row = st.getRow(rowIndex);

              if (row == null) {

                  continue;

              }

              int tempRowSize = row.getLastCellNum() + 1;

              if (tempRowSize > rowSize) {

                  rowSize = tempRowSize;

              }

              String[] values = new String[rowSize];

              Arrays.fill(values, "");

              boolean hasValue = false;

              for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {

                  String value = "";

                  cell = row.getCell(columnIndex);

                  if (cell != null) {

                     // 注意：一定要设成这个，否则可能会出现乱码

                	 
//                     cell.setEncoding(HSSFCell.ENCODING_UTF_16);

                     switch (cell.getCellType()) {

                     case HSSFCell.CELL_TYPE_STRING:

                         value = cell.getStringCellValue();

                         break;

                     case HSSFCell.CELL_TYPE_NUMERIC:

                         if (HSSFDateUtil.isCellDateFormatted(cell)) {

                            Date date = cell.getDateCellValue();

                            if (date != null) {

                                value = new SimpleDateFormat("yyyy-MM-dd")

                                       .format(date);

                            } else {

                                value = "";

                            }

                         } else {

                            value = new DecimalFormat("0").format(cell

                                   .getNumericCellValue());

                         }

                         break;

                     case HSSFCell.CELL_TYPE_FORMULA:

                         // 导入时如果为公式生成的数据则无值

                         if (!cell.getStringCellValue().equals("")) {

                            value = cell.getStringCellValue();

                         } else {

                            value = cell.getNumericCellValue() + "";

                         }

                         break;

                     case HSSFCell.CELL_TYPE_BLANK:

                         break;

                     case HSSFCell.CELL_TYPE_ERROR:

                         value = "";

                         break;

                     case HSSFCell.CELL_TYPE_BOOLEAN:

                         value = (cell.getBooleanCellValue() == true ? "Y"

                                : "N");

                         break;

                     default:

                         value = "";

                     }

                  }

                  if (columnIndex == 0 && value.trim().equals("")) {

                     break;

                  }

                  values[columnIndex] = rightTrim(value);

                  hasValue = true;

              }

 

              if (hasValue) {

                  result.add(values);

              }

           }

       }

       in.close();

       String[][] returnArray = new String[result.size()][rowSize];

       for (int i = 0; i < returnArray.length; i++) {

           returnArray[i] = (String[]) result.get(i);
//           System.out.println(returnArray[i] );
       }

       return returnArray;

    }

   

    /**

     * 去掉字符串右边的空格

     * @param str 要处理的字符串

     * @return 处理后的字符串

     */

     public static String rightTrim(String str) {

       if (str == null) {

           return "";

       }

       int length = str.length();

       for (int i = length - 1; i >= 0; i--) {

           if (str.charAt(i) != 0x20) {

              break;

           }

           length--;

       }

       return str.substring(0, length);

    }
     
     public static String getItemCode(String originalCode,String colorCode){
    	 StringBuilder sb = new StringBuilder();
    	 sb.append(originalCode).append("_").append(colorCode);
    	 return sb.toString();
     }
     
     

}

class Sku{
	private String itemCode;// 货号
	
	private String skuCode;//条形码
	
	private String size;//尺码

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * @return the skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * @param skuCode the skuCode to set
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}
	
	
}

class ItemInfo{
	private String name;
	
	private String itemCode;
	
	private String listPrice;
	
	private String salePrice;
	
	private String style;
	
	private String brand = "levis";
	
	private String color ;
	
	private String age;
	
	private List<Sku> skuList = new ArrayList<Sku>();;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * @return the listPrice
	 */
	public String getListPrice() {
		return listPrice;
	}

	/**
	 * @param listPrice the listPrice to set
	 */
	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	/**
	 * @return the salePrice
	 */
	public String getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice the salePrice to set
	 */
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(String age) {
		if(age.equals("大童")){
			age = "大童(7-16岁)";
		}else if(age.equals("小童")){
			age = "小童(4-7岁)";
		}else if(age.equals("Toddler")){
			age = "婴童(2-4岁)";
		}else{
			age = "新生儿(0-2岁)";
		}
		this.age = age;
	}

	public void setSkuList(List<Sku> skuList) {
		this.skuList = skuList;
	}

	public List<Sku> getSkuList() {
		return skuList;
	}
	
	
}