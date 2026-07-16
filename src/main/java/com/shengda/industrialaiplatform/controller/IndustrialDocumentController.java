package com.shengda.industrialaiplatform.controller;

import com.shengda.industrialaiplatform.entity.IndustrialDocument;
import com.shengda.industrialaiplatform.service.IndustrialDocumentService;
import com.shengda.industrialaiplatform.service.RagService;
import com.shengda.industrialaiplatform.service.impl.RagServiceImpl;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.rag.content.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.AbstractDocument;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController//该类为控制层类，且让框架创建该类的对象，类中的方法返回值全部以json形式输出页面
@CrossOrigin//解决跨域问题
@RequestMapping("/api") //http://localhost:8080/api/xxx
public class IndustrialDocumentController {
    /**
     *
     * @param file
     * @param document
     * @return
     */
    //接受一个文档业务类对象
    @Autowired
    private RagServiceImpl ragServiceimpl;
    @Autowired
    private IndustrialDocumentService idsService;
    @PostMapping("document") //http://localhost:8080/api/document，需要post方式提交请求
    public IndustrialDocument saveDocument(MultipartFile file,IndustrialDocument document) throws IOException {
        /*******从接受的file中获取文档信息，放入document*****/
        //获取文件名
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        //解析文档内容
        ApacheTikaDocumentParser docParser = new ApacheTikaDocumentParser();
        //对file中内容进行解析，形成文档对象
        Document doc = docParser.parse(file.getInputStream());
        /**把所有信息封装到IndustrialDocument document 对象中***/
        String content = doc.text();
        document.setContent(content);
        document.setFileName(filename);
        document.setFileType(contentType);
        document.setUploadTime(LocalDateTime.now());

        /***调用业务类中保存文档的方法**/
        IndustrialDocument industrialDocument = idsService.saveDocument(document);
        ragServiceimpl.initKnowledegeBase();

        return document;
    }

    /**
     * @return
     */

     @GetMapping("document")//http://localhost:8080/api/document,需要get提交请求
    public List<IndustrialDocument> showAllDocuments(){
        List<IndustrialDocument> list = idsService.findAll();
         return list;
    }
    @GetMapping("device/{type}")//http://localhost:8080/api/device/值
    public List<IndustrialDocument> showDocumentByDeviceType(@PathVariable("type") String deviceType){
         List<IndustrialDocument> list = idsService.findDocumentsByDeviceType(deviceType);
         return list;
    }
    @GetMapping("id/{id}")//http://localhost:8080/api/id/值
    public IndustrialDocument showDocumentById(@PathVariable("id")String id){
           IndustrialDocument document = idsService.findDocumentById(id);
           return document;
    }
    @DeleteMapping("id/{id}")//http://localhost:8080/api/id/值,必须用delete请求
    public void deleteDocumentById(@PathVariable("id") String id){
         idsService.deleteDocumentById(id);
        ragServiceimpl.initKnowledegeBase();
    }
}
