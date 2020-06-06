package com.xuecheng.test.freemark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author study
 * @create 2020-04-04 14:43
 */
@SpringBootApplication
public class FreemarkTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreemarkTestApplication.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
