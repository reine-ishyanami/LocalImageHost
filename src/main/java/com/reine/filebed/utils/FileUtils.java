package com.reine.filebed.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.reine.filebed.entity.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 文件操作工具类
 *
 * @author reine
 * @since 2022/4/30 15:40
 */
public class FileUtils {

    /**
     * 文件上传工具类
     *
     * @return 文件查询地址
     */
    public static Result upload(String project, MultipartFile file) {
        // 上传地址
        String url = "http://localhost:8824/upload/" + project;
        // 文件
        Map<String, MultipartFile> fileMap = new HashMap<>();
        fileMap.put("imgFile", file);
        String contentType = "";
        return formUpload(url, fileMap, contentType);
    }

    private static Result formUpload(String urlStr,
                                     Map<String, MultipartFile> fileMap, String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // file
            if (fileMap != null) {
                MultipartFile imgFile = fileMap.get("imgFile");
                // 获取初始文件名
                String filename = imgFile.getName();
                // 创建临时文件
                String name = filename.substring(0, filename.lastIndexOf("."));
                String prefix = filename.substring(filename.lastIndexOf("."));
                File tempFile = File.createTempFile(name, prefix);
                imgFile.transferTo(tempFile);
                String fileName = tempFile.getName();
                // 没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                contentType = new MimetypesFileTypeMap().getContentType(tempFile);
                // contentType非空采用filename匹配默认的图片类型
                if (!"".equals(contentType)) {
                    if (fileName.endsWith(".png")) {
                        contentType = "image/png";
                    } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpe")) {
                        contentType = "image/jpeg";
                    } else if (fileName.endsWith(".gif")) {
                        contentType = "image/gif";
                    } else if (fileName.endsWith(".ico")) {
                        contentType = "image/image/x-icon";
                    }
                }
                if (contentType == null || "".equals(contentType)) {
                    contentType = "application/octet-stream";
                }
                StringBuffer strBuf = new StringBuffer();
                strBuf.append("\r\n").append("--").append(BOUNDARY)
                        .append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\""
                        + "imgFile" + "\"; filename=\"" + fileName
                        + "\"\r\n");
                System.out.println("imgFile" + "," + fileName);

                strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                out.write(strBuf.toString().getBytes());
                DataInputStream in = new DataInputStream(
                        new FileInputStream(tempFile));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                in.close();
                deleteFile(tempFile);
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        Result result = JSONObject.parseObject(res, Result.class);
        return result;
    }

    public static Result delete(String projectAndFileName) {
        HttpURLConnection conn = null;
        try {
            String deleteURL = "http://localhost:8824/delete/" + projectAndFileName;
            URL url = new URL(deleteURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 执行删除操作
            conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(500, "删除失败", null);
        } finally {
            conn.disconnect();
        }
        return new Result(200, "删除成功", null);

    }

    /**
     * 删除临时文件
     *
     * @param file
     */
    private static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

}
