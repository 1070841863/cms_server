package com.xuecheng.manager_cms.service;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.interceptor.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manager_cms.dao.CmsPageRepository;
import com.xuecheng.manager_cms.dao.CmsSiteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * @author study
 * @create 2020-03-31 22:08
 */
@Service
public class PageService  {


    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     *页面查询方法
     * @param page 页码，从1开始计数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList( Integer page,Integer size,QueryPageRequest queryPageRequest){
        if(queryPageRequest==null)
        {
            queryPageRequest=new QueryPageRequest();
        }
        //自定义条件查询
        //定义条件查询匹配器
        ExampleMatcher exampleMatcher=ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        exampleMatcher=exampleMatcher.withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage cmsPage=new CmsPage();
        //设置条件值(站点id)
        if(StringUtils.isNoneEmpty(queryPageRequest.getSiteId()))
        {
            cmsPage.setSiteId(queryPageRequest.getSiteId())  ;
        }
        //设置模板id为查询条件
        if(StringUtils.isNoneEmpty(queryPageRequest.getTemplateId()))
        {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());  ;
        }
        //页面别名
        if(StringUtils.isNoneEmpty(queryPageRequest.getPageAliase()))
        {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());  ;
        }
        //名称
        if(StringUtils.isNoneEmpty(queryPageRequest.getPageName()))
        {
            cmsPage.setPageName(queryPageRequest.getPageName());  ;
        }
        //类型
        if(StringUtils.isNoneEmpty(queryPageRequest.getPageType()))
        {
            cmsPage.setPageType(queryPageRequest.getPageType());  ;
        }
        Example<CmsPage> example=Example.of(cmsPage,exampleMatcher);
        if(page<=0){
            page=1;
        }

        page=page-1;
        if(size<=0){
            size=10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);//实现自定义条件查询并且分页查询
        QueryResult queryResult=new QueryResult();
        queryResult.setTotal(all.getTotalElements());//总记录数
        queryResult.setList(all.getContent());//数据列表
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }


    //增加
    public CmsPageResult add(CmsPage cmsPage) {
        if(cmsPage==null){
            //抛出异常 非法参数异常
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //校验页面名称，站点id，页面webpath的唯一性
        CmsPage cms = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(cms!=null){
            //抛出异常 已存在相同的页面名称
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);//添加页面主键由spring data 自动生成
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
    }


    //根据id查询页面
    public CmsPage findById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    //修改
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //根据id查询页面信息
        CmsPage cmsPage1 = this.findById(id);
        if(cmsPage1!=null){
            //准备更新数据 这里为什么不直接调用update，因为不是所有的字段都可以修改的
            //更新模板id
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点id
            cmsPage1.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            cmsPage1.setPageName(cmsPage.getPageName());
            //更新访问路径
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新dataurl
            cmsPage1.setDataUrl(cmsPage.getDataUrl());
            cmsPageRepository.save(cmsPage1);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage1);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL,null);
    }


    //根据id删除页面
    public ResponseResult delete(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            cmsPageRepository.deleteById(id);
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }

    //添加页面
    public CmsPageResult save(CmsPage cmsPage) {
        //检查页面是否存在，根据页面名称，站点id，页面webpath
        CmsPage cms = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(cms!=null){
            //进行更新
            return this.update(cms.getPageId(),cmsPage);
        }
        return this.add(cmsPage);
    }

    @Autowired
    CreateTemplateService createTemplateService;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    //快速发布
    public CmsPostPageResult postpageQuick(CmsPage cmsPage) {
        //添加页面
        CmsPageResult save = this.save(cmsPage);
        if(!save.isSuccess()){
            return new CmsPostPageResult(CommonCode.FAIL,null);
        }
        CmsPage cmsPage1=save.getCmsPage();
        //要发布的页面id
        String pageId=cmsPage1.getPageId();
        //发布页面
        ResponseResult responseResult = createTemplateService.postPage(pageId);
        if(!responseResult.isSuccess()){
            return new CmsPostPageResult(CommonCode.FAIL,null);
        }
        //得到页面的url
        //页面url=站点域名+站点webpath+页面webpath+页面名称
        //站点id
        String siteId=cmsPage1.getSiteId();
        //查询站点信息
        CmsSite siteInfo = findSiteInfo(siteId);
        String url=siteInfo.getSiteDomain()+siteInfo.getSiteWebPath()+cmsPage1.getPageWebPath()+cmsPage1.getPageName();
        return new CmsPostPageResult(CommonCode.SUCCESS,url);
    }

    private CmsSite findSiteInfo(String siteId){
        Optional<CmsSite> o = cmsSiteRepository.findById(siteId);
        if(o.isPresent()){
            return o.get();
        }
        return null;
    }


}
