package com.appunite.example.debugutilsexample.dagger;

import android.app.Application;
import android.content.Context;

import com.appunite.example.debugutilsexample.App;
import com.appunite.example.debugutilsexample.BuildConfig;
import com.appunite.example.debugutilsexample.service.GitHubService;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public final class AppModule {

    private static final String TAG = AppModule.class.getCanonicalName();

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    @ForApplication
    public Context activityContext() {
        return app.getApplicationContext();
    }


    @Provides
    @Singleton
    Picasso providePicasso(@ForApplication Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .loggingEnabled(BuildConfig.DEBUG)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }


    @Provides
    @Singleton
    GitHubService provideRestAdapterBuilder() {

        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(GitHubService.class);
    }

    @Provides
    @Singleton
    File provideCacheDirectory(@ForApplication Context context) {
        return context.getCacheDir();
    }

    @Provides
    @Singleton
    Locale provideLocale() {
        return Locale.getDefault();
    }


}
