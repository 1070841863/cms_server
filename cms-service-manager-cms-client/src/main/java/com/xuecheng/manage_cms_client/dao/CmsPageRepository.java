package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author study
 * @create 2020-04-06 20:03
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
}
