package com.example.demo12.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo12.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {
    // 文件上传路径
    private static final String filePath = System.getProperty("user.dir") + "/file/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        synchronized (FileController.class){
            // 防止多线程上传导致文件名冲突
            String flag = System.currentTimeMillis() + "";
            // 获取文件名
            String fileName = file.getOriginalFilename();
            try{
                // 创建文件夹
                if(!FileUtil.isDirectory(filePath)){
                    FileUtil.mkdir(filePath);
                }
                // 保存文件
                FileUtil.writeBytes(file.getBytes(), filePath + flag +'_'+ fileName);
                System.out.println(fileName + "上传成功！");
                Thread.sleep(1L);
            }catch (Exception e){
                System.err.println(fileName + "上传失败！");
            }
            return Result.success(flag);
        }
    }
    @GetMapping("/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response){
        if(!FileUtil.isDirectory(filePath)){
            FileUtil.mkdir(filePath);
        }
        // 下载文件
        OutputStream os  ;
        List<String> fileNames = FileUtil.listFileNames(filePath);
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try{
            if(StrUtil.isNotEmpty(avatar)){
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatar, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + avatar);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        }catch (Exception e){
            System.out.println("文件下载失败");
        }
    }
}
