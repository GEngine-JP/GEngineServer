package info.xiaomo.chessgame.log.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * post
     * @param url url
     * @param params params
     * @return String
     */
    public static String post(String url, Map<String, Object> params) {
        BasicHttpClientConnectionManager mgr = null;
        CloseableHttpClient client = null;
        try {
            mgr = new BasicHttpClientConnectionManager();
            client = HttpClients.custom().setConnectionManager(mgr).build();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).setConnectionRequestTimeout(10000).setRedirectsEnabled(false).build();
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            List<NameValuePair> formParams = new ArrayList<>();

            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    formParams.add(new BasicNameValuePair(key, String.valueOf(value)));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            post.setEntity(entity);
            CloseableHttpResponse ret = client.execute(post);
            HttpEntity response = ret.getEntity();
            return EntityUtils.toString(response, Consts.UTF_8);
        } catch (Exception e) {
            LOGGER.error("请求失败,url:" + url + ",params:" + params, e);
            return null;
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mgr != null) {
                mgr.close();
            }

        }

    }

    /**
     * get
     * @param url url
     * @param params params
     * @return String
     */
    public static String get(String url, Map<String, Object> params) {
        BasicHttpClientConnectionManager mgr = null;
        CloseableHttpClient client = null;
        try {
            mgr = new BasicHttpClientConnectionManager();
            client = HttpClients.custom().setConnectionManager(mgr).build();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000)
                    .setConnectTimeout(10000).setConnectionRequestTimeout(10000).setRedirectsEnabled(false).build();
            StringBuilder builder = new StringBuilder(url);
            builder.append("?");
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    builder.append(key);
                    builder.append("=");
                    builder.append(value);
                    builder.append("&");
                }
            }
            HttpGet get = new HttpGet(builder.toString());
            get.setConfig(requestConfig);
            CloseableHttpResponse ret = client.execute(get);
            HttpEntity response = ret.getEntity();
            return EntityUtils.toString(response, Consts.UTF_8);
        } catch (Exception e) {
            LOGGER.error("请求失败,url:" + url + ",params:" + params, e);
            return null;
        } finally {

            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mgr != null) {
                mgr.close();
            }

        }
    }

}
