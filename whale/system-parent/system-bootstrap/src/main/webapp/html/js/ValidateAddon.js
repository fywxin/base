//--------------------------------------------公共验证方法----------------------------------------------------

//校验非法字符
jQuery.validator.addMethod("validIllegalChar", function(value, element) {
	var val = value.toLowerCase();
	if(val.indexOf('<') != -1 
				|| val.indexOf('&amp') != -1
				|| val.indexOf('&lt') != -1
				|| val.indexOf('&gt') != -1
				|| val.indexOf('&quot') != -1
				|| val.indexOf('&nbsp') != -1
				|| val.indexOf('&copy') != -1
				|| val.indexOf('&apos') != -1
				|| val.indexOf('&reg') != -1
				|| val.indexOf('\"') != -1
				|| val.indexOf('\'') != -1){
		return false;
	}
	return true;
}, "非法字符");

//正则表达式验证
jQuery.validator.addMethod("pattern", function(value, element, param) {
    return this.optional(element) || param.test(value);
}, "Invalid format.");

//手机验证
jQuery.validator.addMethod("isMobile", function(phone_number, element) {
    phone_number = phone_number.replace(/\s+/g, "");
	return this.optional(element) || phone_number.length == 11 &&
		(/^13\d{9}$/.test(phone_number) | /^15\d{9}$/.test(phone_number) | /^18\d{9}$/.test(phone_number)|/^14\d{9}$/.test(phone_number));
}, "格式错误");

//不能包含空格
jQuery.validator.addMethod("nowhitespace", function(value, element) {
	return this.optional(element) || /^\S+$/i.test(value);
}, "不能包含空格");
 
	 
//验证正实数
jQuery.validator.addMethod("doubleNum", function(value, element) {
	 
	return this.optional(element) || /^[-\+]?\d+(\.\d+)?$/.test(value) && parseFloat(value)>0;
}, "非法正实数");

