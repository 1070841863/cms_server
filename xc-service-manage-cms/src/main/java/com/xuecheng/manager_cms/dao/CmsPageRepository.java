package com.xuecheng.manager_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author study
 * @create 2020-03-31 21:48
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    //根据页面名称，站点id，页面webpath查询
    public CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
