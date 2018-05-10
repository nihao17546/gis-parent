package com.yugoo.gis.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nihao on 18/5/8.
 */
public class StaticConstant {
    public static final String cookieName = "gis&_token";
    public static final List<String> JUST_NEED_LOGIN = Arrays.asList(
            "/user/updateInfo",
            "/user/updatePassword",
            "/role/list");
}
