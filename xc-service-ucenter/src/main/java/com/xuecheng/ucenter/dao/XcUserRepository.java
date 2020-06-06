package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author study
 * @create 2020-04-25 13:45
 */
public interface XcUserRepository extends JpaRepository<XcUser,String> {
    //根据账号查询
    XcUser findByUsername(String username);
}
