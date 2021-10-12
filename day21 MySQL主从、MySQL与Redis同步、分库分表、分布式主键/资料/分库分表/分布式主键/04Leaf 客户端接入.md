Leaf 客户端接入

# 概述

当然，为了追求更高的性能，需要通过 RPC Server 来部署 Leaf 服务，那仅需要引入 leaf-core 的包，把生成 ID 的 API 封装到指定的 RPC 框架中即可。我们这里先使用 OkHttp3 框架快速接入以便演示完整操作流程

# 什么是 OkHttp

OKHttp 是一个当前主流的网络请求的开源框架，由 Square 公司开发，用于替代 **HttpUrlConnection** 和 **Apache HttpClient**

# OkHttp 特性

- 支持 HTTP2，对一台机器的所有请求共享同一个 Socket
- 内置连接池，支持连接复用，减少延迟
- 支持透明的 gzip 压缩响应体
- 通过缓存避免重复的请求
- 请求失败时自动重试主机的其他 IP，自动重定向

# OkHttp 功能

- PUT，DELETE，POST，GET 等请求
- 文件的上传下载
- 加载图片 (内部会图片大小自动压缩)
- 支持请求回调，直接返回对象、对象集合
- 支持 Session 的保持

# OkHttp 依赖

```
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.2.2</version>
</dependency>
```

# 封装 OkHttp 工具类

创建一个名为 `OkHttpClientUtil` 的工具类，代码如下

```
package com.funtl.spring.cloud.alibaba.commons.net;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OKHttp3
 * <p>
 * Description:
 * </p>
 */
public class OkHttpClientUtil {
    private static final int READ_TIMEOUT = 100;
    private static final int CONNECT_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final byte[] LOCKER = new byte[0];
    private static OkHttpClientUtil mInstance;
    private OkHttpClient okHttpClient;

    private OkHttpClientUtil() {
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        // 读取超时
        clientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        // 连接超时
        clientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        //写入超时
        clientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient = clientBuilder.build();
    }

    /**
     * 单例模式获取 NetUtils
     *
     * @return {@link OkHttpClientUtil}
     */
    public static OkHttpClientUtil getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * GET，同步方式，获取网络数据
     *
     * @param url 请求地址
     * @return {@link Response}
     */
    public Response getData(String url) {
        // 构造 Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * POST 请求，同步方式，提交数据
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     * @return {@link Response}
     */
    public Response postData(String url, Map<String, String> bodyParams) {
        // 构造 RequestBody
        RequestBody body = setRequestBody(bodyParams);
        // 构造 Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * GET 请求，异步方式，获取网络数据
     *
     * @param url       请求地址
     * @param myNetCall 回调函数
     */
    public void getDataAsync(String url, final MyNetCall myNetCall) {
        // 构造 Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);
            }
        });
    }

    /**
     * POST 请求，异步方式，提交数据
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     * @param myNetCall  回调函数
     */
    public void postDataAsync(String url, Map<String, String> bodyParams, final MyNetCall myNetCall) {
        // 构造 RequestBody
        RequestBody body = setRequestBody(bodyParams);
        // 构造 Request
        buildRequest(url, myNetCall, body);
    }

    /**
     * 同步 POST 请求，使用 JSON 格式作为参数
     *
     * @param url  请求地址
     * @param json JSON 格式参数
     * @return 响应结果
     * @throws IOException 异常
     */
    public String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 异步 POST 请求，使用 JSON 格式作为参数
     *
     * @param url       请求地址
     * @param json      JSON 格式参数
     * @param myNetCall 回调函数
     * @throws IOException 异常
     */
    public void postJsonAsync(String url, String json, final MyNetCall myNetCall) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        // 构造 Request
        buildRequest(url, myNetCall, body);
    }

    /**
     * 构造 POST 请求参数
     *
     * @param bodyParams 请求参数
     * @return {@link RequestBody}
     */
    private RequestBody setRequestBody(Map<String, String> bodyParams) {
        RequestBody body = null;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, bodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }

    /**
     * 构造 Request 发起异步请求
     *
     * @param url       请求地址
     * @param myNetCall 回调函数
     * @param body      {@link RequestBody}
     */
    private void buildRequest(String url, MyNetCall myNetCall, RequestBody body) {
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);
            }
        });
    }

    /**
     * 自定义网络回调接口
     */
    public interface MyNetCall {
        /**
         * 请求成功的回调处理
         *
         * @param call     {@link Call}
         * @param response {@link Response}
         * @throws IOException 异常
         */
        void success(Call call, Response response) throws IOException;

        /**
         * 请求失败的回调处理
         *
         * @param call {@link Call}
         * @param e    异常
         */
        void failed(Call call, IOException e);
    }

}
```

# 封装获取 Id 的工具类

创建一个名为 `LeafSnowflakeId` 的工具类，专门用于获取 leaf-snowflake 生成的 ID

```
package com.funtl.spring.cloud.alibaba.commons.id;

import com.funtl.spring.cloud.alibaba.commons.net.OkHttpClientUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

/**
 * 通过 Leaf 获取雪花 ID
 * <p>
 * Description:
 * </p>
 */
public class LeafSnowflakeId {

    /**
     * 注意我这里写死了获取 Leaf 地址，只是为了方便演示
     */
    private static final String LEAF_HOST = "http://192.168.2.121:18080/api/snowflake/get/id";

    /**
     * 生成 ID
     *
     * @return {@code Long} 雪花 ID
     */
    public static Long genId() {
        try {
            String string = Objects.requireNonNull(OkHttpClientUtil.getInstance().getData(LEAF_HOST).body()).string();
            return Long.valueOf(string);
        } catch (IOException e) {
            return 0L;
        }
    }

    /**
     * 测试
     *
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        for (int i = 0; i < 100; i++) {
            System.out.println(LeafSnowflakeId.genId());
        }
    }

}
```