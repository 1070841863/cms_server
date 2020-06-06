package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;

/**
 * @author study
 * @create 2020-04-19 17:41
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestFile {


    //测试文件文件分块
    @Test
    public void testChunk() throws Exception {
        //源文件
        File f=new File("E:\\cloud实战\\test\\test1.avi");
        //块文件目录
        String chunkFileFolder="E:\\cloud实战\\test\\chunks\\";
        //先定义块文件的大小
        long chunkFileSize=1*1024*1024;
        //求出块数
        long chunkNum= (long) Math.ceil(f.length()*1.0/chunkFileSize);
        //创建读文件的对象
        RandomAccessFile f_read=new RandomAccessFile(f,"r");
        //缓冲区
        byte[] b=new byte[1024];
        for(int i=0;i<chunkNum;i++){
            //块文件
            File file=new File(chunkFileFolder+i);
            //创建向块文件的写对象
            RandomAccessFile f_write=new RandomAccessFile(file,"rw");
            int len=-1;
            while((len=f_read.read(b))!=-1){
                f_write.write(b,0,len);
                //如果块文件的大小达到1m，开始写下一块
                if(file.length()>=chunkFileSize){
                    break;
                }
            }
            f_write.close();
        }
        f_read.close();
    }


    //测试文件合并
    @Test
    public void testMergeFile() throws IOException {
        //块文件目录
        String chunckFolderPath="E:\\cloud实战\\test\\chunks\\";
        //块文件目录对象
        File file=new File(chunckFolderPath);
        //块文件列表
        File[] files = file.listFiles();
        //将块文件排序，按名称排序
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName())){
                    //升序
                    return 1;
                }
                //降序
                return -1;
            }
        });

        //合并文件
        File mergeFile=new File("E:\\cloud实战\\test\\res\\t.avi");
        //创建新文件
        boolean newFile=mergeFile.createNewFile();
        //创建写文件
        RandomAccessFile r_write=new RandomAccessFile(mergeFile,"rw");
        byte[] b=new byte[1024];
        for (File file1 : fileList) {
            //创建读文件的对象
            RandomAccessFile r_read=new RandomAccessFile(file1,"r");
            int len=-1;
            while((len=r_read.read(b))!=-1){
                r_write.write(b,0,len);
            }
            r_read.close();
        }
        r_write.close();
    }
}
