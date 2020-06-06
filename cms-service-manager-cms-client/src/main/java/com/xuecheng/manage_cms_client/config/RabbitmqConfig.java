package com.xuecheng.manage_cms_client.config;

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

//    1、创建“ex_cms_postpage”交换机
//    2、每个Cms Client创建一个队列与交换机绑定
//    3、每个Cms Client程序配置队列名称和routingKey，将站点ID作为routingKey

    //交换机名称
    public static final String EXCHANGE_NAME="ex_cms_postpage";
    //队列bean的名称
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";

    @Value("${xuecheng.mq.queue}")
    private String cms_queue_postpage_name;

    @Value("${xuecheng.mq.routingKey}")
    private String routingKey;


    //声明交换机 使用direct
    @Bean(EXCHANGE_NAME)
    public Exchange EXCHANGE_NAME(){
        Exchange build = ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
        return build;
    }

    //声明队列
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue CMS_QUEUE_POSTPAGE_NAME(){
        return new Queue(cms_queue_postpage_name);
    }

    /**
     * 绑定队列到交换机
     */
    @Bean
    public Binding BINDING_QUEUE_SMS(@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue,
                                     @Qualifier(EXCHANGE_NAME) Exchange exchange
                                     )
    {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

}
