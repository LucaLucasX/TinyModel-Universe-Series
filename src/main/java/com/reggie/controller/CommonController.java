package com.reggie.controller;

import com.reggie.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("E:\\BaiduNetdiskDownload\\1 瑞吉外卖项目\\资料\\图片资源\\")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //file临时文件
        log.info(file.toString());
        String originalFilename=file.getOriginalFilename();
        String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid重新生成文件名防止重复
        String filename= UUID.randomUUID() +suffix;
        File dir=new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //没有就创建
            dir.mkdirs();
        }
        file.transferTo(new File(basePath+filename));
        return R.success(filename);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //输入流，读取文件内容
        FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
        //输出流，写会浏览器
        ServletOutputStream outputStream=response.getOutputStream();
        int len=0;
        byte[]bytes=new byte[1024];
        while((len=fileInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        //关闭资源
        outputStream.close();
        fileInputStream.close();
    }
}
 