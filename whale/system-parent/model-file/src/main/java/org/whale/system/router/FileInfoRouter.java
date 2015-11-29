package org.whale.system.router;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.base.Rs;
import org.whale.system.base.UserContext;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.common.util.WebUtil;
import org.whale.system.service.FileInfoService;
import org.whale.system.ueditor.ActionEnter;
import org.whale.system.domain.FileInfo;
import org.whale.system.common.util.JSCHUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/fileInfo")
public class FileInfoRouter extends BaseRouter {
	
	private static final Logger logger = LoggerFactory.getLogger(FileInfoRouter.class);
	
	@Autowired
	private FileInfoService fileInfoService;
	@Autowired
	private DictCacheService dictCacheService;
	
	private static JSCHUtil jschUtil = new JSCHUtil();
	
	@RequestMapping("/goUeditor")
	public ModelAndView goUeditor(){
		return new ModelAndView("system/fileInfo/ueditor");
	}
	
	@ResponseBody
	@RequestMapping("/doUeditorExec")
	public JSONObject doUeditorExec(HttpServletRequest request) {
		return JSON.parseObject(new ActionEnter(request).exec());
	}
	
	@RequestMapping("/goFileUpload")
	public ModelAndView goFileUpload() {

		return new ModelAndView("system/fileInfo/file_uploader");
	}
	
	/**
	 * 上传文件
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/doFileUpload")
	public Rs doFileUpload(HttpServletRequest request) {
		if (!ServletFileUpload.isMultipartContent(request)) {
			return Rs.success("请选择文件。");
		}
		
		FileInfo fileInfo = new FileInfo();

		Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
		if (fileMap == null || fileMap.keySet().size() != 1) {
			return Rs.fail("只能上传一个文件，请开启自动上传模式");
		}

		MultipartFile multipartFile = null;
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			try {
				multipartFile = entry.getValue();
				decorateFileInfo(multipartFile,fileInfo);
				String info = doSaveStream(fileInfo, multipartFile);
				if (info != null) {
					return Rs.fail(info);
				}
				this.fileInfoService.save(fileInfo);
			} catch (Exception e) {
				logger.error("保存文件出错" + e.getMessage(), e);
				return Rs.fail("保存文件异常-"+e.getMessage());
				
			}
		}

		return Rs.success(fileInfo);
	}
	
	@RequestMapping("/goImgUpload")
	public ModelAndView goImgUpload() {

		return new ModelAndView("system/fileInfo/img_uploader");
	}
	
	@ResponseBody
	@RequestMapping("/doImgUpload")
	public Rs doImgUpload() {
		HttpServletRequest request = WebUtil.getRequest();
		if (!ServletFileUpload.isMultipartContent(request)) {
			return Rs.success("请选择文件");
		}

		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
		if (fileMap == null || fileMap.keySet().size() != 1) {
			return Rs.fail("只能上传一个文件，请开启自动上传模式");
		}
		
		FileInfo fileInfo = new FileInfo();
		
		MultipartFile multipartFile = null;
		BufferedImage bi = null;

		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			try {
				multipartFile = entry.getValue();
				bi = ImageIO.read(multipartFile.getInputStream());
				if (bi == null) {
					return Rs.fail("读取不到文件流数据");
				}
				// 设置图片的高度和宽度
				fileInfo.setWidth(bi.getWidth());
				fileInfo.setHeight(bi.getHeight());

				decorateFileInfo(multipartFile, fileInfo);
				String info = doSaveStream(fileInfo, multipartFile);

				if (info != null) {
					return Rs.fail(info);
				}
				bi = null;
				this.fileInfoService.save(fileInfo);
			} catch (Exception e) {
				logger.error("保存图片出错" + e.getMessage(), e);
				return Rs.fail("保存图片异常-"+e.getMessage());
			}
		}
		return Rs.success(fileInfo);
	}
	
	/**
	 * 跳转到图片裁剪主页面
	 * 
	 * @param request
	 * @param response
	 * @param fileId
	 * @return
	 */
	@RequestMapping("/goEditImg")
	public ModelAndView goEditImg(Long fileId, String tagId) {
		FileInfo fileInfo = this.fileInfoService.get(fileId);

		return new ModelAndView("system/fileInfo/img_edit")
			.addObject("fileInfo", fileInfo)
			.addObject("fileId", fileId)
			.addObject("tagId", tagId);
	}

