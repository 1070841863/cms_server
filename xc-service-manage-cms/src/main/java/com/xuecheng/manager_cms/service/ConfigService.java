package com.xuecheng.manager_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manager_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author study
 * @create 2020-04-04 20:40
 */
@Service
public class ConfigService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    public CmsConfig getConfigById(String id){
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }
}
