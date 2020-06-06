package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author study
 * @create 2020-04-09 15:16
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
