package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.management.resource.ResourceRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author study
 * @create 2020-04-20 14:16
 */
@Api(value = "媒资管理接口",description = "媒资管理接口，提供文件上传，处理等接口")
public interface MediaUploadControllerApi {

    //文件上传前的一些准备工作
    @ApiOperation("文件上传注册,校验文件是否存在")
    public ResponseResult register(String fileMd5,
                                    String fileName,
                                    Long fileSize,
                                    String mimetype,
                                    String fileExt);

    @ApiOperation("校验分块文件是否存在")
    public CheckChunkResult checkchunck(String fileMd5,Integer chunk,Integer chunkSize);


    @ApiOperation("上传分块")
    public ResponseResult uploadchunck(MultipartFile file,String fileMd5,Integer chunk);

    @ApiOperation("合并分块")
    public ResponseResult mergechunks(String fileMd5,
                                       String fileName,
                                       Long filesize,
                                       String mimetype,
                                       String fileExt
                                       );


}
