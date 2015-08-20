<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/html/jsp/parent.jsp" %>
        </div>
    </div>

<script src="${html}/www/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="${html}/www/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	
	<script src="${html}/www/js/plugins/jqgrid/i18n/grid.locale-cn.js"></script>
	<script src="${html}/www/js/plugins/jqgrid/jquery.jqGrid.min.js"></script>
	
	<script src="${html}/www/js/hplus.js?v=2.2.0"></script>
	<script src="${html}/www/js/plugins/pace/pace.min.js"></script>
	<script src="${html}/www/js/plugins/jquery-ui/jquery-ui.min.js"></script>
	
	<script src="${html}/js/cookie.js"></script>	
	<script src="${html}/js/fun.js"></script>
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
</script>
</body>
</html>
