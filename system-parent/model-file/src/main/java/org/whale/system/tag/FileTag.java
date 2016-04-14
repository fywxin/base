package org.whale.system.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.domain.FileInfo;
import org.whale.system.service.FileInfoService;


/**
 * 文件上传处理控件
 * 所有文件数据在 filesArr_id 中
 * @author wjs
 *
 */
public class FileTag extends TagSupport {
	private static final long serialVersionUID = 11343L;
	private static final String LOGIC_TRUE = "true";
	
	private String ctx;
	private String resource;
	
	/** ID */
	private String id;
	/** 附件urlPath值 */
	private String value = "";
	/** 只能查看，不能删除，编辑*/
	private String readonly = "false";
	/** 保存方式 DISK|FTP|BOTH|NONE */
	private Integer saveWay = FileInfo.SAVE_DISK;
	/** 保存文件相对路径 */
	private String dirName = "file";
	/** 文件上传处理地址 */
	private String url="fileInfo/doFileUpload";
	/** 自动上传 auto*/
	private String autoSubmit = "true";
	/** 文件大小限制*/
	private Integer fileSizeLimit;
	/** 上传类型限制*/
	private String fileTypeExts;
	/** 文件上传的最多数量*/
	private Integer maxNum = 1;
	/** 扩展数据*/
	private String extInfo = "{}";
	
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request =(HttpServletRequest) pageContext.getRequest();
		ctx = request.getContextPath();
		resource = (String)request.getSession().getServletContext().getAttribute("html");
		
		if(this.maxNum < 1){
			this.maxNum = 1;
		}
		
		boolean isReadonly = LOGIC_TRUE.equals(this.getReadonly());
		if("null".equals(this.value)){
			value = "";
		}
		if(!url.startsWith(request.getContextPath()+"/")){
			url = request.getContextPath()+"/"+url;
		}
		
		List<FileInfo> upLoadFiles = this.getFileInfos();
		
		
		StringBuilder strb = new StringBuilder();
		strb.append(this.bulidHtml(upLoadFiles, isReadonly));
		//
		if(isReadonly){
			strb.append("<script type='text/javascript'>\n")
				.append(this.bulidDownFileStr())
				.append("</script>\n");
		}else{
			strb.append("<script type='text/javascript' src='"+resource+"/plugins/webuploader/webuploader.js'></script>\n")
				.append("<script type='text/javascript' >\n")
				.append(this.bulidUploadJs(request))
				.append(this.bulidEventStr())
				.append("});\n\n")
				.append(this.bulidDownFileStr())
				.append(this.bulidDelFileStr())
				.append("</script>\n");
		}
		try {
			pageContext.getOut().print(strb.toString());
		} catch (java.io.IOException e) {
			e.printStackTrace();
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}
	
	private String bulidHtml(List<FileInfo> fileInfos, boolean isReadonly){
		StringBuilder strb = new StringBuilder();
		
		int itemNum = fileInfos == null ? 0 : fileInfos.size();
		strb.append("<div id=\"file_"+this.id+"\"").append(" itemnum=").append(itemNum).append(" maxnum=").append(this.maxNum).append(" >\n")
			.append("	<div id=\"fileList_"+this.id+"\" class=\"uploader-list\">\n");
		List<Long> fileIds = new ArrayList<Long>();
		if(fileInfos != null && fileInfos.size() > 0){
			for(FileInfo fileInfo : fileInfos){
				strb.append(this.bulidFileHtml(fileInfo, isReadonly));
				fileIds.add(fileInfo.getId());
			}
		}
		strb.append("	</div>\n");
		if(!isReadonly){
			if(itemNum >= this.maxNum){
				strb.append("	<div id=\"fileSel_"+this.id+"\" style=\"visibility:hidden;\">选择文件</div>\n");
			}else{
				strb.append("	<div id=\"fileSel_"+this.id+"\" >选择文件</div>\n");
			}
		}
		strb.append("	<input type=\"hidden\" id=\""+this.id+"\" name=\""+this.id+"\" value=\"").append(value.trim()).append("\" fileid=\"").append(LangUtil.joinIds(fileIds)).append("\" />\n")
			.append("</div>\n\n");
		
		return strb.toString();
	}
	
	private String bulidFileHtml(FileInfo fileInfo, boolean isReadonly){
		if(fileInfo == null)
			return "";
		StringBuilder strb = new StringBuilder();
		
		strb.append("<div class=\"item upload-state-done\" fileid=\""+fileInfo.getId()+"\" >\n")
			.append("	<a href=\"#\" url=\"").append(fileInfo.getUrlPath()+"\" onclick=\"downFile_"+this.id+"(this)\" >\n").append(fileInfo.getRealFileName()).append("</a>\n")
			.append(isReadonly ? "" : "		<span style=\"padding-left:40px;cursor: pointer;\" onclick=\"delFile_"+this.id+"(this)\" >删除</span>\n")
			.append("</div>\n");
		
		return strb.toString();
	}
	
