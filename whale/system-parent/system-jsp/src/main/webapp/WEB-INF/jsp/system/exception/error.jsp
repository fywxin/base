<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/html/jsp/common.jsp" %>

<script language="javascript">

</script>

</head>
<body>
<div class="body-box-form" >
	<div class="content-form">
		<div class="edit-form">
			<form action="" method="post" id="dataForm">
				<table cellspacing="0" cellpadding="0" width="100%">
					<col  width="72"/>
					<col  width=""/>
					<tbody>
						<tr>
							<td class="td-label" >错误信息</td>
							<td class="td-value" >
								<div  style="height: 250px; width:100%; overflow-y:auto;" >
									${exception.message }
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
</body>
</html>

