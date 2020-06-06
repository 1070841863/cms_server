package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastjsonSockJsMessageCodec;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.config.RabbitmqConfig;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听mq，接受页面发布消息
 * @create 2020-04-06 20:41
 */
@Component
public class ConsumerPostPage {

    public static final Logger LOGGER= LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    private PageService pageService;


    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage1(String msg){
        //解析消息
        Map map=JSON.parseObject(msg, Map.class);
        //得到消息中的pageId
        String pageId = (String) map.get("pageId");
        //校验页面是否合法
        CmsPage cmsPage= pageService.findCmsPageById(pageId);
        if(cmsPage==null){
            LOGGER.error("receive postpage msg,Cmspage is null,pageId:{}",pageId);
            return;
        }
        //调用service将页面从gridfs下载到服务器
        pageService.savePageToServerPath(pageId);
    }

//    @RabbitListener(queues = {"${xuecheng.mq.queue2}"})
//    public void postPage2(String msg){
//        //解析消息
//        Map map=JSON.parseObject(msg, Map.class);
//        //得到消息中的pageId
//        String pageId = (String) map.get("pageId");
//        //校验页面是否合法
//        CmsPage cmsPage= pageService.findCmsPageById(pageId);
//        if(cmsPage==null){
//            LOGGER.error("receive postpage msg,Cmspage is null,pageId:{}",pageId);
//            return;
//        }
//        //调用service将页面从gridfs下载到服务器
//        pageService.savePageToServerPath(pageId);
//    }

}
