package org.whale.inf.client;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.whale.inf.api.FileBean;
import org.whale.inf.api.FileIntf;
import org.whale.inf.api.UserInf;
import org.whale.system.base.Rs;
import org.whale.inf.api.User;

@Controller
@RequestMapping("/user")
public class ClientRouter {

	@Autowired
	private UserInf userInf;
	
	@ResponseBody
	@RequestMapping("/get")
	public Rs get(Long id){
		User user = this.userInf.get(id);
		return Rs.success(user);
	}
	
//	@ResponseBody
//	@RequestMapping("/upload")
//	public Rs upload(HttpServletRequest request){
//		if (!ServletFileUpload.isMultipartContent(request)) {
//			return Rs.success("请选择文件。");
//		}
//		Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
//		if (fileMap == null || fileMap.keySet().size() != 1) {
//			return Rs.fail("只能上传一个文件，请开启自动上传模式");
//		}
//		
//		MultipartFile multipartFile = null;
//		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
//			try {
//				multipartFile = entry.getValue();
//				
//				FileBean fileBean = new FileBean();
//				fileBean.setBytes(multipartFile.getBytes());
//				fileBean.setFileName(multipartFile.getName());
//				
//				String name = this.fileIntf.uploadFileBean(fileBean);
//				return Rs.success(name);
//			} catch (Exception e) {
//				return Rs.fail("保存文件异常-"+e.getMessage());
//			}
//		}
//		return Rs.success();
//	}
}
