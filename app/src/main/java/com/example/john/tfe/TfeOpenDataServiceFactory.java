package com.example.john.tfe;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class TfeOpenDataServiceFactory {

    public static TfeOpenDataService getRemoteService(String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tfe-opendata.com/api/v1")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(chain ->  {
                                    return chain.proceed(chain.request()
                                            .newBuilder()
                                            .addHeader("Authorization", "Token: " + token)
                                            .build());
                                }
                        )
                        .build())
                .build();
        return retrofit.create(TfeOpenDataService.class);
    }

    public static TfeOpenDataService getLocalService(Context ctx) {
        return new TfeOpenDataServiceLocal(ctx.getResources());
    }
}
