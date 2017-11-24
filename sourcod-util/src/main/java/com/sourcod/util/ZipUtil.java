package com.sourcod.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip工具类
 * Created by sourcod on 2017/11/24.
 */
public class ZipUtil {

    /**
     * 解压到指定目录
     * @param zipPath  zip文件路径
     * @param descDir  解压目录
     * @author sourcod
     */
    public static void unZipFiles(String zipPath, String descDir)throws IOException {
        unZipFiles(new File(zipPath), descDir);
    }

    /**
     * 解压到指定目录
     * @param zipFile zip文件
     * @param descDir 目录
     * @author sourcod
     * @throws IOException
     */
    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        System.out.println("******************开始解压********************");
        ZipFile zip = new ZipFile(zipFile, Charset.forName("UTF-8"));//解决中文文件夹乱码
        String name = zip.getName().substring(zip.getName().lastIndexOf('\\')+1, zip.getName().lastIndexOf('.'));

        File pathFile = new File(descDir+name);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + name +"/"+ zipEntryName).replaceAll("\\*", "/");

            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            // 输出文件路径信息
            System.out.println(outPath);
            FileOutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        System.out.println("******************解压完毕********************");
    }
}