	/**
	 * 跳转到图片裁剪工作页面
	 * 
	 * @param request
	 * @param response
	 * @param fileId
	 * @return
	 */
	@RequestMapping("/goSplitImg")
	public ModelAndView goSplitImg(Long fileId) {
		FileInfo fileInfo = this.fileInfoService.get(fileId);

		return new ModelAndView("system/fileInfo/img_split")
			.addObject("fileInfo", fileInfo);
	}
	
	/**
	 * 图片裁剪和缩放
	 * 
	 * @param request
	 * @param response
	 * @param fileId
	 *            文件ID
	 * @param nodeX
	 *            裁剪起点X
	 * @param nodeY
	 *            裁剪起点Y
	 * @param nodeW
	 *            裁剪宽度
	 * @param nodeH
	 *            裁剪高度
	 * @param width
	 *            最后图片宽度
	 * @param height
	 *            最后图片高度
	 * @param divWidth
	 *            缩放后图片的宽度
	 * @param divHeight
	 *            缩放后图片的高度
	 */
	@ResponseBody
	@RequestMapping("/doEditImg")
	public Rs doEditImg(HttpServletRequest request,HttpServletResponse response, 
			Long fileId, int nodeX, int nodeY,int nodeW, int nodeH, int width, 
			int height, int divWidth, int divHeight) {
		FileInfo fileInfo = null;
		if (fileId == null || (fileInfo = this.fileInfoService.get(fileId)) == null) {
			return Rs.fail("查找不到文件上传记录");
		}
		double radioX = new Double(divWidth) / fileInfo.getWidth();
		double radioY = new Double(divHeight) / fileInfo.getHeight();

		if (FileInfo.SAVE_DISK.equals(fileInfo.getSaveWay())) {
			String filePath = fileInfo.getAbsolutePath();
			if (!ImageUtil.compressPic(filePath, filePath, radioX, radioY)) {
				return Rs.fail("压缩图片失败");
			}
			// 起点非原点，则有裁剪动作
			if (nodeX != 0 || nodeY != 0) {
				if (!ImageUtil.abscut(filePath, filePath, nodeX, nodeY, width, height)) {
					return Rs.fail("压缩图片成功，裁剪图片失败");
				}
			}

			try {
				File file = new File(filePath);
				if (file.exists()) {
					BufferedImage bi = ImageIO.read(file);
					if (bi != null) {
						fileInfo.setWidth(bi.getWidth());
						fileInfo.setHeight(bi.getHeight());
					}
					fileInfo.setFileSize(file.length());
				}
			} catch (IOException e) {
				logger.error("保存修改文件错误", e);
				return Rs.fail("保存修改文件错误");
			}
		} else {
//			if (!ImageUtil.compressPic(fileInfo, radioX, radioY)) {
//				WebUtil.fail("压缩图片失败");
//				return;
//			}
			fileInfo.setWidth(divWidth);
			fileInfo.setHeight(divHeight);

			// 起点非原点，则有裁剪动作
			if (nodeX != 0 || nodeY != 0) {
//				if (!ImageUtil.abscut(fileInfo, nodeX, nodeY, width, height)) {
//					WebUtil.fail(,
//							"压缩图片成功，裁剪图片失败");
//					return;
//				} else {
//					fileInfo.setWidth(width);
//					fileInfo.setHeight(height);
//				}
			}
		}

		this.fileInfoService.update(fileInfo);
		return Rs.success(fileInfo);
	}

