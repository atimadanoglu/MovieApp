package com.atakanmadanoglu.movieapp.di

import com.atakanmadanoglu.movieapp.BuildConfig
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val AUTHORIZATION_HEADER = "Bearer ${BuildConfig.API_KEY}"
    private const val ACCEPT_HEADER = "application/json"
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("accept", ACCEPT_HEADER)
                .addHeader("Authorization", AUTHORIZATION_HEADER)
                .build()
            chain.proceed(request)
        }.build()

    @Provides
    @Singleton
    fun getMovieService(
        retrofit: Retrofit
    ): MovieService = retrofit.create(MovieService::class.java)
}