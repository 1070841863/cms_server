package com.pro;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author study
 * @create 2020-04-09 13:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {

    @Test
    public void testUpload(){
        try {
            ClientGlobal.initByProperties("conf/fastdfs-client.properties");
            System.out.println("network_time_out"+ClientGlobal.g_network_timeout+"ms");
            System.out.println("charset="+ClientGlobal.g_charset);
            //创建客户端
            TrackerClient tc=new TrackerClient();
            //连接tracker Server
            TrackerServer trackerServer = tc.getConnection();
            if(trackerServer==null){
                System.out.println("getConnection return null");
                return;
            }
            //获取storage储存客户端
            StorageServer ss = tc.getStoreStorage(trackerServer);
            if(ss==null){
                System.out.println("getStorage return null");
                return;
            }
            //创建一个storage储存客户端
            StorageClient1 sc1=new StorageClient1(trackerServer,ss);

            NameValuePair[] meta_list=null; //new NameValuePair[0]
            String item="D:\\img\\1.jpg";
            String fileid;
            fileid=sc1.upload_appender_file1(item,"jpg",meta_list);
            System.out.println("upload local file"+item+"\t ok, fileid="+fileid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void queryFile(){
        try {
            ClientGlobal.initByProperties("conf/fastdfs-client.properties");
            TrackerClient trackerClient=new TrackerClient();
            TrackerServer trackerServer=trackerClient.getConnection();
            StorageServer storageServer=null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            FileInfo fileInfo = storageClient.query_file_info("group1", "M00/00/00/wKh6Z16OuKKEaSibAAAAACZD9yc416.jpg");
            System.out.println(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void download(){
        try {
            ClientGlobal.initByProperties("conf/fastdfs-client.properties");
            TrackerClient trackerClient=new TrackerClient();
            TrackerServer trackerServer=trackerClient.getConnection();
            StorageServer storageServer=null;

            StorageClient1 storageClient1=new StorageClient1(trackerServer,storageServer);
            byte[] result=storageClient1.download_file1("group1/M00/00/00/wKh6Z16OuKKEaSibAAAAACZD9yc416.jpg");
            File f=new File("d:\\img\\3.jpg");
            FileOutputStream outputStream = new FileOutputStream(f);
            outputStream.write(result);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
