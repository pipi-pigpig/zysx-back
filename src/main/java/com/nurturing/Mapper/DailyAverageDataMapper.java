package com.nurturing.Mapper;

import com.nurturing.entity.DailyAverageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 上午11:41
 */
@Mapper
public interface DailyAverageDataMapper {
    /**
     * 查询用户指定周范围内的血糖日平均数据
     */
    List<DailyAverageData> selectBloodSugarByWeek(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
