package com.xuecheng.manager_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author study
 * @create 2020-04-13 15:24
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
