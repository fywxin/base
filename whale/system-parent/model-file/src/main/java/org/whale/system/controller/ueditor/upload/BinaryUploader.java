package org.whale.system.controller.ueditor.upload;


import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.whale.system.controller.FileInfoController;
import org.whale.system.controller.ueditor.define.AppInfo;
import org.whale.system.controller.ueditor.define.BaseState;
import org.whale.system.controller.ueditor.define.FileType;
import org.whale.system.controller.ueditor.define.State;
import org.whale.system.domain.FileInfo;
import org.whale.system.service.FileInfoService;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

public class BinaryUploader {
	
	public static final State save(HttpServletRequest request, Map<String, Object> conf) {
		FileInfo fileInfo = new FileInfo();
		Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
		
		MultipartFile multipartFile = null;
		BufferedImage bi = null;

		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			try {
				multipartFile = entry.getValue();
				if(multipartFile.getInputStream() == null){
					return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
				}
				
				String realName = multipartFile.getOriginalFilename();
				String suffix = FileType.getSuffixByFilename(realName);
				
				if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
					return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
				}
				FileInfoController.decorateFileInfo(request, multipartFile, fileInfo);
				if(FileInfo.TYPE_IMG.equals(fileInfo.getFileType())){
					bi = ImageIO.read(multipartFile.getInputStream());
					// 设置图片的高度和宽度
					fileInfo.setWidth(bi.getWidth());
					fileInfo.setHeight(bi.getHeight());
				}
				
				String info = FileInfoController.doSaveStream(fileInfo, multipartFile, request);

				if (info != null) {
					return new BaseState(false, AppInfo.IO_ERROR);
				}
				bi = null;
				
				fileInfo.setExtInfo("{\"type\": \"ueditor\"}");
				FileInfoService.getThis().save(fileInfo);
				
				State state = new BaseState(true);
				state.putInfo( "size", fileInfo.getSizeKB());
				state.putInfo( "title", fileInfo.getFileName());
				state.putInfo("url", fileInfo.getUrlPath());
				state.putInfo("type", suffix);
				state.putInfo("original", realName);
				return state;
			} catch (Exception e) {
				return new BaseState(false, AppInfo.IO_ERROR);
			}
		}
		
		return new BaseState(false, AppInfo.IO_ERROR);
	}

//	public static final State save(HttpServletRequest request, Map<String, Object> conf) {
//		FileItemStream fileStream = null;
//		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;
//
//		if (!ServletFileUpload.isMultipartContent(request)) {
//			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
//		}
//
//		ServletFileUpload upload = new ServletFileUpload(
//				new DiskFileItemFactory());
//
//        if ( isAjaxUpload ) {
//            upload.setHeaderEncoding( "UTF-8" );
//        }
//
//		try {
//			FileItemIterator iterator = upload.getItemIterator(request);
//
//			while (iterator.hasNext()) {
//				fileStream = iterator.next();
//
//				if (!fileStream.isFormField())
//					break;
//				fileStream = null;
//			}
//
//			if (fileStream == null) {
//				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
//			}
//
//			String savePath = (String) conf.get("savePath");
//			String originFileName = fileStream.getName();
//			String suffix = FileType.getSuffixByFilename(originFileName);
//
//			originFileName = originFileName.substring(0,
//					originFileName.length() - suffix.length());
//			savePath = savePath + suffix;
//
//			long maxSize = ((Long) conf.get("maxSize")).longValue();
//
//			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
//				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
//			}
//
//			savePath = PathFormat.parse(savePath, originFileName);
//
//			String physicalPath = (String) conf.get("rootPath") + savePath;
//
//			InputStream is = fileStream.openStream();
//			State storageState = StorageManager.saveFileByInputStream(is,
//					physicalPath, maxSize);
//			is.close();
//
//			if (storageState.isSuccess()) {
//				//wjs 
//				//storageState.putInfo("url", PathFormat.format(savePath));
//				storageState.putInfo("url", request.getContextPath()+"/"+PathFormat.format(savePath));
//				storageState.putInfo("type", suffix);
//				storageState.putInfo("original", originFileName + suffix);
//			}
//
//			return storageState;
//		} catch (FileUploadException e) {
//			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
//		} catch (IOException e) {
//		}
//		return new BaseState(false, AppInfo.IO_ERROR);
//	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
