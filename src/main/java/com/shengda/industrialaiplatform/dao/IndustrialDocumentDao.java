package com.shengda.industrialaiplatform.dao;

import com.shengda.industrialaiplatform.entity.IndustrialDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


/**
 * 工业文档持久层接口，定义对数据的操作功能
 */
public interface IndustrialDocumentDao extends MongoRepository<IndustrialDocument, String> {
    public List<IndustrialDocument> findDocumentsByDeviceType(String deviceType);

}