	/**
	 * 删除文件和文件记录,多条记录ID用“，”分割， 无事务
	 * 
	 * @param request
	 * @param response
	 * @param fileIds
	 *            多条记录ID用“，”分割
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/doDelete")
	public Rs doDelete(Long fileId) throws Exception {
		if (fileId == null) {
			return Rs.fail("请选择要删除的文件");
		}
		
		FileInfo fileInfo = this.fileInfoService.get(fileId);
		if (fileInfo != null) {
			if(FileInfo.SAVE_DISK.equals(fileInfo.getSaveWay()) || FileInfo.SAVE_BOTH.equals(fileInfo.getSaveWay())){
				File file = new File(fileInfo.getAbsolutePath());
				if(file.exists()){
					file.delete();
				}
			}else if(FileInfo.SAVE_FTP.equals(fileInfo.getSaveWay()) || FileInfo.SAVE_BOTH.equals(fileInfo.getSaveWay())){
				jschUtil.delFile(fileInfo.getAbsolutePath());
			}
			
			this.fileInfoService.delete(fileId);
		}
		return Rs.success();
	}

	/**
	 * 下载文件
	 * 
	 * @param fileId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/download")
	public void download(Long fileId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			FileInfo fileInfo = fileInfoService.get(fileId);
			if (!FileInfo.SAVE_FTP.equals(fileInfo.getSaveWay())) {
				String fileName = (request.getSession().getServletContext()
						.getRealPath("/") + fileInfo.getFilePath())
						.replaceAll("\\\\", "/").replaceAll("//", "/")
						+ fileInfo.getFileName();
				File file = new File(fileName);
				if (file.exists()) {
					WebUtil.downLoad(request, response, fileInfo.getRealFileName(), new FileInputStream(file));
				}
			}else{
				URL url = new URL(fileInfo.getAbsolutePath());
				WebUtil.downLoad(request, response, fileInfo.getRealFileName(), url.openStream());
			}
		} catch (Exception e) {
			logger.error("下载文件出错" + e.getMessage(), e);
			WebUtil.fail(response, "下载文件异常");
		}
	}

	

	
	// --------------------------------------------------------------------------------------------------

	/**
	 * 从请求中获取上传对象
	 * 
	 * @param request
	 * @param file
	 * @param savePath
	 * @param rootPath
	 * @return
	 */
	public static void decorateFileInfo(MultipartFile multipartFile, FileInfo fileInfo) {
		HttpServletRequest request = WebUtil.getRequest();
		String dirName = request.getParameter("dirName");
		if (Strings.isBlank(dirName)) {
			fileInfo.setDirName(DictCacheService.getThis().getItemValue(DictConstant.DICT_FILE, DictConstant.DICT_ITEM_FILE_DIR, "common"));
		}else{
			fileInfo.setDirName(dirName.trim());
		}
		
		String fileName = TimeUtil.formatTime(new Date(), "yyyyMMddHHmmss")+ "_" + new Random().nextInt(10000);
		String fileRealName = multipartFile.getOriginalFilename();

		String suffix = null;
		int suffixIndex = fileRealName.lastIndexOf(".");
		if (suffixIndex != -1) {
			suffix = fileRealName.substring(suffixIndex + 1);
			fileName += "." + suffix;
		}

		/** 上传文件名称* */
		fileInfo.setRealFileName(fileRealName);
		/** 上传文件大小* */
		fileInfo.setFileSize(multipartFile.getSize());
		/** 上传文件后缀* */
		fileInfo.setFileSuffix(suffix);
		/** 文件存储名称* */
		fileInfo.setFileName(fileName);
		/** 文件存储时间* */
		fileInfo.setCreateTime(System.currentTimeMillis());
		/** 文件存储用户ID* */
		fileInfo.setCreator(UserContext.get().getUserId());
		/** 保存方式 * */
		String saveWay = request.getParameter("saveWay");
		fileInfo.setSaveWay(Strings.isBlank(saveWay) ? FileInfo.SAVE_DISK : Integer.parseInt(saveWay));
		
		String fileType = request.getParameter("fileType");
		fileInfo.setFileType(Strings.isBlank(fileType) ? FileInfo.parseType(fileRealName) : Integer.parseInt(fileType));
		if(Strings.isBlank(fileInfo.getExtInfo())){
			Object extInfo = request.getParameter("extInfo");
			if(extInfo != null && Strings.isNotBlank(extInfo.toString())){
				fileInfo.setExtInfo(JSON.toJSONString(extInfo));
			}
		}
	}

