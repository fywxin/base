package org.whale.system.domain;

import java.util.HashMap;
import java.util.Map;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;
import org.whale.system.common.util.Strings;
import org.whale.system.domain.FileInfo;

/**
 * 文件
 *
 * @author 王金绍
 * @Date 2014-9-27
 */
@Table(value="sys_file_info", cnName="文件")
public class FileInfo extends BaseEntry {

	private static final long serialVersionUID = -1411808919857l;
	
	/** 保存到硬盘 */
	public static final Integer SAVE_DISK = 1;
	/** 保存到FTP */
	public static final Integer SAVE_FTP = 2;
	/** 保存到硬盘 和 FTP */
	public static final Integer SAVE_BOTH = 3;
	
	
	/** 文件类型 1.普通文件 */
	public static final Integer TYPE_FILE = 1;
	/** 文件类型 2.图片 */
	public static final Integer TYPE_IMG = 2;
	/** 文件类型 3.文本 */
	public static final Integer TYPE_TXT = 3;
	/** 文件类型 4.压缩包 */
	public static final Integer TYPE_RAR = 4;
	/** 文件类型 5.安装包 */
	public static final Integer TYPE_EXE = 5;
	/** 文件类型 6.媒体文件 */
	public static final Integer TYPE_MEDIA = 6;
	
	public static HashMap<Integer, String> extMap = new HashMap<Integer, String>();

	static{
		extMap.put(FileInfo.TYPE_IMG, "gif,jpg,jpeg,png,bmp");
		extMap.put(FileInfo.TYPE_MEDIA, "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,swf,flv");
		extMap.put(FileInfo.TYPE_TXT, "doc,docx,xls,xlsx,ppt,htm,html,txt,java,c");
		extMap.put(FileInfo.TYPE_EXE, "exe,sh,bat,com,dll");
		extMap.put(FileInfo.TYPE_RAR, "tar,zip,rar,gz,7z");
	}
	
	/**
	 * 根据文件名判断文件类型
	 * @param type
	 * @return
	 */
	public static Integer parseType(String fileName){
		if(Strings.isBlank(fileName))
			return FileInfo.TYPE_FILE;
		fileName = fileName.toLowerCase();
		int index = fileName.lastIndexOf(".");
		if(index != -1){
			fileName = fileName.substring(index+1);
		}
		fileName = fileName.trim();
		
		for(Map.Entry<Integer, String> entry : extMap.entrySet()){
			if(entry.getValue().indexOf(fileName) != -1){
				return entry.getKey();
			}
		}
		return FileInfo.TYPE_FILE;
	}
	
	@Id
	@Column(cnName="id")
	private Long id;
	
	@Validate(required=true)
	@Column(name="fileName", cnName="文件名", unique=true)
	private String fileName;
	
	@Validate(required=true)
  	@Column(name="fileType", cnName="文件类型")
	private Integer fileType;
	
	@Validate(required=true)
  	@Column(name="realFileName", cnName="原文件名")
	private String realFileName;
	
  	@Column(name="fileSuffix", cnName="文件后缀名")
	private String fileSuffix;
  	
  	@Column(name="filePath", cnName="文件路径")
	private String filePath;
  	
  	@Column(name="absolutePath", cnName="绝对路径")
	private String absolutePath;
  	
  	@Validate(required=true)
  	@Column(name="urlPath", cnName="url访问地址", unique=true)
	private String urlPath;
  	
  	@Validate(required=true)
  	@Column(name="saveWay", cnName="保存方式")
	private Integer saveWay;
  	
  	@Column(name="width", cnName="图片宽度")
	private Integer width;
  	
  	@Column(name="height", cnName="图片高度")
	private Integer height;
  	
  	@Validate(required=true)
  	@Column(name="fileSize", cnName="文件大小")
	private Long fileSize;
  	
  	@Column(name="originalImgId", cnName="原始图Id")
	private Long originalImgId;
  	
  	@Column(name="extInfo", cnName="扩展信息")
	private String extInfo;
  	
  	@Column(name="creator", cnName="创建人")
	private Long creator;
  	
  	@Column(name="createTime", cnName="创建时间")
	private Long createTime;
  	
  //不保存到数据库
  	private String dirName;
  	
  	public Long getSizeKB() {
  		if(this.fileSize != null)
  			return this.fileSize /1024;
  		return -1L;
  	}
  	
  	public Float getSizeMB() {
  		if(this.fileSize != null)
  			return new Float(this.fileSize) /1024 / 1024;
  		return -1.0f;
  	}
	
	/**文件名 */
	public String getFileName(){
		return fileName;
	}
	
	/**文件名 */
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	/**id */
	public Long getId(){
		return id;
	}
	
	/**id */
	public void setId(Long id){
		this.id = id;
	}
	
	/**文件类型 */
	public Integer getFileType(){
		return fileType;
	}
	
	/**文件类型 */
	public void setFileType(Integer fileType){
		this.fileType = fileType;
	}
	
	/**原文件名 */
	public String getRealFileName(){
		return realFileName;
	}
	
	/**原文件名 */
	public void setRealFileName(String realFileName){
		this.realFileName = realFileName;
	}
	
	/**文件后缀名 */
	public String getFileSuffix(){
		return fileSuffix;
	}
	
	/**文件后缀名 */
	public void setFileSuffix(String fileSuffix){
		this.fileSuffix = fileSuffix;
	}
	
	/**文件路径 */
	public String getFilePath(){
		return filePath;
	}
	
	/**文件路径 */
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	
	/**绝对路径 */
	public String getAbsolutePath(){
		return absolutePath;
	}
	
	/**绝对路径 */
	public void setAbsolutePath(String absolutePath){
		this.absolutePath = absolutePath;
	}
	
	/**url访问地址 */
	public String getUrlPath(){
		return urlPath;
	}
	
	/**url访问地址 */
	public void setUrlPath(String urlPath){
		this.urlPath = urlPath;
	}
	
	/**保存方式 */
	public Integer getSaveWay(){
		return saveWay;
	}
	
	/**保存方式 */
	public void setSaveWay(Integer saveWay){
		this.saveWay = saveWay;
	}
	
	/**图片宽度 */
	public Integer getWidth(){
		return width;
	}
	
	/**图片宽度 */
	public void setWidth(Integer width){
		this.width = width;
	}
	
	/**图片高度 */
	public Integer getHeight(){
		return height;
	}
	
	/**图片高度 */
	public void setHeight(Integer height){
		this.height = height;
	}
	
	/**文件大小 */
	public Long getFileSize(){
		return fileSize;
	}
	
	/**文件大小 */
	public void setFileSize(Long fileSize){
		this.fileSize = fileSize;
	}
	
	/**原始图Id */
	public Long getOriginalImgId(){
		return originalImgId;
	}
	
	/**原始图Id */
	public void setOriginalImgId(Long originalImgId){
		this.originalImgId = originalImgId;
	}
	
	/**扩展信息 */
	public String getExtInfo(){
		return extInfo;
	}
	
	/**扩展信息 */
	public void setExtInfo(String extInfo){
		this.extInfo = extInfo;
	}
	
	/**创建人 */
	public Long getCreator(){
		return creator;
	}
	
	/**创建人 */
	public void setCreator(Long creator){
		this.creator = creator;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

}