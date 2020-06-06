package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import jdk.management.resource.ResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author study
 * @create 2020-04-20 14:53
 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    private MediaUploadService mediaUploadService;

    //文件上传前的注册
    @PostMapping("/register")
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return mediaUploadService.register(fileMd5,fileName,fileSize,mimetype,fileExt);
    }

    @PostMapping("/checkchunk")
    public CheckChunkResult checkchunck(String fileMd5, Integer chunk, Integer chunkSize) {
        return mediaUploadService.checkchunck(fileMd5,chunk,chunkSize);
    }

    //
    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunck(MultipartFile file, String fileMd5, Integer chunk) {
        return mediaUploadService.uploadchunck(file,fileMd5,chunk);
    }

    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(String fileMd5, String fileName, Long filesize, String mimetype, String fileExt) {
        return mediaUploadService.mergechunks(fileMd5,fileName,filesize,mimetype,fileExt);
    }
}
