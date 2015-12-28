package org.whale.inf.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.whale.inf.api.FileBean;
import org.whale.inf.common.Result;
import org.whale.system.annotation.web.RespBody;

@Controller
@RequestMapping("/fileIntf")
public class FileIntfRouter {

	@RespBody
	@RequestMapping("/uploadFileBean")
	public Result<String> uploadFileBean(FileBean fileBean) throws IOException {
		File file = new File("c://1.jpg");
		FileOutputStream fos = new FileOutputStream(file);
		IOUtils.write(fileBean.getBytes(), fos);
		
		return Result.success(fileBean.getFileName());
	}

	@RespBody
	@RequestMapping("/uploadFile")
	public Result<String> uploadFile(byte[] datas) throws IOException {
		File file = new File("c://2.jpg");
		FileOutputStream fos = new FileOutputStream(file);
		IOUtils.write(datas, fos);
		return Result.success(file.getName());
	}

}
