package com.nurturing.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("equipment")
public class Equipment {

    @TableId(type = IdType.AUTO)
    private long id;

    private String mac;

    private String userId;
}
