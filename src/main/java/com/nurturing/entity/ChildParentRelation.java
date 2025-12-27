package com.nurturing.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChildParentRelation {
    private Long id;
    private Long childId;
    private Long parentId;
    private String relationship;
    private Integer isPrimary; // 0=否, 1=是
    private LocalDateTime bindTime;
    private LocalDateTime createdAt;
}