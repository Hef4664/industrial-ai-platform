package com.shengda.industrialaiplatform.dao;

import com.shengda.industrialaiplatform.entity.OperateReport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 报告持久层接口，设计管理数据库中报告数据的相关方法
 */
public interface OperateReportDao extends MongoRepository<OperateReport, String> {
    // 根据设备ID查询所有的报告
    public  List<OperateReport> findOperateReportByDeviceIdOrderByCreateTimeDesc(String deviceId);


}
