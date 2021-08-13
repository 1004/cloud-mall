package com.xky.mall.cartorder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/12 5:54 下午
 */
@Component
public class OrderUploadFileConstants {

    /**
     * 图片缓存的后端目录，放到配置文件
     */
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setUploadFileDir(String uploadFileDir) {
        FILE_UPLOAD_DIR = uploadFileDir;
    }
}
