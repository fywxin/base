<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<%@include file="/jsp/form.jsp" %>
		<title>图片上传</title>
	</head>


<body>
<div id="bodyDiv" style="margin-top:10px;overflow-x: hidden;">
	<div id="infoBoxDiv" class="my_infoBox alert alert-danger"></div>
	<div class="row">
        <form class="form-horizontal m-t" id="dataForm">
        	<div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>所属组织：</label>
                <div class="col-sm-4 input-group">
                </div>
            </div>
        	<div class="form-group">
                <label class="col-sm-3 col-md-2 col-lg-1 control-label"><i>*</i>用户名：</label>
                <div class="col-sm-9 col-md-10 col-lg-11 form-inline input-group" style="padding-left:0px;">
                   <tag:image id="sdwe"  maxNum="5" dirName="we2"></tag:image>

                </div>
            </div>
            
            <div class="form-group" style="border-bottom:0px;margin-top:20px;">
                <div class="col-sm-offset-3 col-md-offset-2 col-lg-offset-1" >
                    <button class="btn btn-primary" type="button" id="saveBut" onclick="save();"><i class='fa fa-save'></i> 保 存</button>
                    <button class="btn btn-success" type="button" id="continueBut" onclick="window.location.reload();" style="display: none;"><i class='fa fa-thumbs-up'></i> 继续添加</button>
                	<button class="btn btn-default" type="button" id="backBut" onclick="window.top.goTab(1);" style="display: none;"><i class='fa fa-mail-reply'></i> 返回</button>
                </div>
            </div>
        </form>
     </div>
 </div>
</body>
</html>