	/**
	 * 初始化Uploadify
	 * @param request
	 * @param formData
	 * @return
	 */
	private String bulidUploadJs(HttpServletRequest request){
		StringBuilder strb = new StringBuilder();
		
		strb.append("jQuery(function() {\n")
			.append("var $ = jQuery,\n")
			.append("	$list_"+this.id+" = $('#fileList_"+this.id+"'),\n")
			.append("	uploader_"+this.id+";\n\n");
			
		strb.append("uploader_"+this.id+" = WebUploader.create({\n")
			.append("	auto: ").append(this.getAutoSubmit()).append(",\n")
			.append("	swf: '").append(this.resource).append("/plugin/webuploader/Uploader.swf',\n")
			.append("	server: '").append(this.getUrl()).append("',\n")
			.append("	pick: '#fileSel_").append(this.id).append("',\n")
			.append("	duplicate:true,\n");
		if(this.fileSizeLimit != null && this.fileSizeLimit > 1){
		strb.append("	fileSingleSizeLimit:").append(fileSizeLimit).append(",\n");
		}
		strb.append("	formData: {\n")
			.append("		jsessionid: '").append(request.getSession().getId()).append("',\n")
			.append("		fileType: ").append(FileInfo.TYPE_FILE).append(",\n")
			.append("		saveWay: ").append(this.getSaveWay()).append(",\n")
			.append("		dirName: '").append(this.getDirName()).append("',\n")
			.append("		extInfo: ").append(this.extInfo).append("\n")
			.append("	}\n")
			.append("});\n\n");
		return strb.toString();
	}
	
	private String bulidEventStr(){
		StringBuilder strb = new StringBuilder();
		// 当有文件添加进来的时候
		strb.append("uploader_"+this.id+".on('fileQueued', function(file) {\n")
			.append("	$list_"+this.id+".append('<div id=\"' + file.id + '\" class=\"item\">' +\n")
			.append("		'<a href=\"#\" onclick=\"downFile_"+this.id+"(this)\">' +file.name+'</a>'+\n")
			.append("		'<span onclick=\"delFile_"+this.id+"(this)\" style=\"padding-left:40px;cursor: pointer;\">删除</span>'+\n")
			.append("	'</div>'\n")
			.append("	);\n")
			.append("	var curItemNum = parseInt($('#file_"+this.id+"').attr('itemnum'));\n")
			.append("	if((curItemNum+1) >= parseInt($('#file_"+this.id+"').attr('maxnum'))){\n")
			.append("		$('#fileSel_"+this.id+"').css('visibility','hidden');\n")
			.append("	}\n")
			.append("});\n\n");
			
		// 文件上传过程中创建进度条实时显示。	
		strb.append("uploader_"+this.id+".on('uploadProgress', function(file, percentage) {\n")
			.append("	var $li = $('#'+file.id ),\n")
			.append("	$percent = $li.find('.progress span');\n")
			.append("	if(!$percent.length) {\n")
			.append("		$percent = $('<p class=\"progress\"><span></span></p>').appendTo( $li ).find('span');\n")
			.append("	}\n")
			.append("	$percent.css( 'width', percentage * 100 + '%' );\n")
			.append("});\n\n");
			
		// 文件上传成功，给item添加成功class, 用样式标记上传成功。
		strb.append("uploader_"+this.id+".on('uploadSuccess', function(file, response) {\n")
			.append("	if(response.rs){\n")
			.append("		$( '#'+file.id ).addClass('upload-state-done').attr('fileid',response.datas.id);\n")
			.append("		var $a = $( '#'+file.id ).find('a');\n")
			.append("		$a.attr('fileid',response.datas.id).attr('url', response.datas.urlPath);\n")
			.append("		var fileVal = $('#"+this.getId()+"').val();\n")
			.append("		var fileId = $('#"+this.getId()+"').attr('fileid');\n")
			.append("		if(fileVal == '' || fileVal == null){\n")
			.append("			$('#"+this.getId()+"').val(response.datas.urlPath).attr('fileid',response.datas.id);\n")
			.append("		}else{\n")
			.append("			$('#"+this.getId()+"').val(fileVal+','+response.datas.urlPath).attr('fileid',(fileId+','+response.datas.id));\n")
			.append("		}\n")
			.append("		var newItemNum = parseInt($('#file_"+this.id+"').attr('itemnum'))+1;\n")
			.append("		$('#file_"+this.id+"').attr('itemnum', newItemNum);\n")
			.append("	}else{\n")
			.append("		uploadError(file, response);\n")
			.append("	}\n")
			.append("});\n\n");
		
		// 文件上传失败，现实上传出错。
		strb.append("uploader_"+this.id+".on('uploadError', function(file, response) {\n")
			.append("	uploadError(file, response);\n")
			.append("});\n\n");
		
		// 完成上传完了，成功或者失败，先删除进度条。
		strb.append("uploader_"+this.id+".on('uploadComplete', function(file) {\n")
		.append("	$( '#'+file.id ).find('.progress').remove();\n")
		.append("});\n\n");
		
		strb.append("function uploadError(file, response) {\n")
			.append("	$( '#'+file.id ).find('p.state').attr(\"title\",'上传失败:'+response.msg);\n")
			.append("	$('#fileSel_"+this.id+"').css('visibility','visible');")
			.append("}\n\n");
		
		return strb.toString();
	}
	
