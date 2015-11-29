package org.whale.system.router;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.annotation.auth.AuthAdmin;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.domain.Dict;
import org.whale.system.domain.DictItem;
import org.whale.system.service.DictItemService;
import org.whale.system.service.DictService;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/dict")
public class DictRouter extends BaseRouter {

	@Autowired
	private DictService dictService;
	@Autowired
	private DictItemService dictItemService;
	@Autowired
	private DictCacheService dictCacheService;
	
	@Auth(code="dict:list",name="查询字典")
	@RequestMapping("/goTree")
	public ModelAndView goTree(Long clkId){
		if(clkId == null)
			clkId = 0L;
		List<Dict> dictList = this.dictService.queryAll();
		List<DictItem> itemList = this.dictItemService.queryAll();
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(dictList.size()+itemList.size()+1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 0);
		map.put("name", "字典管理");
		map.put("pid", -1L);
		map.put("isParent", true);
		map.put("open", true);
		map.put("orderNo", 0);
		list.add(map);
		for(Dict dict : dictList){
			map = new HashMap<String, Object>();
			map.put("id", dict.getDictId());
			map.put("name", dict.getDictName());
			map.put("pid", 0L);
			map.put("orderNo", dict.getDictId());
			map.put("isParent", true);
			list.add(map);
		}
		for(DictItem item : itemList){
			map = new HashMap<String, Object>();
			map.put("id", "I_"+item.getDictItemId());
			map.put("name", item.getItemName());
			map.put("pid", item.getDictId());
			map.put("orderNo", item.getOrderNo());
			map.put("isParent", false);
			list.add(map);
		}
		
		return new ModelAndView("system/dict/dict_tree")
				.addObject("clkId", clkId)
				.addObject("nodes", JSON.toJSONString(list));
	}
	
	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param dictName
	 * @param dictCode
	 * @return
	 */
	@Auth(code="dict:list",name="查询字典")
	@RequestMapping("/goList")
	public ModelAndView goList(){
		
		return new ModelAndView("system/dict/dict_list");
	}
	
	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param dictName
	 * @param dictCode
	 * @return
	 */
	@Auth(code="dict:list",name="查询字典")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(String dictName, String dictCode){
		Page page = this.newPage();
		page.newCmd(Dict.class).like("dictName", dictName).like("dictCode", dictCode);
		
		this.dictService.queryPage(page);
		
		return page;
	}
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@Auth(code="dict:save",name="新增字典")
	@RequestMapping("/goSave")
	public ModelAndView goSave(){
		return new ModelAndView("system/dict/dict_save");
	}
	
	/**
	 * 跳转到更新页面
	 * @param request
	 * @param response
	 * @param dictId
	 * @return
	 */
	@Auth(code="dict:update",name="修改字典")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(Long dictId){
		Dict dict = this.dictService.get(dictId);
		return new ModelAndView("system/dict/dict_update").addObject("item", dict);
	}
	

	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param dict
	 */
	@Auth(code="dict:save",name="新增字典")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(Dict dict){
		dict.setStatus(SysConstant.STATUS_NORMAL);
		this.dictService.save(dict);
		this.dictCacheService.putDict(dict.getDictCode());
		return Rs.success(dict.getDictId());
	}
	
	/**
	 * 更新操作
	 * @param request
	 * @param response
	 * @param dict
	 */
	@Auth(code="dict:update",name="修改字典")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(Dict dict){		
		if(dict.getStatus() == null)
			dict.setStatus(SysConstant.STATUS_NORMAL);
		
		this.dictService.update(dict);
		this.dictCacheService.putDict(dict.getDictCode());
		return Rs.success();
	}
	
	/**
	 * 删除操作
	 * @param request
	 * @param response
	 * @param dictId
	 */
	@Auth(code="dict:del",name="删除字典")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(Long dictId){
		if(dictId == null){
			return Rs.fail("请选择待删除记录");
		}
		Dict dict = this.dictService.get(dictId);
		this.dictService.transDelete(dictId);
		this.dictCacheService.delDict(Arrays.asList(dict.getDictCode()));
		return Rs.success();
	}
	
	/**
	 * 刷新字典缓存
	 * @date 2015年2月11日 下午2:35:48
	 */
	@AuthAdmin
	@ResponseBody
	@RequestMapping("/doFlush")
	public Rs doFlush(){
		this.dictCacheService.init(null);
		return Rs.success();
	}
}
