package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author study
 * @create 2020-04-06 14:52
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer_springboot_topics {

    //使用
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    public void testSendEmail(){
        /**
         *参数:
         * 1.交换机
         * 2.routeKey
         * 3.消息内容
         */
//        String msg="send email and phone message to user";
//        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_topic_INFORM,"inform.email",msg);
        String msg1="send email and phone message to user";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_topic_INFORM,"inform.phone.email",msg1);
//        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_topic_INFORM,"inform.email",msg);
    }


}
