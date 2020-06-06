package com.xuecheng.manager_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author study
 * @create 2020-04-05 12:03
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {

}
