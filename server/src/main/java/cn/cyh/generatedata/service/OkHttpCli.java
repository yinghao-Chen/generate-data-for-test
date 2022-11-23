package cn.cyh.generatedata.service;

import cn.cyh.generatedata.utils.SSLSocketClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OkHttpCli {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    private OkHttpClient okHttpClient;

    public OkHttpCli() {
        okHttpClient = new OkHttpClient().newBuilder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * get 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头集合
     * @return string
     */
    public String doGet(String url, Map<String, Object> params, Map<String, String> headers) {
        Request request = getUrlParamRequest(url, params, headers, "get");
        return execute(request);
    }

    /**
     * delete 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头集合
     * @return string
     */
    public String doDelete(String url, Map<String, Object> params, Map<String, String> headers) {
        Request request = getUrlParamRequest(url, params, headers, "delete");
        return execute(request);
    }

    /**
     * 获取URL传参请求体对象
     *
     * @param url         请求地址
     * @param params      请求参数集合
     * @param headers     请求头集合
     * @param requestType 请求类型：Get Delete
     * @return Request
     */
    private Request getUrlParamRequest(String url, Map<String, Object> params, Map<String, String> headers, String requestType) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (params.get(key) == null) {
                    continue;
                }
                String param = String.valueOf(params.get(key));
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(param);
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(param);
                }
            }
        }

        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }
        log.debug("do get request and url[{}]", sb.toString());
        if ("delete".equals(requestType.toLowerCase())) {
            return builder.url(sb.toString()).delete().build();
        } else {
            return builder.url(sb.toString()).get().build();
        }
    }

    /**
     * post 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        log.debug("do post request and url[{}]", url);

        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头集合
     * @return string
     */
    public String doPost(String url, Map<String, Object> params, Map<String, String> headers) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, String.valueOf(params.get(key)));
            }
        }

        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key));
            }
        }

        Request request = requestBuilder.url(url).post(builder.build()).build();
        log.debug("do post request and url[{}]", url);

        return execute(request);
    }

    /**
     * 上传文件
     * @param url     请求url地址
     * @param params  上传文件参数
     * @param headers 请求头
     * @return str
     */
    public String doMultipartPost(String url, Map<String, byte[]> files, Map<String, String> params, Map<String, String> headers) {
        MultipartBody.Builder paramBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (files != null && files.keySet().size() > 0) {
            for (String key : files.keySet()) {
                paramBuilder.addFormDataPart(key, key + ".jpg",
                        RequestBody.create(MediaType.parse("multipart/form-data"), params.get(key)));
            }
        }
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                paramBuilder.addFormDataPart(key, params.get(key));
            }
        }
        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder.url(url).post(paramBuilder.build()).build();
        return execute(request);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        log.debug("do post request and url[{}]", url);
        return executePost(url, json, JSON, null);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url     请求url地址
     * @param json    请求数据, json 字符串
     * @param headers 请求头集合
     * @return string
     */
    public String doPostJson(String url, String json, Map<String, String> headers) {
        log.debug("do post request and url[{}]", url);
        return executePost(url, json, JSON, headers);
    }

    /**
     * delete 请求, 请求数据为 json 的字符串
     *
     * @param url     请求url地址
     * @param json    请求数据, json 字符串
     * @param headers 请求头集合
     * @return string
     */
    public String doDeleteJson(String url, String json, Map<String, String> headers) {
        log.debug("do post request and url[{}]", url);
        return executeDelete(url, json, JSON, headers);
    }

    /**
     * put 请求, 请求数据为 json 的字符串
     *
     * @param url     请求url地址
     * @param json    请求数据, json 字符串
     * @param headers 请求头集合
     * @return string
     */
    public String doPutJson(String url, String json, Map<String, String> headers) {
        log.debug("do post request and url[{}]", url);
        return executePut(url, json, JSON, headers);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        log.debug("do post request and url[{}]", url);
        return executePost(url, xml, XML, null);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url     请求url地址
     * @param xml     请求数据, xml 字符串
     * @param headers 请求头集合
     * @return string
     */
    public String doPostXml(String url, String xml, Map<String, String> headers) {
        log.debug("do post request and url[{}]", url);
        return executePost(url, xml, XML, headers);
    }

    private String executePost(String url, String data, MediaType contentType, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request.Builder buider = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                buider.addHeader(key, headers.get(key));
            }
        }
        Request request = buider.url(url).post(requestBody).build();
        return execute(request);
    }

    private String executeDelete(String url, String data, MediaType contentType, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request.Builder buider = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                buider.addHeader(key, headers.get(key));
            }
        }
        Request request = buider.url(url).delete(requestBody).build();
        return execute(request);
    }

    private String executePut(String url, String data, MediaType contentType, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request.Builder buider = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                buider.addHeader(key, headers.get(key));
            }
        }
        Request request = buider.url(url).put(requestBody).build();
        return execute(request);
    }

    private String execute(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("#execute()", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

}
