package com.xuecheng.manager_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author study
 * @create 2020-04-04 20:39
 */
public interface CmsConfigRepository  extends MongoRepository<CmsConfig,String> {

}
