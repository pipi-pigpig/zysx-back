package com.nurturing.properties;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nurturing.alioss")
@Data
public class AliOssProperties {

    @Value("${alioss.endpoint}")
    private String endpoint;
    @Value("${alioss.access-key-id}")
    private String accessKeyId;
    @Value("${alioss.access-key-secret}")
    private String accessKeySecret;
    @Value("${alioss.bucket-name}")
    private String bucketName;

}
