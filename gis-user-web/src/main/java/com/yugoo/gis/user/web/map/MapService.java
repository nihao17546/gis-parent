package com.yugoo.gis.user.web.map;

import com.alibaba.fastjson.JSON;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.common.utils.HttpClientUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author nihao 2018/10/26
 */
@Component
public class MapService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String URL = "http://api.map.baidu.com/place/v2/search?" +
            "query={0}" +
            "&bounds={1}" +
            "&output=json" +
            "&ak=GTd8iA2429tSYGH5DC1kmEOO9ma61UvE";

    public List<MapSearchResult> search(String query, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        String url = MessageFormat.format(URL, query, new StringBuilder().append(minLatitude).append(",")
                .append(minLongitude).append(",").append(maxLatitude).append(",").append(maxLongitude).toString());
        CloseableHttpResponse httpResponse = null;
        HttpEntity entity = null;
        try{
            CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpClientUtils.config(httpGet);
            httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() != 200){
                throw new GisRuntimeException("百度搜索失败,响应码:" + httpResponse.getStatusLine().getStatusCode());
            }
            entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            MapSearchResponse response = null;
            try {
                response = JSON.parseObject(result, MapSearchResponse.class);
            }catch (Exception e) {
                throw new GisRuntimeException("百度搜索字符串解析失败,响应结果:" + result);
            }
            if (response.getStatus() != 0) {
                throw new GisRuntimeException("百度搜索失败,status: " + response.getStatus() + ",message: " + response.getMessage());
            }
            return response.getResults();
        }catch (Exception e){
            logger.error("百度地图检索失败,请求URL: {}", url, e);
            throw new GisRuntimeException(e.getMessage());
        }finally {
            if(httpResponse != null){
                try {
                    httpResponse.close();
                } catch (Exception e) {
                    logger.error("close CloseableHttpResponse error", e);
                }
            }
            if(entity != null){
                try {
                    EntityUtils.consume(entity);
                } catch (Exception e) {
                    logger.error("close HttpEntity error", e);
                }
            }
        }
    }
}
