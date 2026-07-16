package com.shengda.industrialaiplatform.controller;

import com.shengda.industrialaiplatform.entity.OperateReport;
import com.shengda.industrialaiplatform.service.OperateReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 报告请求处理的类
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class OperateController {
    @Autowired
    private OperateReportService operateReportService;

    /**
     * 生成运维报告接口
     */
    @PostMapping("generateReport")
    public List<OperateReport> generateReport(@RequestBody Map<String, String> params) {
        String deviceId = params.get("deviceId");
        String deviceType = params.get("deviceType");
        String logContent = params.get("logContent");

        return operateReportService.generateReport(deviceId, deviceType, logContent);
    }



    /**
     * 获取设备的报告
     */
    @GetMapping("reports/{deviceId}")
    public List<OperateReport> getReports(@PathVariable String deviceId) {
        return operateReportService.getReportsByDeviceId(deviceId);
    }
}
