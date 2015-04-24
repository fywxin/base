package org.whale.system.cache.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.cache.ICacheService;
import org.whale.system.common.constant.OrderNumConstant;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.RemoteCacheException;
import org.whale.system.common.util.Bootable;
import org.whale.system.common.util.PropertiesUtil;
import org.whale.system.common.util.SpringContextHolder;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.DictDao;
import org.whale.system.dao.DictItemDao;
import org.whale.system.domain.Dict;
import org.whale.system.domain.DictItem;

@Component
public class DictCacheService implements Bootable{
	
	private static final Logger logger = LoggerFactory.getLogger(DictCacheService.class);
	
	public static final String CACHE_PREX = "c_Dict";

	@Autowired
	private DictDao dictDao;
	@Autowired
	private DictItemDao dictItemDao;
	@Autowired(required=false)
	private ICacheService<Dict> cacheService;
	
	
	public void initDicts() {
		logger.info("DICT: 字典初始化开始....");
		
		if(cacheService == null){
			logger.warn("缓存被禁用，采用无缓存模式运行！");
			return ;
		}
		
		List<Dict> dictList = this.dictDao.queryAll();
		for(Dict dict : dictList){
			if(dict != null){
				this.putDict(dict.getDictCode());
			}
		}
		logger.info("DICT: 字典初始化完成！");
	}
	
	/**
	 * 保存到缓存
	 * @param dictCode
	 */
	public void putDict(String dictCode){
		Dict dict = null;
		try {
			if(cacheService == null){
				logger.warn("缓存被禁用，采用无缓存模式运行！");
			}else{
				dict = this.getByDictCodeFromDb(dictCode);
			}
		} catch (Exception e) {
			logger.warn("DICT: 从数据库查找该字典dictCode="+dictCode+" 异常！", e);
		}
		if(dict != null && cacheService != null){
			try {
				this.cacheService.put(CACHE_PREX, dict.getDictCode(), dict, PropertiesUtil.getValueInt("cache.dict.expTime", 2592000));
			} catch (RemoteCacheException e){
				logger.error("CACHE: 远程缓存不可用，设置字典失败 dictCode="+dict.getDictCode(), e);
			}
			logger.info("缓存字典: "+dict);
		}else{
			logger.warn("DICT: 数据库中没有该字典dictCode="+dictCode);
		}
	}
	
	public void clearDicts(){
		logger.info("AUTH: 字典清空开始....");
		List<Dict> dictList = this.dictDao.queryAll();
		if(dictList != null){
			List<String> dictCodes = new ArrayList<String>();
			for(Dict dict : dictList){
				if(dict != null){
					dictCodes.add(dict.getDictCode());
				}
			}
			
			this.delDict(dictCodes);
		}
		logger.info("AUTH: 字典清空完成!");
	}
	
	public void delDict(List<String> dictCodes){
		try {
			if(cacheService == null){
				logger.warn("缓存被禁用，采用无缓存模式运行！");
			}else{
				this.cacheService.mdel(CACHE_PREX, dictCodes);
				logger.info("CACHE: 字典["+dictCodes+"]删除完成！");
			}
		} catch (RemoteCacheException e) {
			logger.error("CACHE: 远程缓存不可用！", e);
		}
	}
	
	public Dict getDict(String dictCode){
		if (Strings.isBlank(dictCode))
			return null;
		Dict dict = null;
		boolean cacheDown = false;
		try{
			if(cacheService == null){
				logger.warn("缓存被禁用，采用无缓存模式运行！");
			}else{
				dict = (Dict)cacheService.get(CACHE_PREX, dictCode);
			}
		}catch(RemoteCacheException e){
			logger.error("CACHE: 远程缓存不可用，取字典失败 dictCode="+dictCode, e);
			cacheDown = true;
		}
		if(dict == null){
			logger.info("CACHE: 缓存不存在字典编码["+dictCode+"]数据，开始从数据库查找...");
			dict = this.getByDictCodeFromDb(dictCode);
			if(!cacheDown && dict != null && this.cacheService != null){
				try {
					this.cacheService.put(CACHE_PREX, dictCode, dict, PropertiesUtil.getValueInt("cache.dict.expTime", 2592000));
				} catch (RemoteCacheException e){
					logger.error("CACHE: 远程缓存不可用，设置字典失败 dictCode="+dictCode, e);
					cacheDown = true;
				}
			}
		}
		return dict;	
	}
	
	/**
	 * 从数据库获取数据
	 * @param dictCode
	 * @return
	 */
	private Dict getByDictCodeFromDb(String dictCode){
		Dict dict = this.dictDao.getByDictCode(dictCode);
		if(dict != null){
			List<DictItem> items = this.dictItemDao.getByDictId(dict.getDictId());
			if(items != null){
				Collections.sort(items, new Comparator<DictItem>() {
					@Override
					public int compare(DictItem o1, DictItem o2) {
						return o1.getOrderNo() - o2.getOrderNo();
					}
				});
				dict.setItems(items);
			}
		}else{
			logger.warn("DICT: 数据库中查找不到dictCode="+dictCode);
		}
			
		return dict;
	}
	
	/**
	 * 获取字典元素 (默认不包含禁用元素)
	 * 
	 * @param dictCode
	 * @return
	 */
	public List<DictItem> getItemsByDictCode(String dictCode) {
		return this.getItemsByDictCode(dictCode, true);
	}
	
