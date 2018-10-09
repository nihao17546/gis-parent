package com.yugoo.gis.common.constant;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nihao on 18/5/8.
 */
public class StaticConstant {
    public static final String cookieName = "gis&_token";
    public static final List<String> JUST_NEED_LOGIN_PATH = Arrays.asList(
            "/user/update",
            "/user/updatePassword",
            "/user/ownInfo",
            "/role/all",
            "/group/all",
            "/center/all",
            "/street/all",
            "/upload/pic",
            "/street/types",
            "/index.html",
            "/own.html");
    public static final List<String> MEMBER_PATH = Arrays.asList(
            "/consumer/create",
            "/consumer.html",
            "/street.html",
            "/street/list",
            "/street/create",
            "/street/edit",
            "/street/info",
            "/street/listByCenter",
            "/building.html",
            "/building/list",
            "/building/create",
            "/building/edit"
    );
    public static final List<String> HEADMAN_PATH = Arrays.asList(
            "/group.html",
            "/group/list",
            "/group/edit",
            "/group/info",
            "/center.html",
            "/center/list",
            "/center/create",
            "/center/edit"
    );
    public static final List<String> ADMIN_PATH = Arrays.asList(
            "/user/edit",
            "/user/info",
            "/user/list",
            "/user/create",
            "/user/delete",
            "/group/create",
            "/group/delete",
            "/center/delete",
            "/street/delete",
            "/building/delete",
            "/user.html");
    public static List<String> getPathByRole(Integer roleId) {
        Role role = Role.getByValue(roleId);
        List<String> list = Lists.newArrayList(JUST_NEED_LOGIN_PATH);
        if (role == Role.member) {
            list.addAll(MEMBER_PATH);
        }
        else if (role == Role.headman) {
            list.addAll(MEMBER_PATH);
            list.addAll(HEADMAN_PATH);
        }
        else if (role == Role.admin) {
            list.addAll(MEMBER_PATH);
            list.addAll(HEADMAN_PATH);
            list.addAll(ADMIN_PATH);
        }
        return list;
    }
}
