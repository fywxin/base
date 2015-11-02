package org.whale.system.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.constant.SysConstant;


public class VerifiedServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(VerifiedServlet.class);
	private static final long serialVersionUID = -34545L;

	public VerifiedServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/jpeg");
		OutputStream out = response.getOutputStream();
		try {
			Random random = new Random();
			StringBuilder strb = new StringBuilder()
									.append(random.nextInt(10))
									.append(random.nextInt(10))
									.append(random.nextInt(10))
									.append(random.nextInt(10));
			request.getSession().setAttribute(SysConstant.VERITY_CODE_KEY, strb.toString());
			
			VerifiedCodeGenerator codeGenerator = new VerifiedCodeGenerator();
			codeGenerator.setImgWidth(60);
			codeGenerator.setImgHeight(22);
			codeGenerator.output(out, "jpg", codeGenerator.createImage(strb.toString()));
		} catch (Exception e) {
			logger.error("校验码生产异常：", e);
			//TODO 自动创建temp目录
		}finally{
			if(out != null)
				out.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

	public void init() throws ServletException {
	}

}
