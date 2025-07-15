package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile  file) {
        log.info("文件上传：{}", file);
        /**
         * 获取文件原始名称
         * 截取原始文件名后缀名
         * 构造新文件名称：随机数 + 文件后缀名
         * 构造文件请求路径并返回
         */
        String upload;
        try {
            String originalFilename = file.getOriginalFilename();
            String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
            String name = UUID.randomUUID().toString() + extention;
            upload = aliOssUtil.upload(file.getBytes(), name);
        } catch (IOException e) {
            log.info("文件上传失败：{}", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        return Result.success(upload);
    }
}
