package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author study
 * @create 2020-04-06 15:07
 */
public class Customer04_topic_email {

    //两个队列
    public static final String Queue_email="que_email_topic";
    public static final String EXCHANGE_topic_INFORM="exchange_topic_inform";
    public static final String RouteKey_EAMIL="inform.#.email.#"; //


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

            //消费
            //实现消费方法
            DefaultConsumer defaultConsumer=new DefaultConsumer(channel){
                //当接受到消息后，此方法被调用
                /**
                 * 参数解析
                 * @param consumerTag 消费者标签，用来标识消费者，在监听队列的时候设置channel.basicConsume
                 * @param envelope  信封 通过envelope
                 * @param properties 消息属性
                 * @param body 消息内容
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //交换机
                    String exchange = envelope.getExchange();
                    //消息id:mq在channel中用来标识消费的id
                    long deliveryTag = envelope.getDeliveryTag();
                    String res=new String(body,"utf-8");
                    System.out.println("routing:phone接收到消息:"+res);
                }
            };

            //监听队列
            //String queue, boolean autoAck, Consumer callback
            /**
             * 参数明细:
             * 1. queue 队列名称
             * 2.autoAck 自动回复 当消费者接收到消息要告诉mq消息已接收，如果将此参数设置为true，表示会自动回复mq
             * 如果设置为false，需要通过编程实现回复
             * callback 消费方法 当消费者接受到消息要执行方法
             */
            channel.basicConsume(Queue_email,true,defaultConsumer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }


    }

}
