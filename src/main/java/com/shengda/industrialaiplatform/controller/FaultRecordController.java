package com.shengda.industrialaiplatform.controller;


import com.shengda.industrialaiplatform.entity.FaultRecord;
import com.shengda.industrialaiplatform.service.FaultRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 接收诊断请求
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class FaultRecordController {

    @Autowired
    private FaultRecordService faultRecordService;

    /**
     * 故障诊断接口
     */
    @PostMapping("diagnose")
    public String diagnoseFault(@RequestBody Map<String, String> params) {
        String deviceId = params.get("deviceId");
        String deviceType = params.get("deviceType");
        String faultPhenomenon = params.get("faultPhenomenon");
        return faultRecordService.diagnoseFault(deviceId, deviceType, faultPhenomenon);
    }

    /**
     * 获取设备的故障记录
     */
    @GetMapping("faults/{deviceId}")
    public List<FaultRecord> getFaults(@PathVariable String deviceId) {
        return faultRecordService.getFaultsByDeviceId(deviceId);
    }
}
