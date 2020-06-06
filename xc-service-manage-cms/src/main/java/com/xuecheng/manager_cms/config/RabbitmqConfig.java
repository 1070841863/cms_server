package com.xuecheng.manager_cms.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author study
 * @create 2020-04-06 19:49
 */
@Component
public class RabbitmqConfig {


    //交换机名称
    public static final String EXCHANGE_NAME="ex_cms_postpage";

    //声明交换机 使用direct
    @Bean(EXCHANGE_NAME)
    public Exchange EXCHANGE_NAME(){
        Exchange build = ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
        return build;
    }

}
