package com.shengda.industrialaiplatform.service.impl;

import com.shengda.industrialaiplatform.dao.FaultRecordDao;
import com.shengda.industrialaiplatform.dao.OperateReportDao;
import com.shengda.industrialaiplatform.entity.FaultRecord;
import com.shengda.industrialaiplatform.entity.OperateReport;
import com.shengda.industrialaiplatform.service.FaultRecordService;
import com.shengda.industrialaiplatform.service.RagService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class FaultRecordServiceImpl implements FaultRecordService {

    //故障诊断的持久层对象
    @Autowired
    private FaultRecordDao faultRecordDao;

    //大模型对象
    @Autowired
    @Qualifier("qwen2")
    private QwenChatModel qwenChatModel;

    //rag检索业务对象
    @Autowired
    private RagService ragService;
    @Autowired
    private OperateReportDao operateReportDao;


    /**
     * 故障诊断业务
     * @param deviceId
     * @param deviceType
     * @param faultPhenomenon
     * @return
     */

    @Override
    public String diagnoseFault(String deviceId, String deviceType, String faultPhenomenon) {

        /**查资料，拼接形成问答内容**/
        //检索知识库
        String content = ragService.retrieveRelevantContent(faultPhenomenon);
        String frContent = "";

        try {

            FaultRecord faultRecord = faultRecordDao.findFaultRecordByDeviceIdOrderByCreateTimeDesc(deviceId).get(0);
            frContent = faultRecord.getFaultPhenomenon();

        }catch (Exception e){


        }

        String rpContent="";
        try{

            OperateReport operateReport = operateReportDao.findOperateReportByDeviceIdOrderByCreateTimeDesc(deviceId).get(0);

        }catch(Exception e){


        }
        //生成传输给大模型的完整内容
        String prompt = """
            你是一个资深的工业故障诊断专家，请基于下面的工业设备文档、历史运维记录、历史故障分析，联网查询分析这个故障的原因，给出处理建议。
            分析要专业，步骤清晰，符合运维的操作规范。
            工业故障处理文档：
            {{context}}
            历史运维文档：
            {{operate}}
            历史故障文档：
            {{faultRecord}}
            设备类型：{{deviceType}}
            故障现象：{{phenomenon}}
            """.replace("{{context}}", content).replace("{{deviceType}}", deviceType).replace("{{phenomenon}}", faultPhenomenon).replace("{{operate}}",rpContent).replace("{{faultRecord}}",frContent);
        String replyMessage = qwenChatModel.chat(prompt);

        FaultRecord faultRecord = new FaultRecord();
        faultRecord.setDeviceId(deviceId);
        faultRecord.setDeviceType(deviceType);
        faultRecord.setFaultPhenomenon(faultPhenomenon);
        faultRecord.setDiagnoseResult(replyMessage);
        faultRecord.setCreateTime(LocalDateTime.now());

        faultRecordDao.save(faultRecord);

        return replyMessage;
    }

    /**
     * 查询设备的诊断记录
     * @param deviceId
     * @return
     */
    @Override
    public List<FaultRecord> getFaultsByDeviceId(String deviceId) {
        List<FaultRecord> list = faultRecordDao.findFaultRecordByDeviceIdOrderByCreateTimeDesc(deviceId);
        return list;
    }
}
