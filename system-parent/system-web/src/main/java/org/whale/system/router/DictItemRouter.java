package org.whale.system.router;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.annotation.log.Log;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.LangUtil;
import org.whale.system.domain.Dict;
import org.whale.system.domain.DictItem;
import org.whale.system.annotation.log.LogHelper;
import org.whale.system.service.DictItemService;
import org.whale.system.service.DictService;

@Log(module = "字典元素", value = "")
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
	 * @param dictId
	 * @return
	 */
	@Auth(code="dictItem:list",name="查询元素")
	@RequestMapping("/goList")
	public ModelAndView goList(Long dictId){
		
		return new ModelAndView("system/dict/item/item_list")
			.addObject("dictId", dictId);
	}

	/**
	 * 跳转到列表页面
	 * @param dictId
	 * @param itemName
	 * @param itemCode
	 * @return
	 */
	@Auth(code="dictItem:list",name="查询元素")
	@ResponseBody
	@RequestMapping("/doList")
	public Page doList(Long dictId, String itemName, String itemCode){
		Page page = this.newPage();
		page.newCmd(DictItem.class).eq("dictId", dictId).like("itemName", itemName).like("itemCode", itemCode);
		
		this.dictItemService.queryPage(page);
		
		return page;
	}

	@Auth(code="dictItem:save",name="新增元素")
	@RequestMapping("/goSave")
	public ModelAndView goSave(Long dictId){
		Dict dict = this.dictService.get(dictId);
		return new ModelAndView("system/dict/item/item_save")
				.addObject("dictId", dictId)
				.addObject("dictName", dict.getDictName())
				.addObject("nextNum", this.dictItemService.getCurOrder(dictId)+1);
	}

	/**
	 * 保存操作
	 * @param dictItem
	 */
	@Log("新增字典元素 名称:{}, 编码：{}, 值：{}")
	@Auth(code="dictItem:save",name="新增元素")
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

		LogHelper.addPlaceHolder(dictItem.getItemName(), dictItem.getItemCode(), dictItem.getItemVal());
		return Rs.success(dictItem.getDictItemId());
	}
	
	
	/**
	 * 跳转到更新页面
	 * @param dictItemId
	 * @return
	 */
	@Auth(code="dictItem:update",name="修改元素")
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(Long dictItemId){
		DictItem dictItem = this.dictItemService.get(dictItemId);
		Dict dict = this.dictService.get(dictItem.getDictId());
		return new ModelAndView("system/dict/item/item_update").addObject("item", dictItem).addObject("dictName", dict.getDictName());
	}
	
	/**
	 * 跳转到查看页面
	 * @param dictItemId
	 * @return
	 */
	@Auth(code="dictItem:list",name="查看元素")
	@RequestMapping("/goView")
	public ModelAndView goView(Long dictItemId){
		DictItem dictItem = this.dictItemService.get(dictItemId);
		String dictName = "";
		Dict dict = this.dictService.get(dictItem.getDictId());
		if(dict != null){
			dictName = dict.getDictName();
		}
		return new ModelAndView("system/dict/item/item_view").addObject("item", dictItem).addObject("dictName", dictName);
	}

	/**
	 * 更新操作
	 * @param dictItem
	 */
	@Log("修改字典元素 名称:{}, 编码：{}, 值：{}")
	@Auth(code="dictItem:update",name="修改元素")
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(DictItem dictItem){
		if(dictItem.getOrderNo() == null)
			dictItem.setOrderNo(1);
		dictItem.setStatus(SysConstant.STATUS_NORMAL);
		
		this.dictItemService.update(dictItem);
		
		Dict dict = this.dictService.get(dictItem.getDictId());
		this.dictCacheService.putDict(dict.getDictCode());

		LogHelper.addPlaceHolder(dictItem.getItemName(), dictItem.getItemCode(), dictItem.getItemVal());
		return Rs.success();
	}
	
	/**
	 * 删除操作
	 * @param ids
	 */
	@Log("删除字典元素 名称:{}, 编码：{}")
	@Auth(code="dictItem:del",name="删除元素")
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

		LogHelper.addPlaceHolder(dictItem.getItemName(), dictItem.getItemCode());
		return Rs.success();
	}

	@Log("启禁字典元素 名称:{}, 编码：{}, 状态:{}")
	@Auth(code="dictItem:status",name="启禁元素")
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

		LogHelper.addPlaceHolder(dictItem.getItemName(), dictItem.getItemCode(), status == SysConstant.STATUS_NORMAL? "启用":"禁用");
		return Rs.success();
	}
}
