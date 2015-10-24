<%@include file="/jsp/base.jsp" %>
<script src="${html}/plugins/validate/jquery.validate.min.js"></script>
<script src="${html}/plugins/validate/messages_zh.min.js"></script>
<script src="${html}/plugins/validate/addon.js"></script>

<style type="text/css">
::-webkit-scrollbar{width: 12px;background-color: #F5F5F5;}
.table>tbody>tr>td, .table>tbody>tr>th {padding: 7px;}
</style>
<script type="text/javascript">
$(function(){
	$("#formBoxDiv").height($.h()-58);
});
function closeWin(){
	$.closeWin();
}
</script>