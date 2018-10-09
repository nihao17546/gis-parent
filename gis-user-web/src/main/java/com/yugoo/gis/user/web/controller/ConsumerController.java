package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.user.service.IConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author nihao 2018/10/9
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController extends BaseController {

    @Autowired
    private IConsumerService consumerService;

    @RequestMapping("/create")
    public String create(@RequestParam String name, @RequestParam Integer buildingId,
                         @RequestParam(required = false) String floor, @RequestParam(required = false) String position,
                         @RequestParam(required = false) String number, @RequestParam(required = false) String category,
                         @RequestParam(required = false) String nature, @RequestParam(required = false) Integer peopleNum,
                         @RequestParam(required = false) String status, @RequestParam(required = false) String legal,
                         @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         @Value("#{request.getAttribute('uid')}") Integer uid) throws IOException {
        byte[] pic = null;
        if (multipartFile != null) {
            pic = multipartFile.getBytes();
        }
        try {
            consumerService.create(name, buildingId, floor, position, number, category, nature, peopleNum, pic, status, legal);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

}
