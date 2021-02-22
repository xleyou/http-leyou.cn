package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    private static final List<String> CONTENT_TYPE = Arrays.asList("image/gif", "image/jpeg","image/jpg","image/png");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);
    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadImage(MultipartFile file) {
        //获取文件名
        String originalFilename = file.getOriginalFilename();

        //校验文件类型
        String contentType = file.getContentType();
        if (!CONTENT_TYPE.contains(contentType)) {
            LOGGER.info("文件类型不合法：{}", originalFilename);
            return null;
        }
        //校验文件内容
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容不合法：{}", originalFilename);
                return null;
            }

            //保存到服务器
            //file.transferTo(new File("F:\\leyou\\leyou\\img\\" + originalFilename));
            String ext=StringUtils.substringAfterLast(originalFilename,".");
            StorePath sto=this.storageClient.uploadFile(file.getInputStream(),file.getSize(),ext,null);

            //返回url，进行回显
            //return "http://image.leyou.com/" + originalFilename;
            return "http://image.leyou.com/"+sto.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误: " + originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
