package com.shengda.industrialaiplatform.service;

import com.shengda.industrialaiplatform.entity.OperateReport;

import java.util.List;

/**
 * 报告操作功能接口
 */
public interface OperateReportService {
    //生成设备报告
    public List<OperateReport> generateReport(String deviceId, String deviceType, String logContent);

    //查询设备的报告
    public List<OperateReport> getReportsByDeviceId(String deviceId);
}
