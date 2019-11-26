package com.simon.margaret.net.interceptors;

import android.support.annotation.NonNull;

import com.simon.margaret.app.Margaret;
import com.simon.margaret.app.UserInfo;
import com.simon.margaret.util.storage.MargaretPreference;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by simon
 */

public final class AddCookieInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        Observable
                .just(MargaretPreference.getCustomAppProfile("cookie"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String cookie) throws Exception {
                        //给原生API请求附带上WebView拦截下来的Cookie
                        UserInfo userInfo = Margaret.getCurrentUser();
                        String token = userInfo.getToken();
                        builder.addHeader("token", token);
                        builder.addHeader("content-type", "application/json");
                    }
                });
        return chain.proceed(builder.build());
    }
}
