package com.openshop.gazapp.data.api;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit=null;
    private static String BASE_URL="https://gas.pro.opnshop.net/api/v1/";

    public static ApiServes getClient(){
//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                .tlsVersions(TlsVersion.TLS_1_1)
//                .cipherSuites(CipherSuite.TLS_DH_anon_WITH_AES_256_GCM_SHA384 ,
//
//                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256  ,
//                CipherSuite.TLS_DH_anon_WITH_AES_128_CBC_SHA
//                        ).build();


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
//                .connectionSpecs(Collections.singletonList(spec))
                .build();

        if (retrofit == null){
            retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiServes.class);
    }
}
