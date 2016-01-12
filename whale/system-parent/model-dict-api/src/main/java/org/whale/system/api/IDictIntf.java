package org.whale.system.api;

import java.util.List;
import java.util.Map;

import org.whale.system.domain.Dict;
import org.whale.system.domain.DictItem;

/**
 * 字典接口
 * 
 * @author wjs
 *
 */
public interface IDictIntf {

	public Dict getDict(String dictCode);
	
	public List<DictItem> getItemsByDictCode(String dictCode, boolean noDisabled);
	
	public DictItem getByDictCodeAndItemCode(String dictCode, String itemCode);
	
	public String getItemValue(String dictCode, String itemCode);
	
	public String getItemValueExt(String dictCode, String itemCode);
	
	public Integer getItemIntValue(String dictCode, String itemCode);
	
	public String getItemLabel(String dictCode, String itemCode);
	
	public boolean hasItemValue(String dictCode, String value);
	
	public boolean isValue(String dictCode, String itemCode, String value);
	
	public Map<String, Object> getMap(String dictCode);
	
	public String getMapJson(String dictCode);
}
