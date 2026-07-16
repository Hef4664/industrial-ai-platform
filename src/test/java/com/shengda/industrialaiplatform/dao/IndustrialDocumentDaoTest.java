package com.shengda.industrialaiplatform.dao;

import com.shengda.industrialaiplatform.entity.IndustrialDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class IndustrialDocumentDaoTest {

    @Autowired
    private IndustrialDocumentDao industrialDocumentDao;

    @Test
    public void testMongoDb(){
        //创建一个文档对象
        IndustrialDocument doc = new IndustrialDocument();
        doc.setFileName("电机手册");
        doc.setFileType("pdf");
        doc.setDeviceType("电机");
        doc.setContent("电机使用保养，电机维护，电机维修");
        doc.setUploadTime(LocalDateTime.now());

        industrialDocumentDao.save(doc);
    }

}