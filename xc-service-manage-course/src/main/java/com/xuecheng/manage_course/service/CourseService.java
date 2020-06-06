package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.interceptor.ExceptionCast;
import com.xuecheng.framework.interceptor.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import com.xuecheng.manage_course.feign.CmsPageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
public class CourseService {
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CoursePicRepository coursePicRepository;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePubRepository coursePubRepository;
    @Autowired
    private TeachPlanMediaRepository teachPlanMediaRepository;
    @Autowired
    private TeachPlanMediaPubRepository teachPlanMediaPubRepository;


    //查询课程计划

    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

    //添加课程计划
    @Transactional
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        //处理parentid
        if(teachplan==null||StringUtils.isEmpty(teachplan.getCourseid())||StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程id
        String courseid = teachplan.getCourseid();
        //parentid
        String parentid = teachplan.getParentid();
        if(StringUtils.isEmpty(parentid)){
            //取出该课程的根节点
            parentid= this.getTeachplanRoot(courseid);
        }
        //查询父节点的信息
        Optional<Teachplan> optionalTeachplan = teachplanRepository.findById(parentid);
        Teachplan parentsNode = optionalTeachplan.get();
        //父节点的级别
        String grade = parentsNode.getGrade();
        //新节点信息
        Teachplan teachplan_new=new Teachplan();
        //将页面提交的信息teachplan信息拷贝到teachplan_new对象中
        BeanUtils.copyProperties(teachplan,teachplan_new);
        teachplan_new.setParentid(parentid);
        teachplan_new.setCourseid(courseid);
        if(grade.equals("1")){
            teachplan_new.setGrade("2");//级别，根据父节点的级别来设置
        }else{
            teachplan_new.setGrade("3");
        }
        teachplanRepository.save(teachplan_new);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    //查询课程的根节点,如果查询不到，要自动添加根节点
    private String getTeachplanRoot(String courseid){
        //查询课程信息
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if(!optional.isPresent()){
            return null;
        }
        //课程信息
        CourseBase courseBase = optional.get();
        //查询课程的根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if(teachplanList==null||teachplanList.size()<=0){
            //查询不到，自动添加根节点
            Teachplan teachplan=new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseid);
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        //返回根节点
        return teachplanList.get(0).getId();

    }

    //查询课程视图 包括基本信息，图片，营销，课程计划
    public CourseView getCourseView(String courseid) {
        CourseView courseView=new CourseView();
        //基本信息
        Optional<CourseBase> optional1 = courseBaseRepository.findById(courseid);
        if(optional1.isPresent()){
            courseView.setCourseBase(optional1.get());
        }
        //课程图片
        Optional<CoursePic> optional2 = coursePicRepository.findById(courseid);
        if(optional2.isPresent()){
            courseView.setCoursePic(optional2.get());
        }
        Optional<CourseMarket> optional3 = courseMarketRepository.findById(courseid);
        if(optional3.isPresent()){
            courseView.setCourseMarket(optional3.get());
        }
        TeachplanNode teachplanList = this.findTeachplanList(courseid);
        courseView.setTeachplanNode(teachplanList);
        return courseView;
    }


//    course-publish:
//    siteId: 5e93f7ef3b6c1e44048fe00b
//    templateId: 5aec5dd70e661808240ab7a6
//    previewUrl: http://www.cms.com/cms/preview/
//    pageWebPath: /course/detail
//    pagePhysicalPath: /course/detail/
//    dataUrlPre: http://localhost:31200/course/courseview/
    @Value("${course-publish.siteId}")
    private String siteId;
    @Value("${course-publish.templateId}")
    private String templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;
    @Value("${course-publish.pageWebPath}")
    private String pageWebPath;
    @Value("${course-publish.pagePhysicalPath}")
    private String pagePhysicalPath;
    @Value("${course-publish.dataUrlPre}")
    private String dataUrlPre;
    @Autowired
    CmsPageClient cmsPageClient;


