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
            "/consumer/serviceTypes",
            "/index.html",
            "/own.html",
            "/welcome.html");
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
            "/building/edit",
            "/building/listOwn",
            "/consumer.html",
            "/consumer/create",
            "/consumer/list",
            "/consumer/export",
            "/consumer/edit",
            "/consumer/info",
            "/resource.html",
            "/resource/create",
            "/resource/list",
            "/resource/import",
            "/resource/export",
            "/resource/edit",
            "/center/mapSearch",
            "/building/mapSearch",
            "/map/search",
            "/config/info",
            "/statistic/consumer",
            "/statistic/export/consumer",
            "/statisticConsumer.html",
            "/consumerInfo.html",
            "/consumerInfo/list",
            "/consumerInfo/export"
    );
    public static final List<String> HEADMAN_PATH = Arrays.asList(
            "/group.html",
            "/group/list",
            "/group/edit",
            "/group/info",
            "/center.html",
            "/center/list",
            "/center/create",
            "/center/edit",
            "/user/searchSubordinates",
            "/statistic/center",
            "/statistic/export/center",
            "/statisticCenter.html",
            "/statistic/user",
            "/statistic/export/user",
            "/statisticUser.html"
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
            "/consumer/delete",
            "/resource/delete",
            "/user.html",
            "/config/cu",
            "/config.html",
            "/consumer/import",
            "/notice.html",
            "/noticeIframe.html",
            "/notice/create",
            "/notice/edit",
            "/notice/delete",
            "/notice/list",
            "/notice/info",
            "/learn.html",
            "/learnIframe.html",
            "/learn/create",
            "/learn/edit",
            "/learn/delete",
            "/learn/list",
            "/learn/info",
            "/consumerInfo/delete");
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
