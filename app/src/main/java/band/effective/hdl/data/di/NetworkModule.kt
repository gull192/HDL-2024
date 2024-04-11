package band.effective.hdl.data.di

import band.effective.hdl.data.LeaderIdApi
import band.effective.hdl.data.auth.AppAuth
import band.effective.hdl.data.auth.AppAuthenticator
import band.effective.hdl.data.auth.AuthorizationInterceptor
import band.effective.hdl.domain.PreferencesRepository
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.addAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @OptIn(ExperimentalStdlibApi::class)
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addAdapter(Rfc3339DateJsonAdapter().nullSafe())
            .build()
    }

    @Provides
    @IntoSet
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @IntoSet
    internal fun provideAuthorizationInterceptor(
        preferences: PreferencesRepository
    ): Interceptor = AuthorizationInterceptor(preferences)

    @Provides
    internal fun provideAppAppAuthenticator(
        preferencesRepository: PreferencesRepository,
        appAuth: AppAuth
    ): Authenticator = AppAuthenticator(preferencesRepository, appAuth)

    @Provides
    fun provideBaseOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        authenticator: Authenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply { interceptors.forEach(::addInterceptor) }
            .authenticator(authenticator)
            .callTimeout(50, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideBaseRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://apps.leader-id.ru/api/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()
    }

    @Provides
    fun provideLeaderIdApi(retrofit: Retrofit) =
        retrofit.create(LeaderIdApi::class.java)
}