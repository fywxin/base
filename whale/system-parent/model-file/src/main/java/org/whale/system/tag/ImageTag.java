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
 * 图片上传处理控件
 * 所有文件数据在 filesArr_id 中
 * @author wjs
 *
 */
public class ImageTag extends TagSupport {
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
	private String dirName = "img";
	/** 图片上传处理地址 */
	private String url="fileInfo/doImgUpload";
	/** 自动上传 auto*/
	private String autoSubmit = "true";
	/** 文件大小限制*/
	private Integer fileSizeLimit;
	/** 上传类型限制*/
	private String fileTypeExts = "gif,jpg,jpeg,bmp,png";
	/** 图片上传的最多数量*/
	private Integer maxNum = 1;
	/** 扩展数据*/
	private String extInfo = "{}";
	
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request =(HttpServletRequest) pageContext.getRequest();
		ctx = request.getContextPath();
		resource = (String)request.getSession().getServletContext().getAttribute("resource");
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
			strb.append("<script type='text/javascript' src=\""+resource+"/com/imgpreview.js\"></script>")
				.append("<script type='text/javascript'>\n")
				.append(this.bulidShowImageStr())
				.append("</script>\n");
		}else{
			strb.append("<script type='text/javascript' src=\""+resource+"/com/imgpreview.js\"></script>\n")
				.append("<script type='text/javascript' src=\""+resource+"/plugin/webuploader/webuploader.js\"></script>\n")
				.append("<script type='text/javascript'>\n")
				.append(this.bulidUploadJs(request))
				.append(this.bulidEventStr())
				.append("});\n\n")
				.append(this.bulidShowImageStr())
				.append(this.bulidEditImageStr())
				.append(this.bulidDelImageStr())
				.append(this.bulidChangeImgStr())
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
		strb.append("<div id=\"image_"+this.id+"\"").append(" itemnum=").append(itemNum).append(" maxnum=").append(this.maxNum).append(" >\n")
			.append("	<div id=\"imageList_"+this.id+"\" class=\"uploader-list\">\n");
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
				strb.append("	<div id=\"imageSel_"+this.id+"\" style=\"visibility:hidden;\">选择图片</div>\n");
			}else{
				strb.append("	<div id=\"imageSel_"+this.id+"\" >选择图片</div>\n");
			}
		}
		
		strb.append("	<input type=\"hidden\" id=\""+this.id+"\" name=\""+this.id+"\" value=\"").append(value.trim()).append("\" imgid=\"").append(LangUtil.joinIds(fileIds)).append("\" />\n")
			.append("</div>\n\n");
		
		return strb.toString();
	}
	
	private String bulidFileHtml(FileInfo fileInfo, boolean isReadonly){
		if(fileInfo == null)
			return "";
		StringBuilder strb = new StringBuilder();
		
		strb.append("<div class=\"file-item thumbnail upload-state-done\" imgid=\""+fileInfo.getId()+"\" >\n")
			.append("	<img style=\"width:108px;height:108px;\" src=\"").append(fileInfo.getUrlPath()).append("\" url=\""+fileInfo.getUrlPath()+"\" onclick=\"showImage_"+this.id+"(this)\" mark=\"img\" >\n")
			.append(isReadonly ? "" : "	<div class=\"info\">\n")
			.append(isReadonly ? "" : "		<span style=\"padding-left:5px;cursor: pointer;\" onclick=\"editImage_"+this.id+"(this)\" >编辑</span>\n")
			.append(isReadonly ? "" : "		<span style=\"padding-left:40px;cursor: pointer;\" onclick=\"delImage_"+this.id+"(this)\" >删除</span>\n")
			.append(isReadonly ? "" : "	</div>\n")
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
			//.append("	$(\"img[mark=img]\").imgPreview();\n\n")
			.append("var $ = jQuery,\n")
			.append("	$list_"+this.id+" = $('#imageList_"+this.id+"'),\n")
			.append("	ratio_"+this.id+" = window.devicePixelRatio || 1,\n")
			.append("	thumbnailWidth_"+this.id+" = 100 * ratio_"+this.id+",\n")
			.append("	thumbnailHeight_"+this.id+" = 100 * ratio_"+this.id+",\n")
			.append("	uploader_"+this.id+";\n\n");
			
		strb.append("uploader_"+this.id+" = WebUploader.create({\n")
			.append("	auto: ").append(this.getAutoSubmit()).append(",\n")
			.append("	swf: '").append(this.resource).append("/plugin/webuploader/Uploader.swf',\n")
			.append("	server: '").append(this.getUrl()).append("',\n")
			.append("	pick: '#imageSel_").append(this.id).append("',\n")
			.append("	duplicate:true,\n");
		if(this.fileSizeLimit != null && this.fileSizeLimit > 1){
		strb.append("	fileSingleSizeLimit:").append(fileSizeLimit).append(",\n");
		}
		strb.append("	accept: {\n")
			.append("		title: 'Images',\n")
			.append("		extensions: '").append(this.getFileTypeExts()).append("',\n")
			.append("		mimeTypes: 'image/*'\n")
			.append("	},\n")
			.append("	formData: {\n")
			.append("		jsessionid: '").append(request.getSession().getId()).append("',\n")
			.append("		fileType: ").append(FileInfo.TYPE_IMG).append(",\n")
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
			.append("	var $li = $('<div id=\"' + file.id + '\" class=\"file-item thumbnail\">' +\n")
			.append("		'<img style=\"width:108px;height:108px;\" mark=\"img\" onclick=\"showImage_"+this.id+"(this)\">' +\n")
			.append("		'<div class=\"info\">' +\n")
			.append("			'<span onclick=\"editImage_"+this.id+"(this)\" style=\"padding-left:5px;cursor: pointer;\">编辑</span>'+\n")
			.append("			'<span onclick=\"delImage_"+this.id+"(this)\" style=\"padding-left:40px;cursor: pointer;\">删除</span>'+\n")
			.append("		'</div>'+\n")
			.append("	'</div>'\n")
			.append("	),\n")
			.append("	$img = $li.find('img');\n")
			.append("	$list_"+this.id+".append( $li );\n")
			.append("	uploader_"+this.id+".makeThumb(file, function(error, src ) {\n")
			.append("		if (error) {\n")
			.append("			$img.replaceWith('<span>不能预览</span>');\n")
			.append("			return;\n")
			.append("		}\n")
			.append("		$img.attr( 'src', src );\n")
			.append("	}, thumbnailWidth_"+this.id+", thumbnailHeight_"+this.id+");\n")
			.append("	var curItemNum = parseInt($('#image_"+this.id+"').attr('itemnum'));\n")
			.append("	if((curItemNum+1) >= parseInt($('#image_"+this.id+"').attr('maxnum'))){\n")
			.append("		$('#imageSel_"+this.id+"').css('visibility','hidden');\n")
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
			.append("		$( '#'+file.id ).addClass('upload-state-done').attr('imgid',response.datas.id);\n")
			.append("		var $img = $( '#'+file.id ).find('img');\n")
			.append("		$img.attr('imgid',response.datas.id).attr('url', response.datas.urlPath);\n")
			.append("		var imgVal = $('#"+this.getId()+"').val();\n")
			.append("		var imgId = $('#"+this.getId()+"').attr('imgid');\n")
			.append("		if(imgVal == '' || imgVal == null){\n")
			.append("			$('#"+this.getId()+"').val(response.datas.urlPath).attr('imgid',response.datas.id);\n")
			.append("		}else{\n")
			.append("			$('#"+this.getId()+"').val(imgVal+','+response.datas.urlPath).attr('imgid',(imgId+','+response.datas.id));\n")
			.append("		}\n")
			.append("		var newItemNum = parseInt($('#image_"+this.id+"').attr('itemnum'))+1;\n")
			.append("		$('#image_"+this.id+"').attr('itemnum', newItemNum);\n")
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
			.append("	var $li = $('#'+file.id ),\n")
			.append("	$error = $li.find('div.error');\n")
			.append("	if(!$error.length) {\n")
			.append("		$error = $('<div class=\"error\"></div>').appendTo( $li );\n")
			.append("	}\n")
			.append("	$error.text('上传失败').attr(\"title\",'上传失败:'+response.msg);\n")
			.append("	$('#imageSel_"+this.id+"').css('visibility','visible');")
			.append("}\n\n");
		
		return strb.toString();
	}
	
	private String bulidShowImageStr(){
		StringBuilder strb = new StringBuilder();
		strb.append("function showImage_"+id+"(obj){\n")
			.append("	var url = $(obj).attr('url');\n")
			.append("	$.openWin({url: url, title:'查看图片'});\n")
			.append("}\n\n");
		return strb.toString();
	}
	
	private String bulidEditImageStr(){
		StringBuilder strb = new StringBuilder();
		strb.append("function editImage_"+this.id+"(obj){\n")
			.append("	var fileId = $(obj).parent().parent().attr('imgid');\n")
			.append("	$.openWin({width:700,height:500,url:\""+this.ctx+"/fileInfo/goEditImg?fileId=\"+fileId+\"&tagId="+this.id+"\", title:'编辑图片'});\n")
			.append("}\n\n");
		return strb.toString();
	}
	
	private String bulidDelImageStr(){
		StringBuilder strb = new StringBuilder();
		strb.append("function delImage_"+this.getId()+"(obj){\n")
			.append("	var fileId = $(obj).parent().parent().attr('imgid');\n")
			.append("	$.del({url: '"+ctx+"/fileInfo/doDelete', datas:{fileId: fileId}, onSuccess: function(){\n")
			.append("		$(\"div[imgid=\"+fileId+\"]\").remove();\n")
			.append("		var imgVal = $('#"+this.getId()+"').val();\n")
			.append("		var imgId = $('#"+this.getId()+"').attr('imgid');\n")
			.append("		if(imgVal != null && imgVal.length > 0){\n")
			.append("			var imgVals = imgVal.split(',');\n")
			.append("			var imgIds = imgId.split(',');\n")
			.append("			for(var i=0; i<imgIds.length; i++){\n")
			.append("				if(parseInt(imgIds[i]) == parseInt(fileId)){\n")
			.append("					imgIds.splice(i, 1);\n")
			.append("					imgVals.splice(i, 1);\n")
			.append("					$('#"+this.getId()+"').val(imgVals.join(',')).attr('imgid',imgIds.join(','));\n")
			.append("				}\n")
			.append("			}\n")
			.append("		}\n")
			.append("		var newItemNum = parseInt($('#image_"+this.id+"').attr('itemnum'))-1;\n")
			.append("		$('#image_"+this.id+"').attr('itemnum', newItemNum);\n")
			.append("		if(newItemNum >= parseInt($('#image_"+this.id+"').attr('maxnum'))){\n")
			.append("			$('#imageSel_"+this.id+"').css('visibility','hidden');\n")
			.append("		}else{\n")
			.append("			$('#imageSel_"+this.id+"').css('visibility','visible');\n")
			.append("		}\n")
			.append("	  }});\n")
			.append("}\n\n");
		
		return strb.toString();
	}
	
	private String bulidChangeImgStr(){
		StringBuilder strb = new StringBuilder();
		strb.append("function changeImage_"+this.id+"(fileObj){\n")
			.append("	var url = fileObj.urlPath+\"?\"+new Date().getTime();\n")
			.append("	$(\"img[imgid=\"+fileObj.id+\"]\").attr(\"src\", url);\n")
			.append("}\n\n");
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
