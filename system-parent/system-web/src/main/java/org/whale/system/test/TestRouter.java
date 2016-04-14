package org.whale.system.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.common.util.Strings;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/test")
public class TestRouter {

	@RequestMapping("/doUpload")
	public ModelAndView doUpload(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("manage/app/app_import");

        if (!ServletFileUpload.isMultipartContent(request)) {
            return mv.addObject("info", "11");
        }

        Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
        if (fileMap == null || fileMap.keySet().size() != 1) {
            return mv.addObject("info", "11");
        }

        MultipartFile multipartFile = null;
        Workbook workbook = null;
        CommentXlsParam param = null;
        List<CommentXlsParam> params = new ArrayList<CommentXlsParam>(800);
        
        StringBuilder strb = new StringBuilder();
        StringBuilder strb2 = new StringBuilder();
        
        for (Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            try {
                workbook = ExcelUtil.get(entry.getValue().getInputStream());
                if (workbook == null)
                    return mv.addObject("info", "33");
                
                Sheet sheet = workbook.getSheet(0);
                
                
                int i=1;
                while(Strings.isNotBlank(sheet.getCell(1, i).getContents())){
                	param = new CommentXlsParam();

                    param.setLine(Integer.parseInt(sheet.getCell(0, i).getContents()));
                	param.setCommodityCode(sheet.getCell(1, i).getContents());
                	param.setStyle(sheet.getCell(3, i).getContents());
                	param.setSize(sheet.getCell(4, i).getContents());
                	param.setSales(Integer.parseInt(sheet.getCell(5, i).getContents()));
                	param.setScore(sheet.getCell(6, i).getContents());
                	param.setUserName(sheet.getCell(7, i).getContents());
                	param.setComment(sheet.getCell(8, i).getContents());
                	param.setCommentTime(sheet.getCell(9, i).getContents());

                    i++;
                    params.add(param);
                	strb.append(JSON.toJSONString(param)).append("\n");
                    strb2.append(JSON.toJSONString(param)).append(",\n");
                }
                
                IOUtils.write(strb.toString(), new FileOutputStream(new File("c://comment.txt")));
                IOUtils.write(strb2.toString(), new FileOutputStream(new File("c://comments.txt")));
                IOUtils.write(JSON.toJSONString(params), new FileOutputStream(new File("c://commentArr.txt")));
            }catch (IOException e) {
                e.printStackTrace();
                return mv.addObject("info", "11");
            }
        }
        return mv.addObject("info", "22");
	}
}
