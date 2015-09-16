package org.whale.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.annotation.auth.AuthAdmin;
import org.whale.system.base.BaseController;
import org.whale.system.base.Page;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.WebUtil;
import org.whale.system.domain.Dict;
import org.whale.system.domain.DictItem;
import org.whale.system.jqgrid.Grid;
import org.whale.system.service.DictItemService;
import org.whale.system.service.DictService;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/dict2")
public class Dict2Controller extends BaseController {

	@Autowired
	private DictService dictService;
	@Autowired
	private DictItemService dictItemService;
	@Autowired
	private DictCacheService dictCacheService;
	
	@Auth(code="DICT_LIST",name="查询字典")
	@RequestMapping("/goTree")
	public ModelAndView goTree(HttpServletRequest request, HttpServletResponse response, Long clkId){
		if(clkId == null)
			clkId = 0L;
		List<Dict> dictList = this.dictService.queryAll();
		List<DictItem> itemList = this.dictItemService.queryAll();
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(dictList.size()+itemList.size()+1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 0);
		map.put("name", "字典管理");
		map.put("pid", -1L);
		map.put("orderNo", 0);
		list.add(map);
		for(Dict dict : dictList){
			map = new HashMap<String, Object>();
			map.put("id", dict.getDictId());
			map.put("name", dict.getDictName());
			map.put("pid", 0L);
			map.put("orderNo", dict.getDictId());
			list.add(map);
		}
		for(DictItem item : itemList){
			map = new HashMap<String, Object>();
			map.put("id", "I_"+item.getDictItemId());
			map.put("name", item.getItemName());
			map.put("pid", item.getDictId());
			map.put("orderNo", item.getOrderNo());
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
	@Auth(code="DICT_LIST",name="查询字典")
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response){
		
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
	@Auth(code="DICT_LIST",name="查询字典")
	@RequestMapping("/doList")
	public void doList(HttpServletRequest request, HttpServletResponse response, String dictName, String dictCode){
		Page page = Grid.newPage(request);
		page.newCmd(Dict.class).like("dictName", dictName).like("dictCode", dictCode);
		
		this.dictService.queryPage(page);
		
		WebUtil.print(request, response, Grid.grid(page));
	}
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@Auth(code="DICT_SAVE",name="新增字典")
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("system/dict/dict_save");
	}
	
	/**
	 * 跳转到更新页面
	 * @param request
	 * @param response
	 * @param dictId
	 * @return
	 */
	@Auth(code="DICT_UPDATE",name="修改字典")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, Long dictId){
		Dict dict = this.dictService.get(dictId);
		return new ModelAndView("system/dict/dict_update").addObject("item", dict);
	}
	

	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param dict
	 */
	@Auth(code="DICT_SAVE",name="新增字典")
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, Dict dict){
		dict.setStatus(SysConstant.STATUS_NORMAL);
		this.dictService.save(dict);
		this.dictCacheService.putDict(dict.getDictCode());
		WebUtil.printSuccess(request, response, dict.getDictId());
	}
	
	/**
	 * 更新操作
	 * @param request
	 * @param response
	 * @param dict
	 */
	@Auth(code="DICT_UPDATE",name="修改字典")
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, Dict dict){		
		if(dict.getStatus() == null)
			dict.setStatus(SysConstant.STATUS_NORMAL);
		
		this.dictService.update(dict);
		this.dictCacheService.putDict(dict.getDictCode());
		WebUtil.printSuccess(request, response);
	}
	
	/**
	 * 删除操作
	 * @param request
	 * @param response
	 * @param dictId
	 */
	@Auth(code="DICT_DEL",name="删除字典")
	@RequestMapping("/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, Long dictId){
		if(dictId == null){
			WebUtil.printFail(request, response, "请选择待删除记录");
			return ;
		}
		Dict dict = this.dictService.get(dictId);
		this.dictService.delete(dictId);
		this.dictCacheService.delDict(Arrays.asList(dict.getDictCode()));
		WebUtil.printSuccess(request, response);
	}
	
	/**
	 * 刷新字典缓存
	 * @date 2015年2月11日 下午2:35:48
	 */
	@AuthAdmin
	@RequestMapping("/doFlush")
	public void doFlush(HttpServletRequest request, HttpServletResponse response){
		this.dictCacheService.init(null);
		WebUtil.printSuccess(request, response);
	}
}
