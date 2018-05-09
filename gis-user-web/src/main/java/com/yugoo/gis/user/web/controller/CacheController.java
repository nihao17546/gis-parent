package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.user.service.cache.CacheManagerUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by nihao on 18/5/9.
 */
@RestController
@RequestMapping("/cache")
public class CacheController extends BaseController {

    /**
     * 手动清缓存
     * @param key
     * @return
     */
    @RequestMapping("/clear/{key}")
    public String clear(@PathVariable String key){
        if("all".equals(key)){
            CacheManagerUtils.invalid();
        }
        else{
            CacheManagerUtils.invalid(key);
        }
        return ok().json();
    }

}
