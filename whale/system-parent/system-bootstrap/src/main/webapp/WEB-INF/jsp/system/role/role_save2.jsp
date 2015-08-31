<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/form.jsp" %>
<script type="text/javascript">
//校验函数
$(function() {
	$("#dataForm").validate({
		rules: {
			"roleName": {
				validIllegalChar: true,
				maxlength: 16,
				required: true
			},
			"roleCode": {
				validIllegalChar: true,
				maxlength: 64,
				required: true
			},
			"remark":{
				validIllegalChar: true,
				maxlength: 100
			}
		}
	});
});

</script>

</head>
    
<body class="my_formBody "> 
	<div class="navbar-fixed-bottom my_toolbar" >
		<button type="button" class="btn btn-primary btn-sm" onclick="save()"><i class="fa fa-times" ></i> 保存</button>
		<button type="button" class="btn btn-danger btn-sm" onclick="closeWin()"><i class="fa fa-times" ></i> 关闭</button>
	</div>
	<div id="formBoxDiv" class="my_formBox" >
		<div id="infoBoxDiv" class="my_infoBox alert alert-danger"  style="display: block;">用户名不能为空</div>
		<form action="" method="post" id="dataForm" >
			<div class="form-group">
                  <label class="col-sm-3 control-label">姓名：</label>
                  <div class="col-sm-8">
                      <input id="cname" name="name" minlength="2" type="text" class="form-control" required="" aria-required="true">
                  </div>
              </div>
              <div class="form-group">
                  <label class="col-sm-3 control-label">E-mail：</label>
                  <div class="col-sm-8">
                      <input id="cemail" type="email" class="form-control" name="email" required="" aria-required="true">
                  </div>
              </div>
              <div class="form-group">
                  <label class="col-sm-3 control-label">网站：</label>
                  <div class="col-sm-8">
                      <input id="curl" type="url" class="form-control" name="url">
                  </div>
              </div>
              <div class="form-group">
                  <label class="col-sm-3 control-label">说明：</label>
                  <div class="col-sm-8">
                      <textarea id="ccomment" name="comment" class="form-control" required="" aria-required="true"></textarea>
                  </div>
              </div>
		</table>
		</form>
	</div>
</body>
</html>
