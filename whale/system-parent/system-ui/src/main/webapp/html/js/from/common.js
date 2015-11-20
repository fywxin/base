$(window).resize(function(){
	$("#bodyDiv").height($.h()-40);
});
$(function() {
	$("#bodyDiv").height($.h()-40);
	$("select").chosen({
		no_results_text: "未找到", 
		search_contains: true,
		disable_search_threshold: 10
	});
});
$.validator.setDefaults({
	highlight: function (element) {$(element).closest('.form-group').removeClass('has-success').addClass('has-error');},
	success: function (element) {element.closest('.form-group').removeClass('has-error').addClass('has-success');},
    errorElement: "i",
    errorClass: "vaildError"
});