package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author study
 * @create 2020-04-09 15:04
 */
@Api(value = "文件管理接口",description = "文件管理接口，crud")
public interface FileSystemControllerApi {

    //上传文件
    @ApiOperation("上传文件接口")
    public UploadFileResult upload(MultipartFile multipartFile,//上传的文件
                                   String filetag, //文件标签
                                   String businesskey,//业务表示
                                   String metadata//文件流信息
                                   );

}
