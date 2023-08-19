package com.atakanmadanoglu.movieapp.di

import android.content.Context
import androidx.room.Room
import com.atakanmadanoglu.movieapp.BuildConfig
import com.atakanmadanoglu.movieapp.data.mapper.MovieEntityMapper
import com.atakanmadanoglu.movieapp.data.mapper.MovieUiMapper
import com.atakanmadanoglu.movieapp.data.repository.MovieRepository
import com.atakanmadanoglu.movieapp.data.service.local.MovieDao
import com.atakanmadanoglu.movieapp.data.service.local.MovieDatabase
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import com.atakanmadanoglu.movieapp.presentation.MovieViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val AUTHORIZATION_HEADER = "Bearer ${BuildConfig.API_KEY}"
private const val ACCEPT_HEADER = "application/json"

val serviceModule = module {
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("accept", ACCEPT_HEADER)
                .addHeader("Authorization", AUTHORIZATION_HEADER)
                .build()
            chain.proceed(request)
        }.build()

    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun provideMovieService(
        retrofit: Retrofit
    ): MovieService = retrofit.create(MovieService::class.java)

    single { provideHttpLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideMovieService(get()) }
}

val databaseModule = module {
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase = Room.databaseBuilder(
        context, MovieDatabase::class.java, MovieDatabase.DB_NAME
    ).build()

    fun provideMovieDao(
        database: MovieDatabase
    ): MovieDao = database.movieDao

    single { provideMovieDatabase(get()) }
    single { provideMovieDao(get()) }
}

val viewModelModule = module {
    viewModel {
        MovieViewModel(get())
    }
}

val dataModule = module {
    single {
        MovieRepository(get(), get(), get(), get())
    }
}

val mapperModule = module {
    single {
        MovieUiMapper()
    }
    single {
        MovieEntityMapper()
    }
}