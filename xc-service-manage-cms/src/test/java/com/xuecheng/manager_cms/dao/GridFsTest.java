package com.xuecheng.manager_cms.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.manager_cms.service.CreateTemplateService;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author study
 * @create 2020-04-04 22:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Test
    public void testGridFs() throws FileNotFoundException {
        //要储存的文件
        File f = new File("E:\\cms_study(2020329)\\kc-template\\course.ftl");
        FileInputStream fileInputStream = new FileInputStream(f);
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "2020-4-12测试");
        System.out.println(objectId);

    }

    @Autowired
    private GridFSBucket gridFSBucket;

    @Test
    public void testReadGridFs() throws IOException {
        //根据文件id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e889510e4fea405a0dccc04")));
        //打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridfsResource对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        //从流中来取数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(content);
    }


    @Autowired
    CreateTemplateService createTemplateService;

    @Test
    public void testcreate(){
        String pageHmtl = createTemplateService.getPageHmtl("5e8953cee4fea419bcfc1785");
        System.out.println(pageHmtl);
    }

}
