package org.whale.system.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.base.BaseController;
import org.whale.system.base.Page;
import org.whale.system.base.UserContext;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.constant.DictConstant;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.common.util.WebUtil;
import org.whale.system.controller.ueditor.ActionEnter;
import org.whale.system.service.FileInfoService;
import org.whale.system.domain.FileInfo;

import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/fileInfo")
public class FileInfoController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileInfoController.class);
	
	@Autowired
	private FileInfoService fileInfoService;
	@Autowired
	private DictCacheService dictCacheService;
	
	@RequestMapping("/goUeditor")
	public ModelAndView goUeditor(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("system/fileInfo/ueditor");
	}
	
	@RequestMapping("/doUeditorExec")
	public void doUeditorExec(HttpServletRequest request, HttpServletResponse response) {
		String rs = new ActionEnter(request).exec();
		WebUtil.doPrint(request, response, rs);
	}
	
	@RequestMapping("/goFileUpload")
	public ModelAndView goFileUpload(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("system/fileInfo/file_uploader");
	}
	
	/**
	 * 上传文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/doFileUpload")
	public void doFileUpload(HttpServletRequest request, HttpServletResponse response) {
		if (!ServletFileUpload.isMultipartContent(request)) {
			WebUtil.printSuccess(request, response, "请选择文件。");
			return;
		}
		
		FileInfo fileInfo = new FileInfo();

		Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
		if (fileMap == null || fileMap.keySet().size() != 1) {
			WebUtil.printFail(request, response, "只能上传一个文件，请开启自动上传模式");
			return;
		}

		MultipartFile multipartFile = null;
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			try {
				multipartFile = entry.getValue();
				decorateFileInfo(request, multipartFile,fileInfo);
				String info = doSaveStream(fileInfo, multipartFile, request);
				if (info != null) {
					WebUtil.printFail(request, response, info);
				}
				this.fileInfoService.save(fileInfo);
			} catch (Exception e) {
				WebUtil.printFail(request, response, "保存文件异常-"+e.getMessage());
				logger.error("保存文件出错" + e.getMessage(), e);
			}
		}

		WebUtil.printSuccess(request, response, fileInfo);
		return;
	}
	
	@RequestMapping("/goImgUpload")
	public ModelAndView goImgUpload(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("system/fileInfo/img_uploader");
	}
	
	@RequestMapping("/doImgUpload")
	public void doImgUpload(HttpServletRequest request, HttpServletResponse response) {
		if (!ServletFileUpload.isMultipartContent(request)) {
			WebUtil.printSuccess(request, response, "请选择文件");
			return;
		}

		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
		if (fileMap == null || fileMap.keySet().size() != 1) {
			WebUtil.printFail(request, response, "只能上传一个文件，请开启自动上传模式");
			return;
		}
		
		FileInfo fileInfo = new FileInfo();
		
		MultipartFile multipartFile = null;
		BufferedImage bi = null;

		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			try {
				multipartFile = entry.getValue();
				bi = ImageIO.read(multipartFile.getInputStream());
				if (bi == null) {
					WebUtil.printFail(request, response, "读取不到文件流数据");
					return;
				}
				// 设置图片的高度和宽度
				fileInfo.setWidth(bi.getWidth());
				fileInfo.setHeight(bi.getHeight());

				decorateFileInfo(request, multipartFile, fileInfo);
				String info = doSaveStream(fileInfo, multipartFile, request);

				if (info != null) {
					WebUtil.printFail(request, response, info);
					return ;
				}
				bi = null;
				this.fileInfoService.save(fileInfo);
			} catch (Exception e) {
				WebUtil.printFail(request, response, "保存图片异常-"+e.getMessage());
				logger.error("保存图片出错" + e.getMessage(), e);
			}
		}
		WebUtil.printSuccess(request, response, fileInfo);
		return;
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
	public ModelAndView goEditImg(HttpServletRequest request, HttpServletResponse response, Long fileId) {
		FileInfo fileInfo = this.fileInfoService.get(fileId);

		return new ModelAndView("system/fileInfo/img_edit")
			.addObject("fileInfo", fileInfo)
			.addObject("fileId", fileId)
			.addObject("tagId", request.getParameter("tagId"));
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
	public ModelAndView goSplitImg(HttpServletRequest request, HttpServletResponse response, Long fileId) {
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
	@RequestMapping("/doEditImg")
	public void doEditImg(HttpServletRequest request,HttpServletResponse response, 
			Long fileId, int nodeX, int nodeY,int nodeW, int nodeH, int width, 
			int height, int divWidth, int divHeight) {
		FileInfo fileInfo = null;
		if (fileId == null || (fileInfo = this.fileInfoService.get(fileId)) == null) {
			WebUtil.printFail(request, response, "查找不到文件上传记录");
			return;
		}
		double radioX = new Double(divWidth) / fileInfo.getWidth();
		double radioY = new Double(divHeight) / fileInfo.getHeight();

		if (FileInfo.SAVE_DISK.equals(fileInfo.getSaveWay())) {
			String filePath = fileInfo.getAbsolutePath();
			if (!ImageUtil.compressPic(filePath, filePath, radioX, radioY)) {
				WebUtil.printFail(request, response, "压缩图片失败");
				return;
			}
			// 起点非原点，则有裁剪动作
			if (nodeX != 0 || nodeY != 0) {
				if (!ImageUtil.abscut(filePath, filePath, nodeX, nodeY, width, height)) {
					WebUtil.printFail(request, response, "压缩图片成功，裁剪图片失败");
					return;
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
				WebUtil.printFail(request, response, "保存修改文件错误");
				return ;
			}
		} else {
//			if (!ImageUtil.compressPic(fileInfo, radioX, radioY)) {
//				WebUtil.printFail(request, response, "压缩图片失败");
//				return;
//			}
			fileInfo.setWidth(divWidth);
			fileInfo.setHeight(divHeight);

			// 起点非原点，则有裁剪动作
			if (nodeX != 0 || nodeY != 0) {
//				if (!ImageUtil.abscut(fileInfo, nodeX, nodeY, width, height)) {
//					WebUtil.printFail(request, response,
//							"压缩图片成功，裁剪图片失败");
//					return;
//				} else {
//					fileInfo.setWidth(width);
//					fileInfo.setHeight(height);
//				}
			}
		}

		this.fileInfoService.update(fileInfo);
		WebUtil.printSuccess(request, response, fileInfo);
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
	@RequestMapping(value = "/doDelete")
	public void doDelete(HttpServletRequest request, HttpServletResponse response, Long fileId) throws Exception {
		if (fileId == null) {
			WebUtil.printFail(request, response, "请选择要删除的文件");
			return;
		}
		
		FileInfo fileInfo = this.fileInfoService.get(fileId);
		if (fileInfo != null) {
			if(FileInfo.SAVE_DISK.equals(fileInfo.getSaveWay())){
				File file = new File(fileInfo.getAbsolutePath());
				if(file.exists()){
					file.delete();
				}
			}
			
			//TODO 删除FTP
			
			this.fileInfoService.delete(fileId);
		}
		WebUtil.printSuccess(request, response);
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
			if (fileInfo.getSaveWay() == null || FileInfo.SAVE_DISK.equals(fileInfo.getSaveWay())) {
				String fileName = (request.getSession().getServletContext()
						.getRealPath("/") + fileInfo.getFilePath())
						.replaceAll("\\\\", "/").replaceAll("//", "/")
						+ fileInfo.getFileName();
				File file = new File(fileName);
				if (file.exists()) {
					//WebUtil.downLoad(request, response, fileName);
				}
			}
		} catch (Exception e) {
			logger.error("下载文件出错" + e.getMessage(), e);
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
	public static void decorateFileInfo(HttpServletRequest request, MultipartFile multipartFile, FileInfo fileInfo) {
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
		fileInfo.setCreateTime(new Date());
		/** 文件存储用户ID* */
		fileInfo.setCreator(((UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT)).getUserId());
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
	public static String doSaveStream(FileInfo fileInfo, MultipartFile multipartFile, HttpServletRequest request) {
		if (FileInfo.SAVE_DISK.equals(fileInfo.getSaveWay())) {
			return save2Disk(fileInfo, multipartFile, request);
		} else if (FileInfo.SAVE_FTP.equals(fileInfo.getSaveWay())) {
			return null;
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
	public static String save2Disk(FileInfo fileInfo, MultipartFile multipartFile, HttpServletRequest request) {
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
	 
	public boolean save2Ftp(FileInfo fileInfo, MultipartFile multipartFile) {
		UserContext uc = UserContextHolder.getUserContext();
		// ftp对外展示文件相对路径
		String relativePath = fileInfo.getDirName().trim() + "/"
				+ uc.getCityCode() + "/";
		String netAddress = DictCacheUtil.getItemValue(
				DictConstant.ABSTRACT_DICT_FTP_TEST,
				DictConstant.DICT_NET_ADDRESS);
		if (StringUtils.isBlank(netAddress))
			throw new RuntimeException("字典元素【文件服务器】["
					+ DictConstant.DICT_NET_ADDRESS + "] 为设置地址");
		netAddress = netAddress.trim();
		if (!netAddress.endsWith("/"))
			netAddress = netAddress + "/";

		FtpUtil ftpUtil = new FtpUtil(DictConstant.ABSTRACT_DICT_FTP_TEST);

		fileInfo.setFilePath(relativePath);
		fileInfo.setAffixPath(ftpUtil.getFullPath(relativePath));
		fileInfo.setFullUrl(netAddress + relativePath
				+ fileInfo.getFileName());

		try {
			return ftpUtil.uploadFile(multipartFile.getInputStream(),
					relativePath, fileInfo.getFileName());
		} catch (IOException e) {
			logger.error("保存文件出错" + e.getMessage(), e);
			return false;
		} finally {
			if (ftpUtil != null)
				ftpUtil.closeConnect();
		}
	}
*/
	
	@RequestMapping("/goList")
	public ModelAndView goList(HttpServletRequest request, HttpServletResponse response, FileInfo fileInfo){
		Page page = this.newPage(request);
		
		this.fileInfoService.queryPage(page);
		return new ModelAndView("system/fileInfo/fileInfo_list")
				.addObject("item", fileInfo);
	}
	
	@RequestMapping("/goSave")
	public ModelAndView goSave(HttpServletRequest request, HttpServletResponse response){
		
		return new ModelAndView("system/fileInfo/fileInfo_save");
	}
	
	@RequestMapping("/goUpdate")
	public ModelAndView goUpdate(HttpServletRequest request, HttpServletResponse response, Long id){
		FileInfo fileInfo = this.fileInfoService.get(id);
		if(fileInfo == null){
			throw new SysException("查找不到 文件 id="+id);
		}
		
		return new ModelAndView("system/fileInfo/fileInfo_update")
				.addObject("item", fileInfo);
	}
	
	@RequestMapping("/goView")
	public ModelAndView goView(HttpServletRequest request, HttpServletResponse response, Long id){
		FileInfo fileInfo = this.fileInfoService.get(id);
		if(fileInfo == null){
			throw new SysException("查找不到 文件 id="+id);
		}
		
		return new ModelAndView("system/fileInfo/fileInfo_view")
				.addObject("item", fileInfo);
	}
	
	
	@RequestMapping("/doSave")
	public void doSave(HttpServletRequest request, HttpServletResponse response, FileInfo fileInfo){
	
		try {
			this.fileInfoService.save(fileInfo);
		} catch (OrmException e) {
			WebUtil.printFail(request, response, e.getMessage());
			return ;
		}
		WebUtil.printSuccess(request, response);
	}
	
	
	@RequestMapping("/doUpdate")
	public void doUpdate(HttpServletRequest request, HttpServletResponse response, FileInfo fileInfo){
	
		try {
			this.fileInfoService.update(fileInfo);
		} catch (OrmException e) {
			WebUtil.printFail(request, response, e.getMessage());
			return ;
		}
		WebUtil.printSuccess(request, response);
	}
	
}