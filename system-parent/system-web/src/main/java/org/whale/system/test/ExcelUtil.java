package org.whale.system.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.whale.system.common.exception.BusinessException;

public class ExcelUtil {
	
	private static final Logger logger = LoggerFactory .getLogger(ExcelUtil.class);

	public static Workbook get(InputStream is){
		try {
			return Workbook.getWorkbook(is);
		} catch (Exception e) {
			logger.error("璇诲彇excel鍑虹幇寮傚父锛�"+e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, String> readPairCols(Sheet sheet) {
		return readPairCols(sheet, 0, 1);
	}
	
	public static Map<String, String> readPairCols(Sheet sheet, int keyCol, int valCol){
		if(keyCol < 0 || valCol < 0){
			throw new BusinessException("keyCol < 0 OR valCol < 0");
		}
		int maxCol = keyCol > valCol ? keyCol : valCol;
		
		int rowsNum = sheet.getRows();
		Map<String, String> map = new HashMap<String, String>();
		List<String> datas = null;
		for(int i=0; i<rowsNum; i++){
			datas = read1Line(sheet, i);
			if(datas.size() <= maxCol){
				throw new BusinessException("keyCol OR valCol 瓒呰繃鏁扮粍涓嬬嚎");
			}
			
			if(datas.get(keyCol) == null) 
				continue;
			map.put(datas.get(keyCol), datas.get(valCol));
		}
		return map;
	}
	
	public static List<Map<String, String>> readLineCols(Sheet sheet, int keyRow){
		if(keyRow < 0){
			throw new BusinessException("keyRow < 0 ");
		}
		
		int rowsNum = sheet.getRows();
		if(rowsNum > keyRow){
			throw new BusinessException("鏍囬琛屽彿 閿欒 rowsNum > keyRow");
		}
		
		List<Map<String, String>> list = new ArrayList<Map<String,String>>(rowsNum-keyRow);
		
		List<String> keys = read1Line(sheet, keyRow);
		if(keys == null || keys.size() < 1)
			throw new BusinessException("鎵句笉鍒拌鍙穂"+keyRow+"]瀵瑰簲鐨勬爣棰�");
		
		Map<String, String> map = null;
		List<String> vals = null;
		for(int i = keyRow+1; i< rowsNum; i++){
			vals = read1Line(sheet, i);
			map = new HashMap<String, String>();
			for(int j=0; j < keys.size(); j++){
				map.put(keys.get(j).trim(), vals.get(j).trim());
			}
			list.add(map);
		}
		
		return list;
	}
	
	public static List<String> read1Line(Sheet sheet, int rowNum){
		Cell[] cells = sheet.getRow(rowNum);
		if(cells == null || cells.length < 1)
			return null;
		List<String> rs = new ArrayList<String>(cells.length);
		for(Cell cell : cells){
			rs.add(cell.getContents().trim());
		}
		return rs;
	}
	
}
