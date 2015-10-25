<%@include file="/jsp/base.jsp" %>
<script src="${html}/plugins/validate/jquery.validate.min.js"></script>
<script src="${html}/plugins/validate/messages_zh.min.js"></script>
<script src="${html}/plugins/validate/addon.js"></script>
<link href="${html}/css/over-write.css" rel="stylesheet">

<script type="text/javascript">
	$.validator.setDefaults({
	    highlight: function (element) {
	        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
	    },
	    success: function (element) {
	        element.closest('.form-group').removeClass('has-error').addClass('has-success');
	    },
	    errorElement: "span",
	    errorClass: "help-block m-b-none"
	});
</script>
