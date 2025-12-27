package com.nurturing.Mapper;

import com.nurturing.DTO.GetHealthReportListResponse;
import com.nurturing.entity.HealthReport;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthReportMapper {

    @Insert("insert into health_report (user_id,create_time,report) values (#{userId},#{createTime},#{report})")
    void insertHealthReport(@Param("userId") Long userId, @Param("createTime") LocalDateTime createTime,@Param("report") String report);

    @Select("select id,create_time as createTime from health_report where user_id=#{userId}")
    List<GetHealthReportListResponse> getHealthReportList(@Param("userId") Long userId);

    @Select("select * from health_report where id=#{healthReportId}")
    HealthReport getHealthReport(@Param("healthReportId") Long healthReportId);

    @Delete("delete from health_report where id= #{healthReportId}")
    void deleteHealthReport(@Param("healthReportId") Long healthReportId);
}
