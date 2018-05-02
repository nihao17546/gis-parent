package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.user.web.result.JsonResult;

/**
 * Created by nihao on 17/12/15.
 */
public class BaseController {
    protected JsonResult fail(String message){
        return JsonResult.fail(message);
    }
    protected JsonResult ok(String message){
        return JsonResult.success(message);
    }
    protected JsonResult fail(){
        return JsonResult.fail();
    }
    protected JsonResult ok(){
        return JsonResult.success();
    }
}
