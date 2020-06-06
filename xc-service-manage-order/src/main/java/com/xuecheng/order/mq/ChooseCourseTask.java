package com.xuecheng.order.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author study
 * @create 2020-05-05 21:50
 */
@Component
public class ChooseCourseTask {

    private static final  Logger logger= LoggerFactory.getLogger(ChooseCourseTask.class);

    //定义任务调度的策略
//    @Scheduled(cron = "0/3 * * * * *")//每隔三秒
    @Scheduled(fixedRate = 3000)//在任务开始后三秒执行下一次调度 ，如果三秒内没有执行完成，就等执行完成之后再进行下一次调度
//    @Scheduled(fixedDelay = 3000)//在任务结束后三秒执行下一次调度
    public void task1(){
        logger.info("测试定时任务1开始======");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        logger.info("测试定时任务1结束======");
    }

    //定义任务调度的策略
//    @Scheduled(cron = "0/3 * * * * *")//每隔三秒
    @Scheduled(fixedRate = 3000)//在任务开始后三秒执行下一次调度 ，如果三秒内没有执行完成，就等执行完成之后再进行下一次调度
//    @Scheduled(fixedDelay = 3000)//在任务结束后三秒执行下一次调度
    public void task2(){
        logger.info("测试定时任务2开始======");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        logger.info("测试定时任务2结束======");
    }
}
