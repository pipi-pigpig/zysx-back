package com.nurturing.Mapper;

import com.nurturing.entity.DailyAverageData;
import com.nurturing.entity.MonthlyAverageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 下午5:39
 */
@Mapper
public interface MonthlyAverageDataMapper {
    List<MonthlyAverageData> selectByMonthRange(
            @Param("userId") Long userId,
            @Param("startMonth") LocalDate startMonth,
            @Param("endMonth") LocalDate endMonth,
            @Param("dataType") String dataType
    );

    List<MonthlyAverageData> selectByYear(
            @Param("userId") Long userId,
            @Param("year") Integer year,
            @Param("dataType") String dataType
    );

    /**
     * 查询距今为止最近几次的、指定类型的周平均数据
     */
    List<MonthlyAverageData> selectByLatestTimes(
            @Param("userId") Long userId,
            @Param("dataType") String dataType,
            @Param("times") int times
    );
}