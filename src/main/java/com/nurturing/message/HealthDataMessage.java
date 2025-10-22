package com.nurturing.message;

import com.nurturing.Service.HealthData;

import java.util.Date;
import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午10:33
 */
public class HealthDataMessage<T extends HealthData> {
    private String dataType;
    private List<T> data;
    private Date timestamp;

    public HealthDataMessage(String dataType, List<T> data) {
        this.dataType = dataType;
        this.data = data;
        this.timestamp = new Date();
    }

    // getter 和 setter
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}