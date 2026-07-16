package com.shengda.industrialaiplatform.service.impl;

import com.shengda.industrialaiplatform.dao.OperateReportDao;
import com.shengda.industrialaiplatform.entity.OperateReport;
import com.shengda.industrialaiplatform.service.OperateReportService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class OperateReportImpl implements OperateReportService {
    @Autowired
    private QwenChatModel qwenChatModel ;
    @Autowired
    private OperateReportDao operateReportDao;
    @Autowired
    private  RagServiceImpl ragService;


    /**
     * 自动生成报告保存到数据库
     */

    @Override
    public List<OperateReport> generateReport(String deviceId, String deviceType, String logContent) {


        /**
         * 自动生成运维报告
         */
        String content = ragService.retrieveRelevantContent(logContent);
        String prompt = """
               你是一个资深的工业运维专家，请基于下面的设备运行日志和工业文档，生成一份标准化的运维分析报告。
                报告需要包含：
                1. 设备整体运行状态评估
                2. 发现的异常问题
                3. 对应的处理建议
                4. 后续的运维注意事项
                格式要专业，符合工业运维报告的规范。

                工业文档参考：
                {{context}}

                设备运行日志：
                {{log}}

                设备类型：{{deviceType}}
                """.replace("{{context}}", content).replace("{{log}}", logContent).replace("{{deviceType}}", deviceType);
        String replyMessage =  qwenChatModel.chat(prompt);
        OperateReport operateReport = new OperateReport();
        operateReport.setDeviceId(deviceId);
        operateReport.setDeviceType(deviceType);
        operateReport.setContent(replyMessage);
        operateReport.setReportType("运维报告");
        operateReport.setCreateTime(LocalDateTime.now());
        operateReportDao.save(operateReport);
        List<OperateReport> list = operateReportDao.findOperateReportByDeviceIdOrderByCreateTimeDesc(deviceId);


        return  list;
    }

    /**
     *
     * @param deviceId
     * @return
     */
    @Override
    public List<OperateReport> getReportsByDeviceId(String deviceId) {
        List <OperateReport> list = operateReportDao.findOperateReportByDeviceIdOrderByCreateTimeDesc(deviceId);

        return list ;
    }
}
