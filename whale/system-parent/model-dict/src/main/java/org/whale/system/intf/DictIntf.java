package org.whale.system.intf;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.whale.inf.common.Result;
import org.whale.system.annotation.web.ReqParam;
import org.whale.system.annotation.web.RespBody;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.domain.Dict;
import org.whale.system.domain.DictItem;

@Controller
@RequestMapping("/dictIntf")
public class DictIntf {
	
	@Autowired
	private DictCacheService dictCacheService;

	@RespBody
	@RequestMapping("/getDict")
	public Result<Dict> getDict(@ReqParam String dictCode) {
		
		return Result.success(this.dictCacheService.getDict(dictCode));
	}

	@RespBody
	@RequestMapping("/getItemsByDictCode")
	public Result<List<DictItem>> getItemsByDictCode(@ReqParam String dictCode, @ReqParam boolean noDisabled) {
		return Result.success(this.dictCacheService.getItemsByDictCode(dictCode));
	}

	@RespBody
	@RequestMapping("/getByDictCodeAndItemCode")
	public Result<DictItem> getByDictCodeAndItemCode(@ReqParam String dictCode, @ReqParam String itemCode) {
		return Result.success(this.dictCacheService.getByDictCodeAndItemCode(dictCode, itemCode));
	}

	@RespBody
	@RequestMapping("/getItemValue")
	public Result<String> getItemValue(@ReqParam String dictCode, @ReqParam String itemCode) {
		return Result.success(this.dictCacheService.getItemValue(dictCode, itemCode));
	}

	@RespBody
	@RequestMapping("/getItemValueExt")
	public Result<String> getItemValueExt(@ReqParam String dictCode, @ReqParam String itemCode) {
		return Result.success(this.dictCacheService.getItemValueExt(dictCode, itemCode));
	}

	@RespBody
	@RequestMapping("/getItemIntValue")
	public Result<Integer> getItemIntValue(@ReqParam String dictCode, @ReqParam String itemCode) {
		return Result.success(this.dictCacheService.getItemIntValue(dictCode, itemCode));
	}

	@RespBody
	@RequestMapping("/getItemLabel")
	public Result<String> getItemLabel(@ReqParam String dictCode, @ReqParam String itemCode) {
		return Result.success(this.dictCacheService.getItemLabel(dictCode, itemCode));
	}

	@RespBody
	@RequestMapping("/hasItemValue")
	public Result<Boolean> hasItemValue(@ReqParam String dictCode, @ReqParam String value) {
		return Result.success(this.dictCacheService.hasItemValue(dictCode, value));
	}

	@RespBody
	@RequestMapping("/isValue")
	public Result<Boolean> isValue(@ReqParam String dictCode, @ReqParam String itemCode, @ReqParam String value) {
		return Result.success(this.dictCacheService.isValue(dictCode, itemCode, value));
	}

	@RespBody
	@RequestMapping("/getMap")
	public Result<Map<String, Object>> getMap(@ReqParam String dictCode) {
		return Result.success(this.dictCacheService.getMap(dictCode));
	}

	@RespBody
	@RequestMapping("/getMapJson")
	public Result<String> getMapJson(@ReqParam String dictCode) {
		return Result.success(this.dictCacheService.getMapJson(dictCode));
	}

}
