package com.shengda.industrialaiplatform.service.impl;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.shengda.industrialaiplatform.dao.IndustrialDocumentDao;
import com.shengda.industrialaiplatform.entity.IndustrialDocument;
import com.shengda.industrialaiplatform.service.IndustrialDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文档工能业务实现类
 */
@Service//标注类是业务层类，同时让框架创建该类的对象
public class IndustrialDocumentServiceImpl implements IndustrialDocumentService {
    //用于进行数据库操作
    @Autowired
    private IndustrialDocumentDao industrialDocumentDao;
    /**
     *@param industrialDocument
     *@return
     */
    @Override
    public IndustrialDocument saveDocument(IndustrialDocument industrialDocument) {
        return industrialDocumentDao.save(industrialDocument);
    }

    @Override
    public List<IndustrialDocument> findAll() {

           List<IndustrialDocument> list = industrialDocumentDao.findAll();
            return list;


    }

    /**
     *
     * @param deviceType
     * @return
     */

    @Override
    public List<IndustrialDocument> findDocumentsByDeviceType(String deviceType) {
        //使用持久层对象，调用设计好的查询方法
        List<IndustrialDocument> list = industrialDocumentDao.findDocumentsByDeviceType(deviceType);
        return list;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public IndustrialDocument findDocumentById(String id) {
        IndustrialDocument document =  industrialDocumentDao.findById(id).get();
        return document;
    }

    /**
     *
     * @param id
     */
    @Override
    public void deleteDocumentById(String id) {
        industrialDocumentDao.deleteById(id);
    }
}
