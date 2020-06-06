package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author study
 * @create 2020-04-09 17:34
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    @Transactional
    long deleteByCourseid(String courseid);
}
