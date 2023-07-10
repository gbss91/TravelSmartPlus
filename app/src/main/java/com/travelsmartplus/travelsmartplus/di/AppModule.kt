package com.travelsmartplus.travelsmartplus.di

import android.content.Context
import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.travelsmartplus.travelsmartplus.data.network.AuthInterceptor
import com.travelsmartplus.travelsmartplus.data.network.Endpoints.BASE_URL
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.data.services.AuthServiceImpl
import com.travelsmartplus.travelsmartplus.data.services.BookingService
import com.travelsmartplus.travelsmartplus.data.services.BookingServiceImpl
import com.travelsmartplus.travelsmartplus.data.services.UserService
import com.travelsmartplus.travelsmartplus.data.services.UserServiceImpl
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.utils.SessionManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //------------------ UTILS ------------------//

    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("session", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSessionManager(sharedPreferences: SharedPreferences): SessionManager {
        return SessionManagerImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    //------------------ SERVICES ------------------//

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit, sessionManager: SessionManager): AuthService {
        val authService = retrofit.create(AuthService::class.java)
        return AuthServiceImpl(authService, sessionManager)
    }

    @Provides
    @Singleton
    fun provideBookingService(retrofit: Retrofit): BookingService {
        val bookingService = retrofit.create(BookingService::class.java)
        return BookingServiceImpl(bookingService)
    }


    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit, sessionManager: SessionManager): UserService {
        val userService = retrofit.create(UserService::class.java)
        return UserServiceImpl(userService, sessionManager)
    }


}