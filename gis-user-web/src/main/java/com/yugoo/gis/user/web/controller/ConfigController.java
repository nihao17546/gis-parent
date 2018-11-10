package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.user.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao 2018/11/1
 */
@RestController
@RequestMapping("/config")
public class ConfigController extends BaseController {
    @Autowired
    private IConfigService configService;

    @RequestMapping("/cu")
    public String cu(@RequestParam Integer mapSearchRegion,
                     @RequestParam Integer expirationDateLimit) {
        configService.createOrEdit(mapSearchRegion, expirationDateLimit);
        return ok().json();
    }

    @RequestMapping("/info")
    public String info() {
        return ok().pull("info", configService.get()).json();
    }
}
