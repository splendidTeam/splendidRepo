package com.baozun.nebula.manager.product;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.baozun.nebula.manager.BaseManager;

public interface SkuExportImportManager extends BaseManager  {
	/**
	 * 根据选中的列和输入的商品编码来生成excel文件,并导出
	 * @param shopId
	 * @param industryId
	 * @param selectCodes
	 * @param itemCodes
	 * @param excelFile
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook itemExport(Long shopId, Long industryId, String[] selectCodes, String itemCodes, File excelFile)throws IOException;
	
	
	/**
	 * 导入商品, 并刷新SOLR索引
	 * @param inputStream
	 * @param shopId
	 * @return List<Long> 刷新solr的商品ID集合
	 */
	public List<Long> itemImport(InputStream inputStream, Long shopId);
}
