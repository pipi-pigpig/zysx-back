package com.nurturing.Event;

import com.nurturing.entity.PerfusionIndex;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午11:02
 */
public class PerfusionIndexDataEvent extends HealthDataEvent<PerfusionIndex> {
    public PerfusionIndexDataEvent(Object source, PerfusionIndex perfusionIndex) {
        super(source, perfusionIndex, "perfusionIndex");
    }
}