    public CoursePublishResult preview(String id) {
        //根据id查询基本信息
        CourseBase courseBase = this.CoursefindbyId(id);

        //发布预览课程
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId(siteId);
        cmsPage.setTemplateId(templateId);
        cmsPage.setPageName(id+".html");
        cmsPage.setPageWebPath(pageWebPath);
        cmsPage.setPageAliase(courseBase.getName());
        //储存路径
        cmsPage.setPagePhysicalPath(pagePhysicalPath);
        //数据url
        cmsPage.setDataUrl(dataUrlPre+id);
        //远程访问cms接口保存页面信息到cms_page表中。
        CmsPageResult result = cmsPageClient.save(cmsPage);
        if(!result.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        //页面id
        String pageId=result.getCmsPage().getPageId();
        //页面url
        String pageUrl=previewUrl+pageId;
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    CourseBase CoursefindbyId(String id){
        Optional<CourseBase> byId = courseBaseRepository.findById(id);
        if(byId.isPresent()){
            return byId.get();
        }
        ExceptionCast.cast(CourseCode.COURSE_NO_RESULT);
        return null;
    }


    @Transactional
    public CoursePublishResult publish(String id) {
        //根据id查询基本信息
        CourseBase courseBase = this.CoursefindbyId(id);

        //发布预览课程
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId(siteId);
        cmsPage.setTemplateId(templateId);
        cmsPage.setPageName(id+".html");
        cmsPage.setPageWebPath(pageWebPath);
        cmsPage.setPageAliase(courseBase.getName());
        //储存路径
        cmsPage.setPagePhysicalPath(pagePhysicalPath);
        //数据url
        cmsPage.setDataUrl(dataUrlPre+id);
        //调用cms一键发布接口
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postpageQuick(cmsPage);
        if(!cmsPostPageResult.isSuccess())
        {
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        //将保存课程的发布状态为"已发布"
        CourseBase courseBase1 = updateStatus(id);
        if(courseBase1==null){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        //保存课程索引信息
        //先创建CoursePub对象
        CoursePub coursePub=createCoursePub(id);
        //将CoursePub对象保存到数据库
        saveCoursePub(id,coursePub);

        //向teachplanmediapub保存课程媒资信息
        saveTeachplanMediaPub(id);

        //得到页面的url
        String pageUrl1 = cmsPostPageResult.getPageUrl();

        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl1);
    }

//    teachplanmediapub保存课程媒资信息
    private void saveTeachplanMediaPub(String courseId){
        //先删除teachplanmediapub中的数据
        teachPlanMediaPubRepository.deleteByCourseId(courseId);
        //从teachplanmedia中查询数据
        List<TeachplanMedia> list = teachPlanMediaRepository.findByCourseId(courseId);
        //将查询的数据存在teachplanmediapub表中
        List<TeachplanMediaPub> teachplanMediaPublist=new ArrayList<>();
        for (TeachplanMedia teachplanMedia : list) {
            TeachplanMediaPub teachplanMediaPub=new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            //添加时间戳
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPublist.add(teachplanMediaPub);
        }
        teachPlanMediaPubRepository.saveAll(teachplanMediaPublist);
    }


    //将CoursePub保存到数据库
    private CoursePub saveCoursePub(String id,CoursePub coursePub){
        //根据课程id查询CoursePub
        CoursePub coursePub1=null;
        Optional<CoursePub> byId = coursePubRepository.findById(id);
        if(byId.isPresent()){
            coursePub1=byId.get();
        }else{
            coursePub1=new CoursePub();
        }
        //将这个传入的信息保存到coursePub1
        BeanUtils.copyProperties(coursePub,coursePub1);
        coursePub1.setId(id);
        coursePub1.setCharge("203002");
        coursePub1.setValid("204001");

        //时间戳 时间戳logstash使用
        coursePub1.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        coursePub1.setPubTime(simpleDateFormat.format(new Date()));
        coursePubRepository.save(coursePub1);
        return coursePub1;
    }




    private CoursePub createCoursePub(String id){
        CoursePub coursePub=new CoursePub();
        //根据课程id查询course_base
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            //将CourseBase属性拷贝到CoursePub中
            BeanUtils.copyProperties(courseBase,coursePub);

        }
        //根据课程id查询course_pic
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(id);
        if(optionalCoursePic.isPresent()){
            CoursePic coursePic = optionalCoursePic.get();
            //将coursePic属性拷贝到CoursePub中
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        //课程计划信息
        TeachplanNode teachplanNode=teachplanMapper.selectList(id);
        String string = JSON.toJSONString(teachplanNode);
        //将课程计划信息json串保存到course_pub中
        coursePub.setTeachplan(string);

        return coursePub;
    }

    //更改课程状态为已发布 202002
    private CourseBase updateStatus(String courseId){
        CourseBase courseBase = this.CoursefindbyId(courseId);
        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);
        return courseBase;
    }

    //保存课程计划与媒资文件的关联信息
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia) {
        if(teachplanMedia==null||StringUtils.isEmpty(teachplanMedia.getTeachplanId())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //校验课程计划是否为三级
        String teachplanId = teachplanMedia.getTeachplanId();
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //查询教学计划
        Teachplan teachplan = optional.get();
        String grade = teachplan.getGrade();
        if(grade==null||!grade.equals("3"))
        {
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_TEACHPLAN_GRADEERROR);
        }
        //
        Optional<TeachplanMedia> mediaOptional = teachPlanMediaRepository.findById(teachplanId);
        TeachplanMedia teachplanMedia1=null;
        if(mediaOptional.isPresent()){
             teachplanMedia1 = mediaOptional.get();
        }else{
            teachplanMedia1=new TeachplanMedia();
        }

        //将TeachplanMedia保存到数据库
        teachplanMedia1.setCourseId(teachplan.getCourseid());//课程id
        teachplanMedia1.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());//原始文件的名称
        teachplanMedia1.setMediaId(teachplanMedia.getMediaId());//媒资文件id
        teachplanMedia1.setMediaUrl(teachplanMedia.getMediaUrl());//媒资文件的url
        teachplanMedia1.setTeachplanId(teachplanId);
        teachPlanMediaRepository.save(teachplanMedia1);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
