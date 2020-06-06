package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author study
 * @create 2020-04-21 17:54
 */
@Component
public class MediaProcessTask {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpegpath;
    @Value("${xc-service-manage-media.video-location}")
    private String videolocation;

    @RabbitListener(queues ="${xc-service-manage-media.mq.queue-media-video-processor}",
        containerFactory = "customContainerFactory"
        )
    public void receiveMediaProcessTask(String msg){
        //视频处理
        //1.解析消息内容得到mediaId
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String) map.get("mediaId");
        //2.拿mediaId从数据库查询文件信息
        Optional<MediaFile> optionalMediaFile = mediaFileRepository.findById(mediaId);
        if(!optionalMediaFile.isPresent()){
            return;
        }
        MediaFile mediaFile = optionalMediaFile.get();
        //3.使用工具类将avi生成mp4
        //文件类型
        String fileType = mediaFile.getFileType();
        if(!fileType.equals("avi")){
            mediaFile.setProcessStatus("303004");//将状态更新为无需处理
            mediaFileRepository.save(mediaFile);
            return;
        }
        else{
            //需要处理
            mediaFile.setProcessStatus("303001");//正在处理
            mediaFileRepository.save(mediaFile);
            //处理的文件视频路径
            String video_path = videolocation+mediaFile.getFilePath()+mediaFile.getFileName();
            //生成的文件mp4文件名称
            String mp4_name =mediaFile.getFileId()+".mp4";
            //储存mp4文件的路径 与处理的视频在一块。
            String mp4folder_path =videolocation+mediaFile.getFilePath();
            Mp4VideoUtil mp4VideoUtil=new Mp4VideoUtil(ffmpegpath,video_path,mp4_name,mp4folder_path);
            String s = mp4VideoUtil.generateMp4();
            if(s==null||!s.equals("success")){
                //处理失败
                mediaFile.setProcessStatus("303003");//处理失败
                MediaFileProcess_m3u8 mediaFileProcess_m3u8=new MediaFileProcess_m3u8();
                mediaFileProcess_m3u8.setErrormsg(s);//失败原因
                mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
                mediaFileRepository.save(mediaFile);
                return;
            }
            //4.将mp4生成m3u8和ts文件
            String mp4_path=videolocation+mediaFile.getFilePath()+mediaFile.getFileId()+".mp4";
            String m3u8_name=mediaFile.getFileId()+".m3u8";
            //m3u8文件所在目录
            String m3u8_path=videolocation+mediaFile.getFilePath()+"hls/";
            HlsVideoUtil hlsVideoUtil=new HlsVideoUtil(ffmpegpath,mp4_path,m3u8_name,m3u8_path);
            String res = hlsVideoUtil.generateM3u8();
            if(res==null||!res.equals("success")){
                //处理失败
                mediaFile.setProcessStatus("303003");//处理失败
                MediaFileProcess_m3u8 mediaFileProcess_m3u8=new MediaFileProcess_m3u8();
                mediaFileProcess_m3u8.setErrormsg(s);//失败原因
                mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
                mediaFileRepository.save(mediaFile);
                return;
            }else{
                mediaFile.setProcessStatus("303002");//处理成功
                //获取ts文件列表
                List<String> ts_list = hlsVideoUtil.get_ts_list();
                MediaFileProcess_m3u8 mediaFileProcess_m3u8=new MediaFileProcess_m3u8();
                mediaFileProcess_m3u8.setTslist(ts_list);
                mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);

                //保存fileUrl，此url就是视频播放的相对路径
                String fileUrl=mediaFile.getFilePath()+"hls/"+m3u8_name;
                mediaFile.setFileUrl(fileUrl);
                mediaFileRepository.save(mediaFile);
            }
        }



    }
}