	/**
	 * 保存数据流
	 * 
	 * @param fileInfo
	 * @param request
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String doSaveStream(FileInfo fileInfo, MultipartFile multipartFile) {
		if (FileInfo.SAVE_DISK.equals(fileInfo.getSaveWay())) {
			return save2Disk(fileInfo, multipartFile);
		} else if (FileInfo.SAVE_FTP.equals(fileInfo.getSaveWay())) {
			return save2Ftp(fileInfo, multipartFile);
		} else {
			logger.warn("文件没有设置保存方式，系统将不会自动保存此文件，除非开发人员实现接口中进行文件保存");
		}
		return null;
	}

	/**
	 * 保存硬盘
	 * 
	 * @param fileInfo
	 * @param request
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String save2Disk(FileInfo fileInfo, MultipartFile multipartFile) {
		HttpServletRequest request = WebUtil.getRequest();
		// 从字典中取得上传基础路径
		String basePath = DictCacheService.getThis().getItemValue(DictConstant.DICT_FILE,DictConstant.DICT_ITEM_FILE_PUB_DIRS, "file/");
		basePath = basePath.trim();
		if(!basePath.endsWith("/"));
			basePath += "/";
		
		// 相对路径
		String relativePath = (basePath + fileInfo.getDirName() + "/").replaceAll("\\\\", "/").replaceAll("//", "/");
		// 文件保存目录路径
		String affixPath = (request.getSession().getServletContext()
				.getRealPath("/") + relativePath);
		// 保存文件相对路径
		fileInfo.setFilePath(relativePath+fileInfo.getFileName());
		fileInfo.setUrlPath("http://" + request.getLocalAddr() + ":"
				+ request.getLocalPort() + request.getContextPath() + "/"
				+ relativePath + fileInfo.getFileName());
		
		fileInfo.setAbsolutePath(affixPath + fileInfo.getFileName());
		// 创建文件夹
		File dirFile = new File(affixPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		File file = new File(affixPath + fileInfo.getFileName());
		try {
			multipartFile.transferTo(file);
		} catch (Exception e) {
			logger.error("保存文件出错" + e.getMessage(), e);
			return "保存文件出错" + e.getMessage();
		}
		return null;
	}

	/**
	 * 保存到ftp中
	 * 
	 * @param fileInfo
	 * @param multipartFile
	 * @return
	 */
	public static String save2Ftp(FileInfo fileInfo, MultipartFile multipartFile) {
		// ftp对外展示文件相对路径
		String sshHost = DictCacheService.getThis().getItemValue(DictConstant.DICT_FILE, DictConstant.FILE_SERVER_HOST);
		String sshUserName = DictCacheService.getThis().getItemValue(DictConstant.DICT_FILE, DictConstant.FILE_SERVER_SSH_USER_NAME);
		String sshPassword = DictCacheService.getThis().getItemValue(DictConstant.DICT_FILE, DictConstant.FILE_SERVER_SSH_PASSWORD);
		Integer sshPort = DictCacheService.getThis().getItemIntValue(DictConstant.DICT_FILE, DictConstant.FILE_SERVER_SSH_PORT);
		String serverdeployPath = DictCacheService.getThis().getItemValue(DictConstant.DICT_FILE, DictConstant.FILE_SERVER_DEPLOY_PATH);
		
		try {
			String deployPath = serverdeployPath.trim()+"/"+fileInfo.getDirName().trim();
			while(deployPath.indexOf("//") != -1){
				deployPath = deployPath.replaceAll("//", "/");
			}
			while(deployPath.indexOf("\\") != -1){
				deployPath = deployPath.replaceAll("\\", "/");
			}
			if(!deployPath.startsWith("/")){
				deployPath = "/"+deployPath;
			}
			
			jschUtil.connectSession(sshHost, sshUserName, sshPassword, sshPort);
	        jschUtil.connectSftp();
	        jschUtil.scpStream(multipartFile.getInputStream(), deployPath, fileInfo.getFileName());
	        fileInfo.setFilePath(fileInfo.getDirName());
			fileInfo.setAbsolutePath(deployPath+"/"+fileInfo.getFileName());
			fileInfo.setUrlPath(DictCacheService.getThis().getItemValue(DictConstant.DICT_FILE, DictConstant.FILE_SERVER_APACHE_HOST) + fileInfo.getDirName()+"/" + fileInfo.getFileName());
		} catch (Exception e) {
			logger.error("保存文件出错" + e.getMessage(), e);
			return "保存文件出错" + e.getMessage();
		}
		return null;
	}

	
	@RequestMapping("/goList")
	public ModelAndView goList(FileInfo fileInfo){
		Page page = this.newPage();
		
		this.fileInfoService.queryPage(page);
		return new ModelAndView("system/fileInfo/fileInfo_list")
				.addObject("item", fileInfo);
	}
	
	@RequestMapping("/goSave")
	public ModelAndView goSave(){
		
		return new ModelAndView("system/fileInfo/fileInfo_save");
	}
	
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(Long id){
		FileInfo fileInfo = this.fileInfoService.get(id);
		if(fileInfo == null){
			throw new SysException("查找不到 文件 id="+id);
		}
		
		return new ModelAndView("system/fileInfo/fileInfo_update")
				.addObject("item", fileInfo);
	}
	
	@RequestMapping("/goView")
	public ModelAndView goView(Long id){
		FileInfo fileInfo = this.fileInfoService.get(id);
		if(fileInfo == null){
			throw new SysException("查找不到 文件 id="+id);
		}
		
		return new ModelAndView("system/fileInfo/fileInfo_view")
				.addObject("item", fileInfo);
	}
	
	@ResponseBody
	@RequestMapping("/doSave")
	public Rs doSave(FileInfo fileInfo){
		this.fileInfoService.save(fileInfo);
		return Rs.success();
	}
	
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Rs doUpdate(FileInfo fileInfo){
	
		this.fileInfoService.update(fileInfo);
		return Rs.success();
	}
	
}