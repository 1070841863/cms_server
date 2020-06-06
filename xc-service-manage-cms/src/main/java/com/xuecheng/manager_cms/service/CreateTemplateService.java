package com.xuecheng.manager_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.interceptor.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manager_cms.config.RabbitmqConfig;
import com.xuecheng.manager_cms.dao.CmsConfigRepository;
import com.xuecheng.manager_cms.dao.CmsPageRepository;
import com.xuecheng.manager_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

/**
 * 页面静态化开发
 */
@Service
public class CreateTemplateService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    //页面静态化方法
    /**
     * 1、静态化程序获取页面的DataUrl
     * 2.静态化程序远程请求DataUrl获取数据模型.
     * 3、静态化程序获取页面的模板信息
     * 4.执行页面静态化
     */
    public String getPageHmtl(String pageId) {

        //获取数据模型
        Map model = getModelByPageId(pageId);
        if(model==null){
            //没有模型数据
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面模板
        String template = getTemplateById(pageId);
        if(StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行静态化
        String content = generateHtml(template, model);

        return content;
    }

    //获取数据模型
    private Map getModelByPageId(String pageId) {
        //取出页面信息
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            //取出页面的dataUrl
            //页面url为空
            if(StringUtils.isEmpty(cmsPage.getDataUrl())){
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
            }
            //通过restTemplate远程获取模型数据
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(cmsPage.getDataUrl(), Map.class);
            return forEntity.getBody();

        }else{
            //找不到页面
            ExceptionCast.cast(CmsCode.CMS_CANT_FIND_PAGE);
        }
        return null;
    }

    //获取页面模板
    private String getTemplateById(String pageId) {
        //取出页面信息
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            //取出页面的模板id
            String templateId = cmsPage.getTemplateId();
            if(StringUtils.isEmpty(templateId)){
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
            }
            Optional<CmsTemplate> optional1 = cmsTemplateRepository.findById(templateId);
            if(optional1.isPresent()){
                CmsTemplate cmsTemplate = optional1.get();
                //获取模板文件id
                String templateFileId = cmsTemplate.getTemplateFileId();
                //从GridFs中取模板文件内容
                try {
                    String templateFile_data = getTemplateFile_Data(templateFileId);
                    return templateFile_data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }else{
            //找不到页面
            ExceptionCast.cast(CmsCode.CMS_CANT_FIND_PAGE);
        }
        return null;
    }


    //从通过模板id获取模板文件的信息，转为string
    private String getTemplateFile_Data(String templateFileId) throws IOException {
        //根据文件id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        //打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridfsResource对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        //从流中来取数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        return content;
    }

    //执行静态化方法
    public String generateHtml(String template_data,Map model){
        //定义配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
        //使用一个模板加载器变为模板
        StringTemplateLoader stringTemplateLoader=new StringTemplateLoader();
        //定义模板名称和模板里的值
        stringTemplateLoader.putTemplate("template_lunbo",template_data);
        //在配置中设置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板内容
        Template template = null;
        try {
            template = configuration.getTemplate("template_lunbo", "utf-8");
            //静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            //这个content就是绑定了数据之后的content。
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public ResponseResult postPage(String pageId){
        //1.执行页面静态化
        String pageHmtl = this.getPageHmtl(pageId);
        //2.将页面静态化文件储存到Gridfs中
        CmsPage cmsPage = saveHtml(pageId, pageHmtl);
        //3.向mq发消息
        sendMessage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private void sendMessage(String pageId){
        //得到页面信息
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        CmsPage cmsPage=null;
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        cmsPage=optional.get();
        //拼装消息对象
        Map<String,String> map=new HashMap<>();
        map.put("pageId",pageId);
        //转为json
        String json = JSON.toJSONString(map);
        //发送给mq
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_NAME,cmsPage.getSiteId(),json);

    }


    //保存html到gridfs
    private CmsPage saveHtml(String pageId,String htmlContent){
        //先得到页面信息
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        CmsPage cmsPage=null;
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        cmsPage=optional.get();
        ObjectId objectId=null;
        //将htmlcontent转为输入流
        try {
            InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
            //将html保存在gridfs中
            objectId= gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将html文件的id更新到cmspage中
        cmsPage.setHtmlFileId(objectId.toString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }




}
