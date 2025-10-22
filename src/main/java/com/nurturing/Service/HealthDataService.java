package com.nurturing.Service;

import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午10:31
 */
public interface HealthDataService<T extends HealthData> {
    List<T> getById(Long userId);
    List<T> getRecentData(Long userId);
    void saveHealthData(T entity);
}
