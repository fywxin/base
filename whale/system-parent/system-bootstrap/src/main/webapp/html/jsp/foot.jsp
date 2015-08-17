<%@include file="/html/jsp/parent.jsp" %>

        </div>
    </div>
</body>
<script type="text/javascript">
function loginOut(){
	$.ajax({
		    url: "${ctx}/loginOut",
		    type: 'post',
		    data: null,
		    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		   	dataType: 'json',
		    error: function(){
		        window.location.href="${ctx}";
		    },
		    success: function(obj){
		    	$.cookie("userName", null);
	    		$.cookie("encryptedPwd", null); 
		    	window.location.href="${ctx}";
			}
		});
}

function changePassword(){
	$.openWin({url:"${ctx }/user/goChangePassword", width: 600, height: 300});
}

$("body").addClass('fixed-sidebar');
$('.sidebar-collapse').slimScroll({
    height: '100%',
    railOpacity: 0.9
});
</script>

</html>
