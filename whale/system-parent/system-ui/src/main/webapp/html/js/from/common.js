$(window).resize(function(){
	$("#bodyDiv").height($.h()-40);
});
$(function() {
	$("#bodyDiv").height($.h()-40);
});
$.validator.setDefaults({
	highlight: function (element) {$(element).closest('.form-group').removeClass('has-success').addClass('has-error');},
	success: function (element) {element.closest('.form-group').removeClass('has-error').addClass('has-success');},
    errorElement: "i",
    errorClass: "vaildError"
});