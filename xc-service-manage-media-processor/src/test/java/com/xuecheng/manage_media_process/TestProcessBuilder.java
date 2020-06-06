package com.xuecheng.manage_media_process;

import com.xuecheng.framework.utils.Mp4VideoUtil;
import org.hibernate.dialect.Teradata14Dialect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {

    //使用processBuilder唉调用第三方程序
    @Test
    public void testProcessBuilder() throws IOException {
        //创建ProcessBuilder对象
        ProcessBuilder processBuilder =new ProcessBuilder();
        //设置第三方程序的命令
        processBuilder.command("ping","127.0.0.1");
        //将标准输入流和错误流合并
        processBuilder.redirectErrorStream(true);
        //启动一个进程
        Process start = processBuilder.start();
        //通过标准输入流来拿到正常和错误的输出信息
        InputStream inputStream = start.getInputStream();
        //转为字符流
        InputStreamReader inputStreamReader=new InputStreamReader(new BufferedInputStream(inputStream),"gbk");
        char[] c=new char[1024];
        int len=-1;
        StringBuffer stringBuffer=new StringBuffer();
        while((len=inputStreamReader.read(c))!=-1)
        {
            String s=new String(c,0,len);
            stringBuffer.append(s);
        }
        System.out.println(stringBuffer);
        inputStreamReader.close();
        inputStream.close();
    }

    //
    @Test
    public void testFFmpeg() throws IOException {
        //创建ProcessBuilder对象
        ProcessBuilder processBuilder =new ProcessBuilder();
        //设置第三方程序的命令
        List<String> commands=new ArrayList<String>();
        commands.add("E:\\cms_study(2020329)\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe");
        commands.add("-i");
        commands.add("E:\\cloud实战\\test\\test1.avi");
        commands.add("-y");
        commands.add("-c:v");
        commands.add("libx264");
        commands.add("-s");
        commands.add("1364x768");
        commands.add("-pix_fmt");
        commands.add("yuv420p");
        commands.add("-b:a");
        commands.add("128k");
        commands.add("-b:v");
        commands.add("451k");
        commands.add("-r");
        commands.add("5");
        commands.add("E:\\cloud实战\\test\\javatest\\1.mp4");
        processBuilder.command(commands);
        //将标准输入流和错误流合并
        processBuilder.redirectErrorStream(true);
        //启动一个进程
        Process start = processBuilder.start();
        //通过标准输入流来拿到正常和错误的输出信息
        InputStream inputStream = start.getInputStream();
        //转为字符流
        InputStreamReader inputStreamReader=new InputStreamReader(new BufferedInputStream(inputStream),"gbk");
        char[] c=new char[1024];
        int len=-1;
        StringBuffer stringBuffer=new StringBuffer();
        while((len=inputStreamReader.read(c))!=-1)
        {
            String s=new String(c,0,len);
            stringBuffer.append(s);
        }
        System.out.println(stringBuffer);
        inputStreamReader.close();
        inputStream.close();
    }

}