//验证金额
jQuery.validator.addMethod("money", function(value, element) {
	return this.optional(element) || (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
}, "非法金额");

//只能为整数
jQuery.validator.addMethod("integer", function(value, element) {
	return this.optional(element) || /^-?\d+$/.test(value);
}, "只能为整数");

//只能为英文字母
jQuery.validator.addMethod("lettersonly", function(value, element) {
	return this.optional(element) || /^[a-z]+$/i.test(value);
}, "只能为英文字母");

//字母数字下划线
jQuery.validator.addMethod("alphanumeric", function(value, element) {
	return this.optional(element) || /^\w+$/i.test(value);
}, "字母数字下划线");

//图片控件验证，验证图片长度宽度，大小
jQuery.validator.addMethod("icon", function(value, element,param) {
	if(value == null || $.trim(value) == "" || typeof(value) == "undefined"){
		return true;
	}
	if(typeof(param.rulesType) == "undefied" || param.rulesType == ""){
		param.rulesType = "all";
	}
	
	var id = $(element).attr("fileid");
	var height = $("#img_"+id).attr("imgheight")+"";
	var width = $("#img_"+id).attr("imgwidth")+"";
	var size = parseInt($("#img_"+id).attr("imgsize"));
	
	if(param.rulesType == 'capacity'){
		return size < parseInt(param.sizeIcon);
	}else if(param.rulesType == 'size'){
		if(height != param.heightIcon+""){
			return false;
		}else if(width != param.widthIcon+""){
			return false;
		}
	}else{
		if(height != param.heightIcon+""){
			return false;
		}else if(width != param.widthIcon+""){
			return false;
		}else if(size > parseInt(param.sizeIcon)){
			return false;
		}
	}
	return true;
}, "图片大小不符合");

//密码验证
jQuery.validator.addMethod("validPwd", function(value, element) { 
	var flag = true;
	var pwd = $.trim($(element).val());
    var regular = /^[0-9a-zA-Z]*$/; 
    flag = regular.test(pwd);  
	return flag;
}, "密码只能是数字和字母 !");

//ipv4 验证
jQuery.validator.addMethod("ipv4", function(value, element, param) {
    return this.optional(element) || /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/i.test(value);
}, "IP地址格式错误");

//ipv6 验证
jQuery.validator.addMethod("ipv6", function(value, element, param) {
    return this.optional(element) || /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i.test(value);
}, "IP地址格式错误");

jQuery.validator.addMethod("url2", function(value, element, param) {
	return this.optional(element) || /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)*(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
}, jQuery.validator.messages.url);

/**
 * ajaxValidate 服务端验证
 * param {url:'必填',data:'传递的数据，为空：则自动绑定当前值，非空：则合并上当前值',若为字符串：则直接拼接到url,before:前置方法,after:后置方法}
 * 服务端返回格式 {"rs":true,"msg":"","datas":{}}  可直接调用router中的： this.outPrintSuccess(response,"")  || this.outPrintFail(response, "占用");
 * messages 中如果未填写 ajaxValidate:'' 属性，则错误信息为服务端返回的this.outPrintFail(response, "已存在") 的提示信息 "已存在"
 * @author wjs 20120329
 */
jQuery.validator.addMethod("ajaxValidate", function(value, element, param) {
	
	//url 验证
	if(typeof(param.url)  == 'undefined' || param.url == null || $.trim(param.url) == '') {
		alert("参数URL为空");
		return false;
	}
	
	//前置方法接口 参数为validator对象
	if(typeof(param.before) == 'function') {
		if(!param.before(this)){
			return true;
		}
	}
	
	//将url+上时间搓
	var url = $.trim(param.url + "");
	while(url.charAt(url.length-1) == '/'){
		url = url.substring(0,url.length-2);
	}
	if(url.indexOf("?") == -1){
		url += "?"+new Date().getTime();
	}else{
		url += "&"+new Date().getTime();
	}
	
	//将用户参数传递到服务端，并+入被验证字段的属性值
	var data = {};
	if(typeof(param.data) == 'undefined'){
		data[$(element).attr("name")] = $.trim($(element).val());
	}
	
	if(typeof(param.data) == 'object'){
		data = param.data;
		data[$(element).attr("name")] = $.trim($(element).val());
	}
	
	//数据位字符串类型
	if(typeof(param.data) == 'string'){
		var pData = $.trim(param.data);
		url += "&"+pData+"&" + $(element).attr("name") + "=" + $.trim($(element).val());
	}
	
	var validator = this;
	var previous = this.previousValue(element);
	if (!this.settings.messages[element.name] )
		this.settings.messages[element.name] = {};
	previous.originalMessage = this.settings.messages[element.name].ajaxValidate;
	this.settings.messages[element.name].ajaxValidate = previous.originalMessage;

	if ( this.pending[element.name] ) {
		return "pending";
	}
	if ( previous.old === value ) {
		return previous.valid;
	}
	
	previous.old = value;
	this.startRequest(element);
	
	$.ajax({
		url : url,
		type : 'post',
		data : data,
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		dataType: 'json',
		timeout: 10000,
	    error: function(){
	        alert('验证出现错误');
	        return false;
	    },
	    success: function(obj){
	    	if(typeof(param.after) == 'function') {
				param.after(validator,obj);
			}
			validator.settings.messages[element.name].ajaxValidate = previous.originalMessage;
			$(element).parent().find("em").remove();
			if(obj.rs){
				var submitted = validator.formSubmitted;
				validator.prepareElement(element);
				validator.formSubmitted = submitted;
				validator.successList.push(element);
				//清空错误信息
				validator.showErrors();
				
			}else{
				var errors = {};
				var message = validator.settings.messages[element.name].ajaxValidate || obj.msg;
				errors[element.name] = message;
				//设置错误提示信息
				validator.showErrors(errors);
			}
			previous.valid = obj.rs;
			validator.stopRequest(element, obj.rs);
	    	return obj.rs;
	    }
	});
	var em = $(element).parent().find("em");
	em.remove();
	$(element).after("<em htmlfor="+element.name+" generated=true wait=1>验证中</em>");
}, "值错误");


(function() {

	function stripHtml(value) {
		return value.replace(/<.[^<>]*?>/g, ' ').replace(/&nbsp;|&#160;/gi, ' ')
		.replace(/[0-9.(),;:!?%#$'"_+=\/-]*/g,'');
	}
	jQuery.validator.addMethod("maxWords", function(value, element, params) {
	    return this.optional(element) || stripHtml(value).match(/\b\w+\b/g).length < params;
	}, jQuery.validator.format("Please enter {0} words or less."));

	jQuery.validator.addMethod("minWords", function(value, element, params) {
	    return this.optional(element) || stripHtml(value).match(/\b\w+\b/g).length >= params;
	}, jQuery.validator.format("Please enter at least {0} words."));

	jQuery.validator.addMethod("rangeWords", function(value, element, params) {
	    return this.optional(element) || stripHtml(value).match(/\b\w+\b/g).length >= params[0] && value.match(/bw+b/g).length < params[1];
	}, jQuery.validator.format("Please enter between {0} and {1} words."));

})();

//--------------------------------------------开发人员验证方法----------------------------------------------------

var senitiveWords = ["abstract","assert","boolean","Boolean","break","byte","Byte",
                     "case","catch","char","Char","class","continue",
                     "default","do","double","else","enum","Enum",
                     "extends","final","finally","float","Float","for",
                     "if","implements","import","instanceof","int","Integer",
                     "interface","long","Long","native","new","package",
                     "private","protected","public","return","strictfp",
                     "short","Short","static","super","switch","synchronized",
                     "this","throw","throws","transient","try",
                     "void","volatile","while","List","Map",
                     "Object","String"];

var upStr="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
var downStr="abcdefghijklmnopqrstuvwxyz";
var numStr = "0123456789";
var allStr="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

//java类名校验
jQuery.validator.addMethod("senitive", function(value, element, param) {
	value = $.trim(value);
	for(var i=0; i<senitiveWords.length; i++){
		if(senitiveWords[i] == value){
			return false;
		}
	}
	return true;
}, "关键词");

//java类名校验  字母数字，首字母大写
jQuery.validator.addMethod("clazz", function(value, element, param) {
	value = $.trim(value);
	if(upStr.indexOf(value.charAt(0)) == -1){
		return false;
	}
	for(var i=1; i<value.length; i++){
		if((allStr).indexOf(value.charAt(i)) == -1)
			return false;
	}
	return true;
}, "非法类名");

//java注释校验
jQuery.validator.addMethod("note", function(value, element, param) {
	value = $.trim(value);
	return value.indexOf("/") == -1;
}, "非法注释");

//java属性校验 字母数字下划线，首字母小写
jQuery.validator.addMethod("attr", function(value, element, param) {
	value = $.trim(value);
	if(downStr.indexOf(value.charAt(0)) == -1){
		return false;
	}
	for(var i=1; i<value.length; i++){
		if((allStr+"_").indexOf(value.charAt(i)) == -1)
			return false;
	}
	return true;
}, "非法属性名");

//java方法校验  字母数字，首字母小写
jQuery.validator.addMethod("func", function(value, element, param) {
	value = $.trim(value);
	if(downStr.indexOf(value.charAt(0)) == -1){
		return false;
	}
	for(var i=1; i<value.length; i++){
		if(allStr.indexOf(value.charAt(i)) == -1)
			return false;
	}
	return true;
}, "非法方法名");

//java包校验  小写字母数字点号
jQuery.validator.addMethod("pack", function(value, element, param) {
	value = $.trim(value);
	if(downStr.indexOf(value.charAt(0)) == -1 || value.charAt(value.length-1) == '.'){
		return false;
	}
	for(var i=1; i<value.length; i++){
		if(("."+downStr).indexOf(value.charAt(i)) == -1)
			return false;
	}
	var ps = value.split(".");
	for(var j=0; j<ps.length;j++){
		for(var i=0; i<senitiveWords.length; i++){
			if(senitiveWords[i] == ps[j]){
				return false;
			}
		}
	}
	
	return true;
}, "非法包名");

//小写字母数字下划线，首字母小写
jQuery.validator.addMethod("artifactid", function(value, element, param) {
	value = $.trim(value);
	if(downStr.indexOf(value.charAt(0)) == -1){
		return false;
	}
	for(var i=1; i<value.length; i++){
		if((downStr+numStr+"_").indexOf(value.charAt(i)) == -1)
			return false;
	}
	return true;
}, "错误");

//字母+数字+_-.
jQuery.validator.addMethod("full", function(value, element, param) {
	value = $.trim(value);
	if(value.charAt(0) == '.' || value.charAt(value.length-1) == '.'){
		return false;
	}
	
	for(var i=1; i<value.length; i++){
		if((".-_"+allStr).indexOf(value.charAt(i)) == -1)
			return false;
	}
	return true;
}, "错误");