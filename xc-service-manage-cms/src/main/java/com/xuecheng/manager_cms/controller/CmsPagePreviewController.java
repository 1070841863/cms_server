package com.xuecheng.manager_cms.controller;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manager_cms.service.CreateTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @author study
 * @create 2020-04-05 14:16
 */
@Controller
@RequestMapping("/cms")
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private CreateTemplateService createTemplateService;

    //页面预览
    @GetMapping("/preview/{pageId}")
    public void preview(@PathVariable("pageId")String pageId) throws IOException {
        //执行静态化
        String pageHmtl = createTemplateService.getPageHmtl(pageId);
        ServletOutputStream outputStream = this.response.getOutputStream();
        response.setHeader("Content-type","text/html;charset=utf-8");
        outputStream.write(pageHmtl.getBytes("utf-8"));
        outputStream.close();
    }
}
