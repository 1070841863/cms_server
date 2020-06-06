package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author study
 * @create 2020-04-06 14:52
 */
public class Producer_topics {

    //两个队列
    public static final String Queue_email="que_email_topic";
    public static final String Queue_phone="que_phone_topic";
    public static final String EXCHANGE_topic_INFORM="exchange_topic_inform";
    public static final String RouteKey_EAMIL="inform.#.email.#"; //
    public static final String RouteKey_Phone="inform.#.phone.#";


    public static void main(String[] args) throws IOException, TimeoutException {
        //通过连接工厂来创建连接和mq建立连接
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("192.168.122.102");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机，一个mq的服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        Connection connection =null;
        Channel channel=null;
        try {
            //和mq建立连接
            connection=connectionFactory.newConnection();
            //创建会话通道 每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();
            //声明队列,如果队列在mq中没有则要创建
            //参数:String queue, boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments
            /**
             * 参数明细：
             *  1.queue 队列名称
             *  2.durable 是否持久化，如果持久化，mq重启后队列还在
             *  3.exclusive 是否独占连接，队列只允许在该连接中访问，如果连接关闭后，就自动删除了。
             *  如果将此参数设置为true，可用于临时队列的创建
             *  4.autoDelete 队列不再使用时是否自动删除此队列，
             *  如果将此参数和exclusive都设置为true，就可以实现临时队列（队列不用了就自动删除了）
             *  5.arguments 参数，可以设置一个队列的扩展参数
             */
            channel.queueDeclare(Queue_email,true,false,false,null);
            channel.queueDeclare(Queue_phone,true,false,false,null);
            //声明交换机
            //String exchange, String type
            /**
             * 参数明细：
             * 1.exchange 交换机名称
             * 2.type 交换机类型
             *     FANOUT:对应rabbitmq的工作模式: Publish/Subscribe
             *     DIRECT:对应Routing工作模式
             *     TOPIC:对应topics工作模式
             *     HEADERS:对应headers工作模式
             */
            channel.exchangeDeclare(EXCHANGE_topic_INFORM, BuiltinExchangeType.TOPIC);
            //进行交换机与队列绑定
            //String queue, String exchange, String routingKey
            /**
             * 参数明细:
             * 1.queue 队列名称
             * 2.exchange 交换机名称
             * 3.routingKey 路由key，在发布订阅模式中设置为空串。
             */
            channel.queueBind(Queue_email,EXCHANGE_topic_INFORM,RouteKey_EAMIL);
            channel.queueBind(Queue_phone,EXCHANGE_topic_INFORM,RouteKey_Phone);


            for (int i = 0; i < 5; i++) {
                String message="send phone info message to user";
                //发送消息
                //String exchange, String routingKey, BasicProperties props, byte[] body
                /**
                 * 参数明细:
                 * 1.exchange 交换机 不指定将使用mq的默认交换机
                 * 2.routingKey 路由key，交换机根据路由key来将消息转发到指定的消息队列，
                 * 如果使用默认交换机，routingKey设置为队列名称
                 * 3.props 消息的属性
                 * 4.body 消息消息体
                 */
                channel.basicPublish(EXCHANGE_topic_INFORM,"inform.phone",null,message.getBytes());
                System.out.println("send message:"+message+"...");
            }

            for (int i = 0; i < 5; i++) {
                String message="send email info message to user";
                //发送消息
                //String exchange, String routingKey, BasicProperties props, byte[] body
                /**
                 * 参数明细:
                 * 1.exchange 交换机 不指定将使用mq的默认交换机
                 * 2.routingKey 路由key，交换机根据路由key来将消息转发到指定的消息队列，
                 * 如果使用默认交换机，routingKey设置为队列名称
                 * 3.props 消息的属性
                 * 4.body 消息消息体
                 */
                channel.basicPublish(EXCHANGE_topic_INFORM,"inform.email",null,message.getBytes());
                System.out.println("send message:"+message+"...");
            }


            for (int i = 0; i < 5; i++) {
                String message="send email and phone info message to user";
                //发送消息
                //String exchange, String routingKey, BasicProperties props, byte[] body
                /**
                 * 参数明细:
                 * 1.exchange 交换机 不指定将使用mq的默认交换机
                 * 2.routingKey 路由key，交换机根据路由key来将消息转发到指定的消息队列，
                 * 如果使用默认交换机，routingKey设置为队列名称
                 * 3.props 消息的属性
                 * 4.body 消息消息体
                 */
                channel.basicPublish(EXCHANGE_topic_INFORM,"inform.phone.email",null,message.getBytes());
                System.out.println("send message:"+message+"...");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(channel!=null){
                channel.close();
            }
            if(connection!=null){
                connection.close();
            }
        }


    }

}
