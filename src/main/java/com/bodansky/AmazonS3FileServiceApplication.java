package com.bodansky;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.context.config.annotation.EnableContextResourceLoader;
import org.springframework.cloud.aws.context.support.io.ResourceLoaderBeanPostProcessor;
import org.springframework.context.annotation.Bean;

@EnableContextResourceLoader
@SpringBootApplication
public class AmazonS3FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmazonS3FileServiceApplication.class, args);
    }

    @Bean
    public static AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.defaultClient();
    }

    @Bean
    public static ResourceLoaderBeanPostProcessor resourceLoaderBeanPostProcessor() {
        return new ResourceLoaderBeanPostProcessor(amazonS3());
    }
}
