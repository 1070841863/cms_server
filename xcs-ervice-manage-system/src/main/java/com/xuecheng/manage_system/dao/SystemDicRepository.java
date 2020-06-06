package com.xuecheng.manage_system.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author study
 * @create 2020-04-08 15:11
 */
public interface SystemDicRepository extends MongoRepository<SysDictionary,String> {

    public List<SysDictionary> findByDType(String dtype);
}
