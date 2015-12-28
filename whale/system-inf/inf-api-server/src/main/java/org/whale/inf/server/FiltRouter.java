package org.whale.inf.server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.whale.system.base.Rs;

@Controller
@RequestMapping("/file")
public class FiltRouter {

	@ResponseBody
	@RequestMapping("/upload")
	public Rs upload(HttpServletRequest request){
		if (!ServletFileUpload.isMultipartContent(request)) {
			return Rs.success("请选择文件。");
		}
		Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
		if (fileMap == null || fileMap.keySet().size() != 1) {
			return Rs.fail("只能上传一个文件，请开启自动上传模式");
		}
		
		MultipartFile multipartFile = null;
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			try {
				multipartFile = entry.getValue();
				
				File file = new File("c://"+entry.getKey());
				FileOutputStream fos = new FileOutputStream(file);
				IOUtils.write(multipartFile.getBytes(), fos);
				fos.flush();
				fos.close();
				return Rs.success(file.getName());
			} catch (Exception e) {
				return Rs.fail("保存文件异常-"+e.getMessage());
			}
		}
		return Rs.success();
	}
}