	private String bulidDownFileStr(){
		StringBuilder strb = new StringBuilder();
		strb.append("function downFile_"+id+"(obj){\n")
			.append("	var url = $(obj).attr('url');\n")
			.append("	window.location.href=url;\n")
			.append("}\n\n");
		return strb.toString();
	}

	
	private String bulidDelFileStr(){
		StringBuilder strb = new StringBuilder();
		strb.append("function delFile_"+this.getId()+"(ob){\n")
			.append("	var fileId = $(ob).parent().attr('fileid');\n")
			.append("	$.del({url: '"+ctx+"/fileInfo/doDelete', datas:{fileId: fileId}, onSuccess: function(){\n")
			.append("		$(ob).parent().remove();\n")
			.append("		var fileVal = $('#"+this.getId()+"').val();\n")
			.append("		var fileId = $('#"+this.getId()+"').attr('fileid');\n")
			.append("		if(fileVal != null && fileVal.length > 0){\n")
			.append("			var fileVals = fileVal.split(',');\n")
			.append("			var fileIds = fileId.split(',');\n")
			.append("			for(var i=0; i<fileIds.length; i++){\n")
			.append("				if(parseInt(fileIds[i]) == parseInt(fileId)){\n")
			.append("					fileIds.splice(i, 1);\n")
			.append("					fileVals.splice(i, 1);\n")
			.append("					$('#"+this.getId()+"').val(fileVals.join(',')).attr('fileid',fileIds.join(','));\n")
			.append("				}\n")
			.append("			}\n")
			.append("		}\n")
			.append("		var newItemNum = parseInt($('#file_"+this.id+"').attr('itemnum'))-1;\n")
			.append("		$('#file_"+this.id+"').attr('itemnum', newItemNum);\n")
			.append("		if(newItemNum >= parseInt($('#file_"+this.id+"').attr('maxnum'))){\n")
			.append("			$('#fileSel_"+this.id+"').css('visibility','hidden');\n")
			.append("		}else{\n")
			.append("			$('#fileSel_"+this.id+"').css('visibility','visible');\n")
			.append("		}\n")
			.append("	}});\n")
			.append("}\n");
		
		return strb.toString();
	}
	
	
	//-----------------------------------------------------------------------------
	
	private List<FileInfo> getFileInfos(){
		if(Strings.isNotBlank(this.value)){
			FileInfoService fileInfoService = FileInfoService.getThis();
			
			String[] filePaths = this.value.split(",");
			List<FileInfo> list = new ArrayList<FileInfo>(filePaths.length);
			FileInfo fileInfo = null;
			for(String urlPath : filePaths){
				if(Strings.isNotBlank(urlPath)){
					fileInfo = fileInfoService.getByUrlPath(urlPath);
					if(fileInfo != null){
						list.add(fileInfo);
					}
				}
			}
			return list;
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public Integer getSaveWay() {
		return saveWay;
	}

	public void setSaveWay(Integer saveWay) {
		this.saveWay = saveWay;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAutoSubmit() {
		return autoSubmit;
	}

	public void setAutoSubmit(String autoSubmit) {
		this.autoSubmit = autoSubmit;
	}

	public Integer getFileSizeLimit() {
		return fileSizeLimit;
	}

	public void setFileSizeLimit(Integer fileSizeLimit) {
		this.fileSizeLimit = fileSizeLimit;
	}

	public String getFileTypeExts() {
		return fileTypeExts;
	}

	public void setFileTypeExts(String fileTypeExts) {
		this.fileTypeExts = fileTypeExts;
	}
	
	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

}
