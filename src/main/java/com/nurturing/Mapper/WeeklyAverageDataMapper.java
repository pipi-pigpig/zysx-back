package com.nurturing.Mapper;

import com.nurturing.entity.WeeklyAverageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 下午5:38
 */
@Mapper
public interface WeeklyAverageDataMapper {
    List<WeeklyAverageData> selectBloodSugarByWeekRange(
            @Param("userId") Long userId,
            @Param("startWeek") LocalDate startWeek,
            @Param("endWeek") LocalDate endWeek
    );
}