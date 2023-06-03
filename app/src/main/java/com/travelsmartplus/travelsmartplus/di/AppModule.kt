package com.travelsmartplus.travelsmartplus.di

import android.content.Context
import android.content.SharedPreferences
import com.travelsmartplus.travelsmartplus.data.network.AuthInterceptor
import com.travelsmartplus.travelsmartplus.data.network.HttpRoutes.BASE_URL
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.data.services.AuthServiceImpl
import com.travelsmartplus.travelsmartplus.data.services.UserService
import com.travelsmartplus.travelsmartplus.data.services.UserServiceImpl
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.utils.SessionManagerImpl
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit, sessionManager: SessionManager): AuthService {
        val authService = retrofit.create(AuthService::class.java)
        return AuthServiceImpl(authService, sessionManager)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit, sessionManager: SessionManager): UserService {
        val userService = retrofit.create(UserService::class.java)
        return UserServiceImpl(userService, sessionManager)
    }

    @Provides
    @Singleton
    fun provideSetupViewModel(authService: AuthService, userService: UserService, sessionManager: SessionManager
    ): SetupViewModel {
        return SetupViewModel(authService, userService, sessionManager)
    }

}