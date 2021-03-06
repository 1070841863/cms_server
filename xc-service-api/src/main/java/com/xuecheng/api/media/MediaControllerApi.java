package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author study
 * @create 2020-04-21 20:52
 */
@Api(value = "媒体文件管理",description = "媒体文件管理接口")
public interface MediaControllerApi {

    @ApiOperation("我的媒资文件查询页面")
    public QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);

}
