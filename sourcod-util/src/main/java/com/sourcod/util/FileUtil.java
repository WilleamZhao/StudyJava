package com.sourcod.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件工具类
 * Created by sourcod on 2017/11/24.
 */
public class FileUtil {

    /**
     * 下载文件
     * @param filePath  文件地址
     * @param tempFilePath  下载的路径
     * @return 文件地址
     */
    public static String downloadFile(String filePath, String tempFilePath) throws IOException {
        //String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // 临时目录
        String dir = tempFilePath + DateUtils.getDate();
        File savePath = new File(dir);//创建新文件
        if (!savePath.exists()) {
            savePath.mkdir();
        }
        String[] urlname = filePath.split("/");
        int len = urlname.length - 1;
        String uname = urlname[len];//获取文件名
        File file = new File(savePath + "//" + uname);//创建新文件
        if (file != null && !file.exists()) {
            file.createNewFile();
        }
        OutputStream oputstream = new FileOutputStream(file);
        URL url = new URL(filePath);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
        uc.connect();
        InputStream iputstream = uc.getInputStream();
        System.out.println("file size is:" + uc.getContentLength());//打印文件长度
        byte[] buffer = new byte[4 * 1024];
        int byteRead = -1;
        while ((byteRead = (iputstream.read(buffer))) != -1) {
            oputstream.write(buffer, 0, byteRead);
        }
        oputstream.flush();
        iputstream.close();
        oputstream.close();
        return savePath + "//" + uname;
        /*//读取文件
        StringBuffer strb = new StringBuffer();
        FileInputStream fs = new FileInputStream(new File(savePath+"//"+uname));
        InputStreamReader isr = new InputStreamReader(fs,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String data = "";
        while((data = br.readLine()) != null){
            strb.append(data + "\n");
        }
        br.close();
        fs.close();
        isr.close();*/
        //解压
        // ZipUtil.unZipFiles(dir + "/lua.zip", dir + "/");
    }

    /**
     * 远程文件转inputStream
     * @param url 远程文件地址
     * @return 输入流
     * @throws IOException
     */
    public static InputStream urlToInputStream(String url) throws IOException {
        URL fileUrl = new URL(url);
        URLConnection connection = fileUrl.openConnection();
        InputStream is =  connection.getInputStream();
        return is;
    }

    /**
     * 获取文件名带后缀
     * @param filePath  文件路径
     * @return
     */
    public static String getFileName(String filePath){
        String[] urlname = filePath.split("/");
        int len = urlname.length - 1;
        String uname = urlname[len];//获取文件名
        return uname;
    }

    /**
     * 获取文件名不带后缀
     * @param filePath  文件路径
     * @return
     */
    public static String getFileNameNoSuffix(String filePath){
        String[] urlname = filePath.split("/");
        int len = urlname.length - 1;
        String uname = urlname[len];//获取文件名
        String str = uname.substring(0,uname.lastIndexOf("."));
        return str;
    }
}
