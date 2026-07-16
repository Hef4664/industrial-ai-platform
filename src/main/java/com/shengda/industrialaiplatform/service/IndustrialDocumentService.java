package com.shengda.industrialaiplatform.service;

import com.shengda.industrialaiplatform.dao.IndustrialDocumentDao;
import com.shengda.industrialaiplatform.entity.IndustrialDocument;

import java.util.List;

/**
 * 文档处理功能业务接口
 */
public interface IndustrialDocumentService {

    public IndustrialDocument saveDocument(IndustrialDocument industrialDocument);

           //查询所有文档
    public  List<IndustrialDocument>  findAll();
    public  List<IndustrialDocument>  findDocumentsByDeviceType(String deviceType);

    //根据ID查询文档的业务方法
    public  IndustrialDocument findDocumentById(String id);
    public  void deleteDocumentById(String id);
}
