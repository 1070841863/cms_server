package com.xuecheng.manage_system.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_system.dao.SystemDicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author study
 * @create 2020-04-08 15:11
 */
@Service
public class SysDicService {

    @Autowired
    private SystemDicRepository systemDicRepository;


    public SysDictionary getInfoByType(String type) {
        return systemDicRepository.findByDType(type).get(0);
    }

}
