package com.nurturing.Mapper;

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
    List<MonthlyAverageData> selectBloodSugarByMonthRange(
            @Param("userId") Long userId,
            @Param("startMonth") LocalDate startMonth,
            @Param("endMonth") LocalDate endMonth
    );

    List<MonthlyAverageData> selectBloodSugarByYear(
            @Param("userId") Long userId,
            @Param("year") Integer year
    );
}