package com.nurturing.Mapper;

import com.nurturing.entity.DailyAverageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 *
 *
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 上午11:41
 */
@Mapper
public interface DailyAverageDataMapper {
    /**
     * 查询用户指定周范围内、指定数据类型的日平均数据
     */
    List<DailyAverageData> selectByWeek(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("dataType") String dataType
    );

    /**
     * 查询距今为止最近几次的、指定类型的日平均数据
     */
    List<DailyAverageData> selectByLatestTimes(
            @Param("userId") Long userId,
            @Param("dataType") String dataType,
            @Param("times") int times
    );
}
