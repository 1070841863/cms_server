package com.xuecheng.api.system;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author study
 * @create 2020-04-08 15:06
 */
@Api(value = "数据字典系统接口",description = "提供数据字典的查询")
public interface SystemDicthinaryControllerApi {

    @ApiOperation("查询数据字典接口")
    public SysDictionary getInfoByType(String type);


}
