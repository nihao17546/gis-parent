package com.yugoo.gis.user.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author nihao
 * @create 2018/10/1
 **/
@RestController
@RequestMapping("/upload")
public class UploadController extends BaseController {

    @RequestMapping("/pic")
    public String pic(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        return ok().pull("data", bytes).json();
    }

}
