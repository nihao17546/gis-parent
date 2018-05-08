package com.yugoo.gis.user.service;

import java.util.List;

/**
 * Created by nihao on 18/5/8.
 */
public interface IResourceService {
    List<String> getPathsByRoleId(Integer roleId);
}
