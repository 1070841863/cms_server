package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author study
 * @create 2020-04-04 20:37
 */
@Api(value = "cms配置管理接口",description = "cms配置管理接口，提供数据模型的管理，查询接口")
public interface CmsConfigControllerApi {

    //查询
    @ApiOperation("根据主键id查询cms配置信息")
    public CmsConfig getModel(String id);
}