	/**
	 * 获取字典元素
	 * @param dictCode
	 * @param noDisabled 是否包含禁用元素
	 * @return
	 */
	public List<DictItem> getItemsByDictCode(String dictCode, boolean noDisabled) {
		Dict dict = getDict(dictCode);
		if (dict == null)
			return null;
		List<DictItem> list = dict.getItems();
		if (list == null || list.size() < 1) {
			logger.warn("缓存中字典编码 [" + dictCode + "] 的字典元素为空");
			return null;
		}
		if(noDisabled){
			for(int i=list.size()-1; i>=0; i--){
				if(SysConstant.STATUS_UNUSE == list.get(i).getStatus()){
					list.remove(i);
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据字典编码或元素编码获取字典元素对象
	 * @param dictCode
	 * @param itemCode
	 * @return
	 */
	public DictItem getByDictCodeAndItemCode(String dictCode, String itemCode) {
		if (Strings.isBlank(itemCode))
			return null;
		List<DictItem> list = getItemsByDictCode(dictCode);
		if (list == null || list.size() < 1)
			return null;
		for (DictItem dictItem : list) {
			if (itemCode.trim().equals(dictItem.getItemCode()))
				return dictItem;
		}
		logger.warn("缓存中字典编码 [" + dictCode + "] 字典元素编码 [" + itemCode
				+ "] 的字典元素不存在");
		return null;
	}
	
	/**
	 * 根据字典编码或元素编码获取字典元素值
	 * @param dictCode
	 * @param itemCode
	 * @return
	 */
	public String getItemValue(String dictCode, String itemCode) {
		DictItem dictItem = getByDictCodeAndItemCode(dictCode, itemCode);
		if (dictItem == null)
			return null;
		String value = dictItem.getItemVal();
		if (Strings.isBlank(value)) {
			logger.warn("缓存中字典编码 [" + dictCode + "] 字典元素编码 [" + itemCode
					+ "] 的字典元素的文本值为空");
		}
		return value;
	}
	
	/**
	 * 根据字典编码或元素编码获取字典元素值
	 * @param dictCode
	 * @param itemCode
	 * @param defVal
	 * @return
	 */
	public String getItemValue(String dictCode, String itemCode, String defVal) {
		String value = getItemValue(dictCode, itemCode);
		if(Strings.isBlank(value))
			value = defVal;
		return value;
	}
	
	/**
	 * 获取Integer值
	 * @param dictCode
	 * @param itemCode
	 * @return
	 */
	public Integer getItemIntValue(String dictCode, String itemCode) {
		return this.getItemIntValue(dictCode, itemCode, null);
	}
	
	/**
	 * 获取Integer值
	 * @param dictCode
	 * @param itemCode
	 * @param defVal
	 * @return
	 */
	public Integer getItemIntValue(String dictCode, String itemCode, Integer defVal){
		String val = getItemValue(dictCode, itemCode);
		if(val == null)
			return defVal;
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}
	
	/**
	 * 根据字典编码或元素编码获取字典元素名称
	 * 
	 * @param dictCode
	 *            字典编码
	 * @param itemCode
	 *            元素编码
	 * @return 字典元素名称
	 */
	public String getItemLabel(String dictCode, String itemCode) {
		DictItem dictItem = getByDictCodeAndItemCode(dictCode, itemCode);
		if (dictItem == null)
			return null;
		String label = dictItem.getItemName();
		if (Strings.isBlank(label)) {
			logger.warn("缓存中字典编码 [" + dictCode + "] 字典元素编码 [" + itemCode
					+ "] 的字典元素的文本值为空");
		}
		return label;
	}

	/**
	 * 字典中是否包含有某个值
	 * 
	 * @param dictCode
	 * @param value
	 * @return boolean
	 * 
	 */
	public boolean hasItemValue(String dictCode, String value) {
		if (Strings.isBlank(value))
			return false;
		List<DictItem> dictItems = getItemsByDictCode(dictCode);
		if (dictItems == null || dictItems.size() < 1)
			return false;
		for (DictItem item : dictItems) {
			if (value.trim().equals(item.getItemVal())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 字典项值是否为该值
	 * @param dictCode
	 * @param itemCode
	 * @param value
	 * @return
	 */
	public boolean isValue(String dictCode, String itemCode, String value){
		DictItem item = this.getByDictCodeAndItemCode(dictCode, itemCode);
		if(item == null || Strings.isBlank(item.getItemVal())){
			return false;
		}
		return item.getItemVal().equals(value);
	}
	
	/**
	 * 生成选择项
	 * @param dictCode
	 * @param initValue
	 * @return
	 */
	public String bulidOptions(String dictCode, String initValue){
		List<DictItem> dictItems = getItemsByDictCode(dictCode);
		if(dictItems == null || dictItems.size() < 1)
			return "";
		StringBuilder strb = new StringBuilder();
		for(DictItem dictItem : dictItems){
			if(SysConstant.STATUS_NORMAL == dictItem.getStatus()){
				strb.append("<option value=").append(dictItem.getItemCode());
				if(initValue != null && dictItem.getItemCode().equals(initValue.trim())){
					strb.append(" selected = selected ");
				}
				strb.append(" >").append(dictItem.getItemName()).append("</option>");
			}
		}
		return strb.toString();
	}
	
	
	@Override
	public Object init(Map<String, Object> context) {
		this.initDicts();
		return null;
	}
	
	@Override
	public boolean access() {
		return true;
	}
	
	public static DictCacheService getThis(){
		return SpringContextHolder.getBean(DictCacheService.class);
	}

	@Override
	public int getOrder() {
		return OrderNumConstant.DICT_CACHE_INIT_ORDER;
	}
}
