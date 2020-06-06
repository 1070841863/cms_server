package com.xuecheng.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author study
 * @create 2020-04-06 16:49
 */
//定义RabbitConfig类，配置Exchange、Queue、及绑定交换机。
@Configuration
public class RabbitmqConfig {

    //例如使用topic模式
    public static final String Queue_email="que_email_topic";
    public static final String Queue_phone="que_phone_topic";
    public static final String EXCHANGE_topic_INFORM="exchange_topic_inform";
    public static final String RouteKey_EAMIL="inform.#.email.#"; //
    public static final String RouteKey_Phone="inform.#.phone.#";

    //声明交换机
    @Bean(EXCHANGE_topic_INFORM)
    public Exchange EXCHANGE_topic_INFORM(){
        //durable(true)代表持久化，mq重启之后交换机还在
        Exchange build = ExchangeBuilder.topicExchange(EXCHANGE_topic_INFORM).durable(true).build();
        return build;
    }
    //声明队列

    //声明email队列
    @Bean(Queue_email)
    public Queue Queue_email(){
        return new Queue(Queue_email);
    }

    //声明phone队列
    @Bean(Queue_phone)
    public Queue Queue_phone(){
        return new Queue(Queue_phone);
    }


    //绑定交换机和email队列，指定对应的routekey
    @Bean
    public Binding BINDING_QUEUE_INFORM_EAMIL(@Qualifier(Queue_email) Queue queue,
                                              @Qualifier(EXCHANGE_topic_INFORM) Exchange exchange){
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(RouteKey_EAMIL).noargs();
        return binding;
    }

    //绑定交换机和phone队列,指定对应的routekey
    @Bean
    public Binding BINDING_QUEUE_INFORM_PHONE(@Qualifier(Queue_phone) Queue queue,
                                              @Qualifier(EXCHANGE_topic_INFORM) Exchange exchange){
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(RouteKey_Phone).noargs();
        return binding;
    }


}
