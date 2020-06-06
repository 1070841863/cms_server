package com.xuecheng.manager_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manager_cms.service.CreateTemplateService;
import com.xuecheng.manager_cms.service.PageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author study
 * @create 2020-03-31 21:34
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;


    @Autowired
    private CreateTemplateService createTemplateService;


    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") Integer page,
                                        @PathVariable("size") Integer size,
                                        QueryPageRequest queryPageRequest)
    {
        return pageService.findList(page,size,queryPageRequest);
    }


    @PostMapping("/add")
    //@RequestBody将前台发的json转为对象
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return pageService.findById(id);
    }

    @PutMapping("/edit/{id}") //这里使用put方法，方法中put表示更新
    public CmsPageResult update(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.update(id,cmsPage);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return pageService.delete(id);
    }

    @GetMapping("/postPage/{pageId}")
    public ResponseResult postPage(@PathVariable("pageId")String pageId) {
        return createTemplateService.postPage(pageId);
    }

    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return pageService.save(cmsPage);
    }

    @PostMapping("/postPageQuick")
    public CmsPostPageResult postpageQuick(@RequestBody CmsPage cmsPage) {
        return pageService.postpageQuick(cmsPage);
    }
}
