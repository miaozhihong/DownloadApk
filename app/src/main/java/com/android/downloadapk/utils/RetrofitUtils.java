package com.android.downloadapk.utils;

import com.android.downloadapk.ApiService;
import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author :created by mzh
 * time :2019年3月6日12:22:15
 * 描述：retrofit工具类
 */
public class RetrofitUtils {
    //封装Retrofit  设置拦截器
    static Retrofit createRetofiter() {
        return new Retrofit.Builder().baseUrl("http://qq-1259512018.cos.ap-chengdu.myqcloud.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                try {
                                    Request request = chain.request();
                                    Buffer buffer = new Buffer();
                                    request.body().writeTo(buffer);
                                    Response response = chain.proceed(request);
                                    byte[] resp = response.body().bytes();
                                        LogUtils.i(request.url(), "请求---" + buffer.readUtf8(), "响应---" + new String(resp));
                                    return response.newBuilder().body(ResponseBody.create(MediaType.parse(""), new String(resp))).build();
                                } catch (Exception e) {
                                    return chain.proceed(chain.request());
                                }
                            }
                        }).build()).build();
    }

    public static ApiService instanceApi() {
        return createRetofiter().create(ApiService.class);
    }
}
