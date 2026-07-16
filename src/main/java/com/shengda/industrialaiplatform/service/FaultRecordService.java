package com.shengda.industrialaiplatform.service;

import com.shengda.industrialaiplatform.entity.FaultRecord;

import java.util.List;

/**
 * 故障接口
 */
public interface FaultRecordService {
    //故障诊断业务
    public String diagnoseFault(String deviceId, String deviceType, String faultPhenomenon);

    //查询设备的诊断记录
    public List<FaultRecord> getFaultsByDeviceId(String deviceId);
}
