package com.nurturing.Service.impI;

import com.nurturing.DTO.GetHealthReportListResponse;
import com.nurturing.DTO.UserPageCenterDataDTO;
import com.nurturing.Mapper.DailyAverageDataMapper;
import com.nurturing.Mapper.HealthReportMapper;
import com.nurturing.Mapper.MonthlyAverageDataMapper;
import com.nurturing.Mapper.WeeklyAverageDataMapper;
import com.nurturing.Service.*;
import com.nurturing.chat.LLMClient;
import com.nurturing.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class HealthReportServiceImpl implements HealthReportService {

    @Autowired
    private LLMClient llmClient;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private DailyAverageDataMapper dailyAverageDataMapper;

    @Autowired
    private WeeklyAverageDataMapper weeklyAverageDataMapper;

    @Autowired
    private MonthlyAverageDataMapper monthlyAverageDataMapper;

    @Autowired
    private HealthReportMapper healthReportMapper;


    @Override
    public SseEmitter generateHealthReport(Long userId) throws IOException {

        String sessionId = "query:"+UUID.randomUUID().toString();

        String userHealthMSG="健康档案如下：\n";

        userHealthMSG+=userToString(userLoginService.getUserById(userId));

        HashMap<String, List<DailyAverageData>> dailyAverageHashMap= new HashMap<String, List<DailyAverageData>>();
        HashMap<String, List<WeeklyAverageData>> weeklyAverageHashMap= new HashMap<String, List<WeeklyAverageData>>();
        HashMap<String, List<MonthlyAverageData>> monthlyAverageHashMap= new HashMap<String, List<MonthlyAverageData>>();

        Map<String,String> dataType=Map.of("血氧","blood_oxygen","血糖","blood_sugar","血压","blood_pressure","灌注指数","blood_flow","心率","heart_rate","睡眠","sleep");

        dataType.forEach((k,v)->{
            dailyAverageHashMap.put(k,dailyAverageDataMapper.selectByLatestTimes(userId,v,14));
            weeklyAverageHashMap.put(k,weeklyAverageDataMapper.selectByLatestTimes(userId,v,4));
            monthlyAverageHashMap.put(k,monthlyAverageDataMapper.selectByLatestTimes(userId,v,3));
        });

        userHealthMSG+=dailyAverageDataMapToString(dailyAverageHashMap);
        userHealthMSG+=weeklyAverageDataMapToString(weeklyAverageHashMap);
        userHealthMSG+=monthlyAverageDataMapToString(monthlyAverageHashMap);

        List<ChatSentence> healthMessageSentences=new ArrayList<>();
        healthMessageSentences.add(new ChatSentence("user",userHealthMSG));

        return llmClient.streamGenerateHealthReport(sessionId,healthMessageSentences);
    }

    @Override
    public List<GetHealthReportListResponse> getHealthReportList(Long userId) {
        return healthReportMapper.getHealthReportList(userId);
    }

    @Override
    public HealthReport getHealthReport(Long healthReportId) {
        return healthReportMapper.getHealthReport(healthReportId);
    }

    @Override
    public void saveHealthReport(HealthReport healthReport) {
        healthReportMapper.insertHealthReport(healthReport.getUserId(), LocalDateTime.now(), healthReport.getReport());
        return;
    }

    @Override
    public void deleteHealthReport(Long healthReportId) {
        healthReportMapper.deleteHealthReport(healthReportId);
    }


    private String userToString(User user){
        String userMSG = "姓名："+ user.getUsername()+"，性别："+ user.getGender()+"，年龄:"+ ChronoUnit.YEARS.between(user.getBirthDate(),LocalDate.now()) +"岁，身高："+ user.getHeight()+"cm，体重："+ user.getWeight()+"kg。\n";
        userMSG+=("既往病史："+ user.getPastMedicalHistory()+"。\n家族遗传病史："+ user.getFamilyHistory()+"。\n过敏史："+ user.getAllergyHistory()+"。\n手术史："+ user.getSurgicalHistory()+"。\n用药医嘱："+ user.getMedicalCompliance()+"。\n");

        return  userMSG;
    }


    private String dailyAverageDataMapToString(Map<String,List<DailyAverageData>> dailyAverageDataMap) {
        StringBuilder dailyAverageMSG=new StringBuilder("日平均数据：{\n");

        if(dailyAverageDataMap==null||dailyAverageDataMap.isEmpty()){
            return dailyAverageMSG.toString()+"}\n";
        }

        for (Map.Entry<String, List<DailyAverageData>> entry : dailyAverageDataMap.entrySet()) {
            String dataType = entry.getKey();
            List<DailyAverageData> dailyAverageDataList = entry.getValue();

            dailyAverageMSG.append(dataType+"：");

            StringBuilder dailyAverageListMSG=new StringBuilder();
            dailyAverageListMSG.append("[");
            dailyAverageDataList.forEach(dailyAverageData->{
                dailyAverageListMSG.append("{时间："+dailyAverageData.getRecordDate()+"，"+"数值:"+dailyAverageData.getAverageValue()+"}，");
            });
            if(dailyAverageListMSG.length()>1){
                dailyAverageListMSG.deleteCharAt(dailyAverageListMSG.length()-1);
            }
            dailyAverageListMSG.append("]");

            dailyAverageMSG.append(dailyAverageListMSG.toString()+"，\n");
        }

        dailyAverageMSG.deleteCharAt(dailyAverageMSG.length()-2).append("}\n");

        return dailyAverageMSG.toString();
    }


    /**
     * 周平均数据Map转字符串
     * @return 格式化后的周平均数据字符串
     */
    private String weeklyAverageDataMapToString(Map<String,List<WeeklyAverageData>> weeklyAverageDataMap) {
        // 初始化字符串构建器，保持格式与日数据一致
        StringBuilder weeklyAverageMSG = new StringBuilder("周平均数据：{\n");

        if(weeklyAverageDataMap==null||weeklyAverageDataMap.isEmpty()){
            return weeklyAverageMSG.toString()+"}\n";
        }

        // 遍历周平均数据Map
        for (Map.Entry<String, List<WeeklyAverageData>> entry : weeklyAverageDataMap.entrySet()) {
            String dataType = entry.getKey();
            List<WeeklyAverageData> weeklyAverageDataList = entry.getValue();

            weeklyAverageMSG.append(dataType + "：");

            // 拼接周平均数据列表内容
            StringBuilder weeklyAverageListMSG = new StringBuilder();
            weeklyAverageListMSG.append("[");
            weeklyAverageDataList.forEach(weeklyAverageData -> {
                // 替换为周数据的时间字段（recordWeek），数值字段保持一致
                weeklyAverageListMSG.append("{周开始时间：" + weeklyAverageData.getWeekStartDate()+ "，" + "数值:" + weeklyAverageData.getAverageValue() + "}，");
            });
            // 移除列表末尾多余的中文逗号
            if (weeklyAverageListMSG.length() > 1) {
                weeklyAverageListMSG.deleteCharAt(weeklyAverageListMSG.length() - 1);
            }
            weeklyAverageListMSG.append("]");

            // 将列表内容拼接到外层字符串，保留格式符
            weeklyAverageMSG.append(weeklyAverageListMSG.toString() + "，\n");
        }

        // 移除外层末尾多余的中文逗号，拼接闭合大括号
        weeklyAverageMSG.deleteCharAt(weeklyAverageMSG.length() - 2).append("}\n");

        return weeklyAverageMSG.toString();
    }

    /**
     * 月平均数据Map转字符串
     * @return 格式化后的月平均数据字符串
     */
    private String monthlyAverageDataMapToString(Map<String,List<MonthlyAverageData>> monthlyAverageDataMap) {
        // 初始化字符串构建器，保持格式与日/周数据一致
        StringBuilder monthlyAverageMSG = new StringBuilder("月平均数据：{\n");

        if(monthlyAverageDataMap==null||monthlyAverageDataMap.isEmpty()){
            return monthlyAverageMSG.toString()+"}\n";
        }

        // 遍历月平均数据Map
        for (Map.Entry<String, List<MonthlyAverageData>> entry : monthlyAverageDataMap.entrySet()) {
            String dataType = entry.getKey();
            List<MonthlyAverageData> monthlyAverageDataList = entry.getValue();

            monthlyAverageMSG.append(dataType + "：");

            // 拼接月平均数据列表内容
            StringBuilder monthlyAverageListMSG = new StringBuilder();
            monthlyAverageListMSG.append("[");
            monthlyAverageDataList.forEach(monthlyAverageData -> {
                // 替换为月数据的时间字段（recordMonth），数值字段保持一致
                monthlyAverageListMSG.append("{时间：" + monthlyAverageData.getMonthDate().getDayOfMonth() + "，" + "数值:" + monthlyAverageData.getAverageValue() + "}，");
            });
            // 移除列表末尾多余的中文逗号
            if (monthlyAverageListMSG.length() > 1) {
                monthlyAverageListMSG.deleteCharAt(monthlyAverageListMSG.length() - 1);
            }
            monthlyAverageListMSG.append("]");

            // 将列表内容拼接到外层字符串，保留格式符
            monthlyAverageMSG.append(monthlyAverageListMSG.toString() + "，\n");
        }

        // 移除外层末尾多余的中文逗号，拼接闭合大括号
        monthlyAverageMSG.deleteCharAt(monthlyAverageMSG.length() - 2).append("}\n");

        return monthlyAverageMSG.toString();
    }
}
