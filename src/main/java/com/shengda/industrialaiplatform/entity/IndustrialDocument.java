package com.shengda.industrialaiplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data@AllArgsConstructor@NoArgsConstructor@Document(collection = "IndustrialDocument")
/**
 * 工业文档实体类，用于暂时存储工业文档信息
 */
public class IndustrialDocument {
    @Id
    //文档编号
    private String id;
    //文档名称
    private String fileName;
    //文档内容
    private String content;
    //文档文件类型
    private String fileType;
    //文档的设备内容类型
    private String deviceType;
    //文档上传时间
   private LocalDateTime uploadTime;
}