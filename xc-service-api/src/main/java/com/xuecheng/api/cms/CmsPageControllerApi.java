package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author study
 * @create 2020-03-31 17:05
 */

@Api(value = "cms页面管理接口", description = "cms页面管理接口 crud")
public interface CmsPageControllerApi {
    //页面查询
    @ApiOperation("分页查询参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    public QueryResponseResult findList(Integer page, Integer size, QueryPageRequest queryPageRequest);

    //新增页面
    @ApiOperation("新增页面")
    public CmsPageResult add(CmsPage cmsPage);

    //根据页面id查询页面信息
    @ApiOperation("根据页面id查询页面信息")
    public CmsPage findById(String id);

    //修改页面
    @ApiOperation("修改页面")
    public CmsPageResult update(String id,CmsPage cmsPage);

    //删除页面
    @ApiOperation("删除页面")
    public ResponseResult delete(String id);

    //页面发布
    @ApiOperation("页面发布")
    public ResponseResult postPage(String pageId);

    @ApiOperation("保存页面")
    public CmsPageResult save(CmsPage cmsPage);

    @ApiOperation("一键发布接口")
    public CmsPostPageResult postpageQuick(CmsPage cmsPage);
}
