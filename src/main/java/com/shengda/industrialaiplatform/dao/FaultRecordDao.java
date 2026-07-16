package com.shengda.industrialaiplatform.dao;

import com.shengda.industrialaiplatform.entity.FaultRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 故障数据的持久层，用于操作数据库中故障数据
 */
public interface FaultRecordDao extends MongoRepository<FaultRecord, String> {

    public List<FaultRecord> findFaultRecordByDeviceIdOrderByCreateTimeDesc(String deviceId);
}
