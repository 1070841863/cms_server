package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.feign.CmsPageClient;
import com.xuecheng.manage_course.service.CoursePicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {

    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }
    @Test
    public void testFindTeachplan(){
        TeachplanNode teachplanNode = teachplanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanNode);
    }

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Test
    public void testAdd(){
        Teachplan teachplan=new Teachplan();
        teachplan.setGrade("2");
        teachplan.setCourseid("4028e581617f945f01617f9dabc40000");
        teachplan.setParentid("2");
        teachplan.setPname("测试-1");
        teachplan.setTimelength(10d);
        teachplan.setPtype("1");
        teachplan.setStatus("0");
        teachplanRepository.save(teachplan);
    }

//    @Test
//    public void testPageHelp(){
//        Integer page=1;
//        Integer size=10;
//        //查询第一页的，每页十条数据
//        PageHelper.startPage(page,size);
//        Page<CourseBase> allCourseList = courseMapper.findAllCourseList();
//        System.out.println(allCourseList.getTotal());
//        System.out.println(allCourseList.getResult());
//    }

//    @Autowired
//    private SystemDicRepository systemDicRepository;

//    @Test
//    public void xxx(){
//        List<SysDictionary> byDtype = systemDicRepository.findByDType("200");
//        System.out.println(byDtype);
//    }



}
