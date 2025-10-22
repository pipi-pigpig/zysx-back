package com.nurturing.Service;

import java.util.Date;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午10:27
 */
public interface HealthData {
    Long getUserId();
    Date getRecordTime();
    String getDataType(); // 返回数据类型标识
}
