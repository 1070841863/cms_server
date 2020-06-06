package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author study
 * @create 2020-04-06 17:24
 */
@Component
public class ReceiveMsg {

    @RabbitListener(queues = RabbitmqConfig.Queue_email)
    public void get_emailmsg(String msg, Message message, Channel channel){
        System.out.println("收到消息email:"+msg);
    }

    @RabbitListener(queues = RabbitmqConfig.Queue_phone)
    public void get_phonemsg(String msg, Message message, Channel channel){
        System.out.println("收到消息phone:"+msg);
    }
}
