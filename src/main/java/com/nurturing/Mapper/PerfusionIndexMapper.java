package com.nurturing.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.PerfusionIndex;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PerfusionIndexMapper extends BaseMapper<PerfusionIndex> {
    @Select("select  * from perfusionindexdata where user_id=#{userId}  order by created_at")
    List<PerfusionIndex> getById(Long userId);

    @Insert("insert into perfusion_index_data(user_id, pi_data, record_time) select user_id ,#{piData},#{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("piData") BigDecimal piData, @Param("mac") String mac, @Param("recordTime")LocalDateTime recordTime);

}
