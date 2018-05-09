package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.dao.ResourceDAO;
import com.yugoo.gis.user.service.IResourceService;
import com.yugoo.gis.user.service.cache.CacheManager;
import com.yugoo.gis.user.service.cache.CacheManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nihao on 18/5/8.
 */
@Service
public class ResourceServiceImpl implements IResourceService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ResourceDAO resourceDAO;

    private CacheManager<Integer,List<String>> resourcePathCache = CacheManager.build(1L, TimeUnit.HOURS);
    {
        CacheManagerUtils.add("resource" ,resourcePathCache);
    }

    @Override
    public List<String> getPathsByRoleId(Integer roleId) {
        List<String> list = resourcePathCache.getIfPresent(roleId);
        if(list == null){
            list = resourceDAO.selectPathByRoleId(roleId);
            resourcePathCache.set(roleId, list);
        }
        return list;
    }
}
