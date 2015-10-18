package org.whale.system.router;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.LangUtil;
import org.whale.system.domain.Dict;
import org.whale.system.domain.DictItem;
import org.whale.system.service.DictItemService;
import org.whale.system.service.DictService;

@Controller
@RequestMapping("/dictItem")
public class DictItemRouter extends BaseRouter {

	@Autowired
	private DictService dictService;
	@Autowired
	private DictItemService dictItemService;
	@Autowired
	private DictCacheService dictCacheService;
	
	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param dictItemName
	 * @param dictItemCode
	 * @return
	 */
	@Auth(code="ITEM_LIST",name="查询元素")
	@RequestMapping("/goList")
	public ModelAndView goList(Long dictId){
		
		return new ModelAndView("system/dict/item/item_list")
			.addObject("dictId", dictId);
	}
	
	/**
	 * 跳转到列表页面
	 * @param request
	 * @param response
	 * @param dictItemName
	 * @param dictItemCode
	 * @return
	 */
	@Auth(code="ITEM_LIST",name="查询元素")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(Long dictId, String itemName, String itemCode){
		Page page = this.newPage();
		page.newCmd(DictItem.class).eq("dictId", dictId).like("itemName", itemName).like("itemCode", itemCode);
		
		this.dictItemService.queryPage(page);
		
		return page;
	}
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@Auth(code="ITEM_SAVE",name="新增元素")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long dictId){
		
		return new ModelAndView("system/dict/item/item_save")
				.addObject("dictId", dictId)
				.addObject("nextNum", this.dictItemService.getCurOrder(dictId)+1);
	}

	/**
	 * 保存操作
	 * @param request
	 * @param response
	 * @param dictItem
	 */
	@Auth(code="ITEM_SAVE",name="新增元素")
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(DictItem dictItem){
		if(dictItem.getOrderNo() == null)
			dictItem.setOrderNo(1);
		dictItem.setStatus(SysConstant.STATUS_NORMAL);
		
		if(this.dictItemService.getByDictIdAndItemCode(dictItem.getDictId(), dictItem.getItemCode()) != null){
			return Rs.fail("元素编码["+dictItem.getItemCode()+"] 已存在本字典中");
		}
		
		this.dictItemService.save(dictItem);
		Dict dict = this.dictService.get(dictItem.getDictId());
		this.dictCacheService.putDict(dict.getDictCode());
		
		return Rs.success(dictItem.getDictItemId());
	}
	
	
	/**
	 * 跳转到更新页面
	 * @param request
	 * @param response
	 * @param dictItemId
	 * @return
	 */
	@Auth(code="ITEM_UPDATE",name="修改元素")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(Long dictItemId, Integer view){
		DictItem dictItem = this.dictItemService.get(dictItemId);
		return new ModelAndView("system/dict/item/item_update").addObject("item", dictItem).addObject("view", view);
	}
	
	/**
	 * 跳转到查看页面
	 * @param request
	 * @param response
	 * @param dictItemId
	 * @return
	 */
	@Auth(code="ITEM_VIEW",name="查看元素")
	@RequestMapping("/goView")
	public ModelAndView goView(Long dictItemId){
		DictItem dictItem = this.dictItemService.get(dictItemId);
		return new ModelAndView("system/dict/item/item_view").addObject("item", dictItem);
	}

	/**
	 * 更新操作
	 * @param request
	 * @param response
	 * @param dictItem
	 */
	@Auth(code="ITEM_UPDATE",name="修改元素")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(DictItem dictItem){
		if(dictItem.getOrderNo() == null)
			dictItem.setOrderNo(1);
		dictItem.setStatus(SysConstant.STATUS_NORMAL);
		
		this.dictItemService.update(dictItem);
		
		Dict dict = this.dictService.get(dictItem.getDictId());
		this.dictCacheService.putDict(dict.getDictCode());
		return Rs.success();
	}
	
	/**
	 * 删除操作
	 * @param request
	 * @param response
	 * @param dictItemId
	 */
	@Auth(code="ITEM_DEL",name="删除元素")
	@ResponseBody
	@RequestMapping("/doDelete")
	public Rs doDelete(String ids){
		List<Long> dictItemIds = LangUtil.splitIds(ids);
		if(dictItemIds == null || dictItemIds.size() < 1){
			return Rs.fail("请选择待删除记录");
		}
		DictItem dictItem = this.dictItemService.get(dictItemIds.get(0));
		if(dictItem == null){
			return Rs.fail("删除记录错误");
		}
		this.dictItemService.deleteBatch(dictItemIds);
		Dict dict = this.dictService.get(dictItem.getDictId());
		this.dictCacheService.putDict(dict.getDictCode());
		return Rs.success();
	}
	
	@Auth(code="ITEM_STATUS",name="启禁元素")
	@ResponseBody
	@RequestMapping("/doChangeState")
	public Rs doChangeState(Long dictItemId, Integer status){
		DictItem dictItem = null;
		if(dictItemId == null || (dictItem = this.dictItemService.get(dictItemId)) == null){
			return Rs.fail("字典元素不存在");
		}
		
		if(status != SysConstant.STATUS_NORMAL && status != SysConstant.STATUS_UNUSE){
			return Rs.fail("非法状态值");
		}
		
		dictItem.setStatus(status);
		this.dictItemService.update(dictItem);
		
		Dict dict = this.dictService.get(dictItem.getDictId());
		this.dictCacheService.putDict(dict.getDictCode());
		return Rs.success();
	}
}
